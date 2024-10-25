---
title: "The \"expand\" tag"
permalink: /tutorial/expansion/
toc: true
toc_label: "Contents"
---

The `expand` tag customizes proper scaling on UI elements, and is key to nice looking user interfaces. It could also be considered the final boss among tags, being tougher than the others to master. Some patience and practice is needed here.

Throughout this tutorial, UI element expansion has always been there but you may not have noticed its effects.

Therefore, we'll take baby steps (and plenty of examples) to gradually learn about it and clear out its intricacies.

## Default `expand` values

Thr first examples *will not* add the `expand` tag, to see what happens under the hood and how expansion is calculated by default. It is not a plain value like the other tags. It depends on many contextual factors.

The first example defines one UI element (a blue rectangle).

{% include sandboxframe.html qedsrc="tutorial/expand1.qed" code="65" gui="97" height="110px" %}

When you run it, nothing happens. It did not mean the code failed though. The rectangle not having any `size` indication, its default size is [0, 0] and thus it is not visible.

Replace the above code with `<out: rect; fillStyle: "blue"; size: [100, 50];>` if you want to see it.
{: .notice--info}

We'll now put inside a white text child element.

{% include sandboxframe.html qedsrc="tutorial/expand2.qed" code="65" gui="97" height="110px" %}

Now the enclosing blue rectangle expansion is based on its child size. As a rule of thumb, an unsized container size will be the minimal size to encompass all its children.

Let's embed this code within a parent UI element, a red rectangle this time, having a defined size.

{% include sandboxframe.html qedsrc="tutorial/expand3.qed" code="65" gui="97" height="150px" %}

As you can see the blue rectangle completely covers its parent red rectangle. By default, unsized components take all the expansion they can.

I'll tell you how to override this in the next notice, so read on.
{: .notice--info}

What happens though if there are more than one element displayed on a given direction? Let's add an orange rectangle (containing ", ") and a green one (with "World!" inside) beside the blue rectangle.

{% include sandboxframe.html qedsrc="tutorial/expand4.qed" code="65" gui="97" height="170px" %}

Expansion has stopped on the horizontal dimension, but still occurs on the vertical dimension. You can finally see the right part of the background red rectangle.

