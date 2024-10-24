---
title: "Coroutines"
permalink: /tutorial/coroutines/
---

QED has no thread support but rather implements coroutines in the form of async classes where you define suspension points.

Within a class body, calling the `QedYield` class returns directly to the first caller that invoked it with `new`. To resume the execution of the code following the `QedYield` call, call `qedResume(object)`. The first example shows the main script and an object where execution switches from one to the other using `QedYield()` (from the `coroutine` object) and `qedResume(coroutine)` (from the main script).

{% include sandboxframe.html qedsrc="tutorial/coroutines1.qed" code="57" gui="0" height="300px" %}

You may have remarked that `qedResume(coroutine)` is under a while loop. This function returns true if the object was stopped using `QedYield` and false if not (like the `return` call at the end). This is why the while loop exits at the end of the `coroutine` execution.

The coroutine functionality can also be used to create generators. The next example shows a Fibonacci series generator, used to list the first fifteen numbers. This time, the `QedYield()` call is under the while loop, as it is normal for a generator class. `QedYield` always returns true, which makes it ideal to embed under a `while` loop, as it is a common use case.

{% include sandboxframe.html qedsrc="tutorial/coroutines2.qed" code="67" gui="0" height="300px" %}

Another common use case is many coroutines coexisting together, each doing its particular job. In this case, gather them in an array and use `qedResume(array)` to resume execution *on each* coroutine in the array with one call. Calling `qedResume` on an array returns true if one element was suspended at `QedYield()`, false otherwise. The following example is an unusual implementation of the popular [fizz-buzz](https://en.wikipedia.org/wiki/Fizz_buzz) problem, using coroutines for the "Fizz" signal on numbers divisible by 3 and the "Buzz" signal on numbers divisible by 5.

{% include sandboxframe.html qedsrc="tutorial/coroutines3.qed" code="80" gui="0" height="360px" %}

In this example, an explicit array is used. It works equally well for implicit arrays as well. The "Bouncing balls" in the demo page uses an implicit array to create 30 `Ball` coroutines.
{: .notice--info}

Since `QedYield` is a class, should it be instantiated with `new`? So far we only used `QedYield` calls, right? Yet there are some use cases where `new QedYield()` is very useful. For instance, imagine you are calling a class having potentially lengthy async calls and you want to cancel it after a given delay. To do so, in the main script, declare an `interruptCount` object. This object will be initialized at the start of the lengthy class code as a `new QedYield()` with an event handler. The `new QedYield()` will stop its execution without invoking its handler, so the lengthy code can start safely. Upon reaching the delay (implemented as a `new QedTimer(delay)` in the main script), the `qedResume(interruptCount)` call is made, triggering the `new QedYield()` event handler which forces the lengthy call to return without completing.

{% include sandboxframe.html qedsrc="tutorial/coroutines4.qed" code="70" gui="0" height="310px" %}

This technique is used in the Taco Hotel code from the demo page, where there is an `Exit` button that the user triggers to cancel a `GetTransaction` class call.
{: .notice--info}

