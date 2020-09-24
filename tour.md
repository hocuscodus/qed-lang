---
layout: page
title: 
permalink: /tour/
---

Welcome to this QED language tour. QED is designed to make nice GUI applications simply but before doing so, you must take a peek under the hood to really see how it works. Hence this guide...

The goal of this tour is to show how basic procedural programming can naturally evolve to encompass event-based GUI development. To achieve this, we will play with simple expressions - existing in most mainstream languages - to gradually venture into a new - but easy! - take on programming.

This tour assumes you have a working knowledge of procedural programming, in order to keep it brief and to focus on the key differentiators from other languages.

In short, QED does *not* define:

- Flow control keywords (if, for, switch, return...) burned into grammar
- Type definition syntax (struct, class, typedef, constructors, fields...)
- Separate variable and constant declaration logic
- Asynchronous syntax (async, await, suspend, go...)
- Separate GUI SDK to GUI platforms

Yet it can emulate all these and still do more than most languages, with this smaller set of concepts.

- Internal stores
- New operators `->`, `while` and whitespace
- Flow control *functions*
- UI blocks `<...>` in the code

Here you will get a taste of QED's expressive power.

There are lots of niceties to cover, so without further ado, let’s begin our journey!

## The main divergence: the store

In most languages, statements composed of pure expressions have no effect or are even discouraged (by a warning in C or an error in Java).

```
println("Hi"); // ok, side effect
c = a + b;     // ok, mutation of var c
a * c - b;     // no effect, can be flagged as warning/error
```

Any statement may or may not return a value. Statements returning nothing will be referred to as void statements here. For non-void statements, most languages automatically dismiss their return values upon finishing their execution; they are never considered.

In QED, the return value of non-void statements is saved in a memory area called the store. For instance, in the following block of statements:

```
"John Doe";
println("Hi"); // void statement, no value is stored here
6 + 9 * 2;
a = 53;        // assignments are also void statements
new Point();
```

The execution of this code will generate the store `["John Doe", 24, <Point object>]`. Assignments such as `a = 53` sets the variable `a` with the value 53 but the assignment operation has no return value per se. Thus the following statements are illegal in QED.

```
a = b = 6 + 9 * 2; // should be b = 6 + 9 * 2; a = b;
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

The type of variables and constants is inferred based on their initialization value. After initialization, a variable or constant type can never change.

## Blocks

Blocks in QED are surrounded with well-known delimiters. There are four types of delimiters. We will see three of them here. The fourth block type (angle brackets `<...>`), used to define the user interface, will be studied at a later time.

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
));
```

(You may have remarked that delimiters `;` and `,` can be used interchangeably in all kinds of blocks - and the block last statement does not need one, so it's up to you to determine which syntax is more readable.)

Of course, the most frequent use of parenthesized blocks is the special case with a single statement to override operator precedence.

```
val a = 3 * (5 + 2);
```

Blocks defined with square brackets `[]` return the store itself viewed as an array (`[3, 6]` in our previous example). The array type is inferred based on the initial content (`int[]` in this case). The most popular use case for square brackets is to define an explicit array of elements with the same type.

```
val array = ["Batman", "Robin", "Alfred"]; // String[] type 

println("Second name: " + array[1]); // array indexes are 0-based
```

The square bracket block keeps the store in memory since it returned it as an array. Previous blocks, `{...}` returning void and `(...)` returning the last statement value do not need the store upon return, which is thus dismissed when done.

There is a variant of the curly brace blocks that does not return void and that keeps its store, like square bracket blocks. If a curly brace block is prefixed by the operator `new`, an anonymously typed object consisting of the store content is returned. The variables/constants defined in the block become fields.

```
val obj = new {
    val a = 3;
    val b = a * 2;
}

println("The double of " + obj.a + " is " + obj.b);
```

Thus, with the new store feature and the various kinds of blocks, we can already segment logic (with `{...}`), define calls or change operator precedence (with `(...)`) and define explicit arrays (with `[...]`) as well as objects (with `new {...}`). Stores combined with block types can do a lot.

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

'fun' <return_type> <name>'('<parms...>')' (';' | <exp> ';' | {...})

Void function:

'fun' <name>'('<parms...>')' (';' | <exp> ';' | {...}>)
```

A function can of course take a block as body, using any block delimiter seen above.

```
fun int factorial(int n) (
    var fact = n;

    while(n-- > 1)
        fact *= n;

    fact
);

println("The factorial of 5 is " + factorial(5));
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

For void functions, you can use `return` without the return value argument to exit the function and resume the caller's execution. For any QED function called without argument, parentheses are not required.

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

In QED, a very important feature is that functions can be nested. Nested functions are fully aware of their parent environment.

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

## Implicit arrays

The arrays we've encountered so far were explicit. All elements were defined at creation time.

```
val array = ["Batman", "Robin", "Alfred"]; // String[] type 
```

For very long arrays, this is not convenient.

There is another way in QED to define arrays implicitly, with a new, special, whitespace operator. The whitespace operator allows expressions to be chained using whitespaces in between and is terminated with a separator (or a curly brace block).

```
<exp1> <exp2> ... (<expN> ';' | {...})
```

The last expression (or block) is the body of the implicit array. All previous expressions are dimensions.

For instance, if I want to define an array of 5 integers having value 4 implicitly and change the middle value to 6 (`[4, 4, 6, 4, 4]`), the following would work.

```
var array = 5 4;

array[2] = 6;
```

This implicit array syntax really looks natural when the body is an object.

```
var array = 10 new Person("", 0);

array[1].age = 60;
array[1].setPhone("1-555-455-9383");
```

## Flow control

We have seen that `return` is a QED standard library function, not a grammar keyword. In fact, there is no grammar keyword dedicated to flow control in QED.

An alternate mechanism is used for flow control. It comprises the store, 
implicit arrays, standard library functions and a last novel operator.

## User interface

## Asynchronous operations and coroutines



