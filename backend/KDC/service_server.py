import json
from dataclasses import dataclass
from datetime import datetime, timezone
from pydantic import BaseModel
from KDC.crypto_system import CryptoSystem
from KDC.kdc_utils import JSONDecoderWithDatetime, JSONEncoderWithDatetime
from KDC.ticket_granting_server import ServiceTicket, UserAuth
from database.kdc_db import KDC_DB

class SSRequest(BaseModel):
    userAuth: str
    st: str

@dataclass
class SSResponse:
    serviceAuth: str

@dataclass
class ServiceAuth:
    serviceId: int
    timestamp: datetime

class ServiceServer:
    def __init__(self):
        pass

    @staticmethod
    def get_SA(SS_request: SSRequest):
        # Get service server secret key
        service_secret_key = KDC_DB.get_service_secret_key()

        # Decrypt the ST
        st_string = CryptoSystem.decrypt(SS_request.st, service_secret_key)
        st = ServiceTicket(**json.loads(st_string, cls=JSONDecoderWithDatetime))

        # Check if the ST is expired
        if st.lifetime < datetime.now(timezone.utc):
            return "ST expired"

        # Decrypt the user auth
        user_auth_string = CryptoSystem.decrypt(SS_request.userAuth, st.serviceSessionKey)
        user_auth = UserAuth(**json.loads(user_auth_string, cls=JSONDecoderWithDatetime))

        # Check if user auth is equal to the user in the ST
        if user_auth.id != st.id:
            return "UserAuth does not match ST"

        # Check if user has access to the service
        if not KDC_DB.check_user_authorized_for_service(user_auth.id, st.serviceId):
            return "User does not have access to the service"

        # Create the service auth
        service_auth = ServiceAuth(
            st.serviceId,
            datetime.now(timezone.utc)
        )
        json_service_auth = json.dumps(service_auth.__dict__, cls=JSONEncoderWithDatetime)

        # Encrypt the service auth
        encrypted_service_auth = CryptoSystem.encrypt(json_service_auth, st.serviceSessionKey)

        return {
            "serviceAuth": encrypted_service_auth
        }
