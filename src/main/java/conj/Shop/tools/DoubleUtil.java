package conj.Shop.tools;

import java.text.*;

public class DoubleUtil
{
    public static String toString(final Double value) {
        return NumberFormat.getInstance().format(value);
    }
}
