package mcalibrary.math;

import java.lang.Math;

/**
 * A 4 element unit quaternion represented by single precision floating
 * point x,y,z,w coordinates. The quaternion is always normalized.
 */
public class Quaternion implements java.io.Serializable {

	public static final Quaternion EMPTY = new Quaternion(0,0,0,0);
	final static double EPS = 0.000001;

	public float x;
	public float y;
	public float z;
	public float w;

	public Quaternion(float x, float y, float z, float w)
	{
		float mag;
		mag = (float)(1.0/Math.sqrt( x*x + y*y + z*z + w*w ));
		this.x =  x*mag;
		this.y =  y*mag;
		this.z =  z*mag;
		this.w =  w*mag;
	}

	public Quaternion()
	{
		this(0F, 0F, 0F, 1F);
	}

	public Quaternion(Quaternion q1)
	{
		this.x = q1.x;
		this.y = q1.y;
		this.z = q1.z;
		this.w = q1.w;
	}

	/** Assumes axis is already normalised. Angle must be in radians. */
	public Quaternion(Vector3f axis, float angle) {
		double s = Math.sin(angle/2);
		x = (float) (axis.x * s);
		y = (float) (axis.y * s);
		z = (float) (axis.z * s);
		w = (float) Math.cos(angle/2);
	}

	/** Sets quaternion from the given matrix. */
	public Quaternion(Matrix4f mat)
	{
		double T = 1 + mat.m00 + mat.m11 + mat.m22;
		if( T > 0.00000001 ) //to avoid large distortions!
		{
			double S = Math.sqrt(T) * 2;
			this.x = (float) (( mat.m12 - mat.m21 ) / S);
			this.y = (float) (( mat.m02 - mat.m20 ) / S);
			this.z = (float) (( mat.m10 - mat.m01 ) / S);
			this.w = (float) (0.25 * S);
		} else if(T == 0)
		{
			if ( mat.m00 > mat.m11 && mat.m00 > mat.m22 )  {	// Column 0: 
				double S  = Math.sqrt( 1.0 + mat.m00 - mat.m11 - mat.m22 ) * 2;
				this.x = (float) (0.25 * S);
				this.y = (float) ((mat.m10 + mat.m01 ) / S);
				this.z = (float) ((mat.m02 + mat.m20 ) / S);
				this.w = (float) ((mat.m21 - mat.m12 ) / S);
			} else if ( mat.m11 > mat.m22 ) {			// Column 1: 
				double S  = Math.sqrt( 1.0 + mat.m11 - mat.m00 - mat.m22 ) * 2;
				this.x = (float) ((mat.m10 + mat.m01 ) / S);
				this.y = (float) (0.25 * S);
				this.z = (float) ((mat.m21 + mat.m12 ) / S);
				this.w = (float) ((mat.m02 - mat.m20 ) / S);
			} else {						// Column 2:
				double S  = Math.sqrt( 1.0 + mat.m22 - mat.m00 - mat.m11 ) * 2;
				this.x = (float) ((mat.m02 + mat.m20 ) / S);
				this.y = (float) ((mat.m21 + mat.m12 ) / S);
				this.z = (float) (0.25 * S);
				this.w = (float) ((mat.m10 - mat.m01 ) / S);
			}
		}
	}

	/**
	 * Sets the data in this <code>Quaternion</code> object to be equal to the
	 * passed <code>Quaternion</code> object. The values are copied producing
	 * a new object.
	 *
	 * @param q
	 *            The Quaternion to copy values from.
	 * @return this
	 */
	public Quaternion set(Quaternion q) {
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;
		this.w = q.w;
		return this;
	}

	/** Sets the value of this quaternion to the conjugate of quaternion q1 */
	public final void conjugate(Quaternion q1)
	{
		this.x = -q1.x;
		this.y = -q1.y;
		this.z = -q1.z;
		this.w = q1.w;
	}

