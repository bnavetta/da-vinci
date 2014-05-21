from da_vinci import base

def init_tables():
	"""
	Initialize table generation support, adding required LaTeX packages
	"""
	base.usepackage("tabu")
	base.usepackage("array")
	base.usepackage("booktabs")

def latex_table(data, coltype='X', index=False, index_label='Item'):
	"""
	Generate a LaTeX table from data using `tabu`.

	:param da_vinci.data.Data data: The data to generate a table from
	:param str coltype: The column type to use in the table
	:param bool index: If `True`, a column with row indices starting at one will be added
	:param str index_label: The name of the index column
	:return: The LaTeX table
	:rtype: str
	"""
	num_columns = len(data.frame.columns)
	if index:
		num_columns += 1
	table = "\\begin{tabu} to \\linewidth {%s}\n" % (coltype * num_columns)

	table += "\\toprule\n"
	if index:
		table += index_label + " & "
	table += " & ".join(data.frame.columns.map(data.column_name)) + "\\\\\n"

	table += "\\midrule\n"
	for idx, row in data.frame.iterrows():
		if index:
			table += "{} & ".format(idx + 1)
		table += " & ".join([data.column_format(col, val) for (col, val) in row.iteritems()])
		table += "\\\\\n"

	table += "\\bottomrule\n\\end{tabu}\n"
	return table
