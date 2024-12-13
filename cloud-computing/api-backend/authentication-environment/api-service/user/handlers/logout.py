from flask import request, jsonify
from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
import mysql.connector

db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

def logout():
    data = request.json
    token = data.get('token')

    if not token:
        return jsonify({
            "status": False,
            "message": "Token diperlukan"
        }), 400

    cursor = None

    try:
        cursor = db.cursor()


        query = "UPDATE users SET token = NULL, expires_at = NULL WHERE token = %s"
        cursor.execute(query, (token,))
        db.commit()

        # Kondisi jika tidak ada token
        if cursor.rowcount == 0:
            return jsonify({
                "status": False,
                "message": "Token tidak valid atau sudah logout"
            }), 400

        return jsonify({
            "status": True,
            "message": "Logout berhasil"
        }), 200

    except Exception as e:
        db.rollback()
        return jsonify({
            "status": False,
            "message": "Terjadi kesalahan, coba lagi"
        }), 500

    finally:
        if cursor:
            cursor.close()
