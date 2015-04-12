.class public FactorialRoomAction

.super RoomAction

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual RoomAction/<init>()V
  return
.end method

.method public describe()V
  .limit locals 1
  .limit stack 2
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "There is a red button."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  return
.end method

.method public performAction(Ljava/lang/String;)I
  .limit locals 4
  .limit stack 6
  aload_1
  ldc "use"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_7
  iconst_0
  goto stop_8
  true_7:
  iconst_1
  stop_8:
  dup
  ifne true_6
  pop
  aload_1
  ldc "operate"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_9
  iconst_0
  goto stop_10
  true_9:
  iconst_1
  stop_10:
  true_6:
  dup
  ifne true_5
  pop
  aload_1
  ldc "press"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_11
  iconst_0
  goto stop_12
  true_11:
  iconst_1
  stop_12:
  true_5:
  dup
  ifne true_4
  pop
  aload_1
  ldc "interact"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_13
  iconst_0
  goto stop_14
  true_13:
  iconst_1
  stop_14:
  true_4:
  dup
  ifne true_3
  pop
  aload_1
  ldc "factorial"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_15
  iconst_0
  goto stop_16
  true_15:
  iconst_1
  stop_16:
  true_3:
  dup
  ifne true_2
  pop
  aload_1
  ldc "push"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_17
  iconst_0
  goto stop_18
  true_17:
  iconst_1
  stop_18:
  true_2:
  ifeq else_0
  new joos/lib/JoosRandom
  dup
  invokenonvirtual joos/lib/JoosRandom/<init>()V
  dup
  astore_2
  pop
  aload_2
  invokevirtual joos/lib/JoosRandom/nextInt()I
  ldc 13
  irem
  dup
  istore_3
  pop
  iload_3
  iconst_0
  if_icmplt true_20
  iconst_0
  goto stop_21
  true_20:
  iconst_1
  stop_21:
  ifeq stop_19
  iload_3
  ineg
  dup
  istore_3
  pop
  stop_19:
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "The dials come to a stop. Showing:"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  new java/lang/Integer
  dup
  iload_3
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  ldc "!\n---"
  dup
  ifnull null_24
  goto stop_25
  null_24:
  pop
  ldc "null"
  stop_25:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc ""
  dup
  ifnull null_26
  goto stop_27
  null_26:
  pop
  ldc "null"
  stop_27:
  new java/lang/Integer
  dup
  aload_0
  iload_3
  invokevirtual FactorialRoomAction/f(I)I
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "The dials resume spinning."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  iconst_0
  ireturn
  goto stop_1
  else_0:
  aload_0
  aload_1
  invokevirtual RoomAction/performBaseAction(Ljava/lang/String;)I
  ireturn
  stop_1:
  nop
.end method

.method public f(I)I
  .limit locals 2
  .limit stack 4
  iload_1
  iconst_0
  if_icmpeq true_3
  iconst_0
  goto stop_4
  true_3:
  iconst_1
  stop_4:
  dup
  ifne true_2
  pop
  iload_1
  iconst_1
  if_icmpeq true_5
  iconst_0
  goto stop_6
  true_5:
  iconst_1
  stop_6:
  true_2:
  ifeq else_0
  iconst_1
  ireturn
  goto stop_1
  else_0:
  iload_1
  aload_0
  iload_1
  iconst_1
  isub
  invokevirtual FactorialRoomAction/f(I)I
  imul
  ireturn
  stop_1:
  nop
.end method

