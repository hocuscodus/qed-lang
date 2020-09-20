---
layout: page
title: 
permalink: /tour/
---

Welcome to this QED language tour. QED is designed to make nice GUI applications simply but before doing so, you must take a peek under the hood to really see how it performs its magic. Hence this guide...

The goal of this tutorial is to show how basic procedural programming can naturally evolve to encompass event-based GUI development. To achieve this, we will begin with simple expressions - already existing in most mainstream languages - to gradually venture into a new - but easy! - take on programming.

This tutorial assumes you have a working knowledge of procedural programming, in order to keep it brief and to focus on the key differentiators from other languages.

There are lots of niceties to cover, so without further ado, let’s begin our journey!

## The divergence point


Statements, like in mainstream languages, are units containing expressions. They usually come in groups, or blocks. QED statements are roughly equivalent to statements found in mainstream languages.

println(“Hello, World!”);
a[1] = b + c * 4; // operator precedence applies here too
number = boolFlags[index] ? (1 + b) * 3 : 2;

Operators are the same as in well-known languages, except for a very important new one that we will introduce later.
2. Variables
Some expressions may return a result that you want to refer to at a later time. To do so, you may identify the result of expressions returning a value by using variables. Immutable fields are declared using val whereas mutable variables are declared with var. Variable types are inferred from their initialization expression.

val a = “Hello”;
var b = 4;
var c = println(“Hi!”); // illegal, println does not return a value

b *= 5; // ok, mutable
a++; // illegal because a is immutable
3. Statements
You may have remarked that the above expressions are terminated with a semicolon. Semicolons (and also commas as we will see) are used to delimit statements.

Statements may or may not return a value. For example, the execution of the above statements generates the return values x, y and z.
Declarations return a value but assignments do not.

These values are usually dismissed in other languages but not in QED. QED stores them in a table. We’ll see why in the next section.

Statements usually come in groups. The first novelty QED introduces is that the return values of a group of statements are saved in a list upon execution. For example, the execution of the above statements generates the following table.
4. Blocks
A block is a compound expression consisting of grouped statements delimited with symbols. Like other expressions, it may return a value. There are three kinds of blocks: parenthesized, bracketed and braced. The following three blocks execute the same code and generate the same table.

Their difference lies in what values are returned by the block. Parenthesized blocks return the last table value. Usually, parenthesized blocks are made of a single statement to change the default operator precedence, but are generalized in QED to support multiple statements.

Because the last value is always returned, parenthesized blocks must include at least one statement returning a value. This parenthesized block would be illegal. Other sorts of blocks do not have this requirement.

Bracketed statements return the whole table as an array. The type of the array element is also inferred from the defined elements. In this case, it is an array of Object elements (Object[]). If, for instance, the code would be , the return value would be String[].

Bare braced statements return no value. This code would be illegal.

However, when prefixed with ‘new’, the return value of a braced block would be an object. Variables defined inside the structure would be available as object fields to use and modify.
5. Functions
Like variables are used to identify expressions, functions are used to identify blocks, in order to reuse them.
6. The “black hole” operator
Coroutines
