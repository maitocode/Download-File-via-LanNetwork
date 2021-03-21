package pk;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadDownloadOfServer extends Thread {
	private DataInputStream Is;
	private DataOutputStream Os;
	private Socket socket;
	private String ip;
	private int port;
	private String fileName;
	private File resuilt = null;
	private FileInputStream fis;
	public ThreadDownloadOfServer(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void run()
	{
		try {	
			socket = new Socket(ip, port);
			Is = new DataInputStream(socket.getInputStream());
			Os = new DataOutputStream(socket.getOutputStream());
			
			fileName = Is.readUTF();
			File rootFile = new File("./Server");
			getPathOfFile(rootFile, fileName);
			Os.writeLong(resuilt.length());
			fis = new FileInputStream(resuilt);
			byte[] buffer = new byte[4096];
			
			while(fis.read(buffer) > 0)
			{
				Os.write(buffer);
			}
			
			Os.flush();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void getPathOfFile(File rootFile, String fileName)
	{
		for (File fileEntry : rootFile.listFiles())
		{
			if (!fileEntry.isDirectory())
			{
				if (fileEntry.getName().equals(fileName))
				{
					resuilt = fileEntry;
					return;
				}
			}
			else 
			{
				getPathOfFile(fileEntry, fileName);
			}
		}
	}
}
