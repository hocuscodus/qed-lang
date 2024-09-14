---
title: "Control statements and blocks"
permalink: /tutorial/control/
---

The usual `if-else`, `while`, `for` control keywords are available in QED.

{% include sandboxframe.html qedsrc="tutorial/controls1.qed" gui="0" height="210px" %}

As in C or Java, braces `{}` are used to delimit multiple-line blocks. There is no need for a set of braces when a block consists of a single statement. Also, blocks define scoping rules for variables and functions. Empty blocks must be explicitly defined using `{}` (a semicolon after a control statement won't work).

The only caveat is there is no `break` statement in QED as of this writing. Also the `switch-case` statement is not yet implemented.
{: .notice--warning}
