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



@bp.route('/sign_up/checkDup', methods=['POST'])
def check_dup(): # 아이디 중복 확인 함수
    username_receive = request.form['username'] # 요청으로 들어온 username을 username_receive에 저장
    exists = bool(db.user.find_one({"username": username_receive})) # 데이터베이스에서 클라에서 받은 username을 찾아서 있으면 True, 없으면 False 값으로 exists에 저장
    return jsonify({'exists': exists}) # json형식으로 존재값에 True or False 값으로 반환
    


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

@bp.before_app_request # 모든 라우팅 함수가 실행되기 전에 실행되도록 함. -> 어떤 결과값을 반환하지 않고 로그인 유무 상태만 저장.
def load_logged_in_user(): # 사용자가 로그인한 상태인지 아닌지 확인하는 함수
    user_id = session.get('user_id') # 사용자가 로그인 했을 때 세션에 저장된 아이디값을 user_id에 저장
    if user_id is None: # 만약 사용자가 로그인하지 않은 상태라면 user_id 값이 None값일 것.
        g.user = None # g.user에 None 값을 저장해서 추후 g.user만으로도 로그인 유무를 확인할 수 있게 함.
    else: # user_id가 None값이 아니라면 사용자는 로그인한 상태.
        g.user = User.query.filter_by(id = user_id).first() # 세션에 저장된 아이디값을 이용하여 User 모델에서 사용자를 찾은 후 g.user에 사용자 정보 저장.

@bp.route('/logout/') # GET 요청
def logout(): # 로그아웃 함수
    ip = request.remote_addr # 사용자 ip 저장
    date = datetime.now() # 현재 시각 저장
    print(date)
    print("[로그]", "로그아웃 했습니다. (IP:",str(ip)+")") # 로그 기록
    session.clear() # 세션 초기화
    return jsonify({"logout" : "success"}) # 클라에 로그아웃 했음을 알려줌.

def login_required(): # 로그인이 필요한 페이지에 접근하기 전, 로그인 상태인지 확인하는 함수
    if g.user is None: # 사용자가 로그아웃 상태라면
        return jsonify({"error" : "login required"}) # 클라에 로그인이 필요하다고 알려줌
    return jsonify({"exists" : True}) # 사용자가 로그인 상태라면 요청한 페이지를 보여줌
  

@bp.route('/test1/')
@login_required
def test1():
    return jsonify({"hi":"hi"})