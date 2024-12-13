from flask import request, jsonify
from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
import mysql.connector
import jwt


db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

SECRET_KEY = "secret_key"

def check_token():
    data = request.json
    token = data.get('token')

    if not token:
        return jsonify({
            "status": False,
            "message": "Token diperlukan"
        }), 400

    cursor = None

    try:
        decoded = jwt.decode(token, SECRET_KEY, algorithms=["HS256"])
        email = decoded.get("email")

        cursor = db.cursor(dictionary=True)

        query = "SELECT nama, email FROM users WHERE email = %s AND token = %s"
        cursor.execute(query, (email, token))
        user = cursor.fetchone()

        if not user:
            return jsonify({
                "status": False,
                "message": "Anda harus login ulang"
            }), 401

        # Jika token valid, kembalikan data pengguna
        return jsonify({
            "status": True,
            "email": user['email'],
            "nama": user['nama']
        }), 200

    except jwt.ExpiredSignatureError:
        return jsonify({
            "status": False,
            "pesan": "Token sudah kedaluwarsa"
        }), 401

    except jwt.InvalidTokenError:
        return jsonify({
            "status": False,
            "pesan": "Token tidak valid"
        }), 401

    except Exception as e:
        db.rollback()
        return jsonify({
            "status": False,
            "pesan": "Terjadi kesalahan, coba lagi"
        }), 500

    finally:
        if cursor:
            cursor.close()
