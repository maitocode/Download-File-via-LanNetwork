package pk;

import javax.swing.JTree;

public class DataClientShare {
	String[] tree;
	int progress;
	
	public DataClientShare()
	{
		tree = null;
		progress = 0;
	}
	
	public void setTree(String[] tree)
	{
		this.tree = tree;
	}
	
	public String[] getTree()
	{
		return tree;
	}
	
	public int getProgress()
	{
		return progress;
	}
	
	public void setProgress(int value)
	{
		this.progress = value;
	}
}
