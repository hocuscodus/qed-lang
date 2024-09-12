---
title: "Forms"
permalink: /tutorial/forms/
---

Remember class calls?

Whereas class instantiation creates asynchronous objects (which UI components are part of), class calls are synchronous... just like forms and dialog boxes.

Any UI class (a class with UI elements) can be a component or a form depending on the way we invoke it. We used class instantiation to create components. We can use class calls to create forms.

Let's try it using two simple buttons (one for a component and one for a dialog box) as a first example.

{% include sandboxframe.html qedsrc="forms1.qed" code="70" gui="30" height="200px" %}

The QED UI event system only applies to the latest dialog (class call) shown. Therefore, it is not possible to generate a UI event to parent calls. Clicking on the `Component` button while the `Dialog` button is displayed won't have any effect.
{: .notice--info}

Therefore, in QED, a UI form is a call to a class having UI elements.
{: .notice--info}

So this is another startling practical use of the instantiation/call duality of QED classes: creating components and forms using one single class concept.

We can try with a more practical, down-to-earth example, a yes/no dialog. The common use case of a yes/no popup is within an if-else condition.

{% include sandboxframe.html qedsrc="forms2.qed" code="70" gui="50" height="505px" %}

Any number of overlapping dialog boxes is possible.

{% include sandboxframe.html qedsrc="forms3.qed" code="70" gui="90" height="540px" %}

