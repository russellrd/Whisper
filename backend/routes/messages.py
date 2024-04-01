from flask import Blueprint, request
from database.database_handler import database_command 

message_bp = Blueprint("message", __name__, url_prefix="/message")

# This should be user admin users only 
@message_bp.route("/getAll", methods=["GET"])
def get_all_message_channels():    
    return database_command("""SELECT MESSAGES.id as id, p1.name as user1, p2.name as user2
FROM MESSAGES 
INNER JOIN USERS p1 ON p1.id = MESSAGES.user1 
INNER JOIN USERS p2 ON p2.id = MESSAGES.user2;""")

# Get's all current chats that you're apart of - Employee 
@message_bp.route("/getCurrentChats", methods=["GET"])
def get_user_message_channels():
    user_id = request.args.get("id");
    return database_command(f"""
SELECT temp.id as id, p1.name as user1, p2.name as user2
FROM (
SELECT m.id as id, m.user1 as user1, m.user2 as user2
FROM (SELECT * FROM MESSAGES WHERE (MESSAGES.user1 = "{user_id}")) as m
INNER JOIN USERS p1 ON p1.id = m.user1 
INNER JOIN USERS p2 ON p2.id = m.user2  
UNION 
SELECT m.id as id, m.user2 as user1, m.user1 as user2
FROM (SELECT * FROM MESSAGES WHERE (MESSAGES.user2 = "{user_id}")) as m
INNER JOIN USERS p1 ON p1.id = m.user1 
INNER JOIN USERS p2 ON p2.id = m.user2) as temp
INNER JOIN USERS p1 ON p1.id = temp.user1 
INNER JOIN USERS p2 ON p2.id = temp.user2;
""")
