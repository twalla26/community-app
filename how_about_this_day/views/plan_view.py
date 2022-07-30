import json
from flask import Blueprint, jsonify, url_for, render_template, flash, request, session, g
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import redirect

from datetime import datetime

from how_about_this_day import db
from how_about_this_day.forms import PlanCreateForm, UserCreateForm, UserLoginForm
from how_about_this_day.models import Plan, User
import functools

from how_about_this_day.views.auth_views import login_required

bp = Blueprint('auth', __name__, url_prefix='/plan')

@bp.route('/list/')
def _list():
    plan_list = Plan.query.order_by(Plan.create_date.desc())
    return jsonify({"access" : "plan/list"}, {"plan_list" : plan_list})

'''
@bp.route('detail/<int:plan_id>/')
def detail(plan_id):
    plan = Plan.query.get_or_404(plan_id)
'''

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
        






