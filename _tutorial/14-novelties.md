---
title: "Novelties"
permalink: /tutorial/novelties/
---

So QED classes have been designed to be be invoked as types (with the `new` operator, and `->` to handle return events if need be) or as calls (without `new`). In the previous examples though, we've seen classes that are either designed as pure object (with no `return` call to generate events) or calls (with one `return` call done and pointless calling it with `new` to keep the object).

Yet, QED classes were designed this way, because *there are* uses cases where classes fit perfectly being invoked both ways!

(Venn diagram)

We'll take the time to discover these mysterious classes through the rest of this tutorial. 
