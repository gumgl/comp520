.class public KillRowAndColumnMove

.super Move

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual Move/<init>()V
  return
.end method

.method public apply(IILBoard;Ljava/lang/Character;)V
  .limit locals 7
  .limit stack 3
  iconst_0
  dup
  istore 5
  pop
  start_0:
  iload 5
  aload_3
  invokevirtual Board/getWidth()I
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq stop_1
  aload_3
  iload 5
  iload_2
  invokevirtual Board/clear(II)V
  iload 5
  iconst_1
  iadd
  dup
  istore 5
  pop
  goto start_0
  stop_1:
  iconst_0
  dup
  istore 6
  pop
  start_4:
  iload 6
  aload_3
  invokevirtual Board/getHeight()I
  if_icmplt true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq stop_5
  aload_3
  iload_1
  iload 6
  invokevirtual Board/clear(II)V
  iload 6
  iconst_1
  iadd
  dup
  istore 6
  pop
  goto start_4
  stop_5:
  return
.end method

.method public toString()Ljava/lang/String;
  .limit locals 1
  .limit stack 1
  ldc "Kill Row & Column: clears the row and column where the token lands."
  areturn
  nop
.end method

