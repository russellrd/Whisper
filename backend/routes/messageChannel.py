from flask import Blueprint, request
from database.database_handler import database_command 

message_channel_bp = Blueprint("messageChannel", __name__, url_prefix="/messageChannel")

# This should be user admin AUTHENTICATION_SERVER only 
@message_channel_bp.route("/getAll", methods=["GET"])
def get_all_message_channels():    
    return database_command("""SELECT MESSAGE_CHANNELS.id as id, p1.name as user1, p2.name as user2
FROM MESSAGE_CHANNELS 
INNER JOIN AUTHENTICATION_SERVER p1 ON p1.id = MESSAGE_CHANNELS.user1 
INNER JOIN AUTHENTICATION_SERVER p2 ON p2.id = MESSAGE_CHANNELS.user2;""")

# Get's all current chats that you're apart of - Employee 
@message_channel_bp.route("/getCurrentChats", methods=["GET"])
def get_user_message_channels():
    user_id = request.args.get("id") 
    return database_command(f"""
SELECT temp.id as id, p1.name as user1, p2.name as user2
FROM (
SELECT m.id as id, m.user1 as user1, m.user2 as user2
FROM (SELECT * FROM MESSAGE_CHANNELS WHERE (MESSAGE_CHANNELS.user1 = "{user_id}")) as m
INNER JOIN AUTHENTICATION_SERVER p1 ON p1.id = m.user1 
INNER JOIN AUTHENTICATION_SERVER p2 ON p2.id = m.user2  
UNION 
SELECT m.id as id, m.user2 as user1, m.user1 as user2
FROM (SELECT * FROM MESSAGE_CHANNELS WHERE (MESSAGE_CHANNELS.user2 = "{user_id}")) as m
INNER JOIN AUTHENTICATION_SERVER p1 ON p1.id = m.user1 
INNER JOIN AUTHENTICATION_SERVER p2 ON p2.id = m.user2) as temp
INNER JOIN AUTHENTICATION_SERVER p1 ON p1.id = temp.user1 
INNER JOIN AUTHENTICATION_SERVER p2 ON p2.id = temp.user2;
""")
    
# Get all related AUTHENTICATION_SERVER based on search prompt 
@message_channel_bp.route("/getAUTHENTICATION_SERVERearch", methods=["GET"])
def query_related_AUTHENTICATION_SERVER():
    search = request.args.get("id")
    
    query = database_command(f"SELECT * FROM AUTHENTICATION_SERVER WHERE name LIKE '%{search}%';")
    
    # Sort through the query based on the location of the search index
    query['data'].sort(key=lambda x: x["name"].find(search))
    
    # [Insert section for removing AUTHENTICATION_SERVER that outside of security range]
    
    return query
