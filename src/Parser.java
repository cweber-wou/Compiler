
public class Parser {

	public TreeNode root;
	public Scaner scan;
	public TreeNode current;
	public Token t;
	int level=0;
	
	public Parser (String fileName)
	{
		scan=new Scaner(fileName);
		level=0;
		root = new TreeNode();
		root.nodeType=Constants.PROGRAM;
		current=root;
		next();
	}
	
	public void start()
	{
		root=Program();
	}
	
	private void next()
	{
		Token t = scan.next();
		while (t.id == Constants.START_COMMENT)
		{
			t=scan.next();
		}
		this.t=t;
	}
	private TreeNode Program()
	{   		
		TreeNode tmp = new TreeNode();
		tmp.nodeType=Constants.PROGRAM;
		tmp.lineNumber=t.lineNumber;
		tmp.level=0;
		tmp.sibling=DeclarationList();
		if (t.id!=Constants.EOF)
		{
			throw new IllegalStateException("EOF not Reached token: "+t.toString());
		}
		return tmp;
	}
	
	private TreeNode DeclarationList()
	{
		TreeNode tmp = Declaration();
		if (t.id!=Constants.EOF)
		{
			tmp.sibling=DeclarationList();
		}
		
		return tmp;
	}
	private int TypeSpecifier()
	{
		switch (t.id)
		{
		case Constants.VOID: next(); return Constants.VOID; 
		case Constants.INT: next(); return Constants.INT; 
		default:
			throw new IllegalStateException("Invalid Varible Type token: "+t.toString());
		}
	}
	
	private TreeNode Declaration()
	{
		TreeNode tmp;
		int type;
		String l;
		int line=t.lineNumber;
		type= TypeSpecifier();
		if (t.id==Constants.ID)
		{
			l=t.lexeme;
			next();
		}
		else throw new IllegalStateException("Missing Varible ID token: "+t.toString());
		switch (t.id)
		{
			case Constants.LPAREN: tmp=FunctionDeclaration(); break;
			case Constants.LBRACKET: tmp=ArrayDeclaration(); break;
			case Constants.SEMI: tmp=new TreeNode();
								tmp.nodeType=Constants.VARIABLE;
								
								break;
			default: throw new IllegalStateException("Missing Semi Colon, Array set, or FunctionParams Token: "+t.toString());
			
		}
		tmp.typeSpecifier=type;
		tmp.lineNumber=line;
		tmp.sValue=l;
		tmp.level=level;
		
		return tmp;
	}
	
	private TreeNode VarDeclaration()
	{
		TreeNode tmp;
		int type;
		String l;
		int line=t.lineNumber;
		type= TypeSpecifier();
		if (t.id==Constants.ID)
		{
			l=t.lexeme;
			next();
		}
		else throw new IllegalStateException("Missing Varible ID token: "+t.toString());
		switch (t.id)
		{
			
			case Constants.LBRACKET: tmp=ArrayDeclaration(); break;
			case Constants.SEMI: tmp=new TreeNode();
								tmp.nodeType=Constants.VARIABLE;
								break;
			default: throw new IllegalStateException("Missing Semi Colon, Array set Token: "+t.toString());
			
		}
		tmp.typeSpecifier=type;
		tmp.lineNumber=line;
		tmp.sValue=l;
		tmp.level=level;
		next();
		return tmp;
	}
	
