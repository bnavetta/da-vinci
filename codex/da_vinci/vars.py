"""
Support generating LaTeX variables from Python variables. This is useful for running Python code outside LaTeX and
then using the results in LaTeX.

To generate tables, the recommended approach is to use a package like ``csvsimple``, as documented on
`StackOverflow <http://tex.stackexchange.com/questions/146716/importing-csv-file-into-latex-as-a-table>`_

This module implements a ``pgfkeys``-based key-value store for strings based on a StackOverflow `answer <http://tex.stackexchange.com/a/37113>`_

A set of commands is defined in the preamble for using variables. ``declare`` creates a new variable namespace under the ``/variables`` ``pgfkeys`` dictionary.
``setvalue`` takes variable definitions in the form ``key1 = value1, namespace/key2 = value 2``. ``getvalue`` retrieves the variable at the given key.
"""

import da_vinci.base

setup_script = r"""
\newcommand{\setvalue}[1]{\pgfkeys{/variables, #1}}
\newcommand{\getvalue}[1]{\pgfkeysvalueof{/variables/#1}}
\newcommand{\declare}[1]{%
    \pgfkeys{
	    /variables/#1.is family,
		/variables/#1.unknown/.style = {\pgfkeyscurrentpath/\pgfkeyscurrentname/.initial = ##1}
	}%
}
\declare{}
"""

def init_vars():
	"""
	Initialize the variable module. This adds ``pgfkeys`` to the list of LaTeX packages and adds the setup code to the preamble
	"""
	da_vinci.base.usepackage("pgfkeys")
	da_vinci.base.add_preamble(setup_script)

def declare_namespace(namespace):
	"""
	Declare a variable namespace.

	:param str namespace: The name of the namespace (the ``pgfkeys`` directory under ``/variables``)
	:return: An macro invocation to define the namespace
	:rtype: str
	"""
	return "\\declare{%s}" % namespace

def define_vars(vars, namespace=None):
	"""
	Define variables in LaTeX

	:param str namespace: The namespace to store the variables in, defaulting to just ``/variables``. This namespace must be declared in the LaTeX file before the variables are defined.
	:param dict vars: The variables to define
	:return: The variable definitions using ``\\setvalue``
	:rtype: str
	"""
	# TODO: support namespacing via nested dictionaries
	if namespace is None:
		prefix = ""
	else:
		prefix = namespace + "/"
	return "\\setvalue{%s}" % ", ".join([
		"%s = %s" % (prefix + key, value)
		for (key, value) in vars.items()
	])
