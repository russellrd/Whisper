from flask import Blueprint, jsonify
import sqlite3
import os

announcement_bp = Blueprint("announcement", __name__, url_prefix="/announcement")

def database_command(command):
    # Connect to the database 
    con = sqlite3.connect(os.path.join(os.path.dirname(__file__), "../whisperDatabase.db")) 
    return con.cursor().execute(command).fetchall()

def announcement_tuple_to_dict(announcement):
    return {
        "id": str(announcement[0]),
        "title": announcement[1],
        "department": announcement[2],
    }

@announcement_bp.route("/getAll", methods=["GET"])
def get_all_announcements():
    announcements = database_command("SELECT * FROM ANNOUNCEMENTS")
    formatted_announcements = []
    for announcement in announcements:
        formatted_announcements.append(announcement_tuple_to_dict(announcement))
    res = {"announcementChannels": formatted_announcements}
    return res