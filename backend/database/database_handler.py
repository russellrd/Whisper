import sqlite3
import os

# Whisper Database 
def database_command(command):
    # Connect to the database 
    con = sqlite3.connect(os.path.join(os.path.dirname(__file__), "../whisperDatabase.db")) 
    con.row_factory = sqlite3.Row
    raw_results = con.cursor().execute(command).fetchall()
    con.commit()
    con.close()
    
    return {
        "data": [dict(i) for i in raw_results]
    }