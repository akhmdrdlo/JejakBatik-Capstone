from flask_mail import Mail, Message
from flask import current_app

mail = Mail()

def send_otp(recipient_email, otp_code):
    with current_app.app_context():
        msg = Message(
            subject="Your OTP Code - Jejak Batik",
            sender=current_app.config["MAIL_USERNAME"],
            recipients=[recipient_email]
        )

        # Isi HTML email
        msg.html = f"""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f9f9f9;">
            <div style="background-image: url('https://example.com/batik-pattern.png'); background-size: cover; padding: 40px 0;">
            <div style="max-width: 600px; margin: auto; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);">
            <div style="text-align: center; padding-bottom: 20px;">
                <img src="https://storage.googleapis.com/jejakbatik-storage/catalog-assets/logo_jejak_batik.jpg" alt="Jejak Batik Logo" style="max-width: 150px; margin-bottom: 20px;" />
                <h1 style="color: brown; font-size: 24px; margin: 0;">Welcome to Jejak Batik!</h1>
            </div>
                <p style="font-size: 16px; margin-bottom: 10px;">Hey Batikvers!</p>
                <p style="margin-bottom: 10px;">Ready to dive into the amazing world of batik?</p>
                <p style="margin-bottom: 20px;"><strong>Your OTP code is:</strong></p>
            <div style="text-align: center; margin: 20px 0;">
                <span style="font-size: 28px; font-weight: bold; color: brown; background: #f0f8f5; padding: 10px 20px; border-radius: 5px; display: inline-block;">{otp_code}</span>
            </div>
                <p style="margin-bottom: 10px;">The OTP code is your key to unlock exclusive batik designs at Jejak Batik. The code is active for 5 minutes. Activate your account now and join our batik lovers community!</p>
                <p style="margin-top: 20px; margin-bottom: 10px; font-weight: bold;">Thank You,</p>
                <p style="margin: 0; font-weight: bold;">Jejak Batik Team!</p>
            <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd;">
                <p style="margin: 0; font-size: 14px; color: #888;">Jejak Batik, Jakarta, Indonesia</p>
                <p style="margin: 0; font-size: 14px; color: #888;">&copy; 2024 Jejak Batik. All Rights Reserved.</p>
                </div>
            </div>
            </div>
        </body>
        </html>
        """

        # Kirim email
        mail.send(msg)
    
def send_verify(recipient_email):
    with current_app.app_context():
        msg = Message(
            subject="Verification",
            sender=current_app.config["MAIL_USERNAME"],
            recipients=[recipient_email]
        )
        # msg.body = f"Hello,\n\nThankYou For Your Participations, let's explore more batik in Indonesia Now!\n\nRegards Jejak Batik"
        msg.html = f"""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f9f9f9;">
            <div style="background-image: url('https://example.com/batik-pattern.png'); background-size: cover; padding: 40px 0;">
            <div style="max-width: 600px; margin: auto; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);">
                <div style="text-align: center; padding-bottom: 20px;">
                <img src="https://storage.googleapis.com/jejakbatik-storage/catalog-assets/logo_jejak_batik.jpg" alt="Jejak Batik Logo" style="max-width: 150px; margin-bottom: 20px;" />
                <h1 style="color: brown; font-size: 24px; margin: 0;">Your Verification is Successful!</h1>
                </div>
                <p style="font-size: 16px; margin-bottom: 10px;">Hi Batikvers!</p>
                <p style="margin-bottom: 20px;">Congratulations! Your account has been successfully verified. Now you can fully explore the world of batik and get exclusive access to unique designs at Jejak Batik.</p>
                <div style="text-align: center; margin: 20px;">
                <span style="font-size: 28px; font-weight: bold; color: #4CAF50; background: #f0f8f5; padding: 10px 20px; border-radius: 5px; display: inline-block;">Welcome, Batik Explorer!</span>
                </div>
                <p style="margin-bottom: 20px;">Let's together preserve the beauty of batik as Indonesia's cultural heritage. Discover, share, and enjoy batik artwork with Jejak Batik.</p>
                <p style="margin-top: 20px; margin-bottom: 10px; font-weight: bold;">Thank You,</p>
                <p style="margin: 0; font-weight: bold;">Jejak Batik Team</p>
                <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd;">
                <p style="margin: 0; font-size: 14px; color: #888;">Jejak Batik, Jakarta, Indonesia</p>
                <p style="margin: 0; font-size: 14px; color: #888;">&copy; 2024 Jejak Batik. All Rights Reserved.</p>
                </div>
            </div>
            </div>
        </body>
        </html>
        """
        mail.send(msg)

