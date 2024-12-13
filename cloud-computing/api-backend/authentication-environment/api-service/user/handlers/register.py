from flask import request, jsonify
import random
from email_validator import validate_email, EmailNotValidError
from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
import mysql.connector
from handlers.email_sending import send_otp
import datetime

db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

def register():
    data = request.json
    email = data.get('email')
    status = "active"
    
    # Validasi jika email tidak ada
    if not email:
        return jsonify({"status": False, "message": "Email wajib diisi"}), 400

    # Validasi jika format email tidak didukung
    try:
        validate_email(email)
    except EmailNotValidError as e:
        # return jsonify({"message": str(e)}), 400  # Kode untuk debugging
        return jsonify({"status": False, "message": "Format wajib email"}), 400

    # Generate kode token
    otp_code = random.randint(100000, 999999)
    now = datetime.datetime.utcnow()
    expires_at = now + datetime.timedelta(minutes=5)

    cursor = None

    try:
        cursor = db.cursor()

        # Cek apakah email sudah terdaftar
        check_query = "SELECT COUNT(*) FROM users WHERE email = %s"
        cursor.execute(check_query, (email,))
        result = cursor.fetchone()
        if result[0] > 0:
            return jsonify({
                "status": False,
                "message": "Email sudah digunakan",
            }), 409

        # Cek apakah email sudah didaftarkan
        check_query = "SELECT COUNT(*) FROM session WHERE email = %s"
        cursor.execute(check_query, (email,))
        result = cursor.fetchone()
        if result[0] > 0:
            return jsonify({
                "status": False,
                "message": "Email sudah didaftarkan, segera verifikasi",
            }), 409

        # Jika email sama sekali belum digunakan dapat didaftarkan
        # Memasukkan data baru
        query = """
        INSERT INTO session (email, otp, status, expires_at) 
        VALUES (%s, %s, %s, %s)
        """
        values = (email, otp_code, status, expires_at)
        cursor.execute(query, values)
        db.commit()

        # Kirim OTP ke email
        send_otp(email, otp_code)

        return jsonify({
            "status": True,
            "message": "Berhasil didaftarkan, segera verifikasi."
        }), 201
    except Exception as e:
        db.rollback()
        return jsonify({"message": "Terjadi kesalahan, coba lagi"}), 500

    finally:
        if cursor:
            cursor.close()
