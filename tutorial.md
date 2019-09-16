---
layout: default
title:
permalink: /tutorial/
---
# QED Tutorial Part 1 - On Code

This tutorial details how to build QED program logic. Then, a second part will tell about GUI attributes, which allows sophisticated GUI building on top of this logic.

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

That's basic but vague so, to formally define it, we will gradually unveil the not-so-many QED types of statement.

## Fine, what is a QED statement then?

Although QED offers various sorts of statements, all of them stem from three optional components:

**statement -> [declaration] [simple expression] [block expression]**

To compose your QED statements, you would pick selected components and stack them. The QED grammar enforces ordering (as defined in the above list) and carves out the right syntax for various component mixes. QED statements are thus divided into five categories: simple expressions, block expressions, flow statements, variables and functions.

## Let's start with simple expressions only... what are they?

**statement -> ~~[declaration]~~ [simple expression] ~~[block expression]~~**

Most QED statements are made of simple expressions only. You may refer to various expressions defined in the complete QED grammar but suffice it to say they are very close to expressions in other languages (in both operators and precedence). Like in many languages also, they end with a semicolon. The empty expression is allowed.

    println("Hello, World!");
    a = b * 2 + 5;
    ;

## Looks simple! What's next?

Not so fast! In a list of statements that form a QED module, non-void simple expressions (and also block expressions) are used to initialize a QED object.

## What do you mean?

To each list of statements is associated a QED aggregate type.

Before diving in, QED separate expressions in two categories: those who return nothing (void expressions)

    println("This is a string");
    a = 4; // assigment ops in QED return void so you cannot do a = b = c;

and those who return something (non-void expressions).

    "This is a string";
    4;
    5 + b * 2
    fib(10);

For any statement list, the QED compiler identifies the statements that have simple or block expressions in the second category (not returning void). For these statements, it reserves a field in memory that will hold its return value. The resulting set of fields is the QED object.

At runtime, a QED object is allocated before running the list of statements. Then, each statement is evaluated as usual. However, the return values of non-void expressions are stored in the QED object, initializing it.

| "John";<br>println("Hello");<br>4 + 3 * 2 - 1; | sdfadfasd  |
|------------------------------------------------------|---|

    "John";
    println("Hello");
    4 + 3 * 2 - 1;

So expressions not returning a void type are valid in QED because they do have a side effect: QED stores their results in the object. In the above samples, QED would define an internal object comprising a string ("John") and an integer (9). We will soon see the purpose of QED objects but in the meantime...

## Now with block expressions...

**statement -> ~~[declaration]~~ ~~[simple expression]~~ [block expression]**

A block expression is also an expression, that is, it is evaluated and returns something. However, it is different from a simple expressions as it, just like a QED module, is a list of statements, but delimited by braces. A block expression defines a new QED type inferred from its non-void expressions.

```
{
  <statement1>
  <statement2>
  ...
  <statementN>
}
```

Block expressions have the peculiarity to be preceded by an optional 'new' keyword. The presence or absence of 'new' determines the return type of the block expression. If new is not present, the return value is the object last value (which could be void). If new is present, the return value is the object itself.

```
"Start";
new {
  6;
  println("hello");
  9 * 2;
  {
    8;
    12;
  }
}
```

to simple expressions. So a statement may end with a semicolon but also a block expression.

    terminator        : ';' | block_expression
    block_expression  : 'new'? block
    block             : '{' statement_list '}'

Because a statement allows only a terminator (the expression is optional)

    statement   : expression? terminator so
    statement   : terminator so
    statement   : block_expression

The block expression statement

    {
      1;
      2;
      4;
    }

creates an object having integers 1, 2 and 4 but will return the last item evaluation (integer 4) and discard the object. If the block expression were empty, or if the last element was of type void, it would have been considered a void block expression.

However, we can put the 'new' operator in front of a block expression. Therefore, the block expression statement

    new {
      1;
      println("Hello");
      "John";
      4;
    }

creates and returns an object having values 1 (integer), "John" (string) and 4 (integer). The println statement, which returns void, is not counted in the object fields but will still output "Hello" while evaluated.

Flow statements

Pretty much every language defines at this stage a plethora of grammar rules to process conditionals, loops and other flow statements. Not QED. Here no extra rule is necessary.

Let's put together all rules we have devised so far.

    statement         : expression? terminator
    terminator        : ';' | block_expression
    block_expression  : 'new'? block
    block             : '{' statement_list '}'

A flow statement is defined using both expression and block expression.

    statement   : expression terminator so
    statement   : expression block_expression

The flow statements are occurrences of the following statement combinations.

statement   : expression 'new'? '{' statement_list '}'

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
