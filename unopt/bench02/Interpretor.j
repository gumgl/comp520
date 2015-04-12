.class public Interpretor

.super java/lang/Object

.field protected state Ljava/lang/String;
.field protected f Ljoos/lib/JoosIO;
.field protected ioImported Z
.field protected escape LStringEscapeUtils;

.method public <init>()V
  .limit locals 1
  .limit stack 3
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  ldc "init"
  dup
  aload_0
  swap
  putfield Interpretor/state Ljava/lang/String;
  pop
  new joos/lib/JoosIO
  dup
  invokenonvirtual joos/lib/JoosIO/<init>()V
  dup
  aload_0
  swap
  putfield Interpretor/f Ljoos/lib/JoosIO;
  pop
  iconst_0
  dup
  aload_0
  swap
  putfield Interpretor/ioImported Z
  pop
  new StringEscapeUtils
  dup
  invokenonvirtual StringEscapeUtils/<init>()V
  dup
  aload_0
  swap
  putfield Interpretor/escape LStringEscapeUtils;
  pop
  return
.end method

.method public interpret(Ljava/lang/String;)V
  .limit locals 2
  .limit stack 2
  aload_0
  getfield Interpretor/state Ljava/lang/String;
  ldc "init"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_0
  aload_0
  aload_1
  invokevirtual Interpretor/init(Ljava/lang/String;)V
  goto stop_1
  else_0:
  aload_0
  getfield Interpretor/state Ljava/lang/String;
  ldc "running"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq stop_2
  aload_0
  aload_1
  invokevirtual Interpretor/run(Ljava/lang/String;)V
  stop_2:
  stop_1:
  return
.end method

.method public init(Ljava/lang/String;)V
  .limit locals 2
  .limit stack 3
  aload_1
  ldc "HAI"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  aload_0
  ldc "Program must start with 'HAI'"
  invokevirtual Interpretor/crashed(Ljava/lang/String;)V
  goto stop_1
  else_0:
  ldc "running"
  dup
  aload_0
  swap
  putfield Interpretor/state Ljava/lang/String;
  pop
  stop_1:
  return
.end method

.method public crashed(Ljava/lang/String;)V
  .limit locals 2
  .limit stack 2
  aload_0
  invokevirtual Interpretor/exit()V
  aload_0
  getfield Interpretor/f Ljoos/lib/JoosIO;
  aload_1
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  return
.end method

.method public exit()V
  .limit locals 1
  .limit stack 3
  ldc "exit"
  dup
  aload_0
  swap
  putfield Interpretor/state Ljava/lang/String;
  pop
  return
.end method

.method public run(Ljava/lang/String;)V
  .limit locals 2
  .limit stack 4
  aload_1
  ldc "CAN"
  iconst_0
  invokevirtual java/lang/String/startsWith(Ljava/lang/String;I)Z
  ifeq else_0
  aload_0
  aload_1
  invokevirtual Interpretor/openMod(Ljava/lang/String;)V
  goto stop_1
  else_0:
  aload_1
  ldc "VISIBLE"
  iconst_0
  invokevirtual java/lang/String/startsWith(Ljava/lang/String;I)Z
  ifeq else_2
  aload_0
  aload_1
  invokevirtual Interpretor/visible(Ljava/lang/String;)V
  goto stop_3
  else_2:
  aload_1
  ldc "KTHXBYE"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_4
  aload_0
  invokevirtual Interpretor/exit()V
  goto stop_5
  else_4:
  aload_0
  ldc "'"
  dup
  ifnull null_8
  goto stop_9
  null_8:
  pop
  ldc "null"
  stop_9:
  aload_1
  dup
  ifnull null_10
  goto stop_11
  null_10:
  pop
  ldc "null"
  stop_11:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_6
  goto stop_7
  null_6:
  pop
  ldc "null"
  stop_7:
  ldc "' can not be recognized"
  dup
  ifnull null_12
  goto stop_13
  null_12:
  pop
  ldc "null"
  stop_13:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual Interpretor/crashed(Ljava/lang/String;)V
  stop_5:
  stop_3:
  stop_1:
  return
.end method

.method public visible(Ljava/lang/String;)V
  .limit locals 3
  .limit stack 4
  aload_0
  getfield Interpretor/ioImported Z
  ifeq true_1
  iconst_0
  goto stop_2
  true_1:
  iconst_1
  stop_2:
  ifeq stop_0
  aload_0
  ldc "Can not execute VISIBLE with out stdio"
  invokevirtual Interpretor/crashed(Ljava/lang/String;)V
  stop_0:
  aload_1
  ldc 9
  aload_1
  invokevirtual java/lang/String/length()I
  iconst_1
  isub
  invokevirtual java/lang/String/substring(II)Ljava/lang/String;
  dup
  astore_2
  pop
  aload_0
  getfield Interpretor/f Ljoos/lib/JoosIO;
  aload_0
  getfield Interpretor/escape LStringEscapeUtils;
  aload_2
  invokevirtual StringEscapeUtils/escape(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  return
.end method

.method public openMod(Ljava/lang/String;)V
  .limit locals 2
  .limit stack 3
  aload_1
  ldc "CAN HAS STDIO?"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq true_1
  iconst_0
  goto stop_2
  true_1:
  iconst_1
  stop_2:
  ifeq stop_0
  aload_0
  ldc "Only module 'STDIO' is supported"
  invokevirtual Interpretor/crashed(Ljava/lang/String;)V
  stop_0:
  iconst_1
  dup
  aload_0
  swap
  putfield Interpretor/ioImported Z
  pop
  return
.end method

