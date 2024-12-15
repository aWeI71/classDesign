package surface;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import kernel.*;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

//import kannel.Path;
//import kannel.FileManage;
//import kannel.FileMsg;

/*
 * 主要组件开发进度：
 * 1.创建文件界面                          ✓ 
 * 2.创建文件夹界面                        ✓ 
 * 3.读文件界面                            ✓ 
 * 4.写文件界面                            ✓
 * 5.删除文件冲突提示                      ✓
 * 6.关闭文件按钮                          ✓
 * 7.修改文件属性界面                      ✓
 * 8.文件列表选项表                        ✓
 * 9.文件列表附属按钮                      ✓
 * 10.文件列表图标及名称显示               x
 * 更新日期：   9.22  by 骚明
 */

/*
 * 本周主要任务：
 * 1.文件图标
 * 2.树状目录
 * 3.解决bug
 * 4.文件表目录
 */

/*
 * 主界面，主要作用是显示树状目录、打开文件列表、
 * 当前目录内容，要完成相关接口
 */

public class MainWindow {

	private JFrame frame;             //主界面
	private JTextArea pathText;       //显示文件或文件夹路径
	private JTree tree;               //
	DefaultMutableTreeNode rootNode;  //
	DefaultTreeModel treeModel;       //这三个反正就是树状目录用到的东西
	
	//设置图标
	ImageIcon file_icon = new ImageIcon("src.jpg");
	ImageIcon folder_icon = new ImageIcon("src.jpg");
	ImageIcon upPath_icon = new ImageIcon("src.jpg");
	
	//文件操作面板
	FilePanel filePanel;
	JPopupMenu menu;
	
	//图标位置参数
	int start_X = 400;
	int start_Y = 400;
	int add_X = 80;
	int add_Y = 120;
	
	//LeftPanel lp;
	
	//文件管理系统的参数
	Path systemPath;   //当前访问路径
	//FileManage fm;     //文件管理信息
	
	DemoRenderer dre;   //树叶
	
	//放已打开文件表的版面
	TablePane tablePane;
	//这里是打印已打开文件表的变量
	JTable ofTable;
	String[] tableTitle = {"文件路径名","起始","长度","读/写"};
	DefaultTableModel tableModel;
	
	
	//LeftPanel treePanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		//initTable();
		//updateTable();
		initTree();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("模拟文件系统");
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel pathFront = new JLabel("当前路径：");
		pathFront.setBounds(180, 153, 80, 24);
		frame.getContentPane().add(pathFront);
		
		JLabel label = new JLabel("已打开文件列表：");
		label.setBounds(150, 14, 130, 18);
		frame.getContentPane().add(label);

		systemPath = new Path();
		
		FileManage.init();

		//LeftPanel lp = new LeftPanel();
		//lp.setBounds(10, 10, 140, 430);
		//frame.getContentPane().add(lp);
		
        String str = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        char[] name = new char[4];
        name[0] = name[1] = 'S';
        name[2] = 'R';
        char[] backName = new char[3];
        backName[0] = 'p';
        backName[1] = 'g';
        name[3] = backName[2] ='\0';
        
        Path path = new Path();
        systemPath = path.clone();
        
        Disk.start_statu();
        
        try {
			int blockNum = FileManage.create_file(name, backName, (byte)4, path);
			path.PathDown(name, blockNum);
		} catch (FileException e) {
			// TODO 自动生成的 catch 块
			System.out.println("FileException:"+e);
		}
		
		FileManage.delete_file(path);
		
		//设置文件
		tablePane = new TablePane();
		tablePane.setBounds(150, 40, 425, 105);
		frame.getContentPane().add(tablePane);
		
		//treePanel = new LeftPanel();
		//frame.add(treePanel);
		
		//设置文件显示
		pathText = new JTextArea();
		pathText.setText(systemPath.getPathName());
		pathText.setBounds(250, 153, 320, 24);
		frame.getContentPane().add(pathText);
		
		//文件夹显示区
		filePanel = new FilePanel();
		filePanel.setBounds(150, 180,420, 260);
		filePanel.setBackground(new Color(255,255,255));
		frame.getContentPane().add(filePanel);
		filePanel.setVisible(true);
		
