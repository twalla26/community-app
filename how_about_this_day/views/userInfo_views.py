from flask import Blueprint, jsonify, session, request
import json

from how_about_this_day.models import User, StudyPlan, ExercisePlan, MealPlan


bp = Blueprint('userInfo', __name__, url_prefix='/userInfo')

@bp.route('/') # 사용자 정보 페이지
def userInfo(): 
    user_id = session.get('user_id') # 사용자가 로그인 했을 때 세션에 저장된 아이디값을 user_id에 저장
    user = User.query.get(user_id)
    if user == None:
        return jsonify({"error" : "login required"})
    else:
        username = user.username
        email = user.email
        return jsonify({"username" : username, "email" : email})


@bp.route('/my_plan_list/') # 사용자가 모집한 스터디 약속 페이지
def my_plan_list():

    page = request.args.get('page', type=int, default=1) # http://localhost:5000/question/list/?page=5
    user_id = session.get('user_id')
    user = User.query.get(user_id)

    my_study_plan_list = StudyPlan.query.filter_by(user=user).order_by(StudyPlan.create_date.desc())
    my_study_plan_list = my_study_plan_list.paginate(page, per_page=10) # 한 페이지 당 10개의 약속들이 나옴.

    my_exercise_plan_list = ExercisePlan.query.filter_by(user=user).order_by(ExercisePlan.create_date.desc())
    my_exercise_plan_list = my_exercise_plan_list.paginate(page, per_page=10) # 한 페이지 당 10개의 약속들이 나옴.

    my_meal_plan_list = MealPlan.query.filter_by(user=user).order_by(MealPlan.create_date.desc())
    my_meal_plan_list = my_meal_plan_list.paginate(page, per_page=10) # 한 페이지 당 10개의 약속들이 나옴.

    my_study_plan = []
    my_exercise_plan = []
    my_meal_plan = []

    for plan in my_study_plan_list.items:
            
        if plan.modify_date:
            modified = "True"
        else:
            modified = "False"
        
        data = {"id" : plan.id, 
                "subject" : plan.subject,
                "username" : plan.user.username,
                "modified" : modified,
                "create_date" : plan.create_date.strftime('%Y년 %m월 %d일 %H:%M')}
        data = json.dumps(data, ensure_ascii=False) # 딕셔너리 자료형을 json 문자열로 만듦
        data = json.loads(data) # json 문자열을 딕셔너리로 역변환
        my_study_plan.append(data)
    for plan in my_exercise_plan_list.items:
            
        if plan.modify_date:
            modified = "True"
        else:
            modified = "False"
        
        data = {"id" : plan.id, 
                "subject" : plan.subject,
                "username" : plan.user.username,
                "modified" : modified,
                "create_date" : plan.create_date.strftime('%Y년 %m월 %d일 %H:%M')}
        data = json.dumps(data, ensure_ascii=False) # 딕셔너리 자료형을 json 문자열로 만듦
        data = json.loads(data) # json 문자열을 딕셔너리로 역변환
        my_exercise_plan.append(data)
    for plan in my_meal_plan_list.items:
            
        if plan.modify_date:
            modified = "True"
        else:
            modified = "False"
        
        data = {"id" : plan.id, 
                "subject" : plan.subject,
                "username" : plan.user.username,
                "modified" : modified,
                "create_date" : plan.create_date.strftime('%Y년 %m월 %d일 %H:%M')}
        data = json.dumps(data, ensure_ascii=False) # 딕셔너리 자료형을 json 문자열로 만듦
        data = json.loads(data) # json 문자열을 딕셔너리로 역변환
        my_meal_plan.append(data)

    my_plan_list = []
    my_plan_list.extend(my_study_plan)
    my_plan_list.extend(my_exercise_plan)
    my_plan_list.extend(my_meal_plan)
    return jsonify({"my_plan_list" : my_plan_list})



@bp.route('/my_comment_list/')
def my_comment_list():
    user_id = session.get('user_id')
    user = User.query.get(user_id)
    user_study_comment_list = user.study_plan_comment_set
    user_exercise_comment_list = user.exercise_plan_comment_set
    user_meal_comment_list = user.meal_plan_comment_set
    userStudyCommentList = []
    userExerciseCommentList = []
    userMealCommentList = []
    for comment in user_study_comment_list:
        comment = { "user" : comment.user.username, 
                    "content" : comment.content,
                    "create_date" : comment.create_date.strftime('%Y년 %m월 %d일 %H:%M'),
                    "question_id" : comment.study_plan_id}
                    

        comment = json.dumps(comment, ensure_ascii=False)
        comment = json.loads(comment)
        user_study_comment_list.append(comment) # 댓글 리스트 json 화

    for comment in user_exercise_comment_list:
        comment = { "user" : comment.user.username, 
                    "content" : comment.content,
                    "create_date" : comment.create_date.strftime('%Y년 %m월 %d일 %H:%M'),
                    "question_id" : comment.exercise_plan_id}

        comment = json.dumps(comment, ensure_ascii=False)
        comment = json.loads(comment)
        userExerciseCommentList.append(comment) # 댓글 리스트 json 화
    
    for comment in user_meal_comment_list:
        comment = { "user" : comment.user.username, 
                    "content" : comment.content,
                    "create_date" : comment.create_date.strftime('%Y년 %m월 %d일 %H:%M'),
                    "question_id" : comment.meal_plan_id}

        comment = json.dumps(comment, ensure_ascii=False)
        comment = json.loads(comment)
        userMealCommentList.append(comment) # 댓글 리스트 json 화

    return jsonify({"user_study_plan_comment_set" : userStudyCommentList, 
                    "user_exercise_plan_comment_set" : userExerciseCommentList,
                    "user_meal_plan_comment_set" : userMealCommentList})
    

