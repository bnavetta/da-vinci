package com.bennavetta.davinci.latex.internal;

import com.bennavetta.davinci.latex.Engine;
import com.bennavetta.davinci.latex.LaTeXCompileSpec;
import com.google.common.collect.ImmutableList;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.process.ExecSpec;
import org.gradle.util.GFileUtils;

import java.io.File;
import java.util.List;

/**
 * The pdfTeX engine, usually available as {@code pdflatex}.
 */
public class PDFTeXEngine implements Engine
{
	private String executableName;

	private boolean eightBit;

	public PDFTeXEngine()
	{
		this("pdflatex");
	}

	public PDFTeXEngine(String executableName)
	{
		this.executableName = executableName;
	}

	public boolean is8Bit()
	{
		return eightBit;
	}

	public void set8Bit(boolean eightBit)
	{
		this.eightBit = eightBit;
	}

	public String getExecutableName()
	{
		return executableName;
	}

	public void setExecutableName(String executableName)
	{
		this.executableName = executableName;
	}

	@Override
	public String getName()
	{
		return "pdfTeX";
	}

	@Override
	public File getExecutable()
	{
		return OperatingSystem.current().findInPath(executableName);
	}

	@Override
	public void generateArgs(ExecSpec execSpec, LaTeXCompileSpec compileSpec)
	{
		execSpec.args("-output-directory=" + GFileUtils.canonicalise(compileSpec.getDestinationDir()).getPath());
		if(eightBit)
		{
			execSpec.args("-8bit");
		}
		execSpec.args(GFileUtils.canonicalise(compileSpec.getLatexFile()).getPath());
	}

	@Override
	public boolean needsRerun(String stdout, String stderr)
	{
		List<String> messages = ImmutableList.of("There were undefined references.");
		for(String message : messages)
		{
			if(stdout.contains(message) || stderr.contains(message))
			{
				return true;
			}
		}
		return false;
	}
}
