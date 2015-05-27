/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package main

func main() {
    var a [8]int
    a[0] = 170
    a[1] = 45
    a[2] = 75
    a[3] = -90
    a[4] = -802
    a[5] = 24
    a[6] = 2
    a[7] = 66

    lenA = 8;
    print("unsorted:", a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);
    insertionSort();
    print("sorted!:", a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7])
}

func insertionSort() {
    for i := 1; i < lenA; i++ {
        value := a[i]
        j := i - 1
        for j >= 0 && a[j] > value {
            a[j+1] = a[j]
            j = j - 1
        }
        a[j+1] = value
    }
}
