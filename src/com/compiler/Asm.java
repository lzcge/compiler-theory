package com.compiler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *  
 * 
 *  ������������
 */
public class Asm {
	
	private ArrayList<String> asmCodeList=new ArrayList<String>();
	//private File file = new File("./result/");
	private ArrayList<FourElement> fourElemList;
	private ArrayList<String> id;
	/**
	 * @param fourElemList //��Ԫʽ
	 */
	public Asm(ArrayList<FourElement> fourElemList,ArrayList<String> id, ArrayList<String> fourElemT) {
		// TODO Auto-generated constructor stub
		this.fourElemList=fourElemList;
		this.id=id;
		asmHead(id, fourElemT);//���ͷ��
		asmCode(fourElemList);//���ɴ���δ���
		asmTail();//���β��
		
		for(int i=0;i<asmCodeList.size();i++)
			System.out.println(asmCodeList.get(i));
		
//		try {
//			makdir();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
	
	/**
	 * ��ȡasm�ļ���ַ
	 * @return
	 * @throws IOException
	 */
	public String getAsmFile() throws IOException {
		
		File file = new File("./result/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// �������ļ������ھʹ�����
		}
		String path = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(path + "/c_to_asm.asm");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		
		for(int i=0;i<asmCodeList.size();i++)
			pw1.println(asmCodeList.get(i));
		
		pw1.close();
		return path + "/c_to_asm.asm";
		
	}
	
 
	
	/**
	 * �ж��������λ�á�
	 * @param fourElemList
	 * @return
	 */
	public int endPosition(ArrayList<FourElement> fourElemList) {

		int num = fourElemList.size();
		int position;
		for (int i = num - 1; i >= 0; i--) {

			if (!fourElemList.get(i).op.equals("PRINTF")) {

				return i;
			}

		}

		return 0;

	}

	
	
	/**
	 * ���ͷ��
	 * @param id
	 * @param fourElemT
	 */
	public void asmHead(ArrayList<String> id, ArrayList<String> fourElemT) {

		//������ݶδ��� 
		asmCodeList.add("datasg segment");

		asmCodeList.add("tem db 6,7 dup  (0)");
		for (int i = 0; i < id.size(); i++) {
			asmCodeList.add(id.get(i) + " dw 0");
		}
         
		
		for (int j = 0; j < fourElemT.size(); j++) {
			asmCodeList.add(fourElemT.get(j) + " dw 0");
		}
		
		
		for (int i = 0; i < fourElemList.size(); i++) {
			if(fourElemList.get(i).op.equals("PRINTF")){
			 
				asmCodeList.add("printf_"+fourElemList.get(i).arg1+(i+1)+" db '"+fourElemList.get(i).arg1+":$'");
				
			}else if(fourElemList.get(i).op.equals("SCANF")){
				asmCodeList.add("scanf_"+fourElemList.get(i).arg1+(i+1)+" db 'input "+fourElemList.get(i).arg1+":$'");
			}
		}
		

			asmCodeList.add("datasg ends");
			asmCodeList.add("codesg segment");
			asmCodeList.add("assume cs:codesg,ds:datasg");
			asmCodeList.add("start:");
			asmCodeList.add("MOV AX,datasg");
			asmCodeList.add("MOV DS,AX");
		 
		    
	}
	
	
	
	/**
	 *  ���ɴ���δ���
	 * @param fourElemList
	 */
	public void asmCode(ArrayList<FourElement> fourElemList) {
		 
		int position=endPosition(fourElemList);//��ȡ����ĳ���λ��
		System.out.println("λ��"+position);
		
		for (int i = 0; i < fourElemList.size(); i++) {
			
			// asmCodeList.add((fourElemList.size()+1)+""+"----"+fourElemList.get(i).arg1);
			// asmCodeList.add("13".equals((fourElemList.size()+1+"").toString()));
			
			
			int flag = 0;//�Ƿ���ת������ĳ���λ��TheEnd���������㡣
			if (fourElemList.get(i).arg1.equals(position + 2 + "")) {
			 		
				  if(fourElemList.get(i).op.equals("FJ")||fourElemList.get(i).op.equals("RJ")){
				      fourElemList.get(i).arg1="TheEnd";
			    	  flag = 1;
				  }
			} 
			
 
			if (fourElemList.get(i).op.equals("=")) {

				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg1);
				asmCodeList.add("mov " + fourElemList.get(i).result + ", AX");


			} else if (fourElemList.get(i).op.equals("+")) {

				asmCodeList.add("add AX, " + fourElemList.get(i).arg2);
				asmCodeList.add("mov " + fourElemList.get(i).result + ", AX");
				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg1);
			}else if (fourElemList.get(i).op.equals("++")) {

				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg1);
				asmCodeList.add("add AX, 1");
				asmCodeList.add("mov " + fourElemList.get(i).result + ", AX");
			} else if (fourElemList.get(i).op.equals("-")) {

				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg1);
				asmCodeList.add("sub AX, " + fourElemList.get(i).arg2);
				asmCodeList.add("mov " + fourElemList.get(i).result + ", AX");
			} else if (fourElemList.get(i).op.equals("*")) {

				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg1);
				asmCodeList.add("mov BX," + fourElemList.get(i).arg2);
				asmCodeList.add("mul BX");
				asmCodeList.add("mov " + fourElemList.get(i).result + ", AX");
			} else if (fourElemList.get(i).op.equals("/")) {

				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg1);
				asmCodeList.add("mov BX," + fourElemList.get(i).arg2);
				asmCodeList.add("div BL");
				asmCodeList.add("mov ah,0h");
				asmCodeList.add("mov " + fourElemList.get(i).result + ", Ax");
			}

			else if (fourElemList.get(i).op.equals("FJ")) {

				if (flag == 1) {//���=1����TheEnd,������㡣
					//jnc:���ڻ��������ת
					asmCodeList.add("L" + (i + 1) + ": jnc " + fourElemList.get(i).arg1+";���ڵ�������ת");
				} else {
					asmCodeList.add("L" + (i + 1) + ": jnc L" + fourElemList.get(i).arg1+";���ڵ�������ת");

				}

			} else if (fourElemList.get(i).op.equals("RJ")) {

				if (flag == 1) {//���=1����TheEnd,������㡣
					asmCodeList.add("L" + (i + 1) + ": jmp " + fourElemList.get(i).arg1);
				} else {
					asmCodeList.add("L" + (i + 1) + ": jmp L" + fourElemList.get(i).arg1);

				}

			} else if (  fourElemList.get(i).op.equals("<")) {
				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg1);
				asmCodeList.add("sub AX, " + fourElemList.get(i).arg2);
				 
			}else if (fourElemList.get(i).op.equals(">") ){
				
				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg2);
				asmCodeList.add("sub AX, " + fourElemList.get(i).arg1);
				
			}else if (fourElemList.get(i).op.equals(">=") ){
				
				//asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg2);
				//asmCodeList.add("sub AX, " + fourElemList.get(i).arg1);
				
				
				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg1);
				asmCodeList.add("add AX, 1");
				asmCodeList.add( "mov BX ,AX");
				asmCodeList.add(" mov AX, " + fourElemList.get(i).arg2);
				asmCodeList.add("sub AX, BX");
				
				
			}else if (fourElemList.get(i).op.equals("<=") ){
				asmCodeList.add("L" + (i + 1) + ": mov AX, " + fourElemList.get(i).arg2);
				asmCodeList.add("add AX, 1");
				asmCodeList.add( "mov BX ,AX");
				asmCodeList.add(" mov AX, " + fourElemList.get(i).arg1);
				asmCodeList.add("sub AX, BX");
				
			}
			
			else if (fourElemList.get(i).op.equals("PRINTF")) {
				asmCodeList.add("\n");
				asmCodeList.add(";PRINTF");
				asmCodeList.add("L" + (i + 1) + ":");
				asmCodeList.add("lea dx,printf_"+fourElemList.get(i).arg1+(i+1));
				asmCodeList.add("mov ah,9");
				asmCodeList.add("int 21h");
				
				asmCodeList.add("mov ax,"+fourElemList.get(i).arg1);
				asmCodeList.add("xor cx,cx");
				asmCodeList.add("mov bx,10");
				asmCodeList.add("PT0"+(i+1)+":xor dx,dx");
				asmCodeList.add("div bx");
				asmCodeList.add("or dx,0e30h;0e:��ʾ�ַ�");
				asmCodeList.add("push dx");
				asmCodeList.add("inc cx");
				asmCodeList.add("cmp ax,0;ZF=1��AX=0,ZF=0��AX��=0");
				asmCodeList.add("jnz PT0"+(i+1)+";���ʱ��ת");
				asmCodeList.add("PT1"+(i+1)+":pop ax");
				asmCodeList.add("int 10h;��ʾһ���ַ�");
				asmCodeList.add("loop PT1"+(i+1));
				asmCodeList.add("mov ah,0 ");
				asmCodeList.add(";int 16h ;�����ж�"); 

				asmCodeList.add(";����"); 
				asmCodeList.add("mov dl,0dh"); 
				asmCodeList.add("mov ah,2"); 
				asmCodeList.add("int 21h"); 
				asmCodeList.add("mov dl,0ah"); 
				asmCodeList.add("mov ah,2"); 
				asmCodeList.add("int 21h"); 
				asmCodeList.add("\n");
				 
			}else if (fourElemList.get(i).op.equals("SCANF")) {
				asmCodeList.add("L" + (i + 1) + ":");
				
				asmCodeList.add("\n");
				asmCodeList.add(";SCANF");
				 
				
				
				asmCodeList.add("lea dx,scanf_"+fourElemList.get(i).arg1+(i+1));
				asmCodeList.add("mov ah,9");
				asmCodeList.add("int 21h");
				
				asmCodeList.add(";�����ж�");
				asmCodeList.add("mov al,0h;");
				asmCodeList.add("mov tem[1],al;");
				asmCodeList.add("lea dx,tem;");
				asmCodeList.add(" mov ah,0ah");
				asmCodeList.add("int 21h");
				asmCodeList.add(";������������ݣ�����ֵ������");
				asmCodeList.add("mov cl,0000h;");
				asmCodeList.add("mov al,tem[1];");
				asmCodeList.add("sub al,1;");
				asmCodeList.add("mov cl,al;");
				
				asmCodeList.add("mov ax,0000h;");
				asmCodeList.add("mov bx,0000h;");
				
				asmCodeList.add("mov al,tem[2];");
				asmCodeList.add("sub al,30h;");
				asmCodeList.add("mov "+fourElemList.get(i).arg1+",ax;");
				
				
				asmCodeList.add("mov ax,cx");
				asmCodeList.add("sub ax,1");
				asmCodeList.add("jc inputEnd"+(i+1)+";С������ת");
				
				asmCodeList.add(";");
				asmCodeList.add("MOV SI,0003H;");
				
				
				asmCodeList.add("ln"+(i+1)+":mov bx,10;");
				asmCodeList.add("mov ax,"+fourElemList.get(i).arg1+";");
				
				asmCodeList.add("mul bx;");
				asmCodeList.add("mov "+fourElemList.get(i).arg1+",ax;");
				asmCodeList.add("mov ax,0000h;");
				asmCodeList.add("mov al,tem[si]");
				asmCodeList.add("sub al,30h;");
				asmCodeList.add("add ax,"+fourElemList.get(i).arg1+";");
				asmCodeList.add("mov "+fourElemList.get(i).arg1+",ax");
				asmCodeList.add("INC SI");
				asmCodeList.add("loop ln"+(i+1));
				asmCodeList.add("inputEnd"+(i+1)+": nop");
				asmCodeList.add("");
				asmCodeList.add("");
		 
				asmCodeList.add(";����"); 
				asmCodeList.add("mov dl,0dh"); 
				asmCodeList.add("mov ah,2"); 
				asmCodeList.add("int 21h"); 
				asmCodeList.add("mov dl,0ah"); 
				asmCodeList.add("mov ah,2"); 
				asmCodeList.add("int 21h"); 
				asmCodeList.add("\n");
				
				 
			}
			
			if(i==position){ 
				asmCodeList.add("TheEnd:nop");//��������ĳ���λ��
			}
		 
		}
		
		
		
		
	}



	
	
	
	/**
	 * ���β��
	 */
	public void asmTail() {

		
		asmCodeList.add("mov ax,4c00h; int 21h��4ch���жϣ���ȫ�˳�����");
		asmCodeList.add("int 21h;����ϵͳ�ж�");
		asmCodeList.add("codesg ends");
		asmCodeList.add("end start");

	}
	
	
	
	
}
