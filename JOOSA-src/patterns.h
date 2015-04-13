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


/* WAB - new optimizations */

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

/* Eliminate a swap bytecode in places where it's easy to see what should happen***
 *
 * We specify x != y because positive_increment is the better optimization
 * if it's available.
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


#define OPTS 8

OPTI optimization[OPTS] = {
  simplify_multiplication_right,
  simplify_astore,
  positive_increment,
  positive_increment_different_target,
  simplify_goto_goto,
  simplify_istore,
  simplify_putfield,
  simplify_swap_putfield
};
