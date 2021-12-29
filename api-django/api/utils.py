from .models import *
from rest_framework.response import Response
import base64
from django.contrib.auth.models import User
from django.db.models import Q

# ********files
def base64_file(data, name=None):
	_format, _img_str = data.split(';base64,')
	_name, ext = _format.split('/')
	if not name:
		name = _name.split(":")[-1]
	return ContentFile(base64.b64decode(_img_str), name='{}.{}'.format(name, ext))

# ********what is user's type
def is_customer(data):
    if 'email' not in data.keys():
        return Response({
            'errors': 'the email field is missing'
        },400)

    user = Customer.objects.filter(user__email= data['email'])    

    if not user.exists():
        return Response({
                'errors': 'This user does not exist. Please create an account'
            },status=403)

    user = user.get()

    return user

def is_admin(data):
    if 'email' not in data.keys():
        return Response({
            'errors': 'the email field is missing'
        },400)

    user = Customer.objects.filter(user__email= data['email'], type='AD')    

    if not user.exists():
        return Response({
                'errors': 'Your are not an admin'
            },status=403)

    user = user.get()

    return Admin.objects.filter(user__id= user.id).get()

def is_agency(data):
    if 'email' not in data.keys():
        return Response({
            'errors': 'the email field is missing'
        },400)

    user = Customer.objects.filter(user__email= data['email'], type='AG')    

    if not user.exists():
        return Response({
                'errors': 'Your are not an agency'
            },status=403)

    user = user.get()

    return Agency.objects.filter(user__id= user.id).get()

def is_admin_or_agency(data):
    if 'email' not in data.keys():
        return Response({
            'errors': 'the email field is missing'
        },400)

    user = Customer.objects.filter(
    	Q(user__email= data['email'], type='AD')|
    	Q(user__email= data['email'], type='AG')
    )

    if not user.exists():
        return Response({
                'errors': 'Your are not an admin either an agency'
            },status=403)

    user = user.get()

def is_passenger(data):
    if 'email' not in data.keys():
        return Response({
            'errors': 'the email field is missing'
        },400)

    user = Customer.objects.filter(user__email= data['email'], type='PA')    

    if not user.exists():
        return Response({
                'errors': 'Your are not an passenger'
            },status=403)

    user = user.get()

    return Passenger.objects.filter(user__id= user.id).get()