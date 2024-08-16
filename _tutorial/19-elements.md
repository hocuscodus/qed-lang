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

## When is the UI generated at runtime?

As already specified, the QED script is run. When completed, it processes any queued event. When done, it enters the event loop to process incoming events.

The one thing that was not told yet is the UI is generated, from the UI elements, between having processed queued events and entering the event loop. This way, incoming UI events can be processed.

Whenever the application is about to re-enter the event loop after processing UI events, the UI is regenerated to reflect the potential changes.
