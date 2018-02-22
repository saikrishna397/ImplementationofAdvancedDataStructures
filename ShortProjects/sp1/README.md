
### CS 6301.502. Implementation of advanced data structures and algorithms
### Fall 2017
### Short Project 1: Sorting, BFS.
### Wed, Aug 23, 2017



<br />


1. Implement the merge sort algorithm on generic arrays and on an int array
   and compare their running times on arrays sizes from 1M-16M, and with
   an O(n^2) time algorithm, such as Insertion sort.  Write a report
   with a table and/or chart showing the times for different sizes.
   <br />

   Signatures:
   <br />
   // tmp array is used to store values during the merge operation.
   static<T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp)<br />
   static void mergeSort(int[] arr, int[] tmp)<br />
   static<T extends Comparable<? super T>> void nSquareSort(T[] arr)<br />
   
   <br />

2.Implement breadth-first search (BFS), and solve the problem of
   finding the diameter of a tree that works as follows:
   Run BFS, starting at an arbitrary node as root.  Let u be a node
   at maximum distance from the root.  Run BFS again, with u as the root.
   Output diameter: path from u to a node at maximum distance from u.
<br />
   Signature:
   <br />

   // Return a longest path in g.  Algorithm is correct only if g is a tree.<br />
   static LinkedList<Graph.Vertex> diameter(Graph g) { ... }

