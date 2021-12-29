from rest_framework import viewsets
from rest_framework.decorators import action, api_view
from rest_framework.response import Response
from .models import *
from .serializers import *
from django.shortcuts import get_object_or_404
from django.db.models import Q

from .utils import is_agency, is_passenger, is_admin, is_customer, is_admin_or_agency

class AlertViewSet(viewsets.ViewSet):

	def list(self, request):
		if isinstance(is_customer(request.query_params),Response):
			return is_customer(request.query_params)

		queryset = Alert.objects.filter(receiver= is_customer(request.query_params))
		serializer = AlertSerializer(queryset, many=True)
		return Response(serializer.data)

	def retrieve(self, request, pk):
		if isinstance(is_customer(request.query_params),Response):
			return is_customer(request.query_params)

		queryset = Alert.objects.all()
		pk= request.query_params.get('pk', None)
		a = get_object_or_404(queryset, pk=pk)
		serializer = MessageSerializer(a)
		return Response(serializer.data)
	
	def update(self, request, pk):
		if isinstance(is_customer(request.data), Response):
			return is_customer(request.data)
		
		pk= request.query_params.get('pk', None)
		a = get_object_or_404(Alert, pk=pk)
		a_new = AlertSerializer(data= request.data)

		if not a_new.is_valid():
			return Response({
				"errors": a_new.errors
			},400)

		a = a_new.update(a, request.data)
		a.save()

		return Response({
			'message': 'The alert has been updated',
			'data': AlertSerializer(a).data
		})

	def delete(self, request, pk):
		if isinstance(is_customer(request.data), Response):
			return is_customer(request.data)
		
		pk = request.query_params.get('pk', None)
		a  = get_object_or_404(Alert, pk=pk)
		a.delete()

		return Response({
			'message': 'The alert has been deleted',
		})


class MessageViewSet(viewsets.ViewSet):

	def list(self, request):
		if isinstance(is_customer(request.query_params),Response):
			return is_customer(request.query_params)

		queryset = Message.objects.filter(
			Q(sender= is_customer(request.query_params)) |
			Q(receiver= is_customer(request.query_params))
		)
		serializer = MessageSerializer(queryset, many=True)
		return Response(serializer.data)

	def create(self, request):
		if isinstance(is_customer(request.data), Response):
			return is_customer(request.data)

		a = is_customer(request.data)
		request.data['sender']= a.id
		print(request.data)

		# return Response(request.data)

		m = MessageSerializer(data= request.data)

		if not m.is_valid():
			return Response({
				"errors": m.errors
			},400)

		request.data.pop('email')
		m = m.create(request.data)
		m.save()

		return Response({
			'message': "The message has been sended",
			'data': MessageSerializer(m).data
		})

	def retrieve(self, request, pk):
		if isinstance(is_customer(request.query_params),Response):
			return is_customer(request.query_params)

		pk = request.query_params.get('pk', None)
		queryset = Message.objects.all()
		msg = get_object_or_404(queryset, pk=pk)
		serializer = MessageSerializer(msg)
		return Response(serializer.data)
	
	def update(self, request, pk):
		if isinstance(is_customer(request.data), Response):
			return is_customer(request.data)
		
		pk = request.query_params.get('pk', None)
		msg = get_object_or_404(Message, pk=pk)
		msg_new = MessageSerializer(data= request.data)

		if not msg_new.is_valid():
			return Response({
				"errors": msg_new.errors
			},400)

		msg = msg_new.update(msg, request.data)
		msg.save()

		return Response({
			'message': 'The bus has been updated',
			'data': MessageSerializer(msg).data
		})

	def delete(self, request, pk):
		if isinstance(is_customer(request.data), Response):
			return is_customer(request.data)
		
		pk = request.query_params.get('pk', None)
		msg = get_object_or_404(Message, pk=pk)
		msg.delete()

		return Response({
			'message': 'The bus has been deleted',
		})

