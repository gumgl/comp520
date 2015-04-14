.class public Dungeon

.super java/lang/Object

.field protected infos LDungeonInfos;
.field protected output Ljava/util/Vector;

.method public <init>(LDungeonInfos;)V
  .limit locals 2
  .limit stack 3
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  aload_1
  dup
  aload_0
  swap
  putfield Dungeon/infos LDungeonInfos;
  pop
  new java/util/Vector
  dup
  invokenonvirtual java/util/Vector/<init>()V
  dup
  aload_0
  swap
  putfield Dungeon/output Ljava/util/Vector;
  pop
  return
.end method

.method public draw()V
  .limit locals 6
  .limit stack 4
  aload_0
  invokevirtual Dungeon/createRoom()V
  aload_0
  getfield Dungeon/infos LDungeonInfos;
  invokevirtual DungeonInfos/getMonsters()Ljava/util/Vector;
  dup
  astore 5
  pop
  iconst_0
  dup
  istore_2
  pop
  start_0:
  iload_2
  aload 5
  invokevirtual java/util/Vector/size()I
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  aload_0
  ldc "M"
  aload 5
  iload_2
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  checkcast CustomPoint
  invokevirtual Dungeon/createItem(Ljava/lang/String;LCustomPoint;)V
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_0
  stop_1:
  aload_0
  getfield Dungeon/infos LDungeonInfos;
  invokevirtual DungeonInfos/getUpstairs()Ljava/util/Vector;
  dup
  astore 5
  pop
  iconst_0
  dup
  istore_2
  pop
  start_4:
  iload_2
  aload 5
  invokevirtual java/util/Vector/size()I
  if_icmplt true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq stop_5
  aload_0
  ldc "U"
  aload 5
  iload_2
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  checkcast CustomPoint
  invokevirtual Dungeon/createItem(Ljava/lang/String;LCustomPoint;)V
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_4
  stop_5:
  aload_0
  getfield Dungeon/infos LDungeonInfos;
  invokevirtual DungeonInfos/getDownstairs()Ljava/util/Vector;
  dup
  astore 5
  pop
  iconst_0
  dup
  istore_2
  pop
  start_8:
  iload_2
  aload 5
  invokevirtual java/util/Vector/size()I
  if_icmplt true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  ifeq stop_9
  aload_0
  ldc "D"
  aload 5
  iload_2
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  checkcast CustomPoint
  invokevirtual Dungeon/createItem(Ljava/lang/String;LCustomPoint;)V
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_8
  stop_9:
  aload_0
  getfield Dungeon/infos LDungeonInfos;
  invokevirtual DungeonInfos/getTreasures()Ljava/util/Vector;
  dup
  astore 5
  pop
  iconst_0
  dup
  istore_2
  pop
  start_12:
  iload_2
  aload 5
  invokevirtual java/util/Vector/size()I
  if_icmplt true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  ifeq stop_13
  aload_0
  ldc "T"
  aload 5
  iload_2
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  checkcast CustomPoint
  invokevirtual Dungeon/createItem(Ljava/lang/String;LCustomPoint;)V
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_12
  stop_13:
  aload_0
  getfield Dungeon/infos LDungeonInfos;
  invokevirtual DungeonInfos/getHeros()Ljava/util/Vector;
  dup
  astore 5
  pop
  iconst_0
  dup
  istore_2
  pop
  start_16:
  iload_2
  aload 5
  invokevirtual java/util/Vector/size()I
  if_icmplt true_18
  iconst_0
  goto stop_19
  true_18:
  iconst_1
  stop_19:
  ifeq stop_17
  aload_0
  ldc "H"
  aload 5
  iload_2
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  checkcast CustomPoint
  invokevirtual Dungeon/createItem(Ljava/lang/String;LCustomPoint;)V
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_16
  stop_17:
  new joos/lib/JoosIO
  dup
  invokenonvirtual joos/lib/JoosIO/<init>()V
  dup
  astore_1
  pop
  aload_0
  getfield Dungeon/infos LDungeonInfos;
  invokevirtual DungeonInfos/getDimension()LCustomPoint;
  dup
  astore 4
  pop
  iconst_0
  dup
  istore_2
  pop
  start_20:
  iload_2
  aload 4
  invokevirtual CustomPoint/getY()I
  iconst_2
  iadd
  if_icmplt true_22
  iconst_0
  goto stop_23
  true_22:
  iconst_1
  stop_23:
  ifeq stop_21
  iconst_0
  dup
  istore_3
  pop
  start_24:
  iload_3
  aload 4
  invokevirtual CustomPoint/getX()I
  iconst_2
  iadd
  if_icmplt true_26
  iconst_0
  goto stop_27
  true_26:
  iconst_1
  stop_27:
  ifeq stop_25
  aload_1
  aload_0
  getfield Dungeon/output Ljava/util/Vector;
  iload_2
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  checkcast java/util/Vector
  iload_3
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  invokevirtual java/lang/Object/toString()Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  iload_3
  iconst_1
  iadd
  dup
  istore_3
  pop
  goto start_24
  stop_25:
  aload_1
  ldc ""
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_20
  stop_21:
  return
