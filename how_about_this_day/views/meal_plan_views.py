import json
from flask import Blueprint, jsonify, request, g
from datetime import datetime

from how_about_this_day import db
from how_about_this_day.forms import PlanCreateForm, CommentForm
from how_about_this_day.models import MealPlan, MealPlanComment


bp = Blueprint('meal_plan', __name__, url_prefix='/meal_plan')

@bp.route('/list/') # 약속 목록
def _list(): 
    page = request.args.get('page', type=int, default=1) # http://localhost:5000/question/list/?page=5
    meal_plan_list = MealPlan.query.order_by(MealPlan.create_date.desc()) # 약속 목록을 작성일시 기준 역순으로 나열 -> plan_list에 저장
    meal_plan_list = meal_plan_list.paginate(page, per_page=10) # 한 페이지 당 10개의 약속들이 나옴.
    tempDict = []
    for plan in meal_plan_list.items:

        if g.user == plan.user:
            plan_writer = "True"
        else:
            plan_writer = "False"
            
        if plan.modify_date:
            modified = "True"
        else:
            modified = "False"
        
        data = {"id" : plan.id, 
                "subject" : plan.subject,
                "username" : plan.user.username,
                "plan_writer" : plan_writer,
                "modified" : modified,
                "create_date" : plan.create_date.strftime('%Y년 %m월 %d일 %H:%M')}
        data = json.dumps(data, ensure_ascii=False) # 딕셔너리 자료형을 json 문자열로 만듦
        data = json.loads(data) # json 문자열을 딕셔너리로 역변환
        tempDict.append(data)
    return jsonify({"meal_plan_list" : tempDict})


@bp.route('/detail/<int:plan_id>/', methods=('GET', 'POST')) # 약속 상세
def detail(plan_id):
    form = CommentForm()
    meal_plan = MealPlan.query.get(plan_id) # plan_id에 해당하는 약속을 plan에 저장
    comments = meal_plan.meal_plan_comment_set # plan_id에 해당하는 약속의 답변들을 comments에 저장

    if request.method == 'POST': # 댓글 작성 후 저장 요청
        comment = MealPlanComment(meal_plan=meal_plan, content=form.content.data, create_date=datetime.now(), user=g.user)
        db.session.add(comment)
        db.session.commit()
        ip = request.remote_addr
        date = datetime.now()
        print(date)
        print("[로그]", "댓글을 작성했습니다. (IP:",str(ip)+")") # 로그 기록
        return jsonify({"comment" : "success"})

    else: # GET 요청 -> 약속 상세 페이지
        commentList = [] # 글의 모든 댓글 리스트
        for comment in comments: # 페이지 내의 댓글 들 중 작성자가 작성한 댓글 탐색

            
            # 글에 달린 댓글들 리스트
            comment = { "id" : comment.id,
                        "user" : comment.user.username, 
                        "content" : comment.content,
                        "create_date" : comment.create_date.strftime('%Y년 %m월 %d일 %H:%M')}

            comment = json.dumps(comment, ensure_ascii=False)
            comment = json.loads(comment)
            commentList.append(comment) # 댓글 리스트 json화

        if meal_plan.modify_date:
            return jsonify({"id" : meal_plan.id, 
                            "user" : meal_plan.user.username,
                            "subject" : meal_plan.subject, 
                            "content" : meal_plan.content,
                            "create_date" : meal_plan.create_date.strftime('%Y년 %m월 %d일 %H:%M'),
                            "modify_date" : meal_plan.modify_date.strftime('%Y년 %m월 %d일 %H:%M'),
                            "commentList" : commentList}) # GET요청: 클라에 약속 데이터와 답변 데이터 전송

        else:
            return jsonify({"id" : meal_plan.id, 
                            "user" : meal_plan.user.username,
                            "subject" : meal_plan.subject, 
                            "content" : meal_plan.content,
                            "create_date" : meal_plan.create_date.strftime('%Y년 %m월 %d일 %H:%M'),
                            "commentList" : commentList}) # GET요청: 클라에 약속 데이터와 답변 데이터 전송

        

@bp.route('/comment_delete/<int:comment_id>/') # 댓글 삭제
def delete_comment(comment_id):
    comment = MealPlanComment.query.get(comment_id)
    if g.user != comment.user:
        return jsonify({"error" : "No Permission."})
    else:
        db.session.delete(comment)
        db.session.commit()
        ip = request.remote_addr
        date = datetime.now()
        print(date)
        print("[로그]", "댓글을 삭제했습니다. (IP:",str(ip)+")") # 로그 기록
        return jsonify({"result" : "success"})


@bp.route('/create/', methods=('GET', 'POST')) # 약속 작성
def create_plan(): # 약속 작성 함수 -> 로그인이 필요한 기능
    form = PlanCreateForm() # 입력한 내용을 form으로 받음
    if request.method == 'POST': # POST 요청
        ip = request.remote_addr # 사용자 ip 저장
        date = datetime.now() # 현재 시각 저장
        plan = MealPlan(subject=form.subject.data, content=form.content.data, create_date = datetime.now(), user=g.user) # 입력된 약속 내용 plan에 저장
        db.session.add(plan) # db에 데이터 저장
        db.session.commit()
        print(date)
        print("[로그]", "새로운 계획을 업로드했습니다. (IP:",str(ip)+")") # 로그 기록
        return jsonify({"planCreate" : "success"}) # 클라에 요청 성공 알림
        

@bp.route('/modify/<int:plan_id>/', methods=('GET', 'POST')) # 약속 수정
def modify(plan_id): # 약속 수정 함수 -> 로그인이 필요한 기능 + 글 작성자가 본인이어야 함
    plan = MealPlan.query.get(plan_id)
    if request.method == 'POST': # POST 요청 (수정 권한 있음)
        form = PlanCreateForm() # 사용자가 수정한 내용을 form 변수에 저장
        form.populate_obj(plan) # form 변수에 들어 있는 데이터(화면에서 입력한 데이터)를 plan 객체에 업데이트 하는 역할.
        plan.modify_date = datetime.now() # 수정일시 저장
        ip = request.remote_addr # 사용자 ip 저장
        date = datetime.now() 
        db.session.commit() # db에 저장
        print(date)
        print("[로그]", "계획을 수정했습니다. (IP:",str(ip)+")") # 로그 기록
        return jsonify({"modify" : "success"}) # 클라가 약속 내용을 수정한 후 수정 완료된 글에 다시 들어갈 수 있도록 plan_id를 전달.
    else: # GET 요청
        return jsonify({"subject" : plan.subject, "content" : plan.content}) # 클라가 글을 수정할 수 있도록 기존에 썼던 글


@bp.route('/delete/<int:plan_id>/') # 약속 삭제
def delete(plan_id): # 약속 삭제 함수
    ip = request.remote_addr
    date = datetime.now()
    plan = MealPlan.query.get(plan_id)
    db.session.delete(plan) # db에 글 삭제
    db.session.commit() # db에 저장
    print(date)
    print("[로그]", "계획을 삭제했습니다. (IP:",str(ip)+")") # 로그 기록
    return jsonify({"delete" : "success"}) # 클라에 삭제 완료 전송
