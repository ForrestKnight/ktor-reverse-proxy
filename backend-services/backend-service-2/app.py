from flask import Flask, jsonify

app = Flask(__name__)

@app.route('/api/data')
def get_data():
    return jsonify({"service": "Backend Service 2", "message": "Hello from Flask backend!"})

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8000)
