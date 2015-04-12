.class public ComplementsGenerator

.super java/lang/Object

.field protected complementNumber I
.field protected r Ljoos/lib/JoosRandom;

.method public <init>()V
  .limit locals 1
  .limit stack 3
  aload_0
  invokenonvirtual java/lang/Object/<init>()V
  new joos/lib/JoosRandom
  dup
  iconst_1
  invokenonvirtual joos/lib/JoosRandom/<init>(I)V
  dup
  aload_0
  swap
  putfield ComplementsGenerator/r Ljoos/lib/JoosRandom;
  pop
  return
.end method

.method public generateComment(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
  .limit locals 6
  .limit stack 3
  aload_0
  getfield ComplementsGenerator/r Ljoos/lib/JoosRandom;
  invokevirtual joos/lib/JoosRandom/nextInt()I
  dup
  istore 4
  pop
  iload 4
  ldc 12
  irem
  dup
  istore 5
  pop
  iload 5
  iconst_0
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  iload 5
  ldc 12
  iadd
  dup
  aload_0
  swap
  putfield ComplementsGenerator/complementNumber I
  pop
  goto stop_1
  else_0:
  iload 5
  dup
  aload_0
  swap
  putfield ComplementsGenerator/complementNumber I
  pop
  stop_1:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  iconst_0
  if_icmpeq true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq else_4
  ldc "I love how your hair smells like  a "
  dup
  ifnull null_18
  goto stop_19
  null_18:
  pop
  ldc "null"
  stop_19:
  aload_1
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
  ldc " "
  dup
  ifnull null_22
  goto stop_23
  null_22:
  pop
  ldc "null"
  stop_23:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_14
  goto stop_15
  null_14:
  pop
  ldc "null"
  stop_15:
  aload_3
  dup
  ifnull null_24
  goto stop_25
  null_24:
  pop
  ldc "null"
  stop_25:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_12
  goto stop_13
  null_12:
  pop
  ldc "null"
  stop_13:
  ldc ". It makes me want to "
  dup
  ifnull null_26
  goto stop_27
  null_26:
  pop
  ldc "null"
  stop_27:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_10
  goto stop_11
  null_10:
  pop
  ldc "null"
  stop_11:
  aload_2
  dup
  ifnull null_28
  goto stop_29
  null_28:
  pop
  ldc "null"
  stop_29:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_8
  goto stop_9
  null_8:
  pop
  ldc "null"
  stop_9:
  ldc "."
  dup
  ifnull null_30
  goto stop_31
  null_30:
  pop
  ldc "null"
  stop_31:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_5
  else_4:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  iconst_1
  if_icmpeq true_34
  iconst_0
  goto stop_35
  true_34:
  iconst_1
  stop_35:
  ifeq else_32
  ldc "I always admired you're "
  dup
  ifnull null_42
  goto stop_43
  null_42:
  pop
  ldc "null"
  stop_43:
  aload_3
  dup
  ifnull null_44
  goto stop_45
  null_44:
  pop
  ldc "null"
  stop_45:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_40
  goto stop_41
  null_40:
  pop
  ldc "null"
  stop_41:
  ldc ". I can't wait to "
  dup
  ifnull null_46
  goto stop_47
  null_46:
  pop
  ldc "null"
  stop_47:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_38
  goto stop_39
  null_38:
  pop
  ldc "null"
  stop_39:
  aload_2
  dup
  ifnull null_48
  goto stop_49
  null_48:
  pop
  ldc "null"
  stop_49:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_36
  goto stop_37
  null_36:
  pop
  ldc "null"
  stop_37:
  ldc " with you."
  dup
  ifnull null_50
  goto stop_51
  null_50:
  pop
  ldc "null"
  stop_51:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_33
  else_32:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  iconst_2
  if_icmpeq true_54
  iconst_0
  goto stop_55
  true_54:
  iconst_1
  stop_55:
  ifeq else_52
  ldc "You remind me of my "
  dup
  ifnull null_62
  goto stop_63
  null_62:
  pop
  ldc "null"
  stop_63:
  aload_3
  dup
  ifnull null_64
  goto stop_65
  null_64:
  pop
  ldc "null"
  stop_65:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_60
  goto stop_61
  null_60:
  pop
  ldc "null"
  stop_61:
  ldc ". your "
  dup
  ifnull null_66
  goto stop_67
  null_66:
  pop
  ldc "null"
  stop_67:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_58
  goto stop_59
  null_58:
  pop
  ldc "null"
  stop_59:
  aload_1
  dup
  ifnull null_68
  goto stop_69
  null_68:
  pop
  ldc "null"
  stop_69:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_56
  goto stop_57
  null_56:
  pop
  ldc "null"
  stop_57:
  ldc " legs gives me the shivers."
  dup
  ifnull null_70
  goto stop_71
  null_70:
  pop
  ldc "null"
  stop_71:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_53
  else_52:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  iconst_3
  if_icmpeq true_74
  iconst_0
  goto stop_75
  true_74:
  iconst_1
  stop_75:
  ifeq else_72
  ldc "I was "
  dup
  ifnull null_82
  goto stop_83
  null_82:
  pop
  ldc "null"
  stop_83:
  aload_1
  dup
  ifnull null_84
  goto stop_85
  null_84:
  pop
  ldc "null"
  stop_85:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_80
  goto stop_81
  null_80:
  pop
  ldc "null"
  stop_81:
  ldc " until I met you - and i can't "
  dup
  ifnull null_86
  goto stop_87
  null_86:
  pop
  ldc "null"
  stop_87:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_78
  goto stop_79
  null_78:
  pop
  ldc "null"
  stop_79:
  aload_2
  dup
  ifnull null_88
  goto stop_89
  null_88:
  pop
  ldc "null"
  stop_89:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_76
  goto stop_77
  null_76:
  pop
  ldc "null"
  stop_77:
  ldc " ever since."
  dup
  ifnull null_90
  goto stop_91
  null_90:
  pop
  ldc "null"
  stop_91:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_73
  else_72:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  iconst_4
  if_icmpeq true_94
  iconst_0
  goto stop_95
  true_94:
  iconst_1
  stop_95:
  ifeq else_92
  ldc "I can't wait to show you my "
  dup
  ifnull null_102
  goto stop_103
  null_102:
  pop
  ldc "null"
  stop_103:
  aload_3
  dup
  ifnull null_104
  goto stop_105
  null_104:
  pop
  ldc "null"
  stop_105:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_100
  goto stop_101
  null_100:
  pop
  ldc "null"
  stop_101:
  ldc ". I have a feeling you will "
  dup
  ifnull null_106
  goto stop_107
  null_106:
  pop
  ldc "null"
  stop_107:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_98
  goto stop_99
  null_98:
  pop
  ldc "null"
  stop_99:
  aload_2
  dup
  ifnull null_108
  goto stop_109
  null_108:
  pop
  ldc "null"
  stop_109:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_96
  goto stop_97
  null_96:
  pop
  ldc "null"
  stop_97:
  ldc "."
  dup
  ifnull null_110
  goto stop_111
  null_110:
  pop
  ldc "null"
  stop_111:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_93
  else_92:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  iconst_5
  if_icmpeq true_114
  iconst_0
  goto stop_115
  true_114:
  iconst_1
  stop_115:
  ifeq else_112
  ldc "If you'll leave me it will be a "
  dup
  ifnull null_122
  goto stop_123
  null_122:
  pop
  ldc "null"
  stop_123:
  aload_3
  dup
  ifnull null_124
  goto stop_125
  null_124:
  pop
  ldc "null"
  stop_125:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_120
  goto stop_121
  null_120:
  pop
  ldc "null"
  stop_121:
  ldc ". I would rather be "
  dup
  ifnull null_126
  goto stop_127
  null_126:
  pop
  ldc "null"
  stop_127:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_118
  goto stop_119
  null_118:
  pop
  ldc "null"
  stop_119:
  aload_1
  dup
  ifnull null_128
  goto stop_129
  null_128:
  pop
  ldc "null"
  stop_129:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_116
  goto stop_117
  null_116:
  pop
  ldc "null"
  stop_117:
  ldc "."
  dup
  ifnull null_130
  goto stop_131
  null_130:
  pop
  ldc "null"
  stop_131:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_113
  else_112:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  ldc 6
  if_icmpeq true_134
  iconst_0
  goto stop_135
  true_134:
  iconst_1
  stop_135:
  ifeq else_132
  ldc "I hope our "
  dup
  ifnull null_146
  goto stop_147
  null_146:
  pop
  ldc "null"
  stop_147:
  aload_1
  dup
  ifnull null_148
  goto stop_149
  null_148:
  pop
  ldc "null"
  stop_149:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_144
  goto stop_145
  null_144:
  pop
  ldc "null"
  stop_145:
  ldc " relationship will "
  dup
  ifnull null_150
  goto stop_151
  null_150:
  pop
  ldc "null"
  stop_151:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_142
  goto stop_143
  null_142:
  pop
  ldc "null"
  stop_143:
  aload_2
  dup
  ifnull null_152
  goto stop_153
  null_152:
  pop
  ldc "null"
  stop_153:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_140
  goto stop_141
  null_140:
  pop
  ldc "null"
  stop_141:
  ldc " like a "
  dup
  ifnull null_154
  goto stop_155
  null_154:
  pop
  ldc "null"
  stop_155:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_138
  goto stop_139
  null_138:
  pop
  ldc "null"
  stop_139:
  aload_3
  dup
  ifnull null_156
  goto stop_157
  null_156:
  pop
  ldc "null"
  stop_157:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_136
  goto stop_137
  null_136:
  pop
  ldc "null"
  stop_137:
  ldc "."
  dup
  ifnull null_158
  goto stop_159
  null_158:
  pop
  ldc "null"
  stop_159:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_133
  else_132:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  ldc 7
  if_icmpeq true_162
  iconst_0
  goto stop_163
  true_162:
  iconst_1
  stop_163:
  ifeq else_160
  ldc "You make me want to be a "
  dup
  ifnull null_170
  goto stop_171
  null_170:
  pop
  ldc "null"
  stop_171:
  aload_1
  dup
  ifnull null_172
  goto stop_173
  null_172:
  pop
  ldc "null"
  stop_173:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_168
  goto stop_169
  null_168:
  pop
  ldc "null"
  stop_169:
  ldc " man. from now on I'll "
  dup
  ifnull null_174
  goto stop_175
  null_174:
  pop
  ldc "null"
  stop_175:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_166
  goto stop_167
  null_166:
  pop
  ldc "null"
  stop_167:
  aload_2
  dup
  ifnull null_176
  goto stop_177
  null_176:
  pop
  ldc "null"
  stop_177:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_164
  goto stop_165
  null_164:
  pop
  ldc "null"
  stop_165:
  ldc " harder."
  dup
  ifnull null_178
  goto stop_179
  null_178:
  pop
  ldc "null"
  stop_179:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_161
  else_160:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  ldc 8
  if_icmpeq true_182
  iconst_0
  goto stop_183
  true_182:
  iconst_1
  stop_183:
  ifeq else_180
  ldc "Show me that "
  dup
  ifnull null_190
  goto stop_191
  null_190:
  pop
  ldc "null"
  stop_191:
  aload_1
  dup
  ifnull null_192
  goto stop_193
  null_192:
  pop
  ldc "null"
  stop_193:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_188
  goto stop_189
  null_188:
  pop
  ldc "null"
  stop_189:
  ldc " "
  dup
  ifnull null_194
  goto stop_195
  null_194:
  pop
  ldc "null"
  stop_195:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_186
  goto stop_187
  null_186:
  pop
  ldc "null"
  stop_187:
  aload_3
  dup
  ifnull null_196
  goto stop_197
  null_196:
  pop
  ldc "null"
  stop_197:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_184
  goto stop_185
  null_184:
  pop
  ldc "null"
  stop_185:
  ldc " of yours! "
  dup
  ifnull null_198
  goto stop_199
  null_198:
  pop
  ldc "null"
  stop_199:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_181
  else_180:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  ldc 9
  if_icmpeq true_202
  iconst_0
  goto stop_203
  true_202:
  iconst_1
  stop_203:
  ifeq else_200
  ldc "Let's "
  dup
  ifnull null_210
  goto stop_211
  null_210:
  pop
  ldc "null"
  stop_211:
  aload_2
  dup
  ifnull null_212
  goto stop_213
  null_212:
  pop
  ldc "null"
  stop_213:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_208
  goto stop_209
  null_208:
  pop
  ldc "null"
  stop_209:
  ldc " all night long, you "
  dup
  ifnull null_214
  goto stop_215
  null_214:
  pop
  ldc "null"
  stop_215:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_206
  goto stop_207
  null_206:
  pop
  ldc "null"
  stop_207:
  aload_1
  dup
  ifnull null_216
  goto stop_217
  null_216:
  pop
  ldc "null"
  stop_217:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_204
  goto stop_205
  null_204:
  pop
  ldc "null"
  stop_205:
  ldc " lady, you..."
  dup
  ifnull null_218
  goto stop_219
  null_218:
  pop
  ldc "null"
  stop_219:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_201
  else_200:
  aload_0
  getfield ComplementsGenerator/complementNumber I
  ldc 10
  if_icmpeq true_222
  iconst_0
  goto stop_223
  true_222:
  iconst_1
  stop_223:
  ifeq else_220
  ldc "Show me your "
  dup
  ifnull null_226
  goto stop_227
  null_226:
  pop
  ldc "null"
  stop_227:
  aload_3
  dup
  ifnull null_228
  goto stop_229
  null_228:
  pop
  ldc "null"
  stop_229:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_224
  goto stop_225
  null_224:
  pop
  ldc "null"
  stop_225:
  ldc " and I'll show you mine."
  dup
  ifnull null_230
  goto stop_231
  null_230:
  pop
  ldc "null"
  stop_231:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  goto stop_221
  else_220:
  ldc "you are my "
  dup
  ifnull null_242
  goto stop_243
  null_242:
  pop
  ldc "null"
  stop_243:
  aload_3
  dup
  ifnull null_244
  goto stop_245
  null_244:
  pop
  ldc "null"
  stop_245:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_240
  goto stop_241
  null_240:
  pop
  ldc "null"
  stop_241:
  ldc " my only "
  dup
  ifnull null_246
  goto stop_247
  null_246:
  pop
  ldc "null"
  stop_247:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_238
  goto stop_239
  null_238:
  pop
  ldc "null"
  stop_239:
  aload_3
  dup
  ifnull null_248
  goto stop_249
  null_248:
  pop
  ldc "null"
  stop_249:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_236
  goto stop_237
  null_236:
  pop
  ldc "null"
  stop_237:
  ldc " , you make me "
  dup
  ifnull null_250
  goto stop_251
  null_250:
  pop
  ldc "null"
  stop_251:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_234
  goto stop_235
  null_234:
  pop
  ldc "null"
  stop_235:
  aload_1
  dup
  ifnull null_252
  goto stop_253
  null_252:
  pop
  ldc "null"
  stop_253:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  dup
  ifnull null_232
  goto stop_233
  null_232:
  pop
  ldc "null"
  stop_233:
  ldc " when skies are grey."
  dup
  ifnull null_254
  goto stop_255
  null_254:
  pop
  ldc "null"
  stop_255:
  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;
  areturn
  stop_221:
  stop_201:
  stop_181:
  stop_161:
  stop_133:
  stop_113:
  stop_93:
  stop_73:
  stop_53:
  stop_33:
  stop_5:
  nop
.end method

.method public getComplementNumber()I
  .limit locals 1
  .limit stack 1
  aload_0
  getfield ComplementsGenerator/complementNumber I
  ireturn
  nop
.end method

.method public generateAdjective()Ljava/lang/String;
  .limit locals 3
  .limit stack 2
  aload_0
  getfield ComplementsGenerator/r Ljoos/lib/JoosRandom;
  invokevirtual joos/lib/JoosRandom/nextInt()I
  ldc 10
  irem
  dup
  istore_1
  pop
  iload_1
  iconst_0
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  iload_1
  ldc 10
  iadd
  dup
  istore_2
  pop
  goto stop_1
  else_0:
  iload_1
  dup
  istore_2
  pop
  stop_1:
  iload_2
  iconst_0
  if_icmpeq true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq else_4
  ldc "dead"
  areturn
  goto stop_5
  else_4:
  iload_2
  iconst_1
  if_icmpeq true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  ifeq else_8
  ldc "beautiful"
  areturn
  goto stop_9
  else_8:
  iload_2
  iconst_2
  if_icmpeq true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  ifeq else_12
  ldc "single"
  areturn
  goto stop_13
  else_12:
  iload_2
  iconst_3
  if_icmpeq true_18
  iconst_0
  goto stop_19
  true_18:
  iconst_1
  stop_19:
  ifeq else_16
  ldc "blind"
  areturn
  goto stop_17
  else_16:
  iload_2
  iconst_4
  if_icmpeq true_22
  iconst_0
  goto stop_23
  true_22:
  iconst_1
  stop_23:
  ifeq else_20
  ldc "enormous"
  areturn
  goto stop_21
  else_20:
  iload_2
  iconst_5
  if_icmpeq true_26
  iconst_0
  goto stop_27
  true_26:
  iconst_1
  stop_27:
  ifeq else_24
  ldc "sexy"
  areturn
  goto stop_25
  else_24:
  iload_2
  ldc 6
  if_icmpeq true_30
  iconst_0
  goto stop_31
  true_30:
  iconst_1
  stop_31:
  ifeq else_28
  ldc "talented"
  areturn
  goto stop_29
  else_28:
  iload_2
  ldc 7
  if_icmpeq true_34
  iconst_0
  goto stop_35
  true_34:
  iconst_1
  stop_35:
  ifeq else_32
  ldc "better"
  areturn
  goto stop_33
  else_32:
  iload_2
  ldc 8
  if_icmpeq true_38
  iconst_0
  goto stop_39
  true_38:
  iconst_1
  stop_39:
  ifeq else_36
  ldc "happy"
  areturn
  goto stop_37
  else_36:
  ldc "crappy"
  areturn
  stop_37:
  stop_33:
  stop_29:
  stop_25:
  stop_21:
  stop_17:
  stop_13:
  stop_9:
  stop_5:
  nop
.end method

.method public generateNoun()Ljava/lang/String;
  .limit locals 3
  .limit stack 2
  aload_0
  getfield ComplementsGenerator/r Ljoos/lib/JoosRandom;
  invokevirtual joos/lib/JoosRandom/nextInt()I
  ldc 9
  irem
  dup
  istore_1
  pop
  iload_1
  iconst_0
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  iload_1
  ldc 9
  iadd
  dup
  istore_2
  pop
  goto stop_1
  else_0:
  iload_1
  dup
  istore_2
  pop
  stop_1:
  iload_1
  iconst_0
  if_icmpeq true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq else_4
  ldc "fruit"
  areturn
  goto stop_5
  else_4:
  iload_2
  iconst_1
  if_icmpeq true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  ifeq else_8
  ldc "work-ethic"
  areturn
  goto stop_9
  else_8:
  iload_2
  iconst_2
  if_icmpeq true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  ifeq else_12
  ldc "niece"
  areturn
  goto stop_13
  else_12:
  iload_2
  iconst_3
  if_icmpeq true_18
  iconst_0
  goto stop_19
  true_18:
  iconst_1
  stop_19:
  ifeq else_16
  ldc "problems"
  areturn
  goto stop_17
  else_16:
  iload_2
  iconst_4
  if_icmpeq true_22
  iconst_0
  goto stop_23
  true_22:
  iconst_1
  stop_23:
  ifeq else_20
  ldc "tragedy"
  areturn
  goto stop_21
  else_20:
  iload_2
  iconst_5
  if_icmpeq true_26
  iconst_0
  goto stop_27
  true_26:
  iconst_1
  stop_27:
  ifeq else_24
  ldc "dream"
  areturn
  goto stop_25
  else_24:
  iload_2
  ldc 6
  if_icmpeq true_30
  iconst_0
  goto stop_31
  true_30:
  iconst_1
  stop_31:
  ifeq else_28
  ldc "sunshine"
  areturn
  goto stop_29
  else_28:
  iload_2
  ldc 7
  if_icmpeq true_34
  iconst_0
  goto stop_35
  true_34:
  iconst_1
  stop_35:
  ifeq else_32
  ldc "raccoon"
  areturn
  goto stop_33
  else_32:
  ldc "skin"
  areturn
  stop_33:
  stop_29:
  stop_25:
  stop_21:
  stop_17:
  stop_13:
  stop_9:
  stop_5:
  nop
.end method

.method public generateVerb()Ljava/lang/String;
  .limit locals 3
  .limit stack 2
  aload_0
  getfield ComplementsGenerator/r Ljoos/lib/JoosRandom;
  invokevirtual joos/lib/JoosRandom/nextInt()I
  ldc 10
  irem
  dup
  istore_1
  pop
  iload_1
  iconst_0
  if_icmplt true_2
  iconst_0
  goto stop_3
  true_2:
  iconst_1
  stop_3:
  ifeq else_0
  iload_1
  ldc 10
  iadd
  dup
  istore_2
  pop
  goto stop_1
  else_0:
  iload_1
  dup
  istore_2
  pop
  stop_1:
  iload_1
  iconst_0
  if_icmpeq true_6
  iconst_0
  goto stop_7
  true_6:
  iconst_1
  stop_7:
  ifeq else_4
  ldc "rot"
  areturn
  goto stop_5
  else_4:
  iload_2
  iconst_1
  if_icmpeq true_10
  iconst_0
  goto stop_11
  true_10:
  iconst_1
  stop_11:
  ifeq else_8
  ldc "cry"
  areturn
  goto stop_9
  else_8:
  iload_2
  iconst_2
  if_icmpeq true_14
  iconst_0
  goto stop_15
  true_14:
  iconst_1
  stop_15:
  ifeq else_12
  ldc "puke"
  areturn
  goto stop_13
  else_12:
  iload_2
  iconst_3
  if_icmpeq true_18
  iconst_0
  goto stop_19
  true_18:
  iconst_1
  stop_19:
  ifeq else_16
  ldc "work"
  areturn
  goto stop_17
  else_16:
  iload_2
  iconst_4
  if_icmpeq true_22
  iconst_0
  goto stop_23
  true_22:
  iconst_1
  stop_23:
  ifeq else_20
  ldc "sleep"
  areturn
  goto stop_21
  else_20:
  iload_2
  iconst_5
  if_icmpeq true_26
  iconst_0
  goto stop_27
  true_26:
  iconst_1
  stop_27:
  ifeq else_24
  ldc "be-pleased"
  areturn
  goto stop_25
  else_24:
  iload_2
  ldc 6
  if_icmpeq true_30
  iconst_0
  goto stop_31
  true_30:
  iconst_1
  stop_31:
  ifeq else_28
  ldc "flourish"
  areturn
  goto stop_29
  else_28:
  iload_2
  ldc 7
  if_icmpeq true_34
  iconst_0
  goto stop_35
  true_34:
  iconst_1
  stop_35:
  ifeq else_32
  ldc "nibble"
  areturn
  goto stop_33
  else_32:
  iload_2
  ldc 8
  if_icmpeq true_38
  iconst_0
  goto stop_39
  true_38:
  iconst_1
  stop_39:
  ifeq else_36
  ldc "program"
  areturn
  goto stop_37
  else_36:
  ldc "eat-bananas"
  areturn
  stop_37:
  stop_33:
  stop_29:
  stop_25:
  stop_21:
  stop_17:
  stop_13:
  stop_9:
  stop_5:
  nop
.end method

