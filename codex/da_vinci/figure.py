from da_vinci import base, util
import os

def init_figures():
	"""
	Initialize matplotlib figure support. This must be called before any calls to matplotlib or pyplot,
	since it calls :func:`matplotlib.rc` and :func:`matplotlib.use`. The `pdf` backend is used since the
	`pgf` backend causes some issues. LaTeX rendering is also enabled.
	"""
	import matplotlib
	matplotlib.rc('text', usetex=True)
	matplotlib.use("pdf")
	base.usepackage("float")

def save_figure(name, fig=None, format="pdf"):
	"""
	Save `fig` and track it as a generated file. If no figure is given, :func:`matplotlib.pyplot.gcf` is used.

	:param str name: The name of the file to save the figure in
	:param :matplotlib.figure.Figure fig: The matplotlib figure to save
	:param str format: The format to save the figure as.
	:return: The filename that the figure was saved as
	:rtype: str
	
	"""
	import matplotlib.pyplot as plt
	if not fig:
		fig = plt.gcf()
	filename = base.generated_file(name + "." + format)
	fig.savefig(filename)
	return filename

def latex_figure(name, figure=None, caption=None, options="width=0.8\\textwidth", pre="\\centering", post="", position="H"):
	"""
	Embed a :class:`matplotlib.figure.Figure` in a LaTeX document. By default, :func:`matplotlib.pyplot.gcf` is used.

	:param str name: The name of the figure, and the basename of the file it will be saved in
	:param matplotlib.figure.Figure figure: The matplotlib figure to insert
	:param str caption: A caption to put under the figure using \\caption
	:param str options: Options to pass to \\includegraphics
	:param str pre: LaTeX to insert before \\includegraphics in the `figure` environment
	:param str post: LaTeX to insert after \\includegraphics in the `figure` environment
	:param str position: Where to place the figure (this is the parameter to the `figure` environment)
	:return: A LaTeX snippet to embed the figure.
	:rtype: str
	"""
	filename, _ = os.path.splitext(save_figure(name, figure))
	content = pre + "\n"
	content += "\\includegraphics[%s]{\"%s\"}" % (options, filename)
	if caption:
		content += "\\caption{%s}\n" % caption
	return util.latex_environment('figure', content, "[%s]" % position)
