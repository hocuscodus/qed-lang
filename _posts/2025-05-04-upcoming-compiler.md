---
title:  "The QED compiler coming soon!"
date:   2025-05-04 00:00:00 -0400
author: "Martin Savage"
categories: QED Update
---
Well, I thought I would have given an update sooner. I underestimated the amount of work required to generate a clear JS output from QED source code. After a five month, heart-surgery-level refacto on all compiler passes, the QED compiler slowly awakens to produce visual GUI again. This morning, I even managed to get clear JS for the [Flappy Bird demo](https://qed-lang.org/demos#flappy-bird) that *almost* works!

It will allow to write much better end-to-end unit tests, give much better hints to V8 optimization engine and facilitate JS output code browser debugging.

So, in the coming weeks, I'll fix the remaining wrong code for FB, do some other quick optimizations, test will *all* the website demos, replace the current website QED compiler with this new one and....... *release a first QED compiler*!

This first compiler won't be perfect at all but at least, it will allow you to test it with small programs and see a clear JS output that you can use in your own projects. It will be available in the [Download section](https://qed-lang.org/download).

After releasing the compiler, the next step would be another cleanup in the compiler code, which will allow me to finally release its open source code... a matter of a few months hopefully.

Also, from now on after this huge refacto, QED update posts should arrive more frequently!
