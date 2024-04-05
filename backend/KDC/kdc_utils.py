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
