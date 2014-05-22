package com.bennavetta.davinci.latex.internal;

/**
 * The XeTeX engine, usually available as {@code xelatex}
 */
public class XeTeXEngine extends PDFTeXEngine // extend since they have similar CLIs
{
	public XeTeXEngine()
	{
		super("xetex");
	}
}
