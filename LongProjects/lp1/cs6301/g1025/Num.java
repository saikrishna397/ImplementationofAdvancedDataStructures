/**
 * Big Integer class which has big int arithmetic such as addition,
 * subtraction, division, product etc
 *  @author swaroop, saikumar, antriksh, gunjan
 *
 */
package cs6301.g1025;

import java.util.Iterator;
import java.util.LinkedList;

public class Num implements Comparable<Num> {

	/**
	 * Helper class Split to split for Karatsuba product
	 */
	class Split {
		Num first;
		Num second;

		Split() {
			first = new Num();
			second = new Num();
		}

		public void split(Num n, int k) {
			first.base = n.base;
			second.base = n.base;
			Iterator<Long> it = n.num.iterator();
			while (it.hasNext()) {
				if (k > 0) {
					first.num.add(next(it));
					k--;
				} else {
					second.num.add(next(it));
				}
			}
		}
	}

	/**
	 * Default Base : can be set to any base from 2 to 2^31 This can be changed
	 * to what you want it to be.
	 */
	static long defaultBase = (long) Math.pow(2, 31);
	public static final Num ONE = new Num(1l, defaultBase);
	public static final Num ZERO = new Num(0l, defaultBase);
	public static final Num TWO = new Num(2l, defaultBase);
	
	long base = defaultBase; // Change as needed

	public LinkedList<Long> num;
	boolean sign = false;

	/**
	 * Start of Constructors
	 */

	/**
	 * constructor for initializing input string to default base
	 */
	public Num(String s) {
		this(s, defaultBase);

	}

	/**
	 * constructor for initializing input string with given base
	 */
	public Num(String s, long base) {
		this();
		this.base = base;
		if (s == "")
			return;
		try {
			char[] string = s.toCharArray();
			Num res = new Num("", base);
			int i = 0;
			if (string[i] == '-') {
				this.sign = true;
				i++;
			}
			for (; i < s.length(); i++)
				res = add(product(res, 10), new Num(Long.parseLong(string[i] + ""), base));
			this.num = res.num;
		} catch (Exception e) {
			System.out.println("Unrecognized character Exception: " + " at Value: " + s);
		}
	}

	/**
	 * constructor for initialising base with long input
	 */
	public Num(long x, long base) {
		this();
		this.base = base;
		if (x < 0) {
			sign = true;
		}
		x = Math.abs(x);
		if (x == 0) {
			this.num.add(x);
		} else {
			while (x > 0) {
				long digit = x % this.base;
				this.num.add(digit);
				x /= this.base;
			}
		}

	}

	/**
	 * Constructor for Num of type long
	 */
	public Num(long x) {
		this(x, defaultBase);
	}

	/**
	 * constructor added to create an empty num class
	 */
	Num() {
		num = new LinkedList<Long>();
	}

	/**
	 * End of Constructors
	 */

	/**
	 * Start of Level 1 operation functions: add, subtract, product
	 */

	/**
	 * Difference of two signed big integers
	 *
	 * @param a
	 *            Num
	 * @param b
	 *            Num
	 * @return: Num (a - b)
	 */
	public static Num subtract(Num a, Num b) {
		if (a.sign ^ b.sign) // opp sign
			return unsignedAdd(a, b);
		else
			return a.unsignedCompareTo(b) <= 0 ? unsignedSubtract(b, a, !b.sign) : unsignedSubtract(a, b, a.sign);

	}

	/**
	 * Difference of two unsigned big integers Always abs(a) >= abs(b), sign is
	 * set based on the previous method's input
	 */
	static Num unsignedSubtract(Num a, Num b, boolean sign) {
		Num res = new Num("", a.base);

		Iterator<Long> ita = a.num.iterator();
		Iterator<Long> itb = b.num.iterator();
		long borrow = 0l;
		long diff;
		while (ita.hasNext() || itb.hasNext() || borrow > 0) {
			diff = next(ita) - next(itb) - borrow;
			borrow = 0l;
			if (diff < 0) {
				diff += res.base;
				borrow = 1;
			}
			if (!(!itb.hasNext() && diff == 0) || (diff == 0))
				res.num.add(diff);
		}
		res.trim();
		sign(res, sign);
		return res;
	}

