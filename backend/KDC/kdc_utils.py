from dataclasses import dataclass
from datetime import datetime
import secrets
import json

class KDCServer:
    @staticmethod
    def create_key():
        return secrets.token_bytes(16).hex()

@dataclass
class EncrypedData:
    data: str

class JSONEncoderWithDatetime(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime):
            return obj.isoformat()
        return super().default(self, obj)
    
class JSONDecoderWithDatetime(json.JSONDecoder):
    def __init__(self, *args, **kwargs):
        json.JSONDecoder.__init__(
            self, object_hook=self.object_hook, *args, **kwargs)

    def object_hook(self, obj):
        ret = {}
        for key, value in obj.items():
            if key in {'timestamp', 'lifetime', 'desiredLifetime'}:
                ret[key] = datetime.fromisoformat(value) 
            else:
                ret[key] = value
        return ret

class MessageEncryption():
    # These encryption and decryption methods can be changed to match any encryption protocol 
        # As of right now, this uses a simple progressive cipher encryption 
    def encrypt(key: str, message: str): 
        eMessage = ""
        
        for i in range(len(message)):            
            if (ord(message[i]) >= 97 and ord(message[i]) <= 122):
                eMessage += chr((ord(message[i]) - 97 + ord(key[i % len(key)]) % 26) % 26 + 97)
            elif (ord(message[i]) >= 65 and ord(message[i]) <= 90):
                eMessage += chr((ord(message[i]) - 65 + ord(key[i % len(key)]) % 26) % 26 + 65)
            else: 
                eMessage += message[i]
        
        return eMessage
    
    def decrypt(key, message): 
        dMessage = ""
        
        for i in range(len(message)):            
            if (ord(message[i]) >= 97 and ord(message[i]) <= 122):
                dMessage += chr((ord(message[i]) - 97 - ord(key[i % len(key)]) % 26) % 26 + 97)
            elif (ord(message[i]) >= 65 and ord(message[i]) <= 90):
                dMessage += chr((ord(message[i]) - 65 - ord(key[i % len(key)]) % 26) % 26 + 65)
            else: 
                dMessage += message[i]
        
        return dMessage