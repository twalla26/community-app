import json
from flask import Blueprint, jsonify, url_for, render_template, flash, request, session, g
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import redirect

from datetime import datetime

from how_about_this_day import db
from how_about_this_day.forms import AnswerForm, PlanCreateForm, UserCreateForm, UserLoginForm
from how_about_this_day.models import Plan, User
import functools


bp = Blueprint('userInfo', __name__, url_prefix='/userInfo')

@bp.route('/') # 사용자 정보 페이지
def userInfo(): 
    user_id = session.get('user_id') # 사용자가 로그인 했을 때 세션에 저장된 아이디값을 user_id에 저장
    user = User.query.get_or_404(user_id)
    username = user.username
    email = user.email
    return jsonify({"username" : username, "email" : email})

@bp.route('/user_plan_list/') # 사용자가 참여한 or 참여할 약속 페이지
def user_plan_list():
    user_id = session.get('user_id')
    user = User.query.get_or_404(user_id)
    user_plan_list = user.plan_set
    return jsonify({"user_plan_list" : user_plan_list})


