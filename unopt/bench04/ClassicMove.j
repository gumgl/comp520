.class public ClassicMove

.super Move

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual Move/<init>()V
  return
.end method

.method public apply(IILBoard;Ljava/lang/Character;)V
  .limit locals 5
  .limit stack 3
  aload_3
  iload_1
  aload 4
  invokevirtual Board/place(ILjava/lang/Character;)V
  return
.end method

.method public toString()Ljava/lang/String;
  .limit locals 1
  .limit stack 1
  ldc "Classic: drops a token in a chosen column."
  areturn
  nop
.end method

