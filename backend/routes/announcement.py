from flask import Blueprint, request
from database.database_handler import database_command 
import secrets

announcement_channel_bp = Blueprint("announcementChannel", __name__, url_prefix="/announcementChannel")

#this endpoint is for retreiving a list of all the announcement channels
@announcement_channel_bp.route("/getAll", methods=["GET"])
def get_all_announcements():
    return database_command(f"SELECT * FROM ANNOUNCEMENT_CHANNELS;")

#this endpoint is for retreiving a list of only the announcements channels the user is subscribed to
@announcement_channel_bp.route("/getUserAnnouncementChannels", methods=["GET"])
def get_user_annoucements():
    user_id = request.args.get("id")
    return database_command(f"""SELECT Ac.id, Ac.title, Ac.department 
                            FROM ANNOUNCMENTS_SUBS As, ANNOUNCEMENT_CHANNELS Ac,
                            WHERE {user_id} = As.userID
                                AND As.annoucementID = Ac.id""")

#this endpoint creates a new announcement page with the title and stores it in the database
@announcement_channel_bp.route("/createAnnouncementPage", methods=["POST"])
def create_announcement_channel():
    id = secrets.token_bytes(16).hex()
    title = request.args.get("title")
    return database_command(f"INSERT INTO ANNOUNCEMENT_CHANNELS (id, title, department) VALUES ('{id}', '{title}', 'Software')")

#the following two endpoints are used for subscribing and unsubscribing to an announcements page
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