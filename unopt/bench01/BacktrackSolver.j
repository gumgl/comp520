.class public BacktrackSolver

.super SudokuSolver

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual SudokuSolver/<init>()V
  return
.end method

.method public getVal(II)I
  .limit locals 8
  .limit stack 2
  aload_0
  getfield SudokuSolver/grid Ljava/util/Vector;
  iload_1
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  dup
  astore_3
  pop
  aload_3
  checkcast java/util/Vector
  dup
  astore 5
  pop
  aload 5
  iload_2
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  dup
  astore 4
  pop
  aload 4
  checkcast java/lang/Integer
  dup
  astore 6
  pop
  aload 6
  invokevirtual java/lang/Integer/intValue()I
  ireturn
  nop
.end method

.method public setVal(III)V
  .limit locals 9
  .limit stack 3
  aload_0
  getfield SudokuSolver/grid Ljava/util/Vector;
  iload_1
  invokevirtual java/util/Vector/elementAt(I)Ljava/lang/Object;
  dup
  astore 4
  pop
  aload 4
  checkcast java/util/Vector
  dup
  astore 7
  pop
  new java/lang/Integer
  dup
  iload_3
  invokenonvirtual java/lang/Integer/<init>(I)V
  dup
  astore 6
  pop
  aload 7
  aload 6
  iload_2
  invokevirtual java/util/Vector/setElementAt(Ljava/lang/Object;I)V
  return
.end method

.method public checkRow(II)Z
  .limit locals 4
  .limit stack 3
  iconst_0
  dup
  istore_3
  pop
  start_0:
  iload_3
  ldc 9
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  aload_0
  iload_1
  iload_3
  invokevirtual BacktrackSolver/getVal(II)I
  iload_2
  if_icmpeq true_5
  iconst_0
  goto stop_6
  true_5:
  iconst_1
  stop_6:
  ifeq stop_4
  iconst_0
  ireturn
  stop_4:
  iload_3
  iconst_1
  iadd
  dup
  istore_3
  pop
  goto start_0
  stop_1:
  iconst_1
  ireturn
  nop
.end method

.method public checkCol(II)Z
  .limit locals 4
  .limit stack 3
  iconst_0
  dup
  istore_3
  pop
  start_0:
  iload_3
  ldc 9
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  aload_0
  iload_3
  iload_1
  invokevirtual BacktrackSolver/getVal(II)I
  iload_2
  if_icmpeq true_5
  iconst_0
  goto stop_6
  true_5:
  iconst_1
  stop_6:
  ifeq stop_4
  iconst_0
  ireturn
  stop_4:
  iload_3
  iconst_1
  iadd
  dup
  istore_3
  pop
  goto start_0
  stop_1:
  iconst_1
  ireturn
  nop
.end method

.method public checkBox(III)Z
  .limit locals 6
  .limit stack 4
  iload_1
  iconst_3
  idiv
  iconst_3
  imul
  dup
  istore_1
  pop
  iload_2
  iconst_3
  idiv
  iconst_3
  imul
  dup
  istore_2
  pop
  iconst_0
  dup
  istore 4
  pop
  start_0:
  iload 4
  iconst_3
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
  start_4:
  iload 5
  iconst_3
  if_icmplt true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq stop_5
  aload_0
  iload_1
  iload 4
  iadd
  iload_2
  iload 5
  iadd
  invokevirtual BacktrackSolver/getVal(II)I
  iload_3
  if_icmpeq true_9
  iconst_0
  goto stop_10
  true_9:
  iconst_1
  stop_10:
  ifeq stop_8
  iconst_0
  ireturn
  stop_8:
  iload 5
  iconst_1
  iadd
  dup
  istore 5
  pop
  goto start_4
  stop_5:
  iload 4
  iconst_1
  iadd
  dup
  istore 4
  pop
  goto start_0
  stop_1:
  iconst_1
  ireturn
  nop
.end method

.method public checkFinish()Z
  .limit locals 3
  .limit stack 3
  iconst_0
  dup
  istore_1
  pop
  start_0:
  iload_1
  ldc 9
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  iconst_0
  dup
  istore_2
  pop
  start_4:
  iload_2
  ldc 9
  if_icmplt true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq stop_5
  aload_0
  iload_1
  iload_2
  invokevirtual BacktrackSolver/getVal(II)I
  iconst_0
  if_icmpeq true_9
  iconst_0
  goto stop_10
  true_9:
  iconst_1
  stop_10:
  ifeq stop_8
  iconst_0
  ireturn
  stop_8:
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  goto start_4
  stop_5:
  iload_1
  iconst_1
  iadd
  dup
  istore_1
  pop
  goto start_0
  stop_1:
  iconst_1
  ireturn
  nop
.end method

.method public solveCell(II)V
  .limit locals 4
  .limit stack 4
  aload_0
  invokevirtual BacktrackSolver/checkFinish()Z
  ifeq stop_0
  return
  stop_0:
  aload_0
  iload_1
  iload_2
  invokevirtual BacktrackSolver/getVal(II)I
  iconst_0
  if_icmpne true_3
  iconst_0
  goto stop_4
  true_3:
  iconst_1
  stop_4:
  ifeq else_1
  aload_0
  iload_1
  iload_2
  invokevirtual BacktrackSolver/next(II)V
  goto stop_2
  else_1:
  iconst_1
  dup
  istore_3
  pop
  start_5:
  iload_3
  ldc 10
  if_icmplt true_7
  iconst_0
  goto stop_8
  true_7:
  iconst_1
  stop_8:
  ifeq stop_6
  aload_0
  iload_1
  iload_3
  invokevirtual BacktrackSolver/checkRow(II)Z
  dup
  ifeq false_11
  pop
  aload_0
  iload_2
  iload_3
  invokevirtual BacktrackSolver/checkCol(II)Z
  false_11:
  dup
  ifeq false_10
  pop
  aload_0
  iload_1
  iload_2
  iload_3
  invokevirtual BacktrackSolver/checkBox(III)Z
  false_10:
  ifeq stop_9
  aload_0
  invokevirtual BacktrackSolver/checkFinish()Z
  ifeq true_13
  iconst_0
  goto stop_14
  true_13:
  iconst_1
  stop_14:
  ifeq stop_12
  aload_0
  iload_1
  iload_2
  iload_3
  invokevirtual BacktrackSolver/setVal(III)V
  stop_12:
  aload_0
  iload_1
  iload_2
  invokevirtual BacktrackSolver/next(II)V
  stop_9:
  iload_3
  iconst_1
  iadd
  dup
  istore_3
  pop
  goto start_5
  stop_6:
  aload_0
  invokevirtual BacktrackSolver/checkFinish()Z
  ifeq true_16
  iconst_0
  goto stop_17
  true_16:
  iconst_1
  stop_17:
  ifeq stop_15
  aload_0
  iload_1
  iload_2
  iconst_0
  invokevirtual BacktrackSolver/setVal(III)V
  stop_15:
  stop_2:
  return
.end method

.method public next(II)V
  .limit locals 3
  .limit stack 4
  iload_2
  ldc 8
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  aload_0
  iload_1
  iload_2
  iconst_1
  iadd
  invokevirtual BacktrackSolver/solveCell(II)V
  goto stop_1
  else_0:
  aload_0
  iload_1
  iconst_1
  iadd
  iconst_0
  invokevirtual BacktrackSolver/solveCell(II)V
  stop_1:
  return
.end method

.method public solve()V
  .limit locals 1
  .limit stack 3
  aload_0
  iconst_0
  iconst_0
  invokevirtual BacktrackSolver/solveCell(II)V
  return
.end method

