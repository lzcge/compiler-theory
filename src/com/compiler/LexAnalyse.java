package com.compiler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

/**
 * �ʷ�������
 * 
 * @author KB
 * 
 */
public class LexAnalyse {

	public ArrayList<Word> wordList = new ArrayList<Word>();// ���ʱ�
	public ArrayList<Error> errorList = new ArrayList<Error>();// ������Ϣ�б�
	public static ArrayList<String> intlist = new ArrayList<String>();// int����
	public static ArrayList<String> charlist = new ArrayList<String>();// char����
	public static ArrayList<Typeword> typelist = new ArrayList<Typeword>();// char����
	public int wordCount = 0;// ͳ�Ƶ��ʸ���
	public int errorCount = 0;// ͳ�ƴ������
	public boolean noteFlag = false;// ����ע�ͱ�־
	public boolean lexErrorFlag = false;// �ʷ����������־
	public Stack<String>  typestack=new Stack<String>();//����ջ
	public LexAnalyse() {

	}

	public LexAnalyse(String str) {
		lexAnalyse(str);
	}
	/**
	 * �����ַ��ж�
	 * 
	 * @param ch
	 * @return
	 */
	private static boolean isDigit(char ch) {
		boolean flag = false;
		if ('0' <= ch && ch <= '9')
			flag = true;
		return flag;
	}

