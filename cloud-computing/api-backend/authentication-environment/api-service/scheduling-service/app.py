import schedule
from flask import Flask
from cleanup import delete_otp, delete_expired_otp, delete_expired_tokens, delete_expired_token_delacc
from threading import Thread
import time

app = Flask(__name__)

schedule.every(1).minutes.do(delete_otp)
schedule.every(1).minutes.do(delete_expired_otp)
schedule.every(1).minutes.do(delete_expired_tokens)
schedule.every(1).minutes.do(delete_expired_token_delacc)

def run_schedule():
    """Menjalankan scheduler secara terus-menerus."""
    while True:
        schedule.run_pending()
        time.sleep(1)

scheduler_thread = Thread(target=run_schedule, daemon=True)
scheduler_thread.start()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4500, debug=True)
