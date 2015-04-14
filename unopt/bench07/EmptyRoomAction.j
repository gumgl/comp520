.class public EmptyRoomAction

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
  ldc "There doesn't seem to be anything to do here."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  return
.end method

.method public performAction(Ljava/lang/String;)I
  .limit locals 2
  .limit stack 2
  aload_0
  aload_1
  invokevirtual RoomAction/performBaseAction(Ljava/lang/String;)I
  ireturn
  nop
.end method

