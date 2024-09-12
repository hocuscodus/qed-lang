---
title: "Elements and Tags"
permalink: /tutorial/elements/
---

Before diving into live examples, let's define the syntax first.

You can define a user interface at the end of the QED application script.

```
// Main QED script

... classes, functions and business logic script...

// User interface
<tag: expr; tag: expr;>
<tag: expr;
  // child UI elements defined after all tags
  <tag: expr;>
  <tag: expr; tag: expr;>
>
<tag: expr;>
```

So after the QED program, there is a list of 0 to n UI elements. A UI element is a list of tag/expression value pairs, delimited by angle brackets (`<>`). Each tag/expression value pair consists of a tag identifier, followed by a colon (`:`) character, then an expression value associated to the tag, ending with a semicolon separator (`;`).

```
tagName: expressionValue;
```
Each UI element may also contain child UI elements, defined as a list located after the tag/expression value pairs and before the parent element closing angle bracket. So a UI element is the following, enclosed under angle brackets.
```
<tag-value-pair-list child-UI-element-list>
```
Both lists may contain 0 element.

There are also extra direction symbols in UI element definition that we'll see later.
{: .notice--info}

The good news is, with a handful of tags, you can render all sorts of sophisticated user interfaces! But even if they are few, you have to master them to conquer those nifty looks. Let's review them.

## When is the UI generated at runtime?

The following steps were what was already specified when a QED application is executed:

* business logic is run
* pending events are processed
* event loop is entered to process future system events

The one thing that was kept secret until now is the UI (if any) is generated, from the UI elements, right in between having processed queued events and entering the event loop. So here is the *real* process:

* business logic is run
* pending events are processed
* ***New!* If UI elements are defined, UI is generated**
* event loop is entered to process future system **and UI** events

Whenever the application is about to re-enter the event loop after processing events (UI-based or others), the UI is regenerated to reflect the potential changes.
