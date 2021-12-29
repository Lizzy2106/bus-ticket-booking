from .models import *

from rest_framework.decorators import api_view
from rest_framework.response import Response

from .utils import is_passenger

from io import BytesIO
from django.http import HttpResponse
from django.template.loader import get_template
from django.views import View
from xhtml2pdf import pisa
from datetime import datetime
# Create your views here.

@api_view(['GET'])
def apiOverview(request):
	VOID = 'V'
	FULL = 'F'
	PARTIAL = 'P'

	BUS_STATUSES = {
		VOID : 'Void',
		FULL : 'Full',
		PARTIAL : 'Partial'
	}
	message = ""
	for key in BUS_STATUSES:
		message += " {}: {}".format(key, BUS_STATUSES[key])

	CHOICES = {
		'PA': 'Passenger',
		'AG': 'Agency'
	}
	type_choice=""
	for key in CHOICES:
		type_choice+="{}: {} ".format(key, CHOICES[key])

	api_urls = {
		'register': {
			'uri': 'POST : api/auth/register',
			'params': [
				'email: user email(required)',
				'username: nomprenom (required)',
				'password: user password (required)',
				'type: {}(required)'.format(type_choice)
			],
			'response':{
			    "message": "The user has been succesfully created",
			    "data": {
			        "id": 2,
			        "user": {
			            "id": 2,
			            "email": "user2@gmail.com",
			            "username": "user2",
			            "date_joined": "2021-03-01T23:09:34.712757Z"
			        },
			        "type": "AG"
			    }
			},
			'notes': 'Always use data.id'
		},


		'List Alert':{
			'uri' : """PUT "api/alert" """,
			'params' : [
				'email: user email (required)'
			]
		},
		'Update Alert':{
			'uri' : """GET "api/alert/<int:pk>/" """,
			'params' : [
				'email: user email (required)'
			]
		},
		'Delete Alert':{
			'uri' :'DELETE api/alert/<int:pk>/',
			'params' : [
				'email: user email (required)',
			]
		},


		'Get message': {
			'uri': 'POST : api/message',
			'params': [
				'email: user email(required)',
			],
			'response':[
				{
					'sender':2,
					'receiver':1,
					'subject':'fffffffffffffff',
					'content':'fffffffffffffff',
					'date':'2021-03-01T23:09:34.712757Z'
				}
			]
		},


		'Send message':{
			'uri' : """POST "api/message/send/" """,
			'params' : [
				'email: user email(required)',
				'receiver: customer id (required)',
				'subject: message subject (required)',
				'content: text message body (required)',
			]
		},


		'List Politicy':{
			'uri' : """GET "api/politicy" """,
			'params' : [
				'email: the email of the agency (required)'
			]
		},
		'Create Politicy':{
			'uri' : """POST "api/politicy/" """,
			'params' : [
				'email: the email of the agency (required)',
				'title: the title of the politicy (required)',
				'cancel: bool to determine if its possible to cancel a travel (required)',
				'refund: remboursement (required)'
				'period: time during th politicy is valid (required)'
			]
		},
		'Get a Politicy':{
			'uri' : """GET "api/politicy/<int:pk>/" """,
			'params' : [
				'email: the email of the agency (required)',
			]
		},
		'Update Politicy':{
			'uri' : """PUT "api/politicy/<int:pk>/" """,
			'params' : [
				'email: the email of the agency (required)',
				'title: the title of the politicy (required)',
				'cancel: bool to determine if its possible to cancel a travel (required)',
				'refund: remboursement (required)'
				'period: time during th politicy is valid (required)'
			]
		},
		'Delete Politicy':'DELETE api/politicy/<int:pk>/',


		'List City':{
			'uri' : """GET "api/city" """,
			'params' : [
				'email: the email of the agency or admin (required)'
			]
		},
		'Create City':{
			'uri' : """POST "api/city/" """,
			'params' : [
				'email: the email of the agency (required)',
				'name: the name of the city (required)'
			]
		},
		'Get a City':{
			'uri' : """GET "api/city/<int:pk>" """,
			'params' : [
				'email: the email of the agency (required)',
			]
		},
		'Update City':{
			'uri' : """PUT "api/city/<int:pk>/" """,
			'params' : [
				'email: the email of the agency (required)',
				'name: the name of the city (required)'
			]
		},
		'Delete City':'DELETE api/city/<int:pk>/',


		'create agence': {
			'uri': 'POST : api/auth/agence',
			'params': [
				'user: id of the user (required)',
				'name: the name of the agency (required)',
				'notes: the notes of the agency (not required)',
				'description: the description of the agency (not required)',
				'image: agency image  (not required)'
			]
		},


		'List Bus':{
			'uri' : """GET "api/bus" """,
			'params' : [
				'email: the email of the agency (required)'
			]
		},
		'Create Bus':{
			'uri' : """POST "api/bus/" """,
			'params' : [
				'email: the email of the agency (required)',
				'places: the numbre of places in the bus (required)',
				'image: bus image  (not required)'
			]
		},
		'Get a Bus':{
			'uri' : """GET "api/bus/<int:pk>" """,
			'params' : [
				'email: the email of the agency (required)',
			]
		},
		'Update Bus':{
			'uri' : """PUT "api/bus/<int:pk>/" """,
			'params' : [
				'email: the email of the agency (required)',
				'places: the numbre of places in the bus (required)',
				'image: bus image  (not required)'
			]
		},
		'Delete Bus':'DELETE api/bus/<int:pk>/',

		
		'List Travel':{
			'uri' : """GET "api/travel" """,
			'params' : [
				'email: the email of the agency (required)'
			]
		},
		'Create Travel':{
			'uri' : """POST "api/travel/" """,
			'params' : [
				'email: the email of the agency (required)',
				'bus: the bus id for the travel (required)',
				'politicy: the politicy id (required)',
				'departure: char (required)',
				'departure_stop: char (required)',
				'departure_date: date of the departure (required)',
				'arrival: char (required)',
				'arrival_stop: char (required)',
				'arrival_date: date of the arrival (required)',
				'price: price of the ticket (required)',
				'status: {} (required)'.format(message),
				'do: bool tell if the travel has done or not (required)'
			]
		},
		'Get a Travel':{
			'uri' : """GET "api/travel/<int:pk>" """,
			'params' : [
				'email: the email of the agency (required)',
			]
		},
		'Update Travel':{
			'uri' : """PUT "api/travel/<int:pk>/" """,
			'params' : [
				'email: the email of the agency (required)',
				'bus: the bus id for the travel (required)',
				'politicy: the politicy id (required)',
				'departure: char (required)',
				'departure_stop: char (required)',
				'departure_date: date of the departure (required)',
				'arrival: char (required)',
				'arrival_stop: char (required)',
				'arrival_date: date of the arrival (required)',
				'price: price of the ticket (required)',
				'status: {} (required)'.format(message),
				'do: bool tell if the travel has done or not (required)'
			]
		},
		'Update Booking':{
			'uri' : """PUT "api/booking/<int:pk>/" """,
			'params' : [
				'email: the email of the passenger (required)',
				'travel: id for the booking (required)',
				'seats: number of seats int (required)',
			]
		},
		'make reservation && pay tickets':{
			'uri' : """POST "api/travel/make-reservation/" """,
			'params' : [
				'email: the email of the customer (required)',
				'user: the customer id (required)',
				'phone: phone number char (required)',
				'travel: the travel id (required)',
			]
		},
		'delete reservation':{
			'uri' : """POST "api/travel/delete-reservation/<int:pk>" """,
			'params' : [
				'email: the email of the customer (required)',
			]
		},
		'Delete Travel':'DELETE api/travel/<int:pk>/',

		'Research reservation':{
			'uri' : """GET "api/booking" """,
			'params' : [
				'at least one params required'
				'departure: city id (not required)',
				'departure_date: date of the departure (not required)',
				'arrival: city id (not required)',
				'arrival_date: date of the arrival (not required)',
				'price: price of the ticket (not required)',
				'status: {} (not required)'.format(message),
				'do: bool tell if the travel has done or not (not required)'
			]
		},

		'Research travel':{
			'uri' : """GET "api/travel" """,
			'params' : [
				'at least one params required'
				'departure: city id (not required)',
				'departure_at: date of the departure (not required)',
				'arrival: city id (not required)',
				'arrival_at: date of the arrival (not required)',
				'price: price of the ticket (not required)',
				'status: {} (not required)'.format(message),
				'do: bool tell if the travel has done or not (not required)'
			]
		},


		'Get invoice':{
			'uri' : """GET "api/invoice" """,
			'params' : [
				'email: the email of the passenger (required)',
				'booking : booking id (required)'
			]
		},

	}
	return Response(api_urls)



def render_to_pdf(template_src, context_dict={}):
	template = get_template(template_src)
	html  = template.render(context_dict)
	result = BytesIO()
	pdf = pisa.pisaDocument(BytesIO(html.encode("ISO-8859-1")), result)
	if not pdf.err:
		return HttpResponse(result.getvalue(), content_type='application/pdf')
	return None

class invoice(View):
	def get(self, request, *args, **kwargs):
		if isinstance(is_passenger(request.data),Response):
			return is_passenger(request.data)

		if 'booking' not in data.request.keys():
			return Response({
                'errors': "booking id is required as booking"
            },400)

		b = Booking.objects.filter(id=booking).get()


		data = {
			"company": "LIAISON",
			"address": "Campus UAC, Rue Agro Maquis-FSA, Abomey-Calavi",

			"phone": "(+229) 21 21 21 21",
			"email": "info@liaison.com",

			"b":b,

			"date": datetime.now().strftime("%d/%m/%Y %H:%M:%S")
		}
		
		pdf = render_to_pdf('pdf.html', data)

		response = HttpResponse(pdf, content_type='application/pdf')
		filename = "Invoice_%s.pdf" %("12341231")
		content = "attachment; filename='%s'" %(filename)
		response['Content-Disposition'] = content
		return response
