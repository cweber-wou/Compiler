import java.util.LinkedList;
import java.util.ListIterator;
/*
 * Name: Chris Weber Date: fall2012 Class: compiler Design
 */
//used in checker to walk the tree uses C1,C2,C3,Sibling walk order
public class Walker {

	public TreeNode current;
	public TreeNode root;
	public ListIterator<TreeNode> l;
	public int level=0;
	private LinkedList<TreeNode> list;//use to store values in a list 
	//fills out linked list recursively
	
	public Walker (TreeNode r)
	{
		list = new LinkedList<TreeNode>();
		root=r;
		l=null;
		level=0;
	}
	//scans the tree into list and starts are begining of list
	public int Walk()
	{
		if (root!=null) PreOrder(root);
		else return 0;
		l=list.listIterator();
		current= l.next();
		return list.size();
	}
	
	private void PreOrder (TreeNode t)
	{
		t.level=level; 
		list.add(t);
		level++;
		if (t.C1!=null) PreOrder(t.C1);
		if (t.C2!=null) PreOrder(t.C2);
		if (t.C3!=null) PreOrder(t.C3);
		level--;
		if (t.sibling!=null) PreOrder(t.sibling);
		
		
	}
	public boolean hasNext()
	{
		return l.hasNext();
	}
	//Next value in LinkedList;
	public TreeNode next()
	{
		current= l.next();
		return current;
	}
	
	public void reset()
	{
		l=list.listIterator();
	}
	
}
