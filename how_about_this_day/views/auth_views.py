import json
from flask import Blueprint, jsonify, url_for, render_template, flash, request, session, g
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import redirect

from datetime import datetime

from how_about_this_day import db
from how_about_this_day.forms import UserCreateForm, UserLoginForm
from how_about_this_day.models import User
import functools

bp = Blueprint('auth', __name__, url_prefix='/auth')


@bp.route('/signup/', methods=('GET', 'POST'))# methods=('GET', 'POST'))
def signup(): # 회원가입 함수
     
    form = UserCreateForm() # 전달 받은 데이터를 form 변수에 저장
    if request.method == 'POST': #and form.validate_on_submit(): # POST 요청
        ip = request.remote_addr # 사용자 ip 주소 저장
        date = datetime.now() # 현재 시간 저장 -> 로그 기록용
        user = User.query.filter_by(username=form.username.data).first() # 회원가입하려는 사용자가 기존에 있는 사용자인지 확인
        if not user: # 기존 데이터베이스에 없는 사용자가 회원가입 시도 -> 회원가입 가능
            user = User(username=form.username.data, password=generate_password_hash(form.password1.data), email=form.email.data) # form에 입력 받은 데이터(유저네임, 비밀번호, 이메일)로 신규 유저 생성
            db.session.add(user) # 데이터베이스에 신규 유저 추가
            db.session.commit() # 데이터베이스 저장
            print(date)
            print("[로그]","회원가입에 성공했습니다. (IP:",str(ip)+")") # 로그 기록
            return jsonify({"signup" : "success"}) # 클라에 회원가입 성공 알리기
        else: # 기존에 있는 사용자가 회원가입 시도 -> 회원가입 불가능
            print(date)
            print("[로그]", "회원가입에 실패했습니다. (IP:",str(ip)+")") # 로그 기록
            return jsonify({"signup_error" : "user already exists"}) # 클라에 회원가입 오류와 오류 원인 알리기
    return jsonify({"access" : "auth/signup"}) # GET 요청 -> 클라에 회원가입 창으로 가야함 알리기
    
  
 

@bp.route('/login/', methods=('GET', 'POST'))
def login(): # 로그인 함수
    form = UserLoginForm() # 전달 받은 데이터를 from 변수에 저장
    if request.method == 'POST': # POST 요청
        ip = request.remote_addr # 사용자 ip 저장
        date = datetime.now() # 현재 시간 저장 -> 로그 기록용
        user = User.query.filter_by(username=form.username.data).first() # 로그인하려는 사용자가 기존에 있는 사용자인지 확인
        if not user: # 기존에 없는 사용자일 경우 -> 로그인 불가
            print(date)
            print("[로그]", "로그인 시도 중 아이디를 잘못 입력했습니다. (IP:",str(ip)+")") # 로그 기록
            return jsonify({"login_error" : "user does not exists"}) # 클라에 로그인 오류로 존재하지 않는 사용자임을 알림
        elif not check_password_hash(user.password, form.password.data): # 비밀번호가 틀렸을 경우 -> 로그인 불가
            print(date)
            print("[로그]", "로그인 시도 중 비밀번호를 잘못 입력했습니다. (IP:",str(ip)+")") # 로그 기록
            return jsonify({"login_error" : "the password is incorrect"}) # 클라에 로그인 오류로 비밀번호가 틀렸음을 알림
        else: # 아이디, 비밀번호 둘다 제대로 입력했을 경우
            session.clear() # 세션을 초기화한 뒤,
            session['user_id'] = user.id # 세션에 현재 로그인한 유저의 아이디 저장
            print(date)
            print("[로그]", "로그인에 성공했습니다. (IP:",str(ip)+")") # 로그 기록
            return jsonify({"login" : "success"}) # 클라에 로그인 성공했음을 알림
    return jsonify({"access" : "auth/login"}) # GET 요청 -> 클라에 로그인 창으로 가도록 요청

@bp.before_app_request
def load_logged_in_user():
    user_id = session.get('user_id')
    if user_id is None:
        g.user = None
    else:
        g.user = User.query.filter_by(id = user_id).first()

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

@bp.route('/test1/')
@login_required
def test1():
    return jsonify({"hi":"hi"})