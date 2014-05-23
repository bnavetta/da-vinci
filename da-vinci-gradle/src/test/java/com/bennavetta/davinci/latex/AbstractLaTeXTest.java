package com.bennavetta.davinci.latex;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class AbstractLaTeXTest
{
	@Rule
	public final TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	protected String documentName()
	{
		return "document";
	}
	
	protected File texFile()
	{
		return new File(temporaryFolder.getRoot(), documentName() + ".tex");
	}
	
	protected File texFile(String source) throws IOException
	{
		File texFile = texFile();
		Files.write(source, texFile, Charsets.UTF_8);
		return texFile;
	}
}
