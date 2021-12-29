from django.db import models

from django.contrib.auth.models import User

# Create your models here.

# Common Customers' objects
class Customer(models.Model):
	"""docstring for Customer"""
	# Type choices
	CHOICES = [
		('PA', 'Passenger'),
		('AG', 'Agency'),
		('AD', 'Admin'),
	]
	user = models.OneToOneField(User, null=True, blank=True, on_delete=models.CASCADE)
	type = models.CharField(max_length=300, choices = CHOICES)

	def __str__(self):
		return self.user.username

class Alert(models.Model):
	"""docstring for Alert"""
	receiver = models.ForeignKey(Customer, null=True, blank=True, on_delete=models.CASCADE)
	message  = models.CharField(max_length=200, null=True)
	view     = models.BooleanField(default=False, null=True, blank=False) #does user read alert or not?

	def __str__(self):
		return str(self.pk)
	
class Message(models.Model):
	"""docstring for Message"""
	sender   = models.ForeignKey(Customer, null=True, blank=True, on_delete=models.CASCADE, related_name='sender')
	receiver = models.ForeignKey(Customer, null=True, blank=True, on_delete=models.CASCADE, related_name='receiver')
	date     = models.DateTimeField(auto_now_add=True)
	subject  = models.CharField(max_length=200, null=True, default='Aucun sujet')
	content  = models.TextField()
	view     = models.BooleanField(default=False, null=True, blank=False) #does user read message or not?


# Admin's objects
class Admin(models.Model):
	"""docstring for Admin"""
	user  = models.OneToOneField(Customer, null=True, blank=True, on_delete=models.CASCADE)
	phone = models.CharField(max_length=200, null=True)
	image = models.ImageField(null=True, blank=True, upload_to='admins', default='admins/default.png')
	
	@property
	def imageURL(self):
		try:
			url = self.image.url
		except:
			url = ''
		return url

	def __str__(self):
		return self.user.user.username

class Politicy(models.Model):
	"""docstring for Politicy"""
	title  = models.CharField(max_length=200, null=True, default='Aucune annulation')
	cancel = models.BooleanField(default=False, null=True, blank=False) #can we cancel or not?
	refund = models.FloatField() # refund percentage
	period = models.IntegerField() # number of days after which this rule is no longer applied

	def __str__(self):
		return self.title

class City(models.Model):
	id  = models.AutoField(primary_key=True)
	name = models.TextField(blank=True, null=True)
	
	def __str__(self):
		return self.name


# Agency's objects
class Agency(models.Model):
	"""docstring for Agency"""
	user     = models.OneToOneField(Customer, null=True, blank=True, on_delete=models.CASCADE)
	validate = models.BooleanField(default=False, null=True, blank=False) #has the admin validated the agency or not?
	name     = models.CharField(max_length=200, null=True)
	notes    = models.FloatField(default=0)
	desc     = models.CharField(max_length=200, null=True, default='Aucune description')	
	image    = models.ImageField(null=True, blank=True, upload_to='agencies', default='agencies/default.png')
	
	@property
	def imageURL(self):
		try:
			url = self.image.url
		except:
			url = ''
		return url

class Bus(models.Model):
	"""docstring for Bus"""
	agency = models.ForeignKey(Agency, null=True, blank=True, on_delete=models.CASCADE)
	places = models.IntegerField()
	image  = models.ImageField(null=True, blank=True, upload_to='buses', default='buses/default.png')
	
	@property
	def imageURL(self):
		try:
			url = self.image.url
		except:
			url = ''
		return url

class Travel(models.Model):
	# Status choices
	VOID = 'V'
	FULL = 'F'
	PARTIAL = 'P'

	BUS_STATUSES = (
		(VOID, 'Void'),
		(FULL, 'Full'),
		(PARTIAL, 'Partial'),
	)
	"""docstring for Travel"""
	bus            = models.ForeignKey(Bus, null=True, blank=True, on_delete=models.CASCADE)
	politicy       = models.ForeignKey(Politicy, null=True, blank=True, on_delete=models.SET_NULL)
	departure      = models.ForeignKey(City, null=True, blank=True, on_delete=models.CASCADE, related_name='departure')
	departure_at   = models.DateTimeField(auto_now_add=True)
	arrival        = models.ForeignKey(City, null=True, blank=True, on_delete=models.CASCADE, related_name='arrival')	
	arrival_at     = models.DateTimeField(auto_now_add=True)
	price          = models.FloatField()
	available      = models.IntegerField() #available seats
	status         = models.CharField(choices=BUS_STATUSES, default=VOID, max_length=255)
	do             = models.BooleanField(default=False, null=True, blank=False) #does trip made or not?



# Passenger's objects
class Passenger(models.Model):
	"""docstring for Passenger"""
	user  = models.OneToOneField(Customer, null=True, blank=True, on_delete=models.CASCADE)
	phone = models.CharField(max_length=200, null=True)
	image = models.ImageField(null=True, blank=True, upload_to='passengers', default='passengers/default.png')
	
	@property
	def imageURL(self):
		try:
			url = self.image.url
		except:
			url = ''
		return url

class Booking(models.Model):
	"""docstring for Booking"""
	travel    	= models.ForeignKey(Travel, null=True, blank=True, on_delete=models.CASCADE)
	passenger 	= models.ForeignKey(Passenger, null=True, blank=True, on_delete=models.CASCADE)
	places     	= models.IntegerField()
	created_at 	= models.DateTimeField(auto_now_add=True)
	updated_at 	= models.DateTimeField(auto_now_add=True)
	total 		= models.FloatField()

