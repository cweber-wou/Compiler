public class Token {
	public int id;
	public String lexeme;
	public int lineNumber;
	public int number;

	public String toString() {
		String out = new String();
		String id = new String();
		id = Constants.getSting(this.id);
		out = id + " " + this.lexeme + " " + this.number + " "
				+ this.lineNumber;
		return out;
	}
}
