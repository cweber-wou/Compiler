
public class TreeNode {
	public int lineNumber;
	public int nValue;
	public String sValue;
	public int nodeType;
	public int typeSpecifier;
	public String rename;
	public boolean visited;
	public int level;
	
	public TreeNode C1;
	public TreeNode C2;
	public TreeNode C3;
	public TreeNode sibling;
	
	public TreeNode()
	{
		
	}
	
	public TreeNode(Token in)
	{
		this.lineNumber=in.lineNumber;
		this.nValue=in.number;
		this.sValue=in.lexeme;
		this.nodeType=in.id;
		
	}
	public String toString()
	{
		String o = new String();
		
		o+=" Node Type: "+Constants.getSting(nodeType);
		o+=" Line: "+lineNumber;
		
		o+=" Name: "+sValue;
		if (nodeType == Constants.NUMBER|| nodeType == Constants.ARRAY) o+=" NumberValue: "+nValue;
		
		o+=" Rename: "+rename;
		return o;
		
	}
	public String toString(int level)
	{
		String o = new String();
		String t = "\n";
		for (int i =0; i<level; i++)
		{
			t=t+"\t";
		}
		o+=t+"Node Type: "+Constants.getSting(nodeType);
		o+=t+"Line: "+lineNumber;
		
		o+=t+"Name: "+sValue;
		if (nodeType == Constants.NUMBER|| nodeType == Constants.ARRAY) o+=t+"NumberValue: "+nValue;
		
		o+=t+"Rename: "+rename;
		return o;
		
	}
	public static void main(String[] args) {
	   TreeNode x = new TreeNode();
	   x.lineNumber=23;
	   x.nValue=512;
	   x.sValue="x";
	   x.level=0;
	   x.typeSpecifier=Constants.VOID;
	   x.nodeType=Constants.FUNCTION;
	   x.rename=x.sValue+"0001";
	   for (int i=0; i<10;i++)
	   {
		   x.level=i;
		   System.out.println(x.toString(i));   
	   }
	   
	   
	}
}
