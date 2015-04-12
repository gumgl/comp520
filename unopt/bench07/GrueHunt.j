.class public GrueHunt

.super java/lang/Object

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  return
.end method

.method public testLoop(Ljoos/lib/JoosIO;Z)Ljava/lang/String;
  .limit locals 4
  .limit stack 2
  aconst_null
  dup
  astore_3
  pop
  iload_2
  ifeq true_1
  iconst_0
  goto stop_2
  true_1:
  iconst_1
  stop_2:
  ifeq stop_0
  aload_1
  invokevirtual joos/lib/JoosIO/readLine()Ljava/lang/String;
  dup
  astore_3
  pop
  stop_0:
  aload_3
  areturn
  nop
.end method

.method public static main([Ljava/lang/String;)V
  .limit locals 16
  .limit stack 5
  new GrueHunt
  dup
  invokenonvirtual GrueHunt/<init>()V
  dup
  astore 14
  pop
  iconst_0
  dup
  istore 8
  pop
  ldc "╔═╝┏━┓╻ ╻┏━╸ ║ ║╻ ╻┏┓╻╺┳╸\n║ ║┣┳┛┃ ┃┣╸  ╔═║┃ ┃┃┗┫ ┃ \n══╝╹┗╸┗━┛┗━╸ ╝ ╝┗━┛╹ ╹ ╹ "
  dup
  astore 11
  pop
  ldc 20
  dup
  istore 13
  pop
  ldc 100
  dup
  istore 7
  pop
  new joos/lib/JoosIO
  dup
  invokenonvirtual joos/lib/JoosIO/<init>()V
  dup
  astore_1
  pop
  ldc 11
  dup
  istore 6
  pop
  new Room
  dup
  aconst_null
  ldc "null"
  iload 6
  invokenonvirtual Room/<init>(LRoom;Ljava/lang/String;I)V
  dup
  astore 12
  pop
  iconst_0
  dup
  istore_2
  pop
  iconst_0
  dup
  istore_3
  pop
  ldc 10
  dup
  istore 4
  pop
  aload_1
  ldc "\n"
  dup
  ifnull null_0
  goto stop_1
  null_0:
  pop
  ldc "null"
  stop_1:
  aload 11
  dup
  ifnull null_2
  goto stop_3
  null_2:
  pop
  ldc "null"
  stop_3:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_1
  ldc "\n"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_1
  ldc "Welcome to GrueHunt! Please don't feed the grues."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_1
  ldc "What is your name?"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_1
  invokevirtual joos/lib/JoosIO/readLine()Ljava/lang/String;
  dup
  astore 9
  pop
  aload 9
  aconst_null
  if_acmpeq true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  dup
  ifne true_5
  pop
  aload 9
  ldc ""
  invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
  true_5:
  ifeq stop_4
  ldc "Dave"
  dup
  astore 9
  pop
  stop_4:
  aload 12
  invokevirtual Room/enterRoom()V
  start_8:
  aload 14
  aload_1
  iload 8
  invokevirtual GrueHunt/testLoop(Ljoos/lib/JoosIO;Z)Ljava/lang/String;
  dup
  astore 10
  aconst_null
  if_acmpne true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  ifeq stop_9
  aload_1
  ldc ""
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload 10
  ldc "give up"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  ifeq else_12
  aload_1
  ldc "You have given up, "
  dup
  ifnull null_18
  goto stop_19
  null_18:
  pop
  ldc "null"
  stop_19:
  aload 9
  dup
  ifnull null_20
  goto stop_21
  null_20:
  pop
  ldc "null"
  stop_21:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_16
  goto stop_17
  null_16:
  pop
  ldc "null"
  stop_17:
  ldc "."
  dup
  ifnull null_22
  goto stop_23
  null_22:
  pop
  ldc "null"
  stop_23:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_1
  ldc "You spend the rest of your days wandering this bizarre world. Better luck next time!"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  iconst_1
  dup
  istore 8
  pop
  goto stop_13
  else_12:
  aload 10
  ldc "north"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_26
  iconst_0
  goto stop_27
  true_26:
  iconst_1
  stop_27:
  ifeq else_24
  aload 12
  invokevirtual Room/getNorth()LRoom;
  dup
  astore 15
  pop
  aload 15
  aconst_null
  if_acmpeq true_30
  iconst_0
  goto stop_31
  true_30:
  iconst_1
  stop_31:
  ifeq else_28
  aload_1
  ldc "There is no exit that way."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_29
  else_28:
  aload 15
  dup
  astore 12
  pop
  aload_1
  ldc "You go north."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_29:
  goto stop_25
  else_24:
  aload 10
  ldc "south"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_34
  iconst_0
  goto stop_35
  true_34:
  iconst_1
  stop_35:
  ifeq else_32
  aload 12
  invokevirtual Room/getSouth()LRoom;
  dup
  astore 15
  pop
  aload 15
  aconst_null
  if_acmpeq true_38
  iconst_0
  goto stop_39
  true_38:
  iconst_1
  stop_39:
  ifeq else_36
  aload_1
  ldc "There is no exit that way."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_37
  else_36:
  aload 15
  dup
  astore 12
  pop
  aload_1
  ldc "You go south."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_37:
  goto stop_33
  else_32:
  aload 10
  ldc "west"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_42
  iconst_0
  goto stop_43
  true_42:
  iconst_1
  stop_43:
  ifeq else_40
  aload 12
  invokevirtual Room/getWest()LRoom;
  dup
  astore 15
  pop
  aload 15
  aconst_null
  if_acmpeq true_46
  iconst_0
  goto stop_47
  true_46:
  iconst_1
  stop_47:
  ifeq else_44
  aload_1
  ldc "There is no exit that way."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_45
  else_44:
  aload 15
  dup
  astore 12
  pop
  aload_1
  ldc "You go west."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_45:
  goto stop_41
  else_40:
  aload 10
  ldc "east"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_50
  iconst_0
  goto stop_51
  true_50:
  iconst_1
  stop_51:
  ifeq else_48
  aload 12
  invokevirtual Room/getEast()LRoom;
  dup
  astore 15
  pop
  aload 15
  aconst_null
  if_acmpeq true_54
  iconst_0
  goto stop_55
  true_54:
  iconst_1
  stop_55:
  ifeq else_52
  aload_1
  ldc "There is no exit that way."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_53
  else_52:
  aload 15
  dup
  astore 12
  pop
  aload_1
  ldc "You go east."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_53:
  goto stop_49
  else_48:
  aload 12
  invokevirtual Room/getRoomAction()LRoomAction;
  aload 10
  invokevirtual RoomAction/performAction(Ljava/lang/String;)I
  dup
  istore 5
  pop
  iload 5
  iconst_0
  if_icmpeq true_58
  iconst_0
  goto stop_59
  true_58:
  iconst_1
  stop_59:
  ifeq else_56
  goto stop_57
  else_56:
  iload 5
  iconst_1
  if_icmpeq true_62
  iconst_0
  goto stop_63
  true_62:
  iconst_1
  stop_63:
  ifeq else_60
  iconst_0
  dup
  istore 4
  pop
  goto stop_61
  else_60:
  iload 5
  iconst_2
  if_icmpeq true_66
  iconst_0
  goto stop_67
  true_66:
  iconst_1
  stop_67:
  ifeq else_64
  aload_1
  aload 9
  dup
  ifnull null_68
  goto stop_69
  null_68:
  pop
  ldc "null"
  stop_69:
  ldc ".\n"
  dup
  ifnull null_70
  goto stop_71
  null_70:
  pop
  ldc "null"
  stop_71:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/print(Ljava/lang/String;)V
  goto stop_65
  else_64:
  iload 5
  iconst_3
  if_icmpeq true_74
  iconst_0
  goto stop_75
  true_74:
  iconst_1
  stop_75:
  ifeq else_72
  iload_3
  iconst_1
  iadd
  dup
  istore_3
  pop
  aload_1
  ldc "You have "
  dup
  ifnull null_78
  goto stop_79
  null_78:
  pop
  ldc "null"
  stop_79:
  new java/lang/Integer
  dup
  iload_3
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_76
  goto stop_77
  null_76:
  pop
  ldc "null"
  stop_77:
  ldc " treasure(s)."
  dup
  ifnull null_82
  goto stop_83
  null_82:
  pop
  ldc "null"
  stop_83:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_73
  else_72:
  iload 5
  iconst_4
  if_icmpeq true_86
  iconst_0
  goto stop_87
  true_86:
  iconst_1
  stop_87:
  ifeq else_84
  aload_1
  ldc "You have "
  dup
  ifnull null_90
  goto stop_91
  null_90:
  pop
  ldc "null"
  stop_91:
  new java/lang/Integer
  dup
  iload_3
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_88
  goto stop_89
  null_88:
  pop
  ldc "null"
  stop_89:
  ldc " treasure(s)."
  dup
  ifnull null_94
  goto stop_95
  null_94:
  pop
  ldc "null"
  stop_95:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_85
  else_84:
  iload 5
  iconst_5
  if_icmpeq true_98
  iconst_0
  goto stop_99
  true_98:
  iconst_1
  stop_99:
  ifeq else_96
  iload_2
  iconst_1
  iadd
  dup
  istore_2
  pop
  aload_1
  ldc "You have "
  dup
  ifnull null_102
  goto stop_103
  null_102:
  pop
  ldc "null"
  stop_103:
  new java/lang/Integer
  dup
  iload_2
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_100
  goto stop_101
  null_100:
  pop
  ldc "null"
  stop_101:
  ldc " coin(s)."
  dup
  ifnull null_106
  goto stop_107
  null_106:
  pop
  ldc "null"
  stop_107:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_97
  else_96:
  iload 5
  ldc 6
  if_icmpeq true_110
  iconst_0
  goto stop_111
  true_110:
  iconst_1
  stop_111:
  ifeq else_108
  aload_1
  ldc "You have "
  dup
  ifnull null_114
  goto stop_115
  null_114:
  pop
  ldc "null"
  stop_115:
  new java/lang/Integer
  dup
  iload_2
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_112
  goto stop_113
  null_112:
  pop
  ldc "null"
  stop_113:
  ldc " coin(s)."
  dup
  ifnull null_118
  goto stop_119
  null_118:
  pop
  ldc "null"
  stop_119:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_109
  else_108:
  iload 5
  ldc 7
  if_icmpeq true_122
  iconst_0
  goto stop_123
  true_122:
  iconst_1
  stop_123:
  ifeq else_120
  iload 4
  iconst_1
  iadd
  dup
  istore 4
  pop
  goto stop_121
  else_120:
  iload 5
  ldc 8
  if_icmpeq true_126
  iconst_0
  goto stop_127
  true_126:
  iconst_1
  stop_127:
  ifeq else_124
  iload 4
  iconst_2
  iadd
  dup
  istore 4
  pop
  goto stop_125
  else_124:
  iload 5
  ldc 9
  if_icmpeq true_130
  iconst_0
  goto stop_131
  true_130:
  iconst_1
  stop_131:
  ifeq else_128
  iload 4
  iconst_1
  isub
  dup
  istore 4
  pop
  goto stop_129
  else_128:
  iload 5
  ldc 10
  if_icmpeq true_134
  iconst_0
  goto stop_135
  true_134:
  iconst_1
  stop_135:
  ifeq else_132
  iload 4
  iconst_2
  isub
  dup
  istore 4
  pop
  goto stop_133
  else_132:
  iload 5
  ldc 11
  if_icmpeq true_138
  iconst_0
  goto stop_139
  true_138:
  iconst_1
  stop_139:
  ifeq else_136
  aload_1
  ldc "You have "
  dup
  ifnull null_142
  goto stop_143
  null_142:
  pop
  ldc "null"
  stop_143:
  new java/lang/Integer
  dup
  iload 4
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_140
  goto stop_141
  null_140:
  pop
  ldc "null"
  stop_141:
  ldc " hit points remaining."
  dup
  ifnull null_146
  goto stop_147
  null_146:
  pop
  ldc "null"
  stop_147:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_137
  else_136:
  iload 5
  ldc 12
  if_icmpeq true_149
  iconst_0
  goto stop_150
  true_149:
  iconst_1
  stop_150:
  ifeq stop_148
  aload 12
  ldc 100
  invokevirtual Room/randomRange(I)I
  dup
  istore 7
  pop
  iload 7
  iload 13
  if_icmplt true_153
  iconst_0
  goto stop_154
  true_153:
  iconst_1
  stop_154:
  ifeq else_151
  aload_1
  ldc "You've escaped this bizarre world! You are now free."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_152
  else_151:
  iload 6
  iconst_1
  isub
  dup
  istore 6
  pop
  iload 6
  iconst_5
  if_icmpeq true_156
  iconst_0
  goto stop_157
  true_156:
  iconst_1
  stop_157:
  ifeq stop_155
  ldc 11
  dup
  istore 6
  pop
  stop_155:
  new Room
  dup
  aconst_null
  ldc "null"
  iload 6
  invokenonvirtual Room/<init>(LRoom;Ljava/lang/String;I)V
  dup
  astore 12
  pop
  aload_1
  ldc "You suddenly notice the new place you find yourself."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_152:
  stop_148:
  stop_137:
  stop_133:
  stop_129:
  stop_125:
  stop_121:
  stop_109:
  stop_97:
  stop_85:
  stop_73:
  stop_65:
  stop_61:
  stop_57:
  iload 4
  ldc 15
  if_icmpgt true_159
  iconst_0
  goto stop_160
  true_159:
  iconst_1
  stop_160:
  ifeq stop_158
  ldc 15
  dup
  istore 4
  pop
  stop_158:
  iload 4
  iconst_1
  if_icmplt true_162
  iconst_0
  goto stop_163
  true_162:
  iconst_1
  stop_163:
  ifeq stop_161
  aload_1
  ldc "\n\n"
  dup
  ifnull null_166
  goto stop_167
  null_166:
  pop
  ldc "null"
  stop_167:
  aload 9
  dup
  ifnull null_168
  goto stop_169
  null_168:
  pop
  ldc "null"
  stop_169:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_164
  goto stop_165
  null_164:
  pop
  ldc "null"
  stop_165:
  ldc " died."
  dup
  ifnull null_170
  goto stop_171
  null_170:
  pop
  ldc "null"
  stop_171:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  iconst_0
  dup
  istore 4
  pop
  iconst_1
  dup
  istore 8
  pop
  stop_161:
  iload 7
  iload 13
  if_icmplt true_173
  iconst_0
  goto stop_174
  true_173:
  iconst_1
  stop_174:
  ifeq stop_172
  iconst_1
  dup
  istore 8
  pop
  stop_172:
  stop_49:
  stop_41:
  stop_33:
  stop_25:
  stop_13:
  iload 8
  ifeq true_176
  iconst_0
  goto stop_177
  true_176:
  iconst_1
  stop_177:
  ifeq stop_175
  aload_1
  ldc ""
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload 12
  invokevirtual Room/enterRoom()V
  stop_175:
  goto start_8
  stop_9:
  aload_1
  aload 9
  dup
  ifnull null_186
  goto stop_187
  null_186:
  pop
  ldc "null"
  stop_187:
  ldc " has accumulated "
  dup
  ifnull null_188
  goto stop_189
  null_188:
  pop
  ldc "null"
  stop_189:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_184
  goto stop_185
  null_184:
  pop
  ldc "null"
  stop_185:
  new java/lang/Integer
  dup
  iload_3
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_182
  goto stop_183
  null_182:
  pop
  ldc "null"
  stop_183:
  ldc " treasure(s) and "
  dup
  ifnull null_192
  goto stop_193
  null_192:
  pop
  ldc "null"
  stop_193:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_180
  goto stop_181
  null_180:
  pop
  ldc "null"
  stop_181:
  new java/lang/Integer
  dup
  iload_2
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_178
  goto stop_179
  null_178:
  pop
  ldc "null"
  stop_179:
  ldc " coin(s)."
  dup
  ifnull null_196
  goto stop_197
  null_196:
  pop
  ldc "null"
  stop_197:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_1
  aload 9
  dup
  ifnull null_202
  goto stop_203
  null_202:
  pop
  ldc "null"
  stop_203:
  ldc " has "
  dup
  ifnull null_204
  goto stop_205
  null_204:
  pop
  ldc "null"
  stop_205:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_200
  goto stop_201
  null_200:
  pop
  ldc "null"
  stop_201:
  new java/lang/Integer
  dup
  iload 4
  invokenonvirtual java/lang/Integer/<init>(I)V
  invokevirtual java/lang/Integer/toString()Ljava/lang/String;
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_198
  goto stop_199
  null_198:
  pop
  ldc "null"
  stop_199:
  ldc " hit points remaining."
  dup
  ifnull null_208
  goto stop_209
  null_208:
  pop
  ldc "null"
  stop_209:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_1
  ldc "Thank you for playing\n"
  dup
  ifnull null_210
  goto stop_211
  null_210:
  pop
  ldc "null"
  stop_211:
  aload 11
  dup
  ifnull null_212
  goto stop_213
  null_212:
  pop
  ldc "null"
  stop_213:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  return
.end method

