.class public GirlFriend

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
  aload_2
  ldc "beautiful"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_8
  pop
  aload_2
  ldc "sexy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_8:
  dup
  ifeq false_7
  pop
  aload_3
  ldc "dream"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_9
  pop
  aload_3
  ldc "sunshine"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_9:
  false_7:
  dup
  ifeq false_6
  pop
  aload 4
  ldc "puke"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  false_6:
  ifeq else_4
  ldc "that's the best complement a man has ever gave me!"
  areturn
  goto stop_5
  else_4:
  aload_3
  ldc "raccoon"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_14
  pop
  aload 4
  ldc "puke"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_14:
  ifeq else_12
  ldc "that's the most disgusting thing I've ever heard."
  areturn
  goto stop_13
  else_12:
  ldc "You'll have to do better than that."
  areturn
  stop_13:
  stop_5:
  goto stop_1
  else_0:
  aload_0
  getfield Woman/complementNumber I
  iconst_1
  if_icmpeq true_17
  iconst_0
  goto stop_18
  true_17:
  iconst_1
  stop_18:
  ifeq else_15
  aload_3
  ldc "skin"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifeq false_21
  pop
  aload 4
  ldc "sleep"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_22
  pop
  aload 4
  ldc "eat-banana"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_22:
  false_21:
  ifeq else_19
  ldc "you touched the very botttom of my soul."
  areturn
  goto stop_20
  else_19:
  aload_3
  ldc "niece"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_23
  ldc "you sick bastard, stay away from my family."
  areturn
  goto stop_24
  else_23:
  ldc "that don't impresse me much."
  areturn
  stop_24:
  stop_20:
  goto stop_16
  else_15:
  aload_0
  getfield Woman/complementNumber I
  iconst_2
  if_icmpeq true_27
  iconst_0
  goto stop_28
  true_27:
  iconst_1
  stop_28:
  ifeq else_25
  aload_3
  ldc "dream"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifeq false_31
  pop
  aload_2
  ldc "beautiful"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_32
  pop
  aload_2
  ldc "sexy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_32:
  false_31:
  ifeq else_29
  ldc "My oh my, what a gentelmen.."
  areturn
  goto stop_30
  else_29:
  aload_3
  ldc "raccoon"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_35
  pop
  aload_2
  ldc "enormous"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_35:
  ifeq else_33
  ldc "Go to hell!!"
  areturn
  goto stop_34
  else_33:
  ldc "Try again, mister.."
  areturn
  stop_34:
  stop_30:
  goto stop_26
  else_25:
  aload_0
  getfield Woman/complementNumber I
  iconst_3
  if_icmpeq true_38
  iconst_0
  goto stop_39
  true_38:
  iconst_1
  stop_39:
  ifeq else_36
  aload_2
  ldc "dead"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_44
  pop
  aload_2
  ldc "blind"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_44:
  dup
  ifne true_43
  pop
  aload_2
  ldc "crappy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_43:
  dup
  ifeq false_42
  pop
  aload 4
  ldc "cry"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_46
  pop
  aload 4
  ldc "work"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_46:
  dup
  ifne true_45
  pop
  aload 4
  ldc "sleep"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_45:
  false_42:
  ifeq else_40
  ldc "You are the man of my dreams (-: "
  areturn
  goto stop_41
  else_40:
  aload 4
  ldc "eat-bananas"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_49
  pop
  aload 4
  ldc "puke"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_49:
  ifeq else_47
  ldc "you are a freak. you know that, right?"
  areturn
  goto stop_48
  else_47:
  ldc "nice try, but that won't cut it."
  areturn
  stop_48:
  stop_41:
  goto stop_37
  else_36:
  aload_0
  getfield Woman/complementNumber I
  iconst_4
  if_icmpeq true_52
  iconst_0
  goto stop_53
  true_52:
  iconst_1
  stop_53:
  ifeq else_50
  aload_3
  ldc "niece"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_58
  pop
  aload_3
  ldc "dream"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_58:
  dup
  ifne true_57
  pop
  aload_3
  ldc "skin"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_57:
  dup
  ifeq false_56
  pop
  aload 4
  ldc "cry"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_59
  pop
  aload 4
  ldc "be-pleased"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_59:
  false_56:
  ifeq else_54
  ldc "take me, I'm yours!"
  areturn
  goto stop_55
  else_54:
  aload_3
  ldc "fruit"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_60
  ldc "you pervert, leave me alone!"
  areturn
  goto stop_61
  else_60:
  ldc "I don't think so."
  areturn
  stop_61:
  stop_55:
  goto stop_51
  else_50:
  aload_0
  getfield Woman/complementNumber I
  iconst_5
  if_icmpeq true_64
  iconst_0
  goto stop_65
  true_64:
  iconst_1
  stop_65:
  ifeq else_62
  aload_3
  ldc "tragedy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_66
  aload_2
  ldc "dead"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_68
  ldc "that's the best complement a man has ever gave me!"
  areturn
  goto stop_69
  else_68:
  ldc "that's a bit weird, but good enough.."
  areturn
  stop_69:
  goto stop_67
  else_66:
  aload_3
  ldc "fruit"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_72
  pop
  aload_3
  ldc "work-ethic"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_72:
  ifeq else_70
  ldc "man, you are not making any sence.."
  areturn
  goto stop_71
  else_70:
  ldc "move along, mister."
  areturn
  stop_71:
  stop_67:
  goto stop_63
  else_62:
  aload_0
  getfield Woman/complementNumber I
  ldc 6
  if_icmpeq true_75
  iconst_0
  goto stop_76
  true_75:
  iconst_1
  stop_76:
  ifeq else_73
  aload_2
  ldc "beautifull"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_81
  pop
  aload_2
  ldc "lovely"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_81:
  dup
  ifeq false_80
  pop
  aload 4
  ldc "flourish"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  false_80:
  dup
  ifeq false_79
  pop
  aload_3
  ldc "dream"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_82
  pop
  aload_3
  ldc "summer's-day"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_82:
  false_79:
  ifeq else_77
  ldc "damn, you are god. take me home with you."
  areturn
  goto stop_78
  else_77:
  aload 4
  ldc "rot"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_83
  ldc "'rot'? you stink at giving complements!"
  areturn
  goto stop_84
  else_83:
  ldc "When hell frizes."
  areturn
  stop_84:
  stop_78:
  goto stop_74
  else_73:
  aload_0
  getfield Woman/complementNumber I
  ldc 7
  if_icmpeq true_87
  iconst_0
  goto stop_88
  true_87:
  iconst_1
  stop_88:
  ifeq else_85
  aload_2
  ldc "better"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  ifeq else_89
  ldc "that's a bit wierd, but not to bad.."
  areturn
  goto stop_90
  else_89:
  aload_2
  ldc "dead"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_93
  pop
  aload_2
  ldc "blind"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_93:
  ifeq else_91
  ldc "are you trying to insult me or to complement me?"
  areturn
  goto stop_92
  else_91:
  ldc "na, I don't think so."
  areturn
  stop_92:
  stop_90:
  goto stop_86
  else_85:
  aload_0
  getfield Woman/complementNumber I
  ldc 8
  if_icmpeq true_96
  iconst_0
  goto stop_97
  true_96:
  iconst_1
  stop_97:
  ifeq else_94
  aload_2
  ldc "enormous"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_101
  pop
  aload_2
  ldc "beautiful"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_101:
  dup
  ifeq false_100
  pop
  aload_3
  ldc "skin"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  false_100:
  ifeq else_98
  ldc "you are amazing!"
  areturn
  goto stop_99
  else_98:
  aload_3
  ldc "raccoon"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_104
  pop
  aload_3
  ldc "fruit"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_104:
  ifeq else_102
  ldc "Grose, dude."
  areturn
  goto stop_103
  else_102:
  ldc "no way, man."
  areturn
  stop_103:
  stop_99:
  goto stop_95
  else_94:
  aload_0
  getfield Woman/complementNumber I
  ldc 9
  if_icmpeq true_107
  iconst_0
  goto stop_108
  true_107:
  iconst_1
  stop_108:
  ifeq else_105
  aload_2
  ldc "beautiful"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_111
  pop
  aload_2
  ldc "sexy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_111:
  ifeq else_109
  ldc "A strange proposal, but whu not!"
  areturn
  goto stop_110
  else_109:
  ldc "you are out of line here, mister."
  areturn
  stop_110:
  goto stop_106
  else_105:
  aload_0
  getfield Woman/complementNumber I
  ldc 10
  if_icmpeq true_114
  iconst_0
  goto stop_115
  true_114:
  iconst_1
  stop_115:
  ifeq else_112
  aload_2
  ldc "skin"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_118
  pop
  aload_2
  ldc "sunshine"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_118:
  ifeq else_116
  ldc "you are a complement mechine!"
  areturn
  goto stop_117
  else_116:
  aload_3
  ldc "raccoon"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifne true_121
  pop
  aload_3
  ldc "fruit"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_121:
  ifeq else_119
  ldc "I'm calling the cops."
  areturn
  goto stop_120
  else_119:
  ldc "ah, I don't think so."
  areturn
  stop_120:
  stop_117:
  goto stop_113
  else_112:
  aload_3
  ldc "sunshine"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  dup
  ifeq false_124
  pop
  aload_2
  ldc "happy"
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  false_124:
  ifeq else_122
  ldc "You are good!"
  areturn
  goto stop_123
  else_122:
  ldc "ahm, I think you should try again."
  areturn
  stop_123:
  stop_113:
  stop_106:
  stop_95:
  stop_86:
  stop_74:
  stop_63:
  stop_51:
  stop_37:
  stop_26:
  stop_16:
  stop_1:
  nop
.end method

