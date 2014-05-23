package com.bennavetta.davinci.latex;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;

import org.gradle.api.internal.tasks.SimpleWorkResult;
import org.gradle.api.internal.tasks.compile.CompilationFailedException;
import org.gradle.api.internal.tasks.compile.Compiler;
import org.gradle.api.tasks.WorkResult;
import org.gradle.process.ExecResult;
import org.gradle.process.internal.ExecHandle;
import org.gradle.process.internal.ExecHandleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

/**
 * Compiles LaTeX. This corresponds directly to a LaTeX engine. It does not handle other processors or reruns.
 */
public class LaTeXCompiler implements Compiler<LaTeXCompileSpec>
{
	private static final Logger LOG = LoggerFactory.getLogger(LaTeXCompiler.class);

	private ByteArrayOutputStream compilerOut;
	private ByteArrayOutputStream compilerErr;

	@Override
	public WorkResult execute(LaTeXCompileSpec compileSpec)
	{
		Engine engine = compileSpec.getEngine();
		LOG.info("Compiling with LaTeX engine '{}' ({})", engine.getName(), engine.getExecutable());

		ExecHandle handle = createHandle(compileSpec);

		handle.start();
		ExecResult result = handle.waitForFinish();
		if(result.getExitValue() != 0)
		{
			throw new CompilationFailedException(result.getExitValue());
		}

		return new SimpleWorkResult(true);
	}

	public String getCompilerOut()
	{
		// Assume default charset is suitable for process standard output
		return new String(compilerOut.toByteArray(), Charset.defaultCharset());
	}

	public String getCompilerErr()
	{
		return new String(compilerErr.toByteArray(), Charset.defaultCharset());
	}

	private ExecHandle createHandle(LaTeXCompileSpec spec)
	{
		ExecHandleBuilder builder = new ExecHandleBuilder();

		spec.getEngine().generateArgs(builder, spec);

		/*
		 * Environment variable setup works like this
		 * - Prepend the specified extra inputs to whatever is already set
		 * - if nothing is set, the value should end with the platform path separator (i.e. ":")
		 *     -> this is important because otherwise LaTeX won't use the default *INPUTS variable, which contains all the standard packages
		 */
		if(spec.getExtraInputs().containsKey(LaTeXCompileSpec.LATEX_INPUT))
		{
			String path = spec.getExtraInputs().get(LaTeXCompileSpec.LATEX_INPUT).getAsPath();
			builder.environment("TEXINPUTS", path + File.pathSeparator + Strings.nullToEmpty(System.getenv("TEXINPUTS")));
		}
		if(spec.getExtraInputs().containsKey(LaTeXCompileSpec.BIBLATEX_INPUT))
		{
			String path = spec.getExtraInputs().get(LaTeXCompileSpec.BIBLATEX_INPUT).getAsPath();
			builder.environment("BIBINPUTS", path + File.pathSeparator + Strings.nullToEmpty(System.getenv("BIBINPUTS")));
		}
		if(spec.getExtraInputs().containsKey(LaTeXCompileSpec.PYTHON_INPUT))
		{
			String path = spec.getExtraInputs().get(LaTeXCompileSpec.PYTHON_INPUT).getAsPath();
			builder.environment("PYTHONPATH", path + File.pathSeparator + Strings.nullToEmpty(System.getenv("PYTHONPATH")));
		}

		builder.setWorkingDir(spec.getLatexFile().getParentFile());
		builder.setExecutable(spec.getEngine().getExecutable());
		builder.args(spec.getArguments());
		builder.setIgnoreExitValue(true);

		compilerOut = new ByteArrayOutputStream();
		compilerErr = new ByteArrayOutputStream();
		builder.setStandardOutput(compilerOut);
		builder.setErrorOutput(compilerErr);

		return builder.build();
	}
}
