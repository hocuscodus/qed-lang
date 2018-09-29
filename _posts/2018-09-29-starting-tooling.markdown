---
layout: post
title:  "Tooling QED"
date:   2018-09-29 08:00:47 -0400
categories: QED
---
Now that QED can show its features in the demo page, the next step is to give QED the required tooling. Any decent language must offer nowadays debugging, syntax highlighting, back and forward references, all those cuties that thoroughly simplify programmers lives.

I looked at the different alternatives to achieve that huge step properly withouth reinventing the wheel and take forever to complete. After weighting various options, I decided to go all in on [Xtext](https://www.eclipse.org/Xtext/). I know Xtext is mostly used for DSLs, but then I saw the Xtend language, integrated through Xtext. Xtend grammar is way more extensive than QED's, and Xtend tasks are far more complex. I then got confidence that QED can really benefit form Xtext and is simple enough to be integrated to Xtext. That requires lots of work but the payoff is huge: debugging, integration with Java, semantic coloring,
code completion, cross-reference handling, validation, error checking, hover information, type hierarchy and last but far from least, LSP integration.

So I started to convert the QED code to Xtext. I rewrote the LALR grammar to an ANTLR LL(\*) format and now, I am starting to generate runtime code with it. Things are going well so far, but I cannot tell yet when this will be complete. I'll keep you posted on my whereabouts.

The one thing that really helps me though is the simplicity of QED grammar. It simply consists of well-known literals, operators (with usual precedence rules), field and function declarations, statements, package/import functionality and lists of GUI attributes. That's it! There is no flow keyword (if, else, for, switch-case, break, return, etc.) or separate type/class/interface/trait definition. Thus I am hoping the integration with Xtext shall be done in a reasonable amount of time. As I said, I'll keep you posted.
