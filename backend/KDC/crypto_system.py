import base64
import os
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad

IV_SIZE = 16

class CryptoSystem:
    def __init__(self):
        pass

    @staticmethod
    def encrypt(unencryptedString: str, key: str):
        bytes_unencryptedString = pad(unencryptedString.encode('utf-8'), IV_SIZE)
        bytes_key = key.encode('utf-8')
        iv = os.urandom(IV_SIZE)
        cipher = AES.new(bytes_key, AES.MODE_CBC, iv)
        cipher_text = cipher.encrypt(bytes_unencryptedString)
        return base64.b64encode(iv + cipher_text).decode('utf-8')

    @staticmethod
    def decrypt(encryptedString: str, key: str):
        b64Decoded = base64.b64decode(encryptedString)
        iv = b64Decoded[0:IV_SIZE]
        cipher_text = b64Decoded[IV_SIZE:]
        bytes_key = key.encode('utf-8')
        cipher = AES.new(bytes_key, AES.MODE_CBC, iv)
        decrypted = cipher.decrypt(cipher_text)
        return unpad(decrypted, IV_SIZE).decode('utf-8')
