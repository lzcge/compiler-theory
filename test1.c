//#include<stdio.h>
//阶乘 
void main()

{
//int i,sum,n;
i=0;
//int main //语义错误，非法标识符 
//int 1m,2;//词法出错 
//scanf("%d",n);//语法法出错 
//int n;//语义错误，重复定义
//kk=9;//语义错误，没有定义
scanf("%d",&n);

if(n<1){
sum=0;
}else{
sum=1;
}



while(i<n)
{
i=i+1;
sum=sum*i;
 
}

 
/*
for(int j=1;j<=n;j++){
sum=sum*j;
 printf("%d",sum);
}
*/
 
 printf("%d",sum);
}
