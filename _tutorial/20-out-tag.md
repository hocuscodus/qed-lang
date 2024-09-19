---
title: "The `out` tag"
permalink: /tutorial/out-tag/
---

The `out` tag is fundamental because it outputs all UI elements.

{% include sandboxframe.html qedsrc="tutorial/out1.qed" code="50" gui="100" height="200px" %}

The expression value can be any valid expression and may use business logic variable within its scope.

{% include sandboxframe.html qedsrc="tutorial/out2.qed" code="50" gui="100" height="200px" %}

Try changing the `num` variable before pressing the Run button. You will see the output changes.
{: .notice--info}

You can already do a first animated UI with what's been taught so far! Each `QedTimer(1000)` call in the next example enters the event loop for a second, meaning the UI being refreshed 6 times (5 for the countdown with `QedTimer(1000)` and one for `GO!`, at the end of the script where `num` is 0).

{% include sandboxframe.html qedsrc="tutorial/out3.qed" code="60" gui="100" height="200px" %}

Because UI refreshes must be atomic, class calls in UI element value expressions are strictly forbidden!
{: .notice--danger}

You may define multiple UI elements. They will share the same space, which size is the smallest rectangle containing all *sizeable* elements.

{% include sandboxframe.html qedsrc="tutorial/out4.qed" code="60" gui="100" height="200px" %}

There are a few things to notice here. First, the order of the UI elements is important. It specifies which elements are displayed over others. The blue oval is displayed first, then the `A rather small text` string, followed by the red `Big` text.

Second, the first UI element outputs an oval. The `oval` value expression is a pointer to the `oval()` function (it must have no parenthese as the actual call will occur during the UI refresh). There are other rendering functions such as `rect()` and `roundRect()` that you can use.

Third, this UI element is not sizeable, compared to the other two texts. The size of the oval is determined by the accumulation of the sizes of its siblings, using the height of the `Big` text and the width of the `A rather small text`.

Most times though, the UI elements should not overlap like in here. You'll see very soon (right after the `size` tag) how they can display next to each other.
{: .notice--info}
