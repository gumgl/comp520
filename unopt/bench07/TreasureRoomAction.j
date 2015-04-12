.class public TreasureRoomAction

.super RoomAction

.field protected taken Z

.method public <init>()V
  .limit locals 1
  .limit stack 3
  aload_0
  invokenonvirtual RoomAction/<init>()V
  iconst_0
  dup
  aload_0
  swap
  putfield TreasureRoomAction/taken Z
  pop
  return
.end method

.method public describe()V
  .limit locals 1
  .limit stack 2
  aload_0
  getfield TreasureRoomAction/taken Z
  ifeq true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "There is a treasure box at the center of the room."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_1
  else_0:
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "The treasure box is empty."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_1:
  return
.end method

.method public performAction(Ljava/lang/String;)I
  .limit locals 2
  .limit stack 3
  aload_1
  ldc "pick up"
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
  ldc "interact"
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
  ldc "take"
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
  ldc "open"
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
  ldc "get "
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
  getfield TreasureRoomAction/taken Z
  ifeq else_16
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "The treasure box is empty."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  iconst_0
  ireturn
  goto stop_17
  else_16:
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "You open the treasure box and finds a treasure! Your treasure count: "
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  iconst_1
  dup
  aload_0
  swap
  putfield TreasureRoomAction/taken Z
  pop
  iconst_3
  ireturn
  stop_17:
  goto stop_1
  else_0:
  aload_0
  aload_1
  invokevirtual RoomAction/performBaseAction(Ljava/lang/String;)I
  ireturn
  stop_1:
  nop
.end method

