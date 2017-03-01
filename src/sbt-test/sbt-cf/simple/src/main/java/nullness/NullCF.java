package nullness;

import org.checkerframework.checker.interning.qual.Interned;
import org.checkerframework.checker.nullness.qual.Nullable;

public class NullCF {

    private static void f(Object obj) {
        System.out.println("res: "+obj.toString());
    }

    public static void main(String[] args) {
		@Nullable Object obj = "s";
        f(obj);
    }

}