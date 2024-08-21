---
title: "Components"
permalink: /tutorial/components/
---

Ok, we just defined a button with UI elements. This is a start for a UI. Building a complex UI would be boilerplate to copy this code over and over.

You may doubt there is an easier way to avoid boilerplate code. As a matter of fact, it is very simple. Just embed your script into a class. Then, you may create multiple instances and show them using `out`.

Before diving into live examples, let's define the syntax first.

You can define a user interface at the end of a class.

```
type ClassName(type parm1, type parm2, ...) {
  ...class code...

  // User interface just before class closing brace
  <tag: expr; tag: expr;>
  <tag: expr;
    // child UI elements defined after all tags
    <tag: expr;>
    <tag: expr; tag: expr;>
  >
  <tag: expr;>
}
```

So right before the class closing brace `}`, there is a list of 0 to n UI elements. A UI element is a list of tag/expression value pairs, delimited by angle brackets (`<>`). Each tag/expression value pair consists of a tag identifier, followed by a colon (`:`) character, then an expression value associated to the tag, ending with a semicolon separator (`;`).

```
tagName: expressionValue;
```
Each UI element may also contain child UI elements, defined as a list located after the tag/expression value pairs and before the parent element closing angle bracket. So a UI element is the following, enclosed under angle brackets.
```
<tag-value-pair-list child-UI-element-list>
```
Both lists may contain 0 element.

There is an extra orientation operator in UI element definition that we'll see later.
{: .notice--info}

Also any QED application having a user interface must have a main set of UI elements at the end of the main QED script.

```
// main QED script

... classes, functions and business logic...

// Main user interface
... UI elements...
```

The main user interface tags will establish links to the class inner user interfaces.

In the next pages, the examples will apply on the main script UI, not the UIs in classes, to learn how UI elements work. Bear in mind though that the class UIs exist and follow the same rules. They will be key to defining components and forms, which will be presented after detailing UI elements.
{: .notice--warning}
