package team.cqr.cqrepoured.util.math;

public class GreatestCommonDivisor {

	public static int gcd(int a, int b) {
		while (b > 0) {
			int c = a % b;
			a = b;
			b = c;
		}
		return a;
	}

	public static int gcd(int[] values) {
		int result = -1;
		if (values.length == 1) {
			result = values[0];
		} else if (values.length > 1) {
			result = values[0];
			for (int i = 1; i < values.length; i++) {
				result = gcd(result, values[i]);
			}
		}
		return result;
	}

}
