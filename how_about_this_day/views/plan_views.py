import json
from flask import Blueprint, jsonify, url_for, render_template, flash, request, session, g
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import redirect

from datetime import datetime

from how_about_this_day import db
from how_about_this_day.forms import AnswerForm, PlanCreateForm, UserCreateForm, UserLoginForm
from how_about_this_day.models import Plan, User
import functools


bp = Blueprint('plan', __name__, url_prefix='/plan')

@bp.route('/list/') # 약속 목록 페이지
def _list(): 
    page = request.args.get('page', type=int, default=1) # http://localhost:5000/question/list/?page=5
    plan_list = Plan.query.order_by(Plan.create_date.desc()) # 약속 목록을 작성일시 기준 역순으로 나열 -> plan_list에 저장
    plan_list = plan_list.paginate(page, per_page=10) # 한 페이지 당 10개의 약속들이 나옴.
    return jsonify({"plan_list" : plan_list}) # 클라에 약속 목록 전송


@bp.route('detail/<int:plan_id>/') # 약속 상세 페이지
def detail(plan_id): 
    plan = Plan.query.get_or_404(plan_id) # plan_id에 해당하는 약속을 plan에 저장
    comments = plan.comment_set # plan_id에 해당하는 약속의 답변들을 answers에 저장
    return jsonify({"plan" : plan, "comments" : comments}) # 클라에 약속 데이터와 답변 데이터 전송

@bp.route('/create/', methods=('GET', 'POST')) # 약속 작성 페이지
def create_plan(): # 약속 작성 함수 -> 로그인이 필요한 기능
    if g.user == None: # 사용자가 로그아웃 상태
        return jsonify({"error" : "login required"}) # 클라에 로그인 요청
    form = PlanCreateForm() # 입력한 내용을 form으로 받음
    if request.methods == 'POST': # POST 요청
        ip = request.remote_addr # 사용자 ip 저장
        date = datetime.now() # 현재 시각 저장
        plan = Plan(subject=form.subject.data, content=form.content.data, create_date = datetime.now(), user=g.user) # 입력된 약속 내용 plan에 저장
        db.session.add(plan) # db에 데이터 저장
        db.session.commmit()
        print(date)
        print("[로그]", "새로운 계획을 업로드했습니다. (IP:",str(ip)+")") # 로그 기록
        return jsonify({"planCreate" : "success"}) # 클라에 요청 성공 알림
        
@bp.route('/modify/<int:plan_id>', methods=('GET', 'POST')) # 약속 수정 페이지
def modify(plan_id): # 약속 수정 함수 -> 로그인이 필요한 기능 + 글 작성자가 본인이어야 함
    if g.user == None: # 로그아웃 상태
        return jsonify({"error" : "login required"}) # 클라에 로그인 요청
    plan = Plan.query.get_or_404(plan_id)
    if g.user != plan.user: # 작성자가 아닌 다른 사용자가 글을 수정하려 할 때
        return jsonify({"error" : "You are not allowed to modify"}) # 수정 권한이 없음을 알림
    if request.methods == 'POST': # POST 요청 (수정 권한 있음)
        form = PlanCreateForm() # 사용자가 수정한 내용을 form 변수에 저장
        form.populate_obj(plan) # form 변수에 들어 있는 데이터(화면에서 입력한 데이터)를 plan 객체에 업데이트 하는 역할.
        plan.modify_date = datetime.now() # 수정일시 저장
        ip = request.remote_addr # 사용자 ip 저장
        date = datetime.now()
        db.session.commit() # db에 저장
        print(date)
        print("[로그]", "계획을 수정했습니다. (IP:",str(ip)+")") # 로그 기록
        return jsonify({"plan_id" : plan_id}) # 클라가 약속 내용을 수정한 후 수정 완료된 글에 다시 들어갈 수 있도록 plan_id를 전달.
    else: # GET 요청
        form = PlanCreateForm(obj=plan) # obj 매개변수에 데이터베이스에서 조회한 데이터를 전달하여 폼을 생성
    return jsonify({"form" : form}) # 클라가 글을 수정할 수 있도록 기존에 썼던 글 form을 전달


@bp.route('/delete/<int:plan_id>') # 약속 삭제 페이지
def delete(plan_id): # 약속 삭제 함수
    if g.user == None: # 사용자가 로그아웃 상태
        return jsonify({"error" : "login required"}) # 클라에 로그인 요청
    plan = Plan.query.get_or_404(plan_id)
    if g.user != plan.user: # 작성자가 아닌 다른 사용자가 글을 삭제하려 할 떄
        return jsonify({"error" : "You are not allowed to delete"}) # 클라에 수정 권한이 없음 전송
    db.session.delete(plan) # db에 글 삭제
    db.session.commit() # db에 저장
    return jsonify({"delete" : "success"}) # 클라에 삭제 완료 전송


