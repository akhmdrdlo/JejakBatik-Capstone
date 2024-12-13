from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
from flask import request, jsonify
import mysql.connector
import bcrypt
from handlers.email_sending import send_verify

# Koneksi ke database
db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

def verification():
    data = request.json
    email = data.get('email')
    nama = data.get('nama')
    otp = data.get('otp')
    password = data.get('password')
    status = 'inactive'

    # Validasi input
    if not email or not password or not otp:
        return jsonify({
            "status": False,
            "message": "Email, password, dan OTP wajib diisi"
        }), 400

    cursor = None

    try:
        cursor = db.cursor()

        # Verifikasi apakah OTP valid
        cursor.execute(
            "SELECT * FROM session WHERE email = %s AND otp = %s AND status = 'active'",
            (email, otp)
        )

        otp_record = cursor.fetchone()

        if not otp_record:
            return jsonify({
                "status": False,
                "message": "OTP tidak valid atau sudah kadaluarsa"
            }), 400

        # Masukkan data user ke tabel `users`
        cursor.execute(
            "INSERT INTO users (nama, email, password) VALUES (%s, %s, %s)",
            (nama, email, password,)
        )

        # Perbarui status OTP di tabel `session`
        cursor.execute(
            "UPDATE session SET status = %s WHERE email = %s",
            (status, email)
        )
        db.commit()
        send_verify(email)
        return jsonify({
            "status": True,
            "message": "Akun berhasil diverifikasi dan dibuat."
        }), 200

    except Exception as e:
        db.rollback() 
        return jsonify({"status": False, "message": "Terjadi kesalahan, coba lagi"}), 500

    finally:
        if cursor:
            cursor.close()
