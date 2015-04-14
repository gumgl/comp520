/*
 * COMP 520, group 5, Winter 2015
 *
 * Group members:
 *
 *     Guillaume Labranche . 260585371
 *     William Bain. . . . . 260509251
 *     Si Mei Zhang. . . . . 260400183
 */

/*
 * JOOS is Copyright (C) 1997 Laurie Hendren & Michael I. Schwartzbach
 *
 * Reproduction of all or part of this software is permitted for
 * educational or research use on condition that this copyright notice is
 * included in any copy. This software comes with no warranty of any
 * kind. In no event will the authors be liable for any damages resulting from
 * use of this software.
 *
 * email: hendren@cs.mcgill.ca, mis@brics.dk
 */

/* iload x        iload x        iload x
 * ldc 0          ldc 1          ldc 2
 * imul           imul           imul
 * ------>        ------>        ------>
 * ldc 0          iload x        iload x
 *                               dup
 *                               iadd
 */

int simplify_multiplication_right(CODE **c)
{ int x,k;
  if (is_iload(*c,&x) &&
      is_ldc_int(next(*c),&k) &&
      is_imul(next(next(*c)))) {
     if (k==0) return replace(c,3,makeCODEldc_int(0,NULL));
     else if (k==1) return replace(c,3,makeCODEiload(x,NULL));
     else if (k==2) return replace(c,3,makeCODEiload(x,
                                       makeCODEdup(
                                       makeCODEiadd(NULL))));
     return 0;
  }
  return 0;
}

/* dup
 * astore x
 * pop
 * -------->
 * astore x
 */
int simplify_astore(CODE **c)
{ int x;
  if (is_dup(*c) &&
      is_astore(next(*c),&x) &&
      is_pop(next(next(*c)))) {
     return replace(c,3,makeCODEastore(x,NULL));
  }
  return 0;
}

/* iload x
 * ldc k   (0<=k<=127)
 * iadd
 * istore x
 * --------->
 * iinc x k
 */
int positive_increment(CODE **c)
{ int x,y,k;
  if (is_iload(*c,&x) &&
      is_ldc_int(next(*c),&k) &&
      is_iadd(next(next(*c))) &&
      is_istore(next(next(next(*c))),&y) &&
      x==y && 0<=k && k<=127) {
     return replace(c,4,makeCODEiinc(x,k,NULL));
  }
  return 0;
}

/* goto L1
 * ...
 * L1:
 * goto L2
 * ...
 * L2:
 * --------->
 * goto L2
 * ...
 * L1:    (reference count reduced by 1)
 * goto L2
 * ...
 * L2:    (reference count increased by 1)
 */
int simplify_goto_goto(CODE **c)
{ int l1,l2;
  if (is_goto(*c,&l1) && is_goto(next(destination(l1)),&l2) && l1>l2) {
     droplabel(l1);
     copylabel(l2);
     return replace(c,1,makeCODEgoto(l2,NULL));
  }
  return 0;
}


/* New optimizations */

/* By the same rationale as simplify_astore above. The dup is unnecessary
 * since the extra value will just be popped.
 *
 * dup
 * istore x
 * pop
 * -------->
 * istore x
 */
int simplify_istore(CODE **c) {
  int x;
  if (is_dup(*c) &&
      is_istore(next(*c),&x) &&
      is_pop(next(next(*c)))) {
     return replace(c,3,makeCODEistore(x,NULL));
  }
  return 0;
}

/* By the same rationale as simplify_astore above. The dup is unnecessary
 * since the extra value will just be popped.
 *
 * dup
 * aload n
 * swap
 * putfield ...
 * pop
 * -------->
 * aload n
 * swap
 * putfield ...
 */
int simplify_putfield(CODE **c) {
  int x;
  char *a;
  if (is_dup(*c) &&
      is_aload(next(*c),&x) &&
      is_swap(next(next(*c))) &&
      is_putfield(next(next(next(*c))),&a) &&
      is_pop(next(next(next(next(*c)))))) {
     return replace(c,5,makeCODEaload(x,makeCODEswap(makeCODEputfield(a,NULL))));
  }
  return 0;
}

/* Optimize positive increments where the number is loaded from a different
 * store
 *
 * We specify x != y because positive_increment is the better optimization
 * if it's available.
 *
 * iload x                   ldc k (0 <= k <= 127)
 * ldc k (0 <= k <= 127)     iload x
 * iadd                      iadd
 * istore y                  istore y
 * -------->                 -------->
 * iload x                   iload x
 * istore y                  istore y
 * iinc y k                  iinc y k
 */
int positive_increment_different_target(CODE **c) {
  int x,y,k;
  if (is_iload(*c,&x) &&
      is_ldc_int(next(*c),&k) &&
      is_iadd(next(next(*c))) &&
      is_istore(next(next(next(*c))),&y) &&
      x!=y && 0<=k && k<=127) {

    return replace(c,4,makeCODEiload(x,makeCODEistore(y,makeCODEiinc(y,k,NULL))));
  } else if (is_ldc_int(*c,&k) &&
      is_iload(next(*c),&x) &&
      is_iadd(next(next(*c))) &&
      is_istore(next(next(next(*c))),&y) &&
      x!=y && 0<=k && k<=127) {

    return replace(c,4,makeCODEiload(x,makeCODEistore(y,makeCODEiinc(y,k,NULL))));
  }
  return 0;
}

