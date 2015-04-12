.class public CustomPoint

.super java/lang/Object

.field protected x I
.field protected y I

.method public <init>()V
  .limit locals 1
  .limit stack 3
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  iconst_0
  dup
  aload_0
  swap
  putfield CustomPoint/x I
  pop
  iconst_0
  dup
  aload_0
  swap
  putfield CustomPoint/y I
  pop
  return
.end method

.method public <init>(II)V
  .limit locals 3
  .limit stack 3
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  iload_1
  dup
  aload_0
  swap
  putfield CustomPoint/x I
  pop
  iload_2
  dup
  aload_0
  swap
  putfield CustomPoint/y I
  pop
  return
.end method

.method public getX()I
  .limit locals 1
  .limit stack 1
  aload_0
  getfield CustomPoint/x I
  ireturn
  nop
.end method

.method public getY()I
  .limit locals 1
  .limit stack 1
  aload_0
  getfield CustomPoint/y I
  ireturn
  nop
.end method

