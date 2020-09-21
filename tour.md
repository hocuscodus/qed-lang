---
layout: page
title: 
permalink: /tour/
---

Welcome to this QED language tour. QED is designed to make nice GUI applications simply but before doing so, you must take a peek under the hood to really see how it works. Hence this guide...

The goal of this tour is to show how basic procedural programming can naturally evolve to encompass event-based GUI development. To achieve this, we will play with simple expressions - existing in most mainstream languages - to gradually venture into a new - but easy! - take on programming.

This tour assumes you have a working knowledge of procedural programming, in order to keep it brief and to focus on the key differentiators from other languages.

There are lots of niceties to cover, so without further ado, let’s begin our journey!

## The main divergence: the store

In most languages, statements composed of pure expressions have no effect or are even discouraged (by a warning in C or an error in Java).

```
println("Hi"); // ok, side effect
c = a + b;     // ok, mutation of var c
a * c - b;     // no effect, can be flagged as warning/error
```

A statement may or may not return a value. Statements returning nothing will be referred to as void statements. For non-void statements, most languages automatically dismiss their return values upon finishing their execution; they are never considered.

In QED, the return value of non-void statements is saved in a storage area in memory called the store. For instance, in the following block of statements:

```
"John Doe";
println("Hi"); // void statement, no value in store here
6 + 9 * 2;
a = 6;         // assignments are also void statements
new Point();
```

The execution of this code will generate the store `["John Doe", 24, <Point object>]`. Assignments such as `a = 6` sets the variable a with the value 6 but have no return value per se. Thus the following statements is illegal in QED.

```
a = b = 6 + 9 * 2; // should be b = 6 + 2 * 2; a = b;
b += 4 + (a = 6);  // should be a = 6; b += 4 + a;
```

Now, why save statement return values in a store? The rest of this document shall reveal the numerous advantages of doing such a thing. Let's delve the first tangible benefits right now.

## Variables and constants

In order to access some elements of the store, you can tag them with identifiers, by using prefixes `val <id> =` (for constants) and `var <id> =` (for variables).

```
val num = 3;
var value = num * num;

println("For value " + num + ", the square is " + value);
value *= num;
println("and the cube is " + value + ".");
```

This code does what you expect. The store will first be `[3, 9]`, then `[3, 27]` after `value *= num;` execution. By tagging the store elements with `num` and `value`, you can refer to them or mutate them. Not all store values need to be tagged though, as we will see.

The type of variables and constants is inferred based on their initialization value.

## Blocks

Blocks in QED are surrounded with well-known delimiters. There are four types of delimiters. We will see three of them here. The fourth type (angle brackets '<' and '>'), used to define the user interface, will be studied at a later time.

The following three blocks of code perform the very same execution:

```
{
    val a = 3;
    val b = a * 2;
    println("The double of " + a + " is " + b);
}
```

```
(
    val a = 3,
    val b = a * 2,
    println("The double of " + a + " is " + b)
)
```

```
[
    val a = 3,
    val b = a * 2,
    println("The double of " + a + " is " + b)
]
```

The same code will be executed in all cases and the generated store will be `[3, 6]`. However, blocks are also expressions, so they have a return value of their own, which differs according to their type.

Blocks defined with curly braces `{}` will never return a value (there is an exception we will encounter soon), so the first block has a void return value.

Blocks defined with parentheses `()` return their last statement value (void if no statement). In our example, the last statement is a `println(...)` void expression so it is void. However, parenthesized expressions can be useful to define a single call to an anonymous function.

```
val a = 5;

println("The factorial of " + a + " is " + (
    val count = a;
    var fact = count;

    while(count-- > 1)
        fact *= count;

    fact
);
```

