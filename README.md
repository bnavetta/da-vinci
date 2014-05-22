# Da Vinci

This project is designed to help creating data-based documents with Python and LaTeX. The build system is [Gradle](http://gradle.org)-based, and
[PythonTeX](https://github.com/gpoore/pythontex) is used to integrate Python and LaTeX. A set of Python modules that integrate with the Gradle
build are available from PythonTeX.

## Codex

Codex is the Python component of Da Vinci (his notebooks are called Codex ...). The base module is named `da_vinci`.

## Gradle Design

A `Document` is the core model object. It knows about the main LaTeX file as well as other files it depends on, such as bibliographies,
data files, code, and other LaTeX files. It should also be able to set environment variables like `TEXINPUTS`, `BIBINPUTS`, and `PYTHONPATH` correctly.

There are a couple of ways the build can be configured
* A LaTeX engine such as pdflatex or XeLaTeX can be specified, along with command-line arguments for it
* Processors such as PythonTeX and Biber can be enabled and configured
* It seems like LaTeX needs to be run once initially, then all the processors are run, then LaTeX again until the label/reference messages stops showing up or some maximum number of passes is exceeded.
	* `Label(s) may have changed`
	* `There were undefined references`
* Generate PDF files by default, then convert to other formats as needed (e.g. use `pdf2ps`)
	* The LaTeX wikibook says Ghostscript can be used to compress PDFs
