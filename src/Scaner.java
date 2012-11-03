import java.io.IOException;


public class Scaner {
	private StreamReader f;
	private SymbolTable keyword;
	
	
	public Scaner (String fileName)
	{
		f= new StreamReader (fileName);
		keyword= new SymbolTable();
	}
	
	public Token next()
	{
		Token t= new Token();
		char c=f.getCurent();
		while ((Character.isWhitespace(c)) && ((char) -1 != c))
		{
			
			c=f.next();
		}
		if (Character.isLetter(c))
		{
			String s = new String();
			while (Character.isLetter(c))
			{
				s=s+c;
				c=f.next();
			}
			if (keyword.exists(s))
			{
				t.id=keyword.pull(s);
			}
			else 
			{
				t.id=Constants.ID;	
			}
			t.lexeme=s;
			t.lineNumber=f.getLine();
			
			t.number=0;
		}
		else if (Character.isDigit(c))
		{
			String s = new String();
			while (Character.isDigit(c))
			{
				s=s+c;
				c=f.next();
			}
			t.lexeme=s;
			t.lineNumber=f.getLine();
			t.id=Constants.NUMBER;
			t.number=Integer.parseInt(s);
		}
		else
		{
			switch (c)
			{
			case '+':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.PLUS;
				t.number=0;
				f.next();
				break;
			case '*':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.MULT;
				t.number=0;
				f.next();
				break;
			case '-':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.MINUS;
				t.number=0;
				f.next();
				break;
			case '/':
				switch (f.getPeek())
				{
				case '*':
					while ((f.getCurent()!='*' ) || (f.getPeek()!='/'))
					{
						f.next();
					}
					t.lexeme="Comment";
					t.lineNumber=f.getLine();
					t.id=Constants.START_COMMENT;
					t.number=0;
					f.next();
					break;
				default:
					t.lexeme=Character.toString(c);
					t.lineNumber=f.getLine();
					t.id=Constants.DIV;
					t.number=0;
					break;
				}
				f.next();
				
				break;
			case ';':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.SEMI;
				t.number=0;
				f.next();
				break;
			case ',':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.COMMA;
				t.number=0;
				f.next();
				break;
			case '(':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.LPAREN;
				t.number=0;
				f.next();
				break;
			case ')':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.RPAREN;
				t.number=0;
				f.next();
				break;
			case '{':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.LBRACE;
				t.number=0;
				f.next();
				break;
			case '}':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.RBRACE;
				t.number=0;
				f.next();
				break;
			case '[':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.LBRACKET;
				t.number=0;
				f.next();
				break;
			case ']':
				t.lexeme=Character.toString(c);
				t.lineNumber=f.getLine();
				t.id=Constants.RBRACKET;
				t.number=0;
				f.next();
				break;
			case '=':
				if (f.getPeek()=='=')
				{
					t.lexeme=Character.toString(c)+f.getPeek();
					t.lineNumber=f.getLine();
					t.id=Constants.EQ;
					t.number=0;
					f.next();
					f.next();
				}
				else
				{
					t.lexeme=Character.toString(c);
					t.lineNumber=f.getLine();
					t.id=Constants.ASSIGN;
					t.number=0;
					f.next();
				}
				break;
			case '!':
				if (f.getPeek()=='=')
				{
					t.lexeme=Character.toString(c)+f.getPeek();
					t.lineNumber=f.getLine();
					t.id=Constants.NEQ;
					t.number=0;
					f.next();
					f.next();
				}
				else
				{
					t.lexeme=c+" ";
					t.lineNumber=f.getLine();
					t.id=Constants.ERROR;
					t.number=0;
					f.next();
				}
				break;
			case '<':
				if (f.getPeek()=='=')
				{
					t.lexeme=Character.toString(c)+f.getPeek();
					t.lineNumber=f.getLine();
					t.id=Constants.LEQ;
					t.number=0;
					f.next();
					f.next();
				}
				else
				{
					t.lexeme=Character.toString(c);
					t.lineNumber=f.getLine();
					t.id=Constants.LS;
					t.number=0;
					f.next();
				}
				break;
			case '>':
				if (f.getPeek()=='=')
				{
					t.lexeme=Character.toString(c)+f.getPeek();
					t.lineNumber=f.getLine();
					t.id=Constants.GEQ;
					t.number=0;
					f.next();
					f.next();
				}
				else
				{
					t.lexeme=Character.toString(c);
					t.lineNumber=f.getLine();
					t.id=Constants.GT;
					t.number=0;
					f.next();
				}
				break;
			case (char) -1:
			{
				t.lexeme="EOF";
				t.lineNumber=f.getLine();
				t.id=Constants.EOF;
				t.number=0;
				f.next();
				break;
			}
			default: 
				t.lexeme=c+" ";
				t.lineNumber=f.getLine();
				t.id=Constants.ERROR;
				t.number=0;
				f.next();
				break;
			}
		}
		return t;
	}
	public static void main( String [] args ) throws IOException
	{
		Scaner x=new Scaner("test");
		Token t;
		t=x.next();
		
			while (t.id !=Constants.EOF)
			{
				
				if (t.id == Constants.ERROR) break;
				System.out.println(t.toString());
				
				t=x.next();
				
			}
		System.out.println(t.toString());
		
		
	}

}
