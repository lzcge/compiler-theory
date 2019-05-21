package com.compiler;

import java.util.ArrayList;

/**
 * ����ջ�ڵ���
 * @author KB
 *	String type;//�ڵ�����
	String name;//�ڵ���
	Object value;//�ڵ�ֵ
 */
public class AnalyseNode {
	public final static String NONTERMINAL="���ս��";
	public final static String TERMINAL="�ս��";
	public final static String ACTIONSIGN="������";
	public final static String END="������";
	static ArrayList<String>nonterminal=new ArrayList<String>();//���ս������
	static ArrayList<String>actionSign=new ArrayList<String>();//����������
	static{
		//N:S,B,A,C,,X,R,Z,Z��,U,U��,E,E��,H,H��,G,M,D,L,L��,T,T��,F,O,P,Q
		nonterminal.add("S");
		nonterminal.add("A");
		nonterminal.add("B");
		nonterminal.add("C");
		nonterminal.add("D");
		nonterminal.add("E");
		nonterminal.add("F");
		nonterminal.add("G");
		nonterminal.add("H");
		nonterminal.add("L");
		nonterminal.add("M");
		nonterminal.add("O");
		nonterminal.add("P");
		nonterminal.add("Q");
		nonterminal.add("X");
		nonterminal.add("Y");
		nonterminal.add("Z");
		nonterminal.add("R");
		nonterminal.add("U");
		nonterminal.add("Z'");
		nonterminal.add("U'");
		nonterminal.add("E'");
		nonterminal.add("H'");
		nonterminal.add("L'");
		nonterminal.add("T");
		nonterminal.add("T'");
		actionSign.add("@ADD_SUB");
		actionSign.add("@ADD");
		actionSign.add("@SUB");
		actionSign.add("@DIV_MUL");
		actionSign.add("@DIV");
		actionSign.add("@MUL");
		actionSign.add("@SINGLE");
		actionSign.add("@SINGTLE_OP");
		actionSign.add("@ASS_R");
		actionSign.add("@ASS_Q");
		actionSign.add("@ASS_F");
		actionSign.add("@ASS_U");
		actionSign.add("@TRAN_LF");
		actionSign.add("@EQ");
		actionSign.add("@EQ_U'");
		actionSign.add("@COMPARE");
		actionSign.add("@COMPARE_OP");
		actionSign.add("@IF_FJ");
		actionSign.add("@IF_BACKPATCH_FJ");
		actionSign.add("@IF_RJ");
		actionSign.add("@IF_BACKPATCH_RJ");
		actionSign.add("@WHILE_FJ");
		actionSign.add("@WHILE_BACKPATCH_FJ");
		actionSign.add("@IF_RJ");
		actionSign.add("@FOR_FJ");
		actionSign.add("@FOR_RJ");
		actionSign.add("@FOR_BACKPATCH_FJ");
	}
	
	String type;//�ڵ�����
	String name;//�ڵ���
	String value;//�ڵ�ֵ
	
	public static boolean isNonterm(AnalyseNode node){
		return nonterminal.contains(node.name);
	}
	public static boolean isTerm(AnalyseNode node){
		return Word.isKey(node.name)||Word.isOperator(node.name)||Word.isBoundarySign(node.name)
		||node.name.equals("id")||node.name.equals("num")||node.name.equals("ch")
		||node.name.equals("\"%d\"")||node.name.equals("&");
	}
	public static boolean isActionSign(AnalyseNode node){
		return actionSign.contains(node.name);
	}
	public AnalyseNode(){
		
	}
public AnalyseNode(String type,String name,String value){
		this.type=type;
		this.name=name;
		this.value=value;
	}

}
