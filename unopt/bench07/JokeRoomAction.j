.class public JokeRoomAction

.super RoomAction

.method public <init>()V
  .limit locals 1
  .limit stack 1
  aload_0
  invokenonvirtual RoomAction/<init>()V
  return
.end method

.method public describe()V
  .limit locals 1
  .limit stack 2
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "The old man seems talkative."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  return
.end method

.method public performAction(Ljava/lang/String;)I
  .limit locals 2
  .limit stack 3
  aload_1
  ldc "listen"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_3
  iconst_0
  goto stop_4
  true_3:
  iconst_1
  stop_4:
  dup
  ifne true_2
  pop
  aload_1
  ldc "interact"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_5
  iconst_0
  goto stop_6
  true_5:
  iconst_1
  stop_6:
  true_2:
  ifeq else_0
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "The old man speaks: "
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_0
  invokevirtual JokeRoomAction/printJoke()V
  iconst_0
  ireturn
  goto stop_1
  else_0:
  aload_1
  ldc "talk"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_11
  iconst_0
  goto stop_12
  true_11:
  iconst_1
  stop_12:
  dup
  ifne true_10
  pop
  aload_1
  ldc "speak"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_13
  iconst_0
  goto stop_14
  true_13:
  iconst_1
  stop_14:
  true_10:
  dup
  ifne true_9
  pop
  aload_1
  ldc "hello"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_15
  iconst_0
  goto stop_16
  true_15:
  iconst_1
  stop_16:
  true_9:
  ifeq else_7
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "The old man ignores you and speaks:"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  aload_0
  invokevirtual JokeRoomAction/printJoke()V
  iconst_0
  ireturn
  goto stop_8
  else_7:
  aload_1
  ldc "ignore"
  iconst_0
  invokevirtual java/lang/String/indexOf(Ljava/lang/String;I)I
  iconst_0
  if_icmpge true_19
  iconst_0
  goto stop_20
  true_19:
  iconst_1
  stop_20:
  ifeq else_17
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "The old man hits you with a fish. You feel weakened."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  ldc 9
  ireturn
  goto stop_18
  else_17:
  aload_0
  aload_1
  invokevirtual RoomAction/performBaseAction(Ljava/lang/String;)I
  ireturn
  stop_18:
  stop_8:
  stop_1:
  nop
.end method

.method public printJoke()V
  .limit locals 3
  .limit stack 2
  new joos/lib/JoosRandom
  dup
  invokenonvirtual joos/lib/JoosRandom/<init>()V
  dup
  astore_1
  pop
  aload_1
  invokevirtual joos/lib/JoosRandom/nextInt()I
  ldc 10
  irem
  dup
  istore_2
  pop
  iload_2
  iconst_0
  if_icmplt true_1
  iconst_0
  goto stop_2
  true_1:
  iconst_1
  stop_2:
  ifeq stop_0
  iload_2
  ineg
  dup
  istore_2
  pop
  stop_0:
  iload_2
  iconst_0
  if_icmpeq true_5
  iconst_0
  goto stop_6
  true_5:
  iconst_1
  stop_6:
  ifeq else_3
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "Two cannibals are eating a clown, when one asks the other 'does this taste funny to you?'"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_4
  else_3:
  iload_2
  iconst_1
  if_icmpeq true_9
  iconst_0
  goto stop_10
  true_9:
  iconst_1
  stop_10:
  ifeq else_7
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "Two peanuts were walking down the street, and one was assaulted."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_8
  else_7:
  iload_2
  iconst_2
  if_icmpeq true_13
  iconst_0
  goto stop_14
  true_13:
  iconst_1
  stop_14:
  ifeq else_11
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "A sausage is sitting in a frying pan. Another sausage gets dropped in, and says 'holy crap, it's hot in here!' To which the first sausage replies 'holy crap, a talking sausage!'"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_12
  else_11:
  iload_2
  iconst_3
  if_icmpeq true_17
  iconst_0
  goto stop_18
  true_17:
  iconst_1
  stop_18:
  ifeq else_15
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "Have you heard about that new movie Constipation?\n\nIt hasn't come out yet."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_16
  else_15:
  iload_2
  iconst_4
  if_icmpeq true_21
  iconst_0
  goto stop_22
  true_21:
  iconst_1
  stop_22:
  ifeq else_19
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "What's a foot long and slippery?\n\nA slipper!"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_20
  else_19:
  iload_2
  iconst_5
  if_icmpeq true_25
  iconst_0
  goto stop_26
  true_25:
  iconst_1
  stop_26:
  ifeq else_23
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "If H2O is on the inside of a fire hydrant, what's on the outside? \n\nK9P"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_24
  else_23:
  iload_2
  ldc 6
  if_icmpeq true_29
  iconst_0
  goto stop_30
  true_29:
  iconst_1
  stop_30:
  ifeq else_27
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "Two silk worms were in a race.\n\nIt ended in a tie."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_28
  else_27:
  iload_2
  ldc 7
  if_icmpeq true_33
  iconst_0
  goto stop_34
  true_33:
  iconst_1
  stop_34:
  ifeq else_31
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "Werner Heisenberg is driving down the highway. Cop stops him and says, 'Sir, do you know how fast you were going?' Heisenberg says, 'No, but I know exactly where I am!'"
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_32
  else_31:
  iload_2
  ldc 8
  if_icmpeq true_37
  iconst_0
  goto stop_38
  true_37:
  iconst_1
  stop_38:
  ifeq else_35
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "What's the difference between roast beef and pea soup?\n\nAnyone can roast beef."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_36
  else_35:
  iload_2
  ldc 9
  if_icmpeq true_41
  iconst_0
  goto stop_42
  true_41:
  iconst_1
  stop_42:
  ifeq else_39
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "What do you get when you cross an elephant with a banana?\n\nElephant banana sine theta."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  goto stop_40
  else_39:
  iload_2
  ldc 10
  if_icmpeq true_44
  iconst_0
  goto stop_45
  true_44:
  iconst_1
  stop_45:
  ifeq stop_43
  aload_0
  getfield RoomAction/O Ljoos/lib/JoosIO;
  ldc "What did the girl fungus say to the boy fungus?\n\nYou're a real fun guy."
  invokevirtual joos/lib/JoosIO/println(Ljava/lang/String;)V
  stop_43:
  stop_40:
  stop_36:
  stop_32:
  stop_28:
  stop_24:
  stop_20:
  stop_16:
  stop_12:
  stop_8:
  stop_4:
  return
.end method

