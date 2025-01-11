---
title: "Base syntax"
permalink: /tutorial/syntax/
toc: true
toc_label: "Contents"
---

## Hello, World!

Although QED is a programming language with a compiler, it may act as a scripting language as shown in the various demos below. Of course, we'll start with the traditional "Hello, World!" to assess the language simplicity. It is a two-liner but you may try out this code with only one of the two lines, depending of what you want. Click Run to execute it.

{% include sandboxframe.html qedsrc="tutorial/helloworld.qed" height="140px"%}

The first line is a console version of the application, whereas the second line is a GUI version (both versions can coexist). The GUI version is shown on top-right section and the console version is output at bottom-right.

I know I'm already digressing here with some GUI code but don't worry, we won't see more of it in this section. I just wanted to show you a quick glimpse of QED output possibilities because, well, this is the venerable "Hello, World!" app and it had to showcase both output media.
{: .notice--warning}

## Statements

Statements in QED are delimited by a newline character or by a semicolon (which allows multiple statements to share one line).

{% include sandboxframe.html qedsrc="tutorial/statements1.qed" gui="0" height="100px" %}

Depending on your preference, you may add one or more semicolons at the end of a line without any adverse effect.

You can split a statement into multiple lines but be careful though; because semicolons do not mark the end of a statement, there are rules to follow. For instance, when splitting a binary expression, put the operator at the end of the first line, not at the beginning of the second line, like this:

{% include sandboxframe.html qedsrc="tutorial/statements2.qed" gui="0" height="200px" %}

Here, on the second line, if the plus sign was moved on the next line ("+ 5"), QED would split this statement into two statements, namely `println("3 + 5 = " + (3` and `+5` (also, an error would be generated for the first statement). So operators must end one line to indicate the statement continues on the next line.

## Variables and primitive types

Variables in QED are almost similar to their C/Java counterparts. A variable definition begins with a type, followed by a name and ending with an optional initialization expression. The primitive types are less numerous though: `bool`, `String`, `int` and `float` (the latter two using 64 bits). When not initialized, their default value is `false`, `""`, `0` and `0.0` respectively.

{% include sandboxframe.html qedsrc="tutorial/variables1.qed" code="50" gui="0" height="160px" %}

There is another keyword to introduce: `var`. A variable declared as `var` will infer its type based on its initialization value type. Its type cannot change afterwards.

<!--
todo: insert {% include sandboxframe.html qedsrc="tutorial/variables2.qed" code="50" gui="0" height="160px" %}
-->

However a `var` variable with no initialization value can hold any value from any type (a bit like a `void*` in C).

{% include sandboxframe.html qedsrc="tutorial/variables3.qed" code="50" gui="0" height="160px" %}

## Control statements and blocks

The usual `if-else`, `while`, `for` control keywords are available in QED.

{% include sandboxframe.html qedsrc="tutorial/controls1.qed" gui="0" height="210px" %}

As in C or Java, braces `{}` are used to delimit multiple-line blocks. There is no need for a set of braces when a block consists of a single statement. Also, blocks define scoping rules for variables and functions. Empty blocks must be explicitly defined using `{}` (a semicolon after a control statement won't work).

The only caveat is there is no `break` statement in QED as of this writing. Also the `switch-case` statement is not yet implemented.
{: .notice--warning}

## Functions

Again, functions are defined the same way as in C.
```
type name(type parm1, type parm2, ...) {
  <body>
}
```
Unlike C though, nested functions are allowed and closures are fully supported.

{% include sandboxframe.html qedsrc="tutorial/functions1.qed" gui="0" height="250px" %}

Functions are first-class citizens in QED. Functions types can be defined with any function, leaving their body empty with `{}`. Then you can use them as function types if followed by a star (the semantic of `*` is different from C in QED).

When used as lambdas, functions must always be embedded in a set of parentheses. You may use any function name but the preference would be `lambda` or `l` for clarity.

Also, and very important, the first alphabetic character must be lowercase. Uppercase-beginning "functions" do exist in QED but define something much more powerful as we'll see soon. When used as types (with a `*` suffix), keep the names' first letter lowercase.

## Explicit arrays

Explicit arrays are arrays where elements are defined at initialization, within a pair of brackets after the declaration type (`[]`). The same bracket operator is used to access individual elements. The size() member function, accessible with the `.` operator, returns the size of the array.

{% include sandboxframe.html qedsrc="tutorial/explicit1.qed" gui="0" height="100px" %}

QED does not support null pointers, as per [good practice recommendations](https://en.wikipedia.org/wiki/Null_pointer#:~:text=In%202009%2C,last%20forty%20years.). I might implement in QED helper keywords such as `None` and `Some` someday but in the meantime, you may use an explicit array with one or zero element to emulate the null case.

{% include sandboxframe.html qedsrc="tutorial/explicit2.qed" gui="0" height="340px" %}
