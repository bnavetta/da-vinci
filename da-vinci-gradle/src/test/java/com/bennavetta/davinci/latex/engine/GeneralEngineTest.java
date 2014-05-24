package com.bennavetta.davinci.latex.engine;

import static org.truth0.Truth.ASSERT;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.bennavetta.davinci.latex.LaTeXCompileSpec;
import com.bennavetta.davinci.latex.LaTeXCompiler;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.common.io.Resources;

/**
 * Defines tests that all {@link Engine} implementations should pass.
 *
 */
@RunWith(Parameterized.class)
public class GeneralEngineTest
{
	@Rule
	public final TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Parameters
	public static Collection<Object[]> engines()
	{
		ImmutableList.Builder<Object[]> engines = ImmutableList.builder();
		
		for(Engine engine : pdfTeXEngines())
		{
			engines.add(new Object[]{engine});
		}
		
		return engines.build();
	}
	
	public static Iterable<Engine> pdfTeXEngines()
	{
		ImmutableList.Builder<Engine> engines = ImmutableList.builder();
		
		engines.add(new PDFTeXEngine()); // default
		engines.add(new PDFTeXEngine("latex")); // in TeXLive, 'latex' should be 'pdflatex'
		
		PDFTeXEngine eightBit = new PDFTeXEngine();
		eightBit.set8Bit(true);
		engines.add(eightBit);
		
		return engines.build();
	}
	
	@Parameter
	public Engine engine;
	
	public LaTeXCompileSpec buildSpec(String source) throws IOException
	{
		LaTeXCompileSpec spec = new LaTeXCompileSpec();
		
		File sourceDir = temporaryFolder.newFolder("src");
		File destDir = temporaryFolder.newFolder("dest");
		
		File sourceFile = new File(sourceDir, "document.tex");
		Files.write(source, sourceFile, Charsets.UTF_8);
		
		spec.setLatexFile(sourceFile);
		spec.setDestinationDir(destDir);
		spec.setEngine(engine);
		return spec;
	}
	
	@Test
	public void basicCompilation() throws IOException
	{
		LaTeXCompileSpec spec = buildSpec(goodLaTeX());
		
		LaTeXCompiler compiler = new LaTeXCompiler();
		ASSERT.that(compiler.execute(spec).getDidWork()).isTrue();
		
		// Make sure the destination directory is respected - the only file in the source directory should be the LaTeX file
		ASSERT.that(spec.getLatexFile().getParentFile().listFiles())
			.asList().has().exactly(spec.getLatexFile());
		
		// Make sure files were generated
		ASSERT.that(spec.getDestinationDir().list().length).isInclusivelyInRange(1, Integer.MAX_VALUE);
	}
	
	public String goodLaTeX() throws IOException
	{
		return Resources.toString(Resources.getResource("tex-sources/good.tex"), Charsets.UTF_8);
	}
}
