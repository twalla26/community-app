from flask import Flask
from flask_migrate import Migrate
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import MetaData
from flask_admin import Admin
from flask_admin.contrib.sqla import ModelView


import config


db = SQLAlchemy()
migrate = Migrate()

naming_convention = { # SQLite 데이터베이스를 플라스크 ORM에서 정상으로 사용하기 위한 것
    "ix": "ix_%(column_0_label)s",
    "up": "up_%(table_name)s_%(column_0_name)s",
    "ck": "ck_%(table_name)s_%(column_0_name)s",
    "fk": "fk_%(table_name)s_%(column_0_name)s_%(referred_table_name)s",
    "pk": "pk_%(table_name)s"
}

db = SQLAlchemy(metadata=MetaData(naming_convention=naming_convention))
migrate = Migrate()


def create_app():
    app = Flask(__name__)
    app.config.from_object(config)
    # 한글 인코딩
    app.config['JSON_AS_ASCII'] = False


    # ORM
    db.init_app(app)

    if app.config['SQLALCHEMY_DATABASE_URI'].startswith("sqlite"):
        migrate.init_app(app, db, render_as_batch=True)
    else:
        migrate.init_app(app, db)
    from . import models

    # 블루프린트
    from .views import auth_views, study_plan_views, userInfo_views, meal_plan_views, exercise_plan_views
    app.register_blueprint(auth_views.bp)
    app.register_blueprint(study_plan_views.bp)
    app.register_blueprint(userInfo_views.bp)
    app.register_blueprint(meal_plan_views.bp)
    app.register_blueprint(exercise_plan_views.bp)


    # 관리자 페이지 설정
    # bootswatch theme 설정. bootswatch: free theme for bootstrap
    app.config['FLASK_ADMIN_SWATCH'] = 'cerulean'
    from .models import User, StudyPlan, StudyPlanComment, MealPlan, MealPlanComment, ExercisePlan, ExercisePlanComment
    
    if app.debug: #admin page는 사용자들에게 보여지는 페이지가 아니기 때문에 debug mode일 때만 적용되도록 설정
        admin = Admin(app, name='flask admin', template_mode='bootstrap3')
        admin.add_view(ModelView(StudyPlan, db.session))
        admin.add_view(ModelView(User, db.session))
        admin.add_view(ModelView(StudyPlanComment, db.session))
        admin.add_view(ModelView(MealPlan, db.session))
        admin.add_view(ModelView(ExercisePlan, db.session))
        admin.add_view(ModelView(ExercisePlanComment, db.session))
        admin.add_view(ModelView(MealPlanComment, db.session))

    return app

