/*
 * Author: broegb
 * Created: Friday, August 01, 2003 12:18:39 PM
 * Modified: Friday, August 01, 2003 12:18:39 PM
 */

/*
 * Checker implimentation by Chris Weber 11-23-2012
 */
import java.io.*;
import java.util.Scanner;

public class CodeGen {
	private static int labelNumber;
	private TreeNode root;
	private PrintWriter disk;
	private SmallTable parameters;
	private SmallTable localVariables;
	private SmallTable localArrays;
	private SmallTable globals;


	
	public CodeGen( TreeNode root, File outFile ) {
		this.root = root;
		
		labelNumber = 0;
		try {
			disk = new PrintWriter( new FileOutputStream( outFile ));
		}
		catch (FileNotFoundException e ) {System.out.println("Error in opening output file" );}
		
		parameters = new SmallTable();
		localVariables = new SmallTable();
		localArrays = new SmallTable();
		globals = new SmallTable();
		
	}
	
	private String nextLabel() {
		String label = "L" + labelNumber;
		labelNumber++;
		return label;
	}
	
	private void emitCode( String code ) {
		disk.println( code );
	}
	
	public void closeFile() {
		disk.close();
	}
	
	private void parameters( TreeNode N ) {
		parameters.reset();
		TreeNode t;
		
		if( N.nodeType == Constants.FUNCTION ) {
			//Find parameters
			t = N.C1;
			while( t != null ) {
				if( (t.nodeType == Constants.VARIABLE) || (t.nodeType == Constants.ARRAY) ) {
					parameters.insert( t );
					
				}
				t = t.sibling;
			}
		}
	}
	
	private void localVars( TreeNode N ) {
		if( N != null ) {
			if( (N.nodeType == Constants.VARIABLE) || (N.nodeType == Constants.ARRAY) ) {
				int i = parameters.lookUp( N.rename );
				int j = globals.lookUp( N.rename );
				if( (i < 0 ) && (j < 0) )
					if( N.nodeType == Constants.VARIABLE )
						localVariables.insert( N );
					else
						localArrays.insert( N );
			}
			
			localVars( N.sibling );
			localVars( N.C1 );
			localVars( N.C2 );
			localVars( N.C3 );
		}
	}
	
	
	
