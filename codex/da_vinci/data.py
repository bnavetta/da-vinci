class Data:
	"""
	:class:`Data` combines a :class:`pandas.DataFrame` with assorted information
	to make it easier to present, such as formatters and user-friendly column
	names.

	:param :class:`pandas.DataFrame` data_frame: The actual data being modeled
	:param dict formatters: Formatters for the data. This is a dictionary mapping column names to formatters, which can be callables or format strings
	:param dict names: Display names for the data. This is a dictionary mapping column names to strings.
	"""
	def __init__(self, data_frame, formatters=dict(), names=dict()):
		self.frame = data_frame
		self.formatters = formatters
		self.names = names

	def column_name(self, col):
		"""
		Look up the display name of the column `col`.

		:param str col: A column in :attr:`data_frame`
		:return: The name from :attr:`names`, defaulting to `col`
		:rtype: str
		"""
		return self.names.get(col, col)

	def column_formatter(self, col):
		"""
		Look up the formatter to format values in `col` as a callable. If no formatter is found,
		then :func:`str` is used. If the formatter is not a function, it is assumed to be a
		format string for :func:`format`.

		:param str col: A column in :attr:`data_frame`
		:return: A formatter callable from :attr:`formatters`, defaulting to :func:`str`
		:rtype: callable
		"""
		formatter = self.formatters.get(col, None)
		if formatter is None:
			return lambda value: str(value)
		if hasattr(formatter, '__call__'):
			return formatter
		else:
			return lambda value: format(value, formatter)

	def column_format(self, col, value):
		"""
		Format a value with the formatter defined for `col`.

		:param str col: A column in :attr:`data_frame`
		:param object value: The value to format
		:return: The formatted value (see :func:`column_formatter`)
		:rtype: str
		"""
		return self.column_formatter(col)(value)

	def __getitem__(self, index):
		"""Delegates to :meth:`pandas.DataFrame.__getitem__`"""
		return self.frame[index]