# ************************************Admin's objects************************************
class PoliticyViewSet(viewsets.ViewSet):
	def list(self, request):
		if isinstance(is_admin_or_agency(request.query_params),Response):
			return is_admin_or_agency(request.query_params)

		queryset = Politicy.objects.all()
		serializer = PoliticySerializer(queryset, many=True)
		return Response(serializer.data)
	
	def create(self, request):
		if isinstance(is_admin(request.data),Response):
			return is_admin(request.data)

		p = PoliticySerializer(data= request.data)

		if not p.is_valid():
			return Response({
				"errors": p.errors
			},400)

		p = p.create(p.data)
		p.save()

		return Response({
			'message': 'The politicy has been created',
			'data': PoliticySerializer(p).data
		})

	def retrieve(self, request, pk):
		if isinstance(is_admin(request.query_params),Response):
			return is_admin(request.query_params)

		queryset = Politicy.objects.all()
		pk= request.query_params.get('pk', None)
		bus = get_object_or_404(queryset, pk=pk)
		serializer = PoliticySerializer(bus)
		return Response(serializer.data)

	def update(self, request, pk):
		if isinstance(is_admin(request.data),Response):
			return is_admin(request.data)
		
		pk= request.query_params.get('pk', None)        
		p = get_object_or_404(Politicy, pk= pk)
		p_new = PoliticySerializer(data= request.data)

		if not p_new.is_valid():
			return Response({
				"errors": p_new.errors
			},400)

		p = p_new.update(p, p_new.data)
		p.save()

		return Response({
			'message': 'The politicy has been updated',
			'data': PoliticySerializer(p).data
		})

	def delete(self, request, pk):
		if isinstance(is_admin(request.data), Response):
			return is_admin(request.data)
		
		pk= request.query_params.get('pk', None)
		p = get_object_or_404(Politicy, pk=pk)
		p.delete()

		return Response({
			'message': 'The politicy has been deleted',
		})

class CityViewSet(viewsets.ViewSet):
	def list(self, request):
		if isinstance(is_customer(request.query_params),Response):
			return is_customer(request.query_params)

		queryset = City.objects.all()
		serializer = CitySerializer(queryset, many=True)
		return Response(serializer.data)
	
	def create(self, request):
		if isinstance(is_admin(request.data),Response):
			return is_admin(request.data)

		c = CitySerializer(data= request.data)

		if not c.is_valid():
			return Response({
				"errors": c.errors
			},400)

		c = c.create(c.data)
		c.save()

		return Response({
			'message': 'The city has been created',
			'data': CitySerializer(c).data
		})

	def retrieve(self, request, pk):
		if isinstance(is_customer(request.query_params),Response):
			return is_customer(request.query_params)

		pk= request.query_params.get('pk', None)
		queryset = City.objects.all()
		bus = get_object_or_404(queryset, pk=pk)
		serializer = CitySerializer(bus)
		return Response(serializer.data)

	def update(self, request, pk):
		if isinstance(is_admin(request.data),Response):
			return is_admin(request.data)

		pk= request.query_params.get('pk', None)        
		c = get_object_or_404(City, pk= pk)
		c_new = CitySerializer(data= request.data)

		if not p_new.is_valid():
			return Response({
				"errors": p_new.errors
			},400)

		c = c_new.update(c, p_new.data)
		c.save()

		return Response({
			'message': 'The city has been updated',
			'data': CitySerializer(c).data
		})

	def delete(self, request, pk):
		if isinstance(is_admin(request.data), Response):
			return is_admin(request.data)
		
		pk= request.query_params.get('pk', None)
		c = get_object_or_404(City, pk=pk)
		c.delete()

		return Response({
			'message': 'The city has been deleted',
		})

