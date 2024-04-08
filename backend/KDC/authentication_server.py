import json
from dataclasses import dataclass
from datetime import datetime, timedelta, timezone
from pydantic import BaseModel
from KDC.crypto_system import CryptoSystem
from KDC.kdc_utils import KDCServer, JSONEncoderWithDatetime
from database.kdc_db import KDC_DB

MAX_LIFETIME_HOURS = 2

class ASRequest(BaseModel):
    id: str
    ip: str
    desiredLifetime: datetime

@dataclass
class ASResponse:
    tgsId: str
    timestamp: datetime
    lifetime: datetime
    tgsSessionKey: str

@dataclass
class TicketGrantingTicket:
    id: str
    tgsId: str
    timestamp: datetime
    ip: str
    lifetime: datetime
    tgsSessionKey: str

class AuthenticationServer():
    def __init__(self):
        pass

    @staticmethod
    def get_TGT(AS_request: ASRequest):
        # Check if the client is in the database
        if not KDC_DB.check_client_exists(AS_request.id):
            return "Client does not exist"

        # Check if lifetime is within bounds
        if AS_request.desiredLifetime > (datetime.now(timezone.utc)+timedelta(hours=MAX_LIFETIME_HOURS)):
            return "Lifetime exceeds maximum"
        
        # Generate a TGS session key
        tgs_session_key = KDCServer.create_key()
        KDC_DB.create_tgs_session_key(AS_request.id, tgs_session_key)
        
        # Create the AS response
        AS_response = ASResponse(
            "1", # TODO: Use actual id
            datetime.now(timezone.utc),
            AS_request.desiredLifetime,
            tgs_session_key
        )
        json_AS_response = json.dumps(AS_response.__dict__, cls=JSONEncoderWithDatetime)

        # Get the client's secret key
        client_secret_key = KDC_DB.get_client_secret_key(AS_request.id)

        if client_secret_key is None:
            return "Client secret key not found"

        # Encrypt the AS response
        encrypted_AS_response = CryptoSystem.encrypt(json_AS_response, client_secret_key)

        # Create the TGT
        TGT = TicketGrantingTicket(
            AS_request.id,
            "1", # TODO: Use actual id
            datetime.now(timezone.utc),
            AS_request.ip,
            AS_request.desiredLifetime,
            tgs_session_key
        )
        json_TGT = json.dumps(TGT.__dict__, cls=JSONEncoderWithDatetime)

        # Get the TGS secret key
        tgs_secret_key = KDC_DB.get_tgs_secret_key()

        # Encrypt the TGT
        encrypted_TGT = CryptoSystem.encrypt(json_TGT, tgs_secret_key)

        return {
            "asResponse": encrypted_AS_response,
            "tgt": encrypted_TGT
        }

def test_get_TGT():
    AuthenticationServer().get_TGT(ASRequest(
        "e0e815ce-a09f-4949-bd58-1f38ed498243",
        1,
        "192.168.0.1",
        datetime.now()
    ))

if __name__ == "__main__":
    test_get_TGT()
