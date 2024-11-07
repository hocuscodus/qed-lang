---
layout: single
title: Demos
permalink: /demos/
---

Some demos for now. More later...

## Increment-decrement buttons

{% include demoframe.html qedsrc="demos/incdec.qed" code="65" gui="97" height="400px" %}

## Bouncing balls

{% include demoframe.html qedsrc="demos/bouncingballs.qed" code="65" gui="97" height="400px" %}

## Taco hotel

{% include demoframe.html qedsrc="demos/taco.qed" code="65" gui="97" height="400px" %}

## QED fan

{% include demoframe.html qedsrc="demos/logo.qed" code="55" gui="97" height="400px" %}

## Flappy bird

{% include demoframe.html qedsrc="demos/flappybird.qed" code="55" gui="97" height="400px" %}

<script type="application/javascript">
function resizeIFrameToFitContent( iFrame ) {
    if (iFrame.height != iFrame.contentWindow.document.body.scrollHeight)
      iFrame.height = iFrame.contentWindow.document.body.scrollHeight;
}
window.addEventListener('DOMContentLoaded', function listen(event) {
  try {
    // or, to resize all iframes:
    var iframes = document.querySelectorAll("iframe");

    try {
      for( var i = 0; i < iframes.length; i++)
        iframes[i].width = iframes[i].contentWindow.document.body.scrollWidth;
    } catch (e) {
      console.log(e.name + "-> " + e.message);
    }

    setInterval(() => {
      try {
        for( var i = 0; i < iframes.length; i++) {
          resizeIFrameToFitContent( iframes[i] );
        }
      } catch (e) {
        console.log(e.name + ": " + e.message);
      }
    });
  } catch (f) {
    console.log(f.name + ": " + f.message);
    listen(event)
  }
});
</script>
