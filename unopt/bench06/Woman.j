.class public abstract Woman

.super java/lang/Object

.field protected complementNumber I

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  return
.end method

.method public final setComplementNumber(I)V
  .limit locals 2
  .limit stack 3
  iload_1
  dup
  aload_0
  swap
  putfield Woman/complementNumber I
  pop
  return
.end method

.method public abstract react(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
.end method