# ************************************Agency's objects************************************
class BusViewSet(viewsets.ViewSet):

	def list(self, request):
		if isinstance(is_agency(request.query_params),Response):
			return is_agency(request.query_params)

		queryset = Bus.objects.filter(agency= is_agency(request.query_params))
		serializer = BusSerializer(queryset, many=True)
		return Response(serializer.data)

	def create(self, request):
		if isinstance(is_agency(request.data), Response):
			return is_agency(request.data)

		data = request.data

		b = BusSerializer(data= data)

		if not b.is_valid():
			return Response({
				"errors": b.errors
			},400)

		data['agency'] = is_agency(data)
		data.pop('email')

		b = b.create(data)
		b.save()

		return Response({
			'message': 'The bus has been created',
			'data': BusSerializer(b).data
		})

	def retrieve(self, request, pk):
		if isinstance(is_agency(request.query_params),Response):
			return is_agency(request.query_params)

		pk= request.query_params.get('pk', None)
		queryset = Bus.objects.all()
		bus = get_object_or_404(queryset, pk=pk)
		serializer = BusSerializer(bus)
		return Response(serializer.data)
	
	def update(self, request, pk):
		if isinstance(is_agency(request.data), Response):
			return is_agency(request.data)
		
		pk= request.query_params.get('pk', None)
		b = get_object_or_404(Bus, pk=pk)
		b_new = BusSerializer(data= request.data)

		if not b_new.is_valid():
			return Response({
				"errors": b_new.errors
			},400)

		b = b_new.update(b, request.data)
		b.save()

		return Response({
			'message': 'The bus has been updated',
			'data': BusSerializer(b).data
		})

	def delete(self, request, pk):
		if isinstance(is_agency(request.data), Response):
			return is_agency(request.data)
		
		pk= request.query_params.get('pk', None)
		b = get_object_or_404(Bus, pk=pk)
		b.delete()

		return Response({
			'message': 'The bus has been deleted',
		})


class TravelViewSet(viewsets.ViewSet):
	def list(self, request):
		if isinstance(is_customer(request.query_params),Response):
			return is_customer(request.query_params)

		if not isinstance(is_agency(request.query_params),Response):
			queryset = Travel.objects.filter(bus__agency = is_agency(request.query_params))
			serializer = TravelSerializer(queryset, many=True)

		elif not isinstance(is_admin(request.query_params),Response):
			queryset = Travel.objects.all()
			serializer = TravelSerializer(queryset, many=True)

		else:
			queryset = Travel.objects.all().filter(bus__agency__validate = True)
			serializer = TravelSerializer(queryset, many=True)

		return Response(serializer.data)
	
	def create(self, request):
		if isinstance(is_agency(request.data),Response):
			return is_agency(request.data)

		t = TravelSerializer(data= request.data)

		if not t.is_valid():
			return Response({
				"errors": t.errors
			},400)
		
		t = t.create(t.data)
		t.available=t.bus.places
		t.save()
		
		return Response({
			'message': "The travel has been created",
			'data': TravelSerializer(t).data
		})

	def retrieve(self, request, pk):
		if isinstance(is_agency(request.query_params),Response):
			return is_agency(request.query_params)

		pk= request.query_params.get('pk', None)
		queryset = Travel.objects.all()
		bus = get_object_or_404(queryset, pk=pk)
		serializer = TravelSerializer(bus)
		return Response(serializer.data)
	
	def update(self, request, pk):
		if isinstance(is_agency(request.data),Response):
			return is_agency(request.data)
		
		pk= request.query_params.get('pk', None)
		t = get_object_or_404(Travel, pk= pk)
		t_new = TravelSerializer(data= request.data)

		if not t_new.is_valid():
			return Response({
				"errors": t_new.errors
			},400)

		t = t_new.update(t,request.data)
		t.save()

		return Response({
			'message': 'The travel has been updated',
			'data': isinstance(t, Travel) #TravelSerializer(t).data
		})

	def delete(self, request, pk):
		if isinstance(is_agency(request.data), Response):
			return is_agency(request.data)
		
		pk= request.query_params.get('pk', None)
		t = get_object_or_404(Travel, pk=pk)
		t.delete()

		return Response({
			'message': 'The travel has been deleted',
		})

	@action(detail=True, methods=['post'], name='make_reservation', url_path='make-reservation/')
	def make_reservation(self, request):
		if isinstance(is_customer(request.data),Response):
			return is_customer(request.data)
		
		p = PassengerSerializer(data= request.data)

		if not Booking.objects.filter(travel__id= request.data['travel']).exists():
			return Response({
				'errors': 'The travel does not exists'
			})

		travel = Travel.objects.filter(pk= request.data['travel']).get()

		if travel.status=='F':
			return Response ({
				'errors': 'The bus is full'
			})

		if request.data['places'] > travel.available:
			return Response ({
				'errors': 'The places are not enough for your reservation. There is only {}'.format(travel.available)
			})
		
		travel.status = 'P'
		travel.save()


		if not p.is_valid():
			return Response({
				"errors": p.errors
			},400)
		
		p = p.create(p.data)
		p.save()

		request.data['passenger'] = p.id

		b = BookingSerializer(data= request.data)

		if not b.is_valid():
			return Response({
				"errors": b.errors
			},400)
		
		b = b.create(b.data)
		b.total = b.places*travel.price
		b.save()

		travel.available-=b.places
		if travel.available==0:
			travel.status = 'F'
		travel.save()

		return Response({
			'message': 'The Reservation has been created',
			'data': BookingSerializer(b).data
		})

	@action(detail=True, methods=['post'], name='change_reservation', url_path='change-reservation/')
	def change_reservation(self, request):
		if isinstance(is_customer(request.data),Response):
			return is_customer(request.data)
		
		if not Booking.objects.filter(pk= request.data['id']).exists():
			return Response({
				'errors': 'The travel does not exists'
			})

		b = Booking.objects.filter(pk= request.data['id']).get()
		travel = b.travel.get()
		if request.data['places'] > travel.available:
			return Response ({
				'errors': 'The places are not enough for your reservation. There is only {}'.format( (Booking.objects.filter(travel__id).count() - travel.bus.places) )
			})


		# Update available places
		travel.available-=b.places
		travel.available+=request.data['places']

		if travel.available==0:
			travel.status = 'F'
		elif travel.available==travel.bus.places:
			travel.status = 'V'
		else:
			travel.status = 'P'
		travel.save()

		b.places = request.data['places']
		b.total = request.data['places'].places*travel.price
		b.save()

		b = BookingSerializer(data= b)

		return Response({
			'message': 'The Reservation has been updated',
			'data': b.data
		})

	@action(detail=True, methods=['delete'], name='delete_reservation', url_path='delete-reservation/<int:pk>')
	def delete_reservation(self, request, pk):
		if isinstance(is_customer(request.data),Response):
			return is_customer(request.data)
		
		pk= request.query_params.get('pk', None)
		b = get_object_or_404(Booking, pk= pk)

		# Update available places
		travel = b.travel.get()
		travel.available-=b.places

		if travel.available==0:
			travel.status = 'F'
		elif travel.available==travel.bus.places:
			travel.status = 'V'
		else:
			travel.status = 'P'
		travel.save()

		b.delete()

		return Response({
			'message': 'The Reservation has been deleted'
		})

