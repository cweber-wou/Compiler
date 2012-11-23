import java.util.Scanner;

/*
 * Name: Chris Weber Date: fall2012 Class: compiler Design
 */
public class Checker {

	public Parser p;
	public Walker w;
	public int count;
	public int level = 0;
	public int debug = 1;// Turns on output for pass number 0=none
	public SymbolTable st = new SymbolTable();

	// 1: Varibles arrays and functions declared before use
	// 2:Make sure Variables arrays and functions are not declared twice in the
	// same block
	// 3: Variables, Arrays and functions can not have the same name in the same
	// block
	public void pass1(TreeNode t) {
		Symbol s;
		TreeNode x;
		switch (t.nodeType) {
		case Constants.FUNCTION:
			// add function def
			s = new Symbol(t);
			Symbol f = st.get(s);
			if (f != null && f.blockLevel == s.blockLevel) {
				throw new IllegalStateException(
						"Tried to use same name twice in function "
								+ s.toString() + "\n -> " + f.toString());
			} else {
				s = st.store(s);
				t.rename = s.rename;
				if (debug == 1)
					System.out.println("Added Function: " + s.toString());
			}
			if (t.C1.typeSpecifier != Constants.VOID) // add parameter list
			{
				x = t.C1.sibling;

				if (x == null)
					throw new IllegalStateException("Missing Parameter List");
				while (x != null) {
					s = new Symbol(x);
					s = st.store(s);
					if (debug == 1)
						System.out.println("Added to ParameterList: "
								+ s.toString());
					x.rename = s.rename;
					x = x.sibling;
				}

			}
			level++;
			if (t.C2 != null)
				pass1(t.C2);
			else
				throw new IllegalStateException(
						"Error Missing Compound Statement in function");
			count = st.Remove(level);
			if (debug == 1)
				System.out.println("Function Level: " + level
						+ " Number of items Removed from table " + count);
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
				f = st.get(s);
				if (f != null && f.blockLevel == s.blockLevel) {
					throw new IllegalStateException(
							"Tried to same name twice in Compound Statement"
									+ s.toString() + "\n -> " + f.toString());
				}
				s = st.store(s);
				x.rename = s.rename;
				if (debug == 1)
					System.out.println("Compound Define: " + s.toString());
				x = x.sibling;
			}
			level++;
			if (t.C2 != null)
				pass1(t.C2);
			else
				throw new IllegalStateException(
						"Missing Statement List in Compound");
			count = st.Remove(level);
			if (debug == 1)
				System.out.println("Compound Level " + level
						+ " Number of items Removed from table " + count);
			level--;
			if (t.sibling != null)
				pass1(t.sibling);
			break;
		case Constants.CALL:
			s = st.get(t.sValue);
			if (s == null)
				throw new IllegalStateException("Undefined Function Call: "
						+ t.toString());
//			 else if (debug == 1)
//			 System.out.println("Function Lookup" + s.toString());// Look up
			// function
			if (t.C1.typeSpecifier == Constants.INT)
				pass1(t.C1.sibling);
			if (t.sibling != null)
				pass1(t.sibling);
			break;
		case Constants.ARRAY: // then add else lookup
		case Constants.VARIABLE:
			if (level == 0) {
				
				s = new Symbol(t);
				f = st.get(s);
				if (f != null && f.blockLevel == s.blockLevel) {
					throw new IllegalStateException(
							"Tried to same name twice with a Global Variable  "
									+ s.toString() + "\n -> " + f.toString());
				}
				s = st.store(s);
				t.rename = s.rename;
				if (debug == 1)
					System.out.println("Global Var Define " + s.toString());
			} else {
				f = new Symbol(t);

				s = st.get(f);
				if (s == null)
					throw new IllegalStateException("Varible not found: "
							+ t.toString());
				else {
//					 if (debug == 1) {
//					 System.out.println("Lookup Sucess " + s.toString());
//					 }
					
					t.rename = s.rename;
					t.nodeType = s.entryType;
					t.typeSpecifier = s.dataType;
					if (t.nodeType == Constants.ARRAY)
						t.nValue = s.arrayMax;
					else
						t.nValue = s.value;
					if (t.C1!=null && t.nodeType==Constants.VARIABLE) throw new IllegalStateException(
							"Trying to Derefrence a Varible instead of a array  "+t.toString());
				}
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

	// 4:Void function can not have a return statement
	// 5:int function must have a return type
	public void pass4(TreeNode t) {
		if (t.typeSpecifier == Constants.VOID && hasReturn(t.C2))
			throw new IllegalStateException("Void Function has Return "
					+ t.toString());
		if (t.typeSpecifier == Constants.INT && !hasReturn(t.C2))
			throw new IllegalStateException("Int Function has No Return "
					+ t.toString());

		while (t.sibling != null) {
			t = t.sibling;
			if (t.nodeType == Constants.FUNCTION) {
				if (t.typeSpecifier == Constants.VOID && hasReturn(t.C2))
					throw new IllegalStateException("Void Function has Return "
							+ t.toString());
				if (t.typeSpecifier == Constants.INT && !hasReturn(t.C2))
					throw new IllegalStateException(
							"Int Function has No Return " + t.toString());
			}
		}
	}

	private boolean hasReturn(TreeNode t) {
		if (t == null)
			return false;
		if (t.nodeType == Constants.RETURN)
			return true;

		return hasReturn(t.C3) || hasReturn(t.C2) || hasReturn(t.C1)
				|| hasReturn(t.sibling);
	}

	// 6: void function can not be used in a expression
	public void pass6(TreeNode t) {

		switch (t.nodeType) {
		case Constants.IF:
		// things that contain expresions only in c1
		case Constants.CALL:
		case Constants.ARRAY:
		case Constants.WHILE:
		case Constants.WRITE:
			if (hasVoid(t.C1))
				throw new IllegalStateException(
						"Void Function used in expression " + t.toString());
			break;
		case Constants.EQ:
		case Constants.ASSIGN:// things that have expresions in c1 and c2
		case Constants.LEQ:
		case Constants.LS:
		case Constants.GEQ:
		case Constants.GT:
		case Constants.NEQ:
		case Constants.MULT:
		case Constants.DIV:
		case Constants.PLUS:

		case Constants.MINUS:
			if (hasVoid(t.C1) || hasVoid(t.C2))
				throw new IllegalStateException(
						"Void Function used in expression " + t.toString());
			break;
		case Constants.ARGUMENTS: // siblings are expresions
			TreeNode x = t.sibling;
			while (x != null) {
				if (hasVoid(x))
					throw new IllegalStateException(
							"Void Function used in expression " + t.toString());
				x = x.sibling;
			}
			break;
		}
		if (t.C1 != null)
			pass6(t.C1);
		if (t.C2 != null)
			pass6(t.C2);
		if (t.C3 != null)
			pass6(t.C3);
		if (t.sibling != null)
			pass6(t.sibling);

	}

	private boolean hasVoid(TreeNode t) {
		if (t == null)
			return false;
		if (t.nodeType == Constants.CALL) {
			Symbol s = st.get(t.sValue);
			if (s.returnType == Constants.VOID)
				return true;
		}

		return hasVoid(t.C3) || hasVoid(t.C2) || hasVoid(t.C1);
	}

	// 7: Array that is used in the same block must remain in bounds
	public void pass7(TreeNode t) {
		Symbol s, f;
		TreeNode x;
		switch (t.nodeType) {
		case Constants.FUNCTION:

			if (t.C1.typeSpecifier != Constants.VOID) // add parameter list
			{
				x = t.C1.sibling;

				if (x == null)
					throw new IllegalStateException("Missing Parameter List");
				while (x != null) {
					s = new Symbol(x);
					s = st.store(s);
					if (debug == 7)
						System.out.println("Added to ParameterList: "
								+ s.toString());
					x.rename = s.rename;
					x = x.sibling;
				}

			}
			level++;
			if (t.C2 != null)
				pass7(t.C2);
			else
				throw new IllegalStateException(
						"Error Missing Compound Statement in function");
			count = st.Remove(level);
			if (debug == 7)
				System.out.println("Function Level: " + level
						+ " Number of items Removed from table " + count);
			level--;
			if (t.sibling != null)
				pass7(t.sibling);
			break;

		case Constants.COMPOUND:
			x = t.C1.sibling;
			while (x != null) {
				s = new Symbol(x);
				f = st.get(s);
				if (f != null && f.blockLevel == s.blockLevel) {
					throw new IllegalStateException(
							"Tried to same name twice in Compound Statement"
									+ s.toString() + "\n -> " + f.toString());
				}
				s = st.store(s);
				x.rename = s.rename;
				if (debug == 7)
					System.out.println("Compound Define: " + s.toString());
				x = x.sibling;
			}
			level++;
			if (t.C2 != null)
				pass7(t.C2);
			else
				throw new IllegalStateException(
						"Missing Statement List in Compound");
			count = st.Remove(level);
			if (debug == 7)
				System.out.println("Compound Level " + level
						+ " Number of items Removed from table " + count);
			level--;
			if (t.sibling != null)
				pass7(t.sibling);
			break;
		case Constants.CALL:
			s = st.get(t.sValue);
			if (s == null)
				throw new IllegalStateException("Undefined Function Call: "
						+ t.toString());
			if (t.sibling != null)
				pass7(t.sibling);
			break;
		case Constants.ASSIGN:
			pass7(t.C1);
			switch (t.C2.nodeType) {
			case Constants.NUMBER:
			case Constants.PLUS:
			case Constants.MINUS:
			case Constants.MULT:
			case Constants.DIV:
				int b;
				try {
					b = eval(t.C2);

					Symbol find = new Symbol(t.C1);
					s = st.get(find);
					s.value = b;
				} catch (ArrayCheck e) {

				}
				break;
			case Constants.ARRAY:
				pass7(t.C2);
			}
			if (t.sibling != null)
				pass7(t.sibling);

			break;
		case Constants.ARRAY: // then add else lookup
		case Constants.VARIABLE: {
			f = new Symbol(t);

			s = st.get(f);
			if (s == null)
				throw new IllegalStateException("Varible not found: "
						+ t.toString());
			else {
				if (debug == 7) {
					System.out.println("Lookup Sucess " + s.toString());
				}
				t.rename = s.rename;
				t.nodeType = s.entryType;
				if (t.nodeType == Constants.ARRAY)
					t.nValue = s.arrayMax;
				else
					t.nValue = s.value;
			}
			if (t.nodeType == Constants.ARRAY) {// not a define
				s = st.get(t.rename); // pull array from symbol table
				if (s.arrayMax != 0 && s.blockLevel == level - 1
						&& s.blockLevel > 0) {// array is not
					// a passed
					// parameter
					// and is in
					// the same
					// block
					// level
					int b;
					try {
						b = eval(t.C1);
						// What location are we trying to look for
						if (b < 0)
							throw new IllegalStateException(
									"Negitive array lookup " + t.toString());// atempting
																				// to
																				// look
																				// before
																				// start
																				// of
																				// array
						if (b > s.arrayMax-1)
							throw new IllegalStateException(
									"Lookup Past end of array" + t.toString());// attemting
																				// to
																				// look
																				// after
																				// end
																				// of
																				// array
					} catch (ArrayCheck e) {// we had something that could not
											// be evaluated

					}
				}
			}

		}

		default:

			if (t.C1 != null)
				pass7(t.C1);
			if (t.C2 != null)
				pass7(t.C2);
			if (t.C3 != null)
				pass7(t.C3);
			if (t.sibling != null)
				pass7(t.sibling);
			// pass7 subnodes
		}

	}

	// used to check array out of bounds
	public int eval(TreeNode t) throws ArrayCheck {

		switch (t.nodeType) {
		case Constants.NUMBER:
			return t.nValue;
		case Constants.VARIABLE:
			return st.get(t.rename).value;
		case Constants.PLUS:
			return eval(t.C1) + eval(t.C2);
		case Constants.MINUS:
			return eval(t.C1) - eval(t.C2);
		case Constants.MULT:
			return eval(t.C1) * eval(t.C2);
		case Constants.DIV:
			return eval(t.C1) / eval(t.C2);
		}

		throw new ArrayCheck();// If something else then return 0 so it has no
								// effect of the evaluation
	}

	// 8:An Array when used with an expression must have a location and a Variable
	// cannot be used as an array
	// 9: Must pass valid parameters into function calls
	public void pass8(TreeNode t) {
		switch (t.nodeType) {
		case Constants.ASSIGN:// things that contain expresions
		case Constants.IF:
		case Constants.ARRAY:
		case Constants.WHILE:
		case Constants.WRITE:
			if (hasNoArrayLocation(t))
				throw new IllegalStateException(
						"No Array Location in expression " + t.toString());
			if (t.C1 != null)
				pass8(t.C1);
			if (t.C2 != null&&t.C2.nodeType!=Constants.CALL)
				pass8(t.C2);
			if (t.sibling != null)
				pass8(t.sibling);
			break;
		case Constants.EQ:
		case Constants.LEQ:
		case Constants.LS:
		case Constants.GEQ:
		case Constants.GT:
		case Constants.NEQ:
		case Constants.MULT:
		case Constants.DIV:
		case Constants.PLUS:
		case Constants.MINUS:
			if (hasNoArrayLocation(t))
				throw new IllegalStateException(
						"No Array Location in expression " + t.toString());
			if (t.C1 != null)
				pass8(t.C1);
			if (t.C2 != null)
				pass8(t.C2);
			if (t.C3 != null)
				pass8(t.C3);
			if (t.sibling != null)
				pass8(t.sibling);
			break;
		case Constants.FUNCTION:
			if (t.C2 != null)
				pass8(t.C2);
			if (t.sibling != null)
				pass8(t.sibling);
			break;
		case Constants.CALL:
			if (t.C1.sibling != null) // arugments in call
			{
				TreeNode a, b;
				Symbol s = st.get(t.sValue); // Pull function def
				if (s.pramList == null)
					throw new IllegalStateException(
							"Passed parameters to a void parameter function "
									+ t.toString());
				a = s.pramList.sibling;
				b = t.C1.sibling;
				while (a != null) {
					switch (a.nodeType) {
					case Constants.ARRAY:
						if (b.nodeType != Constants.ARRAY)
							throw new IllegalStateException(
									"Invalid Parameter Function Expected Array "
											+ b.toString());
						if (b.C1 != null)
							throw new IllegalStateException(
									"Array dereferenced when Expecting Array "
											+ b.toString());
						break;
					case Constants.VARIABLE:
						switch (b.nodeType) {
						case Constants.NUMBER:
						case Constants.VARIABLE:
						case Constants.CALL:
							break; // Integer or number valid for integer
									// Function ok becuse we already checked for
									// Nulls
						case Constants.ARRAY:
							if (b.C1 == null)
								throw new IllegalStateException(
										"Array not dereferenced when Expecting int "
												+ t.toString());
						}
					}

					if (a.sibling == null ^ b.sibling == null) {
						throw new IllegalStateException(
								"Mismatched Number of Parameters in function call "
										+ t.toString());
					} else {
						b = b.sibling;
						a = a.sibling;
					}

				}
			} else if (t.sibling != null)
				pass8(t.sibling);

		default:
			if (t.C1 != null)
				pass8(t.C1);
			if (t.C2 != null)
				pass8(t.C2);
			if (t.C3 != null)
				pass8(t.C3);
			if (t.sibling != null)
				pass8(t.sibling);
		}

	}

	private boolean hasNoArrayLocation(TreeNode t) {
		if (t == null)
			return false;
		// if (t.nodeType==Constants.CALL)return false;
		if (t.nodeType == Constants.ARRAY && (t.C1 == null && t.nValue == 0))
			return true;
		return hasNoArrayLocation(t.C1) || hasNoArrayLocation(t.C2)
				|| hasNoArrayLocation(t.C3);
	}

	public Checker(String fName) {
		p = new Parser(fName);
		p.start();

	}

	/**
	 * @param args
	 */
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
	}

}
