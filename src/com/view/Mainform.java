package com.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import com.compiler.*;

public class Mainform extends JFrame {

	JTextArea sourseFile;//用来显示源文件的文本框
	String soursePath;// 源文件路径
	String LL1Path;
	String wordListPath;
	String fourElementPath;
	LexAnalyse lexAnalyse;
	TextArea jTextField;
	Parser parser;
	JSplitPane jSplitPane1;
	JScrollPane jScrollPane;
	String a[];
	JList row;
	public Mainform() {
		this.init();
	}

	public void init() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();
		setTitle("C语言编译器");
		setSize(750, 480);
		super.setResizable(true);
		super.setLocation(screen.width / 2 - this.getWidth() / 2, screen.height/ 2 - this.getHeight() / 2);
		this.setContentPane(this.createContentPane());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JPanel createContentPane() {
		creatMenu();
		JPanel p = new JPanel(new BorderLayout());
		p.add(BorderLayout.NORTH, creatBottomPane());
		p.add(BorderLayout.CENTER, createcCenterPane());
		return p;
	}

	private Component createcCenterPane() {
		
		jSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true);
		JPanel p = new JPanel(new BorderLayout());
		JLabel label = new JLabel("  源文件：");
		sourseFile = new JTextArea();
		
		Font mf = new Font("宋体",Font.PLAIN,15);
		sourseFile.setFont(mf);
		a=new String[50];
		for(int i=0;i<50;i++){
			a[i]=String.valueOf(i+1);
		}
		row=new JList(a);
		row.setForeground(Color.red);
		
		jScrollPane=new JScrollPane(sourseFile);
		mf = new Font("宋体",Font.PLAIN,14);
		row.setFont(mf);
		jScrollPane.setRowHeaderView(row);
		sourseFile.setForeground(Color.BLACK);
		p.add( label,BorderLayout.NORTH);
		p.add(jScrollPane,BorderLayout.CENTER );
		
		
		JPanel p2 = new JPanel(new BorderLayout());
		JLabel label2 = new JLabel("  控制台：");
		jTextField = new TextArea(1,100);
		jTextField.setEditable(false);
		jTextField.setForeground(Color.BLUE);
		p2.add(label2, BorderLayout.NORTH);
		p2.add(jTextField, BorderLayout.CENTER);
		p2.setEnabled(true);
		
		jSplitPane1.add(p, JSplitPane.TOP);   
        jSplitPane1.add(p2, JSplitPane.BOTTOM);   
        jSplitPane1.setEnabled(true); 
        jSplitPane1.setOneTouchExpandable(true);
        
