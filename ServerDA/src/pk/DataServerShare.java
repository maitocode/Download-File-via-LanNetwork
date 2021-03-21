package pk;
import javax.swing.JTree;

public class DataServerShare {
	
	String tree;
	private String workName;
	private boolean haveWork;
	private String[] fileName;
	
	private byte[] fileToByte;
	
	public DataServerShare()
	{
		this.workName = "None";
		this.haveWork = false;
	}
	
	public void setTree(String tree)
	{
		this.tree = tree;
	}
	
	public String getTree()
	{
		return tree;
	}
	
	public String getWorkName()
	{
		return workName;
	}
	
	public void setWorkName(String name)
	{
		workName = name;
	}
	
	public boolean getHaveWork()
	{
		return haveWork;
	}
	
	public void setHaveWork(boolean value)
	{
		haveWork = value;
	}
	
	public String[] getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String[] fileName)
	{
		this.fileName = fileName;
	}
}