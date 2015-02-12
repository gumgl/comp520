%{
#include <string.h>
#include <math.h>
#include "tree.h"
#include "mini.tab.h"
#include "malloc_safe.h"

/*extern int lineno;*/
/*\n        lineno++;*/
%}

%%
[ \t\r\n]+  /* ignore */;
#.*         /* comment; matches up to EOL */;

0|([1-9][0-9]*) {
  yylval.intconst = atoi(yytext);
  return tINTCONST;
}

(0|([1-9][0-9]*))\.[0-9]+ {
  yylval.floatconst = atof(yytext);
  return tFLOATCONST;
}

"var"     return tVAR;
"float"   return tFLOAT;
"int"     return tINT;
"while"   return tWHILE;
"do"      return tDO;
"done"    return tDONE;
"if"      return tIF;
"then"    return tTHEN;
"else"    return tELSE;
"endif"   return tENDIF;
"print"   return tPRINT;
"read"    return tREAD;

[a-zA-Z_][a-zA-Z0-9_]* {
    yylval.stringconst = (char *) malloc_safe(strlen(yytext) + 1);
    sprintf(yylval.stringconst, "%s", yytext);
    return tIDENTIFIER;
}

.         return yytext[0];
%%