	/**
	 * Sum of two unsigned big integers
	 */
	static Num unsignedAdd(Num a, Num b) {
		Num res = new Num("", a.base);
		long carry = 0l;
		Iterator<Long> ita = a.num.iterator();
		Iterator<Long> itb = b.num.iterator();

		long sum = 0;
		while (ita.hasNext() || itb.hasNext() || carry > 0) {
			sum = next(ita) + next(itb) + carry;
			res.num.add(sum % res.base);
			carry = sum / res.base;
		}
		res.sign = a.sign;
		return res;
	}

	/**
	 * Sum of two signed big integers
	 */
	public static Num add(Num a, Num b) {
		if (!(a.sign ^ b.sign)) {// if both signs are same, xor will be false
			return unsignedAdd(a, b);
		} else
			return a.unsignedCompareTo(b) <= 0 ? unsignedSubtract(b, a, b.sign) : unsignedSubtract(a, b, a.sign);
	}

	/**
	 * Product of a Num and long
	 */
	private static Num product(Num n, long b) {
		if (b == 0)
			return new Num(0l, n.base);

		Iterator<Long> it = n.num.iterator();
		long carry = 0l;
		Num res = new Num("", n.base);

		while (it.hasNext() || carry > 0) {
			long sum = (next(it) * b + carry);
			res.num.add(sum % res.base);
			carry = sum / res.base;
		}
		return res;
	}

	/**
	 * Karatsuba Product Num * Num RT = O(n^log3)
	 *
	 * @param a:
	 *            Num
	 * @param b:
	 *            Num
	 * @return: Num - a*b
	 */
	public static Num product(Num a, Num b) {
		Num res = new Num();
		if (size(b) == 1 || size(a) == 1) {
			res = size(b) == 1 ? product(a, b.num.getFirst()) : product(b, a.num.getFirst());
		} else if (size(a) == 0 || size(b) == 0) {
			res = new Num("", a.base);
		} else if (size(a) >= size(b)) {
			res = karatsubaSplit(a, b);
		} else {
			res = karatsubaSplit(b, a);
		}
		sign(res, (a.sign ^ b.sign));
		return res;
	}

	public static Num productLong(Num a, Num b) {
		Num res = new Num();
		Iterator<Long> ib = b.num.iterator();
		int offset = 0;
		while (ib.hasNext()) {
			Num local = product(a, next(ib));
			int shift = offset++;
			while (shift > 0) {
				leftShift(local);
				shift--;
			}
			res = add(res, local);
		}
		return res;
	}

	/**
	 * Method splitting the two Nums into two halves for Karatsuba product
	 *
	 * Splits a to aL and aH Splits b to bL and bH part1 = aH * bH *
	 * rightShift2k part3 = aL * bL part2 = ((aL+aH)*(bL+bH) - part1 - part3) *
	 * rightShiftk return part1 + part2 + part3
	 */
	static Num karatsubaSplit(Num a, Num b) {

		int k = (int) size(b) / 2;
		Num part1 = new Num("", a.base);

		// Splitting a to aL and aH
		Split split = a.new Split();
		split.split(a, k);
		Num aL = split.first;// first k bits;
		Num aH = split.second;// a.size - k bits;\

		// Splitting b to bL and bH
		split = b.new Split();
		split.split(b, k);
		Num bL = split.first;// first k bits;
		Num bH = split.second;// b.size - k bits;

		Num aHbHProd = product(aH, bH);
		part1.num = new LinkedList<>(aHbHProd.num);
		leftShift(part1, 2 * k);

		Num part3 = product(aL, bL);

		Num aLaHSum = add(aL, aH);
		Num bLbHSum = add(bL, bH);
		Num aLHbLHProd = product(aLaHSum, bLbHSum);
		Num part2 = subtract(subtract(aLHbLHProd, aHbHProd), part3);
		leftShift(part2, k);
		Num res = add(add(part1, part2), part3);

		return res;
	}

	/**
	 * a^n, power function Num ^ long
	 */
	public static Num power(Num a, long n) {
		if (n == 0)
			return ONE;
		else if (n == 1)
			return a;
		else {
			
			Num div1 = power(a, n / 2);
			Num res = product(div1, div1);
			if (n % 2 == 1) {
				return product(res, a);
			} else
				return res;
		}
	}

