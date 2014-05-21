class Data:
	def __init__(self, data_frame, formatters=dict(), names=dict()):
		self.frame = data_frame
		self.formatters = formatters
		self.names = names

	def column_name(self, label):
		return self.names.get(label, label)

	def column_formatter(self, label):
		formatter = self.formatters.get(label, None)
		if formatter is None:
			return lambda value: str(value)
		if hasattr(formatter, '__call__'):
			return formatter
		else:
			return lambda value: format(value, formatter)
	def column_format(self, label, value):
		return self.column_formatter(label)(value)

	def __getitem__(self, index):
		"""Delegates to :meth:`pandas.DataFrame.__getitem__`"""
		return self.frame[index]
