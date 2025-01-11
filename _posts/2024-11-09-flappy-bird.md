---
title:  "Flappy bird, in QED"
date:   2024-11-09 00:00:00 -0400
author: "Martin Savage"
categories: Demo
---
I just coded [a first game in QED](https://github.com/hocuscodus/qed-lang/blob/gh-pages/sandbox/demos/flappybird.qed), a port of a javascript Flappy Bird!

To [play online](https://qed-lang.org/demos), go to the last demo and press Run. Click the mouse on the canvas to play. You may also use the Space, W or Up Arrow keys to play.

The peculiarity of this implementation is that there is no state (getReady-play-gameOver) nor state machine to handle the update-draw code. It is rather strictly procedural, which is uncommon in an event-based browser env.

It is basically a while loop (line 217) on GetReady(), Play(), GameOver() calls. The various components are coroutines, so they execute in their own environment. For instance, the bird has two while loops: waiting to play and falling/flapping (then its fields are set for death). All objects are cleanly encapsulated.

As a bonus, there is 42% less lines of code than the original JS version.

I know this OO strategy is against good practices when it comes to maxing game performance but for such a simple game, it still runs flawlessly while being easier to maintain and expand.