	private TreeNode ArrayDeclaration()
	{	TreeNode tmp=new TreeNode();
		tmp.nodeType=Constants.ARRAY;
		next();
		if (t.id==Constants.NUMBER)
			tmp.nValue=t.number;
		else throw new IllegalStateException("Missing number in Array Declaration Token: "+t.toString());
		next();
		if (t.id!=Constants.RBRACKET)throw new IllegalStateException("Missing Right Bracket in Array Declration Token: "+t.toString());
		next();
		if (t.id!=Constants.SEMI)throw new IllegalStateException("Missing Semi Colon Token: "+t.toString());
		next();
		return tmp;
	}
	
	
	private TreeNode FunctionDeclaration()
	{
		TreeNode tmp=new TreeNode();
		tmp.nodeType=Constants.FUNCTION;
		tmp.C1=ParameterList();
		tmp.C2=Compound();
		
		return tmp;
	}
	private TreeNode ParameterList()
	{
		level++;
		next();
		TreeNode tmp = new TreeNode();
		TreeNode c=tmp;
		tmp.nodeType=Constants.PARAMETER_LIST;
		tmp.lineNumber=t.lineNumber;
		
		if (t.id==Constants.VOID) {
			tmp.typeSpecifier=Constants.VOID;
			next();		
		}
		else {
			tmp.typeSpecifier=Constants.INT;
			while (t.id!=Constants.RPAREN)
			{
				
				c.sibling=Param();
				c=c.sibling;
				if (t.id!=Constants.RPAREN)
				if (t.id!=Constants.COMMA)throw new IllegalStateException("Missing Comma in Parameter List Token: "+t.toString());
				else next();
				
			}
		}
		level --;
		if (t.id!=Constants.RPAREN) throw new IllegalStateException("Missing Right Paran in Parameter List Token: "+t.toString());
		next();
		return tmp;
	}
	private TreeNode Param()
	{
		TreeNode tmp;
		int type;
		String l;
		int line=t.lineNumber;
		type= TypeSpecifier();
		if (t.id==Constants.ID)
		{
			l=t.lexeme;
			next();
		}
		else throw new IllegalStateException("Missing Varible ID token: "+t.toString());
		switch (t.id)
		{
			
			case Constants.LBRACKET:
									next();
									if (t.id != Constants.RBRACKET)throw new IllegalStateException("Missing Right Bracket in array Decleration: "+t.toString());
									next();
									tmp=new TreeNode();
									tmp.nodeType=Constants.ARRAY;
									break;
			case Constants.COMMA: tmp=new TreeNode();
								tmp.nodeType=Constants.VARIABLE;
								break;
			case Constants.RPAREN:tmp = new TreeNode();
								tmp.nodeType=Constants.VARIABLE;
								break;
			default: throw new IllegalStateException("Misplaced Token in parameter list: "+t.toString());
			
		}
		tmp.typeSpecifier=type;
		tmp.lineNumber=line;
		tmp.sValue=l;
		tmp.level=level;
		
		return tmp;
		
		
	}

	private TreeNode Compound()
	{
		TreeNode tmp=new TreeNode();
		tmp.nodeType=Constants.COMPOUND;
		tmp.lineNumber=t.lineNumber;
		if (t.id!=Constants.LBRACE) throw new IllegalStateException("Missing Left Brace in Compound Statement Token: "+t.toString());
			
		next();
		level++;
		tmp.C1=LocalDeclaration();
		tmp.C2=StatementList();
		level--;
		if (t.id!=Constants.RBRACE)throw new IllegalStateException("Missing Right Brace in Compound Statement Token: "+t.toString());
		next();
		return tmp;
	}

	private TreeNode StatementList()
	{
		TreeNode tmp= new TreeNode();
		TreeNode c=tmp;
		tmp.nodeType=Constants.STATEMENT_LIST;
		tmp.lineNumber=t.lineNumber;
		while (t.id != Constants.RBRACE)
		{
			c.sibling=Statement();
			c=c.sibling;
		}
		
		return tmp;
	}
	
	private TreeNode LocalDeclaration()
	{ 
		TreeNode tmp=new TreeNode();
		TreeNode c=tmp;
		tmp.nodeType=Constants.DECLARATION;
		tmp.lineNumber=t.lineNumber;
		while (t.id==Constants.INT)
		{
			c.sibling=VarDeclaration();
			c=c.sibling;
			
		}
		return tmp;
	}
	
	private TreeNode Statement()
	{
		
		TreeNode tmp;
		int line=t.lineNumber;
		switch (t.id)
		{
		case Constants.ID: tmp=ExpressionSmt();break;
		case Constants.RETURN: tmp=Return();break;
		case Constants.READ: tmp=Read();break;
		case Constants.WRITE:tmp=Write();break;
		case Constants.IF: tmp=Selection();break;
		case Constants.WHILE: tmp=Iteration();break;
		case Constants.LBRACE: tmp=Compound();break;
		default:throw new IllegalStateException("Unknown Statement  Token: "+t.toString()); 
		}
		tmp.lineNumber=line;
		
		return tmp;
	}
	private TreeNode SimpleExpresion()
	{
		TreeNode tmp = AdditiveExpression();
		switch (t.id)
		{
		case Constants.EQ:
		case Constants.LEQ:
		case Constants.LS:
		case Constants.GEQ:
		case Constants.GT:
		case Constants.NEQ: 
			TreeNode tmp2 = Relop(); 
			tmp2.C1=tmp; 
			tmp=tmp2; 
			tmp.C2=AdditiveExpression(); 
			break;
		case Constants.SEMI:  return tmp; 
		//default: throw new IllegalStateException("Unexpected Token in Simple Expresion Token: "+t.toString()); 
		}
		
		return tmp;
	}
	
