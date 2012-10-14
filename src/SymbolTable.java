import java.util.HashMap;


public class SymbolTable {
	
	private HashMap<String, Integer> table = new HashMap<String, Integer>();
	
	private void keywords()
	{
		table.put("if",Constants.IF);
		table.put("else",Constants.ELSE);
		table.put("int",Constants.INT);
		table.put("return",Constants.RETURN);
		table.put("void",Constants.VOID);
		table.put("while",Constants.WHILE);
		table.put("read",Constants.READ);
		table.put("write",Constants.WRITE);
				
	}
	
	public int pull (String in)
	{
		
		return table.get(in);
	}
	
	public boolean exists (String in)
	{
		
		return table.containsKey(in);
	}
	
	public int push (String key, int value)
	{	// output -1 if new value added to table otherwise output previous value;
		Integer x = table.put(key,value);
		if (x==null)
			return -1;
		else return x.intValue();
		
	}
	
	public SymbolTable()
	{
		keywords();
	}
	
	
	public static void main(String[] args) {
		SymbolTable x = new SymbolTable();
		System.out.println(x.pull("if"));
		System.out.println(x.pull("else"));
		System.out.println(x.pull("write"));
		System.out.println(x.pull("read"));
		System.out.println(x.pull("while"));
		System.out.println(x.pull("return"));
		System.out.println(x.push("x",2000));
		System.out.println(x.push("y",3000));
		System.out.println(x.push("z",4000));
		System.out.println(x.pull("x"));
		System.out.println(x.pull("y"));
		System.out.println(x.pull("z"));
		System.out.println(x.push("z",4500));
		System.out.println(x.push("z",4700));
		System.out.println(x.pull("z"));
		System.out.println(x.pull("q"));
		
	}

}