def new_password(recipient_email, password):
    with current_app.app_context():
        msg = Message(
            subject="Verification",
            sender=current_app.config["MAIL_USERNAME"],
            recipients=[recipient_email]
        )
        msg.html = f"""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f9f9f9;">
            <div style="background-image: url('https://example.com/batik-pattern.png'); background-size: cover; padding: 40px 0;">
            <div style="max-width: 600px; margin: auto; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);">
            <div style="text-align: center; padding-bottom: 20px;">
                <img src="https://storage.googleapis.com/jejakbatik-storage/catalog-assets/logo_jejak_batik.jpg" alt="Jejak Batik Logo" style="max-width: 150px; margin-bottom: 20px;" />
                <h1 style="color: brown; font-size: 24px; margin: 0;">New Password</h1>
            </div>
                <p style="font-size: 16px; margin-bottom: 10px;">Hey Batikvers!</p>
                <p style="margin-bottom: 10px;">You just requested a new password. If you didn't request it, never give this new password to anyone. If you did, you can use the login again.</p>
            <div style="text-align: center; margin: 20px 0;">
                <span style="font-size: 28px; font-weight: bold; color: brown; background: #f0f8f5; padding: 10px 20px; border-radius: 5px; display: inline-block;">{password}</span>
            </div>
                <p style="margin-top: 20px; margin-bottom: 10px; font-weight: bold;">Thank You,</p>
                <p style="margin: 0; font-weight: bold;">Jejak Batik Team!</p>
            <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd;">
                <p style="margin: 0; font-size: 14px; color: #888;">Jejak Batik, Jakarta, Indonesia</p>
                <p style="margin: 0; font-size: 14px; color: #888;">&copy; 2024 Jejak Batik. All Rights Reserved.</p>
                </div>
            </div>
            </div>
        </body>
        </html>
        """
        mail.send(msg)

def del_info(recipient_email, link_delete_account):
    with current_app.app_context():
        msg = Message(
            subject="Delete Account",
            sender=current_app.config["MAIL_USERNAME"],
            recipients=[recipient_email]
        )
        msg.html = f"""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f9f9f9;">
            <div style="background-image: url('https://example.com/batik-pattern.png'); background-size: cover; padding: 40px 0;">
            <div style="max-width: 600px; margin: auto; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);">
                <div style="text-align: center; padding-bottom: 20px;">
                <img src="https://storage.googleapis.com/jejakbatik-storage/catalog-assets/logo_jejak_batik.jpg" alt="Jejak Batik Logo" style="max-width: 150px; margin-bottom: 20px;" />
                <h1 style="color: brown; font-size: 24px; margin: 0;">Delete Account</h1>
                </div>
                <p style="font-size: 16px; margin-bottom: 10px;">Hi Batikvers!</p>
                <p style="margin-bottom: 20px;">Warning! This action of deleting an account is dangerous and the deleted account cannot be restored. If you are not the one who requested it, do not continue. If you are sure to delete, press the delete account button below!</p>
                <!-- Tombol dengan href -->
                <div style="text-align: center; margin-top: 30px;">
                    <a href="{link_delete_account}" style="
                        background-color: #4CAF50;
                        color: white;
                        text-decoration: none;
                        padding: 10px 20px;
                        font-size: 16px;
                        font-weight: bold;
                        border-radius: 5px;
                        display: inline-block;
                    ">Delete Account</a>
                </div>

                <p style="margin-top: 20px; margin-bottom: 10px; font-weight: bold;">Thank You,</p>
                <p style="margin: 0; font-weight: bold;">Jejak Batik Team</p>
                <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd;">
                <p style="margin: 0; font-size: 14px; color: #888;">Jejak Batik, Jakarta, Indonesia</p>
                <p style="margin: 0; font-size: 14px; color: #888;">&copy; 2024 Jejak Batik. All Rights Reserved.</p>
                </div>
            </div>
            </div>
        </body>
        </html>
        """
        mail.send(msg)

def del_ver_info(recipient_email):
    with current_app.app_context():
        msg = Message(
            subject="Your Account Deleted",
            sender=current_app.config["MAIL_USERNAME"],
            recipients=[recipient_email]
        )
        msg.html = f"""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f9f9f9;">
            <div style="background-image: url('https://example.com/batik-pattern.png'); background-size: cover; padding: 40px 0;">
            <div style="max-width: 600px; margin: auto; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);">
            <div style="text-align: center; padding-bottom: 20px;">
                <img src="https://storage.googleapis.com/jejakbatik-storage/catalog-assets/logo_jejak_batik.jpg" alt="Jejak Batik Logo" style="max-width: 150px; margin-bottom: 20px;" />
                <h1 style="color: brown; font-size: 24px; margin: 0;">Account Has Been Deleted</h1>
            </div>
                <p style="margin-bottom: 10px;">Your account has been deleted, you cannot use this account to log in again. Your account data cannot be recovered. If you want to rejoin, create it again or use another account.</p>
                <p style="margin-top: 20px; margin-bottom: 10px; font-weight: bold;">Thank You,</p>
                <p style="margin: 0; font-weight: bold;">Jejak Batik Team!</p>
            <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd;">
                <p style="margin: 0; font-size: 14px; color: #888;">Jejak Batik, Jakarta, Indonesia</p>
                <p style="margin: 0; font-size: 14px; color: #888;">&copy; 2024 Jejak Batik. All Rights Reserved.</p>
                </div>
            </div>
            </div>
        </body>
        </html>
        """
        mail.send(msg)