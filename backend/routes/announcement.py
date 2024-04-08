from flask import Blueprint, request
from database.database_handler import database_command 
import secrets

announcement_channel_bp = Blueprint("announcementChannel", __name__, url_prefix="/announcementChannel")

@announcement_channel_bp.route("/getAll", methods=["GET"])
def get_all_announcements():
    return database_command(f"SELECT * FROM ANNOUNCEMENT_CHANNELS;")

def get_user_annoucements():
    user_id = request.args.get("id")
    return database_command(f"""SELECT Ac.id, Ac.title, Ac.department 
                            FROM ANNOUNCMENTS_SUBS As, ANNOUNCEMENT_CHANNELS Ac,
                            WHERE {user_id} = As.userID
                                AND As.annoucementID = Ac.id""")

@announcement_channel_bp.route("/createAnnouncementPage", methods=["POST"])
def create_announcement_channel():
    id = secrets.token_bytes(16).hex()
    title = request.args("title")
    return database_command(f"INSERT INTO ANNOUNCEMENT_CHANNELS (id, title, department) VALUES ('{id}', '{1}', '{NULL}')")
