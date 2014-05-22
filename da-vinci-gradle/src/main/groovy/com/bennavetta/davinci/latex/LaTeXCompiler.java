package com.bennavetta.davinci.latex;

import org.gradle.api.internal.tasks.SimpleWorkResult;
import org.gradle.api.internal.tasks.compile.CompilationFailedException;
import org.gradle.api.internal.tasks.compile.CompileSpecToArguments;
import org.gradle.api.internal.tasks.compile.Compiler;
import org.gradle.api.internal.tasks.compile.ExecSpecBackedArgCollector;
import org.gradle.api.tasks.WorkResult;
import org.gradle.process.ExecResult;
import org.gradle.process.internal.ExecHandle;
import org.gradle.process.internal.ExecHandleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;

/**
 * Compiles LaTeX. This corresponds directly to a LaTeX engine. It does not handle other processors or reruns.
 */
public class LaTeXCompiler implements Compiler<LaTeXCompileSpec>
{
	private static final Logger LOG = LoggerFactory.getLogger(LaTeXCompiler.class);

	private final CompileSpecToArguments<LaTeXCompileSpec> argumentsGenerator;

	private ByteArrayOutputStream compilerOut;
	private ByteArrayOutputStream compilerErr;

	public LaTeXCompiler()
	{
		argumentsGenerator = new LaTeXCompilerArgumentsGenerator();
	}

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
		builder.setWorkingDir(spec.getLatexFile().getParentFile());
		builder.setExecutable(spec.getEngine().getExecutable());
		argumentsGenerator.collectArguments(spec, new ExecSpecBackedArgCollector(builder));
		builder.setIgnoreExitValue(true);

		compilerOut = new ByteArrayOutputStream();
		compilerErr = new ByteArrayOutputStream();
		builder.setStandardOutput(compilerOut);
		builder.setErrorOutput(compilerErr);

		return builder.build();
	}
}
