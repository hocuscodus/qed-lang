---
layout: single
title: QED FAQ
permalink: /faq/
---

## What is QED

QED is a programming language primarily aimed at developing web applications. It sits on top of Javascript for developing the GUI related parts in shorter, clearer code than JS. From the QED source code, the QED compiler generates the equivalent JS code so your application can be run by existing browser engines without needing any extension. There is no QED interpreter/virtual machine, only a compiler to generate plain JS code.

## Is QED intended to replace JS?

No, not at all! QED is rather a complement to JS for writing the GUI part of a web application (you can freely use JS for the rest). QED fully relies on JS speed, power and huge libraries.

## What are QED advantages over HTML/CSS/JS code for web development?

QED is focused on the web app so all the UI/styling/logic is kept in the same code (although the separation between these is still clear). Within the same code, the scoping rules are used for all three layers. For instance, the UI code has full access to the business logic declared variables, from any environment scope within the code.

Also, asynchronous concurrent code is written just like synchronous blocking code in QED. No need for special keywords or concepts such as async, await, promises, callbacks... The possibility to write the GUI code the same way as synchronously-based console application allows much better readability and developing ease.

Finally, the GUI rules in QED are specifically targeted to GUI development, compared to HTML/CSS rules that were designed for documents in mind. The GUI rules are completely shielded from HTML/CSS by operating on a single HTML5 canvas element to display forms, widgets, sprites and other GUI-related components. No DOM reload is ever performed (unless the QED app itself creates/removes DOM elements).

## How to integrate a QED application in my web page?

When you are done developing the QED code in a file (let's call the file myprog.qed), use the compiler to generate the JS equivalent (myprog.js).

```
qed myprog.qed > myprog.js
```

Then, in your HTML body, add a canvas with id "canvas" and the myprog.qed script. Invoke it via the this.start_() function.

```
<!doctype html>
  ... your HTML code ...

  <canvas id="canvas" width="800" height="480" style="touch-action:none">
    Your browser does not support the HTML5 canvas tag.
  </canvas>
  <script src="./myprog.js"></script>
  <script>
    this.start_([], function(_ret) {
      <process _ret return value>
    });
  </script>

  ... your HTML code ...
</html>
```

If you want the QED app to auto-dimension the canvas, use width="0" and height="0" instead of specific values.
{: .notice--info}

That's it. No need for further HTML/CSS/JS code (that is, unless your QED app relies on other JS code/libs - include them before myprog.js in this case). Just try your web app on any browser.

## Does QED have its own library?

QED has a basic library, intentionally kept as small as can be. Written in QED, it basically implements the background code for implicit arrays and GUI logic, plus some helper wrappers for JS math and async functions. It is automatically embedded in all QED applications for now, so you don't need to specify qedlib.js in your web pages.

## Can I write a desktop app with QED?

QED is for now targeted to web applications. That said, after generating the JS code from the QED source, you may in theory use frameworks such as Electron to generate a desktop app. I haven't tried it yet though.

Integrating QED directly with other programming languages/desktop environments is in the plans if QED gains popularity.

## Is QED free to use?

Yes. Although the compiler is closed-source (as of today - that may change later depending on how it goes), it will be free to download binaries and use when ready, hopefully in a few months.

The QED library will be open source, under the AGPL license, as QED is not ready for production yet. You can freely use it for your own applications/web pages if they are open source.

## This looks interesting. May I contribute?

Contributors are welcome! There are still lots of things to do on many aspects: compiler, library, QED modules, UI component templates, better tooling, documentation, etc. Please DM me through email (qedlang@gmail.com), [X](https://x.com/qedlang) or [reddit](https://www.reddit.com/r/qedlang). If enough people are interested, I'll set up something like, for instance, a Discord community.