---
layout: default
title:
permalink: /tutorial/
---
# QED Tutorial Part 1 - On Code

This tutorial details how to build a QED program. Then, a second part will tell about GUI attributes, which allows sophisticated GUI building on top of the program logic.

## Ok, what is a QED program?

A QED program (or module) is, simply put, a batch of statements (along with optional package declaration and list of imports). We will skip package and import definitions in this tutorial as they behave like in mainstream languages.

```none
package <namespace>; // optional

import <namespaceX>; // optional

<statement1>
<statement2>
...
<statementN>
```

That's basic but vague so, to formally define it, we will gradually unveil the not-so-many QED statement types.

## Fine, what is a QED statement then?

Although QED offers various sorts of statements, all of them stem from three optional components:

**statement -> [declaration] [simple expression] [block expression]**

To compose your QED statements, you would pick selected components and stack them. The QED grammar enforces ordering (as defined in the above list) and carves out the right syntax for various component mixes. From these mixes, QED statements are thus divided into five categories: simple expressions, block expressions, flow statements, variables and functions.

## Let's start with basic statements... what are they?

**statement -> ~~[declaration]~~ [simple expression] ~~[block expression]~~**

Most QED statements are made of simple expressions only. You may refer to various expressions defined in the complete QED grammar but suffice it to say they are very close to expressions in C/Java (in both operators and precedence). Like in many languages also, they end with a semicolon. The empty expression is allowed.

    println("Hello, World!");
    a = b * 2 + 5;
    ;

## Looks simple! What's next?

Not so fast! At runtime, when evaluating the list of statements that form a QED module, non-void simple expressions (and also block expressions) are used to initialize a QED object.

## Wait, you lost me here, what do you mean by QED object?

To each execution of a list of statements, there is an associated QED composite object that is created. In QED, the object replaces the stack frame used while executing a function call in many other languages.

To understand how this object is built, QED classifies statement expressions in two categories: those who return nothing (void expressions)

    println("This is a string");
    a = 4; // assigment ops in QED return void so you cannot do a = b = c;

and those who return something (non-void expressions).

    "This is a string";
    4;
    5 + b * 2;
    fib(10);

For any statement list, the QED compiler identifies the statements that have simple or block expressions in the second category (not returning void). For these statements, it reserves a field in memory that will hold its return value. The resulting set of fields is the QED object.

At runtime, a QED object is allocated before executing the list of statements. Then, each statement is evaluated as usual. However, the return values of non-void expressions are stored in the QED object, initializing it.

![](/schema1.png)

So expressions not returning a void type are valid in QED because they do have a side effect: QED stores their results in the associated composite object. In the above samples, the QED object comprises a string ("John") and an integer (9). We will soon see the purpose of QED objects but in the meantime...

## Why store the non-void expression values?

Good question. Allow me to answer a bit later but here is a clue: it has something to do with flows. And arrays too...

## Sure. Now with block expressions?

**statement -> ~~[declaration]~~ ~~[simple expression]~~ [block expression]**

Right. A block expression is also an expression, that is, it is evaluated and returns something. However, it is different from a simple expressions as it, just like a QED module, is also a list of statements, but delimited by braces. A block expression defines a new QED object inferred from its non-void expressions in statements.

```
{
  <statement1>
  <statement2>
  ...
  <statementN>
}
```

As opposed to statements with simple expressions, statements with block expressions do not require a semicolon to end. The closing brace will do.

## But what is the return value? The last statement value?

Yes... and no. It depends on the `new` keyword presence or absence.

Block expressions have the peculiarity to be preceded by an optional `new` keyword. The presence or absence of `new` determines the return value of the block expression.

If `new` is not present, the return value is the last statement value (which could be void for a void last statement or if there is no statement in the block). When the last statement value is returned, the object generated from the block expression execution is no longer needed and is thus freed.

However, if `new` is present, the return value is the object itself. There is no special treatment for the last value of the block and the whole object is kept in memory.

![](/schema2.png)

In this example, three QED objects are created. The main object includes "Start" and a child QED object, because the associated block expression is declared with `new`. The child object includes 6, 18 and 12. The 12 comes from a third QED object, which block expression is declared without `new`. This third object was containing 8 and 12 but because `new` was not present, the third object returns its last value (12) in the second object and frees itself.

## I'm ok with expressions, both simple and block... I see the remaining statements are flows, variables and functions. Let's start with flows!

**statement -> ~~[declaration]~~ [simple expression] [block expression]**

Pretty much every language defines at this stage a plethora of grammar rules to process conditionals, loops and other flow statements. Not QED. All you need to define a flow statement is to use both simple expression and block expression in the statement.

If, for instance, you define a statement with a boolean simple expression and a valid block expression

```
5 > 3 {
  println("Five is greater than three.");
}
```

you just defined a conditional flow statement. Any boolean simple expression associated with a block expression acts as a condition to execute or skip the block expression evaluation. So in memory, conditional statements generate fields that contain an object for the block expression (if true) or null (if false).

You may use as well the if(...) system function that returns its argument (converted to boolean if required) to see a more familiar flow statement

```
if(5 > 3) {
  println("Five is greater than three.");
}
```

## Simple conditions are fine but if-else conditions can't be done...

Wrong. The boolean else() system function that creates a field that is the negation of the previous object field...

```
if (5 > 3) {
  println("Five is greater than three.");
}
else {
  println("Three is greater than or equal to five.");
}
```

## How about loops with this system? Is there a for(;;) system function?

## Ok... but this only works for loops having a predefined size... how do you do `while` loops?

## What about simple expressions that are neither conditional nor integer? What do they do about the block expression?

## And for more complicated flows like in switch-case statements?

## Any error handling in QED? Is there try-catch-finally system functions?

## Finally, we're inserting declarations in statements!

Exactly. Declarations will be useful for both variable and function definitions!

Let's start with variables.

**statement -> [declaration] ~~[simple expression]~~ ~~[block expression]~~**

**statement -> [declaration] [simple expression] ~~[block expression]~~**

**statement -> [declaration] ~~[simple expression]~~ [block expression]**

**statement -> [declaration] [simple expression] [block expression]**

Let's continue with functions.

**statement -> [declaration] ~~[simple expression]~~ ~~[block expression]~~**

**statement -> [declaration] ~~[simple expression]~~ [block expression]**

statement   : expression `new`? '{' statement_list '}'

statement         : while_expression? terminator
while_expression  : 'while'? expression


Variables

statement :
  declaration ';' |
  declaration '=' while_expression terminator |
  declaration '=' block_expression |
  while_expression? terminator
declaration : type ID

Functions

statement       :
  declaration parameters (';' | block) |
  declaration ';' |
  declaration '=' while_expression terminator |
  declaration '=' block_expression |
  while_expression? terminator
parameters     : '(' parameter_list? ')'
parameter_list : parameter (',' parameter)*
parameter      : type ID

Object oriented concepts

statement :
  declaration parameters (';' | block) ('->' expression ';')? |
  declaration ';' |
  declaration '=' while_expression terminator |
  declaration '=' block_expression |
  while_expression? terminator
parameters : '(' parameter_list? ')' (':' superclasses)?
superclasses : function_call (',' function_call)*
function_call : type '(' argument_list? ')'
argument_list : expression (',' expression)*

Invoking Functions

GUI
