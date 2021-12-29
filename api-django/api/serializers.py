from rest_framework import serializers
from django.contrib.auth.models import User
from django.core.files.base import ContentFile
from .models import *
from .utils import base64_file

# ************************************Common Customers' objects************************************
class UserSerializer(serializers.Serializer):
	
	id          = serializers.IntegerField(required=False)
	email       = serializers.EmailField(required=True)
	username    = serializers.CharField(required=True,min_length=3)
	password    = serializers.CharField(required=True,min_length=8,write_only=True)
	date_joined = serializers.DateTimeField(required=False)
	
	def create(self, validated_data):
		return User(**validated_data)
	
	def update(self, instance, validated_data):
		instance.email    = validated_data.get('email', instance.email)
		instance.password = validated_data.get('password', instance.password)
		instance.username = validated_data.get('username', instance.username)
		return instance
	

	class Meta(object):
		"""docstring for Meta"""
		model  = User
		# fields = '__all__'
		read_only_fields = [
			'id',
			'date_joined'
		]
		fields = [
			'id',
			'email',
			'username',
			'password',
			'date_joined',
		]

class CustomerSerializer(serializers.ModelSerializer):
	"""docstring for CustomerSerializer"""
	requires_context = True
	user = UserSerializer()
	type = serializers.ChoiceField(
		choices = [
			('PA', 'Passenger'),
			('AG', 'Agency'),
		],
		required=True
	)

	class Meta(object):
		"""docstring for Meta"""
		model  = Customer
		fields = '__all__'
	
	def create(self, validated_data):
		user_data = validated_data.pop('user')
		user_data = UserSerializer.create(UserSerializer, user_data)
		password  = validated_data.pop('password')
		user_data.set_password(password)
		user_data.save()
		return Customer(user= user_data, type=validated_data.pop('type'))
	
	def update(self, instance, validated_data):
		instance.type = validated_data.get('type', instance.type)
		return instance

class AlertSerializer(serializers.ModelSerializer):
	"""docstring for AlertSerializer"""
	requires_context = True
	class Meta(object):
		"""docstring for Meta"""
		model  = Alert
		fields = '__all__'

class MessageSerializer(serializers.Serializer):
	"""docstring for MessageSerializer"""
	sender              =   serializers.IntegerField(required=True, write_only=True)
	receiver            =   serializers.IntegerField(required=True, write_only=True)
	subject             =   serializers.CharField(required=True)
	content             =   serializers.CharField(required=True)
	date                =   serializers.DateTimeField(required=False)
	receiver_info       =   serializers.SerializerMethodField(method_name="get_receiver_info", read_only=True)
	sender_info         =   serializers.SerializerMethodField(method_name="get_sender_info", read_only=True)

	def get_receiver_info(self, m):
		if isinstance(m, Message):
			return CustomerSerializer(m.receiver).data
		return 

	def get_sender_info(self, m):
		if isinstance(m, Message):
			return CustomerSerializer(m.sender).data
		return
 
	def validate_receiver(self, value):
		if not Customer.objects.filter(pk= value).exists():
			raise serializers.ValidationError('This user does not exist')
		return value
			
	def validate_sender(self, value):
		if not Customer.objects.filter(pk= value).exists():
			raise serializers.ValidationError('This user does not exist')
		return value

	def create(self, validated_data):
		validated_data['sender'] = Customer.objects.filter(pk= validated_data['sender']).get()
		
		validated_data['receiver'] = Customer.objects.filter(pk= validated_data['receiver']).get()
		return Message(**validated_data)

	class Meta(object):
		"""docstring for Meta"""
		model  = Message
		fields = '__all__'

# ************************************Admin's objects************************************

class AdminSerializer(serializers.Serializer):
	"""docstring for AdminSerializer"""

	user        = serializers.IntegerField(required=True, write_only=True)
	phone 		= serializers.CharField(max_length=200, required=True)
	image 		= serializers.CharField(required=False)

	customer = serializers.SerializerMethodField(method_name='get_user_info')

	def get_user_info(self, c):
		if isinstance(c, Customer):
			return CustomerSerializer(c).data
	
	def validate_user(self, value):
		if Customer.objects.filter(pk = value).exists():
			raise serializers.ValidationError('This customer does not exist')
		return value

	def create(self, validated_data):
		validated_data['user'] = Customer.objects.filter(pk = value).get()
		if 'image' in validated_data.keys():
			validated_data['image']  = base64_file(validated_data['image'], name= validated_data['name'])
		return Admin(**validated_data)

	def update(self, instance, validate_data):
		if 'image' in validated_data.keys():
			instance.image = base64_file(validated_data['image'])
		instance.phone = validated_data.get('phone', instance.phone)
		instance.user = Customer.objects.filter(pk=validated_data['user']).get()

		return instance


	class Meta(object):
		"""docstring for Meta"""
		model  = Admin
		fields = '__all__'

