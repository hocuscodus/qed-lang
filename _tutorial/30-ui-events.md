---
title: "User interaction"
permalink: /tutorial/ui-events/
---

Besides all the tags we've seen to generate and render a UI, there also is a small set of tags used for user interaction.

Beware: it is basic for now and it handles basic needs. It will evolve over a relatively short period.
{: .notice--warning}

The UI user interaction tags all begin with the prefix "on", which makes them recognizable.

The `onPress` and `onRelease` tags are used to execute code upon user pressing or releasing the device pointer (mouse, finger, ...). Let's define a rectangle with a "Click me" text inside. When the device pointer presses and releases the pointer over the text, console messages are generated.

{% include sandboxframe.html qedsrc="tutorial/ui-events1.qed" code="70" gui="50" height="120px" %}

You can also add `onPress` and `onRelease` tags on other UI elements. Let's define one for the parent UI element (the background). Pressing and releasing the pointer on both surfaces show the correct console message.

{% include sandboxframe.html qedsrc="tutorial/ui-events2.qed" code="70" gui="50" height="150px" %}

Having `onPress` and `onRelease` tags on a container UI element, and not on its children, makes them valid for the children if the user tries to interact with them. In the following example, pressing or releasing the pointer on both background and text will spawn the same handler code.

{% include sandboxframe.html qedsrc="tutorial/ui-events3.qed" code="70" gui="50" height="120px" %}

You can implement in your UI event handlers visual cues to show the user that the interaction was received and processed. For instance, you can add a black rectangle on top of the element which opacity depends on the pressed state. To achieve this, you need to define a boolean that will wold the pressed state and modify it upon `onPress` and `onRelease` events. The rendering process uses the state when computing the opacity of the black rectangle. This way, pressing the element darkens it.

{% include sandboxframe.html qedsrc="tutorial/ui-events4.qed" code="70" gui="50" height="250px" %}

Adding clipping rounded corners and a bit of margins now truly makes this look as a button. We're at that moment at last, after all this journey: to create UI components.