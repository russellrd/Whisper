from flask import Blueprint 
from database.database_handler import database_command 

announcement_bp = Blueprint("announcement", __name__, url_prefix="/announcement")

@announcement_bp.route("/getAll", methods=["GET"])
def get_all_announcements():
    return database_command("SELECT * FROM ANNOUNCEMENT_CHANNELS;")
