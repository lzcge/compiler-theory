package com.compiler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
/**
 * �﷨������
 * @author KB
 *
 */
public class Parser {

	/**
	 * @param args
	 */

	private LexAnalyse lexAnalyse ;//�ʷ�������
	ArrayList<Word>wordList=new ArrayList<Word>();//���ʱ�
	Stack<AnalyseNode>analyseStack=new Stack<AnalyseNode>();//����ջ
	Stack<String>semanticStack=new Stack<String>();//����ջ
	public ArrayList<FourElement>fourElemList=new ArrayList<FourElement>();//��Ԫʽ�б�
	public ArrayList<Error>errorList=new ArrayList<Error>();//������Ϣ�б�
	StringBuffer bf;//����ջ������
	int errorCount=0;//ͳ�ƴ������
	public boolean graErrorFlag=false;//�﷨���������־
	int tempCount=0;//����������ʱ����
	int fourElemCount=0;//ͳ����Ԫʽ����
	AnalyseNode S,B,A,C,X,Y,R,Z,Z1,U,U1,E,E1,H,H1,G,M,D,L,L1,T,T1,F,O,P,Q;
	AnalyseNode ADD_SUB,DIV_MUL,ADD,SUB,DIV,MUL,ASS_F,ASS_R,ASS_Q,ASS_U,TRAN_LF;
	AnalyseNode SINGLE,SINGLE_OP,EQ,EQ_U1,COMPARE,COMPARE_OP,SCANF,PRINTF,IF_FJ,IF_RJ,IF_BACKPATCH_FJ,IF_BACKPATCH_RJ;
	AnalyseNode WHILE_FJ,WHILE_RJ,WHILE_BACKPATCH_FJ,FOR_FJ,FOR_RJ,FOR_BACKPATCH_FJ;
	AnalyseNode top;//��ǰջ��Ԫ��
	Word firstWord;//����������
	String OP=null;
	String ARG1,ARG2,RES;
	Error error;
	//int if_fj,if_rj,while_fj,while_rj,for_fj,for_rj;
	Stack<Integer>if_fj,if_rj,while_fj,while_rj,for_fj,for_rj;//if while for ��ת��ַջ
	Stack<String>for_op=new Stack<String>();
	public ArrayList<String> fourElemT=new ArrayList<String>();
public Parser(){
		
	}
public Parser(LexAnalyse lexAnalyse){
		this.lexAnalyse=lexAnalyse;
		this.wordList=lexAnalyse.wordList;
		init();
	}
private String newTemp(){
	tempCount++;
	fourElemT.add("T"+tempCount);
	return "T"+tempCount;
}
public void init(){
	S=new AnalyseNode(AnalyseNode.NONTERMINAL, "S", null);
	A=new AnalyseNode(AnalyseNode.NONTERMINAL, "A", null);
	B=new AnalyseNode(AnalyseNode.NONTERMINAL, "B", null);
	C=new AnalyseNode(AnalyseNode.NONTERMINAL, "C", null);
	X=new AnalyseNode(AnalyseNode.NONTERMINAL, "X", null);
	Y=new AnalyseNode(AnalyseNode.NONTERMINAL, "Y", null);
	Z=new AnalyseNode(AnalyseNode.NONTERMINAL, "Z", null);
	Z1=new AnalyseNode(AnalyseNode.NONTERMINAL, "Z'", null);
	U=new AnalyseNode(AnalyseNode.NONTERMINAL, "U", null);
	U1=new AnalyseNode(AnalyseNode.NONTERMINAL, "U'", null);
	E=new AnalyseNode(AnalyseNode.NONTERMINAL, "E", null);
	E1=new AnalyseNode(AnalyseNode.NONTERMINAL, "E'", null);
	H=new AnalyseNode(AnalyseNode.NONTERMINAL, "H", null);
	H1=new AnalyseNode(AnalyseNode.NONTERMINAL, "H'", null);
	G=new AnalyseNode(AnalyseNode.NONTERMINAL, "G", null);
	F=new AnalyseNode(AnalyseNode.NONTERMINAL, "F", null);
	D=new AnalyseNode(AnalyseNode.NONTERMINAL, "D", null);
	L=new AnalyseNode(AnalyseNode.NONTERMINAL, "L", null);
	L1=new AnalyseNode(AnalyseNode.NONTERMINAL, "L'", null);
	T=new AnalyseNode(AnalyseNode.NONTERMINAL, "T", null);
	T1=new AnalyseNode(AnalyseNode.NONTERMINAL, "T'", null);
	O=new AnalyseNode(AnalyseNode.NONTERMINAL, "O", null);
	P=new AnalyseNode(AnalyseNode.NONTERMINAL, "P", null);
	Q=new AnalyseNode(AnalyseNode.NONTERMINAL, "Q", null);
	R=new AnalyseNode(AnalyseNode.NONTERMINAL, "R", null);
	ADD_SUB=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ADD_SUB", null);
	ADD=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ADD", null);
	SUB=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SUB", null);
	DIV_MUL=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@DIV_MUL", null);
	DIV=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@DIV", null);
	MUL=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@MUL", null);
	ASS_F=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_F", null);
	ASS_R=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_R", null);
	ASS_Q=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_Q", null);
	ASS_U=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_U", null);
	TRAN_LF=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@TRAN_LF", null);
	SINGLE=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE", null);
	SINGLE_OP=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null);
	EQ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@EQ", null);
	EQ_U1=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@EQ_U'", null);
	COMPARE=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@COMPARE", null);
	COMPARE_OP=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@COMPARE_OP", null);
	IF_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_FJ", null);
	SCANF=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SCANF", null);
	PRINTF=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@PRINTF", null);
IF_RJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_RJ", null);
IF_BACKPATCH_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_BACKPATCH_FJ", null);
IF_BACKPATCH_RJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_BACKPATCH_RJ", null);
 WHILE_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_FJ", null);
WHILE_RJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_RJ", null);
WHILE_BACKPATCH_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_BACKPATCH_FJ", null);
FOR_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_FJ", null);
FOR_RJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_RJ", null);
FOR_BACKPATCH_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_BACKPATCH_FJ", null);
if_fj=new Stack<Integer>();
if_rj=new Stack<Integer>();
while_fj=new Stack<Integer>();
while_rj=new Stack<Integer>();
for_fj=new Stack<Integer>();
for_rj=new Stack<Integer>();
	
}
public void grammerAnalyse(){//LL1�������������﷨����
	if(lexAnalyse.isFail())javax.swing.JOptionPane.showMessageDialog(null, "�ʷ�����δͨ�������ܽ����﷨����");
	bf=new StringBuffer();
	int gcount=0;
	error=null;
	analyseStack.add(0,S);
	analyseStack.add(1,new AnalyseNode(AnalyseNode.END, "#", null));
	semanticStack.add("#");
	while(!analyseStack.empty()&&!wordList.isEmpty()){
		//System.out.println(fourElemCount);
		bf.append('\n');
		bf.append("����"+gcount+"\t");
		if(gcount++>10000){
			graErrorFlag=true;
			break;
		}
//		if(){
//			System.out.println("EEEEEE");
//			break;
//		}
			
		top=analyseStack.get(0);//��ǰջ��Ԫ��
		firstWord=wordList.get(0);//����������
		if(firstWord.value.equals("#")//��������
				&&top.name.equals("#")){
			bf.append("\n");
			analyseStack.remove(0);
			wordList.remove(0);
			
		}
		else if(top.name.equals("#")){
			analyseStack.remove(0);
			graErrorFlag=true;
			break;
			
		}
		else if(AnalyseNode.isTerm(top)){//�ռ���ʱ�Ĵ���
			 termOP(top.name);
		}else if(AnalyseNode.isNonterm(top)){
			nonTermOP(top.name);	
		}else if(top.type.equals(AnalyseNode.ACTIONSIGN)){//ջ���Ƕ�������ʱ�Ĵ���
			actionSignOP();
		}
		
		bf.append("��ǰ����ջ:");
		for(int i=0;i<analyseStack.size();i++){
			bf.append(analyseStack.get(i).name);
			//System.out.println(analyseStack.get(i).name);
		}
		bf.append("\t").append("�������Ŵ���");
		for(int j=0;j<wordList.size();j++){
			bf.append(wordList.get(j).value);
		}
		bf.append("\t").append("����ջ:");
		for(int k=semanticStack.size()-1;k>=0;k--){
			bf.append(semanticStack.get(k));
		}
	}
}
private void termOP(String term) {
	if (
		(firstWord.type.equals(Word.INT_CONST)||firstWord.type.equals(Word.CHAR_CONST))
		||(firstWord.type.equals(Word.OPERATOR)&&term.equals(firstWord.value))
		||(firstWord.type.equals(Word.BOUNDARYSIGN)&&term.equals(firstWord.value))
		||(firstWord.type.equals(Word.KEY)&&term.equals(firstWord.value))
		|| (term.equals("id") && firstWord.type.equals(Word.IDENTIFIER))
		||(term.equals("\"%d\"")&&firstWord.type.equals(Word.IDENTIFIER))
		){
		System.out.println("name:"+term+"   "+"succeful");
		analyseStack.remove(0);
		wordList.remove(0);
	} 
	else {
		errorCount++;
		analyseStack.remove(0);
		wordList.remove(0);
		error = new Error(errorCount, "�﷨����", firstWord.line, firstWord);
		System.out.println("name:"+term+"   "+"defeat"+"  "+errorCount);
		errorList.add(error);
		graErrorFlag = true;
	}
}

