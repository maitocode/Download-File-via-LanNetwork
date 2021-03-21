 package pk;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTree;
import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.util.List;

import javax.swing.event.CaretEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerApp extends JFrame {
	private JPanel contentPane;
	private JTextField ttfEnterPort;
	private JLabel lbEnterPort;
	private JButton btnStartServer;
	private JPanel pTree;
	private JTree tree;
	private JLabel lbState;
	private JButton btnCloseServer;
	private ThreadServer threadServer = null;
	private Thread listenClient;
	private boolean stopListenClientThread;
	static DataServerShare data;
	private File folder;
	private ThreadDownloadOfServer threadDownload;
	
	public ServerApp() {
		setTitle("ServerApp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 362, 337);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		data = new DataServerShare();
		
		lbEnterPort = new JLabel("Enter Port\r\n");
		lbEnterPort.setBounds(32, 15, 65, 14);
		contentPane.add(lbEnterPort);
		
		ttfEnterPort = new JTextField();
		ttfEnterPort.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (threadServer == null)
				{
					if (!ttfEnterPort.getText().equals(""))
						btnStartServer.setEnabled(true);
					else btnStartServer.setEnabled(false);
				}			
			}
		});
		ttfEnterPort.setBounds(91, 12, 65, 20);
		contentPane.add(ttfEnterPort);
		ttfEnterPort.setColumns(10);	
		
		btnCloseServer = new JButton("Close Server");
		btnCloseServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopListenClientThread = true;
				threadServer.CloseSV();
				btnCloseServer.setEnabled(false);
				ttfEnterPort.setText("");
				btnStartServer.setEnabled(false);
				threadServer = null;
				ttfEnterPort.setEditable(true);
				ttfEnterPort.setEnabled(true);
				writeState("Closed!", Color.BLUE);
			}
		});
		btnCloseServer.setForeground(Color.BLACK);
		btnCloseServer.setEnabled(false);
		btnCloseServer.setBounds(208, 40, 113, 23);
		contentPane.add(btnCloseServer);			
		
		btnStartServer = new JButton("Start Server\r\n");
		btnStartServer.setForeground(new Color(0, 0, 0));
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port = Integer.parseInt(ttfEnterPort.getText());	
				threadServer = new ThreadServer(port, data);
				writeState("Connected!", Color.BLUE);
				btnStartServer.setEnabled(false);
				btnCloseServer.setEnabled(true);	
				StartListenClient();
				threadServer.start();	
				listenClient.start();
				ttfEnterPort.setEditable(false);
			}
		});
		btnStartServer.setEnabled(false);
		btnStartServer.setBounds(208, 11, 113, 23);
		contentPane.add(btnStartServer);

		pTree = new JPanel();
		pTree.setBounds(10, 74, 326, 213);
		contentPane.add(pTree);
		pTree.setLayout(null);

		File fileServers = new File("./Server");
		fileServers.mkdir();
		File fileServer = new File("./Server/FileServer");
		fileServer.mkdir();
		
		File fileServer1 = new File(fileServer, "./DATA.dat");
		File fileServer2 = new File(fileServer, "./DATA2.dat");
		File fileServer3 = new File(fileServer, "./DATA3.dat");
		List<File> list = new ArrayList<File>();
		list.add(fileServer1);
		list.add(fileServer2);
		list.add(fileServer3);
		try
		{
			for (File myFile : list)
			{
				FileOutputStream fStream = new FileOutputStream(myFile);
				DataOutputStream dataOut = new DataOutputStream(fStream);
			}
		}
		catch(Exception e1)
		{
			writeState("Can't add file to Server File", Color.RED);
		}
		
		folder = new File("./Server");
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Server");
		listFilesForFolder(folder, root);
		tree = new JTree(root);
		tree.setCellRenderer(new MyTreeCellRenderer());
		JScrollPane treeView = new JScrollPane(tree);
		this.add(treeView);
		tree.setEditable(false);
		tree.setBounds(10, 11, 306, 191);
		pTree.add(tree);
		
		lbState = new JLabel("Not Connect");
		lbState.setEnabled(false);
		lbState.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		lbState.setHorizontalAlignment(SwingConstants.CENTER);
		lbState.setBackground(Color.WHITE);
		lbState.setForeground(Color.BLUE);
		lbState.setBounds(11, 40, 150, 23);
		contentPane.add(lbState);
		
		try {
			InetAddress ip = InetAddress.getLocalHost();
			writeState("┌IP Address: " + ip.getHostAddress().trim(), Color.BLUE);
		}
		catch(Exception e)
		{
			writeState("Can't get IP", Color.yellow);
		}
		this.setResizable(false);
	}
	
	private void StartListenClient()
	{
		stopListenClientThread = false;
		listenClient = new ListenClient() 
		{
			@Override
			public void run()
			{
				synchronized(data)
				{
					while (!stopListenClientThread)
					{
						if (data.getHaveWork())
						{								
							String workName = data.getWorkName();
							if (workName.equals("refresh"))
							{
								System.out.println("sending...");
								String str = getLeafToSend(folder);
								data.setTree(str);
								threadServer.sendTree();
							}
							else
							{
								String[] arr = workName.split(" ", 0);
								threadDownload = new ThreadDownloadOfServer(arr[1], Integer.parseInt(arr[2]));
								threadDownload.start();
							}
							data.setHaveWork(false);
							data.notifyAll();
						}
						else 
						{
							try {
								data.wait();
							} catch (InterruptedException e) {
								writeState("Error at Thread listen Client", Color.red);
							}
						}
					}
				}				
			}
		};
	}
	
	private void listFilesForFolder(File folder, DefaultMutableTreeNode tree)
	{
		for (File fileEntry : folder.listFiles())
		{
			if (fileEntry.isDirectory())
			{
				infoFolder infoF = new infoFolder(fileEntry.getName(), false);
				DefaultMutableTreeNode bough = new DefaultMutableTreeNode(infoF);
				bough.setAllowsChildren(true);
				listFilesForFolder(fileEntry, bough);
				tree.add(bough);
			}
			else 
			{
				infoFolder infoF = new infoFolder(fileEntry.getName(), true);
				DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(infoF);
				tree.add(leaf);
			}
		}
	}
	
	private String getLeafToSend(File folder)
	{
		if (folder.listFiles() == null) return "";
		String valueStr = "";
		for (File fileEntry : folder.listFiles())
		{
			if (fileEntry.isDirectory())
			{
				valueStr += getLeafToSend(fileEntry);
			}
			else
			{
				valueStr += fileEntry.getName() + '-';
			}
		}
		return valueStr;
	}
	
	private void writeState(String str, Color color)
	{
		lbState.setText(str);
		lbState.setForeground(color);
		lbState.setEnabled(true);
	}
}