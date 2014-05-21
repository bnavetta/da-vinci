from da_vinci import base, util
import os

def init_figures():
	import matplotlib
	matplotlib.rc('text', usetex=True)
	matplotlib.use("pdf")
	base.usepackage("float")

def save_figure(name, fig=None, format="pdf"):
	import matplotlib.pyplot as plt
	if not fig:
		fig = plt.gcf()
	filename = base.generated_file(name + "." + format)
	fig.savefig(filename)
	return filename

def latex_figure(name, figure=None, caption=None, options="width=0.8\\textwidth", pre="\\centering", post="", position="H"): #TODO: default to h or usepackage float
	filename, _ = os.path.splitext(save_figure(name, figure))
	content = pre + "\n"
	content += "\\includegraphics[%s]{\"%s\"}" % (options, filename)
	if caption:
		content += "\\caption{%s}\n" % caption
	return util.latex_environment('figure', content, "[%s]" % position)
