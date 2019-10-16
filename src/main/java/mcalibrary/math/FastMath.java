package mcalibrary.math;

public class FastMath {
	/** A "close to zero" double epsilon value for use*/
	public static final double DBL_EPSILON = 2.220446049250313E-16d;
	/** A "close to zero" float epsilon value for use*/
	public static final float FLT_EPSILON = 1.1920928955078125E-7f;
	/** A "close to zero" float epsilon value for use*/
	public static final float ZERO_TOLERANCE = 0.0001f;
	/** The value PI as a float (180 degrees). */
	public static final float PI = (float) Math.PI;
    /** The value 2PI as a float. (360 degrees) */
    public static final float TWO_PI = 2.0f * PI;
    /** The value PI/2 as a float. (90 degrees) */
    public static final float HALF_PI = 0.5f * PI;
    /** The value PI/4 as a float. (45 degrees) */
    public static final float QUARTER_PI = 0.25f * PI;

	/**
	 * Returns the square root of a given value.
	 */
	public static float sqrt(float fValue) {
		return (float) Math.sqrt(fValue);
	}

	/**
	 * Returns 1/sqrt(fValue).
	 */
	public static float invSqrt(float fValue) {
		return (float) (1.0f / Math.sqrt(fValue));
	}

	/**
	 * Returns Absolute value of a float.
	 */
	public static float abs(float fValue) {
		if (fValue < 0) {
			return -fValue;
		}
		return fValue;
	}
	
	public static float sin(float v) {
		return (float) Math.sin(v);
	}

	public static float cos(float v) {
		return (float) Math.cos(v);
	}
	
    /**
     * Returns the tangent of a value.  If USE_FAST_TRIG is enabled, an approximate value
     * is returned.  Otherwise, a direct value is used.
     * @param fValue The value to tangent, in radians.
     * @return The tangent of fValue.
     * @see java.lang.Math#tan(double)
     */
    public static float tan(float fValue) {
        return (float) Math.tan(fValue);
    }

	/**
	 * Returns the arc sine of a value.
	 * Special cases:
	 * If fValue is smaller than -1, then the result is -HALF_PI.
	 * If the argument is greater than 1, then the result is HALF_PI.
	 */
	public static float asin(float fValue) {
		if (-1.0f < fValue) {
			if (fValue < 1.0f) {
				return (float) Math.asin(fValue);
			}

			return HALF_PI;
		}

		return -HALF_PI;
	}

	/**
	 * Returns the arc cosine of an angle given in radians.
	 * Special cases:
	 * If fValue is smaller than -1, then the result is PI.
	 * If the argument is greater than 1, then the result is 0.
	 */
	public static float acos(float fValue) {
		if (-1.0f < fValue) {
			if (fValue < 1.0f) {
				return (float) Math.acos(fValue);
			}

			return 0.0f;
		}

		return PI;
	}

	/**
	 * Returns the arc tangent of an angle given in radians.
	 * @param fValue The angle, in radians.
	 * @return fValue's atan
	 * @see java.lang.Math#atan(double)
	 */
	public static float atan(float fValue) {
		return (float) Math.atan(fValue);
	}
	
	  /**
     * A direct call to Math.atan2.
     * @param fY
     * @param fX
     * @return Math.atan2(fY,fX)
     * @see java.lang.Math#atan2(double, double)
     */
    public static float atan2(float fY, float fX) {
        return (float) Math.atan2(fY, fX);
    }

    /**
     * Rounds a fValue up.  A call to Math.ceil
     * @param fValue The value.
     * @return The fValue rounded up
     * @see java.lang.Math#ceil(double)
     */
    public static float ceil(float fValue) {
        return (float) Math.ceil(fValue);
    }
    
    /**
     * Returns a number rounded down.
     * @param fValue The value to round
     * @return The given number rounded down
     * @see java.lang.Math#floor(double)
     */
    public static float floor(float fValue) {
        return (float) Math.floor(fValue);
    }
}
