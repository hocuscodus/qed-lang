---
title:  "The gist of QED (DRAFT)"
date:   2025-05-03 00:00:00 -0400
author: "Martin Savage"
categories: QED article
permalink: gist.html
---
The QED programming language has an ambitious goal: to simplify programming in general (and web development in the current version). As it is a bold claim, it needs to be addressed accordingly, hence this post. How does QED achieve it?

Many languages offer the following seemingly disparate features:

- the core language
- concurrency
- classes
- arrays
- events
- UI
- coroutines

Usually, these features are loosely coupled if not orthogonal.

QED is arguably is the first language to operate *a next-level fusion* of these features, yielding tangible benefits for the developer:

- more flattened learning curve
- noticeably smaller code
- enhanced code readability

Let's show how each feature tightly integrates using runnable demos. Starting with concurrency, in QED, there is no reason why an asynchronous function blocking call has to be more explicit than a synchronous function call.

{% include sandboxframe.html qedsrc="gist/blockingcall.qed" gui="0" height="150px" %}

No need for extra keywords such as async/await that only clutter the code. The sole thing that tells QED function Fn is async is *its uppercase first letter*. Note there is another blocking call inside it to `QedTimer(delayMs)`, another (core library this time) async function.

Does this mean a non-blocking call is more complex? Not really. The well-known `new` keyword prefixes a non-blocking call. Handling the `Fn(delay)` return value is managed by the `->` operator, which right operand defines an *event handler*. The `_ret` keyword holds the future return value.

{% include sandboxframe.html qedsrc="gist/asynccall.qed" gui="0" height="150px" %}

The use of the `new` keyword seems strange since, by convention, it is used for creating objects. Well, no worries, this is indeed the case with QED too! Async functions *are in fact classes* where fields and methods can be defined.

{% include sandboxframe.html qedsrc="gist/asyncclass.qed" gui="0" height="170px" %}

A plain class in QED defines fields and methods. No need to call `return` in the class logic.

{% include sandboxframe.html qedsrc="gist/class.qed" gui="0" height="200px" %}

An async function/class can be used as a coroutine too, using native `QedYield()` blocking calls within its code. The main script controlling the coroutine uses `qedResume(coroutine)` calls that return `true` until the coroutine ends.

{% include sandboxframe.html qedsrc="gist/coroutine.qed" gui="0" height="300px" %}

Coroutines usually work in multiple instances. These instances can be created using a dynamic array expression. In QED, these are defined by listing dimension expressions (which can have indexes such as `index::dim`), followed by an initializer expression, all separated with spaces. See some dynamic array uses here.

{% include sandboxframe.html qedsrc="gist/array.qed" gui="0" height="240px" %}

When using dynamic arrays to define multiple coroutines, the `qedResume(array)` calls is applied to the entire array, enabling coroutines to run concurrently.

{% include sandboxframe.html qedsrc="gist/coroutines.qed" gui="0" height="300px" %}

In QED, one may also encode a declarative UI using a simple markup-like syntax with tags and values.

{% include sandboxframe.html qedsrc="gist/gui.qed" gui="97" height="150px" %}

Mixing business logic and declarative UI can be done by putting the latter at the end. The UI can then freely use the business logic variables and functions in their tag values.

{% include sandboxframe.html qedsrc="gist/guibl.qed" gui="97" height="200px" %}

As seen here, the UI is refreshed upon going to the event loop (at each `QedAnimation()` call - an async function thin wrapper of JS's window.refreshAnimationFrame() - in the last example).

A declarative UI can also be declared at the end of an async function/class body. This way, many instances can be shown using their internal UI. These classes with UI are considered UI components in an app.

If the UI component is a coroutine, it can be used as a sprite for game development. Here there are three circular sprites having their own independant movement.

{% include sandboxframe.html qedsrc="gist/lights.qed" gui="97" height="340px" %}

Using sprites allows simple games such as Flappy Bird, with "Get Ready/Play/Game Over" states encoded as blocking calls rather than a state machine, [to be coded in under 200 lines of code](https://qed-lang.org/demos#flappy-bird).

If the UI component is not a coroutine, then it can be used as a widget. You can define your own widgets such as the simple checkbox shown here. Creating instances is done through non-blocking calls with `new`. The same event mechanism like we've seen before with the `->` operator is reused in the UI context to handle user interaction.

{% include sandboxframe.html qedsrc="gist/checkbox.qed" gui="50" height="200px" %}

QED will soon include an assortment of frequently used widgets so they won't need to be encoded manually. The QedTextButton is already available, as shown here.

{% include sandboxframe.html qedsrc="gist/components.qed" gui="60" height="200px" %}

A widget can be used as a form as well, which needs to be dismissed to let the application continue. In this case, just replace the non-blocking call by a blocking one (that can fully be used inside an `if` or `while` statement to have a more readable flow control).

{% include sandboxframe.html qedsrc="gist/forms.qed" gui="97" height="470px" %}

QED will also incorporate the most used forms such as the yes/no popup here, enabling the last demo to only consist of the last five lines.

So as you can see, the natural fusion of so many concepts makes the gist of QED.