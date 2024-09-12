---
title: "The \"expand\" tag"
permalink: /tutorial/expansion/
---

The `expand` tag customizes proper scaling on UI elements, and is key to nice looking user interfaces. It could also be considered the final boss tag, being tougher than the others to master. Some patience and practice is needed here.

Throughout this tutorial, UI element expansion has always been there but you may not have noticed its effects.

Therefore, we'll take baby steps (and plenty of examples) to gradually learn about it and clear out its intricacies.

## Default `expand` values

Thr first examples *will not* add the `expand` tag, to see wgat happens under the hood and how expansion is calculated by default. It is not a plain value like the other tags. It depends on many contextual factors.

The first example deefines one UI element (a blue rectangle).

{% include sandboxframe.html qedsrc="expand1.qed" code="65" gui="97" height="110px" %}

When you run it, nothing happens. It did not mean the code failed though. The rectangle not having any `size` indication, its default size is [0, 0] and thus it is not visible.

Replace the above code with `<out: rect; color: 0x0000FF; size: [100, 50];>` if you want to see it.
{: .notice--info}

We'll now put inside a white text child element.

{% include sandboxframe.html qedsrc="expand2.qed" code="65" gui="97" height="110px" %}

Now the enclosing blue rectangle expansion is based on its child size. As a rule of thumb, an unsized container size will be the minimal size to encompass all its children.

Let's embed this code within a parent UI element, a red rectangle this time, having a defined size.

{% include sandboxframe.html qedsrc="expand3.qed" code="65" gui="97" height="150px" %}

As you can see the blue rectangle completely covers its parent red rectangle. By default, unsized components take all the expansion they can.

I'll tell you how to override this in the next notice, so read on.
{: .notice--info}

What happens though if there are more than one elements that are displayed on a given direction? Let's add a "World" text under another rectangle (green this time) beside the blue rectangle.

{% include sandboxframe.html qedsrc="expand4.qed" code="65" gui="97" height="150px" %}

Expansion has stopped on the horizontal dimension, but still occurs on the vertical dimension. You can finally see the right part of the background red rectangle.

Is it not the fact that we have more elements that changes the expansion behavior. *It is the direction of the parent set.* So if you go back to the previous example (with a single element) and change the first character `<` with `<_`, `<|` or `<\`, you will stop expansion on one or both dimensions! You can try on this example as well.
{: .notice--info}

use of size
use of align

So this is it with the default expansion. Why this set of rules and not a default simple behavior such as no expansion whatever happens? Because this way is not really hard to grasp and, more important, is convenient for most situations. I tried to spare the use of the `expand` tag to not clutter the code for no reason.

## Using the `expand` tag

{% include sandboxframe.html qedsrc="expand10.qed" code="78" gui="97" height="110px" %}

{% include sandboxframe.html qedsrc="expand20.qed" code="65" gui="97" height="500px" %}

Pro tip: a good way to properly scale a more complex UI is to first define it without any `expand` attribute. When complete, you can do a top-down parse of the UI element tree to add `expand` tags wherever needed. Please test frequently during this parse to verify each small batch of `expand` additions to make sure these behave accordingly.