public class Token {
	public int id;
	public String lexeme;
	public int lineNumber;
	public int number;

	public String toString() {
		String out = new String();
		String id = new String();
		switch (this.id) {
		case Constants.EOF:
			id = "EOF";
			break;
		case Constants.ERROR:
			id = "error";
			break;
		case Constants.ELSE:
			id = "else";
			break;
		case Constants.IF:
			id = "if";
			break;
		case Constants.INT:
			id = "int";
			break;
		case Constants.RETURN:
			id = "return";
			break;
		case Constants.VOID:
			id = "void";
			break;
		case Constants.WHILE:
			id = "while";
			break;
		case Constants.PLUS:
			id = "Plus";
			break;
		case Constants.MINUS:
			id = "minus";
			break;
		case Constants.MULT:
			id = "mult";
			break;
		case Constants.DIV:
			id = "Div";
			break;
		case Constants.LS:
			id = "LS" ;
			break;
		case Constants.LEQ:
			id = "leq";
			break;
		case Constants.GT:
			id = "Gt";
			break;
		case Constants.GEQ:
			id = "geq";
			break;
		case Constants.EQ:
			id = "eq";
			break;
		case Constants.NEQ:
			id = "neq";
			break;
		case Constants.ASSIGN:
			id = "Asignment";
			break;
		case Constants.SEMI:
			id = "Semi";
			break;
		case Constants.COMMA:
			id = "Comma";
			break;
		case Constants.LPAREN:
			id = "LParen";
			break;
		case Constants.RPAREN:
			id = "RParen";
			break;
		case Constants.LBRACKET:
			id = "Lbracket";
			break;
		case Constants.RBRACKET:
			id = "Rbracket";
			break;
		case Constants.LBRACE:
			id = "Lbrace";
			break;
		case Constants.RBRACE:
			id = "Rbrace";
			break;
		case Constants.START_COMMENT:
			id = "Start Comment";
			break;
		case Constants.STOP_COMMENT:
			id = "Stop Comment";
			break;
		case Constants.READ:
			id = "Read";
			break;
		case Constants.WRITE:
			id = "Write";
			break;
		case Constants.NUMBER:
			id = "Number";
			break;
		case Constants.ID:
			id = "ID";
			break;
		case Constants.PROGRAM:
			id = "Program";
			break;		
		case Constants.DECLARATION:
			id = "Declaration";
			break;
		case Constants.VARIABLE:
			id = "Variable";
			break;
		case Constants.ARRAY:
			id = "Array";
			break;
		case Constants.FUNCTION:
			id = "Function";
			break;
		case Constants.EXPRESSION:
			id = "Expression";
			break;
		case Constants.CALL:
			id = "Call";
			break;
		case Constants.COMPOUND:
			id = "Compound";
			break;
		case Constants.TYPE_SPECIFIER:
			id = "Type Spec";
			break;
		case Constants.PARAMETER_LIST:
			id = "Parameter List";
			break;
		case Constants.PARAMETER:
			id = "Parameter";
			break;
		case Constants.STATEMENT_LIST:
			id = "Statement List";
			break;
		case Constants.STATEMENT:
			id = "Statement";
			break;
		case Constants.ARGUMENTS:
			id = "Arguments";
			break;

		}
		out = id + " " + this.lexeme + " " + this.number + " "
				+ this.lineNumber;
		return out;
	}
}
