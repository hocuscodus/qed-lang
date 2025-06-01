---
layout: single
title: Download & Sandbox
permalink: /download/
---

The QED compiler is now available as an npm package (check if node.js is installed, if not, go to [https://nodejs.org](https://nodejs.org)). To download and install the QED compiler, run the following command:

```
npm -g qedlang
```

To use the installed QED compiler on an application, run the following command

```
qed myapp.qed > myapp.js
```

where *myapp* is the name of the .qed file (just keep the .qed extension to your QED files). Here, the input file myapp.qed will, if no error occurs, transpile to myapp.js.

You can then reference the myapp.js file into a web page.

```
... HTML code
<div>
  <canvas id="canvas" width="800" height="480" style="top: 0; left: 0; max-width: 100%; max-height: 100%;">
    Your browser does not support the HTML5 canvas tag.
  </canvas>
</div>
<script src="myapp.js"></script>
<script>
  if (start_)
    start_([], function(_ret) {});
</script>
... HTML code
```

If *myapp* has a user interface, a canvas (which id is "canvas") must be defined first. If the canvas has width and height set to 0, the QED application will auto-resize it to what it needs.

Then you can insert the myapp.js script. To run it, use the `start_(args, callback)` function (defined by the myapp.js script). The first `start_` parameter is an array of arguments that you may supply to the QED script. The second callback parameter will be called if the main script of myapp.js calls `return`, with the return value if any.

If you just want to play with QED without downloading the compiler, here is a sandbox component where you can paste simple code to test in the left pane, then press `Run`.

{% include sandboxframe.html code="60" gui="50" height="400px" %}
