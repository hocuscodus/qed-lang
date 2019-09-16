---
layout: default
title:
permalink: /grammar/
---
# QED Reference

# Appendix A - QED EBNF grammar

```none
qed_module : package? import* statement_list;

package : 'package' name ';';

name : ID ('.' ID)*;

import : 'import' name '.*'? ';';

statement_list : (statement attribute*)*;

statement :
  declaration parameters (';' | block) ('->' block)* |
  declaration ';' |
  declaration '=' (while_expression terminator | block_expression) |
  while_expression? terminator;

declaration : type ID;

type : basic_type | array_type;

basic_type : name;

array_type : name ('[' ']')+;

parameters : '(' parameter_list? ')' (':' superclasses)?;

parameter_list : declaration (',' declaration)*;

superclasses : reference (',' reference)*;

block : '{' statement_list '}';

while_expression : 'while'? simple_expression;

terminator : ';' | block_expression;

block_expression : 'new'? block;

simple_expression : assignment_expression;

assignment_expression :
  reference
  ('=' | '*=' | '/=' | '%=' | '+=' | '-=' | '>>=' | '<<=' | '>>>=' | '&=' | '|=' | '^=')
  conditional_expression;

conditional_expression : conditional_or_expression ('?' expression ':' expression)?;

conditional_or_expression : conditional_and_expression ('||' conditional_and_expression)*;

conditional_and_expression : inclusive_or_expression ('&&' inclusive_or_expression)*;

inclusive_or_expression : exclusive_or_expression ('|' exclusive_or_expression)*;

exclusive_or_expression : and_expression ('^' and_expression)*;

and_expression : equality_expression ('&' equality_expression)*;

equality_expression : relational_expression (('==' | '!=') relational_expression)*;

relational_expression : shift_expression (('<' | '>' | '<=' | '>=') shift_expression)*;

shift_expression : additive_expression (('<<' | '>>' | '>>>') additive_expression)*;

additive_expression : multiplicative_expression (('+' | '-') multiplicative_expression)*;

multiplicative_expression : unary_expression (('*' | '/' | '%') unary_expression)*;

unary_expression :
  ('++' | '--' | '+' | '-')* unary_expression_not_plus_minus |
  '&' INTEGER;

unary_expression_not_plus_minus :
  ('~' | '!')* unary_expression |
  cast_expression |
  postfix_expression;

cast_expression : '(' (simple_expression | array_type) ')' unary_expression_not_plus_minus;

postfix_expression : primary ('++' | '--')*;

primary : '(' expression ')' | reference | literal;

reference :
  (primary '.')? 'new'? ID ('(' argument_list? ')')? ('[' expression ']')* ('->' block)?;

argument_list : expression (',' expression)*;

literal ::= INTEGER | FLOAT | BOOLEAN | CHARACTER | STRING;

expression : simple_expression | block_expression;

attribute : '@' ID '(' expression? ')';

ID : (A..Z | a..z | '_')) (A..Z | a..z | '_' | '-'))*;
INTEGER : '-'? (0..9* | '0x' (0..9 | A..F | a..f)+);
FLOAT : '-'? 0..9* ('.' 0..9*)+;
BOOLEAN : 'true' | 'false';
CHARACTER : "'" . "'";
STRING : '"' .* '"';
COMMENT : '/*' -> '*/' | '//' -> '\n';
WS : ' ';
```
