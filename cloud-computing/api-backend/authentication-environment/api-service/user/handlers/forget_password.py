import random
import string
from flask import request, jsonify
from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
from handlers.email_sending import new_password
import mysql.connector
import bcrypt

db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

def forget_password():
    data = request.json
    email = data.get('email')

    if not email:
        return jsonify({"status": False, "message": "Email diperlukan"}), 400

    cursor = None

    try:
        cursor = db.cursor(dictionary=True)
        query = "SELECT id FROM users WHERE email = %s"
        cursor.execute(query, (email,))
        user = cursor.fetchone()

        if not user:
            return jsonify({"status": False, "message": "Email tidak ditemukan"}), 404

        # Generate password baru
        new_password_value = ''.join(random.choices(string.ascii_letters + string.digits, k=10))

        # Kirim password baru
        new_password(email, new_password_value)

        # Dinonaktifkan
        # Enkripsi password baru
        # salt = bcrypt.gensalt()
        # hashed_password = bcrypt.hashpw(new_password_value.encode('utf-8'), salt)

        # Update password
        update_query = "UPDATE users SET password = %s WHERE id = %s"
        cursor.execute(update_query, (new_password_value, user['id']))
        db.commit()

        return jsonify({
            "status": True, 
            "message": "Password baru anda telah dikirimkan ke email, cek sekarang"
            }), 200

    except Exception as e:
        db.rollback()
        return jsonify({"status": False, "message": "Terjadi Kesalahan"})

    finally:
        if cursor:
            cursor.close()