	private TreeNode Relop()
	{
		TreeNode tmp=new TreeNode();
		switch (t.id)
		{
		case Constants.EQ: 
		case Constants.LEQ:
		case Constants.LS:
		case Constants.GEQ:
		case Constants.GT:
		case Constants.NEQ: tmp.nodeType=t.id; tmp.lineNumber=t.lineNumber;tmp.sValue=t.lexeme;break;
		default: throw new IllegalStateException("Missing Relational Operator Token: "+t.toString()); 
		}
		next();
		
		return tmp;
	}
	private TreeNode AdditiveExpression()
	{
		TreeNode tmp=new TreeNode();
		tmp.C1=Term();
		switch (t.id)
		{
		case Constants.PLUS:
		case Constants.MINUS:tmp.nodeType=t.id;next();
			tmp.C2=Term();
			break;
		default:tmp=tmp.C1;break;
		}
		return tmp;
	}
	private TreeNode Term()
	{
		TreeNode tmp=new TreeNode();
		tmp.C1=Factor();
		switch (t.id)
		{
		case Constants.MULT:
		case Constants.DIV:tmp.nodeType=t.id;next();
			tmp.C2=Factor();
			break;
		default:tmp=tmp.C1;break;
		}
		
		return tmp;
	}
	private TreeNode Factor()
	{
		TreeNode tmp=new TreeNode();
		switch (t.id)
		{
		case Constants.LPAREN: next(); tmp=Expresion(); if (t.id != Constants.RPAREN) throw new IllegalStateException("Missing Left Paren Token: "+t.toString()); next(); break;
		case Constants.NUMBER: tmp.nodeType=t.id; tmp.lineNumber=t.lineNumber; tmp.sValue=t.lexeme; tmp.nValue=t.number; next(); return tmp;
		case Constants.ID: tmp=Variable(); 
			switch (t.id)
			{
			case Constants.LPAREN: TreeNode tmp2=Call(); tmp2.sValue=tmp.sValue; tmp=tmp2; break;
			}break;
			default: tmp=null;
		}
		
		return tmp;
	}
	
	private TreeNode Variable()
	{
		TreeNode tmp = new TreeNode();
		tmp.sValue=t.lexeme;
		tmp.lineNumber=t.lineNumber;
		tmp.nodeType=Constants.VARIABLE;
		next();
		if (t.id==Constants.LBRACKET)
		{
			next();
			tmp.C1 = Expresion();
			if (t.id!=Constants.RBRACKET) throw new IllegalStateException("Missign Right Backet in Array Token: "+t.toString());
			next();
			tmp.nodeType=Constants.ARRAY;
		}
		return tmp;
	}
	private TreeNode Assignment()
	{
		TreeNode tmp = new TreeNode();
		tmp.nodeType=Constants.ASSIGN;
		return tmp;
	}
	private TreeNode Iteration()
	{
		TreeNode tmp=new TreeNode();
		next(); //consume while
		if (t.id!=Constants.LPAREN) throw new IllegalStateException("Missing Left Paren in while  Token: "+t.toString()); 
		next (); //consume (
		tmp.C1=Expresion();
		if (t.id!=Constants.RPAREN) throw new IllegalStateException("Missing Right Paren in while  Token: "+t.toString()); 
		next (); //consume )
		tmp.C2=Statement();
		tmp.nodeType=Constants.WHILE;
		return tmp;
	}
	