        jSplitPane1.setDividerSize(4);
		return jSplitPane1;
	}
	
	private void creatMenu(){
		JMenuBar jmb=new JMenuBar();
		JMenu jm1=new JMenu("文件");
		JMenu jm2=new JMenu("编辑");
		JMenu jm3=new JMenu("工程");
		JMenu jm4=new JMenu("视图");
		JMenu jm5=new JMenu("帮助");
		JMenuItem jmimportItem1=new JMenuItem("创建新项目");
		JMenuItem jmimportItem2=new JMenuItem("文件另存为");
		JMenuItem jmimportItem3=new JMenuItem("创建新项目");
		JMenuItem jmimportItem4=new JMenuItem("文件另存为");
		JMenuItem jmimportItem5=new JMenuItem("创建新项目");
		JMenuItem jmimportItem6=new JMenuItem("使用帮助");
		JMenuItem jmimportItem=new JMenuItem("导入源文件");
		JMenuItem jmexitItem=new JMenuItem("退出程序");
		jmimportItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				int ret = chooser.showOpenDialog(new JPanel());
				if (ret == JFileChooser.APPROVE_OPTION) {
					String text;
					try {
						soursePath =chooser.getSelectedFile().getPath();
						text = readFile(soursePath);
						sourseFile.setText(text);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		jmexitItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(-1);
			}
		});
		jm1.add(jmimportItem);
		jm1.add(jmexitItem);
		jm2.add(jmimportItem1);
		jm2.add(jmimportItem2);
		jm4.add(jmimportItem3);
		jm4.add(jmimportItem4);
		jm3.add(jmimportItem5);
		jm5.add(jmimportItem6);
		jmb.add(jm1);
		jmb.add(jm2);
		jmb.add(jm3);
		jmb.add(jm4);
		jmb.add(jm5);
		this.setJMenuBar(jmb);
		
	}
	public void seterrorline(int i){
		try {
			sourseFile.requestFocus();
			int selectionStart = sourseFile.getLineStartOffset(i-1);
			int selectionEnd = sourseFile.getLineEndOffset(i-1);
			sourseFile.setSelectionStart(selectionStart);
			sourseFile.setSelectionEnd(selectionEnd);
			sourseFile.setSelectionColor(Color.green);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private Component creatBottomPane() {
		JPanel p = new JPanel(new FlowLayout());
		JButton bt1 = new JButton("词法分析");
		JButton bt2 = new JButton("语法分析");
		JButton bt3 = new JButton("语义分析");
		JButton bt4 = new JButton("目标代码生成");
		bt1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(sourseFile.getText().equals("")){
					return;
				}
				try {
					lexAnalyse=new LexAnalyse(sourseFile.getText());
					wordListPath = lexAnalyse.outputWordList();
					if(lexAnalyse.isFail()){
						int i=lexAnalyse.errorList.get(0).line;
						seterrorline(i);
					}
					jTextField.setText(readFile(wordListPath));
					jTextField.setCaretPosition(jTextField.getText().length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				InfoFrame inf = new InfoFrame("词法分析", wordListPath);
//
//				inf.setVisible(true);
			}
		});
		bt2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(sourseFile.getText().equals("")){
					return;
				}
			lexAnalyse=new LexAnalyse(sourseFile.getText());
			parser=new Parser(lexAnalyse);
				try {
					parser.grammerAnalyse();
					if(parser.graErrorFlag){
						   int i=parser.errorList.get(0).line;
						   seterrorline(i);
						}
					LL1Path= parser.outputLL1();
					jTextField.setText(readFile(LL1Path));
					jTextField.setCaretPosition(jTextField.getText().length());
					LexAnalyse.typelist.clear();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				InfoFrame inf = new InfoFrame("语法分析", LL1Path);
//				inf.setVisible(true);
			}
		});
		bt3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(sourseFile.getText().equals("")){
					return;
				}
				try {
					lexAnalyse=new LexAnalyse(sourseFile.getText());
					parser=new Parser(lexAnalyse);
					parser.grammerAnalyse();
					fourElementPath=parser.outputFourElem();
					jTextField.setText(readFile(fourElementPath));
					jTextField.setCaretPosition(jTextField.getText().length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				InfoFrame inf = new InfoFrame("四元式", fourElementPath);
//				inf.setVisible(true);
				
			}
		});
		bt4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {



				
				//获取标识符
				lexAnalyse = new LexAnalyse(sourseFile.getText());
				ArrayList<Word> wordList;
				wordList = lexAnalyse.getWordList();
				ArrayList<String> id = new ArrayList<String>();
				for (int i = 0; i < wordList.size(); i++) {
					if(wordList.get(i).type.equals(Word.IDENTIFIER)){					
					if(!id.contains(wordList.get(i).value)){
					id.add(wordList.get(i).value);
					System.out.println(wordList.get(i).value);
					}
					}
				}

//				FourElement temp;
//				for (int i = 0; i < parser.fourElemList.size(); i++) {
//					temp = parser.fourElemList.get(i);
//					System.out.println( temp.id + "(" + temp.op + "," + temp.arg1 + "," + temp.arg2 + "," + temp.result + ")");
//				}
				
				parser = new Parser(lexAnalyse);
				parser.grammerAnalyse();
				Asm asm = new Asm(parser.fourElemList, id, parser.fourElemT);
 
				
				
				 
				try {
					jTextField.setText(readFile(asm.getAsmFile()));
					jTextField.setCaretPosition(jTextField.getText().length());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		p.add(bt1);
		p.add(bt2);
		p.add(bt3);
		p.add(bt4);
		return p;
	}

	public static String readFile(String fileName) throws IOException {
		StringBuilder sbr = new StringBuilder();
		String str;
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
		BufferedReader in = new BufferedReader(isr);
		while ((str = in.readLine()) != null) {
			sbr.append(str).append('\n');
		}
		in.close();
		return sbr.toString();
	}

	public static void main(String[] args) {
		Mainform mf = new Mainform();
		mf.setVisible(true);
		mf.jSplitPane1.setDividerLocation(0.7);
	}
	
}
