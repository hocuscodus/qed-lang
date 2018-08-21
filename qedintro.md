---
layout: page
title: An introduction to QED
permalink: /qedintro/
---

Welcome to the QED programming language. I hope you will enjoy discovering it. This basic introduction shall ramp you up in minutes with something as strange - at first look, no worries! - as QED.

QED is great at designing complex graphical user interfaces the simple way.

Without further ado, let’s explore it.

Here is QED’s Hello, World! app, a simple one-liner:

    println("Hello, World!");

The string Hello, World! is output to the console through the println call business logic, nothing new here.

Now, take a deep breath as you are about to enter the forbidden zone. When coming back, you may not be the same…

Ok then, let’s get in! Did I just say that QED was great at programming GUIs? Yes I did, so here is a GUI version of the Hello, World! application, another one-liner:

    ; @out("Hello, World!")

The @out GUI attribute (each GUI attribute starts with @) reserves an area to hold the Hello, World! string and displays it in the reserved area.

So first lesson here: the QED language supports both business logic calls (println) and GUI attributes (@out). But how do they cope together?

In the last example, you may wonder why the line started with a semicolon. This is not a typo. The semicolon is a separator between the business logic, at its left (a NOP in the last example), and the GUI directives, at its right.

\<business logic\> ; \<GUI attributes\>

Sets of braces are also used to separate business logic from GUI attributes, just like semicolons

\<business logic\> { \<child statements\> } \<GUI attributes\>

That implies business logic can be programmed alongside GUI attributes, as shown here:

    println("Hello, World!"); @out("Hello, World!")

There are really two execution phases here, one for the business logic and one for the GUI. Each phase is executed separately and differently. First, the business logic is executed and Hello, World! is printed in the console. Then, since no return() call has been encountered, the business logic halts and the second execution phase begins. The GUI attributes are executed and Hello, World! is also displayed as GUI.

What is the advantage of sharing a single code for both business logic and GUI attributes? Binding both worlds is made very easy. Consider the following example:

    int num = 17; @out(num)

The business logic is executed first and assigns to integer num the value 17. No return call is found so the business logic halts (keeping all variables active). Then the GUI is built and uses variable num from the business logic (17 is displayed).

A secret here... binding goes both ways:

    int num; @out("Count: " + num) @onrelease(num++)

The business logic of this example declares an integer variable num initialized to 0, then halts (no return call encountered). Then the GUI displays Count: \<num\>. When the user clicks on the GUI display, the @onrelease (mouse release) attribute value gets executed and variable num is increased. Then, the GUI application is run again, now displaying Count: \<num + 1\>. At each trapped event such as mouse release, the GUI gets rebuilt to reflect variable changes.

Mixing variables from business logic to various GUI attributes and encapsulating them into functions allows building complex logic with few lines.

The following describes a function that renders a simple button. It accepts one parameter, text, which is displayed in the button. Its logic consists of one int variable col that holds the button RGB color, light grey. When Button() is called, since return is not part of the business logic, the function halts after declaring col and generates a GUI. The GUI displays the text padded with spaces. The background of the padded text comprises a rectangle having color col (light grey for now).

    void Button(string text) {
        int col = 0xC0C0C0; @out("  " + text + "  ")
    } @out(rect()) @bgcol(col) @onpress(col = 0x808080) @onrelease([col = 0xC0C0C0, return()])

When pressed, the @onpress attribute expression is executed and col changes to medium grey. A GUI refresh is performed then, making the button look pressed. Upon release, the col variable returns to light grey (to look released) and only then, the function returns to the caller.

Direct execution of the last example does nothing, because we declared a function without calling it. Let’s do so.

    void Button(string text) {
        int col = 0xC0C0C0; @out("  " + text + "  ")
    } @out(rect()) @bgcol(col) @onpress(col = 0x808080) @onrelease([col = 0xC0C0C0, return()])

    Button("Form 1");
    Button("Form 2");
    Button("Form 3");
    return(0);

As you see, a function call is used to display a form, as simple as the form can be (a single small button here). Between each Button() call, the application waits for the user to click the form button to exit the form. The last return exits the entire program.

Now, let’s try something very new. The QED langage has no defined keyword to declare types or classes. Functions are types too! By preceding the function call with the new operator, a function is called as an object that can then be used by the caller. Objects can be fields in QED if they appear in @out attributes. So a QED function may not only be used as a form but also as a field.

    void Button(string text) {
        int col = 0xC0C0C0; @out("  " + text + "  ")
    } @out(rect()) @bgcol(col) @onpress(col = 0x808080) @onrelease([col = 0xC0C0C0, return()])

    {
        Button button1 = new Button("Field 1") -> println("Field 1 clicked"); @out(button1)
        Button button2 = new Button("Field 2") -> println("Field 2 clicked"); @out(button2)
        Button quit = new Button("Quit") -> return(0); @out(quit)
    } @cdir(2)

When called as an object, the Button function executes its business logic (the int col line) and returns itself immediately to the caller (no halting of any sort here) as an object with data members text and col. The caller may do whatever it wants with the object. If the caller business logic completes without returning, the caller GUI is generated. The caller GUI may include created objects in its @out attributes (e.g. @out(button1)). Then the button is displayed as a field. In the previous example, three Button fields are created and displayed vertically using @cdir(2) (1 means horizontal and 2 means vertical). When clicked, the Button @onrelease expression is executed and Button.return() is called. The return() behavior is different when its function is invoked as an object. It sends an event to the caller object, trapped by the -> event operator at its right (if any). What is at the right of the event operator gets executed upon event and the GUI is refreshed.

From now on, you should be able to have a better understanding of the examples here. If you want to go further into exploring QED, there is a [video describing the philosophical roots](https://www.youtube.com/watch?v=a2OhmxXQbwo) of the language but beware though, it is not for everybody. I did it because some people may think I am insane, in order to prove them right! In any case, this documentation will expand quite a lot and will cover the complete subject over time.