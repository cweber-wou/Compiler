import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
/*
 * Name: Chris Weber Date: fall2012 Class: compiler Design
 */

public class SymbolTable {
	public HashMap<String, Symbol> table = new HashMap<String, Symbol>();
	private int rename=0;
	public Symbol store (Symbol s)
	{
		if (s.rename==null) 
		{
			String out = new String();
			rename++;
			out="tempVar"+String.format("%06d",rename);
			s.rename=out;
		}
		table.put(s.rename, s);
		
		return table.get(s.rename);
	}
	public Symbol get (Symbol find)
	{
			String f, c;
			f=find.ID;
			Iterator<Entry<String, Symbol>> x = table.entrySet().iterator();
			while (x.hasNext())
			{
				Entry<String, Symbol> y = x.next();
				c=y.getValue().ID;
				if (c.compareTo(f)==0) return y.getValue();
				
			}
			return null;
		
	}
	public Symbol get (String key)
	{
		if (table.containsKey(key))
		{
			return table.get(key);
		}
		else 
		{
			return null;
		}
	}
	
	
}
