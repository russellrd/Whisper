import json
from dataclasses import dataclass
from datetime import datetime, timedelta, timezone
from pydantic import BaseModel
from KDC.authentication_server import TicketGrantingTicket
from KDC.crypto_system import CryptoSystem
from KDC.kdc_utils import JSONDecoderWithDatetime, JSONEncoderWithDatetime, KDCServer
from database.kdc_db import KDC_DB

MAX_LIFETIME_HOURS = 2

class TGSRequest(BaseModel):
    serviceId: str
    desiredLifetime: datetime
    userAuth: str
    tgt: str

@dataclass
class TGSResponse:
    serviceId: str
    timestamp: datetime
    lifetime: datetime
    serviceSessionKey: str

class UserAuth(BaseModel):
    id: str
    timestamp: datetime

@dataclass
class ServiceTicket:
    id: str
    serviceId: str
    timestamp: datetime
    ip: str
    lifetime: datetime
    serviceSessionKey: str

class TicketGrantingServer():
    def __init__(self):
        pass

    @staticmethod
    def get_ST(TGS_request: TGSRequest):
        # Get ticket granting server secret key
        tgs_secret_key = KDC_DB.get_tgs_secret_key()

        # Decrypt the TGT
        tgt_string = CryptoSystem.decrypt(TGS_request.tgt, tgs_secret_key)
        tgt = TicketGrantingTicket(**json.loads(tgt_string, cls=JSONDecoderWithDatetime))

        # Check if the TGT is expired
        if tgt.lifetime < datetime.now(timezone.utc):
            return "TGT expired"

        # Decrypt the user auth
        user_auth_string = CryptoSystem.decrypt(TGS_request.userAuth, tgt.tgsSessionKey)
        user_auth = UserAuth(**json.loads(user_auth_string, cls=JSONDecoderWithDatetime))

        # Check if user auth is equal to the user in the TGT
        if user_auth.id != tgt.id:
            return "UserAuth does not match TGT"
        
        # Check if the service exists
        if not KDC_DB.check_service_exists(TGS_request.serviceId):
            return f"Service \"{TGS_request.serviceId}\"does not exist"
        
        # Check if lifetime is within bounds
        if TGS_request.desiredLifetime > (datetime.now(timezone.utc)+timedelta(hours=MAX_LIFETIME_HOURS)):
            return "Lifetime exceeds maximum"
        
        # Generate a service session key
        service_session_key = KDCServer.create_key()
        # KDC_DB.create_service_session_key(TGS_request.serviceId, service_session_key)

        # Create the TGS response
        TGS_response = TGSResponse(
            TGS_request.serviceId,
            datetime.now(timezone.utc),
            TGS_request.desiredLifetime,
            service_session_key
        )
        json_TGS_response = json.dumps(TGS_response.__dict__, cls=JSONEncoderWithDatetime)

        # Encrypt the TGS response
        encrypted_TGS_response = CryptoSystem.encrypt(json_TGS_response, tgt.tgsSessionKey)

        # Create the ST
        ST = ServiceTicket(
            tgt.id,
            TGS_request.serviceId,
            datetime.now(timezone.utc),
            tgt.ip,
            TGS_request.desiredLifetime,
            service_session_key
        )
        json_ST = json.dumps(ST.__dict__, cls=JSONEncoderWithDatetime)

        # Get the service secret key
        service_secret_key = KDC_DB.get_service_secret_key()

        # Encrypt the ST
        encrypted_ST = CryptoSystem.encrypt(json_ST, service_secret_key)

        print(encrypted_TGS_response)
        print(encrypted_ST)

        return {
            "tgsResponse": encrypted_TGS_response,
            "st": encrypted_ST
        }

    @staticmethod
    def sendMessage():
        pass

    @staticmethod
    def receiveMessage():
        pass
