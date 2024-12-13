from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
from flask import request, jsonify
import jwt
import datetime
import mysql.connector
import bcrypt

# Koneksi ke database
db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

SECRET_KEY = "secret_key"

def login():
    data = request.json
    email = data.get('email')
    password = data.get('password')

    # Validasi isi password dan email
    if not email or not password:
        return jsonify({
            "status": False, 
            "message": "Email dan Password wajib diisi"
        }), 400

    cursor = None

    try:
        cursor = db.cursor(dictionary=True)

        # Ambil data pengguna berdasarkan email
        query = "SELECT * FROM users WHERE email = %s"
        cursor.execute(query, (email,))
        user = cursor.fetchone()

        # Kondisi jika email atau password salah
        if not user:
            return jsonify({
                "status": False,
                "message": "Email atau Password salah"
            }), 401
        
        password_db = user['password']

        if password != password_db:
            return jsonify({
                "status": False,
                "message": "Password salah"
            })

        # Buat kode token JWT
        expires_at = datetime.datetime.utcnow() + datetime.timedelta(days=30)
        token_payload = {
            "email": user['email'],
            "exp": expires_at
        }
        token = jwt.encode(token_payload, SECRET_KEY, algorithm="HS256")

        # Simpan token ke database
        update_query = "UPDATE users SET token = %s, expires_at = %s WHERE email = %s"
        cursor.execute(update_query, (token, expires_at, email))
        db.commit()

        return jsonify({
            "status": True,
            "kode_token": token,
            "email": user['email'],
            "nama": user['nama']
        }), 200

    except Exception as e:
        db.rollback()
        return jsonify({"status": False, "message": "Terjadi kesalahan, coba lagi"}), 500

    finally:
        if cursor:
            cursor.close()
