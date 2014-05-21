__version__ = '1.0'

from base import *
from util import *
from data import *

try:
	import matplotlib
	from figure import *
except ImportError as e:
	pass

from table import *