/* By the same rationale as positive_increment. We replace a subtraction by a constant with an
 * incrementation by the negative of the constant.
 *
 * iload x
 * ldc k   (0<=k<=127)
 * isub
 * istore x
 * --------->
 * iinc x -k
 */
int simplify_subtraction(CODE **c) {
  int x,y,k;
  if (is_iload(*c,&x) &&
      is_ldc_int(next(*c),&k) &&
      is_isub(next(next(*c))) &&
      is_istore(next(next(next(*c))),&y) &&
      x==y && 0<=k && k<=127) {
     return replace(c,4,makeCODEiinc(x,-k,NULL));
  }
  return 0;
}


/* Eliminate a swap bytecode before putfield operations were the value which
 * is swapped has just been loaded.
 *
 * iload x         ldc ...         aload x
 * aload y         aload y         aload y
 * swap            swap            swap
 * putfield ...    istore y        istore y
 * -------->       -------->       -------->
 * aload y         aload y         aload y
 * iload x         ldc ...         aload x
 * putfield ...    putfield ...    putfield ...
 */
int simplify_swap_putfield(CODE **c) {
  int x,y,k;
  char *a, *b;
  if (is_aload(next(*c),&y) &&
      is_swap(next(next(*c))) &&
      is_putfield(next(next(next(*c))),&a)) {

    if (is_iload(*c,&x)) {
      return replace(c,4,makeCODEaload(y,makeCODEiload(x,makeCODEputfield(a,NULL))));
    }

    else if (is_aload(*c,&x)) {
      return replace(c,4,makeCODEaload(y,makeCODEaload(x,makeCODEputfield(a,NULL))));
    }

    else if (is_ldc_int(*c,&k)) {
      return replace(c,4,makeCODEaload(y,makeCODEldc_int(k,makeCODEputfield(a,NULL))));
    }

    else if (is_ldc_string(*c,&b)) {
      return replace(c,4,makeCODEaload(y,makeCODEldc_string(b,makeCODEputfield(a,NULL))));
    }
  }

  return 0;
}

/* When we coerce an object to string, we check if it's null as a special case.
 * But if the object is a string that was just loaded, the check is unneccessary.
 *
 * ldc string
 * dup
 * ifnull L1
 * ...
 * L1:
 * -------->
 * ldc string
 * ...
 * L1:     (reference count reduced by 1)
 */
int remove_null_check_string_const(CODE **c) {
  char *a;
  int l1;
  if (is_ldc_string(*c,&a) &&
      is_dup(next(*c)) &&
      is_ifnull(next(next(*c)),&l1)) {
    droplabel(l1);
    return replace(c,3,makeCODEldc_string(a,NULL));
  }
  return 0;
}

/* Iterate over the code starting at d until we encounter a label which is
 * not dead. For every instruction which references a label, deallocate the
 * appropriate reference. Return the number of instructions encountered. */
int dealloc_dead_code(CODE *d) {
  int l1;
  int i = 0;
  while (d != NULL && ((!is_label(d,&l1)) || deadlabel(l1))) {
    if (uses_label(d,&l1)) {
      droplabel(l1);
    }

    i++;
    d = next(d);
  }
  return i;
}

/* Given a goto, remove all unreachable instructions which follow
 * (i.e. everything up to the next non-dead label).
 */
int remove_goto_dead_code(CODE **c) {
  int l1;
  int unreachable_ops;

  if (is_goto(*c,&l1)) {
    unreachable_ops = dealloc_dead_code(next(*c));

    if (unreachable_ops == 0) {
      return 0;
    }

    return replace(c, unreachable_ops+1, makeCODEgoto(l1,NULL));
  }
  return 0;
}

/* Given a return, remove all unreachable instructions which follow
 * (i.e. everything up to the next non-dead-label).
 */
int remove_return_dead_code(CODE **c) {
  int l1;
  int unreachable_ops;

  CODE *r;
  if (is_return(*c)) {
    r = makeCODEreturn(NULL);
  } else if (is_areturn(*c)) {
    r = makeCODEareturn(NULL);
  } else if (is_ireturn(*c)) {
    r = makeCODEireturn(NULL);
  } else {
    return 0;
  }

  unreachable_ops = dealloc_dead_code(next(*c));

  if (unreachable_ops == 0) {
    return 0;
  }

  return replace(c, unreachable_ops+1, r);
}

/* Remove a goto when its label follows immediately after. If the label becomes dead,
 * remove it too.
 */
int simplify_goto_label(CODE **c) {
  int l1,l2;
  if (is_goto(*c,&l1) &&
      is_label(next(*c),&l2) &&
      l1 == l2) {
    droplabel(l1);
    if (deadlabel(l1)) {
      return replace(c,2,NULL);
    }
    return replace(c,1,NULL);
  }
  return 0;
}

#define OPTS 13

OPTI optimization[OPTS] = {
  simplify_multiplication_right,
  simplify_astore,
  positive_increment,
  positive_increment_different_target,
  simplify_subtraction,
  simplify_goto_goto,
  simplify_istore,
  simplify_putfield,
  simplify_swap_putfield,
  remove_null_check_string_const,
  remove_goto_dead_code,
  remove_return_dead_code,
  simplify_goto_label
};
