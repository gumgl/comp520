.class public BFRoomAction

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
  .limit locals 2
  .limit stack 3
  aload_1
  ldc "use"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  dup
  ifne true_5
  pop
  aload_1
  ldc "operate"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_8
  iconst_0
  goto stop_9
  true_8:
  iconst_1
  stop_9:
  true_5:
  dup
  ifne true_4
  pop
  aload_1
  ldc "interact"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  true_4:
  dup
  ifne true_3
  pop
  aload_1
  ldc "press"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_12
  iconst_0
  goto stop_13
  true_12:
  iconst_1
  stop_13:
  true_3:
  dup
  ifne true_2
  pop
  aload_1
  ldc "push"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  true_2:
  ifeq else_0
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "Hello World"
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

