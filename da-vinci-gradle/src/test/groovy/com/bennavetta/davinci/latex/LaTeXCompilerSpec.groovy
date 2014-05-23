package com.bennavetta.davinci.latex

import org.gradle.internal.os.OperatingSystem
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class LaTeXCompilerSpec extends Specification
{
	@Rule
	final TemporaryFolder temporaryFolder = new TemporaryFolder()

	protected String getDocumentName()
	{
		return "document";
	}

	protected File getLatexFile()
	{
		return temporaryFolder.newFile("${documentName}.tex")
	}

	protected File latexFile(String source)
	{
		latexFile.text = source
		latexFile
	}

	Engine engine = Mock()

	def "compiler captures standard output"()
	{
		given:
		engine.getExecutable() >> OperatingSystem.current().findInPath("echo")
		engine.generateArgs(_) >> {exec, compile -> exec.args "Hello, World!"}
		def compileSpec = new LaTeXCompileSpec(latexFile: latexFile("Hello"), engine: engine)
		def compiler = new LaTeXCompiler()

		when:
		compiler.execute(compileSpec)

		then:
		compiler.compilerOut == "Hello, World!"
	}
}