class PoliticySerializer(serializers.Serializer):
	"""docstring for PoliticySerializer"""
	class Meta(object):
		"""docstring for Meta"""
		model  = Politicy
		fields = '__all__'
	id     = serializers.IntegerField(read_only=True)
	title  = serializers.CharField(max_length=200, required=True)
	cancel = serializers.BooleanField(required=True) #can we cancel or not?
	refund = serializers.FloatField(required=True) # refund percentage
	period = serializers.IntegerField(required=True)

	def create(self, validated_data):
		return Politicy(**validated_data)
	
	def update(self, instance, validated_data):
		instance.title = validated_data.get('title', instance.title)
		instance.cancel = validated_data.get('cancel', instance.cancel)
		instance.refund = validated_data.get('refund', instance.refund)
		instance.period = validated_data.get('period', instance.period)
		return instance


class CitySerializer(serializers.Serializer):
	"""docstring for CitySerializer"""
	class Meta(object):
		"""docstring for Meta"""
		model  = City
		fields = '__all__'
	id     = serializers.IntegerField(read_only=True)
	name   = serializers.CharField(max_length=200, required=True)

	def create(self, validated_data):
		return City(**validated_data)
	
	def update(self, instance, validated_data):
		instance.name = validated_data.get('name', instance.name)
		return instance



# ************************************Agency's objects************************************
class AgencySerializer(serializers.Serializer):
	"""docstring for AgencySerializer"""
	id    			= serializers.IntegerField(required=False)
	user_id  		= serializers.IntegerField(required=True, write_only=True)
	user  			= CustomerSerializer(required=False, read_only=True)
	name  			= serializers.CharField(min_length=3)
	notes			= serializers.FloatField(required=False)
	desc  			= serializers.CharField(required=False)
	image 			= serializers.CharField(required=False)
	validate		= serializers.BooleanField(required=False)

	# def create(self, validated_data):
	# 	if 'image' in validated_data.keys():
	#     	validated_data['image'] = base64_file(data= validated_data['image'], name= validated_data['name'])
	# 	return 

	def validate_user_id(self, value):
		if not Customer.objects.filter(pk= value).exists():
			raise serializers.ValidationError('This user does not exist')
		return value

	def update(self, instance, validated_data):
		instance.user      = Customer.objects.filter(pk= validated_data['user_id']).get()
		instance.name      = validated_data.get('name', instance.name)
		instance.note      = validated_data.get('note', instance.note)
		instance.desc      = validated_data.get('desc', instance.desc)
		instance.validate  = validated_data.get('validate', instance.validate)
		if 'image' in validated_data.keys():
			instance.image = base64_file(validated_data['image'])

		return instance

	class Meta(object):
		"""docstring for Meta"""
		model  = Agency
		fields = '__all__'



class BusSerializer(serializers.Serializer):
	"""docstring for BusSerializer"""
	id = serializers.IntegerField(required=False)
	agency = AgencySerializer(required=False)
	places = serializers.IntegerField(required=True)
	image = serializers.CharField(required=False)

	def create(self, validated_data):
		if 'image' in validated_data.keys():
			validated_data['image']  = base64_file(validated_data['image'], name= validated_data['name'])
		return Bus(**validated_data)
		# return validated_data['agence']

	def update(self, instance, validated_data):
		if 'image' in validated_data.keys():
			instance.image = base64_file(validated_data['image'])
		instance.places = validated_data.get('places', instance.places)

		return instance

	class Meta(object):
		"""docstring for Meta"""
		model  = Bus
		fields = '__all__'



