---
title: "The \"align\" tag"
permalink: /tutorial/alignment/
---

The first spacing management tag we'll see is the `align` tag. It simply aligns the corresponding UI element according to the its total allocated area at painting time.

Let's say we want to center a small circle within a larger rectangle. You may define the large rectangle first and the small circle after, so the circle occupies the foreground. However, by default, the circle will be drawn at the top left boundary. But due to the large rectangle *behind* the circle (no direction has been specified), their respective sizes are combined together to compute the total UI size. Therefore, at paint time, the circle free area is the whole rectangle size. The `align` tag specifies where to draw the circle within its free area.

{% include sandboxframe.html qedsrc="align1.qed" code="65" gui="97" height="160px" %}

In this demo, you may try out different values here for the small circle `align` value. The value `0` (the default) displays the element at top-left position, whereas the value `100%` means bottom-right. Values `[0, 100%]` and `[100%, 0]` will display the circle in the bottom-left and top-right positions, respectively. You may also change the rectangle dimensions to see how it influences the circle position.
{: .notice--info}

The `align` tag can also be used with contiguous UI elements. UI elements displayed on the vertical direction will have an horizontal size of the largest UI element (the rectangular "painting" here). Therefore, the "My painting!" UI element which has a lower horizontal size (thus having an horizontal empty area) will be aligned in the center with its tag `align: 50%;`.

{% include sandboxframe.html qedsrc="align2.qed" code="65" gui="97" height="250px" %}

Again, you can run the demo with many `align` values here (between 0 and 1 - or 0% and 100% for the sake of clarity). You will see that only the horizontal alignment works, even if `50%` really means `[50%, 50%]`. That's because the painted rectangle vertical size has no influence on the `My painting!` label vertical size, both being displayed vertically. So the vertical free area of `My painting!` is always its vertical size itself. There is no room to position the element vertically (try using direction `_` instead of `|` in the first line and change alignment values; you will see that only the vertical dimension will be aligned - and using direction `\` will nullify the `align` tag positioning in this demo).
{: .notice--info}

Finally, the `align` tag may be useful for animation when its expression value includes variables! Used in conjunction with a mechanism such as the `Animation` class in the QED library, which returns the timestamp of the previous frame's rendering, the `align` tag can be used to animate UI elements.

{% include sandboxframe.html qedsrc="align3.qed" code="80" gui="97" height="450px" %}

The library `Animation` class is a simple wrapper to the Javascript window.refreshAnimate(fn) call. Since it is now a QED class, you may call is synchronously without supplying a callback function, within a while loop for instance (like in this example). Each `Animation()` call in the while loop like any class call, will automatically trigger a UI refresh.
{: .notice--info}

Also, the `rand` function is a thin QED wrapper that directly calls the Math.rand() Javascript function (which returns a random float between 0 and 1).
{: .notice--info}
