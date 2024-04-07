from flask import Blueprint 
from database.database_handler import database_command 

announcement_channel_bp = Blueprint("announcementChannel", __name__, url_prefix="/announcementChannel")

@announcement_channel_bp.route("/getAll", methods=["GET"])
def get_all_announcements():
    return database_command("SELECT * FROM ANNOUNCEMENT_CHANNELS;")
