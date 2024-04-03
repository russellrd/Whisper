from database.database_handler import database_command

class KDC_DB:
    def __init__(self):
        pass

    @staticmethod
    def check_client_exists(id: str):
        client = database_command(f"SELECT * FROM AUTHENTICATION_SERVER WHERE id = '{id}'")
        if len(client) == 0:
            return False
        return True
    
    @staticmethod
    def check_service_exists(id: str):
        service = database_command(f"SELECT * FROM TICKET_GRANTING_SERVER WHERE service_id = '{id}'")
        if len(service) == 0:
            return False
        return True
    
    @staticmethod
    def get_client_secret_key(id: str):
        client_secret_key = database_command(f"SELECT client_secret_key FROM AUTHENTICATION_SERVER WHERE id = '{id}'")
        if len(client_secret_key["data"]) == 0:
            return None #TODO: Error handling
        return client_secret_key["data"][0]["client_secret_key"]
    
    @staticmethod
    def get_tgs_secret_key():
        tgs_secret_key = database_command("SELECT value FROM KDC_KEYS WHERE key = 'tgs_secret_key'")
        if len(tgs_secret_key["data"]) == 0:
            return None #TODO: Error handling
        return tgs_secret_key["data"][0]["value"]
    
    @staticmethod
    def get_service_secret_key():
        service_secret_key = database_command("SELECT value FROM KDC_KEYS WHERE key = 'service_secret_key'")
        if len(service_secret_key["data"]) == 0:
            return None #TODO: Error handling
        return service_secret_key["data"][0]["value"]
    
    @staticmethod
    def get_tgs_session_key(id: str):
        tgs_session_key = database_command(f"SELECT tgs_session_key FROM TGS_SESSION_KEYS WHERE id = '{id}'")
        if len(tgs_session_key["data"]) == 0:
            return None #TODO: Error handling
        return tgs_session_key["data"][0]["tgs_session_key"]
    
    @staticmethod
    def create_tgs_session_key(id: str, tgs_session_key: str):
        # Check if user already has a session key
        if KDC_DB.get_tgs_session_key(id) is not None:
            # Update the session key
            database_command(f"UPDATE TGS_SESSION_KEYS SET tgs_session_key = '{tgs_session_key}' WHERE id = '{id}'")
        else:
            # Create a new session key
            database_command(f"INSERT INTO TGS_SESSION_KEYS (id, tgs_session_key) VALUES ('{id}', '{tgs_session_key}')")
    
    @staticmethod
    def get_service_session_key(id: str):
        service_session_key= database_command(f"SELECT service_session_key FROM SERVICE_SESSION_KEYS WHERE id = '{id}'")
        if len(service_session_key["data"]) == 0:
            return None #TODO: Error handling
        return service_session_key["data"][0]["service_session_key"]
    
    @staticmethod
    def check_user_authorized_for_service(id: str, serviceId: str):
        client = database_command(f"SELECT * FROM AUTHENTICATION_SERVER WHERE id = '{id}'")
        if len(client) == 0:
            return False
        if int(client["data"][0]["role"]) < int(serviceId):
            return False
        return True
