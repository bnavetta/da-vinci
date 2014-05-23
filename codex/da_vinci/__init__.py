__version__ = '1.0'

from da_vinci.base import *
from da_vinci.util import *
from da_vinci.data import *

try:
	import matplotlib
	from da_vinci.figure import *
except ImportError as e:
	pass

from da_vinci.table import *
