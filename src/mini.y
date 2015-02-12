%{
#include <stdio.h>
#include "tree.h"
#include "minitypes.h"

extern char *yytext;

void yyerror() {
  fprintf(stderr, "syntax error before \"%s\"\n", yytext);
}
%}

%parse-param {program **program_addr}

%union {
  program *prog;
  decl_node *decl;
  stmt_node *stmt;
  expr_node *expr;
  int intconst;
  float floatconst;
  char *stringconst;
}

%token tVAR tFLOAT tINT tWHILE tDO tDONE tIF tTHEN tELSE tENDIF tPRINT tREAD
%token <intconst> tINTCONST
%token <floatconst> tFLOATCONST
%token <stringconst> tIDENTIFIER

%type <prog> program
%type <decl> decls decl
%type <stmt> stmts stmt
%type <expr> expr

%left '+' '-'
%left '*' '/'

%start program

%%
program: decls stmts   {$$ = *program_addr = make_program($1, $2);}
;

decls: decl decls      {$1->next = $2; $$ = $1;}
     |                 {$$ = NULL;}
;

stmts: stmt stmts      {$1->next = $2; $$ = $1;}
     |                 {$$ = NULL;}
;

decl: tVAR tIDENTIFIER ':' tFLOAT ';'
        {$$ = make_decl($2, FLOAT_TYPE);}
    | tVAR tIDENTIFIER ':' tINT ';'
        {$$ = make_decl($2, INT_TYPE);}
;

stmt: tIF expr tTHEN stmts tENDIF
        {$$ = make_stmt_if($2, $4);}
    | tIF expr tTHEN stmts tELSE stmts tENDIF
        {$$ = make_stmt_if_else($2, $4, $6);}
    | tWHILE expr tDO stmts tDONE
        {$$ = make_stmt_while($2, $4);}
    | tIDENTIFIER '=' expr ';'
        {$$ = make_stmt_assign($1, $3);}
    | tREAD tIDENTIFIER ';'
        {$$ = make_stmt_read($2);}
    | tPRINT expr ';'
        {$$ = make_stmt_print($2);}
;

expr: tIDENTIFIER      {$$ = make_expr_id($1);}
    | tINTCONST        {$$ = make_expr_int_const($1);}
    | tFLOATCONST      {$$ = make_expr_float_const($1);}
    | expr '*' expr    {$$ = make_expr_mult($1, $3);}
    | expr '/' expr    {$$ = make_expr_div($1, $3);}
    | expr '+' expr    {$$ = make_expr_plus($1, $3);}
    | expr '-' expr    {$$ = make_expr_minus($1, $3);}
    | '-' expr         {$$ = make_expr_unary_minus($2);}
    | '(' expr ')'     {$$ = $2;}
;
%%