class TravelSerializer(serializers.ModelSerializer):
	"""docstring for TravelSerializer"""
	
	bus            = serializers.IntegerField(required=True, write_only=True)
	politicy       = serializers.IntegerField(required=True, write_only=True )
	departure      = serializers.IntegerField(required=True)	
	departure_at   = serializers.DateTimeField(required=True)
	arrival        = serializers.IntegerField(required=True)	
	arrival_at     = serializers.DateTimeField(required=True)
	available      = serializers.IntegerField(required=False)
	price          = serializers.FloatField(required=True)
	status         = serializers.CharField(max_length=255)
	do             = serializers.BooleanField(default=False) #does trip made or not?

	more_info      = serializers.SerializerMethodField(method_name="get_info", read_only=True)
	booking_info   = serializers.SerializerMethodField(method_name="get_booking_info", read_only=True)

	def get_booking_info(self, t):
		if isinstance(t, Travel):
			return  BookingSerializer(Booking.objects.filter(travel= t), many= True).data,
		return 

	def get_info(self, t):
		if isinstance(t, Travel):
			return {
				'bus': BusSerializer(t.bus).data,
				'politicy': PoliticySerializer(t.politicy).data
			}
		return 

	def validate_departure(self, value):
		if not City.objects.filter(pk= value).exists():
			raise serializers.ValidationError('This bus does not exists in database')
		return value

	def validate_arrival(self, value):
		if not City.objects.filter(pk= value).exists():
			raise serializers.ValidationError('This bus does not exists in database')
		return value

	def validate_bus(self, value):
		if not Bus.objects.filter(pk= value).exists():
			raise serializers.ValidationError('This bus does not exists in database')
		return value
	
	def validate_politicy(self, value):
		if not Politicy.objects.filter(pk= value).exists():
			raise serializers.ValidationError('This politicy does not exists in database')
		return value

	def validate_status(self, value):
		VOID = 'V'
		FULL = 'F'
		PARTIAL = 'P'

		BUS_STATUSES = {
			VOID : 'Void',
			FULL : 'Full',
			PARTIAL : 'Partial'
		}
		message = 'This bus statues does not exists. Choose beetwen'
		for key in BUS_STATUSES:
			message += " {}: {}".format(key, BUS_STATUSES[key])
		if value not in BUS_STATUSES.keys():
			raise serializers.ValidationError(message)
		
		return value 

	def create(self, validated_data):
		validated_data['bus']       = Bus.objects.filter(pk= validated_data['bus']).get()
		validated_data['politicy']  = Politicy.objects.filter(pk= validated_data['politicy']).get()
		validated_data['departure'] = City.objects.filter(pk= validated_data['departure']).get()
		validated_data['arrival']   = City.objects.filter(pk= validated_data['arrival']).get()
		return Travel(**validated_data)
	
	def update(self, instance, validated_data):
		instance.bus            = Bus.objects.filter(pk= validated_data['bus']).get()
		instance.politicy       = Politicy.objects.filter(pk= validated_data['politicy']).get()
		instance.departure      = City.objects.filter(pk= validated_data['departure']).get()
		instance.arrival        = City.objects.filter(pk= validated_data['arrival']).get()
		instance.departure_date = validated_data.get('departure_date', instance.departure_date)
		instance.arrival_date   = validated_data.get('arrival_date', instance.arrival_date)
		instance.price          = validated_data.get('price', instance.price)
		instance.available      = validated_data.get('available', instance.available)
		instance.status         = validated_data.get('status', instance.status)
		instance.do             = validated_data.get('do', instance.do)
		return instance


	class Meta(object):
		"""docstring for Meta"""
		model  = Travel
		fields = '__all__'


# ************************************Passenger's objects************************************
class PassengerSerializer(serializers.Serializer):
	"""docstring for PassengerSerializer"""

	user        = serializers.IntegerField(required=True, write_only=True)
	phone 		= serializers.CharField(max_length=200, required=True)
	image 		= serializers.CharField(required=False)

	customer = serializers.SerializerMethodField(method_name='get_user_info')

	def get_user_info(self, c):
		if isinstance(c, Customer):
			return CustomerSerializer(c).data
	
	def validate_user(self, value):
		if Customer.objects.filter(pk = value).exists():
			raise serializers.ValidationError('This customer does not exist')
		return value

	def create(self, validated_data):
		validated_data['user'] = Customer.objects.filter(pk = value).get()
		if 'image' in validated_data.keys():
			validated_data['image']  = base64_file(validated_data['image'], name= validated_data['name'])
		return Passenger(**validated_data)

	def update(self, instance, validate_data):
		if 'image' in validated_data.keys():
			instance.image = base64_file(validated_data['image'])
		instance.phone = validated_data.get('phone', instance.phone)
		instance.user = Customer.objects.filter(pk=validated_data['user']).get()

		return instance


	class Meta(object):
		"""docstring for Meta"""
		model  = Passenger
		fields = '__all__'