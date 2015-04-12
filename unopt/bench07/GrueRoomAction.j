.class public GrueRoomAction

.super RoomAction

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual RoomAction/<init>()V
  return
.end method

.method public performAction(Ljava/lang/String;)I
  .limit locals 2
  .limit stack 3
  aload_1
  ldc "fight"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "You are ambushed."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_1
  else_0:
  aload_1
  ldc "defend"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq else_4
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "You die from lack of sustenance."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_5
  else_4:
  aload_1
  ldc "light"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  ifeq else_8
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "You have nothing with which to lighten up the room."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_9
  else_8:
  aload_1
  ldc "run"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_13
  iconst_0
  goto stop_14
  true_13:
  iconst_1
  stop_14:
  ifeq stop_12
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "You are too slow."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_12:
  stop_9:
  stop_5:
  stop_1:
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "You are eaten by a grue. "
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  iconst_1
  ireturn
  nop
.end method