	public void processFunction( TreeNode N ) {
		if( (N != null ) && (!N.visited) ) {
			N.visited = true;
			switch( N.nodeType ) {
				
			case Constants.ASSIGN:
				genCode( N.C2 );
				
				if( N.C1.nodeType == Constants.VARIABLE ) {
					int i = parameters.lookUp( N.C1.rename );
					if( i >= 0 ) {
						int n = parameters.numberOfEntries();
						emitCode( "\t\t popl 0, " + ( n - i + 1 ) );
					}
					else {
						 i = localVariables.lookUp( N.C1.rename );
						if( i >= 0 )
							emitCode( "\t\t popl 0, " + -( i + 1 ));
						else {
							i = globals.lookUp( N.C1.rename );
							if( i >= 0 )
								emitCode( "\t\t popc " + N.C1.rename );
						}
					}
				}
				else if( N.C1.nodeType == Constants.ARRAY ) {
					genCode( N.C1.C1 );
					int i = parameters.lookUp( N.C1.rename );
					int j = localArrays.lookUp( N.C1.rename );
					int k = globals.lookUp( N.C1.rename );
					if( j >= 0 ) {
						emitCode( "\t\t push " + N.C1.rename );
						emitCode( "\t\t add" );
						emitCode( "\t\t neg" );
						emitCode( "\t\t popi " ); 
					}
					else if ( i >= 0 ) {
						emitCode( "\t\t pushi " );
					}
					else if( k >= 0 ) {
						emitCode( "\t\t neg" );
						emitCode( "\t\t push " + N.C1.rename );
						emitCode( "\t\t add" );
						emitCode( "\t\t pop" );
					}
				}
				break;
				
			case Constants.CALL:
	
				break;
				
			case Constants.IF:
				String L1 = nextLabel();
				String L2 = nextLabel();
				
				genCode( N.C1 );
				emitCode( "\t\t breql " + L1 );
				genCode( N.C2 );
				emitCode( "\t\t branch " + L2 );
				emitCode( L1 + ":" );
				genCode( N.C3 );
				emitCode( L2 + ":" );
				break;
				
			case Constants.WHILE: 
				L1 = nextLabel();
				L2 = nextLabel();
				
				emitCode( L1 + ":" );
				genCode( N.C1 );
				emitCode( "\t\t breql " + L2 );
				genCode( N.C2 );
				emitCode( "\t\t branch " + L1 );
				emitCode( L2 + ":" );
				break;
				
			case Constants.PLUS:
				genCode( N.C1 );
				genCode( N.C2 );
				emitCode( "\t\t add" );
				break;
				
			case Constants.MINUS:
				genCode( N.C1 );
				genCode( N.C2 );
				emitCode( "\t\t sub" );
				break;
				
			case Constants.MULT:
				genCode( N.C1 );
				genCode( N.C2 );
				emitCode( "\t\t mul" );
				break;
				
			case Constants.DIV:
				genCode( N.C1 );
				genCode( N.C2 );
				emitCode( "\t\t div" );
				break;
				
			case Constants.VARIABLE:
		
				break;
				
			case Constants.NUMBER:
		
				break;
				
			case Constants.WRITE:
				genCode( N.C1 );
				emitCode( "\t\t wrint " );
				emitCode( "\t\t pushc 10 \n\t\t pushc 13 \n\t\t wrchar \n\t\t wrchar" );
				break;
				
			case Constants.READ:
				emitCode( "\t\t rdint" );
				
				if( N.C1.nodeType == Constants.VARIABLE ) {
					int i = parameters.lookUp( N.C1.rename );
					if( i >= 0 ) {
						int n = parameters.numberOfEntries();
						emitCode( "\t\t popl 0, " + ( n - i) );
					}
					else {
						i = localVariables.lookUp( N.C1.rename );
						if( i >= 0 )
							emitCode( "\t\t popl 0, " + -( i + 1 ));
						else {
							i = globals.lookUp( N.C1.rename );
							if( i >= 0 )
								emitCode( "\t\t popc " + N.C1.rename );
						}
					}
				}
				else if( N.C1.nodeType == Constants.ARRAY ) {
					genCode( N.C1.C1 );
					int i = parameters.lookUp( N.C1.rename );
					int j = localArrays.lookUp( N.C1.rename );
					int k = globals.lookUp( N.C1.rename );
					if( j >= 0 ) {
						emitCode( "\t\t push " + N.C1.rename );
						emitCode( "\t\t add" );
						emitCode( "\t\t neg" );
						emitCode( "\t\t popi " ); 
					}
					else if ( i >= 0 ) {
						emitCode( "\t\t pushi " );
					}
					else if( k >= 0 ) {
						emitCode( "\t\t neg" );
						emitCode( "\t\t push " + N.C1.rename );
						emitCode( "\t\t add" );
						emitCode( "\t\t pop" );
					}
				}
				break;
				
			case Constants.COMPOUND:
				N.visited = false;
				genCode( N );
				break;
				
			case Constants.STATEMENT_LIST:
				TreeNode t = N.sibling;
				while( t != null ) {
					genCode( t );
					t = t.sibling;
				}
				break;
			}
			
			if( N.nodeType != Constants.FUNCTION )
				processFunction( N.sibling );
			
			processFunction( N.C1 );
			processFunction( N.C2 );
			processFunction( N.C3 );
		}
		
	}
	
