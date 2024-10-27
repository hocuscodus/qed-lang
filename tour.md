---
layout: single
title: Quick tour of QED
permalink: /tour/
---

The QED programming language is a take on the age-old GUI problem, for those like me who still consider it unsolved.

Elucidating it means, to me, being able to encode a GUI application as easily as a text-based app, with about the same number of lines.

The only way I can think of to reach that goal is to design a language that perfectly integrates GUI core principles in its intrinsic design.

Without further ado, here is how these GUI concepts get gradually infused within the QED language.

Let's assume as a starting point that QED has a basic syntax close to C for flow keywords, primitive types and function declarations.

Unlike C though, QED has the concept of a **class** that is declared just like a function, except for an *uppercase first letter* in the name to make the distinction. These classes can be instantiated with `new`. Instance members are accessed with the dot operator.

{% include sandboxframe.html qedsrc="tour/instances.qed" gui="0" height="230px" %}

The QED classes are designed like functions because *we can also invoke them as such*. Therefore, classes have a **dual nature** as both types and functions.

{% include sandboxframe.html qedsrc="tour/calls.qed" gui="0" height="130px" %}

Instantiated classes can also provide return values as events. When a QED script is done, it enters an **event loop** (a first fundamental GUI concept!). Adding a event handler with the -> operator on a class instance allows the event to be processed in the event loop.

{% include sandboxframe.html qedsrc="tour/handlers.qed" code="60" gui="0" height="130px" %}

Unlike plain function calls, the `return` statement must be invoked to exit class calls. If a call does not encounter `return` in its execution, it also enters an event loop, waiting for an event that will eventually call `return`.

{% include sandboxframe.html qedsrc="tour/noreturn.qed" gui="0" height="250px" %}

The real usefulness of this dual nature (call/instantiation) first applies to QED native classes, which are wrappers to *asynchronous JS calls*. The QedTimer native class, for instance, can be called with `new` (asynchronous) or without `new` (blocking). No need for an external async/await mechanism.

{% include sandboxframe.html qedsrc="tour/concurrency.qed" gui="0" height="210px" %}

This introduces another fundamental GUI concept, **parallelism**. GUI components are parallel (asynchronous), whereas forms/dialogs are blocking (synchronous).

We'll present live examples of this soon but first, we need to introduce the last fundamental GUI concept, the **UI definition**.

For a GUI element, you first write your code, if any, for its business logic (model/state/...). Then you can write GUI code using a tree of markup UI elements such as this one below. Each time the application enters the event loop (at an unfinished class call, at the end of the script or when events are processed), the UI defined in markups is refreshed.

{% include sandboxframe.html qedsrc="tour/gui.qed" gui="97" height="230px" %}

To encapsulate a business logic/UI markups combo into a component, just wrap it into a reusable class. Any *class instantiation* becomes a **component**. Let's package the previous countdown example as a reusable component (or course that also applies to any well-known GUI elements such as buttons, check boxes, lists...).

{% include sandboxframe.html qedsrc="tour/components.qed" gui="60" height="430px" %}

To create various forms, just like for components, encapsulate their business logic/UI markups into a class, but use *blocking class calls* to invoke them as **forms**. As an exercise, let's define a recursive dialog form.

{% include sandboxframe.html qedsrc="tour/forms.qed" gui="50" height="520px" %}

You can have any number of overlapping dialogs, as they are directly linked to the class call stack.

That's all! As you see, armed with such a language design fully integrating GUI concepts, you can easily build any sophisticated GUI apps!