	/**
	 * �жϵ����Ƿ�Ϊint����
	 * 
	 * @param
	 * @return
	 */
	private static boolean isInteger(String word) {
		int i;
		boolean flag = false;
		for (i = 0; i < word.length(); i++) {
			if (Character.isDigit(word.charAt(i))) {
				continue;
			} else {
				break;
			}
		}
		if (i == word.length()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * �жϵ����Ƿ�Ϊchar����
	 * 
	 * @param word
	 * @return
	 */
	private static boolean isChar(String word) {
		boolean flag = false;
		int i = 0;
		char temp = word.charAt(i);
		if (temp == '\'') {
			for (i = 1; i < word.length(); i++) {
				temp = word.charAt(i);
				if (0 <= temp && temp <= 255)
					continue;
				else
					break;
			}
			if (i + 1 == word.length() && word.charAt(i) == '\'')
				flag = true;
		} else
			return flag;

		return flag;
	}

	/**
	 * �ж��ַ��Ƿ�Ϊ��ĸ
	 * 
	 * @param ch
	 * @return
	 */
	private static boolean isLetter(char ch) {
		boolean flag = false;
		if (('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z'))
			flag = true;
		return flag;
	}

	/**
	 * �жϵ����Ƿ�Ϊ�Ϸ���ʶ��
	 * 
	 * @param word
	 * @return
	 */
	private static boolean isID(String word) {
		boolean flag = false;
		int i = 0;
		if (Word.isKey(word))
			return flag;
		char temp = word.charAt(i);
		if (isLetter(temp) || temp == '_') {
			for (i = 1; i < word.length(); i++) {
				temp = word.charAt(i);
				if (isLetter(temp) || temp == '_' || isDigit(temp))
					continue;
				else
					break;
			}
			if (i >= word.length())
				flag = true;
		} else
			return flag;

		return flag;
	}

	/**
	 * �жϴʷ������Ƿ�ͨ��
	 * 
	 */
	public boolean isFail() {
		return lexErrorFlag;
	}

	public void analyse(String str, int line) {
		int beginIndex;
		int endIndex;
		int index = 0;
		int length = str.length();
		Word word = null;
		Typeword typeword=null;
		Error error;
		// boolean flag=false;
		char temp;
		while (index < length) {
			temp = str.charAt(index);
			if (!noteFlag) {
				if (isLetter(temp) || temp == '_') {// �ж��ǲ��Ǳ�־��
					beginIndex = index;
					index++;
					// temp=str.charAt(index);
					while ((index < length)
							&& (!Word.isBoundarySign(str.substring(index,
									index + 1)))
							&& (!Word.isOperator(str
									.substring(index, index + 1)))
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) {
						index++;
						// temp=str.charAt(index);
					}
					endIndex = index;
					word = new Word();
					wordCount++;
					word.id = wordCount;
					word.line = line;
					word.value = str.substring(beginIndex, endIndex);
					if (Word.isKey(word.value)) {
						word.type = Word.KEY;
						if(word.value.equals("int")||word.value.equals("char")){
							typestack.push(word.value);
						}
					} else if (isID(word.value)) {
						word.type = Word.IDENTIFIER;
						if(typestack.size()>0){
							 word.attribute=typestack.lastElement();
							 if(word.attribute.equals("int")||word.attribute.equals("char")){
								 typeword=new Typeword();
								 typeword.value=word.value;
								 typeword.type=word.attribute;
								 typelist.add(typeword);
							 }	 
						}
						  
						
					} else {
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;

				} else if (isDigit(temp)) {// �ж��ǲ���int����

					beginIndex = index;
					index++;
					// temp=str.charAt(index);
					while ((index < length)
							&& (!Word.isBoundarySign(str.substring(index,
									index + 1)))
							&& (!Word.isOperator(str
									.substring(index, index + 1)))
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) {
						index++;
						// temp=str.charAt(index);
					}
					endIndex = index;
					word = new Word();
					wordCount++;
					word.id = wordCount;
					word.line = line;
					word.value = str.substring(beginIndex, endIndex);
					if (isInteger(word.value)) {
						word.type = Word.INT_CONST;
					} else {
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				} else if (str.charAt(index)=='\'') {// �ַ�����
					// flag=true;
					beginIndex = index;
					index++;
					temp = str.charAt(index);
					if(index < length && isLetter(temp)) {
						index++;
						if (index < length&&str.charAt(index)=='\''){
							word = new Word();
							wordCount++;
							word.id = wordCount;
							word.line = line;
							word.value = str.substring(beginIndex, index+1);
							word.type = Word.CHAR_CONST;
						}
						else {
							endIndex = index;
							word = new Word();
							wordCount++;
							word.id = wordCount;
							word.line = line;
							word.value = str.substring(beginIndex, index+1);
							word.type = Word.UNIDEF;
							word.flag = false;
							errorCount++;
							error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
							errorList.add(error);
							lexErrorFlag = true;
						}
						// temp=str.charAt(index);
					}
					else {
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, index+1);
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					
				} 
				else if (str.charAt(index)=='"') {// scanf��ʽ�ж�
					// flag=true;
					beginIndex = index;
					index++;
					//int fast=index;
					temp = str.charAt(index);
					while (index < length) {
						if(str.charAt(index)=='%'){
							index++;
							if(index<length&&str.charAt(index)=='d'|str.charAt(index)=='c'){
								index++;
								if(str.charAt(index)=='"'){
									endIndex = index;
									word = new Word();
									wordCount++;
									word.id = wordCount;
									word.line = line;
									word.value = str.substring(beginIndex, index+1);
									word.type = Word.KEY;
									break;
								}
							}
							else{
								word = new Word();
								wordCount++;
								word.id = wordCount;
								word.line = line;
								word.value = str.substring(beginIndex, index);
								word.type = Word.UNIDEF;
								word.flag = false;
								errorCount++;
								error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
								errorList.add(error);
								lexErrorFlag = true;
								break;
							}
						}
						else{
							word = new Word();
							wordCount++;
							word.id = wordCount;
							word.line = line;
							word.value = str.substring(beginIndex, index);
							word.type = Word.UNIDEF;
							word.flag = false;
							errorCount++;
							error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
							errorList.add(error);
							lexErrorFlag = true;
							break;
						}
						// temp=str.charAt(index);
					}
				} 
				else if (temp == '=') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '!') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						index++;
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '&') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '&') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '|') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '|') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '+') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '+') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;

					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '-') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '-') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '/') {
					index++;
					if (index < length && str.charAt(index) == '/')
						break;
					/*
					 * { index++; while(str.charAt(index)!='\n'){ index++; } }
					 */
					else if (index < length && str.charAt(index) == '*') {
						noteFlag = true;
					} else {
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
					}
					index--;
				} 
				 else if (temp == '<') {
						beginIndex = index;
						index++;
						if (index < length && str.charAt(index) == '=') {
							endIndex = index + 1;
							word = new Word();
							wordCount++;
							word.id = wordCount;
							word.line = line;
							word.value = str.substring(beginIndex, endIndex);
							word.type = Word.OPERATOR;
						} else {
							// endIndex=index;
							word = new Word();
							wordCount++;
							word.id = wordCount;
							word.line = line;
							word.value = str.substring(index - 1, index);
							word.type = Word.OPERATOR;
							index--;
						}
					}
				 else if (temp == '>') {
						beginIndex = index;
						index++;
						if (index < length && str.charAt(index) == '=') {
							endIndex = index + 1;
							word = new Word();
							wordCount++;
							word.id = wordCount;
							word.line = line;
							word.value = str.substring(beginIndex, endIndex);
							word.type = Word.OPERATOR;
						} else {
							// endIndex=index;
							word = new Word();
							wordCount++;
							word.id = wordCount;
							word.line = line;
							word.value = str.substring(index - 1, index);
							word.type = Word.OPERATOR;
							index--;
						}
					}
				else {// ���Ǳ�ʶ�������ֳ������ַ�������

					switch (temp) {
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						word = null;
						break;// ���˿հ��ַ�
					case ';':
						if(typestack.size()>0){
							typestack.pop();
						}
					case '[':
					case ']':
					case '(':
					case ')':
					case '{':
					case '}':
					case ',':
					case '"':
					case '.':
						// case '+':
						// case '-':
					case '*':
						// case '/':
					case '%':
					case '?':
					case '#':
						word = new Word();
						word.id = ++wordCount;
						word.line = line;
						word.value = String.valueOf(temp);
						if (Word.isOperator(word.value))
							word.type = Word.OPERATOR;
						else if (Word.isBoundarySign(word.value))
							word.type = Word.BOUNDARYSIGN;
						else
							word.type = Word.END;
						break;
					default:
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = String.valueOf(temp);
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
				}
			} else {
				int i = str.indexOf("*/");
				if (i != -1) {
					noteFlag = false;
					index = i + 2;
					continue;
				} else
					break;
			}
			if (word == null) {
				index++;
				continue;
			}

			wordList.add(word);
			index++;
		}
	}

	public ArrayList<Word> lexAnalyse(String str) {
		String buffer[];
		if(str==null){
			if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
				Word word = new Word(++wordCount, "#", Word.END, 1);
				wordList.add(word);
			}
			return wordList;
		}
		buffer = str.split("\n");
		int line = 1;
		for (int i = 0; i < buffer.length; i++) {
			analyse(buffer[i].trim(), line);
			line++;
		}
		if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
			Word word = new Word(++wordCount, "#", Word.END, line++);
			wordList.add(word);
		}
		return wordList;
	}

	public ArrayList<Word> lexAnalyse1(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "utf-8");
		BufferedReader inbr = new BufferedReader(isr);
		String str = "";
		int line = 1;
		while ((str = inbr.readLine()) != null) {
			// System.out.println(str);
			analyse(str.trim(), line);
			line++;
		}
		inbr.close();
		if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
			Word word = new Word(++wordCount, "#", Word.END, line++);
			wordList.add(word);
		}
		return wordList;
	}

	public String outputWordList() throws IOException {
		System.out.println(typelist);
		File file = new File("./result/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// �������ļ������ھʹ�����
		}
		String path = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(path + "/wordList.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		pw1.println("�������\t���ʵ�ֵ\t��������\t���������� \t�����Ƿ�Ϸ�\t��������");
		Word word;
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			pw1.println(word.id + "\t\t" + word.value + "\t\t" + word.type + "\t\t"
					+ "\t" + word.line + "\t" + word.flag+"\t\t\t"+word.attribute);
			System.out.println(word.id + "\t\t" + word.value + "\t\t" + word.type + "\t\t"
					+ "\t" + word.line + "\t" + word.flag+"\t\t\t"+word.attribute);
		}
		if (lexErrorFlag) {
			Error error;
			pw1.println("������Ϣ���£�");

			pw1.println("�������\t������Ϣ\t���������� \t���󵥴�");
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				pw1.println(error.id + "\t\t" + error.info + "\t\t" + error.line
						+ "\t" + error.word.value);
			}
		} else {
			pw1.println("�ʷ�����ͨ����");
		}
		pw1.close();
		return path + "/wordList.txt";
	}
	public static ArrayList<String> getTypelist(){
		ArrayList<String> list = new ArrayList<String>();
		
		for(Typeword x : typelist ){
			list.add(x.getValue());
		}
		System.out.println(list.toString());
		return list;
		
	}
	public static ArrayList<String> getCharlist(){
		
		return charlist;
		
	}
	

	

	public static void main(String[] args) throws IOException {
		LexAnalyse lex = new LexAnalyse();
		lex.lexAnalyse1("test.txt");
		lex.outputWordList();
	}

	public ArrayList<Word> getWordList() {
		// TODO Auto-generated method stub
		return wordList;
	}


}
