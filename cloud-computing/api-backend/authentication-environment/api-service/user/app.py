from flask import Flask, render_template, jsonify, request, redirect, url_for, session, flash
from threading import Thread
import schedule
from handlers.register import register
from handlers.verify import verification
from handlers.login import login
from handlers.check_token import check_token
from handlers.logout import logout
from handlers.forget_password import forget_password
from handlers.change_password import change_password
from handlers.delete_account import delete_account_step1
import time
from handlers.email_sending import mail

app = Flask(__name__)

# Konfigurasi Mail Server
app.config["MAIL_SERVER"] = "smtp.gmail.com"
app.config["MAIL_PORT"] = 587
app.config["MAIL_USERNAME"] = "alamat_email"  # Email Anda
app.config["MAIL_PASSWORD"] = "password_service_email"    # Password aplikasi
app.config["MAIL_USE_TLS"] = True
app.config["MAIL_USE_SSL"] = False
mail.init_app(app)

@app.route('/register', methods=['POST'])
def reg():
    return register()

@app.route('/verify', methods=['POST'])
def verif():
    return verification()

@app.route('/login', methods=['POST'])
def log():
    return login()

@app.route('/token', methods=['POST'])
def cek():
    return check_token()

@app.route('/logout', methods=['POST'])
def user_logout():
    return logout()

@app.route('/forgetpassword', methods=['POST'])
def lupa_password():
    return forget_password()

@app.route('/changepassword', methods=['POST'])
def ganti_password():
    return change_password()

@app.route('/deleteaccount', methods=['POST'])
def del_akun():
    return delete_account_step1()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4000, debug=True)
