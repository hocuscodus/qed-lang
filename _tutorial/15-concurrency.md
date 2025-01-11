---
title: "Concurrency"
permalink: /tutorial/concurrency/
---

So QED classes have been designed to be be invoked as types (with the `new` operator, and `->` to handle return events if need be) or as calls (without `new`). In the previous examples though, we've mostly seen classes that are either designed as pure object (using `new`, without `return` call to generate events) or calls (with one `return` call done, not using `new`). Implementing these calls as plain lowercase-first-letter functions would have been more efficient so there seems to be no purpose using class calls.

Yet, QED classes were designed this way, because *there are* use cases where classes fit perfectly being invoked both ways!

We'll take the time to discover these mysterious classes through the rest of this tutorial. 
