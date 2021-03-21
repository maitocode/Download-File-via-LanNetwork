package pk;

public class infoFolder {

	private String name;
	private boolean leaf;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public infoFolder (String name, boolean leaf)
	{
		this.name = name;
		this.leaf = leaf;
	}
	public boolean isLeaf()
	{
		return leaf;
	}
	public String getName()
	{
		return name;
	}	
	@Override
	public String toString()
	{
		return name;
	}
}
