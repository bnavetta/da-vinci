NAME := example

LATEXMK_OPTS := -pdf -pdflatex="xelatex %O %S" -latex="xelatex %O %S" -r pythontex-latexmkrc

all: $(NAME).pdf

.PHONY: clean preview

$(NAME).pdf: $(NAME).tex
	latexmk $(LATEXMK_OPTS) $(NAME)

clean:
	latexmk -C $(LATEXMK_OPTS) $(NAME)
	rm -rf pythontex-files-$(NAME)

preview:
	latexmk -pv $(LATEXMK_OPTS) $(NAME)
