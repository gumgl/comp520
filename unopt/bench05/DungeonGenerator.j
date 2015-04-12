.class public DungeonGenerator

.super java/lang/Object

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  return
.end method

.method public static main([Ljava/lang/String;)V
  .limit locals 4
  .limit stack 3
  new DungeonInfos
  dup
  invokenonvirtual DungeonInfos/<init>()V
  dup
  astore_1
  pop
  aload_1
  invokevirtual DungeonInfos/initializeFromStdIn()V
  new Dungeon
  dup
  aload_1
  invokenonvirtual Dungeon/<init>(LDungeonInfos;)V
  dup
  astore_2
  pop
  new joos/lib/JoosIO
  dup
  invokenonvirtual joos/lib/JoosIO/<init>()V
  dup
  astore_3
  pop
  aload_2
  invokevirtual Dungeon/draw()V
  return
.end method

