---
title: "Explicit arrays"
permalink: /tutorial/explicit-arrays/
---

Explicit arrays are arrays where elements are defined at initialization, within a pair of brackets after the declaration type (`[]`). The same bracket operator is used to access individual elements. The size() member function, accessible with the `.` operator, returns the size of the array.

{% include sandboxframe.html qedsrc="tutorial/explicit1.qed" gui="0" height="230px" %}

{% capture notice-1 %}
#### The next steps

Based on what we've seen, we almost have a full-fledged language. Even if types are not defined at this point in the tutorial, closures can be used to emulate them. But QED can do much more, as we'll see next with new constructs and ultimately, GUI support. We're now at the moment QED begins to drift away from other languages.
{% endcapture %}
<div class="notice--success">{{ notice-1 | markdownify }}</div>

<!--
This will be a tutorial...

{% include sandboxframe.html qedsrc="tutorial/taco.qed" code="50" gui="80" height="300px" %}

Another example...

{% include sandboxframe.html qedsrc="tutorial/bouncingballs.qed" gui="70" height="300px" autorun="" %}
-->