		//设置按钮
		JButton back_path = new JButton(upPath_icon);
		back_path.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(systemPath.bottomNum()==2) {
					JOptionPane.showMessageDialog(null,"当前已是根目录！","提示信息",JOptionPane.WARNING_MESSAGE);
				}
				else {
					systemPath.PathUp();
				    pathText.setText(systemPath.getPathName());
				}
				
			}
		});
		back_path.setBounds(150, 152, 24, 24);
		frame.getContentPane().add(back_path);
		
	}
	
	public void changePath() {
		
	}
	
	public void initTree() {
		rootNode  = new DefaultMutableTreeNode("A:/");
		DefaultMutableTreeNode node=new DefaultMutableTreeNode("hh");
		rootNode.add(node);
		
		tree = new JTree(rootNode);
		tree.setBounds(10, 10, 130, 430);
		tree.setFont(new Font("宋体", Font.PLAIN, 8));
		// DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		DemoRenderer renderer = new DemoRenderer();

	    renderer.setFont(new Font("Serif", Font.PLAIN, 22));// 设置树的整体字体样式
		renderer.setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
		renderer.setBackgroundSelectionColor(new Color(0, 0, 0, 0));
		
		tree.setCellRenderer(renderer);
		tree.setOpaque(false);
	    frame.getContentPane().add(tree);
		
	}
	
	class TablePane extends JScrollPane {
		TablePane(){
			initTable();
		}
		public void updateTable() {
			Object[][] tab = new Object[5][4];
			for(int i = 0;i<FileManage.ofLine.getLength();i++) {
				OFTLE ofMsg = FileManage.ofLine.getOfMsg(i);
		    	tab[i][0] = ofMsg.pathName;
		    	tab[i][1] = ofMsg.number;
		    	tab[i][2] = ofMsg.length;
		    	tab[i][3] = (ofMsg.flag==0?"读":"写");
			}
			tableModel = new DefaultTableModel(tab,tableTitle);
			tableModel.fireTableDataChanged();
		}
		
		public void initTable(){
			//tab = new Object[userInfoNumber][6]
		    Object[][] tab = new Object[5][4];
		    for(int i=0;i<FileManage.ofLine.getLength();i++) {
		    	OFTLE ofMsg = FileManage.ofLine.getOfMsg(i);
		    	tab[i][0] = ofMsg.pathName;
		    	tab[i][1] = ofMsg.number;
		    	tab[i][2] = ofMsg.length;
		    	tab[i][3] = (ofMsg.flag==0?"读":"写");
		    }
		    tableModel = new DefaultTableModel(tab,tableTitle);
		    tableModel.fireTableDataChanged();
		    
		    /*
		    ofTable = new JTable(new DefaultTableModel(
		    	new Object[][] {
		    		{null, null, null, null},
		    		{null, null, null, null},
		    		{null, null, null, null},
		    		{null, null, null, null},
		    		{null, null, null, null},
		    	},
		    	
		    ));
		    */
		    
			//JScrollPane tablePane = new JScrollPane();
			//tablePane.setBounds(150, 40, 425, 105);
			//frame.getContentPane().add(tablePane);
			ofTable = new JTable(tableModel);
			
			TableColumnModel columns = ofTable.getColumnModel();
			TableColumn  column = columns.getColumn(0);
			column.setPreferredWidth(220);
			
			column = columns.getColumn(1);
			column.setPreferredWidth(20);
			
			column = columns.getColumn(2);
			column.setPreferredWidth(20);
			
			column = columns.getColumn(3);
			column.setPreferredWidth(20);
			
			this.add(ofTable);
			this.setViewportView(ofTable);
			
			JButton closeFileButton = new JButton("关闭文件");
			closeFileButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int n = ofTable.getSelectedRow();
					if(n>=0) {
						//删除文件
						FileManage.close_file((byte)FileManage.ofLine.getOfMsg(n).number);
						
						//更新列表
						tableModel.removeRow(n);
						tableModel.fireTableDataChanged();
						ofTable.updateUI();
					}
					
				}
			});
			closeFileButton.setBounds(430, 10, 100, 25);
			frame.getContentPane().add(closeFileButton);
			
			setVisible(true);
			
			//JPanel iconPanel = new JPanel();
			//iconPanel.setBackground(Color.WHITE);
			//iconPanel.setBounds(150, 185, 425, 260);
			//frame.getContentPane().add(iconPanel);
			
			
		}
		
		void changePane() {
			
		}
	}
	
	
	
	/*
	public void initIconPanel() {
		//JMenuItem mOPEN, mCREATE_FILE, mCREATE_DIA, mDEL, mATT;
		JMenuItem create,deliete,openF,closeF,readF,writeF,change;
		//设置接口:
		//1.编写显示图标
		//2.编写选项
		
		filePanel.setBounds(150,240,180,340);
		filePanel.setVisible(true);
		
		
		
	}*/
	
	
	
	
	
	class FilePanel extends JPanel{
		JMenuItem create,deliete,open,closeF,readF,writeF,change,reflash,test;
		ArrayList<FileMsg> systemList = new ArrayList<FileMsg>();
		Path path;
		int index;
		FileMsg fms;
		//设置接口:
		//1.编写显示图标
		//2.编写选项
		
		
		FilePanel(){
			systemPath = new Path();
			initFilePanel();
		}
		
		FilePanel(Path path){
			this.path = path;
		}
		
		void showMenu(int x,int y) {
			menu.show(this,x,y);
		}
		
		void addNewListener() {
			this.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						// 弹出菜单
						index = e.getX()/add_X+e.getY()/add_Y*4;
						add_Icon(e.getX()+start_X,e.getY()+start_Y);
						
						showMenu(e.getX(),e.getY());
						
						System.out.println("getX:"+e.getX()+"getY:"+e.getY());
						System.out.println("get index:"+index);
						
					}
				}
			});
			//创建文件目录
		}

		void initFilePanel() {
			updateIcon();
			addNewListener();
			
		}
		
		void setMsg() {
			if(index<0||index>=systemList.size()) {
				return;
			}
			else {
				fms = systemList.get(index);
			}
		}
		
		void updateIcon() {
			//主要是更新版面内容
			//filePanel.removeAll();
			removeAll();
			
			systemList = FileManage.dir(systemPath);
			//int index_X = start_X;
			//int index_Y = start_Y;
			int index_X = 0;
			int index_Y = 0;
			for(int i=0;i<systemList.size();i++) {
				
				FileMsg f = systemList.get(i);
				//JLabel jlab = new JLabel(f.getAllName());
				JButton iconButton;
				
				if(f.getAttribute()<8) {
					 iconButton = new JButton(f.getAllName(),file_icon);
					 iconButton.setBounds(index_X, index_Y, 60, 60);
				}
				else {
					 iconButton = new JButton(f.getAllName(),folder_icon);
					 iconButton.setBounds(index_X, index_Y, 60, 60);
				}
				iconButton.setVerticalTextPosition(JButton.BOTTOM);
				iconButton.setHorizontalTextPosition(JButton.CENTER);
				
				iconButton.setOpaque(false); //设置背景透明 
				this.add(iconButton);
				//jlab.setBounds(index_X, index_Y+35, 35, 20);
				//add(jlab);
				
				if(i%4==3) {
					index_X -= 3*add_X;
					index_Y += add_Y;
				}
				else {
					index_X += add_X;
				}
			}
			this.validate();
		}
		
		Path getIndexPath() {
			if(index<0||index>=systemList.size()) {
				return null;
			}
			else {
				return systemList.get(index).getPath();
			}
		}
		
		boolean here_is_file() {
			if(index<0||index>=systemList.size()) {
				return false;
			}
			if(systemList.get(index).getAttribute()/8==1) {
				return false;
			}
			return true;
		}
		
		void add_Icon(int x,int y) {
            menu = new JPopupMenu();
			boolean isFile = here_is_file();
            
			create = new JMenuItem("创建");
			create.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					CreateFrame createFrame = new CreateFrame(systemPath);
					createFrame.setVisible(true);
					updateIcon();
				}
			});
			menu.add(create);
			
			//获取当前选定的文件夹
			path = getIndexPath();
			
			//删除功能
			deliete = new JMenuItem("删除");
			deliete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//检查是否存在已打开文件
					if(FileManage.withOpedFile(path)) {
						//若存在，则询问是否需要继续删除
						if(JOptionPane.showConfirmDialog(null, "当前删除对象存在已打开文件，\n是否需要删除？",
								"提示信息",JOptionPane.YES_OPTION)==JOptionPane.YES_OPTION) {
							if(isFile) {
								//删除文件
								FileManage.delete_file(path);
							}
							else {
								//删除文件夹
								try {
									FileManage.rd(path);
								}catch(FileException event) {
									JOptionPane.showMessageDialog(null, event.warnMess(),"错误类型",JOptionPane.WARNING_MESSAGE);
								}
							}
							
							//这里弄完更新一下已打开文件列表
							tablePane.updateTable();
							
						}
					}
					else {
						//这里复制一下上面的
						if(isFile) {
							//删除文件
							FileManage.delete_file(path);
						}
						else {
							//删除文件夹
							try {
								FileManage.rd(path);
							}catch(FileException event) {
								JOptionPane.showMessageDialog(null, event.warnMess(),"错误类型",JOptionPane.WARNING_MESSAGE);
							}
						}
						
						//这里弄完更新一下已打开文件列表
						tablePane.updateTable();
						ofTable.updateUI();
					}
					//刷新
					updateIcon();
				}				
			});
			menu.add(deliete);
			
			//打开文件夹
			open = new JMenuItem("打开");
			open.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(!isFile) {
						//打开文件夹
						if(path!=null) {
							systemPath = path;
						    filePanel.updateIcon();
						    updateIcon();
						}
						//这里弄完更新一下已打开文件列表
						tablePane.updateTable();
						ofTable.updateUI();
					}
				}
			});
			menu.add(open);
			
			readF = new JMenuItem("读");
			open.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(path!=null&&isFile) {
						try {
							String s = FileManage.read_file(path);
							
							//ReadFrame rf = new ReadFrame(path.getPathName(),s);
							//ReadFrame rf = new ReadFrame( )
							//rf.setVisible(true);
							tablePane.updateTable();
							ofTable.updateUI();
						}catch(FileException event) {
							JOptionPane.showMessageDialog(null, event.warnMess(),"错误类型",JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				
			});
			
			menu.add(readF);
			
			//调用写文件窗口
			writeF = new JMenuItem("写");
			writeF.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setMsg();
					if(fms!=null) {
						WriteFrame wf = new WriteFrame(fms);
						wf.setVisible(true);
						
						tablePane.updateTable();
						ofTable.updateUI();
						
						
					}
				}
			});
			menu.add(writeF);
			
			//调用修改属性窗口
			change = new JMenuItem("修改属性");
			change.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(path!=null) {
						ChangeFrame cf = new ChangeFrame(path,systemList.get(index).getAttribute());
						cf.setVisible(true);
					}
				}
			});
			menu.add(change);
			
			reflash = new JMenuItem("刷新");
			reflash.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					updateIcon();
				}
			});
			menu.add(reflash);
			
			test = new JMenuItem("显示数据");
			test.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					TestFrame tFrame = new TestFrame();
					tFrame.setVisible(true);
				}
			});
			menu.add(test);
		}
	}
	
	
	

	/*
    class LeftPanel extends JPanel{	
    	
		LeftPanel(){
			
			Path newPath = new Path();
			
			//rootNode = new DefaultMutableTreeNode("A:/");
			
			//treeModel = new DefaultTreeModel(rootNode);
			
			//tree = new JTree( treeModel );
            //add(tree);
            
			
			
            /*
            tree.addTreeSelectionListener( new TreeSelectionListener(){
    			
    			public void valueChanged(TreeSelectionEvent arg0) {
    				//如果不被选，则不执行，反之，执行
    				if ( !tree.isSelectionEmpty()){
    					//获得所选节点的路径名
    					TreePath[] selectPaths = tree.getSelectionPaths();
    					textArea.setText("");
    					for ( int i = 0; i < selectPaths.length; i ++  ){
    						//System.out.println( selectPaths[ i ]);
    						//以Object数组的形式返回该路径中的所有节点的对象
    						Object[] paths = selectPaths[ i ].getPath();
    						for ( int j = 0; j < paths.length; j ++ ){
    							//System.out.print( paths[ j ] + " -> " );
    							DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[ j ];
    							textArea.append( node.getUserObject() + (( j == paths.length -1  ) ? "":" -> ") );
    							//(( j == paths.length -1  ) ? "":" -> ")表示的是不在最后一个节点加入 “->”符号
    						}
    					
    					}
    					//System.out.println();
    				}
    			}
    			});
    			*/
            /*
            tree.addTreeSelectionListener(new TreeSelectionListener(){
            	public void valueChanged(TreeSelectionEvent arg0) {
            		//如果不被选，则不执行，反之，执行
            		if(!tree.isSelectionEmpty()) {
            			ArrayList<FileMsg> fmsg = new ArrayList<FileMsg>();
            			fmsg = FileManage.dir(systemPath);
            			for(int i = 0;i<fmsg.size();i++) {
            				DefaultMutableTreeNode node = new DefaultMutableTreeNode(fmsg.get(i).getName());
            				//tree.add
            				//textArea.append(arg0);
            			}
            		}
            		
            	}
            	
            });
		}
    }*/
}
