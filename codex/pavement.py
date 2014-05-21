from paver.easy import *
import paver.doctools

from paver.setuputils import setup, install_distutils_tasks
from sphinxcontrib import paverutils
from da_vinci import __version__

install_distutils_tasks()

options(
	setup=dict(
		name="da-vinci",
		version=__version__,
		url="http://github.com/roguePanda",
		packages=["da_vinci"],
		requires=[ #TODO: versions
			"matplotlib",
			"pyplot"
		]
	),

	docs=Bunch(
		doc_dir = path("docs"),
		build_root = path("build") / "docs"
	)
)

@task
@needs('generate_setup', 'minilib', 'setuptools.command.sdist')
def sdist():
	"""Overrides sdist to make sure that our setup.py is generated."""
	pass

@task
def api_docs(options):
	api_dir = options.docs.doc_dir / "api"
	api_dir.makedirs_p()
	sh("sphinx-apidoc -T -f -o %s da_vinci" % api_dir)

@task
@needs("api_docs")
def docs(options):
	"""Build Sphinx documentation"""
	doc_dir = options.docs.doc_dir
	build_root = options.docs.build_root
	for builder in ["html", "latex", "man", "texinfo"]:
		build_dir = build_root / builder
		build_dir.makedirs_p()
		sphinx_args = ["-b", builder, doc_dir, build_dir]
		sh("sphinx-build %s" % " ".join(sphinx_args))
	# Some builders need to have `make` run
	for builder in ["latex", "texinfo"]:
		build_dir = build_root / builder
		sh("cd %s; make" % build_dir)
