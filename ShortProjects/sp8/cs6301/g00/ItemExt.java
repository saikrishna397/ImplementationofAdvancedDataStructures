/**
 * Item class for generics programming
 *
 * @author Antriksh, Gunjan, Atif
 * Short Project 1: 2017/08/28
 */

package cs6301.g00;

import java.util.Comparator;

public class ItemExt extends Item implements Comparator<ItemExt> {
    private int extra;

    public ItemExt(int x) {
        super(x);
        extra = -x;
    }

    public int getItem() {
        return element;
    }

    public void setItem(int x) {
        element = x;
    }

    public String toString() {
        return Integer.toString(element);
    }

    public int compare(ItemExt first, ItemExt second) {
        if (first.extra > second.extra) {
            return 1;
        } else if (first.extra < second.extra) {
            return -1;
        } else return 0;
    }
}
