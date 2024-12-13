from flask import request, jsonify
from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
import mysql.connector
import bcrypt

db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

def change_password():
    data = request.json
    email = data.get('email')
    old_password = data.get('password_lama')
    new_password = data.get('password_baru')

    if not all([email, old_password, new_password]):
        return jsonify({"status": "gagal", "pesan": "Email, password lama, dan password baru diperlukan"}), 400

    if old_password == new_password:
        return jsonify({"status": "gagal", "pesan": "Password baru tidak boleh sama dengan password lama"}), 400

    try:
        cursor = db.cursor(dictionary=True)

        # Validasi password lama
        query = "SELECT id, password FROM users WHERE email = %s"
        cursor.execute(query, (email,))
        user = cursor.fetchone()

        if not user:
            return jsonify({"status": "gagal", "pesan": "Email tidak ditemukan"}), 404

        stored_hash = user['password']
        if not bcrypt.checkpw(old_password.encode('utf-8'), stored_hash.encode('utf-8')):
            return jsonify({"status": "gagal", "pesan": "Password lama salah"}), 401

        # Cek apakah password baru pernah digunakan
        history_query = "SELECT old_password FROM password_history WHERE user_id = %s"
        cursor.execute(history_query, (user['id'],))
        recent_passwords = [row['old_password'] for row in cursor.fetchall()]

        for old_hash in recent_passwords:
            if bcrypt.checkpw(new_password.encode('utf-8'), old_hash.encode('utf-8')):
                return jsonify({"status": "gagal", "pesan": "Password baru tidak boleh sama dengan password lama sebelumnya"}), 400

        # Dinonaktifkan
        # Enkripsi password baru
        # hashed_new_password = bcrypt.hashpw(new_password.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')

        # Update password baru
        update_query = "UPDATE users SET password = %s WHERE id = %s"
        cursor.execute(update_query, (hashed_new_password, user['id']))

        # Simpan password lama ke history
        insert_history = "INSERT INTO password_history (user_id, old_password) VALUES (%s, %s)"
        cursor.execute(insert_history, (user['id'], stored_hash))

        db.commit()
        return jsonify({"status": "berhasil", "pesan": "Password berhasil diubah"}), 200

    except Exception as e:
        db.rollback()
        return jsonify({"status": "gagal", "pesan": f"Terjadi kesalahan: {str(e)}"}), 500

    finally:
        cursor.close()
