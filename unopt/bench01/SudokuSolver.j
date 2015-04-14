.class public abstract SudokuSolver

.super java/lang/Object

.field protected grid Ljava/util/Vector;

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  return
.end method

.method public parse(Ljoos/lib/JoosIO;)V
  .limit locals 8
  .limit stack 4
  new java/util/Vector
  dup
  ldc 9
  invokenonvirtual java/util/Vector/<init>(I)V
  dup
  aload_0
  swap
  putfield SudokuSolver/grid Ljava/util/Vector;
  pop
  iconst_0
  dup
  istore_2
  pop
  start_0:
  iload_2
  ldc 9
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  aload_0
  getfield SudokuSolver/grid Ljava/util/Vector;
  new java/util/Vector
  dup
  ldc 9
  invokenonvirtual java/util/Vector/<init>(I)V
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  iconst_0
  dup
  istore_3
  pop
  start_4:
  iload_3
  ldc 9
  if_icmplt true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq stop_5
  aload_0
  getfield SudokuSolver/grid Ljava/util/Vector;
  iload_2
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  dup
  astore 4
  pop
  aload 4
  checkcast java/util/Vector
  dup
  astore 5
  pop
  aload_1
  invokevirtual joos/lib/JoosIO/readInt()I
  dup
  istore 6
  pop
  new java/lang/Integer
  dup
  iload 6
  invokenonvirtual java/lang/Integer/<init>(I)V
  dup
  astore 7
  pop
  aload 5
  aload 7
  invokevirtual java/util/Vector/addElement(Ljava/lang/Object;)V
  iload_3
  iconst_1
  iadd
  dup
  istore_3
  pop
  goto start_4
  stop_5:
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_0
  stop_1:
  return
.end method

.method public abstract solve()V
.end method

.method public print(Ljoos/lib/JoosIO;)V
  .limit locals 12
  .limit stack 3
  iconst_0
  dup
  istore 4
  pop
  iconst_0
  dup
  istore_2
  pop
  start_0:
  iload_2
  ldc 12
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  iconst_0
  dup
  istore 5
  pop
  iconst_0
  dup
  istore_3
  pop
  start_4:
  iload_3
  ldc 13
  if_icmplt true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq stop_5
  iload_2
  iconst_0
  if_icmpeq true_12
  iconst_0
  goto stop_13
  true_12:
  iconst_1
  stop_13:
  dup
  ifne true_11
  pop
  iload_2
  iconst_4
  if_icmpeq true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  true_11:
  dup
  ifne true_10
  pop
  iload_2
  ldc 8
  if_icmpeq true_16
  iconst_0
  goto stop_17
  true_16:
  iconst_1
  stop_17:
  true_10:
  ifeq else_8
  aload_1
  ldc "|"
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  goto stop_9
  else_8:
  iload_3
  iconst_0
  if_icmpeq true_23
  iconst_0
  goto stop_24
  true_23:
  iconst_1
  stop_24:
  dup
  ifne true_22
  pop
  iload_3
  iconst_4
  if_icmpeq true_25
  iconst_0
  goto stop_26
  true_25:
  iconst_1
  stop_26:
  true_22:
  dup
  ifne true_21
  pop
  iload_3
  ldc 8
  if_icmpeq true_27
  iconst_0
  goto stop_28
  true_27:
  iconst_1
  stop_28:
  true_21:
  dup
  ifne true_20
  pop
  iload_3
  ldc 12
  if_icmpeq true_29
  iconst_0
  goto stop_30
  true_29:
  iconst_1
  stop_30:
  true_20:
  ifeq else_18
  aload_1
  ldc "|"
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  goto stop_19
  else_18:
  aload_0
  getfield SudokuSolver/grid Ljava/util/Vector;
  iload 4
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  dup
  astore 10
  pop
  aload 10
  checkcast java/util/Vector
  dup
  astore 11
  pop
  aload 11
  iload 5
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  dup
  astore 6
  pop
  iload 5
  iconst_1
  iadd
  dup
  istore 5
  pop
  new java/lang/Integer
  dup
  iconst_0
  invokenonvirtual java/lang/Integer/<init>(I)V
  dup
  astore 8
  pop
  aload 6
  checkcast java/lang/Integer
  dup
  astore 7
  pop
  aload 7
  aload 8
  if_acmpne true_33
  iconst_0
  goto stop_34
  true_33:
  iconst_1
  stop_34:
  ifeq else_31
  aload 7
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  dup
  astore 9
  pop
  aload_1
  aload 9
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  goto stop_32
  else_31:
  aload_1
  ldc " "
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  stop_32:
  stop_19:
  stop_9:
  iload_3
  iconst_1
  iadd
  dup
  istore_3
  pop
  goto start_4
  stop_5:
  iload_2
  iconst_0
  if_icmpne true_38
  iconst_0
  goto stop_39
  true_38:
  iconst_1
  stop_39:
  dup
  ifeq false_37
  pop
  iload_2
  iconst_4
  if_icmpne true_40
  iconst_0
  goto stop_41
  true_40:
  iconst_1
  stop_41:
  false_37:
  dup
  ifeq false_36
  pop
  iload_2
  ldc 8
  if_icmpne true_42
  iconst_0
  goto stop_43
  true_42:
  iconst_1
  stop_43:
  false_36:
  ifeq stop_35
  iload 4
  iconst_1
  iadd
  dup
  istore 4
  pop
  stop_35:
  aload_1
  ldc "\n"
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_0
  stop_1:
  return
.end method

