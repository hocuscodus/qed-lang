---
title: "Forms"
permalink: /tutorial/forms/
---

Remember [class calls]({{ site.baseurl }}/tutorial/calls/)?

Whereas class instantiation creates asynchronous objects (which UI components are part of), class calls are synchronous... just like forms and dialog boxes.

Any UI class (a class with UI elements) can be a component or a form depending on the way we invoke it. We used class instantiation to create components. We can use class calls to create forms.

Let's try it using two simple buttons (one for a component and one for a dialog box) as a first example. Click the "Component" button to open the dialog box. Click the "Dialog" button to go back to the "Component" main form.

{% include sandboxframe.html qedsrc="tutorial/forms1.qed" code="70" gui="30" height="200px" %}

Therefore, in QED, a UI form is a call to a class having UI elements.
{: .notice--info}

So this is another startling practical use of the instantiation/call duality of QED classes: creating components and forms using one single class concept.

We can try with a more practical, down-to-earth example, a yes/no dialog. The common use case of a yes/no popup is within an if-else condition. Most languages would handle this case with an async call having a callback to handle successful and erroneous paths. It is much clearer here with the if-else flow and the use of a class call.

{% include sandboxframe.html qedsrc="tutorial/forms2.qed" code="70" gui="50" height="500px" %}

Upon exit, there is a `return` statement *on the main script* which, yes, can return (and no longer run its event loop). The main script return type is an integer which is intended to be used the same way as the main() one in C applications (0 for success or an error code).

Any number of overlapping dialog boxes is possible. As an example, let's create a Popup class that calls itself recursively when its "Yes" button is clicked, and returns when the "No" button is triggered.

{% include sandboxframe.html qedsrc="tutorial/forms3.qed" code="70" gui="90" height="500px" %}

The QED UI event system only applies to the latest dialog (class call) shown. Therefore, it is not possible to generate a UI event to parent calls. Clicking on any form behind the topmost one won't have any effect.
{: .notice--info}

