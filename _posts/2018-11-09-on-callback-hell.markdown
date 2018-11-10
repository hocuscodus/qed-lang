---
layout: post
title:  "On callback hell"
date:   2018-11-09 09:10:47 -0400
categories: QED update
---
I am planning to write an article in the coming months about callback hell, where I will propose a new approach which is imho much better (even than JS promises/async/await).

Suffice it to say for now there are tangible benefits about functions that are of dual synchronous/asynchronous nature depending on how they are invoked. In QED, the following lines do exactly as expected:

    println("Starting...");
    Timer(4000); // wait four seconds
    println("Timer 1 done!");
    Timer(3000); // wait three other seconds
    println("Timer 2 done!");

However, calling Timer() using new makes Timer calls asynchronous (without creating threads in order to keep generated JS code compatibility). Here is an asynchronous version.

    println("Starting...");
    new Timer(4000) -> println("Timer 1 done!");
    new Timer(3000) -> println("Timer 2 done!");

This will print "Timer 2 done" 1 second before "Timer 1 done". Both timers work asynchronously. These two examples work well on the [online demo page](https://qed-lang.org/demos) should you try them out.

So the way to sequentially execute asynchronous tasks will be something like this:

    void asyncTask(...) {
        // execute all async functions synchronously (without using new)
        asynFn1(...);
        asyncFn2(...);
        ...
        return();
    }

    // use new here to make whole task async
    new asyncTask(...) -> <do something when complete>;
    println("Async task started");
    ...

Much more detail will be provided in the upcoming article...
