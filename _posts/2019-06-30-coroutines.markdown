---
layout: post
title:  "A 'new', easy coroutine model"
date:   2019-06-30 09:10:47 -0400
categories: Article
---

I encountered coroutines by accident.

While designing the QED language, I was keen on making it great for UI development and simpler programming concepts as a whole but never considered concurrency. While I know it is an important topic, it was too distracting to me and I had no expertise on its various implementation flavors. So I bluntly ignored concurrency in QED design. I told myself if it were becoming in demand later I may then ponder over it. But until recently, the idea of handling threads and al on top of my core concepts was too much a burden.

When I started to showcase QED last August, I  also opened myself to subjects tantalizing the programming language community. Reading about callback hell, or what color is your language, enlightened myself. I realized my programming language, without any thread-related feature, was so close to solving cooperative multitasking elegantly. This is where I finally learned the word coroutine (go ahead, mock me) and started to inspect the many implementations. I deeply felt I had to provide mine for QED. And here it is, finally...

So what novel set of co-prefixed keywords did I devise to implement coroutines in QED? Well, there is none actually. By only tweaking a small set of well-known programming language assumptions, as done in QED, and adding few system functions, we provide a nifty coroutine model.

At the end of this article you should understand how the following code works (inspired from Simon Tatham's article). You may already assess how pretty readable it looks.

Questions anyone?

- So you don't need to define a separate type system. Good for you... What's in it for me?

- It will be very handy when you add GUI-related code to a function. You will be able to invoke your GUI component as a form or a widget by calling the function with or without the new operator. I'll explain it in a future article.

- Ok... I don't understand a word of what you just said. I repeat then: so what?

I would have been floored by the question... until today. There is now a startling use case for types as functions: easy concurrency management.

Functions and types have traditionally be complementary but different, like matter and energy, space and time, gravity and acceleration.
Lets do a thought experiment. This is where I had to challenge some well-known programming rules

I hope such an approach, if popular, will help democratize the use of coroutines. I also hope in  a few years no more developer will discover them accidentally.
