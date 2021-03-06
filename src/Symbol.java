public class Symbol {
	/*
	 * Name: Chris Weber Date: fall2012 Class: compiler Design
	 */
	public String ID;
	public int entryType;
	public int dataType;
	public int blockLevel;
	public TreeNode pramList;
	public int returnType;
	public int arrayMax;
	public String rename;
	public int line;
	public int value;

	public String toString()
	{
		String out=new String();
		out+=" ID: "+ID;
		//out+=" Line: "+line;
		out+=" Entry type: "+Constants.getSting(entryType);
		//out+=" Dtype: "+Constants.getSting(dataType);
		out+=" Level: "+blockLevel;
		out+=" Renam: "+rename;
		
		if (entryType==Constants.FUNCTION)
		{ 
			if(pramList.sibling!=null)out+=" PList != null";
			else out+=" PList = null";
			out+=" RType: "+Constants.getSting(returnType);
		}
		if (entryType==Constants.ARRAY)
		 out+=" ArrayMax: "+arrayMax;
		
		
		return out;
	}
	
	public Symbol(TreeNode t) {
		ID = t.sValue;
		entryType = t.nodeType;
		dataType = t.typeSpecifier;
		blockLevel = t.level;
		line=t.lineNumber;
		rename=t.rename;
		switch (t.nodeType) {
		case Constants.FUNCTION:
			rename=t.sValue;
			pramList = t.C1;
			returnType = t.typeSpecifier;
			break;
		case Constants.ARRAY:
			arrayMax = t.nValue;
			break;
		}

	}
}
