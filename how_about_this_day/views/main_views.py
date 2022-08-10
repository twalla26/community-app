from flask import Blueprint, Flask, jsonify
from werkzeug.utils import redirect
from how_about_this_day import db
from how_about_this_day.models import test


bp = Blueprint('main', __name__, url_prefix='/')

@bp.route('/')
def index():
    return jsonify({"hello":"world"})

