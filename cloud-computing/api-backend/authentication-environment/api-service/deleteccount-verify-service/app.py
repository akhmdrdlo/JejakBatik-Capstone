from flask_mail import Mail, Message
from flask import Flask, jsonify, request, current_app, render_template
from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
from firebase_admin import credentials, firestore
import firebase_admin
import mysql.connector
import datetime

app = Flask(__name__)

# Konfigurasi Flask-Mail
app.config["MAIL_SERVER"] = "smtp.gmail.com"
app.config["MAIL_PORT"] = 587
app.config["MAIL_USERNAME"] = "alamat_email"  
app.config["MAIL_PASSWORD"] = "password_service_akun" 
app.config["MAIL_USE_TLS"] = True
app.config["MAIL_USE_SSL"] = False

# Inisialisasi Flask-Mail
mail_send = Mail(app)

# Inisialisasi Firebase
cred = credentials.Certificate("credential.json")
firebase_admin.initialize_app(cred)
db_firestore = firestore.client()

# Koneksi MySQL
db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

# Fungsi untuk mengirim email
def del_ver_info(recipient_email):
    with current_app.app_context():
        msg = Message(
            subject="Your Account Deleted",
            sender=current_app.config["MAIL_USERNAME"],
            recipients=[recipient_email]
        )
        msg.html = f"""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f9f9f9;">
            <div style="background-image: url('https://example.com/batik-pattern.png'); background-size: cover; padding: 40px 0;">
            <div style="max-width: 600px; margin: auto; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);">
            <div style="text-align: center; padding-bottom: 20px;">
                <img src="https://storage.googleapis.com/jejakbatik-storage/catalog-assets/logo_jejak_batik.jpg" alt="Jejak Batik Logo" style="max-width: 150px; margin-bottom: 20px;" />
                <h1 style="color: brown; font-size: 24px; margin: 0;">Account Has Been Deleted</h1>
            </div>
                <p style="margin-bottom: 10px;">Your account has been deleted, you cannot use this account to log in again. Your account data cannot be recovered. If you want to rejoin, create it again or use another account.</p>
                <p style="margin-top: 20px; margin-bottom: 10px; font-weight: bold;">Thank You,</p>
                <p style="margin: 0; font-weight: bold;">Jejak Batik Team!</p>
            <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd;">
                <p style="margin: 0; font-size: 14px; color: #888;">Jejak Batik, Jakarta, Indonesia</p>
                <p style="margin: 0; font-size: 14px; color: #888;">&copy; 2024 Jejak Batik. All Rights Reserved.</p>
                </div>
            </div>
            </div>
        </body>
        </html>
        """
        mail.send(msg)


@app.route('/delete', methods=['GET'])
def delete_account_step2():
    token = request.args.get('token', '').strip()
    cursor = None
    try:
        cursor = db.cursor(dictionary=True)
        # Verifikasi token
        query_token = """
            SELECT t.user_id, t.expire_at, u.email 
            FROM delete_account_sessions t 
            JOIN users u ON t.user_id = u.id 
            WHERE t.token = %s
        """
        cursor.execute(query_token, (token,))
        token_record = cursor.fetchone()

        user_id = token_record["user_id"]
        email = token_record["email"]

        # Hapus data di Firestore
        doc_ref = db_firestore.collection('histories').document(email)
        entries_ref = doc_ref.collection('entries')
        entries = entries_ref.stream()
        for entry in entries:
            entry.reference.delete()
        doc_ref.delete()

        # Hapus data pengguna di database MySQL
        delete_user_query = "DELETE FROM users WHERE id = %s"
        cursor.execute(delete_user_query, (user_id,))

        # Hapus token dari tabel
        delete_token_query = "DELETE FROM delete_account_sessions WHERE token = %s"
        cursor.execute(delete_token_query, (token,))

        del_ver_info(email)

        db.commit()

        return render_template('success.html')

    except mysql.connector.Error as db_err:
        db.rollback()
        return "Sepertinya ada Kesalahan"

    except Exception as e:
        db.rollback()
        return "Sepertinya ada Kesalahan"

    finally:
        if cursor:
            cursor.close()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)

