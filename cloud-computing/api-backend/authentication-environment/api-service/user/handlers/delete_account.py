from flask import request, jsonify, render_template
from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
import mysql.connector
import bcrypt
import firebase_admin
import uuid
from firebase_admin import credentials, storage, firestore
import datetime
from handlers.email_sending import del_info

# URL App Engine Service
base_url = "https://deleteaccountverify-dot-jejakbatik.et.r.appspot.com"

db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

def delete_account_step1():
    # email = request.args.get('email')
    email = request.form.get('email')
    cursor = None
    try:
        cursor = db.cursor(dictionary=True)
        query_user = "SELECT id FROM users WHERE email = %s"
        cursor.execute(query_user, (email,))
        user = cursor.fetchone()
        if not user:
            return jsonify({"status": "gagal", "pesan": "Akun tidak ditemukan"}), 404
        user_id = user['id']

        # Generate token unik
        token = str(uuid.uuid4())
        expire_time = datetime.datetime.utcnow() + datetime.timedelta(minutes=10)

        # Simpan token di database
        insert_token_query = """
            INSERT INTO delete_account_sessions (user_id, token, expire_at) 
            VALUES (%s, %s, %s)
        """
        cursor.execute(insert_token_query, (user_id, token, expire_time))
        db.commit()

        # Kirimkan link untuk penghapusan
        delete_link = f"{base_url}/delete?token={token}"
        del_info(email, delete_link)
        return jsonify({
            "status": True,
            "message": "Cek email untuk penghapusan lebih lanjut",
        }), 200

    except Exception as e:
        db.rollback()
        return jsonify({
            "status": False,
            "message": "Anda sudah meminta hapus akun, segera cek email"
        }), 404

    finally:
        if cursor:
            cursor.close()
