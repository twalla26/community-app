from flask_wtf import FlaskForm
from wtforms import StringField, TextAreaField, PasswordField, EmailField

 
class PlanCreateForm(FlaskForm):
    subject = StringField()
    content = TextAreaField()

class UserCreateForm(FlaskForm):
    username = StringField()
    password1 = PasswordField()
    password2 = PasswordField()
    email = EmailField()

class UserLoginForm(FlaskForm):
    username = StringField()
    password = PasswordField()

class CommentForm(FlaskForm):
    content = TextAreaField()

class CheckDupForm(FlaskForm):
    username = StringField()
 