	private TreeNode Selection()
	{
		TreeNode tmp=new TreeNode();
		next(); //consume if
		if (t.id!=Constants.LPAREN) throw new IllegalStateException("Missing Left Paren in If  Token: "+t.toString()); 
		next (); //consume (
		tmp.C1=Expresion();
		if (t.id!=Constants.RPAREN) throw new IllegalStateException("Missing Right Paren in If  Token: "+t.toString()); 
		next (); //consume )
		tmp.C2=Statement();
		if (t.id==Constants.ELSE)
			{
				next();//consume else
				tmp.C3=Statement();
			}
		tmp.nodeType=Constants.IF;
		return tmp;
	}
	private TreeNode Write()
	{
		TreeNode tmp=new TreeNode();
		tmp.nodeType=Constants.WRITE;
		next();//consumer write
		tmp.C1=Expresion();
		if (t.id!=Constants.SEMI) throw new IllegalStateException("Missing ; after write  Token: "+t.toString()); 
		next();
		return tmp;
	}
	private TreeNode Read()
	{
		TreeNode tmp=new TreeNode();
		tmp.nodeType=Constants.READ;
		next();//consume read
		tmp.C1=Variable();
		if (t.id!=Constants.SEMI) throw new IllegalStateException("Missing ; after read  Token: "+t.toString()); 
		next();
		return tmp;
	}
	private TreeNode Return()
	{
		TreeNode tmp=new TreeNode();
		next();//eat return
		tmp.nodeType=Constants.RETURN;
		tmp.C1=Expresion();
		if (t.id!=Constants.SEMI) throw new IllegalStateException("Missing ; after return  Token: "+t.toString()); 
		next();
		return tmp;
	}
	private TreeNode ExpressionSmt()
	{
		TreeNode tmp = Expresion();
		if (t.id!=Constants.SEMI)throw new IllegalStateException("Missign Semi in Expression Token: "+t.toString());
		next();
		return tmp;
	}
	private TreeNode Expresion()
	{
		TreeNode tmp=new TreeNode();
		TreeNode tmp2=null;
		if (t.id == Constants.ID && (scan.peek.id==Constants.ASSIGN|| scan.peek.id==Constants.LBRACKET)) 
			{tmp2=Variable();// call, var
		switch (t.id)
		{
		/*case Constants.LPAREN: 	tmp=Call(); 
								tmp.lineNumber=t.lineNumber;
								tmp.sValue=tmp2.sValue;
								if (t.id==Constants.SEMI)return tmp; 
								else throw new IllegalStateException("No Semi Collon after Function call Token: "+t.toString());*/
									
		case Constants.ASSIGN: 	tmp=Assignment();
								tmp.C1=tmp2;
								next();
								tmp2=tmp;tmp=SimpleExpresion();
								if (tmp2!=null)
								{
									if (tmp.nodeType!=0) 
										tmp2.C2=tmp;
									tmp=tmp2;
								}
								
								return tmp;
		
			case Constants.EQ: 
			case Constants.LEQ:
			case Constants.LS:
			case Constants.GEQ:
			case Constants.GT:
			case Constants.NEQ:
			tmp=SimpleExpresion();
				tmp.C1=tmp2;
				return tmp;
			default:tmp=tmp2; return tmp;
			
			
								

		//default:throw new IllegalStateException("ID Missing Asignment or Parameters  Token: "+t.toString());	
		}}
	   //next();
		tmp=SimpleExpresion();
		
		
		return tmp;
	}
	private TreeNode Call()
	{
		TreeNode tmp=new TreeNode();
		tmp.nodeType=Constants.CALL;
		
		tmp.C1=Arguments();
		return tmp;
	}
	
	private TreeNode Arguments()
	{
		TreeNode tmp=new TreeNode();
		TreeNode c=tmp;
		tmp.nodeType=Constants.ARGUMENTS;
		
		while (t.id!= Constants.RPAREN)
		{
			next(); //consume ( or ,
			c.sibling=Argument();
			c=c.sibling;
			if (t.id!=Constants.COMMA&&t.id!=Constants.RPAREN) throw new IllegalStateException("Missign Comma in Argument list Token: "+t.toString());
		}
		next(); // consume )
		return tmp;
	}
	
	private TreeNode Argument()
	{
		TreeNode tmp=new TreeNode();
		switch (t.id)
		{
		case Constants.ID: tmp=Variable(); break;
		case Constants.NUMBER: tmp.lineNumber=t.lineNumber; tmp.nValue=t.number; tmp.nodeType=t.id; tmp.sValue=t.lexeme; next();break;
		//default: throw new IllegalStateException("unexpected Token in argument Token: "+t.toString());
		}
		return tmp;
	}
	
	public static void main(String[] args) {
		Parser p = new Parser("test");
		p.root=p.Program();
		System.out.println("Complete!");
		Navi n = new Navi();
		n.preorder(p.root);
	}

}
