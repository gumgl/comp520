.class public InGenerate

.super java/lang/Object

.field protected rand Ljoos/lib/JoosRandom;

.method public <init>()V
  .limit locals 1
  .limit stack 3
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  new joos/lib/JoosRandom
  dup
  invokenonvirtual joos/lib/JoosRandom/<init>()V
  dup
  aload_0
  swap
  putfield InGenerate/rand Ljoos/lib/JoosRandom;
  pop
  return
.end method

.method public static main([Ljava/lang/String;)V
  .limit locals 5
  .limit stack 2
  new InGenerate
  dup
  invokenonvirtual InGenerate/<init>()V
  dup
  astore_3
  pop
  new joos/lib/JoosIO
  dup
  invokenonvirtual joos/lib/JoosIO/<init>()V
  dup
  astore 4
  pop
  iconst_0
  dup
  istore_1
  pop
  iconst_0
  dup
  istore_2
  pop
  aload 4
  ldc "Dave"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  start_0:
  iload_1
  ldc 500
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  aload_3
  iconst_4
  invokevirtual InGenerate/randRange(I)I
  dup
  istore_2
  pop
  iload_2
  iconst_0
  if_icmpeq true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq else_4
  aload 4
  ldc "go north"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_5
  else_4:
  iload_2
  iconst_1
  if_icmpeq true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  ifeq else_8
  aload 4
  ldc "go south"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_9
  else_8:
  iload_2
  iconst_2
  if_icmpeq true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  ifeq else_12
  aload 4
  ldc "go east"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_13
  else_12:
  iload_2
  iconst_3
  if_icmpeq true_17
  iconst_0
  goto stop_18
  true_17:
  iconst_1
  stop_18:
  ifeq stop_16
  aload 4
  ldc "go west"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_16:
  stop_13:
  stop_9:
  stop_5:
  aload_3
  ldc 100
  invokevirtual InGenerate/randRange(I)I
  ldc 30
  if_icmpgt true_20
  iconst_0
  goto stop_21
  true_20:
  iconst_1
  stop_21:
  ifeq stop_19
  aload 4
  ldc "interact"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_19:
  aload_3
  ldc 100
  invokevirtual InGenerate/randRange(I)I
  ldc 70
  if_icmpgt true_23
  iconst_0
  goto stop_24
  true_23:
  iconst_1
  stop_24:
  ifeq stop_22
  aload 4
  ldc "show hp"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_22:
  aload_3
  ldc 100
  invokevirtual InGenerate/randRange(I)I
  ldc 70
  if_icmpgt true_26
  iconst_0
  goto stop_27
  true_26:
  iconst_1
  stop_27:
  ifeq stop_25
  aload 4
  ldc "show treasure"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_25:
  aload_3
  ldc 100
  invokevirtual InGenerate/randRange(I)I
  ldc 70
  if_icmpgt true_29
  iconst_0
  goto stop_30
  true_29:
  iconst_1
  stop_30:
  ifeq stop_28
  aload 4
  ldc "show coin"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_28:
  iload_1
  iconst_1
  iadd
  dup
  istore_1
  pop
  goto start_0
  stop_1:
  return
.end method

.method public randRange(I)I
  .limit locals 3
  .limit stack 2
  aload_0
  getfield InGenerate/rand Ljoos/lib/JoosRandom;
  invokevirtual joos/lib/JoosRandom/nextInt()I
  dup
  istore_2
  pop
  iload_2
  iconst_0
  if_icmplt true_1
  iconst_0
  goto stop_2
  true_1:
  iconst_1
  stop_2:
  ifeq stop_0
  iload_2
  ineg
  dup
  istore_2
  pop
  stop_0:
  iload_2
  iload_1
  irem
  ireturn
  nop
.end method

