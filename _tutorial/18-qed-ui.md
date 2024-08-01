---
title: "QED UI"
permalink: /tutorial/qed-ui/
---

QED classes, with dual nature classes acting as both function and object type, have the required functionality to incorporate UI encodings. When you think of it, a form and a component (UI element or embedded container) are the two faces of the same medal. When a class is instantiated, it becomes a component of a form, in parallel with the other form components. When the same class is called, it becomes a synchronous form that must be dismissed to resume processing.

Button code as a component and as a form.

{% include sandboxframe.html qedsrc="taco.qed" code="50" gui="80" height="500px" %}

Of course, forms will be more complex than single buttons and will contain more elements. I just wanted here to outline that forms are class calls whereas components are class instantiations.