	private void globalVars( TreeNode N ) {
		String [] vars = new String[ 50 ];
		int counter = -1;
		globals.reset();
		TreeNode t = N;
		
		while( t != null ) {
			if( t.nodeType == Constants.VARIABLE ) {
				counter++;
				vars[ counter ] = t.rename + ":\t\t0" ;
				globals.insert( t );
				t.visited = true;
			}
			else if( t.nodeType == Constants.ARRAY ) {
				counter++;
				vars[ counter ] = "\t\t .BLOCK " + (t.nValue - 1) + "\n" + t.rename + ":\t 0 ";
				globals.insert( t );
				t.visited = true;
			}
			
			t = t.sibling;
		}
		
		if( counter > -1 ) {
			emitCode( "; Global Variables " );
			for( int i = 0; i <= counter; i++ )
				emitCode( vars[ i ] );
		}
	}
	
	public void genCode( TreeNode N ) {
		TreeNode t;
		
		if( (N != null) && (!N.visited) ) {
			N.visited = true;
			
			switch( N.nodeType ) {
				
			case Constants.PROGRAM:
				emitCode( "\t call main, 0\t; begin program\n" );
				emitCode( "\t halt\n" );
				globalVars( N );
				t = N.sibling;
				while( t != null ) {
					genCode( t );
					t = t.sibling;
				}
				break;
				
			case Constants.DECLARATION:
				
				break;
				
			case Constants.FUNCTION:
				emitCode("\n;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;" );
				emitCode( N.sValue + ":\t\t ;function " + N.sValue );
				parameters ( N );
				localVariables.reset();
				localArrays.reset();
				localVars( N.C2 );
				emitCode( "\n\t\t ;local variables " );

				localVariables.emit( disk );
				localArrays.emit( disk );
			
				emitCode( "\t\t ;function body\n" );
				
				N.visited = false;
				processFunction( N );
				
				if( N.typeSpecifier == Constants.VOID )
					emitCode( "\t\t return " + parameters.numberOfEntries() );

				emitCode("\n;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;\n" );
				
				int offset = localVariables.numberOfEntries() + 1;
				int count = localArrays.numberOfEntries();
				for( int i = 0; i < count; i++ ) {
					t = localArrays.entry( i );
					emitCode( t.rename + ": \t" + offset );
					offset += t.nValue;
				}
				
				emitCode("\n;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;" );
				emitCode( "\t\t nop" );
				break;
				
			case Constants.RETURN:
				genCode( N.C1 );
				emitCode( "\t\t freturn " + parameters.numberOfEntries() );
				break;
				
			case Constants.CALL:
				genCode( N.C1 );
				emitCode( "\t\t call " + N.sValue + ", 0" );
				break;
				
			case Constants.ARGUMENTS:
				t = N.sibling;
				while( t != null ) {
					if( t.nodeType != Constants.ARRAY ) {
						t.visited = false;
						genCode( t );
					}
					else if( t.C1 != null ) {
						t.visited = false;
						genCode( t );
					}
					else {
						int i = parameters.lookUp( t.rename );
						int j = localArrays.lookUp( t.rename );
						int k = globals.lookUp( t.rename );
						
						if( j >= 0 ) {
							emitCode( "\t\t pushl -1, 0" );
							emitCode( "\t\t push " + t.rename );
							emitCode( "\t\t sub " );
						}
						if( i >= 0 ) {
							int n = parameters.numberOfEntries();
							emitCode( "\t\t pushl 0, " + ( n - i ) );
						}
						if( k >= 0 ) {
							emitCode( "\t\t pushc " + t.rename );
						}
					}
					t = t.sibling;
				}
				
				break;
				
			case Constants.PARAMETER_LIST:
				
				break;
				
			case Constants.COMPOUND:
				t = N.C2.sibling;
				while( t != null ) {
					genCode( t );
					t = t.sibling;
				}
				
				break;
				
			case Constants.STATEMENT_LIST:
				t = N.sibling;
				while( t != null ) {
					genCode( t );
					t = t.sibling;
				}
				break;
				
			case Constants.IF:
				String L1 = nextLabel();
				String L2 = nextLabel();
				
				genCode( N.C1 );
				emitCode( "\t\t breql " + L1 );
				genCode( N.C2 );
				emitCode( "\t\t branch " + L2 );
				emitCode( L1 + ":" );
				genCode( N.C3 );
				emitCode( L2 + ":" );
				break;
				
			case Constants.WHILE: 
				L1 = nextLabel();
				L2 = nextLabel();
				
				emitCode( L1 + ":" );
				genCode( N.C1 );
				emitCode( "\t\t breql " + L2 );
				genCode( N.C2 );
				emitCode( "\t\t branch " + L1 );
				emitCode( L2 + ":" );
				break;
				
			case Constants.ASSIGN:
				genCode( N.C2 );
				
				if( N.C1.nodeType == Constants.VARIABLE ) {
					int i = parameters.lookUp( N.C1.rename );
					if( i >= 0 ) {
						int n = parameters.numberOfEntries();
						emitCode( "\t\t popl 0, " + ( n - i ) );
					}
					else {
						i = localVariables.lookUp( N.C1.rename );
						if( i >= 0 )
							emitCode( "\t\t popl 0, " + -( i + 1 ));
						else {
							i = globals.lookUp( N.C1.rename );
							if( i >= 0 )
								emitCode( "\t\t popc " + N.C1.rename );
						}
					}
				}
				else if( N.C1.nodeType == Constants.ARRAY ) {
					genCode( N.C1.C1 );
					
					int i = parameters.lookUp( N.C1.rename );
					int j = localArrays.lookUp( N.C1.rename );
					int k = globals.lookUp( N.C1.rename );
					
					if( j >= 0 ) {
						emitCode( "\t\t push " + N.C1.rename );
						emitCode( "\t\t add" );
						emitCode( "\t\t neg" );
						emitCode( "\t\t popi " ); 
					}
					else if ( i >= 0 ) {
						int n = parameters.numberOfEntries();
						emitCode( "\t\t pushl 0, " + ( n - i ) );
						emitCode( "\t\t sub " );
						emitCode( "\t\t neg " );
						emitCode( "\t\t pop " );
					}
					else if( k >= 0 ) {
						emitCode( "\t\t neg" );
						emitCode( "\t\t pushc " + N.C1.rename );
						emitCode( "\t\t add" );
						emitCode( "\t\t pop" );
					}
				}
				break;
				
			case Constants.LS:
				genCode( N.C2 );
				genCode( N.C1 );
				emitCode( "\t\t less" );
				break;
				
			case Constants.LEQ:
				genCode( N.C2 );
				genCode( N.C1 );
				emitCode( "\t\t lsseql" );
				break;
				
			case Constants.GT:
				genCode( N.C2 );
				genCode( N.C1 );
				emitCode( "\t\t greater" );
				break;
				
			case Constants.GEQ:
				genCode( N.C2 );
				genCode( N.C1 );
				emitCode( "\t\t gtreql" );
				break;
				
			case Constants.EQ:
				genCode( N.C2 );
				genCode( N.C1 );
				emitCode( "\t\t equal" );
				break;
				
			case Constants.NEQ:
				genCode( N.C2 );
				genCode( N.C1 );
				emitCode( "\t\t noteql" );
				break;
				
			case Constants.PLUS:
				genCode( N.C1 );
				genCode( N.C2 );
				emitCode( "\t\t add" );
				break;

				
			case Constants.MINUS:
				genCode( N.C1 );
				genCode( N.C2 );
				emitCode( "\t\t sub" );
				break;
				
			case Constants.MULT:
				genCode( N.C1 );
				genCode( N.C2 );
				emitCode( "\t\t mul" );
				break;
				
			case Constants.DIV:
				genCode( N.C1 );
				genCode( N.C2 );
				emitCode( "\t\t div" );
				break;
				
			case Constants.VARIABLE:
				int i = parameters.lookUp( N.rename );
				int j = localVariables.lookUp( N.rename );
				int k = globals.lookUp( N.rename );
				
				if( i >= 0 ) {
					int n = parameters.numberOfEntries();
					emitCode( "\t\t pushl 0, " + (n - i) );
				}
				else if( j >= 0 )
					emitCode( "\t\t pushl 0, " + -(j + 1) );
				else if( k >= 0 )
					emitCode( "\t\t push " + N.rename );
				
				break;
				
			case Constants.NUMBER:
				emitCode( "\t\t pushc " + N.nValue );
				break;
				
			case Constants.ARRAY:
				//if array is global
				i = globals.lookUp( N.rename );
				if( i >= 0 ) {
					genCode( N.C1 );
					emitCode( "\t\t neg" );
					emitCode( "\t\t pushc " + N.rename );
					emitCode( "\t\t add" );
					emitCode( "\t\t contents" );
					break;
				}
				
				i = parameters.lookUp( N.rename );
				if( i < 0 ) {
					genCode( N.C1 );
					emitCode( "\t\t push " + N.rename );
					emitCode( "\t\t add" );
					emitCode( "\t\t neg" );
					emitCode( "\t\t pushi " );
				}
				else {
					int n = parameters.numberOfEntries();
					emitCode( "\t\t pushl 0, " + (n - i) );
					genCode( N.C1 );
					emitCode( "\t\t sub" );
					emitCode( "\t\t contents" );
				}
				
				break;
				
			case Constants.WRITE:
				genCode( N.C1 );
				emitCode( "\t\t wrint " );
				emitCode( "\t\t pushc 10 \n\t\t pushc 13 \n\t\t wrchar \n\t\t wrchar" );
				break;
				
			case Constants.READ:
				emitCode( "\t\t rdint" );
				
				if( N.C1.nodeType == Constants.VARIABLE ) {
					i = parameters.lookUp( N.C1.rename );
					if( i >= 0 ) {
						emitCode( "\t\t popl 0, " + ( i + 1 ) );
					}
					else {
						i = localVariables.lookUp( N.C1.rename );
						if( i >= 0 )
							emitCode( "\t\t popl 0, " + -( i + 1 ));
						else {
							i = globals.lookUp( N.C1.rename );
							if( i >= 0 )
								emitCode( "\t\t popc " + N.C1.rename );
						}
					}
				}
				else if( N.C1.nodeType == Constants.ARRAY ) {
					genCode( N.C1.C1 );
					
					 i = parameters.lookUp( N.C1.rename );
					 j = localArrays.lookUp( N.C1.rename );
					 k = globals.lookUp( N.C1.rename );
						
					if( j >= 0 ) {
						emitCode( "\t\t push " + N.C1.rename );
						emitCode( "\t\t add" );
						emitCode( "\t\t neg" );
						emitCode( "\t\t popi " ); 
					}
					else if ( i >= 0 ) {
						emitCode( "\t\t pushi " );
					}
					else if( k >= 0 ) {
						emitCode( "\t\t neg" );
						emitCode( "\t\t pushc " + N.C1.rename );
						emitCode( "\t\t add" );
						emitCode( "\t\t pop" );
					}
				}
				
				break;
				
			default:
			}
		}
	}
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Input File Name for parser: ");
		String infile = in.nextLine();
		Checker c = new Checker(infile);
		 try {
		c.pass1(c.p.root);
		c.pass4(c.p.root);
		c.pass6(c.p.root);
		c.level = 0;
		c.pass7(c.p.root);
		c.pass8(c.p.root);
		Navi n = new Navi();
		n.preorder(c.p.root);
		 } catch (IllegalStateException e) {
		 System.out.println(e.getMessage());
		 }
		 File f=new File(infile+".bin");
		 CodeGen x = new CodeGen(c.p.root, f);
		 x.genCode(x.root);
		 x.closeFile();
	}
}
