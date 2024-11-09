---
title:  "Flappy bird, in QED"
date:   2024-11-09 00:00:00 -0400
author: "Martin Savage"
categories: Demo
---
I just coded [a first game in QED](https://github.com/hocuscodus/qed-lang/blob/gh-pages/sandbox/demos/flappybird.qed), a port of a javascript Flappy Bird!

To [play online](https://qed-lang.org/demos), go to the last demo and press Run.

The peculiarity of this implementation is that there is no state (getReady-play-gameOver) nor state machine to handle the update-draw code. It is rather strictly procedural, which is uncommon in an event-based browser env.

It is basically a while loop (line 268) on GetReady(), Play(), GameOver() calls (line 192). The various components are coroutines, so they execute in their own environment. For instance, the bird has three while loops: waiting to play, play, and falling (then its fields are set for death). All objects are cleanly encapsulated.

As a bonus, there is 33% less code than the original JS version.

I know this OO strategy is against good practices when it comes to maxing game performance but for such a simple game, it still runs flawlessly while being easier to maintain and expand.

