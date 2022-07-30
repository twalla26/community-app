from email.utils import format_datetime
from flask import Flask
from flask_migrate import Migrate
from flask_sqlalchemy import SQLAlchemy
from pip import main
from sqlalchemy import MetaData
from flask_admin import Admin
from flask_admin.contrib.sqla import ModelView

import config

db = SQLAlchemy()
migrate = Migrate()

naming_convention = {
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

    # ORM
    db.init_app(app)

    if app.config['SQLALCHEMY_DATABASE_URI'].startswith("sqlite"):
        migrate.init_app(app, db, render_as_batch=True)
    else:
        migrate.init_app(app, db)
    from . import models

    # 블루프린트
    from .views import main_views, auth_views
    app.register_blueprint(main_views.bp)
    app.register_blueprint(auth_views.bp)

    # 필터
    from .filter import format_datetime
    app.jinja_env.filters['datetime'] = format_datetime

    # 관리자 페이지 설정
    # bootswatch theme 설정. bootswatch: free theme for bootstrap
    app.config['FLASK_ADMIN_SWATCH'] = 'cerulean'
    from .models import Plan, User, test
    
    if app.debug: #admin page는 사용자들에게 보여지는 페이지가 아니기 때문에 debug mode일 때만 적용되도록 설정
        admin = Admin(app, name='flask admin', template_mode='bootstrap3')
        admin.add_view(ModelView(Plan, db.session))
        admin.add_view(ModelView(User, db.session))
        admin.add_view(ModelView(test, db.session)) 

    return app

