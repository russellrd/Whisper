from flask import Blueprint, request
from database.database_handler import database_command 
from KDC.kdc_utils import MessageEncryption
import time

message_bp = Blueprint("message", __name__, url_prefix="/message")

# Send a message to another use 
@message_bp.route("/sendMessage", methods=["POST"])
def send_message():
    sender = request.args.get("sender")
    receiver = request.args.get("receiver")
    message_channel = request.args.get("channel")
    timestamp = str(time.time())
    message = request.args.get("message")
    id = timestamp 
    
    # Get the message channel session key 
    query = database_command(f"SELECT channel_key FROM MESSAGE_CHANNELS WHERE id = '{message_channel}';")
    encryption_key = query['data'][0]['channel_key']
    
    # Encrypt the message 
    encrypted_message = MessageEncryption.encrypt(encryption_key, message)
    
    return database_command(f"""INSERT INTO MESSAGES(id, message_channel, sender, receiver, timestamp, message) VALUES ("{id}", "1", "{sender}", "{receiver}", "{timestamp}", "{encrypted_message}");""") 

# Get's all current chat histroy for a specific chat 
@message_bp.route("/getChatHistory", methods=["GET"])
def get_user_message_channels():
    id = request.args.get("id")
    all_messages =  database_command(f"SELECT * FROM MESSAGES WHERE message_channel = '{id}';")
    
    # Get the channel encryption key 
    query = database_command(f"SELECT channel_key FROM MESSAGE_CHANNELS WHERE id = {id};")
    encryption_key = query['data'][0]['channel_key']
    
    # Decrypt all the messages 
    for element in all_messages['data']:
        element['message'] = MessageEncryption.decrypt(encryption_key, element['message'])
    
    return all_messages