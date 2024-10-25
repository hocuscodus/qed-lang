---
title: "Directions"
permalink: /tutorial/directions/
---

There was one feature not explained yet in UI elements: the setting of a direction for its child set.

Right after the UI element opening angle bracket `<`, you can insert a symbol that will specify the direction to apply to its child UI elements.

| Symbol        | Children direction |
| ------------: | --------- |
| No symbol | Overlapping from first to last (default) |
| Underscore (`_`) | From left to right |
| Vertical line (`|`) | From top to botton |
| Backslash (`\`) | From top-left to bottom-right |

So having a parent UI element that has a direction symbol will orient the child UI elements. Here it is for the horizontal dimension.

{% include sandboxframe.html qedsrc="tutorial/directions1.qed" code="50" gui="100" height="200px" %}

The vertical dimension is shown here, with a small twist. Nothing holds you to output something in the parent UI element. It will be displayed prior to the child UI elements, as the background. If now sizeable like in here, it will accumulate the sizes of the children.

{% include sandboxframe.html qedsrc="tutorial/directions2.qed" code="50" gui="100" height="200px" %}

The oblique direction looks odd at first sight.

{% include sandboxframe.html qedsrc="tutorial/directions3.qed" code="50" gui="100" height="200px" %}

In a few cases though, it can be useful. For instance, to create 25x25px top-left and 25x25px bottom-right margins.

{% include sandboxframe.html qedsrc="tutorial/directions4.qed" code="65" gui="100" height="200px" %}

<!-- The dot symbol `.` can be used to explicitly specify UI elements overlapping but this is the default behavior so having no direction symbol implies using the `.` symbol. -->