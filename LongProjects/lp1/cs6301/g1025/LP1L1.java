/**
 * Class to execute level 1 methods
 *  @author swaroop, saikumar, antriksh, gunjan
 *
 */
package cs6301.g1025;

import cs6301.g00.Timer;

import java.math.BigInteger;

public class LP1L1 {
    public static void main(String[] args) {

        Num x = new Num("-50");
        Num y = new Num("-100");

        BigInteger a = new BigInteger("3");
        BigInteger b = new BigInteger("4");


        Timer timer = new Timer();
        timer.start();
        Num z = Num.subtract(x, y);
        timer.end();
        System.out.println("Num1 class: " + timer);
        System.out.println();

        timer.start();
        BigInteger c = a.multiply(b);
        timer.end();
        System.out.println("BigInteger: " + timer);

        System.out.println(c);
        System.out.println(z);

//        System.out.println(false ^ true);
        //System.out.println(c.toString().compareTo(z.toString()));
    }
}
