package hu.steradian.hexffs.mcplugin;

import static org.apache.commons.lang.math.NumberUtils.min;

public class Formula {
    // Szia uram
    static public double trapezoid(double x, double a, double b, double c, double d, double periodLen, double offset) {
        x += offset;
        while (x > periodLen)
            x -= periodLen;
        while (x < 0)
            x += periodLen;
        return Math.max(min((x - a) / (b - a), 1, (d - x) / (d - c)), 0) * 2 - 1;
    }
}
