
public class Navi {

	int level = 0;
	
	void preorder (TreeNode root)
	{
		System.out.println(root.toString(level));
		level++;
		String t = "\n";
				for (int i =0; i<level; i++)
				{
					t=t+"\t";
				}
		if (root.C1 != null) 
			{
				System.out.print(t+"C1");
				preorder(root.C1);
			}
		
		if (root.C2 != null) 
			{
				System.out.print(t+"C2");
				preorder(root.C2);
			}
		
		if (root.C3 != null) 
			{
				System.out.print(t+"C3");
				preorder(root.C3);
			}
		level--;
		t = "\n";
		for (int i =0; i<level; i++)
		{
			t=t+"\t";
		}
		if (root.sibling != null) 
			{
				System.out.print(t+"Sibling");
				preorder(root.sibling);
			}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
