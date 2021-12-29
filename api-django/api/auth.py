from django.core.serializers import serialize
from django.core.exceptions import *
from django.contrib.auth import authenticate
from rest_framework.authtoken.models import Token
# from rest_framework.authtoken.views import ObtainAuthToken
from django.shortcuts import get_object_or_404

from rest_framework.decorators import api_view
from rest_framework.response import Response

from .models import *
from .serializers import *
from .utils import base64_file
from django.db.models import Q

@api_view(["POST"])
def login(request):
	user = User.objects.filter(Q(username= request.data['email']) | Q(email= request.data['email']))

	if not user.exists():
		return Response({
			'error': "The username or the password is incorrect"
		},400)
	
	user = user.first()
	user = authenticate(username= user.username, password= request.data['password'])
	
	if not user:
		return Response({
			'error': "The username or the password is incorrect"
		},400)

	return Response(CustomerSerializer(Customer.objects.filter(user= user).get()).data)

@api_view(["POST"])
def register(request):
	u = UserSerializer(data=request.data)

	if not u.is_valid():
		return Response({
			'errors': u.errors
		},400)

	if User.objects.filter(email = u.data['email']).count() == 1:
		return Response({
			"error": 'The email is already taken'
		},400)
	elif User.objects.filter(username = u.data['username']).count() == 1:
		return Response({
			"error": 'The username is already taken'
		},400)

	data = {
		'user': u.data,
		'type': request.data['type'],
		'password':request.data['password']
	}

	c = CustomerSerializer.create(CustomerSerializer, data)
	c.save()
	c.user.set_password(request.data['password'])
	c.user.save()

	return Response(
		{
			'message': "The user has been succesfully created",
			'data': CustomerSerializer(c).data
		},
		content_type="application/json"
	)

@api_view(["POST"])
def create_agence(request):

	data = request.data.copy()
	
	a = AgencySerializer(data= data)

	if not a.is_valid():
		return Response({
			'errors': a.errors
		},400)

	data = a.data

	data['user'] = Customer.objects.filter(pk= request.data['user_id']).get()
	
	if 'image' in a.data.keys():
		data['image'] = base64_file(data= a.data['image'], name= a.data['name'])

	a = Agency(**data)
	a.save()

	alert = Alert.objects.create(
		receiver = Admin.objects.first().user,
		message = data['user'].user.email +" vient d'enreigistrer une nouvelle agence. Merci de la valider ou la supprimer.",
		view = False
	)

	return Response({
		'data': AgencySerializer(a).data,
		"message": "The agence has been created"
	})
