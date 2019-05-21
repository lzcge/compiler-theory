datasg segment
tem db 6,7 dup  (0)
i dw 0
sum dw 0
n dw 0
T1 dw 0
T2 dw 0
T3 dw 0
T4 dw 0
scanf_n2 db 'input n:$'
printf_sum15 db 'sum:$'
datasg ends
codesg segment
assume cs:codesg,ds:datasg
start:
MOV AX,datasg
MOV DS,AX
L1: mov AX, 0
mov i, AX
L2:


;SCANF
lea dx,scanf_n2
mov ah,9
int 21h
;输入中断
mov al,0h;
mov tem[1],al;
lea dx,tem;
 mov ah,0ah
int 21h
;处理输入的数据，并赋值给变量
mov cl,0000h;
mov al,tem[1];
sub al,1;
mov cl,al;
mov ax,0000h;
mov bx,0000h;
mov al,tem[2];
sub al,30h;
mov n,ax;
mov ax,cx
sub ax,1
jc inputEnd2;小于则跳转
;
MOV SI,0003H;
ln2:mov bx,10;
mov ax,n;
mul bx;
mov n,ax;
mov ax,0000h;
mov al,tem[si]
sub al,30h;
add ax,n;
mov n,ax
INC SI
loop ln2
inputEnd2: nop


;换行
mov dl,0dh
mov ah,2
int 21h
mov dl,0ah
mov ah,2
int 21h


L3: mov AX, n
sub AX, 1
L4: jnc L7;大于等于则跳转
L5: mov AX, 0
mov sum, AX
L6: jmp L8
L7: mov AX, 1
mov sum, AX
L8: mov AX, i
sub AX, n
L9: jnc TheEnd;大于等于则跳转
add AX, 1
mov T3, AX
L10: mov AX, i
L11: mov AX, T3
mov i, AX
L12: mov AX, sum
mov BX,i
mul BX
mov T4, AX
L13: mov AX, T4
mov sum, AX
L14: jmp L8
TheEnd:nop


;PRINTF
L15:
lea dx,printf_sum15
mov ah,9
int 21h
mov ax,sum
xor cx,cx
mov bx,10
PT015:xor dx,dx
div bx
or dx,0e30h;0e:显示字符
push dx
inc cx
cmp ax,0;ZF=1则AX=0,ZF=0则AX！=0
jnz PT015;相等时跳转
PT115:pop ax
int 10h;显示一个字符
loop PT115
mov ah,0 
;int 16h ;键盘中断
;换行
mov dl,0dh
mov ah,2
int 21h
mov dl,0ah
mov ah,2
int 21h


mov ax,4c00h; int 21h的4ch号中断，安全退出程序。
int 21h;调用系统中断
codesg ends
end start
