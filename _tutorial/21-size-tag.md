---
title: "The `size` tag"
permalink: /tutorial/size-tag/
toc: true
toc_label: "Contents"
---

## Specifying sizes

The size of the user interface area is determined by the UI elements. It is the minimal area where all UI elements fit in. Some UI elements do not have a determined size though, the ones with the `out` tag having a drawing function pointer as value expression such as `rect`, `oval`, `roundRect`. These elements use sibling elements to determine their size.

{% include sandboxframe.html qedsrc="tutorial/size1.qed" code="60" gui="100" height="200px" %}

The `radius: 8;` tag specifies the corner radius for round rects (only applies to `out: roundRect;` tags).
{: .notice--info}

Most of the times though, you want to specify the size of drawing elements in pixels. The `size` tag does so, with an explicit array of two numbers.

{% include sandboxframe.html qedsrc="tutorial/size2.qed" code="60" gui="100" height="200px" %}

It often happens that you want a square as the size of an element. In this case, you may shorten the explicit array notation by using a single number. In this example, a 100x100 oval (a circle in fact) is shown.

{% include sandboxframe.html qedsrc="tutorial/size3.qed" code="60" gui="100" height="200px" %}

This rule also applies to the other tags taking a pair of numbers as we'll see.
{: .notice--info}

## Clipping and freeform drawing

The `size` tag also applies for sizeable outputs, such as text. In this case, it supercedes the sizeable output size, clipping the output if the latter is too large.

{% include sandboxframe.html qedsrc="tutorial/size4.qed" code="65" gui="100" height="200px" %}

QED offers basic drawing functions such as `rect`, `oval` and `roundRect` in its library for convenience but you may want to do freeform drawing in your application. To achieve it, you will have to supply your own, custom drawing functions as well.

Your drawing function must start with a lowercase letter (it cannot be a class) and must follow this signature and body.

```
void myDrawingFunction(int x, int y, int width, int height) {
/$
  <JS drawing code. The constant ctx is available for Context2D access.>
$/
}
```

As an example, let's draw a triangle with our own freeform drawing function. Tags such as `lineWidth`, `strokeStyle` and `fillStyle` will be applied on paths when you call `qedDraw()` (you can call it multiple times in your drawing function). We will also use it as a clipping area for a red circle inside.

{% include sandboxframe.html qedsrc="tutorial/size5.qed" code="65" gui="100" height="300px" %}

The painting tags (`lineWidth`, `strokeStyle`, `fillStyle`, `font`, `fontSize`, `radius`, ...) are inheritable in QED. Child UI elements inherit from the ones defined for a parent UI element. For `fillStyle` and `strokeStyle`, you may supply the value `none` to reset their value. That is, if `none` is specified for these tags, QED will not use them in `qedDraw()`.