private void nonTermOP(String nonTerm){
	if(nonTerm.equals("Z'"))nonTerm="1";
	if(nonTerm.equals("U'"))nonTerm="2";
	if(nonTerm.equals("E'"))nonTerm="3";
	if(nonTerm.equals("H'"))nonTerm="4";
	if(nonTerm.equals("L'"))nonTerm="5";
	if(nonTerm.equals("T'"))nonTerm="6";
	switch(nonTerm.charAt(0)){//ջ��Ϊ���ս������
	//N:S,B,A,C,,X,R,Z,Z��,U,U��,E,E��,H,H��,G,M,D,L,L��,T,T��,F,O,P,Q
	case 'S':
	if(firstWord.value.equals("void")){
		analyseStack.remove(0);
		analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "void", null));
		analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "main", null));
		analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
		analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
		analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
		analyseStack.add(5,A);
		analyseStack.add(6,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
	}else{
		errorCount++;
		analyseStack.remove(0);
		wordList.remove(0);
		error=new Error(errorCount,"������û�з���ֵ",firstWord.line,firstWord);
		errorList.add(error);	
		graErrorFlag=true;
	}
		break;
	case 'A':
		if(firstWord.value.equals("int")||firstWord.value.equals("char")
				||firstWord.value.equals("bool")){
			analyseStack.remove(0);
			analyseStack.add(0,C);
			analyseStack.add(1,A);
		}else if(firstWord.value.equals("printf")){
			analyseStack.remove(0);
			analyseStack.add(0,C);
			analyseStack.add(1,A);
			
		}
		else if(firstWord.value.equals("scanf")){
			analyseStack.remove(0);
			analyseStack.add(0,C);
			analyseStack.add(1,A);
		}
		else if(firstWord.value.equals("if")){
			analyseStack.remove(0);
			analyseStack.add(0,C);
			analyseStack.add(1,A);
		}
		else if(firstWord.value.equals("while")){
			analyseStack.remove(0);
			analyseStack.add(0,C);
			analyseStack.add(1,A);
		}
		else if(firstWord.value.equals("for")){
			analyseStack.remove(0);
			analyseStack.add(0,C);
			analyseStack.add(1,A);
			}
		else if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,C);
			analyseStack.add(1,A);
		}else{
			analyseStack.remove(0);
		}
		break;
	
	case 'B':
 if (firstWord.value.equals("printf")) {
		analyseStack.remove(0);
		analyseStack.add(0, new AnalyseNode(AnalyseNode.TERMINAL,"printf", null));
		analyseStack.add(1, new AnalyseNode(AnalyseNode.TERMINAL, "(",null));
		analyseStack.add(2, new AnalyseNode(AnalyseNode.TERMINAL, "\"%d\"",null));
		analyseStack.add(3, new AnalyseNode(AnalyseNode.TERMINAL, ",",null));
		analyseStack.add(4,F);
		analyseStack.add(5, new AnalyseNode(AnalyseNode.TERMINAL, ")",null));
		analyseStack.add(6,PRINTF);
		analyseStack.add(7, A);
		analyseStack.add(8, new AnalyseNode(AnalyseNode.TERMINAL, ";",null));
	}
 
		else if (firstWord.value.equals("scanf")) {
			analyseStack.remove(0);
			analyseStack.add(0, new AnalyseNode(AnalyseNode.TERMINAL,
					"scanf", null));
			analyseStack.add(1, new AnalyseNode(AnalyseNode.TERMINAL, "(",
					null));
			analyseStack.add(2, new AnalyseNode(AnalyseNode.TERMINAL, "\"%d\"",
					null));
			analyseStack.add(3, new AnalyseNode(AnalyseNode.TERMINAL, ",",
					null));
			analyseStack.add(4, new AnalyseNode(AnalyseNode.TERMINAL, "&",
					null));
			analyseStack.add(5,F);
			analyseStack.add(6, new AnalyseNode(AnalyseNode.TERMINAL, ")",
					null));
			analyseStack.add(7,SCANF);
			analyseStack.add(8, A);
			analyseStack.add(9, new AnalyseNode(AnalyseNode.TERMINAL, ";",
					null));
		}
		else if(firstWord.value.equals("if")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "if", null));
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
			analyseStack.add(2,G);
			analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
			analyseStack.add(4,IF_FJ);
			analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
			analyseStack.add(6,A);
			analyseStack.add(7,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
			analyseStack.add(8,IF_BACKPATCH_FJ);
			analyseStack.add(9,IF_RJ);
			analyseStack.add(10,new AnalyseNode(AnalyseNode.TERMINAL, "else", null));
			analyseStack.add(11,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
			analyseStack.add(12,A);
			analyseStack.add(13,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
			analyseStack.add(14,IF_BACKPATCH_RJ);
		}
		else if(firstWord.value.equals("while")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "while", null));
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
			analyseStack.add(2,G);
			analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
			analyseStack.add(4,WHILE_FJ);
			analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
			analyseStack.add(6,A);
			analyseStack.add(7,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
			analyseStack.add(8,WHILE_RJ);
			analyseStack.add(9,WHILE_BACKPATCH_FJ);
		}
		else if(firstWord.value.equals("for")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "for", null));
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
			analyseStack.add(2,Y);
			analyseStack.add(3,Z);
			analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
			analyseStack.add(5,G);
			analyseStack.add(6,FOR_FJ);
			analyseStack.add(7,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
			analyseStack.add(8,Q);
			analyseStack.add(9,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
			analyseStack.add(10,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
			analyseStack.add(11,A);
			analyseStack.add(12,SINGLE);
			analyseStack.add(13,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
			analyseStack.add(14,FOR_RJ);
			analyseStack.add(15,FOR_BACKPATCH_FJ);
		}
		else{
			analyseStack.remove(0);
		}
		break;
	case 'C':
		
			analyseStack.remove(0);
			analyseStack.add(0,X);
			analyseStack.add(1,B);
			analyseStack.add(2,R);
		break;
	case 'X':
		if(firstWord.value.equals("int")||firstWord.value.equals("char")||firstWord.value.equals("bool")){
			analyseStack.remove(0);
			analyseStack.add(0,Y);
			analyseStack.add(1,Z);
			analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
		}else{
			analyseStack.remove(0);
		}
		break;
	case 'Y':
		if(firstWord.value.equals("int")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "int", null));
		}
		else if(firstWord.value.equals("char")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "char", null));
		}
		else if(firstWord.value.equals("bool")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "bool", null));
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"�Ƿ���������",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case 'Z':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,U);
			analyseStack.add(1,Z1);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"�Ƿ���ʶ��",firstWord.line,firstWord);
			errorList.add(error);
			graErrorFlag=true;
		}
		break;
	case '1'://z'
		if(firstWord.value.equals(",")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, ",", null));
			analyseStack.add(1,Z);
		}
		else{
			analyseStack.remove(0);
		}
		break;
	case 'U':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,ASS_U);
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
			analyseStack.add(2,U1);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"�Ƿ���ʶ��",firstWord.line,firstWord);
			errorList.add(error);
			graErrorFlag=true;
		}
		break;
	case '2'://U'
		if(firstWord.value.equals("=")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "=", null));
			analyseStack.add(1,L);
			analyseStack.add(2,EQ_U1);
		}
		else{			
			analyseStack.remove(0);
		}
		break;
	case 'R':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_R", null));
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
			analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "=", null));
			analyseStack.add(3,L);
			analyseStack.add(4,EQ);
			analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
		}
		else{
			analyseStack.remove(0);
		}
		break;
	case 'P':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
		}else if(firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
		}else if(firstWord.type.equals(Word.CHAR_CONST)){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "ch", null));
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"�����������������",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case 'E':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,H);
			analyseStack.add(1,E1);
		}else if(firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			analyseStack.add(0,H);
			analyseStack.add(1,E1);
		}else if(firstWord.value.equals("(")){
			analyseStack.remove(0);
			analyseStack.add(0,H);
			analyseStack.add(1,E1);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case '3'://E'
		if(firstWord.value.equals("&&")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "&&", null));
			analyseStack.add(1,E);
		}else {
			analyseStack.remove(0);
		}
		break;
	case 'H':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,G);
			analyseStack.add(1,H1);
		}else if(firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			analyseStack.add(0,G);
			analyseStack.add(1,H1);
		}else if(firstWord.value.equals("(")){
			analyseStack.remove(0);
			analyseStack.add(0,G);
			analyseStack.add(1,H1);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case '4'://H'
		if(firstWord.value.equals("||")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "||", null));
			analyseStack.add(1,E);
		}else {
			analyseStack.remove(0);
		}
		break;
	case 'D':
		if(firstWord.value.equals("==")){
			analyseStack.remove(0);
			analyseStack.add(0,COMPARE_OP);
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "==", null));
		}else if(firstWord.value.equals("!=")){
			analyseStack.remove(0);
			analyseStack.add(0,COMPARE_OP);
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "!=", null));
			
		}else if(firstWord.value.equals(">")){
			analyseStack.remove(0);
			analyseStack.add(0,COMPARE_OP);
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, ">", null));
		}else if(firstWord.value.equals("<")){
			analyseStack.remove(0);
			analyseStack.add(0,COMPARE_OP);
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "<", null));
		}else if(firstWord.value.equals(">=")) {
			analyseStack.remove(0);
			analyseStack.add(0, COMPARE_OP);
			analyseStack.add(1, new AnalyseNode(AnalyseNode.TERMINAL, ">=",null));
	    }else if(firstWord.value.equals("<=")) {
			analyseStack.remove(0);
			analyseStack.add(0, COMPARE_OP);
			analyseStack.add(1, new AnalyseNode(AnalyseNode.TERMINAL, "<=",	null));
	    }else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"�Ƿ������",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case 'G':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,F);
			analyseStack.add(1,D);
			analyseStack.add(2,F);
			analyseStack.add(3,COMPARE);
		}else if(firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			analyseStack.add(0,F);
			analyseStack.add(1,D);
			analyseStack.add(2,F);
			analyseStack.add(3,COMPARE);
		}
		else if(firstWord.value.equals("(")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
			analyseStack.add(1,E);
			analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
		}
		else if(firstWord.value.equals("!")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "!", null));
			analyseStack.add(1,E);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"���ܽ�������������������ͻ����Ų�ƥ��",firstWord.line,firstWord);
			errorList.add(error);
			graErrorFlag=true;
		}
		break;
	case 'L':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,T);
			analyseStack.add(1,L1);
			analyseStack.add(2,ADD_SUB);
		}else if(firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			analyseStack.add(0,T);
			analyseStack.add(1,L1);
			analyseStack.add(2,ADD_SUB);
		}
		else if(firstWord.value.equals("(")){
			analyseStack.remove(0);
			analyseStack.add(0,T);
			analyseStack.add(1,L1);
			analyseStack.add(2,ADD_SUB);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"���ܽ�������������������ͻ����Ų�ƥ��",firstWord.line,firstWord);
			errorList.add(error);
			graErrorFlag=true;
		}
		break;
	case '5'://l'
		if(firstWord.value.equals("+")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "+", null));
			analyseStack.add(1,L);
			analyseStack.add(2,ADD);
		}
		else if(firstWord.value.equals("-")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "-", null));
			analyseStack.add(1,L);
			analyseStack.add(2,SUB);
		}else {
			analyseStack.remove(0);
		}
		break;

	case 'T':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,F);
			analyseStack.add(1,T1);
			analyseStack.add(2,DIV_MUL);
		}else if(firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			analyseStack.add(0,F);
			analyseStack.add(1,T1);
			analyseStack.add(2,DIV_MUL);
		}
		else if(firstWord.value.equals("(")){
			analyseStack.remove(0);
			analyseStack.add(0,F);
			analyseStack.add(1,T1);
			analyseStack.add(2,DIV_MUL);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case '6'://T'
		if(firstWord.value.equals("*")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "*", null));
			analyseStack.add(1,T);
			analyseStack.add(2,MUL);
		}
		else if(firstWord.value.equals("/")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "/", null));
			analyseStack.add(1,T);
			analyseStack.add(2,DIV);
		}else {
			analyseStack.remove(0);
		}
		break;
	case 'F':
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,ASS_F);
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
		}else if(firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			analyseStack.add(0,ASS_F);
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
		}else if(firstWord.value.equals("(")){
			analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
			analyseStack.add(1,L);
			analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
			analyseStack.add(3,TRAN_LF);
		}
		else{
			//errorCount++;
			analyseStack.remove(0);
		//	wordList.remove(0);
			//error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
			//errorList.add(error);
			//graErrorFlag=true;
		}
		break;
	case 'O':
		if(firstWord.value.equals("++")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null));
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "++", null));
		}else if(firstWord.value.equals("--")){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null));
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "--", null));

		}else {
			analyseStack.remove(0);
		}
		break;
	case 'Q'://Q
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_Q", null));
			analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
			analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "O", null));
		}else {
			analyseStack.remove(0);
		}
		break;
		
	}
}
private void actionSignOP(){
	if(top.name.equals("@ADD_SUB")){
		if(OP!=null&&(OP.equals("+")||OP.equals("-"))){
			ARG2=semanticStack.pop();
			ARG1=semanticStack.pop();
			RES=newTemp();
			//fourElemCount++;
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
			fourElemList.add(fourElem);
			L.value=RES;
			semanticStack.push(L.value);
			OP=null;
		}
		analyseStack.remove(0);
		
	}else if(top.name.equals("@ADD")){
		OP="+";
		analyseStack.remove(0);
	}else if(top.name.equals("@SUB")){
		OP="-";
		analyseStack.remove(0);
	}else if(top.name.equals("@DIV_MUL")){
		if(OP!=null&&(OP.equals("*")||OP.equals("/"))){
			ARG2=semanticStack.pop();
			ARG1=semanticStack.pop();
			RES=newTemp();
			//fourElemCount++;
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
			fourElemList.add(fourElem);
			T.value=RES;
			semanticStack.push(T.value);
			OP=null;
		}
		analyseStack.remove(0);
	}
	else if(top.name.equals("@DIV")){
		OP="/";
		analyseStack.remove(0);
		}
	else if(top.name.equals("@MUL")){
		OP="*";
		analyseStack.remove(0);
	}else if(top.name.equals("@TRAN_LF")){
		F.value=L.value;
		//semanticStack.push(F.value);
		analyseStack.remove(0);
	}else if(top.name.equals("@ASS_F")){
		F.value=firstWord.value;
		if(!LexAnalyse.getTypelist().contains(F.value)&&(F.value.charAt(0)>64)){
//			System.out��println();
			error=new Error(errorCount,"û�ж���      ",firstWord.line,firstWord);
            errorList.add(error);
            graErrorFlag=true;
		};
//		for (String x : LexAnalyse.getChar()) { 
//            if(U.value.equals(x)) {
//             error=new Error(errorCount,"�ظ�����      "+x,firstWord.line,firstWord);
//                errorList.add(error);
//                graErrorFlag=true;
//                }
// }
		//System.out.println(F.value);
		semanticStack.push(F.value);
		analyseStack.remove(0);
	}else if(top.name.equals("@ASS_R")){
		R.value=firstWord.value;
		if(!LexAnalyse.getTypelist().contains(R.value)&&(R.value.charAt(0)>64)){
			error=new Error(errorCount,"û�ж���      ",firstWord.line,firstWord);
            errorList.add(error);
            graErrorFlag=true;
		};
		semanticStack.push(R.value);
		analyseStack.remove(0);
	}else if(top.name.equals("@ASS_Q")){
		Q.value=firstWord.value;
		semanticStack.push(Q.value);
		analyseStack.remove(0);
	}
	else if(top.name.equals("@ASS_U")){
		 U.value=firstWord.value;
		 for (String x : semanticStack) { 
		            if(U.value.equals(x)) {
		             error=new Error(errorCount,"�ظ�����      "+x,firstWord.line,firstWord);
		                errorList.add(error);
		                graErrorFlag=true;
		                }
		 }
		semanticStack.push(U.value);
		analyseStack.remove(0);
	}else if(top.name.equals("@SINGLE")){
		if(for_op.peek()!=null){
			ARG1=semanticStack.pop();
			RES=ARG1;
			//fourElemCount++;
			FourElement fourElem=new FourElement(++fourElemCount,for_op.pop(),ARG1,"/",RES);
			fourElemList.add(fourElem);
		}
		analyseStack.remove(0);
	}else if(top.name.equals("@SINGLE_OP")){
		for_op.push(firstWord.value);
		analyseStack.remove(0);
	}else if(top.name.equals("@EQ")){
		OP="=";
		ARG1=semanticStack.pop();
		RES=semanticStack.pop();;
		//fourElemCount++;
		FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/",RES);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}
	else if(top.name.equals("@EQ_U'")){
		OP="=";
		ARG1=semanticStack.pop();
		RES=semanticStack.pop();;
		//fourElemCount++;
		FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/",RES);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@COMPARE")){//???
		ARG2=semanticStack.pop();
		OP=semanticStack.pop();
		ARG1=semanticStack.pop();
		RES=newTemp();
		//fourElemCount++;
		FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
		fourElemList.add(fourElem);
		G.value=RES;
		semanticStack.push(G.value);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@COMPARE_OP")){//???
		D.value=firstWord.value;
		semanticStack.push(D.value);
		analyseStack.remove(0);
	}else if(top.name.equals("@IF_FJ")){
		OP="FJ";
		ARG1=semanticStack.pop();
		FourElement fourElem=new FourElement(++fourElemCount,OP,RES,ARG1,"/");
		if_fj.push(fourElemCount);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@SCANF")){
		OP="SCANF";
		ARG1=semanticStack.pop();
		FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/","/");
//		if_fj.push(fourElemCount);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@PRINTF")){
		OP="PRINTF";
		ARG1=semanticStack.pop();
		FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/","/");
//		if_fj.push(fourElemCount);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@IF_BACKPATCH_FJ")){
		backpatch(if_fj.pop(), fourElemCount+2);
		analyseStack.remove(0);
	}else if(top.name.equals("@IF_RJ")){
		OP="RJ";
		FourElement fourElem=new FourElement(++fourElemCount,OP,"/","/","/");
		if_rj.push(fourElemCount);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@IF_BACKPATCH_RJ")){
		backpatch(if_rj.pop(), fourElemCount+1);
		analyseStack.remove(0);
	}else if(top.name.equals("@WHILE_FJ")){
		OP="FJ";
		ARG1=semanticStack.pop();
		FourElement fourElem=new FourElement(++fourElemCount,OP,"/",ARG1,"/");
		while_fj.push(fourElemCount);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@WHILE_RJ")){
		OP="RJ";
		RES=(while_fj.peek()-1)+"";
		FourElement fourElem=new FourElement(++fourElemCount,OP,RES,"/","/");
		for_rj.push(fourElemCount);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@WHILE_BACKPATCH_FJ")){
		backpatch(while_fj.pop(), fourElemCount+1);
		analyseStack.remove(0);
	}else if(top.name.equals("@FOR_FJ")){
		OP="FJ";
		ARG1=semanticStack.pop();
		FourElement fourElem=new FourElement(++fourElemCount,OP,"/",ARG1,"/");
		for_fj.push(fourElemCount);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@FOR_RJ")){
		OP="RJ";
		RES=(for_fj.peek()-1)+"";
		FourElement fourElem=new FourElement(++fourElemCount,OP,RES,"/","/");
		for_rj.push(fourElemCount);
		fourElemList.add(fourElem);
		OP=null;
		analyseStack.remove(0);
	}else if(top.name.equals("@FOR_BACKPATCH_FJ")){
		backpatch(for_fj.pop(), fourElemCount+1);
		analyseStack.remove(0);
	}
	
	
}
private void backpatch(int i,int res){
FourElement temp=fourElemList.get(i-1);
temp.arg1=res+"";
fourElemList.set(i-1, temp);
}
public String outputLL1() throws IOException{
	//grammerAnalyse();
	File file=new File("./result/");
	if(!file.exists()){
		file.mkdirs();
		file.createNewFile();//�������ļ������ھʹ�����
	}
	String path=file.getAbsolutePath();
	FileOutputStream fos=new FileOutputStream(path+"/LL1.txt");  
	BufferedOutputStream bos=new BufferedOutputStream(fos); 
	OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
	PrintWriter pw1=new PrintWriter(osw1);
	pw1.println(bf.toString());
	bf.delete(0, bf.length());
	if(graErrorFlag){
		Error error;
		pw1.println("������Ϣ���£�");
	
	pw1.println("�������\t������Ϣ\t���������� \t���󵥴�");
	for(int i=0;i<errorList.size();i++){
		error=errorList.get(i);
		pw1.println(error.id+"\t"+error.info+"\t\t"+error.line+"\t"+error.word.value);
	}
	}else {
		pw1.println("�﷨����ͨ����");
	}
	pw1.close();
	return path+"/LL1.txt";
}
public String outputFourElem() throws IOException{
	
	File file=new File("./result/");
	if(!file.exists()){
		file.mkdirs();
		file.createNewFile();//�������ļ������ھʹ�����
	}
	String path=file.getAbsolutePath();
	FileOutputStream fos=new FileOutputStream(path+"/FourElement.txt");  
	BufferedOutputStream bos=new BufferedOutputStream(fos); 
	OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
	PrintWriter pw1=new PrintWriter(osw1);
	pw1.println("���ɵ���Ԫʽ����");
	pw1.println("��ţ�OP,ARG1��ARG2��RESULT��");
	FourElement temp;
	for(int i=0;i<fourElemList.size();i++){
		temp=fourElemList.get(i);
		pw1.println(temp.id+"("+temp.op+","+temp.arg1+","+temp.arg2+","+temp.result+")");
	}
	pw1.close();
	
	return path+"/FourElement.txt";
}
	public static void main(String[] args) {

	}

}
