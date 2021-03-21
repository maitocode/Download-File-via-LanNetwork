package pk;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTree;

public class ThreadClient extends Thread {

	private Socket socket;
	String ip;
	int port;

	DataInputStream input;
	DataOutputStream output;
	boolean exit;
	DataClientShare data;
	//int flag;

	public ThreadClient(String ip, int port, DataClientShare data) {
		this.ip = ip;
		this.port = port;
		this.data = data;
	}

	@Override
	public void run() {
		synchronized(data)
		{
			exit = false;
			while (!exit) {
				try {
					if (input.available() > 0) {
						System.out.println("Data UTF avaiable!!");
						
						//flag = 1 mean refresh
						//flag = 2 mean download
							String[] treeStr = (input.readUTF()).split("-", 0);
							for (String a : treeStr)
							{
								System.out.println(a);
							}
							data.setTree(treeStr);						
					}
						 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				input.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

	public boolean ConnectSV() {
		try {

			socket = new Socket(ip, port);
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void CloseClient() {
		exit = true;
	}

	public void sendDownload(int port) {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			output.writeUTF("download " + addr.getHostAddress().toString() + " " + port);
//			this.flag = 2;
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	public void sendRefresh() {
		try {
			output.writeUTF("refresh");
//			this.flag = 1;
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
}

// get String of File to String to send after send download;
