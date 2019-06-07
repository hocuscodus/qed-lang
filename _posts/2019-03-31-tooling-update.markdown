---
layout: post
title:  "QED Tooling Update"
date:   2019-03-31 09:10:47 -0400
categories: QED Tooling update
---
Last month I was geared to get an end-to-end working version of QED, built with [Xtext](https://www.eclipse.org/Xtext/index.html). The Java code generator was working fine and I had to integrate the QED library, GUI engine and JS code generation to have a new POC with Xtext.

Then, I started to think about the port to Xtext itself. It was a bumpy road to generate the Java code but I learned a lot. Besides, Xtext was designed to provide me all tooling goodies (debugging, FFI, smart editing, highlighting, LSP integration and so on). But there is work to do to get all these. I wasn't sure I could have it easily... The end-to-end version became less attractive; even if I get it done, if debugging would never work, the Xtext investment would be close to worthless. In other words, QED would stay forever a toy language, never to be used in the real world tackling real issues.

So I changed the course of my development. Before heading to the end-to-end version right now, I started the real tooling work on QED.

The priorities are, in order:
1) debugging
2) FFI (with Java, eventually JS)
3) Standalone and Eclipse plugin releases
4) LSP integration

Out of these four, debugging is the toughest part. I worked on it the whole month. It was painful, but I am now very happy to report that it works. It was so cool to stop at breakpoints and watch variable contents. That gives the impression QED is more a real language now!

I am starting FFI now. It will still be bumpy but I also learned a lot about FFI last month and I am expecting it to work fine (for Java) during April.

The other two priorities look easy, so hopefully by the end of the month, all four should be covered.
