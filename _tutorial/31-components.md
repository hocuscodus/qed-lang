---
title: "Components"
permalink: /tutorial/components/
---

Ok, we just defined a button component with UI elements. Let's put it back here as a refresher.

{% include sandboxframe.html qedsrc="tutorial/ui-events4.qed" code="70" gui="50" height="250px" %}

This is a good start for a very simple UI but building a complex one would generate a lot of boilerplate code if we write all components on the same script.

You may doubt there is an easier way to avoid boilerplate code. As a matter of fact, it is very simple. Just wrap your script into a class. Then, in the main script, you may create multiple instances and show them using `out`.

{% include sandboxframe.html qedsrc="tutorial/components1.qed" code="70" gui="50" height="400px" %}

Therefore, in QED, a UI component is an instantiation of a class having UI elements.
{: .notice--info}

Yes, the `out` tag does accept UI components too and renders them according to their UI elements.
{: .notice--success}

This is cool but when you define a UI in a class, you may want more flexibility to avoid creating similar instances like we just did.

You can use class features for that. In the case of a button:

* the text can become a class parameter so the button no longer displays a hardcoded string
* when released, instead of printing "Clicked", we can simply call `return`, so an event handler at instance creation will process a clicked button the way it wants to

Using this, you really have a button component that can be used in a variety of situations.

{% include sandboxframe.html qedsrc="tutorial/components2.qed" code="70" gui="50" height="450px" %}

As an aside, components do not have to be windowing elements. They can for instance be moving objects within a playfield too for gaming. If so, there may be many similar instances battling together. The best way to implement this is by using coroutines. Define a coroutine to a class having UI elements, then build many instances using an implicit array. This kind of coroutine with UI elements has a more familiar name: *a sprite*.

{% include sandboxframe.html qedsrc="tutorial/components3.qed" code="70" gui="97" height="535px" %}

Let's get back to windowing elements. So with classes, you can define many components. QED will offer a set of native basic components (buttons, checkboxes, lists ...) to simplify development but nothing holds you to tailor your own the way you want using classes.

We can use the native QedTextButton class to avoir ourselves defining a button. The code will be much simpler at first glance.

{% include sandboxframe.html qedsrc="tutorial/components4.qed" code="75" gui="40" height="110px" %}

While it works, we must admit it looks ugly. The good news is, you can add *styling* to components. Parent painting tags (`lineWidth`, `strokeStyle`, `fillStyle`, `font`, `fontSize`, `radius`, ...) are inherited not only by child UI elements. Components also inherit from them as well!

In the next example, we define a single `QedTextButton` instance, viewed multiple times using different painting tags. You can see how QED components 

{% include sandboxframe.html qedsrc="tutorial/components5.qed" code="75" gui="50" height="440px" %}

Let's tweak the `QedTextButton` to match the look of the button we defined at hand. In addition to inherited tags, components also provide other functions to further customize their look or behavior.

{% include sandboxframe.html qedsrc="tutorial/components6.qed" code="75" gui="40" height="150px" %}

By using more components and properly laying them out in our UI element tree, we can define a complete form. That said, a single form is often not enough for a complete app. Other forms such as dialog boxes or other windows are necessary in most cases. Let's see how we can do it...

If you have read the whole tutorial up to this page, you may have guessed already!
{: .notice--info}

