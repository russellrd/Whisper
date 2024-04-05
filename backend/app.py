from flask import Flask 

# Import all Blueprints 
from routes.announcement import announcement_bp
from routes.auth import auth_bp
from routes.messages import message_bp

# Start the flask server 
if __name__ == "__main__":
    app = Flask(__name__)
    
    # Register all blueprints 
    app.register_blueprint(announcement_bp)
    app.register_blueprint(auth_bp)
    app.register_blueprint(message_bp)
    
    app.run(host="localhost", port=4321) # Random port 