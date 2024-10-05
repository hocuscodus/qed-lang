---
title: "Async classes"
permalink: /tutorial/async-classes/
---

QED has native functions such as `println()` but also has native classes too. Most of these classes are of asynchronous nature.

The `QedTimer(millis)` async class suspends the current class execution for a number of milliseconds (defined by `millis`). Then it returns.

The `QedTimer` class can be instantiated. Upon return the event handler if any will be invoked.

{% include sandboxframe.html qedsrc="tutorial/async1.qed" code="67" gui="0" height="110px" %}

As seen before, "Program done" is printed first, but the event loop is still lurking and processes the `QedTimer` return event a second later.
{: .notice--info}

Like any QED class, it can be invoked as a call as well.

{% include sandboxframe.html qedsrc="tutorial/async2.qed" code="67" gui="0" height="110px" %}

While waiting, any incoming event is still processed. Let's extend the wait to two seconds for the call. Before making this call though, let's create a `QedTimer` instance that lasts one second. The event handler of the `QedTimer` object will be invoked *during* the wait of the `QedTimer` call.

{% include sandboxframe.html qedsrc="tutorial/async3.qed" code="67" gui="0" height="110px" %}

The `QedTimer` class is a prime example of a class invoked as an object or as a call. Asynchronously or synchronously. In other words, the design of QED classes *enables concurrency for free*!

As QED is being release, other native async classes will become available, to load a file or an image, make REST calls, etc.
{: .notice--info}

Invoking many async classes as instances will make them run concurrently. To spawn a number of similar async class instances - let's call them tasks from now on, it is best to use an implicit array. Let's start 10 random timers that last up to five seconds.

{% include sandboxframe.html qedsrc="tutorial/async4.qed" code="67" gui="0" height="110px" %}

See how an implicit array of class instantiations looks nice and readable.
{: .notice--info}

This works but directly using a native async class is limited output-wise. To achieve a more effective result, define a custom wrapper class on top of a `QedTimer` *call*. Embedding at least one async class into wrapper *as a call* makes it asynchronous too! Let's rewrite the previous example with such a wrapper async class to provide a much better output.

{% include sandboxframe.html qedsrc="tutorial/async5.qed" code="67" gui="0" height="140px" %}

You can embed your own wrapper into any number of enclosing wrapper classes as calls. The sync/async rules will still apply.
{: .notice--info}

Let's say you want to pick the first timer result and cancel the others. To do so, wrap your multiple timer logic into a `GetFirstResult` class and use `return` in the timer event handler. A `GetFirstResult` class call would then return upon the first `QedTimer` handling. Upon return, all other timers will be automatically cancelled.

{% include sandboxframe.html qedsrc="tutorial/async6.qed" code="67" gui="0" height="230px" %}

A more common scenario is to do continue processing after finishing a set of async tasks. To achieve it requires another native class, `QedWaitValues`. A call to this class, with an array of new async instances in parameter, will return upon completion of all instances. Its return value is another array, containing the returned values of the instances.

{% include sandboxframe.html qedsrc="tutorial/async7.qed" code="67" gui="0" height="230px" %}

Never forget to call `return` in your wrapper classes else invoking them as calls will suspend forever at the end of their execution.
{: .notice--info}

If you have a custom asynchronous JS method and want to wrap it into an async class (which can then become a blocking call in QED code), you have to use embedded JS code where the callback calls the inner `_qedCallback(<className>$this, <returnValue>)` QED function to send the returned value (`null` if `void`) for the QED class object and resume execution. For instance, let's create a `Sound` QED class on top of the native JS `Audio.play()` call with the `onEnded` handler that will resume the QED code execution.

{% include sandboxframe.html qedsrc="tutorial/async8.qed" code="60" gui="0" height="345px" %}
