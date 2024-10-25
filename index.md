---
layout: home
author_profile: true
---

Welcome to QED!

QED is a programming language for web app development. It sits on top of Javascript and provides features that simplify application development compared to using native JS. It does not replaces JS at all; it rather enhances JS programming when it comes to developing a GUI and implementing concurrency.

<div>
  <canvas id="canvas" width="700" height="220" style="top: 0; left: 0; max-width: 100%; max-height: 100%;">
    Your browser does not support the HTML5 canvas tag.
  </canvas>
</div>
<div style = "text-align: right;">
  <small>The QED fan. Give it a spin!</small>
</div>
<script src="assets/js/logo.js"></script>
<script>
  if (this.start_)
    this.start_([], function(_ret) {});
</script>

Academically, the goal of QED is to illustrate how simple concepts such as function call and class instantiation can lead up to more sophisticated topics such as concurrency and GUI design, hence the QED name (Q.E.D. means "quod erat demonstrandum" or "that which was to be demonstrated" - not related to type theory in our context). Practically though, the real goal is to simplify your life as a software developer by providing more power to your lines of code (or the same power for *less* lines of code).

You may take a look at the [demos]({{ site.baseurl }}/demos) or, if you want to dig further to discover how it works, the [tutorial]({{ site.baseurl }}/tutorial/qed-basics). There's also a [FAQ]({{ site.baseurl }}/faq) that will grow over time.

If you have a more specific question on QED, please inquire at qedlang@gmail.com.
{: .notice--info}
