package pk;

import java.awt.EventQueue;
import java.lang.Object;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.CaretEvent;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ClientApp extends JFrame {
	private JPanel contentPane;
	private JTextField ttfIP1;
	private JTextField ttfPort1;
	private JTextField ttfIP2;
	private JTextField ttfPort2;
	private JTextField ttfIP3;
	private JTextField ttfPort3;
	private JButton btnDownload;
	private JButton btnViewFileDownload;
	private JPanel panel;
	private JLabel lbIP1;
	private JLabel lbPort1;
	private JPanel pChoose1;
	private JTree tree1;
	private JButton btnTree1;
	private JButton btnConnect1;
	private JPanel pProgress1;
	private JPanel panel_2;
	private JLabel lbIP2;
	private JLabel lbPort2;
	private JPanel pChoose2;
	private JTree tree2;
	private JButton btnTree2;
	private JButton btnConnect2;
	private JPanel pProgress2;
	private JPanel panel_3;
	private JLabel lbIP3;
	private JLabel lbPort3;
	private JPanel pChoose3;
	private JTree tree3;
	private JButton btnTree3;
	private JButton btnConnect3;
	private JPanel pProgress3;
	private JLabel lbState;
	private JButton btnClose1;
	private JButton btnClose2;
	private JButton btnClose3;
	private JButton btnRefresh1;
	private JButton btnRefresh2;
	private JButton btnRefresh3;
	private JLabel lbFileName1;
	private JLabel lbFileName2;
	private JLabel lbFileName3;
	
	private JLabel lbProgress1 = new JLabel("...");
	private JLabel lbProgress2 = new JLabel("...");
	private JLabel lbProgress3 = new JLabel("...");
	
	private DataClientShare data1;
	private DataClientShare data2;
	private DataClientShare data3;
	
	private ThreadClient client1;
	private ThreadClient client2;
	private ThreadClient client3;
	private ThreadDownloadOfClient threadDownload1;
	private ThreadDownloadOfClient threadDownload2;
	private ThreadDownloadOfClient threadDownload3;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public ClientApp() {
		setResizable(false);
		setTitle("ClientApp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 824, 593);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		data1 = new DataClientShare();
		data2 = new DataClientShare();
		data3 = new DataClientShare();
		
		File fileServers = new File("./Client File");
		fileServers.mkdir();
		/*
		Thread t1 = new Thread() {
			@Override
			public void run()
			{
					int value = 0;
					while (value < 100)
					{
						psBProgress1.setValue(value);
						value = data1.getProgress();
					}
					psBProgress1.setValue(100);			
			}
		};
		*/
		btnDownload = new JButton("Download\r\n");
		btnDownload.setEnabled(false);
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//if (btnTree1.getModel().isPressed())
				//{
					DefaultMutableTreeNode value1 = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
					threadDownload1 = new ThreadDownloadOfClient(19, value1.getUserObject().toString(), data1);
					client1.sendDownload(19);
					threadDownload1.start();
					btnTree1.setEnabled(false);
				//}
				
				//if (btnTree2.getModel().isPressed())
				//{
					DefaultMutableTreeNode value2 = (DefaultMutableTreeNode) tree2.getLastSelectedPathComponent();
					threadDownload2 = new ThreadDownloadOfClient(20, value2.getUserObject().toString(), data2);
					client2.sendDownload(20);
					threadDownload2.start();
					btnTree2.setEnabled(false);
				//}
				
				//if (btnTree3.getModel().isPressed())
				//{
					DefaultMutableTreeNode value3 = (DefaultMutableTreeNode) tree3.getLastSelectedPathComponent();
					threadDownload3 = new ThreadDownloadOfClient(21, value3.getUserObject().toString(), data3);			
					client3.sendDownload(21);
					threadDownload3.start();
					btnTree3.setEnabled(false);
				//}
				
				btnDownload.setEnabled(false);
				//t1.start();
				// tao 3 thread cho ket noi cong 19, 20, 21. moi thread co 1 bien luu ten thu muc can tai. khi co ket noi toi thi se gui ten thu muc di va cho nhan file. sau khi nhan duoc file thi luu file va dong cong
			}
		});
		btnDownload.setBounds(286, 522, 94, 23);
		contentPane.add(btnDownload);
		
		btnViewFileDownload = new JButton("File Downloaded\r\n");
		btnViewFileDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Desktop d = null;
				File file = new File("./Client File");
				if (Desktop.isDesktopSupported())
				{
					d = Desktop.getDesktop();
				}
				try {
					d.open(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
		btnViewFileDownload.setEnabled(true);
		btnViewFileDownload.setBounds(390, 522, 139, 23);
		contentPane.add(btnViewFileDownload);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Server1", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setBounds(10, 37, 243, 458);
		contentPane.add(panel);
		panel.setLayout(null);
		
		ttfIP1 = new JTextField();
		ttfIP1.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (client1 == null)
				{
					if (!ttfPort1.getText().equals("") && !ttfIP1.getText().equals(""))
						btnConnect1.setEnabled(true);
					else 
						btnConnect1.setEnabled(false);
				}
			}
		});
		ttfIP1.setColumns(10);
		ttfIP1.setBounds(50, 21, 86, 20);
		panel.add(ttfIP1);
		
		ttfPort1 = new JTextField();
		ttfPort1.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (client1 == null)
				{
					if (!ttfIP1.getText().equals("") && !ttfPort1.getText().equals(""))
						btnConnect1.setEnabled(true);
					else 
						btnConnect1.setEnabled(false);
				}	
			}
		});
		ttfPort1.setColumns(10);
		ttfPort1.setBounds(50, 46, 86, 20);
		panel.add(ttfPort1);
		
		lbIP1 = new JLabel("IP:");
		lbIP1.setBounds(10, 24, 46, 14);
		panel.add(lbIP1);
		
		lbPort1 = new JLabel("Port:");
		lbPort1.setBounds(10, 49, 46, 14);
		panel.add(lbPort1);
		
		pChoose1 = new JPanel();
		pChoose1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Server File", TitledBorder.LEFT, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));
		pChoose1.setLayout(null);
		pChoose1.setBounds(10, 77, 224, 305);
		panel.add(pChoose1);
		
		tree1 = new JTree();
		tree1.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode value = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
				if (value != null)
					btnTree1.setEnabled(true);
			}
		});
		tree1.setEditable(false);
		tree1.setEnabled(false);
		tree1.setShowsRootHandles(true);
		tree1.setBounds(10, 28, 204, 236);
		pChoose1.add(tree1);
		btnTree1 = new JButton("OK");
		btnTree1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode value = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
				lbFileName1.setText(value.getUserObject().toString());
				btnTree1.setEnabled(false);
				tree1.setEnabled(false);
				btnDownload.setEnabled(true);
			}
		});
		btnTree1.setEnabled(false);
		btnTree1.setBounds(125, 271, 89, 23);
		pChoose1.add(btnTree1);
		
		btnRefresh1 = new JButton("Refresh");
		btnRefresh1.setEnabled(false);
		btnRefresh1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				client1.sendRefresh();
				System.out.print("null at getTree Client1");
				while (data1.getTree() == null)
				{
					System.out.print("null at getTree Client");
				}
				System.out.print("null at getTree Client2");
				String[] treeArr = data1.getTree();
				DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Server File");
				for (String str : treeArr)
				{
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(str);
					rootNode.add(childNode);
				}
				DefaultTreeModel defaultTreeModel = new DefaultTreeModel(rootNode);
				tree1.setModel(defaultTreeModel);
				tree1.setEnabled(true);
				lbFileName1.setText("file_name_1");
				data1.setTree(null);
			}
		});
		btnRefresh1.setBounds(10, 271, 89, 23);
		pChoose1.add(btnRefresh1);
		
		btnConnect1 = new JButton("Connect");
		btnConnect1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!lbIP1.getText().equals("") && !lbPort1.getText().equals(""))
				{
					int port = Integer.parseInt(ttfPort1.getText());
					String IP = ttfIP1.getText();
					client1 = new ThreadClient(IP, port, data1);
					if (client1.ConnectSV())
					{
						writeState("Server1 Connected!", Color.BLUE);
						client1.start();
						lbState.setEnabled(true);
						ttfIP1.setEditable(false);
						ttfPort1.setEditable(false);
						btnConnect1.setEnabled(false);
						btnClose1.setEnabled(true);
						btnRefresh1.setEnabled(true);
					}
					else
					{
						writeState("Server1: IP or Port is wrong!", Color.RED);
						ttfPort1.setText("");
						ttfIP1.setText("");
					}
				}
			}
		});
		btnConnect1.setEnabled(false);
		btnConnect1.setBounds(146, 20, 88, 23);
		panel.add(btnConnect1);
		
		pProgress1 = new JPanel();
		pProgress1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Progress", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));
		pProgress1.setBounds(10, 394, 223, 53);
		panel.add(pProgress1);
		pProgress1.setLayout(null);
		
		lbProgress1.setEnabled(true);
		lbProgress1.setBounds(10, 28, 203, 14);
		lbProgress1.setVerticalTextPosition(JLabel.CENTER);
		lbProgress1.setHorizontalAlignment(JLabel.CENTER);
		pProgress1.add(lbProgress1);
		
		
		btnClose1 = new JButton("Close");
		btnClose1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client1.CloseClient();
				writeState("Client1 Closed!", Color.BLUE);
				btnConnect1.setEnabled(false);
				ttfPort1.setText("");
				ttfIP1.setText("");
				ttfPort1.setEditable(true);
				ttfIP1.setEditable(true);
				btnClose1.setEnabled(false);
				btnConnect1.setEnabled(false);
				btnRefresh1.setEnabled(false);
				tree1.setEnabled(false);
				client1 = null;
			}
		});
		btnClose1.setEnabled(false);
		btnClose1.setBounds(146, 45, 88, 23);
		panel.add(btnClose1);
		
		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Server2", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(286, 37, 243, 458);
		contentPane.add(panel_2);
		
		ttfIP2 = new JTextField();
		ttfIP2.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (client2 == null)
				{
					if (!ttfPort2.getText().equals("") && !ttfIP2.getText().equals(""))
						btnConnect2.setEnabled(true);
					else 
						btnConnect2.setEnabled(false);
				}
			}
		});
		ttfIP2.setColumns(10);
		ttfIP2.setBounds(50, 21, 86, 20);
		panel_2.add(ttfIP2);
		
		ttfPort2 = new JTextField();
		ttfPort2.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (client2 == null)
				{
					if (!ttfIP2.getText().equals("") && !ttfPort2.getText().equals(""))
						btnConnect2.setEnabled(true);
					else 
						btnConnect2.setEnabled(false);
				}	
			}
		});
		ttfPort2.setColumns(10);
		ttfPort2.setBounds(50, 46, 86, 20);
		panel_2.add(ttfPort2);
		
		lbIP2 = new JLabel("IP:");
		lbIP2.setBounds(10, 24, 46, 14);
		panel_2.add(lbIP2);
		
		lbPort2 = new JLabel("Port:");
		lbPort2.setBounds(10, 49, 46, 14);
		panel_2.add(lbPort2);
		
		pChoose2 = new JPanel();
		pChoose2.setLayout(null);
		pChoose2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Server File", TitledBorder.LEFT, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));
		pChoose2.setBounds(10, 77, 224, 305);
		panel_2.add(pChoose2);
		
		tree2 = new JTree();
		tree2.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode value = (DefaultMutableTreeNode) tree2.getLastSelectedPathComponent();
				if (value != null)
					btnTree2.setEnabled(true);
			}
		});
		tree2.setEnabled(false);
		tree2.setBounds(10, 28, 204, 236);
		pChoose2.add(tree2);
		
		btnTree2 = new JButton("OK");
		btnTree2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode value = (DefaultMutableTreeNode) tree2.getLastSelectedPathComponent();
				lbFileName2.setText(value.getUserObject().toString());
				btnTree2.setEnabled(false);
				tree2.setEnabled(false);
				btnDownload.setEnabled(true);
			}
		});
		btnTree2.setEnabled(false);
		btnTree2.setBounds(125, 271, 89, 23);
		pChoose2.add(btnTree2);
		
		btnRefresh2 = new JButton("Refresh");
		btnRefresh2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				client2.sendRefresh();
				System.out.print("null at getTree Client1");
				while (data2.getTree() == null)
				{
					System.out.print("null at getTree Client");
				}
				System.out.print("null at getTree Client2");
				String[] treeArr = data2.getTree();
				DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Server File");
				for (String str : treeArr)
				{
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(str);
					rootNode.add(childNode);
				}
				DefaultTreeModel defaultTreeModel = new DefaultTreeModel(rootNode);
				tree2.setModel(defaultTreeModel);
				tree2.setEnabled(true);
				lbFileName2.setText("file_name_2");
				data2.setTree(null);
			}
		});
		btnRefresh2.setEnabled(false);
		btnRefresh2.setBounds(10, 271, 89, 23);
		pChoose2.add(btnRefresh2);
		
		btnConnect2 = new JButton("Connect");
		btnConnect2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!lbIP2.getText().equals("") && !lbPort2.getText().equals(""))
				{
					int port = Integer.parseInt(ttfPort2.getText());
					String IP = ttfIP2.getText();
					client2 = new ThreadClient(IP, port, data2);
					if (client2.ConnectSV())
					{
						writeState("Server2 Connected!", Color.BLUE);
						client2.start();
						lbState.setEnabled(true);
						ttfIP2.setEditable(false);
						ttfPort2.setEditable(false);
						btnConnect2.setEnabled(false);
						btnClose2.setEnabled(true);
						btnRefresh2.setEnabled(true);
					}
					else
					{
						writeState("Server2: IP or Port is wrong!", Color.RED);
						ttfPort2.setText("");
						ttfIP2.setText("");
					}
				}
			}
		});
		btnConnect2.setEnabled(false);
		btnConnect2.setBounds(146, 20, 88, 23);
		panel_2.add(btnConnect2);

		pProgress2 = new JPanel();
		pProgress2.setLayout(null);
		pProgress2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Progress", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));
		pProgress2.setBounds(11, 393, 223, 53);
		panel_2.add(pProgress2);

		lbProgress2.setEnabled(true);
		lbProgress2.setBounds(10, 28, 203, 14);
		lbProgress2.setVerticalTextPosition(JLabel.CENTER);
		lbProgress2.setHorizontalAlignment(JLabel.CENTER);
		pProgress2.add(lbProgress2);
		
		btnClose2 = new JButton("Close\r\n");
		btnClose2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client2.CloseClient();
				writeState("Client2 Closed!", Color.BLUE);
				btnConnect2.setEnabled(false);
				ttfPort2.setText("");
				ttfIP2.setText("");
				ttfPort2.setEditable(true);
				ttfIP2.setEditable(true);
				btnClose2.setEnabled(false);
				btnConnect2.setEnabled(false);
				btnRefresh2.setEnabled(false);
				tree2.setEnabled(false);
				client2 = null;
			}
		});
		btnClose2.setEnabled(false);
		btnClose2.setBounds(146, 45, 88, 23);
		panel_2.add(btnClose2);

		panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Server3", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(563, 37, 243, 458);
		contentPane.add(panel_3);
		
		ttfIP3 = new JTextField();
		ttfIP3.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (client3 == null)
				{
					if (!ttfPort3.getText().equals("") && !ttfIP3.getText().equals(""))
						btnConnect3.setEnabled(true);
					else 
						btnConnect3.setEnabled(false);
				}
			}
		});
		ttfIP3.setColumns(10);
		ttfIP3.setBounds(50, 21, 86, 20);
		panel_3.add(ttfIP3);
		
		ttfPort3 = new JTextField();
		ttfPort3.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (client3 == null)
				{
					if (!ttfIP3.getText().equals("") && !ttfPort3.getText().equals(""))
						btnConnect3.setEnabled(true);
					else 
						btnConnect3.setEnabled(false);
				}	
			}
		});
		ttfPort3.setColumns(10);
		ttfPort3.setBounds(50, 46, 86, 20);
		panel_3.add(ttfPort3);

		lbIP3 = new JLabel("IP:");
		lbIP3.setBounds(10, 24, 46, 14);
		panel_3.add(lbIP3);

		lbPort3 = new JLabel("Port:");
		lbPort3.setBounds(10, 49, 46, 14);
		panel_3.add(lbPort3);
		
		pChoose3 = new JPanel();
		pChoose3.setLayout(null);
		pChoose3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Server File", TitledBorder.LEFT, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));
		pChoose3.setBounds(10, 77, 224, 305);
		panel_3.add(pChoose3);
		
		tree3 = new JTree();
		tree3.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode value = (DefaultMutableTreeNode) tree3.getLastSelectedPathComponent();
				if (value != null)
					btnTree3.setEnabled(true);
			}
		});
		tree3.setEnabled(false);
		tree3.setBounds(10, 28, 204, 236);
		pChoose3.add(tree3);
		
		btnTree3 = new JButton("OK");
		btnTree3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode value = (DefaultMutableTreeNode) tree3.getLastSelectedPathComponent();
				lbFileName3.setText(value.getUserObject().toString());
				btnTree3.setEnabled(false);
				tree3.setEnabled(false);
				btnDownload.setEnabled(true);
			}
		});
		btnTree3.setEnabled(false);
		btnTree3.setBounds(125, 271, 89, 23);
		pChoose3.add(btnTree3);
		
		btnRefresh3 = new JButton("Refresh");
		btnRefresh3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				client3.sendRefresh();
				System.out.print("null at getTree Client3");
				while (data3.getTree() == null)
				{
					System.out.print("null at getTree Client");
				}
				System.out.print("null at getTree Client3");
				String[] treeArr = data3.getTree();
				DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Server File");
				for (String str : treeArr)
				{
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(str);
					rootNode.add(childNode);
				}
				DefaultTreeModel defaultTreeModel = new DefaultTreeModel(rootNode);
				tree3.setModel(defaultTreeModel);
				tree3.setEnabled(true);
				lbFileName3.setText("file_name_3");
				data3.setTree(null);
			}
		});
		btnRefresh3.setEnabled(false);
		btnRefresh3.setBounds(10, 271, 89, 23);
		pChoose3.add(btnRefresh3);

		btnConnect3 = new JButton("Connect");
		btnConnect3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!lbIP3.getText().equals("") && !lbPort3.getText().equals(""))
				{
					int port = Integer.parseInt(ttfPort3.getText());
					String IP = ttfIP3.getText();
					client3 = new ThreadClient(IP, port, data3);
					if (client3.ConnectSV())
					{
						writeState("Server3 Connected!", Color.BLUE);
						client3.start();
						lbState.setEnabled(true);
						ttfIP3.setEditable(false);
						ttfPort3.setEditable(false);
						btnConnect3.setEnabled(false);
						btnClose3.setEnabled(true);
						btnRefresh3.setEnabled(true);
					}
					else
					{
						writeState("Server3: IP or Port is wrong!", Color.RED);
						ttfPort3.setText("");
						ttfIP3.setText("");
					}
				}
			}
		});
		btnConnect3.setEnabled(false);
		btnConnect3.setBounds(146, 20, 88, 23);
		panel_3.add(btnConnect3);
		

		pProgress3 = new JPanel();
		pProgress3.setLayout(null);
		pProgress3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Progress", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));
		pProgress3.setBounds(10, 394, 223, 53);
		panel_3.add(pProgress3);
		
		lbProgress3.setEnabled(true);
		lbProgress3.setBounds(10, 28, 203, 14);
		lbProgress3.setVerticalTextPosition(JLabel.CENTER);
		lbProgress3.setHorizontalAlignment(JLabel.CENTER);
		pProgress3.add(lbProgress3);
		
		btnClose3 = new JButton("Close");
		btnClose3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client3.CloseClient();
				writeState("Client3 Closed!", Color.BLUE);
				btnConnect3.setEnabled(false);
				ttfPort3.setText("");
				ttfIP3.setText("");
				ttfPort3.setEditable(true);
				ttfIP3.setEditable(true);
				btnClose3.setEnabled(false);
				btnConnect3.setEnabled(false);
				btnRefresh3.setEnabled(false);
				tree3.setEnabled(false);
				client3 = null;
			}
		});
		btnClose3.setEnabled(false);
		btnClose3.setBounds(146, 45, 88, 23);
		panel_3.add(btnClose3);
		
		lbState = new JLabel("State...");
		lbState.setEnabled(false);
		lbState.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbState.setForeground(new Color(0, 0, 255));
		lbState.setBackground(new Color(255, 255, 255));
		lbState.setHorizontalAlignment(SwingConstants.CENTER);
		lbState.setBounds(10, 11, 796, 23);
		contentPane.add(lbState);		
		
		lbFileName1 = new JLabel("file_name_1");
		lbFileName1.setHorizontalAlignment(SwingConstants.CENTER);
		lbFileName1.setBounds(52, 11, 112, 20);
		pProgress1.add(lbFileName1);
		
		lbFileName2 = new JLabel("file_name_2");
		lbFileName2.setHorizontalAlignment(SwingConstants.CENTER);
		lbFileName2.setBounds(51, 11, 112, 20);
		pProgress2.add(lbFileName2);

		lbFileName3 = new JLabel("file_name_3");
		lbFileName3.setHorizontalAlignment(SwingConstants.CENTER);
		lbFileName3.setBounds(52, 11, 112, 20);
		pProgress3.add(lbFileName3);
	}
	void writeState(String str, Color color)
	{
		lbState.setText(str);
		lbState.setForeground(color);
		lbState.setEnabled(true);
	}
}