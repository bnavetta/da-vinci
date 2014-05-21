import os

pytex = None
gen_dir = None
# source_file = None

latex_packages = []
latex_preamble = []

def init(pytex_obj):
	global pytex, gen_dir, source_file
	pytex = pytex_obj
	try:
		gen_dir = os.environ['DA_VINCI_GEN_DIR']
	except KeyError as e:
		gen_dir = os.getcwd()
	# source_file = os.environ.get('DA_VINCI_SOURCE_FILE', default=os.getcwd())

def usepackage(name):
	latex_packages.append(name)

def add_preamble(text):
	latex_preamble.append(text)

def preamble():
	content = ''
	for package in latex_packages:
		content += "\\usepackage{%s}\n" % package
	for text in latex_preamble:
		content += text + "\n"
	return content

def generated_file(name):
	filename = os.path.join(gen_dir, name)
	pytex.add_created(filename)
	return filename
