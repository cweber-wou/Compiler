import java.util.Collection;
import java.util.Iterator;
/*
 * Name: Chris Weber Date: fall2012 Class: compiler Design
 */
public class Checker {

	public Parser p;
	public Walker w;
	public int count;
	public int level = 0;
	public int debug = 1;
	public SymbolTable st = new SymbolTable();

	// Varibles arrays and functions declared before use
	public void pass1(TreeNode t) {
		Symbol s;
		TreeNode x;
		switch (t.nodeType) {
		case Constants.FUNCTION:
			// add function def
			s = new Symbol(t);
			s = st.store(s);
			if (debug == 1)
				System.out.println("Added Function: " + s.toString());
			if (t.C1.typeSpecifier != Constants.VOID) // add parameter list
			{
				x = t.C1.sibling;

				if (x == null)
					System.out.println("Missing Parameter List");
				while (x != null) {
					s = new Symbol(x);
					s = st.store(s);
					if (debug == 1)
						System.out.println("Added to ParameterList: "
								+ s.toString());
					x = x.sibling;
				}

			}
			level++;
			if (t.C2 != null)
				pass1(t.C2);
			else
				System.out
						.println("Error Missing Compound Statement in function");
			level--;
			if (t.sibling != null)
				pass1(t.sibling);
			break;

		case Constants.COMPOUND:
			x = t.C1.sibling;
			// if (x == null)
			// System.out.println("Missing Compount Statement");
			while (x != null) {
				s = new Symbol(x);
				s = st.store(s);
				if (debug == 1)
					System.out.println("Compound Define: " + s.toString());
				x = x.sibling;
			}
			level++;
			if (t.C2 != null)
				pass1(t.C2);
			else
				System.out.println("Missing Statement List in Compound");
			level--;
			if (t.sibling != null)
				pass1(t.sibling);
			break;
		case Constants.CALL: s=st.get(t.sValue); if (s==null) System.out.println("Undefined Function Call: "+t.toString()); else if (debug==1) System.out.println("Function Lookup"+s.toString());// Look up function
		case Constants.ARRAY: // then add else lookup
		case Constants.VARIABLE:
			if (level == 0) {
				s = new Symbol(t);
				s = st.store(s);
				if (debug == 1)
					System.out.println("Global Var Define " + s.toString());
			} else {
				Symbol f=new Symbol(t); 
				
				s= st.get(f);
				if (s==null) System.out.println("Varible not found: "+t.toString()); else System.out.println("Lookup Sucess "+s.toString());
			}

		default:
			
			if (t.C1 != null)
				pass1(t.C1);
			if (t.C2 != null)
				pass1(t.C2);
			if (t.C3 != null)
				pass1(t.C3);
			if (t.sibling != null)
				pass1(t.sibling);
			// pass1 subnodes
		}

	}

	// Make sure Variables arrays and functions are not declared twice in the
	// same block
	public void pass2() {

	}

	// Variables, Arrays and functions can not have the same name in the same
	// block
	public void pass3() {

	}

	// Void function can not have a return statement
	public void pass4() {

	}

	// int function must have a return type
	public void pass5() {

	}

	// void function can not be used in a expression
	public void pass6() {

	}

	// Array that is used in the same block must remain in bounds
	public void pass7() {

	}

	// An Array when used with an expression must have a location and a Variable
	// cannot be used as an array
	public void pass8() {

	}

	public Checker(String fName) {
		p = new Parser(fName);
		p.start();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Checker c = new Checker("test");

		c.pass1(c.p.root);

	}

}
