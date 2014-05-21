import os

pytex = None
gen_dir = None
# source_file = None

latex_packages = []
latex_preamble = []

def init(pytex_obj):
	"""
	Initialize Da Vinci. The generated files directory is looked up from the :envvar:`DA_VINCI_GEN_DIR` environment variable. If that
	environment variable is not set, it defaults to :func:`os.getcwd`
	"""
	global pytex, gen_dir, source_file
	pytex = pytex_obj
	try:
		gen_dir = os.environ['DA_VINCI_GEN_DIR']
	except KeyError as e:
		gen_dir = os.getcwd()
	# source_file = os.environ.get('DA_VINCI_SOURCE_FILE', default=os.getcwd())

def usepackage(name):
	r"""
	Register a LaTeX package to be used in the preamble. When :func:`preamble`
	is called, ``\usepackage`` statements will be added for each package.

	:param str name: The name of the LaTeX package
	"""
	latex_packages.append(name)

def add_preamble(text):
	"""
	Add text to be inserted in the preamble.

	:param str text: The text to append to the preamble
	"""
	latex_preamble.append(text)

def preamble():
	r"""
	Generate a preamble with the registered text and packages.

	.. code-block:: latex

		\begin{pycode}
		print(da_vinci.preamble())
		\end{pycode}
		\printpythontex

	:return: LaTeX to add to the preamble of the document
	:rtype: str
	"""
	content = ''
	for package in latex_packages:
		content += "\\usepackage{%s}\n" % package
	for text in latex_preamble:
		content += text + "\n"
	return content

def generated_file(name):
	"""
	All generated files should be under the generated files directory and
	registered with PythonTeX so they can be cleaned easily. This function
	returns a path in the generated files directory with the given name and
	calls :func:`pytex.add_created`.

	:param str name: The file name of the generated file
	:returns: The file name as a child of the generated file directory
	:rtype: str
	"""
	filename = os.path.join(gen_dir, name)
	pytex.add_created(filename)
	return filename
