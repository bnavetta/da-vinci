#!/bin/bash

echo "Running pdfLaTeX... This should warn about undefined references"
pdflatex document

echo "Running Biber..."
biber document

echo "Running pdfLaTeX again... There shouldn't be any warnings about undefined references"
pdflatex document