class BookingViewSet(viewsets.ViewSet):
	def list(self, request):
		if isinstance(is_customer(request.query_params),Response):
			return is_customer(request.query_params)

		if isinstance(is_agency(request.query_params), Agency):
			queryset = Booking.objects.filter(travel__bus__agency = is_agency(request.query_params))
			serializer = BookingSerializer(queryset, many=True)

		elif isinstance(is_customer(request.query_params), Customer):
			queryset = Booking.objects.filter(passenger__user = is_customer(request.query_params))
			serializer = BookingSerializer(queryset, many=True)

		return Response(serializer.data)
	


@api_view(['GET'])
def search_travel(self, request):
	"""
		params: 
		
	"""
	travels = Travel.objects.distinct(**request.query_params)
	if travels.count() == 0:
		return Response(
			{
				'message': "Aucune donnéés"
			},
		)

	
	return Response(
		{
			'data': TravelSerializer(travels, many=True).data
		}
	)


# ************************************Passenger's objects************************************

@api_view(['GET'])
def search_reservation(self, request):
	if len(request.query_params.keys()) == 0:
		return Response(
			{
				'message': "Params missing"
			},
		)

	travels = Travel.objects.distinct(**request.query_params)
	if travels.count() == 0:
		return Response(
			{
				'message': "Aucune donnéés"
			},
		)
	b = []
	for t in travels:
		b.append(
			BookingSerializer(Booking.objects.filter(travel= t.get())).data
		)

	
	return Response(
		{
			'data': b
		}
	)