	/* End of Level 1 */

	/* Start of Level 2 */

	/**
	 * Binary Search for divide operation Initial Call: low=1, high=x,
	 * x=Numerator, y=Denominator
	 */
	static Num binarySearch(Num low, Num high, Num x, Num y) {

		Num mid = divideBy2(unsignedAdd(low, high));
		Num left = product(mid, y);
		Num right = unsignedAdd(left,y);
		int leftcomp = left.unsignedCompareTo(x);
		int rightcomp = x.unsignedCompareTo(right);
		if (leftcomp > 0) {
			return binarySearch(low, mid, x, y);
		}

		else if (leftcomp <= 0 && rightcomp < 0) {
			return mid;
		} else {
			return binarySearch(unsignedAdd(mid, ONE), high, x, y);

		}


	}

	

	/**
	 * Finds the Half of a Divide by 2 operation
	 */
	private static Num divideBy2(Num a) {
		Num res = product(a, a.base / 2);
		rightShift(res);
		return res;
	}

	/**
	 * Divide function
	 *
	 * @param x:
	 *            Num
	 * @param y:
	 *            Num
	 * @return: Num - a/b
	 */
	public static Num divide(Num x, Num y) throws Exception {

		Num res = new Num("", x.base);

		if (y.unsignedCompareTo(ZERO) == 0)
			throw new Exception("Divide by zero encountered.");
		else if (y.unsignedCompareTo(ONE) == 0)
			res = x;
		else if (y.unsignedCompareTo(TWO) == 0)
			res = divideBy2(x);

		int ret = x.unsignedCompareTo(y);

		if (ret < 0)
			res = ZERO;
		else if (ret == 0)
			res = ONE;
		else {
			res = binarySearch(ONE, x, x, y);
		}
		sign(res, x.sign ^ y.sign);
		return res;
	}

	/**
	 * Mod function
	 * 
	 * @param a:
	 *            Num
	 * @param b:
	 *            Num
	 * @return: Num - remainder of a/b
	 */
	public static Num mod(Num a, Num b) throws Exception {
		if (a.compareTo(ZERO) == 0)
			return ZERO;
		else if (a.compareTo(ONE) == 0)
			return ONE;
		int ret = a.compareTo(b);
		if (ret == 0)
			return ZERO;
		else if (ret < 1)
			return a;
		else return subtract(a, product(divide(a, b), b));
	}

	/**
	 * power function Num ^ Num
	 * 
	 * @param a
	 *            Num
	 * @param n
	 *            Num
	 * @return: Num - remainder of a^b
	 */
	public static Num power(Num a, Num n) {

		if (size(n) == 1)
			return power(a, n.num.getFirst());

		rightShift(n);
		return product(power(power(a, n), a.base), power(a, n.num.getFirst()));
	}

	/**
	 * Square Root function Num ^ (1/2)
	 * 
	 * @param a
	 *            Num
	 * @return Num - a^(1/2)
	 */
	public static Num squareRoot(Num a) {
		if (a.sign)
			throw new ArithmeticException("Square root of a negative number");
		else if (a.unsignedCompareTo(ZERO) == 0)
			return ZERO;
		else if (size(a) == 1 && a.num.getFirst() <= 1)
			return a;
		return sqSearch(ONE, Num.divideBy2(a), a);

	}
	/**
	 * Square Root Search function 
	 * 
	 * @param low,high,given
	 * given-find the square root of given value
	 *            
	 * @return Num -square root of a
	 */
	static Num sqSearch(Num low,Num high,Num given){
		Num mid = divideBy2(unsignedAdd(low, high));
		Num left=product(mid,mid);
		Num right=unsignedAdd(unsignedAdd(left,product(mid,2)),ONE);
		int leftcomp=left.unsignedCompareTo(given);
		int rightcomp=given.unsignedCompareTo(right);
		if(leftcomp<=0&&rightcomp<0){
			return mid;
		}
		else if(leftcomp>0){
			return sqSearch(low, mid,given);
		}
		else{
			return sqSearch(unsignedAdd(mid,ONE), high,given);
		}
		
		
	}
	
	

