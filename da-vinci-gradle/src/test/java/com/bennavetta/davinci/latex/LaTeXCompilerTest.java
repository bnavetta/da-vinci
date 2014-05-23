package com.bennavetta.davinci.latex;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;
import static org.truth0.Truth.ASSUME;

import java.io.File;
import java.io.IOException;

import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.file.collections.SimpleFileCollection;
import org.gradle.api.internal.tasks.compile.CompilationFailedException;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.process.ExecSpec;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.ImmutableMap;

public class LaTeXCompilerTest extends AbstractLaTeXTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void captureStdout()
	{
		File echoProgram = OperatingSystem.current().findInPath("echo");
		ASSUME.that(echoProgram).isNotNull();
		
		Engine engine = mock(Engine.class);
		when(engine.getExecutable()).thenReturn(echoProgram);
		doAnswer(invocation -> {
			ExecSpec exec = (ExecSpec) invocation.getArguments()[0];
			exec.args("Hello, World!");
			return null;
		}).when(engine).generateArgs(any(), any());
		
		LaTeXCompileSpec spec = new LaTeXCompileSpec();
		spec.setLatexFile(texFile());
		spec.setEngine(engine);
		
		LaTeXCompiler compiler = new LaTeXCompiler();
		ASSERT.that(compiler.execute(spec).getDidWork()).isTrue();
		ASSERT.that(compiler.getCompilerOut()).is("Hello, World!\n");
		ASSERT.that(compiler.getCompilerErr()).is("");
		
		verify(engine).generateArgs(anyObject(), eq(spec));
		verify(engine, atLeastOnce()).getExecutable();
	}
	
	@Test
	public void exitValueFailure()
	{
		File falseProgram = OperatingSystem.current().findInPath("false");
		// We need the "false" program
		ASSUME.that(falseProgram).isNotNull();
		
		Engine engine = mock(Engine.class);
		when(engine.getExecutable()).thenReturn(falseProgram);
		
		LaTeXCompileSpec spec = new LaTeXCompileSpec();
		spec.setLatexFile(texFile());
		spec.setEngine(engine);
		
		thrown.expect(CompilationFailedException.class);
		thrown.expectMessage("Compilation failed with exit code 1; see the compiler error output for details.");
		
		LaTeXCompiler compiler = new LaTeXCompiler();
		compiler.execute(spec);
	}
	
	@Test
	public void workingDirectory() throws IOException
	{
		File touchProgram = OperatingSystem.current().findInPath("touch");
		ASSUME.that(touchProgram).isNotNull();
		
		File targetFile = new File(texFile().getParentFile(), "test");
		ASSUME.that(targetFile.exists()).isFalse();
		
		Engine engine = mock(Engine.class);
		when(engine.getExecutable()).thenReturn(touchProgram);
		doAnswer(invocation -> {
			ExecSpec exec = (ExecSpec) invocation.getArguments()[0];
			exec.args(targetFile);
			return null;
		}).when(engine).generateArgs(any(), any());
		
		LaTeXCompileSpec spec = new LaTeXCompileSpec();
		spec.setLatexFile(texFile());
		spec.setEngine(engine);
		
		LaTeXCompiler compiler = new LaTeXCompiler();
		compiler.execute(spec);
		
		ASSERT.that(targetFile.isFile()).isTrue();
	}
	
	@Test
	public void texinputsSet()
	{
		testEnvironmentVariable(LaTeXCompileSpec.LATEX_INPUT,
				"TEXINPUTS",
				temporaryFolder.getRoot(), new File(".").getAbsoluteFile());
	}
	
	@Test
	public void bibinputsSet()
	{
		testEnvironmentVariable(LaTeXCompileSpec.BIBLATEX_INPUT,
				"BIBINPUTS",
				temporaryFolder.getRoot(), new File(".").getAbsoluteFile());
	}
	
	@Test
	public void pythonPathSet()
	{
		testEnvironmentVariable(LaTeXCompileSpec.PYTHON_INPUT,
				"PYTHONPATH",
				temporaryFolder.getRoot(), new File(".").getAbsoluteFile());
	}
	
	private void testEnvironmentVariable(String inputKey, String variableName, File... files)
	{
		File printenv = OperatingSystem.current().findInPath("printenv");
		ASSUME.that(printenv).isNotNull();
		ASSUME.that(System.getenv(variableName)).isNull(); // can't reliably test if the variable is set externally
		FileCollection fileCollection = new SimpleFileCollection(files);
		
		Engine engine = mock(Engine.class);
		when(engine.getExecutable()).thenReturn(printenv);
		doAnswer(invocation -> {
			ExecSpec exec = (ExecSpec) invocation.getArguments()[0];
			exec.args(variableName);
			return null;
		}).when(engine).generateArgs(any(), any());
		
		LaTeXCompileSpec spec = new LaTeXCompileSpec();
		spec.setLatexFile(texFile());
		spec.setEngine(engine);
		spec.setExtraInputs(ImmutableMap.of(inputKey, fileCollection));
		
		LaTeXCompiler compiler = new LaTeXCompiler();
		compiler.execute(spec);
		
		String output = compiler.getCompilerOut();
		ASSERT.that(output).is(fileCollection.getAsPath() + File.pathSeparator + "\n");
	}
}
