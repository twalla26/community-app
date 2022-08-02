import json
from flask import Blueprint, jsonify, url_for, render_template, flash, request, session, g
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import redirect

from datetime import datetime

from how_about_this_day import db
from how_about_this_day.forms import AnswerForm, PlanCreateForm, UserCreateForm, UserLoginForm
from how_about_this_day.models import Plan, User
import functools

from how_about_this_day.views.auth_views import login_required

bp = Blueprint('plan', __name__, url_prefix='/plan')

@bp.route('/list/')
def _list():
    page = request.args.get('page', type=int, default=1) # http://localhost:5000/question/list/?page=5
    plan_list = Plan.query.order_by(Plan.create_date.desc())
    plan_list = plan_list.paginate(page, per_page=10)
    return jsonify({"access" : "plan/list", "plan_list" : plan_list})


@bp.route('detail/<int:plan_id>/')
def detail(plan_id):
    form = AnswerForm()
    plan = Plan.query.get_or_404(plan_id)
    return jsonify({"plan" : plan, "form" : form})

@bp.route('/create/', methods=('GET', 'POST'))
@login_required
def create_plan():
    form = PlanCreateForm()
    if request.methods == 'POST' and form.validate_on_submit():
        ip = request.remote_addr
        date = datetime.now()
        plan = Plan(subject=form.subject.data, content=form.content.data, create_date = datetime.now(), user=g.user)
        db.session.add(plan)
        db.session.commmit()
        print(date)
        print("[로그]", "새로운 계획을 업로드했습니다. (IP:",str(ip)+")")
        return jsonify({"planCreate" : "success"})
    return jsonify({"access" : "plan/create"})
        
@bp.route('/modify/<int:plan_id>', methods=('GET', 'POST'))
@login_required
def modify(plan_id):
    plan = Plan.query.get_or_404(plan_id)
    if g.user != plan.user: # 작성자가 아닌 다른 사용자가 글을 수정하려 할 때
        return jsonify({"error" : "You are not allowed to modify"})
    if request.methods == 'POST': # POST 요청
        form = PlanCreateForm() # 사용자가 수정한 내용을 form 변수에 저장
        if form.validate_on_submit(): # 서식에 맞으면,
            form.populate_obj(plan) # form 변수에 들어 있는 데이터(화면에서 입력한 데이터)를 plan 객체에 업데이트 하는 역할.
            plan.modify_date = datetime.now() # 수정일시 저장
            ip = request.remote_addr
            date = datetime.now()
            db.session.commit() # db에 저장
            print(date)
            print("[로그]", "계획을 수정했습니다. (IP:",str(ip)+")")
            return jsonify({"access" : "plan/detail", "plan_id" : plan_id}) 
    else: # GET 요청
        form = PlanCreateForm(obj=plan) # obj 매개변수에 데이터베이스에서 조회한 데이터를 전달하여 폼을 생성
    return jsonify({"access" : "question/questionForm", "form" : form})