	/** Sets the value of this quaternion to the conjugate of itself */
	public final void conjugate()
	{
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	/** Sets the value of this quaternion to the quaternion product of this and q1. */
	public final void mul(Quaternion q1) {
		this.mul(this, q1);
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * quaternions q1 and q2 (this = q1 * q2). 
	 * Note that this is safe for aliasing (e.g. this can be q1 or q2).
	 * This operation is used for adding the 2 orientations.
	 */
	public final void mul(Quaternion q1, Quaternion q2)
	{
		if (this != q1 && this != q2) {
			this.w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
			this.x = q1.w*q2.x + q2.w*q1.x + q1.y*q2.z - q1.z*q2.y;
			this.y = q1.w*q2.y + q2.w*q1.y - q1.x*q2.z + q1.z*q2.x;
			this.z = q1.w*q2.z + q2.w*q1.z + q1.x*q2.y - q1.y*q2.x;
		} else {
			float x, y, w;

			w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
			x = q1.w*q2.x + q2.w*q1.x + q1.y*q2.z - q1.z*q2.y;
			y = q1.w*q2.y + q2.w*q1.y - q1.x*q2.z + q1.z*q2.x;
			this.z = q1.w*q2.z + q2.w*q1.z + q1.x*q2.y - q1.y*q2.x;
			this.w = w;
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Multiplies quaternion q1 by the inverse of quaternion q2 and places
	 * the value into this quaternion.  The value of both argument quaternions
	 * is preservered (this = q1 * q2^-1)
	 */
	public final void mulInverse(Quaternion q1, Quaternion q2)
	{  
		Quaternion  tempQuat = new Quaternion(q2); 

		tempQuat.inverse();
		this.mul(q1, tempQuat);
	}

	/**
	 * Sets the value of this quaternion to quaternion inverse of quaternion q1.
	 * @param q1 the quaternion to be inverted
	 */
	public final void inverse(Quaternion q1)
	{
		float norm;

		norm = 1.0f/(q1.w*q1.w + q1.x*q1.x + q1.y*q1.y + q1.z*q1.z);
		this.w =  norm*q1.w;
		this.x = -norm*q1.x;
		this.y = -norm*q1.y;
		this.z = -norm*q1.z;
	}

	/**
	 * Sets the value of this quaternion to the quaternion inverse of itself.
	 */
	public final void inverse()
	{
		float norm; 

		norm = 1.0f/(this.w*this.w + this.x*this.x + this.y*this.y + this.z*this.z);
		this.w *=  norm;
		this.x *= -norm;
		this.y *= -norm;
		this.z *= -norm;
	}

	/**
	 * Sets the value of this quaternion to the normalized value
	 * of quaternion q1.
	 * @param q1 the quaternion to be normalized.
	 */
	public final void normalize(Quaternion q1)
	{
		float norm;

		norm = (q1.x*q1.x + q1.y*q1.y + q1.z*q1.z + q1.w*q1.w);

		if (norm > 0.0f) {
			norm = 1.0f/(float)Math.sqrt(norm);
			this.x = norm*q1.x;
			this.y = norm*q1.y;
			this.z = norm*q1.z;
			this.w = norm*q1.w;
		} else {
			this.x = (float) 0.0;
			this.y = (float) 0.0;
			this.z = (float) 0.0;
			this.w = (float) 0.0;
		}
	}


	/**
	 * Normalizes the value of this quaternion in place.
	 */
	public final void normalize()
	{
		float norm;

		norm = (this.x*this.x + this.y*this.y + this.z*this.z + this.w*this.w);

		if (norm > 0.0f) {
			norm = 1.0f / (float)Math.sqrt(norm);
			this.x *= norm;
			this.y *= norm;
			this.z *= norm;
			this.w *= norm;
		} else {
			this.x = (float) 0.0;
			this.y = (float) 0.0;
			this.z = (float) 0.0;
			this.w = (float) 0.0;
		}
	}

	/**
	 * Adds the values of this quaternion to those of the
	 * parameter quaternion. The result is stored in this Quaternion.
	 *
	 * @param q
	 *            the quaternion to add to this.
	 * @return This Quaternion after addition.
	 */
	public Quaternion add(Quaternion q) {
		this.x += q.x;
		this.y += q.y;
		this.z += q.z;
		this.w += q.w;
		return this;
	}

	/**
	 * Subtracts the values of the parameter quaternion
	 * from those of this quaternion. The result is stored in this Quaternion.
	 *
	 * @param q
	 *            the quaternion to subtract from this.
	 * @return This Quaternion after subtraction.
	 */
	public Quaternion subtract(Quaternion q) {
		this.x -= q.x;
		this.y -= q.y;
		this.z -= q.z;
		this.w -= q.w;
		return this;
	}

	/**
	 * Calculates and returns the dot product of this
	 * quaternion with that of the parameter quaternion.
	 *
	 * @param q
	 *            the quaternion to calculate the dot product of.
	 * @return the dot product of this and the parameter quaternion.
	 */
	public float dot(Quaternion q) {
		return w * q.w + x * q.x + y * q.y + z * q.z;
	}

	/**
	 * Returns the norm of this quaternion. This is the dot
	 * product of this quaternion with itself.
	 *
	 * @return the norm of the quaternion.
	 */
	public float norm() {
		return w * w + x * x + y * y + z * z;
	}
	
    /**
     * Normalizes the current <code>Quaternion</code>.
     * The result is stored internally.
     */
    public Quaternion normalizeLocal() {
        float n = FastMath.invSqrt(norm());
        x *= n;
        y *= n;
        z *= n;
        w *= n;
        return this;
    }

	/**
	 * <code>slerp</code> sets this quaternion's value as an interpolation
	 * between two other quaternions.
	 *
	 * @param q1
	 *            the first quaternion.
	 * @param q2
	 *            the second quaternion.
	 * @param t
	 *            the amount to interpolate between the two quaternions.
	 */
	public Quaternion slerp(Quaternion q1, Quaternion q2, float t) {
		// Create a local quaternion to store the interpolated quaternion
		if (q1.x == q2.x && q1.y == q2.y && q1.z == q2.z && q1.w == q2.w) {
			this.set(q1);
			return this;
		}

		float result = (q1.x * q2.x) + (q1.y * q2.y) + (q1.z * q2.z)
				+ (q1.w * q2.w);

		if (result < 0.0f) {
			// Negate the second quaternion and the result of the dot product
			q2.x = -q2.x;
			q2.y = -q2.y;
			q2.z = -q2.z;
			q2.w = -q2.w;
			result = -result;
		}

		// Set the first and second scale for the interpolation
		float scale0 = 1 - t;
		float scale1 = t;

		// Check if the angle between the 2 quaternions was big enough to
		// warrant such calculations
		if ((1 - result) > 0.1f) {// Get the angle between the 2 quaternions,
			// and then store the sin() of that angle
			float theta = FastMath.acos(result);
			float invSinTheta = 1f / FastMath.sin(theta);

			// Calculate the scale for q1 and q2, according to the angle and
			// it's sine value
			scale0 = FastMath.sin((1 - t) * theta) * invSinTheta;
			scale1 = FastMath.sin((t * theta)) * invSinTheta;
		}

		// Calculate the x, y, z and w values for the quaternion by using a
		// special
		// form of linear interpolation for quaternions.
		this.x = (scale0 * q1.x) + (scale1 * q2.x);
		this.y = (scale0 * q1.y) + (scale1 * q2.y);
		this.z = (scale0 * q1.z) + (scale1 * q2.z);
		this.w = (scale0 * q1.w) + (scale1 * q2.w);

		// Return the interpolated quaternion
		return this;
	}

	/**
	 * Sets the values of this quaternion to the slerp from itself to q2 by
	 * changeAmnt
	 *
	 * @param q2
	 *            Final interpolation value
	 * @param changeAmnt
	 *            The amount diffrence
	 */
	public void slerp(Quaternion q2, float changeAmnt) {
		if (this.x == q2.x && this.y == q2.y && this.z == q2.z
				&& this.w == q2.w) {
			return;
		}

		float result = (this.x * q2.x) + (this.y * q2.y) + (this.z * q2.z)
				+ (this.w * q2.w);

		if (result < 0.0f) {
			// Negate the second quaternion and the result of the dot product
			q2.x = -q2.x;
			q2.y = -q2.y;
			q2.z = -q2.z;
			q2.w = -q2.w;
			result = -result;
		}

		// Set the first and second scale for the interpolation
		float scale0 = 1 - changeAmnt;
		float scale1 = changeAmnt;

		// Check if the angle between the 2 quaternions was big enough to
		// warrant such calculations
		if ((1 - result) > 0.1f) {
			// Get the angle between the 2 quaternions, and then store the sin()
			// of that angle
			float theta = FastMath.acos(result);
			float invSinTheta = 1f / FastMath.sin(theta);

			// Calculate the scale for q1 and q2, according to the angle and
			// it's sine value
			scale0 = FastMath.sin((1 - changeAmnt) * theta) * invSinTheta;
			scale1 = FastMath.sin((changeAmnt * theta)) * invSinTheta;
		}

		// Calculate the x, y, z and w values for the quaternion by using a
		// special
		// form of linear interpolation for quaternions.
		this.x = (scale0 * this.x) + (scale1 * q2.x);
		this.y = (scale0 * this.y) + (scale1 * q2.y);
		this.z = (scale0 * this.z) + (scale1 * q2.z);
		this.w = (scale0 * this.w) + (scale1 * q2.w);
	}

	/**
	 * Sets the values of this quaternion to the nlerp from itself to q2 by blend.
	 * @param q2
	 * @param blend
	 */
	public void nlerp(Quaternion q2, float blend) {
		float dot = dot(q2);
		float blendI = 1.0f - blend;
		if (dot < 0.0f) {
			x = blendI * x - blend * q2.x;
			y = blendI * y - blend * q2.y;
			z = blendI * z - blend * q2.z;
			w = blendI * w - blend * q2.w;
		} else {
			x = blendI * x + blend * q2.x;
			y = blendI * y + blend * q2.y;
			z = blendI * z + blend * q2.z;
			w = blendI * w + blend * q2.w;
		}
		normalizeLocal();
	}
}