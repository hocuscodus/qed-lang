---
title: "Implicit Arrays"
permalink: /tutorial/implicit-arrays/
---

We have explained what explicit arrays were but there is another way to declare an array. In a QED expression, the spacing separator (set of spaces/tabs) is considered an **array operator**. Consider the following:

// example

As you see, the left expression is the array count and the right expression is the array element expression. You can prefix the array count expression by `indexVarName::` to define an array index for this expression, then use it in the element expression.

// another example

You can also define multiple dimensions by using more than one array count expressions. The rightmost expression will always be the element expression.

The return type of the array expression is an array of elements. If the element expression type is void (or the element expression is a block using braces `{}`), there is no return type, that is, it acts like a `for` loop and cannot be stored in a variable.

You can also invoke classes in the array expressions (counts or element), either synchronously or asynchronously.

We can see that when created using implicit arrays, asynchronous tasks run concurrently!
