package com.compiler;

public class Error {
int id ;//������ţ�
String info;//������Ϣ��
public int line ;//����������
Word word;//����ĵ���
public Error(){
	
}
//public Error(int id,String info,int line){
//	this.id=id;
//	this.info=info;
//	this.line=line;
//}
public Error(int id,String info,int line,Word word){
	this.id=id;
	this.info=info;
	this.line=line;
	this.word=word;
}
}
