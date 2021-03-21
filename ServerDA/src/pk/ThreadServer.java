package pk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

class ThreadServer extends Thread {

	protected Socket socket;
	protected ServerSocket serverSocket;
	protected DataInputStream input;
	protected DataOutputStream output;
	private boolean exit;
	private DataServerShare data;
	
	public ThreadServer(int port, DataServerShare data)
	{
		socket = new Socket();
		this.data = data;
		exit = false;
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		synchronized(data)
		{		
			while (!exit)
			{
				try {
					if (input.available() > 0)
					{		
						String workName = input.readUTF();
						data.setWorkName(workName);
						data.setHaveWork(true);
						try {
							data.notifyAll();
							data.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("sent");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try
			{
				input.close();
				serverSocket.close();
				socket.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public boolean sendTree()
	{	
		try
		{
			System.out.println(data.getTree());
			output.writeUTF(data.getTree());
			return true;
		}
		catch(Exception e)
		{
			e.getStackTrace();
			return false;
		}
	}
	
	public void sendFile()
	{
		
	}
	
	public void CloseSV()
	{
		exit = true;
	}	
}