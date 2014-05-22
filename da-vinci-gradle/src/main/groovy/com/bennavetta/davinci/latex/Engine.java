package com.bennavetta.davinci.latex;

import org.gradle.process.ExecSpec;

import java.io.File;

/**
 * A LaTeX engine such as XeTeX, LuaTeX, or pdfTeX.
 */
public interface Engine
{
	public String getName();

	public File getExecutable();

	/**
	 * Generate command-line arguments to compile the given {@link LaTeXCompileSpec} with this {@link Engine}.
	 * @param compileSpec the compile spec
	 * @return the command-line arguments
	 */
	public Iterable<String> generateArgs(LaTeXCompileSpec compileSpec);

	/**
	 * Check if LaTeX needs to be rerun. This is mostly to handle updating references from packages like BibLaTeX and
	 * PythonTex, which require multiple runs. For example, when using BibLaTeX, after running a backend like Biber,
	 * LaTeX must be rerun until messages like {@code Label(s) may have changed} and {@code There were undefined references}
	 * stop appearing in the LaTeX output.
	 * @param stdout the content of the standard output stream of the LaTeX process
	 * @param stderr the content of the standard error stream of the LaTeX process
	 * @return {@code true} if the LaTeX engine should be rerun
	 */
	public boolean needsRerun(String stdout, String stderr);
}
