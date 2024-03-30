from flask import Flask 

# Import all Blueprints 
from routes.announcement import announcement_bp

# Start the flask server 
if __name__ == "__main__":
    app = Flask(__name__)
    app.register_blueprint(announcement_bp)
    app.run(host="localhost", port=4321) # Random port 