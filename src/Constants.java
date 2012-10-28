/*
* Author: Bob Broeg
* Created: Tuesday, April 20, 2004 9:13:23 AM
* Modified: Tuesday, April 20, 2004 9:13:23 AM
* Constants for the C Minus Compiler Project
*/
public class Constants {
public final static int
EOF = 0,
ERROR = 1,
ELSE = 2,
IF = 3,
INT = 4,
RETURN = 5,
VOID = 6,
WHILE = 7,
PLUS = 8,
MINUS = 9,
MULT = 10,
DIV = 11,
LS = 12,
LEQ = 13,
GT = 14,
GEQ = 15,
EQ = 16,
NEQ = 17,
ASSIGN = 18,
SEMI = 19,
COMMA = 20,
LPAREN = 21,
RPAREN = 22,
LBRACKET = 23,
RBRACKET = 24,
LBRACE = 25,
RBRACE = 26,
START_COMMENT = 27,
STOP_COMMENT = 28,
READ = 30,
WRITE = 31,
NUMBER = 32,
ID = 33,
PROGRAM = 40,
DECLARATION = 41,
VARIABLE = 42,
ARRAY = 43,
FUNCTION = 44,
EXPRESSION = 45,
CALL = 46,
COMPOUND = 47,
TYPE_SPECIFIER = 48,
PARAMETER_LIST = 49,
PARAMETER = 50,
STATEMENT_LIST = 51,
STATEMENT = 52,
ARGUMENTS = 53;

public static String getSting(int id)
{
	String out="";
switch (id) {
case EOF:
	out = "EOF";
	break;
case ERROR:
	out = "error";
	break;
case ELSE:
	out = "else";
	break;
case IF:
	out = "if";
	break;
case INT:
	out = "int";
	break;
case RETURN:
	out = "return";
	break;
case VOID:
	out = "void";
	break;
case WHILE:
	out = "while";
	break;
case PLUS:
	out = "Plus";
	break;
case MINUS:
	out = "minus";
	break;
case MULT:
	out = "mult";
	break;
case DIV:
	out = "Div";
	break;
case LS:
	out = "LS" ;
	break;
case LEQ:
	out = "leq";
	break;
case GT:
	out = "Gt";
	break;
case GEQ:
	out = "geq";
	break;
case EQ:
	out = "eq";
	break;
case NEQ:
	out = "neq";
	break;
case ASSIGN:
	out = "Asignment";
	break;
case SEMI:
	out = "Semi";
	break;
case COMMA:
	out = "Comma";
	break;
case LPAREN:
	out = "LParen";
	break;
case RPAREN:
	out = "RParen";
	break;
case LBRACKET:
	out = "Lbracket";
	break;
case RBRACKET:
	out = "Rbracket";
	break;
case LBRACE:
	out = "Lbrace";
	break;
case RBRACE:
	out = "Rbrace";
	break;
case START_COMMENT:
	out = "Start Comment";
	break;
case STOP_COMMENT:
	out = "Stop Comment";
	break;
case READ:
	out = "Read";
	break;
case WRITE:
	out = "Write";
	break;
case NUMBER:
	out = "Number";
	break;
case ID:
	out = "ID";
	break;
case PROGRAM:
	out = "Program";
	break;		
case DECLARATION:
	out = "Declaration";
	break;
case VARIABLE:
	out = "Variable";
	break;
case ARRAY:
	out = "Array";
	break;
case FUNCTION:
	out = "Function";
	break;
case EXPRESSION:
	out = "Expression";
	break;
case CALL:
	out = "Call";
	break;
case COMPOUND:
	out = "Compound";
	break;
case TYPE_SPECIFIER:
	out = "Type Spec";
	break;
case PARAMETER_LIST:
	out = "Parameter List";
	break;
case PARAMETER:
	out = "Parameter";
	break;
case STATEMENT_LIST:
	out = "Statement List";
	break;
case STATEMENT:
	out = "Statement";
	break;
case ARGUMENTS:
	out = "Arguments";
	break;

}
return out;
}
}
