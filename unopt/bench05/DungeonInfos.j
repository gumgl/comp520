.class public DungeonInfos

.super java/lang/Object

.field protected dimension LCustomPoint;
.field protected monsters Ljava/util/Vector;
.field protected upstairs Ljava/util/Vector;
.field protected downstairs Ljava/util/Vector;
.field protected treasures Ljava/util/Vector;
.field protected heros Ljava/util/Vector;

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  aload_0
  invokevirtual DungeonInfos/initializeTo0()V
  return
.end method

.method public initializeTo0()V
  .limit locals 1
  .limit stack 3
  new CustomPoint
  dup
  invokenonvirtual CustomPoint/<init>()V
  dup
  aload_0
  swap
  putfield DungeonInfos/dimension LCustomPoint;
  pop
  new java/util/Vector
  dup
  invokenonvirtual java/util/Vector/<init>()V
  dup
  aload_0
  swap
  putfield DungeonInfos/monsters Ljava/util/Vector;
  pop
  new java/util/Vector
  dup
  invokenonvirtual java/util/Vector/<init>()V
  dup
  aload_0
  swap
  putfield DungeonInfos/upstairs Ljava/util/Vector;
  pop
  new java/util/Vector
  dup
  invokenonvirtual java/util/Vector/<init>()V
  dup
  aload_0
  swap
  putfield DungeonInfos/downstairs Ljava/util/Vector;
  pop
  new java/util/Vector
  dup
  invokenonvirtual java/util/Vector/<init>()V
  dup
  aload_0
  swap
  putfield DungeonInfos/treasures Ljava/util/Vector;
  pop
  new java/util/Vector
  dup
  invokenonvirtual java/util/Vector/<init>()V
  dup
  aload_0
  swap
  putfield DungeonInfos/heros Ljava/util/Vector;
  pop
  return
.end method

.method public initializeFromStdIn()V
  .limit locals 5
  .limit stack 5
  new joos/lib/JoosIO
  dup
  invokenonvirtual joos/lib/JoosIO/<init>()V
  dup
  astore_1
  pop
  aload_1
  invokevirtual joos/lib/JoosIO/readLine()Ljava/lang/String;
  dup
  astore_2
  pop
  start_0:
  aload_2
  aconst_null
  if_acmpne true_3
  iconst_0
  goto stop_4
  true_3:
  iconst_1
  stop_4:
  dup
  ifeq false_2
  pop
  aload_2
  ldc ""
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq true_5
  iconst_0
  goto stop_6
  true_5:
  iconst_1
  stop_6:
  false_2:
  ifeq stop_1
  aload_1
  invokevirtual joos/lib/JoosIO/readInt()I
  dup
  istore_3
  pop
  aload_1
  invokevirtual joos/lib/JoosIO/readInt()I
  dup
  istore 4
  pop
  aload_2
  ldc "Room"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq stop_7
  new CustomPoint
  dup
  iload_3
  iload 4
  invokenonvirtual CustomPoint/<init>(II)V
  dup
  aload_0
  swap
  putfield DungeonInfos/dimension LCustomPoint;
  pop
  stop_7:
  aload_2
  ldc "Monster"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_8
  aload_0
  getfield DungeonInfos/monsters Ljava/util/Vector;
  new CustomPoint
  dup
  iload_3
  iload 4
  invokenonvirtual CustomPoint/<init>(II)V
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  goto stop_9
  else_8:
  aload_2
  ldc "Upstairs"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_10
  aload_0
  getfield DungeonInfos/upstairs Ljava/util/Vector;
  new CustomPoint
  dup
  iload_3
  iload 4
  invokenonvirtual CustomPoint/<init>(II)V
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  goto stop_11
  else_10:
  aload_2
  ldc "Downstairs"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_12
  aload_0
  getfield DungeonInfos/downstairs Ljava/util/Vector;
  new CustomPoint
  dup
  iload_3
  iload 4
  invokenonvirtual CustomPoint/<init>(II)V
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  goto stop_13
  else_12:
  aload_2
  ldc "Treasure"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_14
  aload_0
  getfield DungeonInfos/treasures Ljava/util/Vector;
  new CustomPoint
  dup
  iload_3
  iload 4
  invokenonvirtual CustomPoint/<init>(II)V
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  goto stop_15
  else_14:
  aload_2
  ldc "Hero"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq stop_16
  aload_0
  getfield DungeonInfos/heros Ljava/util/Vector;
  new CustomPoint
  dup
  iload_3
  iload 4
  invokenonvirtual CustomPoint/<init>(II)V
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  stop_16:
  stop_15:
  stop_13:
  stop_11:
  stop_9:
  aload_1
  invokevirtual joos/lib/JoosIO/readLine()Ljava/lang/String;
  dup
  astore_2
  pop
  goto start_0
  stop_1:
  return
.end method

.method public getDimension()LCustomPoint;
  .limit locals 1
  .limit stack 1
  aload_0
  getfield DungeonInfos/dimension LCustomPoint;
  areturn
  nop
.end method

.method public getMonsters()Ljava/util/Vector;
  .limit locals 1
  .limit stack 1
  aload_0
  getfield DungeonInfos/monsters Ljava/util/Vector;
  areturn
  nop
.end method

.method public getUpstairs()Ljava/util/Vector;
  .limit locals 1
  .limit stack 1
  aload_0
  getfield DungeonInfos/upstairs Ljava/util/Vector;
  areturn
  nop
.end method

.method public getDownstairs()Ljava/util/Vector;
  .limit locals 1
  .limit stack 1
  aload_0
  getfield DungeonInfos/downstairs Ljava/util/Vector;
  areturn
  nop
.end method

.method public getTreasures()Ljava/util/Vector;
  .limit locals 1
  .limit stack 1
  aload_0
  getfield DungeonInfos/treasures Ljava/util/Vector;
  areturn
  nop
.end method

.method public getHeros()Ljava/util/Vector;
  .limit locals 1
  .limit stack 1
  aload_0
  getfield DungeonInfos/heros Ljava/util/Vector;
  areturn
  nop
.end method

