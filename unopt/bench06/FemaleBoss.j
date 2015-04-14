.class public FemaleBoss

.super Woman

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual Woman/<init>()V
  return
.end method

.method public react(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
  .limit locals 5
  .limit stack 2
  aload_0
  getfield Woman/complementNumber I
  iconst_0
  if_icmpeq true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  ldc "This is highly inappropriate."
  areturn
  goto stop_1
  else_0:
  aload_0
  getfield Woman/complementNumber I
  iconst_1
  if_icmpeq true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq else_4
  aload_3
  ldc "work-ethic"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_11
  pop
  aload_3
  ldc "dream"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_11:
  dup
  ifeq false_10
  pop
  aload 4
  ldc "work"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_12
  pop
  aload 4
  ldc "program"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_12:
  false_10:
  ifeq else_8
  ldc "I feel a promotion coming your way!!"
  areturn
  goto stop_9
  else_8:
  ldc "get out of my office."
  areturn
  stop_9:
  goto stop_5
  else_4:
  aload_0
  getfield Woman/complementNumber I
  iconst_2
  if_icmpeq true_15
  iconst_0
  goto stop_16
  true_15:
  iconst_1
  stop_16:
  ifeq else_13
  ldc "are you familiar with the term sexual harassment?"
  areturn
  goto stop_14
  else_13:
  aload_0
  getfield Woman/complementNumber I
  iconst_3
  if_icmpeq true_19
  iconst_0
  goto stop_20
  true_19:
  iconst_1
  stop_20:
  ifeq else_17
  ldc "I am your boss, you know."
  areturn
  goto stop_18
  else_17:
  aload_0
  getfield Woman/complementNumber I
  iconst_4
  if_icmpeq true_23
  iconst_0
  goto stop_24
  true_23:
  iconst_1
  stop_24:
  ifeq else_21
  aload_3
  ldc "work-ethic"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifeq false_27
  pop
  aload_2
  ldc "sexy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq true_28
  iconst_0
  goto stop_29
  true_28:
  iconst_1
  stop_29:
  false_27:
  ifeq else_25
  ldc "I'm impressed, you are hired."
  areturn
  goto stop_26
  else_25:
  ldc "get out of my office, you sick sick man."
  areturn
  stop_26:
  goto stop_22
  else_21:
  aload_0
  getfield Woman/complementNumber I
  iconst_5
  if_icmpeq true_32
  iconst_0
  goto stop_33
  true_32:
  iconst_1
  stop_33:
  ifeq else_30
  ldc "If I was your girlfriend, that might have been funny. you are fired."
  areturn
  goto stop_31
  else_30:
  aload_0
  getfield Woman/complementNumber I
  ldc 6
  if_icmpeq true_36
  iconst_0
  goto stop_37
  true_36:
  iconst_1
  stop_37:
  ifeq else_34
  ldc "who the hell do you think you are talking to??"
  areturn
  goto stop_35
  else_34:
  aload_0
  getfield Woman/complementNumber I
  ldc 7
  if_icmpeq true_40
  iconst_0
  goto stop_41
  true_40:
  iconst_1
  stop_41:
  ifeq else_38
  aload_2
  ldc "better"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_46
  pop
  aload_2
  ldc "happy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_46:
  dup
  ifne true_45
  pop
  aload_2
  ldc "talented"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_45:
  dup
  ifeq false_44
  pop
  aload 4
  ldc "work"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_47
  pop
  aload 4
  ldc "program"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_47:
  false_44:
  ifeq else_42
  ldc "you have a slippery tongue..well done"
  areturn
  goto stop_43
  else_42:
  ldc "get out of my office."
  areturn
  stop_43:
  goto stop_39
  else_38:
  aload_0
  getfield Woman/complementNumber I
  ldc 8
  if_icmpeq true_50
  iconst_0
  goto stop_51
  true_50:
  iconst_1
  stop_51:
  ifeq else_48
  aload_3
  ldc "work-ethic"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_52
  ldc "that's a strange proposal, but Ive heard worse.."
  areturn
  goto stop_53
  else_52:
  ldc "you are going to have hard time keeping your job like that."
  areturn
  stop_53:
  goto stop_49
  else_48:
  aload_0
  getfield Woman/complementNumber I
  ldc 9
  if_icmpeq true_56
  iconst_0
  goto stop_57
  true_56:
  iconst_1
  stop_57:
  ifeq else_54
  aload 4
  ldc "work"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_61
  pop
  aload 4
  ldc "program"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_61:
  dup
  ifeq false_60
  pop
  aload_2
  ldc "happy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_62
  pop
  aload_2
  ldc "talented"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_62:
  false_60:
  ifeq else_58
  ldc "let's indeed! you are a very talented young man.."
  areturn
  goto stop_59
  else_58:
  ldc "are you familiar with the term sexual harassment?"
  areturn
  stop_59:
  goto stop_55
  else_54:
  aload_0
  getfield Woman/complementNumber I
  ldc 10
  if_icmpeq true_65
  iconst_0
  goto stop_66
  true_65:
  iconst_1
  stop_66:
  ifeq else_63
  aload_3
  ldc "work-ethic"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_70
  pop
  aload_3
  ldc "problems"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_70:
  dup
  ifne true_69
  pop
  aload_3
  ldc "dream"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_69:
  ifeq else_67
  ldc "I don't understand exactly what you want, but whatever."
  areturn
  goto stop_68
  else_67:
  ldc "get out of my office, you pervert."
  areturn
  stop_68:
  goto stop_64
  else_63:
  ldc "WTF??"
  areturn
  stop_64:
  stop_55:
  stop_49:
  stop_39:
  stop_35:
  stop_31:
  stop_22:
  stop_18:
  stop_14:
  stop_5:
  stop_1:
  nop
.end method

