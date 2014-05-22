package com.bennavetta.davinci.latex;

import com.google.common.collect.Iterables;
import org.gradle.api.internal.tasks.compile.ArgCollector;
import org.gradle.api.internal.tasks.compile.CompileSpecToArguments;

public class LaTeXCompilerArgumentsGenerator implements CompileSpecToArguments<LaTeXCompileSpec>
{
	@Override
	public void collectArguments(LaTeXCompileSpec spec, ArgCollector argCollector)
	{
		argCollector.args(generate(spec));
	}

	public Iterable<String> generate(LaTeXCompileSpec spec)
	{
		Iterable<String> engineArgs = spec.getEngine().generateArgs(spec);
		Iterable<String> userArgs = spec.getArguments();
		return Iterables.concat(engineArgs, userArgs);
	}
}
