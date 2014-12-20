package de.dakror.gravityrun.util;

import java.util.Arrays;

/**
 * Central debugging class.
 * Very short names for easy and fast typing :)
 * 
 * @author Dakror
 */
public class D {
	static long last = 0;
	
	/**
	 * Time measuring. call once before and once after the the action to be time measured.
	 */
	public static void u() {
		if (last == 0) last = System.nanoTime();
		else {
			double dif = System.nanoTime() - last;
			p(r(dif) + "ns = " + r(dif /= 1000.0) + "µs = " + r(dif /= 1000.0) + "ms = " + r(dif /= 1000.0) + "s = " + r(dif /= 60.0) + "m = " + r(dif /= 60.0) + "h");
			last = 0;
		}
	}
	
	/**
	 * Rounding method for {@link #u()}
	 * 
	 * @param d
	 * @return
	 */
	private static String r(double d) {
		String s = (Math.round(d * 1000) / 1000.0) + "";
		while (s.length() < 10)
			s = " " + s;
		
		return s;
	}
	
	/**
	 * The same as a System.out.println(Object...) method.
	 * 
	 * @param objects stuff to print out
	 */
	public static void p(Object... objects) {
		if (objects.length == 1) System.out.println("" + objects[0]);
		else System.out.println(Arrays.toString(objects));
	}
}
