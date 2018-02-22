
### CS 6301.502. Implementation of advanced data structures and algorithms
### Fall 2017
### Short Project 2: Lists, stacks and queues





### Solve as many problems as you wish.  Maximum score: 50


1. Given two linked lists implementing sorted sets, write functions for
   union, intersection, and set difference of the sets.

    public static<T extends Comparable<? super T>> <br />
        void intersect(List<T> l1, List<T> l2, List<T> outList) { <br />
	   // Return elements common to l1 and l2, in sorted order. <br />
	   // outList is an empty list created by the calling <br />
           // program and passed as a parameter. <br />
	   // Function should be efficient whether the List is <br />
	   // implemented using ArrayList or LinkedList.<br />
	   // Do not use HashSet/Map or TreeSet/Map or other complex<br />
           // data structures.<br />
	}

    public static<T extends Comparable<? super T>><br />
        void union(List<T> l1, List<T> l2, List<T> outList) {<br />
	   // Return the union of l1 and l2, in sorted order.<br />
	   // Output is a set, so it should have no duplicates.<br />
	}<br />

    public static<T extends Comparable<? super T>><br />
        void difference(List<T> l1, List<T> l2, List<T> outList) {<br />
	   // Return l1 - l2 (i.e, items in l1 that are not in l2), in sorted order.<br />
	   // Output is a set, so it should have no duplicates.<br />
	}
<br />

2. Write the Merge sort algorithm that works on linked lists.  This will<br />
   be a member function of a linked list class, so that it can work with<br />
   the internal details of the class.  The function should use only<br />
   O(log n) extra space (mainly for recursion), and not make copies of<br />
   elements unnecessarily.  You can start from the SinglyLinkedList class<br />
   provided or create your own.<br />

   static<T extends Comparable<? super T>> void mergeSort(SortableList<T> list) { ... }<br />

   Here is a skeleton of SortableList.java:<br />
   

public class SortableList<T extends Comparable<? super T>> extends SinglyLinkedList<T> {<br />
    void merge(SortableList<T> otherList) {  // Merge this list with other list<br />
    }<br />
    void mergeSort() { Sort this list<br />
    }<br />
    public static<T extends Comparable<? super T>> void mergeSort(SortableList<T> list) {<br />
	list.mergeSort();<br />
    }<br />
}<br />


3. Extend the "unzip" algorithm discussed in class to "multiUnzip" method<br />
   in the SinglyLinkedList class:<br />

   void multiUnzip(int k) {<br />
   	// Rearrange elements of a singly linked list by chaining<br />
   	// together elements that are k apart.  k=2 is the unzip<br />
   	// function discussed in class.  If the list has elements<br />
	// 1..10 in order, after multiUnzip(3), the elements will be<br />
   	// rearranged as: 1 4 7 10 2 5 8 3 6 9.  Instead if we call<br />
	// multiUnzip(4), the list 1..10 will become 1 5 9 2 6 10 3 7 4 8.<br />
   }<br />


4. Write recursive and nonrecursive functions for the following tasks:<br />
   (i) reverse the order of elements of the SinglyLinkedList class,<br />
   (ii) print the elements of the SinglyLinkedList class, in reverse order.<br />
   Write the code and annotate it with proper loop invariants.<br />
   Running time: O(n).<br />


5.Implement array-based, bounded-sized queues, that support the following
   operations: offer, poll, peek, isEmpty (same behavior as in Java's Queue
   interface).  In addition, implement the method resize(), which doubles
   the queue size if the queue is mostly full (over 90%, say), or halves it
   if the queue is mostly empty (less then 25% occupied, say).  Let the
   queue have a minimum size of 16, at all times.


6. Implement array-based, bounded-sized stacks.  Array size is specified
   in the constructor and is fixed.  When the stack gets full, push()
   operation should throw an exception.


7. Write Merge sort algorithm without using recursion by maintaining your
   own stack and simulating how the compiler implements function calls.


8. Implement the Shunting Yard algorithm:
	https://en.wikipedia.org/wiki/Shunting-yard_algorithm
   for parsing arithmetic expressions using the following precedence rules
   (highest to the lowest).

   * Parenthesized expressions (...)<br />
   * Unary operator: factorial (!)<br />
   * Exponentiation (^), right associative.<br />
   * Product (*), division (/).  These operators are left associative.<br />
   * Sum (+), and difference (-).  These operators are left associative.<br />

   Output the equivalent expression in postfix.<br />
   
9. Implement arithmetic with sparse polynomials, implementing the<br />
   following operations: addition, multiplication, evaluation.<br />
   Terms of the polynomial should be stored in a linked list, ordered by<br />
   the exponent field.  Implement multiplication without using HashMaps.<br />

