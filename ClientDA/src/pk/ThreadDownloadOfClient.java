package pk;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JProgressBar;

public class ThreadDownloadOfClient extends Thread {

	private String fileName = null;
	private DataOutputStream Os;
	private DataInputStream Is;
	private Socket socket;
	private ServerSocket serverSocket;
	private int port;
	File fileClient = new File("./Client File");
	int filesize;
	int read = 0;
	int totalRead = 0;
	int remaining;
	DataClientShare data;
	public ThreadDownloadOfClient(int port, String fileName, DataClientShare data)
	{
		this.port = port;
		this.fileName = fileName;
		this.data = data;
	}
	@Override
	public void run()
	{	
		try {	
			Socket socket = new Socket();
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();	
			Os = new DataOutputStream(socket.getOutputStream());
			Is = new DataInputStream(socket.getInputStream());
			Os.writeUTF(fileName);
			filesize = (int) Is.readLong();
			
			File saveFile = new File(fileClient, fileName);
			FileOutputStream fos = new FileOutputStream(saveFile);
			
			remaining = filesize;
			byte[] buffer = new byte[4096];
			while((read = Is.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) 
			{
				totalRead += read;
				remaining -= read;
				int value = (totalRead / filesize) * 100;
				//setProgress(value);
				System.out.println("read " + totalRead + " bytes.");
				fos.write(buffer, 0, read);
			}
			fos.close();
			Is.close();
			Os.close();
			serverSocket.close();
			socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	void setProgress(int value)
	{
		data.setProgress(value);
	}
}
