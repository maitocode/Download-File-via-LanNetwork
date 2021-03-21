package pk;

import java.awt.Color;

public class ListenClient extends Thread {
	public boolean exit;
	protected boolean doneWork;
	public ListenClient()
	{
		this.exit = false;
		this.doneWork = true;
	}
	public void setExit()
	{
		exit = true;
	}
	
	protected synchronized void doWork(String workName)
	{
		System.out.println(workName + "cjc");
		this.doneWork = true;
	}
	
	public boolean isClose()
	{
		return exit;
	}
}
