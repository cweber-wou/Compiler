
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
	
	private void Match(int type)
	{
		if (current.typeSpecifier!=type)
		{
			//error
		}
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
	{   level++;		
		TreeNode tmp = DeclarationList();
		if (t.id!=Constants.EOF)
		{
			throw new IllegalStateException("EOF not Reached token: "+t.toString());
		}
		return tmp;
	}
	
	private TreeNode DeclarationList()
	{
		level++;
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
		next();
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
			next();		
		}
		else {
			while (t.id!=Constants.RPAREN)
			{
				
				c.sibling=Param();
				c=c.sibling;
				next();
				if (t.id!=Constants.COMMA)throw new IllegalStateException("Missing Comma in Parameter List Token: "+t.toString());
				next();
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
		String l="";
		int n=0;
		TreeNode tmp;
		int line=t.lineNumber;
		switch (t.id)
		{
		case Constants.ID: l=t.lexeme; next();// call, var
						switch (t.id)
						{
						case Constants.LPAREN: tmp=Call();break;
						case Constants.EQ:
						case Constants.LBRACKET:tmp=Expresion();break;
						default:throw new IllegalStateException("ID Missing Asignment or Parameters  Token: "+t.toString());	
						}
		case Constants.RETURN: tmp=Return();break;
		case Constants.READ: tmp=Read();break;
		case Constants.WRITE:tmp=Write();break;
		case Constants.IF: tmp=Selection();break;
		case Constants.WHILE: tmp=Iteration();break;
		case Constants.RBRACE: tmp=Compound();break;
		default:throw new IllegalStateException("Unknown Statement  Token: "+t.toString()); 
		}
		tmp.lineNumber=line;
		tmp.sValue=l;
		tmp.nValue=n;
		return tmp;
	}
	
	private TreeNode Iteration()
	{
		TreeNode tmp=new TreeNode();
		return tmp;
	}
	
	private TreeNode Selection()
	{
		TreeNode tmp=new TreeNode();
		return tmp;
	}
	private TreeNode Write()
	{
		TreeNode tmp=new TreeNode();
		return tmp;
	}
	private TreeNode Read()
	{
		TreeNode tmp=new TreeNode();
		return tmp;
	}
	private TreeNode Return()
	{
		TreeNode tmp=new TreeNode();
		return tmp;
	}
	private TreeNode Expresion()
	{
		TreeNode tmp=new TreeNode();
		
		return tmp;
	}
	private TreeNode Call()
	{
		TreeNode tmp=new TreeNode();
		
		return tmp;
	}
	
	public static void main(String[] args) {
		Parser p = new Parser("test");
		p.root=p.Program();
		
	}

}
