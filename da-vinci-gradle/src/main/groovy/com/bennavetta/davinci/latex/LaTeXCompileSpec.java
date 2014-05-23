package com.bennavetta.davinci.latex;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.tasks.compile.CompileSpec;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Models LaTeX compilation
 */
public class LaTeXCompileSpec implements CompileSpec
{
	/**
	 * The extra input name for BibLaTex bibliographies. BibLaTeX inputs are used to set the {@code BIBINPUTS} environment variable.
	 */
	public static final String BIBLATEX_INPUT = "biblatex";

	/**
	 * The extra input name for Python sources. Python inputs are used to set the {@code PYTHONPATH} environment variable.
	 */
	public static final String PYTHON_INPUT = "python";

	/**
	 * The extra input name for LaTeX inputs other than the main file. This is used to set the {@code TEXINPUTS} environment
	 * variable, so it is useful for custom styles and modular documents.
	 */
	public static final String LATEX_INPUT = "latex";

	private File destinationDir;

	private Map<String, FileCollection> extraInputs = Maps.newHashMap();

	private File latexFile;

	private Engine engine;
	private List<String> arguments = Lists.newArrayList();

	/**
	 * Returns the output directory for LaTeX compilation. This is where generated {@code .log}, {@code .aux}, {@code .pdf}, and
	 * other files will go.
	 */
	public File getDestinationDir()
	{
		return destinationDir;
	}

	/**
	 * Returns the extra inputs for this compilation. This is used to specify additional sources such as Python code for
	 * PythonTex or bibliographies for BibLaTeX.
	 */
	public Map<String, FileCollection> getExtraInputs()
	{
		return extraInputs;
	}

	/**
	 * Returns the main LaTeX file for compilation. This is the file specified on the command line for normal LaTeX compilation,
	 * i.e. {@code pdflatex myfile.tex}.
	 */
	public File getLatexFile()
	{
		return latexFile;
	}

	/**
	 * Returns any additional command-line arguments that should be included when running the LaTeX engine. For example, this could
	 * be used to add the {@code -8bit} flag when using XeLaTeX.
	 */
	public List<String> getArguments()
	{
		return arguments;
	}

	/**
	 * Returns the LaTeX {@link Engine} (i.e. XeLaTeX) for compilation.
	 * @return
	 */
	public Engine getEngine()
	{
		return engine;
	}
	
	public void setDestinationDir(File destinationDir)
	{
		this.destinationDir = destinationDir;
	}

	public void setExtraInputs(Map<String, FileCollection> extraInputs)
	{
		this.extraInputs = extraInputs;
	}

	public void setLatexFile(File latexFile)
	{
		this.latexFile = latexFile;
	}

	public void setEngine(Engine engine)
	{
		this.engine = engine;
	}

	public void setArguments(List<String> arguments)
	{
		this.arguments = arguments;
	}
}