(You may have remarked that delimiters `;` and `,` can be used interchangeably in all kinds of blocks - and the block last statement does not need one, so it's up to you to determine which syntax is more readable.)

Of course, the most frequent use of parenthesized blocks is the special case with a single statement to override operator precedence.

```
val a = 3 * (5 + 2);
```

Blocks defined with square brackets `[]` return the store itself viewed as an array (`[3, 6]` in our previous example). The array type is inferred based on the initial content (`int[]` in this case). The most popular use case for square brackets is to define an array of elements with the same type.

```
val array = ["Batman", "Robin", "Alfred"]; // String[] type 

println("Second name: " + array[1]); // array indexes are 0-based
```

The square bracket block keeps the store in memory since it returned it as an array. Previous blocks, `{...}` returning void and `(...)` returning the last statement value do not need the store upon return, which is thus dismissed when done.

There is a variant of the curly brace blocks with that does not return void and that keeps its store, like square bracket blocks. If a curly brace block is prefixed by the operator `new`, an anonymously typed object consisting of the store content is returned. The variables/constants defined in the block become fields.

```
val obj = new {
    val a = 3;
    val b = a * 2;
}

println("The double of " + obj.a + " is " + obj.b);
```

Thus, with the new store feature and the various kinds of blocks, we can already segment logic (with `{...}`), define calls or change operator precedence (with `(...)`), define arrays (with `[...]`) and objects (with `new {...}`). Stores combined with block types can do a lot.

## Functions and types

In the last section, we introduced calls and object instantations, but with anonymous functions and types that cannot be reused. However, we've also seen that tags can identify store values as variables or constants. Likewise, `fun`-prefixed tags can also identify expressions, including blocks, as reusable functions and types.

```
fun int double(int a) 2 * a;

println("The double of 5 is " + double(5));
```

Function parameters are also part of the store as first tagged fields. At the end of `double` execution, its store is `[5, 10]`. Upon completion, 10 is returned (because `2 * a` is the last statement) and the store, no longer required, is discarded.

A function tag takes this form:

```
Non-void function:

'fun' <return_type> <name>'('<parms...>')' (';' | <expression> ';' | <block>)

Void function:

'fun' <name>'('<parms...>')' (';' | <expression> ';' | <block>)
```

A function can of course take a block as body, using any block delimiter seen above.

```
fun int factorial(int n) (
    var fact = n;

    while(n-- > 1)
        fact *= n;

    fact
)

println("The factorial of 5 is " + factorial(5);
```

You can also use curly brace blocks, even if by themselves they do not return any value. The trick to return a value inside curly brace blocks is to use the `return` function, which stops function execution and resumes the caller execution with the return value sent as argument.

```
fun int factorial(int n) {
    var fact = n;

    while(n-- > 1)
        fact *= n;

    return(fact);
    // any code from here will not execute
}

println("The factorial of 5 is " + factorial(5);
```

For void functions, you can use `return` without the return value argument to exit the function and resume the caller's execution. If a QED function is called without argument, parentheses are not required.

```
fun printResult(String result) {
    println("Result: " + result);
    return;  // or return();
}
```

Bear in mind `return` is not a QED keyword as there is no grammar keyword in QED for flow control. `return` is truly a function and therefore, can be omitted. 

Not calling `return` inside a curly brace function will suspend the execution at the end of its execution.

```
fun printResult(String result) {
    println("Result: " + result);
    // no return() call so suspend here
}

printResult("GOOD"); // this call never completes
```

You may at first glance perceive this behavior as a design flaw, but call suspension is in fact a great feature, especially in the user interface context where, for instance, a dialog box suspends the application after display, waiting for user input. We will cover this case later.

Do you remember the `new {...}` syntax, seen above, that creates anonymously typed objects from their store contents? We can reuse it with tagged blocks, or functions, to create fully typed objects! As a convention, tags in this case start with a capital letter. Function parameters are also considered fields.

```
fun Person(int name, int age) {
    var phone = "";
}

var p = new Person("Joe", 45);

p.phone = "1-555-455-9383";
println(p.name + ", " + p.age + "yo, tel: " + p.phone);
```

The `return` function can still be called in an object creation context. Its behavior is different though. Instead of stopping function execution and resuming caller with a return value, it sends the return value as an event, processed by the caller, and resumes execution. The operator `->` defines a event handler expression (or block, except curly brace blocks) attached to the object. Inside the handler, the internal constant `_obj` defines the current object calling `return` and the internal constant `_ret` defines the return value.

```
fun int Adder(int a, int b) {
    return(a + b);
}

val n1 = 5;
val n2 = 7;

println(n1 + " + " + n2 + " = " + Adder(n1, n2));

var c = 0;
var adder = new Adder(n1, n2) -> (
    println(_obj.a + " + " + _obj.b + " = " + _ret);
    c = _ret;
);

println(adder.a + " + " + adder.b + " = " + c);
```

Both calls to `Adder` generate the same store `[5, 7]` and the three printed lines are exactly the same: `5 + 7 = 12`. 

In QED, a very important feature is that functions can be nested. Nested functions have full access to the parent environment.

```
fun Person(int name, int age) {
    var phone = "";

    fun setPhone(String newPhone)
        phone = newPhone;

    fun String getAge() {
        return(age + "yo");
    }

    fun print()
        println(name + ", " + getAge + ", tel: " + phone);
}

var p = new Person("Joe", 45);

p.setPhone("1-555-455-9383");
p.print;
```

## Flow control

two operators: while and black hole

## Asynchronous operations and coroutines



## User interface

