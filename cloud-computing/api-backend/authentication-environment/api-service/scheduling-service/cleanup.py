import mysql.connector
from db import MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, MYSQL_PORT
import datetime

db = mysql.connector.connect(
    host=MYSQL_HOST,
    user=MYSQL_USER,
    password=MYSQL_PASSWORD,
    database=MYSQL_DB,
    port=MYSQL_PORT
)

def delete_otp():
    """Fungsi untuk menghapus token dengan status 'expired'."""
    cursor = None
    try:
        cursor = db.cursor()
        cursor.execute("DELETE FROM session WHERE status = 'expired'")
        db.commit()
        print("Deleted inactive tokens.")
    except Exception as e:
        db.rollback()
        # print(f"Error deleting tokens: {e}")
    finally:
        if cursor:
            cursor.close()

def delete_expired_otp():
    cursor = None
    try:
        cursor = db.cursor()
        now = datetime.datetime.utcnow()

        # Query untuk membaca dan menghapus row yang sudah expired
        delete_query = "DELETE FROM session WHERE expires_at < %s"
        cursor.execute(delete_query, (now,))
        db.commit()

        print(f"{cursor.rowcount} expired otp deleted successfully.")

    except Exception as e:
        db.rollback()

    finally:
        # Tutup cursor
        if cursor:
            cursor.close()

def delete_expired_tokens():
    cursor = None
    try:
        cursor = db.cursor()
        now = datetime.datetime.utcnow()

        delete_query = "UPDATE users SET token = NULL, expires_at = NULL WHERE expires_at < %s"
        cursor.execute(delete_query, (now,))
        db.commit()

        print(f"{cursor.rowcount} expired tokens deleted successfully.")

    except Exception as e:
        db.rollback()

    finally:
        if cursor:
            cursor.close()

def delete_expired_token_delacc():
    cursor = None
    try:
        cursor = db.cursor()
        now = datetime.datetime.utcnow()
        delete_query = "DELETE FROM delete_account_sessions WHERE expire_at < %s"
        cursor.execute(delete_query, (now,))
        db.commit()
        print(f"{cursor.rowcount} expired tokens deleted successfully.")

    except Exception as e:
        db.rollback()

    finally:
        if cursor:
            cursor.close()
