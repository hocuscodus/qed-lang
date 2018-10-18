---
layout: post
title:  "Precisions on QED documentation"
date:   2018-10-16 20:00:47 -0400
categories: QED
---
I got today an interesting question from redditor /u/AsIAm: "Btw, is the documentation for QED up to date or the video is? I was a bit confused because they seemed to describe different things..."

You may be surprised but the [video](https://www.youtube.com/watch?v=a2OhmxXQbwo) and the [quick tutorial](/qedintro) describe the exact same programming language, through very different angles.

I worked on the video first. I wanted to present a formal definition of QED, as I think great languages should be based on rigorous, scientific principles. Any great science starts from a minimal set of axioms and derive lots of wonders from this set. To me, good programming languages should follow this path as well. This is why the video first define QED axioms (variables, functions, expressions, primitive elements, flow-control tree, GUI attributes) and then develop novelties from them (automatic if/for/while, tree structure instead of stack, types as functions, parallel GUI/BL code). I wanted to show QED is based on a very solid ground and that there's not much to learn to master it.

I then developed this website and wrote the quick tutorial page. I did not want to take the same approach for this page as on the video. The video requires a lot of attention span to truly understand it, may look dry for certain people due to its scientific rigor and requires the watcher a lot of time to see the whole subject.

What I wanted with this intro, as with the [demo](/demos) page, was the reader to grasp the QED differentiator immediately. This is why I started the text by what I described at the end of the video: GUI attributes and GUI/BL parallel code (from what I know, no other language has this feature). I also outlined the function/type equivalence at the end of the text (the button as a field rather than a form). To keep it simple, I skipped many interesting parts of the language (if/for/while automation, primitive elements...) but I gave the link to the video, for those with the will and courage to discover more.

Still, I am fully conscious the documentation is still very scarce and not really usable yet. However, first things first, I must provide a true, downloadable product and required tooling before adding real meat to the doc. I have good news on this side: working with [Xtext](https://www.eclipse.org/Xtext/) so far has been a charm, things are advancing well.

I can't wait for the day QED will be fully documented. Somewhere is Q1 2019 I still hope! Providing good documentation is an art, I must do it well. I'll let you know for sure when done with it.
