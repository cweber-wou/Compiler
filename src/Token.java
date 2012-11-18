public class Token {
	/*
	 * Name: Chris Weber Date: fall2012 Class: compiler Design
	 */
	public int id;
	public String lexeme;
	public int lineNumber;
	public int number;

	public String toString() {
		String out = new String();
		String id = new String();
		id = Constants.getSting(this.id);
		//out = id + " " + this.lexeme + " " + this.number + " "+ this.lineNumber;
		out=this.lineNumber+": Lexeme: "+this.lexeme+" -> NodeType: "+id;
		if (this.id == Constants.NUMBER) out+=" : "+this.number;
		return out;
	}
}
