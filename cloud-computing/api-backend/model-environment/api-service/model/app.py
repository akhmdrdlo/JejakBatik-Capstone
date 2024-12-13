import os
import tempfile
import numpy as np
import tensorflow as tf
import io
from datetime import datetime
import pytz
import firebase_admin
from flask import Flask, request, jsonify
from tensorflow.keras.applications import efficientnet
from tensorflow.keras.utils import load_img, img_to_array
from tensorflow.keras.models import load_model
from PIL import Image
from firebase_admin import credentials, storage, firestore
from dictionary import data
from catalog import data_catalog

# Inisialisasi Flask app
app_flask = Flask(__name__)

# Koneksi dan Akses Firestore
cred = credentials.Certificate("credential.json")
firebase_admin.initialize_app(cred, {'storageBucket': 'jejakbatik-storage'})
db = firestore.client()

# Daftar class labels untuk hasil prediksi
class_labels = [
    "Aceh_Pintu_Aceh", "Bali_Barong", "Bali_Merak", "DKI_Ondel_Ondel", "Jawa_Barat_Megamendung",
    "Jawa_Tengah_Batik_Lasem", "Jawa_Timur_Pring", "Kalimantan_Barat_Insang", "Kalimantan_Dayak",
    "Lampung_Gajah", "Madura_Mataketeran", "Maluku_Pala", "NTB_Lumbung", "Palembang_Songket",
    "Papua_Asmat", "Papua_Cendrawasih", "Papua_Tifa", "Solo_Parang", "Solo_Sidoluhur", "Solo_Sogan",
    "Solo_Truntum", "Sulawesi_Selatan_Lontara", "Sumatera_Barat_Rumah_Minang", "Sumatera_Utara_Boraspati",
    "Yogyakarta_Kawung", "Yogyakarta_Sidomukti"
]

# Load model TensorFlow
model = load_model("model_testing_1.keras")

# Ekstensi data
ALLOWED_EXTENSIONS = {'jpg', 'jpeg', 'png'}

# Signature default untuk prediksi
MAX_IMAGE_SIZE = 2 * 1024 * 1024

# Fungsi cek ekstensi file
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

# Fungsi upload image ke Cloud Storage
def upload_blob(img, destination_blob_name):
    # Simpan gambar sementara dalam file
    with tempfile.NamedTemporaryFile(delete=False, suffix=".jpg") as temp_file:
        img.save(temp_file, format='JPEG')
        temp_file_path = temp_file.name

    try:
        # Mengunggah file sementara ke Firebase Storage
        bucket = storage.bucket()
        blob = bucket.blob(destination_blob_name)
        
        # Mengatur metadata tipe MIME
        blob.upload_from_filename(temp_file_path, content_type='image/jpeg')
        
        # Mendapatkan URL publik file
        public_url = blob.public_url
    finally:
        # Menghapus file sementara setelah diupload
        os.remove(temp_file_path)

    return public_url

@app_flask.route('/predict', methods=['POST'])
def predict():
    # Validasi Email & Gambar
    if 'img' not in request.files:
        return jsonify({"status": False, "message": "Tidak ada gamabr"}), 400
    if 'email' not in request.form:
        return jsonify({"status": False, "message": "Email diperlukan"}), 400

    # Menerima Gambar Dan Email
    img = request.files['img']
    email = request.form['email']

    # Validasi Gambar, Ukuran, Format
    if img.filename == '':
        return jsonify({"status": False, "message": "Tidak ada gambar"}), 400
    if not allowed_file(img.filename):
        return jsonify({"status": False, "message": f"Format file tidak didukung. Hanya diperbolehkan: {', '.join(ALLOWED_EXTENSIONS)}"}), 400
    if len(img.read()) > MAX_IMAGE_SIZE:
        return jsonify({"status": False, "message": "Ukuran gambar terlalu besar"}), 400
    img.seek(0)

    try:
        # Load image dan preproses
        img = load_img(io.BytesIO(img.read()), target_size=(224, 224))
        img_array = img_to_array(img)
        img_array = efficientnet.preprocess_input(img_array)
        img_array = np.expand_dims(img_array, axis=0)
        predictions = model.predict(img_array)

        # Apply softmax to convert predictions to probabilities
        probabilities = tf.nn.softmax(predictions).numpy() * 1000  # Convert to range 0-100
        predicted_index = np.argmax(probabilities, axis=1)[0]  
        predicted_confidence = int(probabilities[0][predicted_index])
        predicted_province = class_labels[predicted_index]
        # Menghapus karakter '_' dari nama prediksi
        predicted_province_cleaned = predicted_province.replace('_', ' ')
        predicted_id = np.random.randint(1000, 9999)
        
        # Validasi predict value
        score = predicted_confidence
        threshold = 90
        keys = list(data.keys())
        if score > threshold:
            response = {
                "status": True,
                "data": data[keys[class_labels.index(predicted_province)]]
            }
            # Membuat jam, nama, dan upload ke Cloud Storage
            jakarta_tz = pytz.timezone("Asia/Jakarta")
            created_at = datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S")
            utc_time = datetime.strptime(created_at, "%Y-%m-%d %H:%M:%S").replace(tzinfo=pytz.utc)
            converted_createdAt = utc_time.astimezone(jakarta_tz).strftime("%Y-%m-%d %H:%M:%S")
            name = f"{email.replace('@', '_').replace('.', '_')}_{converted_createdAt.replace(' ', '_').replace(':', '_')}"
            image_url = upload_blob(img, f"picture-history/{name}.jpg")   
            history_ref = db.collection('histories').document(email).collection('entries')
            history_ref.add({
                "id": str(predicted_id),
                "result": predicted_province_cleaned,
                "createdAt": converted_createdAt,
                "data": data[keys[class_labels.index(predicted_province)]],
                "imageUrl": image_url,
            })

        else:
            response = {
                "status": False,
                "message": "Mungkin bukan batik"
            }
        return jsonify(response)

    except Exception as e:
        # return jsonify({"status": False,"message": str(e)}), 500
        return jsonify({"status": False,"message": "Terjadi kesalahan, coba lagi"}), 500

@app_flask.route('/predict/histories', methods=['GET'])
def get_histories():
    email = request.args.get('email')

    # Validasi email
    if not email:
        return jsonify({
            "status": False,"message": "Email diperlukan"}), 400

    try:
        # Mengambil data dari firestore
        histories_ref = db.collection('histories').document(email).collection('entries')
        docs = histories_ref.stream()
        histories = []
        for doc in docs:
            # Mengubah waktu UTC ke WIB
            history_data = doc.to_dict()
            histories.append(history_data)

        # Validasi ketersedian file
        if len(histories) == 0:
            return jsonify({
                "status": False,
                "message": "Anda belum melakukan memiliki riwayat pindai"
            }), 404

        return jsonify({
            "status": True,
            "message": "Berhasil ambil riwayat",
            "data": histories
        })
    
    except Exception as e:
        return jsonify({"status": False, "message": "Terjadi kesalahan, coba lagi"}), 500

@app_flask.route('/catalog', methods=['GET'])
def catalog():
    return jsonify({
        "data": data_catalog
    })

if __name__ == '__main__':
    app_flask.run(debug=True, host="0.0.0.0", port=5000)