	/* End of Level 2 */

	// Utility functions
	// compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	// otherwise

	/**
	 * Unsigned Compare function Compares the mod value of this Num and the
	 * other Num
	 * 
	 * @param other
	 *            Num
	 * @return integer compareTo value
	 */
	public int unsignedCompareTo(Num other) {
		if (size(this) < size(other)) {
			return -1;
		} else if (size(this) > size(other)) {
			return 1;
		} else {
			return traverseToCompare(this, other);
		}
	}

	/**
	 * Print List storing the Num Output using the format "base: elements of
	 * list ..." For example, if base=100, and the number stored corresponds to
	 * 10965, then the output is "100: 65 9 1"
	 */
	public void printList() {
		Iterator<Long> iterator = num.iterator();
		System.out.print(base + ": ");
		while (iterator.hasNext())
			System.out.print(iterator.next() + " ");
		System.out.print("\n");
	}

	/**
	 * to String method
	 * 
	 * @return Return number to a string in base 10
	 */
	public String toString() {
		Num targetBase = convertBase(this, 10);

		StringBuilder sb = new StringBuilder();

		Iterator<Long> it = targetBase.num.iterator();
		
		while (it.hasNext()) {
			sb.insert(0, it.next());
		}
		return (this.sign ? '-' : "") + sb.toString();

	}

	// Returns this base
	public long base() {
		return this.base;
	}

	/**
	 * Now follow all the helper functions: nextInt next size sign
	 * traverseToCompare leftShift rightShift convertBase
	 */

	static long nextInt(Iterator<Long> it) {
		return it.hasNext() ? it.next() : -1;
	}

	static long next(Iterator<Long> it) {
		return it.hasNext() ? it.next() : 0;
	}

	static long last(Num a) {
		return (a.num.peekLast() != null) ? a.num.removeLast() : 0;
	}

	static long getLast(Num a) {
		return (a.num.peekLast() != null) ? a.num.getLast() : 0;
	}

	static long size(Num a) {
		return a.num.size();
	}

	static void sign(Num a, boolean sign) {
		a.sign = size(a) == 1 ? (a.num.peek() == 0 ? false : sign) : sign;
	}

	/**
	 * Compares two nums by traversing from the back and returns the comparison
	 * of two numbers
	 */
	static int traverseToCompare(Num a, Num b) {
		Iterator<Long> ita = a.num.descendingIterator();
		Iterator<Long> itb = b.num.descendingIterator();
		while (ita.hasNext()) {
			Long ai = next(ita);
			Long bi = next(itb);
			if (ai.compareTo(bi) != 0) {
				return ai.compareTo(bi);
			}
		}
		return 0;
	}

	static void leftShift(Num n, int k) {
		while (k > 0) {
			n.num.addFirst(0l);
			k--;
		}
	}

	static void leftShift(Num n) {
		n.num.addFirst(0l);
	}

	static void rightShift(Num n) {
		n.num.removeFirst();
	}

	static void rightShift(Num n, int k) {
		while (k > 0) {
			n.num.removeFirst();
			k--;
		}
	}

	void trim() {
		while (this.num.peekLast() == 0) {
			if (size(this) == 1)
				return;
			else
				this.num.removeLast();
		}
	}

	public static Num convertBase(Num a, long baseB) {
		long baseA = a.base;
		
		Iterator<Long> ita = a.num.descendingIterator();
		Num res = new Num("", baseB);
		while (ita.hasNext()) {
			
			res = add(product(res, baseA), new Num(next(ita), baseB));
		}
		res.sign = a.sign;
		return res;

	}

	/**
	 * Signed Compare to function implements Comparable class compares this Num
	 * with other Num
	 * 
	 * @param other
	 *            Num
	 * @return integer compare to value
	 */
	@Override
	public int compareTo(Num other) {

		if (this.sign && !other.sign) {
			return -1;
		} else if (!this.sign && other.sign) {
			return 1;
		} else {
			if (size(this) != size(other))
				return size(this) < size(other) ? -1 : 1;
			else
				return traverseToCompare(this, other);
		}

	}
}
