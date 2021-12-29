from django.urls import include, path
from .views import *
from .auth import *

from rest_framework import routers
from .viewsets import *

router = routers.SimpleRouter()

urlpatterns = [
	path('', apiOverview, name='api-overview'),
	path('auth/login', login, name="login"),
	path('auth/register', register, name="register"),
	path('booking', search_reservation, name='search_reservation'),
	path('travel/search', search_travel, name='search_travel'),
	path('auth/agency', create_agence, name="create_agence"),
	path('invoice', invoice.as_view(), name="invoice"),
]

router.register(r'alert', AlertViewSet, basename='alert')
router.register(r'message', MessageViewSet, basename='message')
router.register(r'city', CityViewSet, basename='city')
router.register(r'bus', BusViewSet, basename='bus')
router.register(r'politicy', PoliticyViewSet, basename='politicy')
router.register(r'travel', TravelViewSet, basename='travel')
router.register(r'booking', BookingViewSet, basename='booking')
urlpatterns += router.urls
