---
title:  "A first available QED compiler!"
date:   2025-06-01 00:00:00 -0400
author: "Martin Savage"
categories: Update Release
---

Finally, [here is the first release of the QED compiler](/download).

This first compiler is quite crude at this point though. There is no fancy compiler option available now. The only thing it does is to transpile QED code to runnable Javascript.

It first automatically compiles the QED library into JS, then appends the JS equivalent of the .qed file given in parameter, to standard output. The `>` shell operator must be used to redirect it to a .js file that can be referenced by a web page.

I tested the compiler on most examples presented in this website. Although it works, I am certain there are lots of bug fixes to do on it. Also lots of compiler improvements that will come over time.

But, it is here for real now, so I am glad. The next step will be to clean up the github source to make it ready for everyone to see and use.

Stay tuned though as in the next few days, I'll upload another post, this time on what differentiates QED from other languages.
