---
title: "The \"pos\" tag"
permalink: /tutorial/position/
---

The `pos` tag specifies, **in pixels**, the position of the UI element. This is different from the `align` tag, which indicates the alignment of the element (from top-left 0 to bottom-right 100%). The `pos` tag allows you to specify the precise position of the UI element, in pixels. In this example, a 20x20 green square is located in position (20, 10) over a larger red square, using the `pos` tag.

{% include sandboxframe.html qedsrc="tutorial/pos1.qed" code="65" gui="100" height="115px" %}

Just like the `align` tag, you can use the `pos` tag with expression values containing variables, which allows various pixel-based movement in games for instance, using code similar to the one of the previous section (just replace `align` with `tag` and use pixel coordinates instead of percentages).

Another possibility with pixel-based movement is fine scrolling though. In the example below, the drawStripes function is a UI render function that uses Javascript to draw stripes. It is used in the last defined UI element at the end of the code, along with the `pos` tag which vertical value repeatedly goes down from 0 to -200 (because of the while loop calling the `Animation` class). This value is applied to the `y` parameter of the `drawStripes` function call and redraws the same image from a different offset causing the infinite scrolling illusion.

{% include sandboxframe.html qedsrc="tutorial/pos2.qed" code="75" gui="100" height="460px" %}

Putting an increasingly negative value in `pos` will scroll up, hence the -vpos value. An increasingly positive value would scroll down. Change `-vpos` to `vpos` in the `pos` tag (and re-run) to witness downward scrolling.
{: .notice--info}

Finally, the `align` and `pos` tags, both positioning UI elements, are not antagonistic. In fact they work hand in hand with each other. When using both tags in a single UI element, the QED layout algorithm first uses the `align` value to calculate the aligned position. Then, it uses the `pos` value to move the UI element by the specified pixel-base coordinate, from the aligned position.

Using both tags is therefore practical in many cases to avoid boilerplate, cryptic calculations and rather let he QED layout algorithm take care of it. For instance, in the 150x150 dice example below, I want the horizontal margins for the 35x35 dots to be 20 pixels wide, and the vertical margins to be 10 pixels high. Positioning those dots using a blend of `align` and `pos` values is ideal. For instance, the bottom-right dot uses `align: 100%;` to specify the bottom-right location, but the `pos: [-20, -10];` moves the location 20 pixels left and 10 pixels up to set up the desired margins (much clearer than using `pos: [150 - 35 - 20, 150 - 35 - 10];` without using the `align` tag). The other dots use a similar strategy to easily encode their positioning.

{% include sandboxframe.html qedsrc="tutorial/pos3.qed" code="70" gui="100" height="200px" %}

