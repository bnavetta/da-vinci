from da_vinci import base

def init_tables():
	base.usepackage("tabu")
	base.usepackage("array")
	base.usepackage("booktabs")

def latex_table(data, coltype='X', index=False, index_label='Item'):
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