Is it not the fact that we have more elements that changes the expansion behavior. *It is the direction of the parent set.* So if you go back to the previous example (with a single element) and change the first character `<` with `<_`, `<|` or `<\`, you will stop expansion on one or both dimensions! You can try changing directions on this example as well.
{: .notice--info}

Let's get back to the example with a single child text "Hello". The expansion of the blue rectangle matches the sized parent red rectangle. There are two other ways to change this default behavior (that is, without using `expand` explicitly or a direction on the parent element). The first one is to use a `size` tag on the blue container. This way, expansion is not used and the blue rectangle size no longer depends on the parent area (or the inner text size).

{% include sandboxframe.html qedsrc="tutorial/expand5.qed" code="65" gui="97" height="150px" %}

Remember, a sized parent UI elements clips the children display if the latter is larger.
{: .notice--info}

If you wanted the blue area to exactly cover the "Hello" text over the big red rectangle, an easy way is to use the `align` tag with value 0, or [0, 0]. Using `align` tells the layout algorithm that the UI element uses the parent area as a region to align its content and therefore, the blue rectangle no longer expands to the red rectangle region.

{% include sandboxframe.html qedsrc="tutorial/expand6.qed" code="65" gui="97" height="150px" %}

Of course you can use `align: 50%` if you want to center it, or `align: 100%` to put it at the bottom right for instance.
{: .notice--info}

The `align` tag also works fine with a parent element having a direction (and multiple children possibly), to align certain elements elsewhere than the top (for `_`) or the left (for `|`).

{% include sandboxframe.html qedsrc="tutorial/expand7.qed" code="65" gui="97" height="170px" %}

So this is it with the default expansion. There is no predefined default value like the other tags. It rather depends on many contextual factors: UI element children, parent area and direction, and the presence of the `size` or `align` tags. Why this set of rules and not a default simple behavior such as no expansion whatever happens? Because this way is convenient in most situations, and not really hard to grasp over time. QED tries to spare you the explicit use of the `expand` tag to prevent cluttering the code for no sound reason.

## Using the `expand` tag

When the default expansion is no longer convenient to your needs, you must specify the `expand` tag explicitly. The good news is, once you know how default expansion works, the `expand` tag usage is rather easy.

Let's start with the previous example (recopied here to facilitate comparison with the next one), where the blue rectangle, by default, completely covers the red rectangle.

{% include sandboxframe.html qedsrc="tutorial/expand3.qed" code="65" gui="97" height="150px" %}

Imagine you first want to completely hold down the expansion of the blue rectangle. The `expand` tag is here for that. Using `expand: 0` (or `0%` or `[0%, 0]`...) will achieve it and will now only cover the text inside. You overrode the default expansion!

{% include sandboxframe.html qedsrc="tutorial/expand8.qed" code="65" gui="97" height="150px" %}

Now, if we want to cover 50% of the red portion, starting from the text size and ending to the red rectangle boundaries, on both vertical and horizontal dimensions, just replace the `expand: 0` with `expand: 50%` (or `[50%, 50%]`). That will do the trick.

{% include sandboxframe.html qedsrc="tutorial/expand9.qed" code="65" gui="97" height="150px" %}

Add characters to the "Hello" string to see changes in calculating the blue rectangle size. It really takes into account both text and red rectangle sizes.
{: .notice--info}

You may rather want to cover exactly 50% of the red rectangle size, regardless of the text size. Just add `size: 0` to the blue rectangle UI element. This way, the `size` tag overrides the text size. Calculation of expansion from size [0, 0] no longer takes the text size into account in the expansion calculation, only leaving the red rectangle size as the sole criteria.

{% include sandboxframe.html qedsrc="tutorial/expand10.qed" code="65" gui="97" height="150px" %}

This time, adding characters to the "Hello" has no effect on the blue rectangle size. The blue rectangle dimensions is only based on the red rectangle size.
{: .notice--info}

For cases where the `out` tag does not specify the size (e.g. `out: rect`) and the UI element has no sizeable child, you don't have to specify the `size: 0` tag. It will assume it.

{% include sandboxframe.html qedsrc="tutorial/expand11.qed" code="78" gui="97" height="110px" %}

As you see here, alignment is performed *after* expansion. Therefore, when calculating a UI element position, we have to take into account the `expand` value. Here are the tags used, by order of precedence:

- `expand`
- `align`
- `pos`

To complete our exploration of the `expand` tag, let's reintroduce the previous example with the horizontal set of three text elements. We'll add customized expansion to it!

{% include sandboxframe.html qedsrc="tutorial/expand4.qed" code="65" gui="97" height="170px" %}

Ok, now, let's say we want to control the expansion in the empty red area. The expansion will cover 90% of it, both by the blue "Hello" and the green "World!" rectangles (esch one using 45% of this red space). The yellow ", " rectangle is left without expansion. Here is how we do.

{% include sandboxframe.html qedsrc="tutorial/expand12.qed" code="65" gui="97" height="170px" %}

If you intend to use 100% of one dimension, you are not obligated to define percentages in the UI elements where the total is 100%. You may have a greater total. QED will convert them into percentages. That allows for "weights" or "parts" (which you may have seen in other languages) to be used instead of percentages. Let's say you want three "parts" of the empty space for the "Hello" rectangle and two for the "World!" rectangle. Just use these numbers in the right dimension (horizontal in this case). This time the vertical expansion will be nullified (which now requires an `expand: 0` tag in the ", " rectangle). Let's also center "Hello" and "World!" to make it look better.

{% include sandboxframe.html qedsrc="tutorial/expand13.qed" code="65" gui="97" height="170px" %}

Using the `expand` tag wisely enables you to scale a tree of UI elements properly. This is particularly important for forms, where components have to be well laid out for aesthetical purpose. As a last demo, let's present such a form that scales dynamically back and forth. As we haven't covered components and forms yet, let's pretend for now that components are plain rectangles within the form. We will unveil them in the next section anyway.

{% include sandboxframe.html qedsrc="tutorial/expand14.qed" code="65" gui="97" height="500px" %}

The fair use of `expand` makes the components of this form scale smartly. 

You may try tweaking the expand values in this example to see the effects.
{: .notice--info}

Pro tip: a good way to properly scale a more complex UI is to first define it without any `expand` tag. When complete, you can do a top-down parse of the UI element tree to add `expand` tags wherever needed. Please test frequently during this parse to verify each small batch of `expand` additions to make sure these behave accordingly.
{: .notice--info}