.end method

.method public createRoom()V
  .limit locals 5
  .limit stack 3
  aload_0
  getfield Dungeon/infos LDungeonInfos;
  invokevirtual DungeonInfos/getDimension()LCustomPoint;
  dup
  astore_3
  pop
  iconst_0
  dup
  istore_1
  pop
  start_0:
  iload_1
  aload_3
  invokevirtual CustomPoint/getY()I
  iconst_2
  iadd
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  new java/util/Vector
  dup
  invokenonvirtual java/util/Vector/<init>()V
  dup
  astore 4
  pop
  iconst_0
  dup
  istore_2
  pop
  start_4:
  iload_2
  aload_3
  invokevirtual CustomPoint/getX()I
  iconst_2
  iadd
  if_icmplt true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq stop_5
  iload_1
  iconst_0
  if_icmpeq true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  dup
  ifeq false_13
  pop
  iload_2
  iconst_0
  if_icmpeq true_16
  iconst_0
  goto stop_17
  true_16:
  iconst_1
  stop_17:
  false_13:
  dup
  ifne true_12
  pop
  iload_1
  aload_3
  invokevirtual CustomPoint/getY()I
  iconst_1
  iadd
  if_icmpeq true_19
  iconst_0
  goto stop_20
  true_19:
  iconst_1
  stop_20:
  dup
  ifeq false_18
  pop
  iload_2
  iconst_0
  if_icmpeq true_21
  iconst_0
  goto stop_22
  true_21:
  iconst_1
  stop_22:
  false_18:
  true_12:
  dup
  ifne true_11
  pop
  iload_1
  iconst_0
  if_icmpeq true_24
  iconst_0
  goto stop_25
  true_24:
  iconst_1
  stop_25:
  dup
  ifeq false_23
  pop
  iload_2
  aload_3
  invokevirtual CustomPoint/getX()I
  iconst_1
  iadd
  if_icmpeq true_26
  iconst_0
  goto stop_27
  true_26:
  iconst_1
  stop_27:
  false_23:
  true_11:
  dup
  ifne true_10
  pop
  iload_1
  aload_3
  invokevirtual CustomPoint/getY()I
  iconst_1
  iadd
  if_icmpeq true_29
  iconst_0
  goto stop_30
  true_29:
  iconst_1
  stop_30:
  dup
  ifeq false_28
  pop
  iload_2
  aload_3
  invokevirtual CustomPoint/getX()I
  iconst_1
  iadd
  if_icmpeq true_31
  iconst_0
  goto stop_32
  true_31:
  iconst_1
  stop_32:
  false_28:
  true_10:
  ifeq else_8
  aload 4
  ldc "+"
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  goto stop_9
  else_8:
  iload_1
  iconst_0
  if_icmpeq true_36
  iconst_0
  goto stop_37
  true_36:
  iconst_1
  stop_37:
  dup
  ifne true_35
  pop
  iload_1
  aload_3
  invokevirtual CustomPoint/getY()I
  iconst_1
  iadd
  if_icmpeq true_38
  iconst_0
  goto stop_39
  true_38:
  iconst_1
  stop_39:
  true_35:
  ifeq else_33
  aload 4
  ldc "-"
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  goto stop_34
  else_33:
  iload_2
  iconst_0
  if_icmpeq true_43
  iconst_0
  goto stop_44
  true_43:
  iconst_1
  stop_44:
  dup
  ifne true_42
  pop
  iload_2
  aload_3
  invokevirtual CustomPoint/getX()I
  iconst_1
  iadd
  if_icmpeq true_45
  iconst_0
  goto stop_46
  true_45:
  iconst_1
  stop_46:
  true_42:
  ifeq else_40
  aload 4
  ldc "|"
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  goto stop_41
  else_40:
  aload 4
  ldc " "
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  stop_41:
  stop_34:
  stop_9:
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_4
  stop_5:
  aload_0
  getfield Dungeon/output Ljava/util/Vector;
  aload 4
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
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

.method public createItem(Ljava/lang/String;LCustomPoint;)V
  .limit locals 4
  .limit stack 3
  aload_0
  getfield Dungeon/output Ljava/util/Vector;
  aload_2
  invokevirtual CustomPoint/getY()I
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  checkcast java/util/Vector
  dup
  astore_3
  pop
  aload_3
  aload_1
  aload_2
  invokevirtual CustomPoint/getX()I
  invokevirtual java/util/Vector/setElementAt(Ljava/lang/Object;I)V
  return
.end method

