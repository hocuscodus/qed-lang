---
title: "Concurrency"
permalink: /tutorial/concurrency/
---

QED has native functions such as `println()` but also has native classes too.

The `Timer(millis)` class stops the current code execution for a number of milliseconds (the parameter) before resuming it. Other tasks may run during the suspension period so the application is not hung. If the `millis` argument is 0, the application is still suspended but resumes whenever it processes the event loop.

This class can be called as object type or function.

// as call

// as object

Both do not provide the same output. When invoked as a call, the `Timer()` class stops execution and resumes it after 1 second, printing `Timer ends` and `Program Done` afterwards. However, both printouts are reversed in the object version. How come?

As we know, the `Timer` object is returned its execution is suspended, so, in this case, just after having scheduled when it resumes. The main program execution then resumes when it has the object, therefore it immediately prints `Program Done`. Even when this code is done, QED enters the event loop and waits 1 second for the upcoming `Timer ends` event.

The `Timer` class is a prime example of a class invoked as an object or as a call. Asyncbronously or synchronously. In other words, the design of QED classes *enables concurrency for free*!

Other native classes include .....
{: .notice--info}

If you embed a native class into your own class *as a call*, your class can now be called synchronously or asynchronously. Using it *as an object* will provide it when the native class *call* is made.

Never forget to call `return` in your wrapper classes else invoking them as calls will suspend forever at the end of their execution.
{: .notice--info}

You can use your class as a call to if you want a synchronous process.

You can embed your own wrapper into any number of enclosing wrapper classes as calls. The sync/async rules will still apply.
{: .notice--info}
