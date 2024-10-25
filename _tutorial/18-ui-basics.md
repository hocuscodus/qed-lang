---
title: "UI basics"
permalink: /tutorial/ui-basics/
---

Starting with this section, we'll explain how to code a UI in QED.

QED has a built-in mechanism that greatly facilitates the encoding of user interfaces. To achieve such power, besides the C-like syntax used for business logic, QED implements a different but simple complementary syntax for UI encoding.

This alternative syntax, better adapted for UI design, is more akin to markup languages such as HTML so while it is different, it won't sound like an alien dialect. In fact, it will rather look familiar in many aspects. It works hand in hand with the business logic syntax and the internal event handling system to truly enhance the GUI development process.

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

1. run business logic until first class call (or end of main script)
2. process pending events
3. enter event loop to wait for any system event
4. go back to step 2 unless `return` is called in the main script
5. application ends with integer return code

The one thing that was kept secret until now is the UI (if any) is generated, from the UI elements, right in between having processed queued events and entering the event loop. So here is the *real* process:

1. run business logic until first class call (or end of main script)
2. process pending events
3. ***New!* Regenerate UI if UI elements are defined**
4. enter event loop to wait for any system **or UI** event
5. go back to step 2 unless `return` is called in the main script
6. application ends with integer return code

Whenever the application is about to re-enter the event loop after processing events (UI-based or others), the UI is regenerated to reflect the potential changes.
