.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 invokestatic Output/read()I
 istore 0
L1:
 invokestatic Output/read()I
 istore 1
L2:
 ldc 1
 ldc 2
 ldc 3
 ldc 4
 iadd 
 invokestatic Output/print(I)V
L3:
 iload 0
 iload 1
 if_icmpgt L7
 goto L6
L7:
 iload 0
 invokestatic Output/print(I)V
 goto L4
L6:
 iload 1
 invokestatic Output/print(I)V
L4:
L9:
 iload 0
 ldc 0
 if_icmpgt L10
 goto L8
L10:
 iload 0
 ldc 1
 isub 
 istore 0
L11:
 iload 0
 invokestatic Output/print(I)V
L12:
L13:
 goto L9
L8:
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

