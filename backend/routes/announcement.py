from flask import Blueprint, request
from database.database_handler import database_command 
import secrets

announcement_channel_bp = Blueprint("announcementChannel", __name__, url_prefix="/announcementChannel")

@announcement_channel_bp.route("/getAll", methods=["GET"])
def get_all_announcements():
    return database_command(f"SELECT * FROM ANNOUNCEMENT_CHANNELS;")

@announcement_channel_bp.route("/getUserAnnouncementChannels", methods=["GET"])
def get_user_annoucements():
    user_id = request.args.get("id")
    return database_command(f"""SELECT Ac.id, Ac.title, Ac.department 
                            FROM ANNOUNCMENTS_SUBS As, ANNOUNCEMENT_CHANNELS Ac,
                            WHERE {user_id} = As.userID
                                AND As.annoucementID = Ac.id""")

@announcement_channel_bp.route("/createAnnouncementPage", methods=["POST"])
def create_announcement_channel():
    id = secrets.token_bytes(16).hex()
    title = request.args.get("title")
    return database_command(f"INSERT INTO ANNOUNCEMENT_CHANNELS (id, title, department) VALUES ('{id}', '{title}', 'Software')")

### Possible routes for sub/ un-sub
@announcement_channel_bp.route("/subAnnouncementPage", methods=["POST"])
def subscribe_announcement_page():
    userID = None # userID = Current User ID
    announcementID = None# announcementID = Current Channel ID
    return database_command(f"INSERT INTO ANNOUNCEMENTS_SUBS (userID, announcementID) VALUES ('{userID}', '{announcementID}')")

@announcement_channel_bp.route("/unSubAnnouncementPage", methods=["POST"])
def unsubscribe_announcement_page():
    userID = None # userID = Current User ID
    announcementID = None# announcementID = Current Channel ID
    return database_command(f"DELETE FROM ANNOUNCEMENTS_SUB WHERE userID = '{userID}' AND announcementID = '{announcementID}'")