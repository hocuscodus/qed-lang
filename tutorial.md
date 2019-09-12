---
layout: default
title: 
permalink: /tutorial/
---
QED Tutorial Part 1 - On Code

This tutorial will first detail each of these categories, in order to build QED program logic. Then, a second part will tell about GUI attributes, which allows sophisticated GUI building on top of QED program logic.

A QED program, simply put, is a batch of statements. That's basic but vague so, to formally define it, we will gradually unveil the QED grammar (in ANTLR form) and explain it as we go.

qed_module      : statement_list
statement_list  : statement*

The key to understanding QED is the statement definition. QED offers various sorts of statements but all QED statements are made of these three optional components:

    statement -> [declaration] [expression] [block_expression]

To compose your QED statements, you would pick selected components and stack them. The only requirement is to keep the ordering as defined in the above list. For instance, you cannot add declaration after the expression.

Fortunately, the QED grammar enforces ordering and carves out the right syntax for various component mixes. QED statements are thus divided into five categories: expressions, block expressions, flow statements, variables and functions.

Expressions

    statement   : expression? terminator
    terminator  : ';'

Most QED statements are made of expressions only. You may refer to various expressions defined in the complete QED grammar at the end of this tutorial but suffice it to say that they are very close to expressions in other languages (in both operators and precedence). Like in many languages also, they end with a semicolon and an empty expression is allowed.

    println("Hello, World!");
    a = b * 2 + 5;

Expressions (and block expressions) can be divided in two categories: those who return nothing (void expressions)

    println("This is a string");
    a = 4; // assigment ops in QED returns void

and those who return something (non-void expressions).

    "This is a string";
    4;
    5 + b * 2
    fib(10);

Before evaluating a list of statements, QED allocates a chunk of memory for non-void expression values. This chunk of memory is considered a QED object. When evaluating non-void expressions, return values are stored in the associated object.

| "John";<br>println("Hello");<br>4 + 3 * 2 - 1; | sdfadfasd  |
|------------------------------------------------------|---|

    "John";
    println("Hello");
    4 + 3 * 2 - 1;

Expressions not returning a void type are valid in QED because QED stores their results in memory. In the above samples, QED would define an internal object comprising a string ("John") and an integer (provided that b and c are integers). We will soon see how to access and use this object.

Block expressions

Block expression is also an expression, that is, it is evaluated and returns something. However, they are orthogonal (separate) to classic expressions as there cannot be block expressions within classic expressions. So a statement may end with a semicolon but also a block expression.

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
