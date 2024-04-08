from flask import Blueprint, jsonify, request
from KDC.authentication_server import ASRequest, AuthenticationServer
from KDC.service_server import SSRequest, ServiceServer
from KDC.ticket_granting_server import TGSRequest, TicketGrantingServer

auth_bp = Blueprint("auth", __name__, url_prefix="/auth")

@auth_bp.route("/tgs", methods=["POST"])
def get_ST():
    TGS_request = TGSRequest(**request.get_json())
    TGS_response = TicketGrantingServer().get_ST(TGS_request)
    return jsonify(TGS_response)

@auth_bp.route("/ss", methods=["POST"])
def get_SA():
    SS_request = SSRequest(**request.get_json())
    SS_response = ServiceServer().get_SA(SS_request)
    return jsonify(SS_response)

@auth_bp.route("/login", methods=["POST"])
def login():
    AS_request = ASRequest(**request.get_json())
    AS_respone = AuthenticationServer().get_TGT(AS_request)
    return jsonify(AS_respone)

@auth_bp.route("/logout", methods=["POST"])
def logout():
    pass
