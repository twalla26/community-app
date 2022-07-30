import json
from flask import Blueprint, jsonify, url_for, render_template, flash, request, session, g
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import redirect

from datetime import datetime

from how_about_this_day import db
from how_about_this_day.forms import userCreateForm, UserLoginForm
from how_about_this_day.models import User
import functools

bp = Blueprint('auth', __name__, url_prefix='/auth')



@bp.route('/signup/', methods=('GET', 'POST'))
def signup():
    form = userCreateForm()
    if request.method == 'POST' and form.validate_on_submit():
        ip = request.remote_addr
        date = datetime.now()
        user = User.query.filter_by(username=form.username.data).first()
        if not user:
            user = User(username=form.username.data, password=generate_password_hash(form.password1.data), email=form.email.data)
            db.session.add(user)
            db.session.commit()
            print(date)
            print("[로그]","회원가입에 성공했습니다. (IP:",str(ip)+")")
            return jsonify({"signup" : "success"})
        else:
            print("[로그]", "회원가입에 실패했습니다. (IP:",str(ip)+")")
            return jsonify({"signup_error" : "user already exists"})
    return jsonify({"access" : "auth/signup"})

@bp.route('/login/', methods=('GET', 'POST'))
def login():
    form = UserLoginForm()
    if request.method == 'POST':
        ip = request.remote_addr
        date = datetime.now()
        user = User.query.filter_by(username=form.username.data).first()
        if not user:
            print(date)
            print("[로그]", "로그인 시도 중 아이디를 잘못 입력했습니다. (IP:",str(ip)+")")
            return jsonify({"login_error" : "user does not exists"})
        #elif not check_password_hash(user.password, form.password.data):
        elif not user.password == form.password.data:
            print(date)
            print("[로그]", "로그인 시도 중 비밀번호를 잘못 입력했습니다. (IP:",str(ip)+")")
            return jsonify({"login_error" : "the password is incorrect"})
        else:
            session.clear()
            session['user_id'] = user.id
            print(date)
            print("[로그]", "로그인에 성공했습니다. (IP:",str(ip)+")")
            return jsonify({"login" : "success"})
    return jsonify({"access" : "auth/login"})

@bp.before_app_request
def load_logged_in_user():
    user_id = session.get('user_id')
    if user_id is None:
        g.user = None
    else:
        g.user = User.query.get(id = user_id)

@bp.route('/logout/')
def logout():
    ip = request.remote_addr
    date = datetime.now()
    print(date)
    print("[로그]", "로그아웃 했습니다. (IP:",str(ip)+")")
    session.clear()
    return jsonify({"logout" : "success"})

def login_required(view):
    @functools.wraps(view)
    def wrapped_view(**kwargs):
        if g.user is None:
            return jsonify({"error" : "login required"})
        return view(**kwargs)
    return wrapped_view
