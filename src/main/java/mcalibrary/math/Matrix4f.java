package mcalibrary.math;

public class Matrix4f implements java.io.Serializable {

	/**
	 * The first element of the first row.
	 */
	public float m00;

	/**
	 * The second element of the first row.
	 */
	public float m01;

	/**
	 * The third element of the first row.
	 */
	public float m02;

	/**
	 * The fourth element of the first row.
	 */
	public float m03;

	/**
	 * The first element of the second row.
	 */
	public float m10;

	/**
	 * The second element of the second row.
	 */
	public float m11;

	/**
	 * The third element of the second row.
	 */
	public float m12;

	/**
	 * The fourth element of the second row.
	 */
	public float m13;

	/**
	 * The first element of the third row.
	 */
	public float m20;

	/**
	 * The second element of the third row.
	 */
	public float m21;

	/**
	 * The third element of the third row.
	 */
	public float m22;

	/**
	 * The fourth element of the third row.
	 */
	public float m23;

	/**
	 * The first element of the fourth row.
	 */
	public float m30;

	/**
	 * The second element of the fourth row.
	 */
	public float m31;

	/**
	 * The third element of the fourth row.
	 */
	public float m32;

	/**
	 * The fourth element of the fourth row.
	 */
	public float m33;

	private static final double EPS = 1.0E-8;

	/**
	 * Constructs and initializes a Matrix4f from the specified 16 values.
	 * 
	 * @param m00 the [0][0] element
	 * @param m01 the [0][1] element
	 * @param m02 the [0][2] element
	 * @param m03 the [0][3] element
	 * @param m10 the [1][0] element
	 * @param m11 the [1][1] element
	 * @param m12 the [1][2] element
	 * @param m13 the [1][3] element
	 * @param m20 the [2][0] element
	 * @param m21 the [2][1] element
	 * @param m22 the [2][2] element
	 * @param m23 the [2][3] element
	 * @param m30 the [3][0] element
	 * @param m31 the [3][1] element
	 * @param m32 the [3][2] element
	 * @param m33 the [3][3] element
	 */
	public Matrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;

		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;

		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;

		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	/**
	 * Constructs and initializes a Matrix4f from the specified 16
	 * element array. this.m00 =v[0], this.m01=v[1], etc.
	 * 
	 * @param v the array of length 16 containing in order
	 */
	public Matrix4f(float[] v) {
		this.m00 = v[0];
		this.m01 = v[1];
		this.m02 = v[2];
		this.m03 = v[3];

		this.m10 = v[4];
		this.m11 = v[5];
		this.m12 = v[6];
		this.m13 = v[7];

		this.m20 = v[8];
		this.m21 = v[9];
		this.m22 = v[10];
		this.m23 = v[11];

		this.m30 = v[12];
		this.m31 = v[13];
		this.m32 = v[14];
		this.m33 = v[15];
	}

	/**
	 * Constructs and initializes a Matrix4f from the quaternion,
	 * translation, and scale values; the scale is applied only to the
	 * rotational components of the matrix (upper 3x3) and not to the
	 * translational components.
	 * 
	 * @param q1 the quaternion value representing the rotational component
	 * @param t1 the translational component of the matrix
	 * @param s  the scale value applied to the rotational components
	 */
	public Matrix4f(Quaternion q1, Vector3f t1, float s) {
		this.m00 = (float) (s * (1.0 - 2.0 * q1.y * q1.y - 2.0 * q1.z * q1.z));
		this.m10 = (float) (s * (2.0 * (q1.x * q1.y + q1.w * q1.z)));
		this.m20 = (float) (s * (2.0 * (q1.x * q1.z - q1.w * q1.y)));

		this.m01 = (float) (s * (2.0 * (q1.x * q1.y - q1.w * q1.z)));
		this.m11 = (float) (s * (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.z * q1.z));
		this.m21 = (float) (s * (2.0 * (q1.y * q1.z + q1.w * q1.x)));

		this.m02 = (float) (s * (2.0 * (q1.x * q1.z + q1.w * q1.y)));
		this.m12 = (float) (s * (2.0 * (q1.y * q1.z - q1.w * q1.x)));
		this.m22 = (float) (s * (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.y * q1.y));

		this.m03 = t1.x;
		this.m13 = t1.y;
		this.m23 = t1.z;

		this.m30 = 0.0f;
		this.m31 = 0.0f;
		this.m32 = 0.0f;
		this.m33 = 1.0f;
	}

	/**
	 * Constructs a new matrix with the same values as the
	 * Matrix4f parameter.
	 * 
	 * @param m1 the source matrix
	 */
	public Matrix4f(Matrix4f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m03 = m1.m03;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m13 = m1.m13;

		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
		this.m23 = m1.m23;

		this.m30 = m1.m30;
		this.m31 = m1.m31;
		this.m32 = m1.m32;
		this.m33 = m1.m33;
	}

	/**
	 * Constructs and initializes a Matrix4f from the rotation matrix,
	 * translation, and scale values; the scale is applied only to the
	 * rotational components of the matrix (upper 3x3) and not to the
	 * translational components of the matrix.
	 * 
	 * @param m1 the rotation matrix representing the rotational components
	 * @param t1 the translational components of the matrix
	 * @param s  the scale value applied to the rotational components
	 */
	public Matrix4f(Matrix3f m1, Vector3f t1, float s) {
		this.m00 = m1.m00 * s;
		this.m01 = m1.m01 * s;
		this.m02 = m1.m02 * s;
		this.m03 = t1.x;

		this.m10 = m1.m10 * s;
		this.m11 = m1.m11 * s;
		this.m12 = m1.m12 * s;
		this.m13 = t1.y;

		this.m20 = m1.m20 * s;
		this.m21 = m1.m21 * s;
		this.m22 = m1.m22 * s;
		this.m23 = t1.z;

		this.m30 = 0.0f;
		this.m31 = 0.0f;
		this.m32 = 0.0f;
		this.m33 = 1.0f;
	}

	/**
	 * Constructs and initializes a Matrix4f to all zeros.
	 */
	public Matrix4f() {
		this.m00 = (float) 0.0;
		this.m01 = (float) 0.0;
		this.m02 = (float) 0.0;
		this.m03 = (float) 0.0;

		this.m10 = (float) 0.0;
		this.m11 = (float) 0.0;
		this.m12 = (float) 0.0;
		this.m13 = (float) 0.0;

		this.m20 = (float) 0.0;
		this.m21 = (float) 0.0;
		this.m22 = (float) 0.0;
		this.m23 = (float) 0.0;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 0.0;
	}

	/**
	 * Returns a string that contains the values of this Matrix4f.
	 * 
	 * @return the String representation
	 */
	@Override
	public String toString() {
		return this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n" + this.m30
				+ ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
	}

	/**
	 * Sets this Matrix4f to identity.
	 */
	public final void setIdentity() {
		this.m00 = (float) 1.0;
		this.m01 = (float) 0.0;
		this.m02 = (float) 0.0;
		this.m03 = (float) 0.0;

		this.m10 = (float) 0.0;
		this.m11 = (float) 1.0;
		this.m12 = (float) 0.0;
		this.m13 = (float) 0.0;

		this.m20 = (float) 0.0;
		this.m21 = (float) 0.0;
		this.m22 = (float) 1.0;
		this.m23 = (float) 0.0;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Sets the specified element of this matrix4f to the value provided.
	 * 
	 * @param row    the row number to be modified (zero indexed)
	 * @param column the column number to be modified (zero indexed)
	 * @param value  the new value
	 */
	public final void setElement(int row, int column, float value) {
		switch (row) {
		case 0:
			switch (column) {
			case 0:
				this.m00 = value;
				break;
			case 1:
				this.m01 = value;
				break;
			case 2:
				this.m02 = value;
				break;
			case 3:
				this.m03 = value;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
			}
			break;

		case 1:
			switch (column) {
			case 0:
				this.m10 = value;
				break;
			case 1:
				this.m11 = value;
				break;
			case 2:
				this.m12 = value;
				break;
			case 3:
				this.m13 = value;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
			}
			break;

		case 2:
			switch (column) {
			case 0:
				this.m20 = value;
				break;
			case 1:
				this.m21 = value;
				break;
			case 2:
				this.m22 = value;
				break;
			case 3:
				this.m23 = value;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
			}
			break;

		case 3:
			switch (column) {
			case 0:
				this.m30 = value;
				break;
			case 1:
				this.m31 = value;
				break;
			case 2:
				this.m32 = value;
				break;
			case 3:
				this.m33 = value;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
			}
			break;

		default:
			throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
		}
	}

	/**
	 * Retrieves the value at the specified row and column of this matrix.
	 * 
	 * @param row    the row number to be retrieved (zero indexed)
	 * @param column the column number to be retrieved (zero indexed)
	 * @return the value at the indexed element
	 */
	public final float getElement(int row, int column) {
		switch (row) {
		case 0:
			switch (column) {
			case 0:
				return (this.m00);
			case 1:
				return (this.m01);
			case 2:
				return (this.m02);
			case 3:
				return (this.m03);
			default:
				break;
			}
			break;
		case 1:
			switch (column) {
			case 0:
				return (this.m10);
			case 1:
				return (this.m11);
			case 2:
				return (this.m12);
			case 3:
				return (this.m13);
			default:
				break;
			}
			break;

		case 2:
			switch (column) {
			case 0:
				return (this.m20);
			case 1:
				return (this.m21);
			case 2:
				return (this.m22);
			case 3:
				return (this.m23);
			default:
				break;
			}
			break;

		case 3:
			switch (column) {
			case 0:
				return (this.m30);
			case 1:
				return (this.m31);
			case 2:
				return (this.m32);
			case 3:
				return (this.m33);
			default:
				break;
			}
			break;

		default:
			break;
		}
		throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
	}

	/**
	 * Performs an SVD normalization of this matrix in order to acquire
	 * the normalized rotational component; the values are placed into
	 * the Matrix3f parameter.
	 * 
	 * @param m1 matrix into which the rotational component is placed
	 */
	public final void get(Matrix3f m1) {
		double[] tmp_rot = new double[9]; // scratch matrix
		double[] tmp_scale = new double[3]; // scratch matrix

		this.getScaleRotate(tmp_scale, tmp_rot);

		m1.m00 = (float) tmp_rot[0];
		m1.m01 = (float) tmp_rot[1];
		m1.m02 = (float) tmp_rot[2];

		m1.m10 = (float) tmp_rot[3];
		m1.m11 = (float) tmp_rot[4];
		m1.m12 = (float) tmp_rot[5];

		m1.m20 = (float) tmp_rot[6];
		m1.m21 = (float) tmp_rot[7];
		m1.m22 = (float) tmp_rot[8];

	}

	/**
	 * Performs an SVD normalization of this matrix in order to acquire
	 * the normalized rotational component; the values are placed into
	 * the Quaternion parameter.
	 * 
	 * @param q1 quaternion into which the rotation component is placed
	 */
	public final void get(Quaternion q1) {
		double[] tmp_rot = new double[9]; // scratch matrix
		double[] tmp_scale = new double[3]; // scratch matrix
		this.getScaleRotate(tmp_scale, tmp_rot);

		double ww;

		ww = 0.25 * (1.0 + tmp_rot[0] + tmp_rot[4] + tmp_rot[8]);
		if (!((ww < 0 ? -ww : ww) < 1.0e-30)) {
			q1.w = (float) Math.sqrt(ww);
			ww = 0.25 / q1.w;
			q1.x = (float) ((tmp_rot[7] - tmp_rot[5]) * ww);
			q1.y = (float) ((tmp_rot[2] - tmp_rot[6]) * ww);
			q1.z = (float) ((tmp_rot[3] - tmp_rot[1]) * ww);
			return;
		}

		q1.w = 0.0f;
		ww = -0.5 * (tmp_rot[4] + tmp_rot[8]);
		if (!((ww < 0 ? -ww : ww) < 1.0e-30)) {
			q1.x = (float) Math.sqrt(ww);
			ww = 0.5 / q1.x;
			q1.y = (float) (tmp_rot[3] * ww);
			q1.z = (float) (tmp_rot[6] * ww);
			return;
		}

		q1.x = 0.0f;
		ww = 0.5 * (1.0 - tmp_rot[8]);
		if (!((ww < 0 ? -ww : ww) < 1.0e-30)) {
			q1.y = (float) (Math.sqrt(ww));
			q1.z = (float) (tmp_rot[7] / (2.0 * q1.y));
			return;
		}

		q1.y = 0.0f;
		q1.z = 1.0f;
	}

	/**
	 * Retrieves the translational components of this matrix.
	 * 
	 * @param trans the vector that will receive the translational component
	 */
	public final void get(Vector3f trans) {
		trans.x = this.m03;
		trans.y = this.m13;
		trans.z = this.m23;
	}

	/**
	 * Gets the upper 3x3 values of this matrix and places them into
	 * the matrix m1.
	 * 
	 * @param m1 the matrix that will hold the values
	 */
	public final void getRotationScale(Matrix3f m1) {
		m1.m00 = this.m00;
		m1.m01 = this.m01;
		m1.m02 = this.m02;
		m1.m10 = this.m10;
		m1.m11 = this.m11;
		m1.m12 = this.m12;
		m1.m20 = this.m20;
		m1.m21 = this.m21;
		m1.m22 = this.m22;
	}

	/**
	 * Performs an SVD normalization of this matrix to calculate
	 * and return the uniform scale factor. If the matrix has non-uniform
	 * scale factors, the largest of the x, y, and z scale factors will
	 * be returned. This matrix is not modified.
	 * 
	 * @return the scale factor of this matrix
	 */
	public final float getScale() {
		double[] tmp_rot = new double[9]; // scratch matrix
		double[] tmp_scale = new double[3]; // scratch matrix

		this.getScaleRotate(tmp_scale, tmp_rot);

		return ((float) Matrix3d.max3(tmp_scale));
	}

	/**
	 * Replaces the upper 3x3 matrix values of this matrix with the
	 * values in the matrix m1.
	 * 
	 * @param m1 the matrix that will be the new upper 3x3
	 */
	public final void setRotationScale(Matrix3f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
	}

	/**
	 * Sets the specified row of this matrix4f to the four values provided.
	 * 
	 * @param row the row number to be modified (zero indexed)
	 * @param x   the first column element
	 * @param y   the second column element
	 * @param z   the third column element
	 * @param w   the fourth column element
	 */
	public final void setRow(int row, float x, float y, float z, float w) {
		switch (row) {
		case 0:
			this.m00 = x;
			this.m01 = y;
			this.m02 = z;
			this.m03 = w;
			break;

		case 1:
			this.m10 = x;
			this.m11 = y;
			this.m12 = z;
			this.m13 = w;
			break;

		case 2:
			this.m20 = x;
			this.m21 = y;
			this.m22 = z;
			this.m23 = w;
			break;

		case 3:
			this.m30 = x;
			this.m31 = y;
			this.m32 = z;
			this.m33 = w;
			break;

		default:
			throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
		}
	}

	/**
	 * Sets the specified row of this matrix4f to the four values provided
	 * in the passed array.
	 * 
	 * @param row the row number to be modified (zero indexed)
	 * @param v   the replacement row
	 */
	public final void setRow(int row, float v[]) {
		switch (row) {
		case 0:
			this.m00 = v[0];
			this.m01 = v[1];
			this.m02 = v[2];
			this.m03 = v[3];
			break;

		case 1:
			this.m10 = v[0];
			this.m11 = v[1];
			this.m12 = v[2];
			this.m13 = v[3];
			break;

		case 2:
			this.m20 = v[0];
			this.m21 = v[1];
			this.m22 = v[2];
			this.m23 = v[3];
			break;

		case 3:
			this.m30 = v[0];
			this.m31 = v[1];
			this.m32 = v[2];
			this.m33 = v[3];
			break;

		default:
			throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
		}
	}

	/**
	 * Sets the specified column of this matrix4f to the four values provided.
	 * 
	 * @param column the column number to be modified (zero indexed)
	 * @param x      the first row element
	 * @param y      the second row element
	 * @param z      the third row element
	 * @param w      the fourth row element
	 */
	public final void setColumn(int column, float x, float y, float z, float w) {
		switch (column) {
		case 0:
			this.m00 = x;
			this.m10 = y;
			this.m20 = z;
			this.m30 = w;
			break;

		case 1:
			this.m01 = x;
			this.m11 = y;
			this.m21 = z;
			this.m31 = w;
			break;

		case 2:
			this.m02 = x;
			this.m12 = y;
			this.m22 = z;
			this.m32 = w;
			break;

		case 3:
			this.m03 = x;
			this.m13 = y;
			this.m23 = z;
			this.m33 = w;
			break;

		default:
			throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f9");
		}
	}

	/**
	 * Sets the specified column of this matrix4f to the four values provided.
	 * 
	 * @param column the column number to be modified (zero indexed)
	 * @param v      the replacement column
	 */
	public final void setColumn(int column, float v[]) {
		switch (column) {
		case 0:
			this.m00 = v[0];
			this.m10 = v[1];
			this.m20 = v[2];
			this.m30 = v[3];
			break;

		case 1:
			this.m01 = v[0];
			this.m11 = v[1];
			this.m21 = v[2];
			this.m31 = v[3];
			break;

		case 2:
			this.m02 = v[0];
			this.m12 = v[1];
			this.m22 = v[2];
			this.m32 = v[3];
			break;

		case 3:
			this.m03 = v[0];
			this.m13 = v[1];
			this.m23 = v[2];
			this.m33 = v[3];
			break;

		default:
			throw new ArrayIndexOutOfBoundsException("PROBLEM!!! Matrix4f");
		}
	}

	/**
	 * Adds a scalar to each component of this matrix.
	 * 
	 * @param scalar the scalar adder
	 */
	public final void add(float scalar) {
		this.m00 += scalar;
		this.m01 += scalar;
		this.m02 += scalar;
		this.m03 += scalar;
		this.m10 += scalar;
		this.m11 += scalar;
		this.m12 += scalar;
		this.m13 += scalar;
		this.m20 += scalar;
		this.m21 += scalar;
		this.m22 += scalar;
		this.m23 += scalar;
		this.m30 += scalar;
		this.m31 += scalar;
		this.m32 += scalar;
		this.m33 += scalar;
	}

	/**
	 * Adds a scalar to each component of the matrix m1 and places
	 * the result into this. Matrix m1 is not modified.
	 * 
	 * @param scalar the scalar adder
	 * @param m1     the original matrix values
	 */
	public final void add(float scalar, Matrix4f m1) {
		this.m00 = m1.m00 + scalar;
		this.m01 = m1.m01 + scalar;
		this.m02 = m1.m02 + scalar;
		this.m03 = m1.m03 + scalar;
		this.m10 = m1.m10 + scalar;
		this.m11 = m1.m11 + scalar;
		this.m12 = m1.m12 + scalar;
		this.m13 = m1.m13 + scalar;
		this.m20 = m1.m20 + scalar;
		this.m21 = m1.m21 + scalar;
		this.m22 = m1.m22 + scalar;
		this.m23 = m1.m23 + scalar;
		this.m30 = m1.m30 + scalar;
		this.m31 = m1.m31 + scalar;
		this.m32 = m1.m32 + scalar;
		this.m33 = m1.m33 + scalar;
	}

	/**
	 * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
	 * 
	 * @param m1 the first matrix
	 * @param m2 the second matrix
	 */
	public final void add(Matrix4f m1, Matrix4f m2) {
		this.m00 = m1.m00 + m2.m00;
		this.m01 = m1.m01 + m2.m01;
		this.m02 = m1.m02 + m2.m02;
		this.m03 = m1.m03 + m2.m03;

		this.m10 = m1.m10 + m2.m10;
		this.m11 = m1.m11 + m2.m11;
		this.m12 = m1.m12 + m2.m12;
		this.m13 = m1.m13 + m2.m13;

		this.m20 = m1.m20 + m2.m20;
		this.m21 = m1.m21 + m2.m21;
		this.m22 = m1.m22 + m2.m22;
		this.m23 = m1.m23 + m2.m23;

		this.m30 = m1.m30 + m2.m30;
		this.m31 = m1.m31 + m2.m31;
		this.m32 = m1.m32 + m2.m32;
		this.m33 = m1.m33 + m2.m33;
	}

	/**
	 * Sets the value of this matrix to the sum of itself and matrix m1.
	 * 
	 * @param m1 the other matrix
	 */
	public final void add(Matrix4f m1) {
		this.m00 += m1.m00;
		this.m01 += m1.m01;
		this.m02 += m1.m02;
		this.m03 += m1.m03;

		this.m10 += m1.m10;
		this.m11 += m1.m11;
		this.m12 += m1.m12;
		this.m13 += m1.m13;

		this.m20 += m1.m20;
		this.m21 += m1.m21;
		this.m22 += m1.m22;
		this.m23 += m1.m23;

		this.m30 += m1.m30;
		this.m31 += m1.m31;
		this.m32 += m1.m32;
		this.m33 += m1.m33;
	}

	/**
	 * Performs an element-by-element subtraction of matrix m2 from
	 * matrix m1 and places the result into matrix this (this =
	 * m2 - m1).
	 * 
	 * @param m1 the first matrix
	 * @param m2 the second matrix
	 */
	public final void sub(Matrix4f m1, Matrix4f m2) {
		this.m00 = m1.m00 - m2.m00;
		this.m01 = m1.m01 - m2.m01;
		this.m02 = m1.m02 - m2.m02;
		this.m03 = m1.m03 - m2.m03;

		this.m10 = m1.m10 - m2.m10;
		this.m11 = m1.m11 - m2.m11;
		this.m12 = m1.m12 - m2.m12;
		this.m13 = m1.m13 - m2.m13;

		this.m20 = m1.m20 - m2.m20;
		this.m21 = m1.m21 - m2.m21;
		this.m22 = m1.m22 - m2.m22;
		this.m23 = m1.m23 - m2.m23;

		this.m30 = m1.m30 - m2.m30;
		this.m31 = m1.m31 - m2.m31;
		this.m32 = m1.m32 - m2.m32;
		this.m33 = m1.m33 - m2.m33;
	}

	/**
	 * Sets this matrix to the matrix difference of itself and
	 * matrix m1 (this = this - m1).
	 * 
	 * @param m1 the other matrix
	 */
	public final void sub(Matrix4f m1) {
		this.m00 -= m1.m00;
		this.m01 -= m1.m01;
		this.m02 -= m1.m02;
		this.m03 -= m1.m03;

		this.m10 -= m1.m10;
		this.m11 -= m1.m11;
		this.m12 -= m1.m12;
		this.m13 -= m1.m13;

		this.m20 -= m1.m20;
		this.m21 -= m1.m21;
		this.m22 -= m1.m22;
		this.m23 -= m1.m23;

		this.m30 -= m1.m30;
		this.m31 -= m1.m31;
		this.m32 -= m1.m32;
		this.m33 -= m1.m33;
	}

	/**
	 * Sets the value of this matrix to its transpose in place.
	 */
	public final Matrix4f transpose() {
		float temp;

		temp = this.m10;
		this.m10 = this.m01;
		this.m01 = temp;

		temp = this.m20;
		this.m20 = this.m02;
		this.m02 = temp;

		temp = this.m30;
		this.m30 = this.m03;
		this.m03 = temp;

		temp = this.m21;
		this.m21 = this.m12;
		this.m12 = temp;

		temp = this.m31;
		this.m31 = this.m13;
		this.m13 = temp;

		temp = this.m32;
		this.m32 = this.m23;
		this.m23 = temp;

		return this;
	}

	/**
	 * Sets the value of this matrix to the transpose of the argument matrix.
	 * 
	 * @param m1 the matrix to be transposed
	 */
	public final void transpose(Matrix4f m1) {
		if (this != m1) {
			this.m00 = m1.m00;
			this.m01 = m1.m10;
			this.m02 = m1.m20;
			this.m03 = m1.m30;

			this.m10 = m1.m01;
			this.m11 = m1.m11;
			this.m12 = m1.m21;
			this.m13 = m1.m31;

			this.m20 = m1.m02;
			this.m21 = m1.m12;
			this.m22 = m1.m22;
			this.m23 = m1.m32;

			this.m30 = m1.m03;
			this.m31 = m1.m13;
			this.m32 = m1.m23;
			this.m33 = m1.m33;
		} else {
			this.transpose();
		}
	}

	/**
	 * Sets the value of this matrix to the matrix conversion of the
	 * single precision quaternion argument.
	 * 
	 * @param q1 the quaternion to be converted
	 */
	public final Matrix4f set(Quaternion q1) {
		/*
		 * How it is done:
		 * 1.0f - 2.0f*qy*qy - 2.0f*qz*qz, 2.0f*qx*qy - 2.0f*qz*qw, 2.0f*qx*qz + 2.0f*qy*qw, 0.0f,
		 * 2.0f*qx*qy + 2.0f*qz*qw, 1.0f - 2.0f*qx*qx - 2.0f*qz*qz, 2.0f*qy*qz - 2.0f*qx*qw, 0.0f,
		 * 2.0f*qx*qz - 2.0f*qy*qw, 2.0f*qy*qz + 2.0f*qx*qw, 1.0f - 2.0f*qx*qx - 2.0f*qy*qy, 0.0f,
		 * 0.0f, 0.0f, 0.0f, 1.0f
		 */
		this.m00 = (1.0f - 2.0f * q1.y * q1.y - 2.0f * q1.z * q1.z);
		this.m10 = (2.0f * (q1.x * q1.y + q1.w * q1.z));
		this.m20 = (2.0f * (q1.x * q1.z - q1.w * q1.y));

		this.m01 = (2.0f * (q1.x * q1.y - q1.w * q1.z));
		this.m11 = (1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.z * q1.z);
		this.m21 = (2.0f * (q1.y * q1.z + q1.w * q1.x));

		this.m02 = (2.0f * (q1.x * q1.z + q1.w * q1.y));
		this.m12 = (2.0f * (q1.y * q1.z - q1.w * q1.x));
		this.m22 = (1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.y * q1.y);

		this.m03 = (float) 0.0;
		this.m13 = (float) 0.0;
		this.m23 = (float) 0.0;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;

		return this;
	}

	/**
	 * Sets the value of this matrix to the matrix conversion of the
	 * (single precision) axis and angle argument.
	 * 
	 * @param a1 the axis and angle to be converted
	 */
	public final void set(AxisAngle4f a1) {
		float mag = (float) Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);
		if (mag < EPS) {
			this.m00 = 1.0f;
			this.m01 = 0.0f;
			this.m02 = 0.0f;

			this.m10 = 0.0f;
			this.m11 = 1.0f;
			this.m12 = 0.0f;

			this.m20 = 0.0f;
			this.m21 = 0.0f;
			this.m22 = 1.0f;
		} else {
			mag = 1.0f / mag;
			float ax = a1.x * mag;
			float ay = a1.y * mag;
			float az = a1.z * mag;

			float sinTheta = (float) Math.sin((double) a1.angle);
			float cosTheta = (float) Math.cos((double) a1.angle);
			float t = 1.0f - cosTheta;

			float xz = ax * az;
			float xy = ax * ay;
			float yz = ay * az;

			this.m00 = t * ax * ax + cosTheta;
			this.m01 = t * xy - sinTheta * az;
			this.m02 = t * xz + sinTheta * ay;

			this.m10 = t * xy + sinTheta * az;
			this.m11 = t * ay * ay + cosTheta;
			this.m12 = t * yz - sinTheta * ax;

			this.m20 = t * xz - sinTheta * ay;
			this.m21 = t * yz + sinTheta * ax;
			this.m22 = t * az * az + cosTheta;
		}
		this.m03 = 0.0f;
		this.m13 = 0.0f;
		this.m23 = 0.0f;

		this.m30 = 0.0f;
		this.m31 = 0.0f;
		this.m32 = 0.0f;
		this.m33 = 1.0f;
	}

	/**
	 * Sets the value of this matrix from the rotation expressed
	 * by the quaternion q1, the translation t1, and the scale s.
	 * 
	 * @param q1 the rotation expressed as a quaternion
	 * @param t1 the translation
	 * @param s  the scale value
	 */
	public final void set(Quaternion q1, Vector3f t1, float s) {
		this.m00 = (s * (1.0f - 2.0f * q1.y * q1.y - 2.0f * q1.z * q1.z));
		this.m10 = (s * (2.0f * (q1.x * q1.y + q1.w * q1.z)));
		this.m20 = (s * (2.0f * (q1.x * q1.z - q1.w * q1.y)));

		this.m01 = (s * (2.0f * (q1.x * q1.y - q1.w * q1.z)));
		this.m11 = (s * (1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.z * q1.z));
		this.m21 = (s * (2.0f * (q1.y * q1.z + q1.w * q1.x)));

		this.m02 = (s * (2.0f * (q1.x * q1.z + q1.w * q1.y)));
		this.m12 = (s * (2.0f * (q1.y * q1.z - q1.w * q1.x)));
		this.m22 = (s * (1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.y * q1.y));

		this.m03 = t1.x;
		this.m13 = t1.y;
		this.m23 = t1.z;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Sets the value of this matrix to a copy of the
	 * passed matrix m1.
	 * 
	 * @param m1 the matrix to be copied
	 */
	public final void set(Matrix4f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m03 = m1.m03;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m13 = m1.m13;

		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
		this.m23 = m1.m23;

		this.m30 = m1.m30;
		this.m31 = m1.m31;
		this.m32 = m1.m32;
		this.m33 = m1.m33;
	}

	/**
	 * Sets the value of this matrix to the matrix inverse
	 * of the passed (user declared) matrix m1.
	 * 
	 * @param m1 the matrix to be inverted
	 */
	public final void invert(Matrix4f m1) {
		this.invertGeneral(m1);
	}

	/**
	 * Inverts this matrix in place.
	 */
	public final void invert() {
		this.invertGeneral(this);
	}

	/**
	 * General invert routine. Inverts m1 and places the result in "this".
	 * Note that this routine handles both the "this" version and the
	 * non-"this" version.
	 *
	 * Also note that since this routine is slow anyway, we won't worry
	 * about allocating a little bit of garbage.
	 */
	final void invertGeneral(Matrix4f m1) {
		double temp[] = new double[16];
		double result[] = new double[16];
		int row_perm[] = new int[4];
		int i, r, c;

		// Use LU decomposition and backsubstitution code specifically
		// for floating-point 4x4 matrices.

		// Copy source matrix to t1tmp
		temp[0] = m1.m00;
		temp[1] = m1.m01;
		temp[2] = m1.m02;
		temp[3] = m1.m03;

		temp[4] = m1.m10;
		temp[5] = m1.m11;
		temp[6] = m1.m12;
		temp[7] = m1.m13;

		temp[8] = m1.m20;
		temp[9] = m1.m21;
		temp[10] = m1.m22;
		temp[11] = m1.m23;

		temp[12] = m1.m30;
		temp[13] = m1.m31;
		temp[14] = m1.m32;
		temp[15] = m1.m33;

		// Calculate LU decomposition: Is the matrix singular?
		if (!luDecomposition(temp, row_perm)) {
			// Matrix has no inverse
			try {
				throw new Exception("PROBLEM!!! Matrix4f12");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Perform back substitution on the identity matrix
		for (i = 0; i < 16; i++) {
			result[i] = 0.0;
		}
		result[0] = 1.0;
		result[5] = 1.0;
		result[10] = 1.0;
		result[15] = 1.0;
		luBacksubstitution(temp, row_perm, result);

		this.m00 = (float) result[0];
		this.m01 = (float) result[1];
		this.m02 = (float) result[2];
		this.m03 = (float) result[3];

		this.m10 = (float) result[4];
		this.m11 = (float) result[5];
		this.m12 = (float) result[6];
		this.m13 = (float) result[7];

		this.m20 = (float) result[8];
		this.m21 = (float) result[9];
		this.m22 = (float) result[10];
		this.m23 = (float) result[11];

		this.m30 = (float) result[12];
		this.m31 = (float) result[13];
		this.m32 = (float) result[14];
		this.m33 = (float) result[15];

	}

	/**
	 * Given a 4x4 array "matrix0", this function replaces it with the
	 * LU decomposition of a row-wise permutation of itself. The input
	 * parameters are "matrix0" and "dimen". The array "matrix0" is also
	 * an output parameter. The vector "row_perm[4]" is an output
	 * parameter that contains the row permutations resulting from partial
	 * pivoting. The output parameter "even_row_xchg" is 1 when the
	 * number of row exchanges is even, or -1 otherwise. Assumes data
	 * type is always double.
	 *
	 * This function is similar to luDecomposition, except that it
	 * is tuned specifically for 4x4 matrices.
	 *
	 * @return true if the matrix is nonsingular, or false otherwise.
	 */
	//
	// Reference: Press, Flannery, Teukolsky, Vetterling,
	// _Numerical_Recipes_in_C_, Cambridge University Press,
	// 1988, pp 40-45.
	//
	static boolean luDecomposition(double[] matrix0, int[] row_perm) {

		double row_scale[] = new double[4];

		// Determine implicit scaling information by looping over rows
		{
			int i, j;
			int ptr, rs;
			double big, temp;

			ptr = 0;
			rs = 0;

			// For each row ...
			i = 4;
			while (i-- != 0) {
				big = 0.0;

				// For each column, find the largest element in the row
				j = 4;
				while (j-- != 0) {
					temp = matrix0[ptr++];
					temp = Math.abs(temp);
					if (temp > big) {
						big = temp;
					}
				}

				// Is the matrix singular?
				if (big == 0.0) {
					return false;
				}
				row_scale[rs++] = 1.0 / big;
			}
		}

		{
			int j;
			int mtx;

			mtx = 0;

			// For all columns, execute Crout's method
			for (j = 0; j < 4; j++) {
				int i, imax, k;
				int target, p1, p2;
				double sum, big, temp;

				// Determine elements of upper diagonal matrix U
				for (i = 0; i < j; i++) {
					target = mtx + (4 * i) + j;
					sum = matrix0[target];
					k = i;
					p1 = mtx + (4 * i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						p1++;
						p2 += 4;
					}
					matrix0[target] = sum;
				}

				// Search for largest pivot element and calculate
				// intermediate elements of lower diagonal matrix L.
				big = 0.0;
				imax = -1;
				for (i = j; i < 4; i++) {
					target = mtx + (4 * i) + j;
					sum = matrix0[target];
					k = j;
					p1 = mtx + (4 * i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						p1++;
						p2 += 4;
					}
					matrix0[target] = sum;

					// Is this the best pivot so far?
					if ((temp = row_scale[i] * Math.abs(sum)) >= big) {
						big = temp;
						imax = i;
					}
				}

				if (imax < 0) {
					throw new RuntimeException("PROBLEM!!! Matrix4f");
				}

				// Is a row exchange necessary?
				if (j != imax) {
					// Yes: exchange rows
					k = 4;
					p1 = mtx + (4 * imax);
					p2 = mtx + (4 * j);
					while (k-- != 0) {
						temp = matrix0[p1];
						matrix0[p1++] = matrix0[p2];
						matrix0[p2++] = temp;
					}

					// Record change in scale factor
					row_scale[imax] = row_scale[j];
				}

				// Record row permutation
				row_perm[j] = imax;

				// Is the matrix singular
				if (matrix0[(mtx + (4 * j) + j)] == 0.0) {
					return false;
				}

				// Divide elements of lower diagonal matrix L by pivot
				if (j != (4 - 1)) {
					temp = 1.0 / (matrix0[(mtx + (4 * j) + j)]);
					target = mtx + (4 * (j + 1)) + j;
					i = 3 - j;
					while (i-- != 0) {
						matrix0[target] *= temp;
						target += 4;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Solves a set of linear equations. The input parameters "matrix1",
	 * and "row_perm" come from luDecompostionD4x4 and do not change
	 * here. The parameter "matrix2" is a set of column vectors assembled
	 * into a 4x4 matrix of floating-point values. The procedure takes each
	 * column of "matrix2" in turn and treats it as the right-hand side of the
	 * matrix equation Ax = LUx = b. The solution vector replaces the
	 * original column of the matrix.
	 *
	 * If "matrix2" is the identity matrix, the procedure replaces its contents
	 * with the inverse of the matrix from which "matrix1" was originally
	 * derived.
	 */
	//
	// Reference: Press, Flannery, Teukolsky, Vetterling,
	// _Numerical_Recipes_in_C_, Cambridge University Press,
	// 1988, pp 44-45.
	//
	static void luBacksubstitution(double[] matrix1, int[] row_perm, double[] matrix2) {

		int i, ii, ip, j, k;
		int rp;
		int cv, rv;

		// rp = row_perm;
		rp = 0;

		// For each column vector of matrix2 ...
		for (k = 0; k < 4; k++) {
			// cv = &(matrix2[0][k]);
			cv = k;
			ii = -1;

			// Forward substitution
			for (i = 0; i < 4; i++) {
				double sum;

				ip = row_perm[rp + i];
				sum = matrix2[cv + 4 * ip];
				matrix2[cv + 4 * ip] = matrix2[cv + 4 * i];
				if (ii >= 0) {
					// rv = &(matrix1[i][0]);
					rv = i * 4;
					for (j = ii; j <= i - 1; j++) {
						sum -= matrix1[rv + j] * matrix2[cv + 4 * j];
					}
				} else if (sum != 0.0) {
					ii = i;
				}
				matrix2[cv + 4 * i] = sum;
			}

			// Backsubstitution
			// rv = &(matrix1[3][0]);
			rv = 3 * 4;
			matrix2[cv + 4 * 3] /= matrix1[rv + 3];

			rv -= 4;
			matrix2[cv + 4 * 2] = (matrix2[cv + 4 * 2] - matrix1[rv + 3] * matrix2[cv + 4 * 3]) / matrix1[rv + 2];

			rv -= 4;
			matrix2[cv + 4 * 1] = (matrix2[cv + 4 * 1] - matrix1[rv + 2] * matrix2[cv + 4 * 2] - matrix1[rv + 3] * matrix2[cv + 4 * 3]) / matrix1[rv + 1];

			rv -= 4;
			matrix2[cv + 4 * 0] = (matrix2[cv + 4 * 0] - matrix1[rv + 1] * matrix2[cv + 4 * 1] - matrix1[rv + 2] * matrix2[cv + 4 * 2] - matrix1[rv + 3] * matrix2[cv + 4 * 3]) / matrix1[rv + 0];
		}
	}

	/**
	 * Computes the determinate of this matrix.
	 * 
	 * @return the determinate of the matrix
	 */
	public final float determinant() {
		float det;
		det = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
		det -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
		det += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
		det -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);

		return (det);
	}

	/**
	 * Sets the rotational component (upper 3x3) of this matrix to the
	 * matrix values in the single precision Matrix3f argument; the other
	 * elements of this matrix are initialized as if this were an identity
	 * matrix (i.e., affine matrix with no translational component).
	 * 
	 * @param m1 the single-precision 3x3 matrix
	 */
	public final void set(Matrix3f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m03 = 0.0f;
		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m13 = 0.0f;
		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
		this.m23 = 0.0f;
		this.m30 = 0.0f;
		this.m31 = 0.0f;
		this.m32 = 0.0f;
		this.m33 = 1.0f;
	}

	/**
	 * Sets the value of this matrix to a scale matrix with the
	 * the passed scale amount.
	 * 
	 * @param scale the scale factor for the matrix
	 */
	public final void set(float scale) {
		this.m00 = scale;
		this.m01 = (float) 0.0;
		this.m02 = (float) 0.0;
		this.m03 = (float) 0.0;

		this.m10 = (float) 0.0;
		this.m11 = scale;
		this.m12 = (float) 0.0;
		this.m13 = (float) 0.0;

		this.m20 = (float) 0.0;
		this.m21 = (float) 0.0;
		this.m22 = scale;
		this.m23 = (float) 0.0;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Sets the values in this Matrix4f equal to the row-major
	 * array parameter (ie, the first four elements of the
	 * array will be copied into the first row of this matrix, etc.).
	 * 
	 * @param m the single precision array of length 16
	 */
	public final void set(float[] m) {
		this.m00 = m[0];
		this.m01 = m[1];
		this.m02 = m[2];
		this.m03 = m[3];
		this.m10 = m[4];
		this.m11 = m[5];
		this.m12 = m[6];
		this.m13 = m[7];
		this.m20 = m[8];
		this.m21 = m[9];
		this.m22 = m[10];
		this.m23 = m[11];
		this.m30 = m[12];
		this.m31 = m[13];
		this.m32 = m[14];
		this.m33 = m[15];
	}

	/**
	 * Sets the value of this matrix to a translate matrix with
	 * the passed translation value.
	 * 
	 * @param v1 the translation amount
	 */
	public final void set(Vector3f v1) {
		this.m00 = (float) 1.0;
		this.m01 = (float) 0.0;
		this.m02 = (float) 0.0;
		this.m03 = v1.x;

		this.m10 = (float) 0.0;
		this.m11 = (float) 1.0;
		this.m12 = (float) 0.0;
		this.m13 = v1.y;

		this.m20 = (float) 0.0;
		this.m21 = (float) 0.0;
		this.m22 = (float) 1.0;
		this.m23 = v1.z;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Sets the value of this transform to a scale and translation matrix;
	 * the scale is not applied to the translation and all of the matrix
	 * values are modified.
	 * 
	 * @param scale the scale factor for the matrix
	 * @param t1    the translation amount
	 */
	public final void set(float scale, Vector3f t1) {
		this.m00 = scale;
		this.m01 = (float) 0.0;
		this.m02 = (float) 0.0;
		this.m03 = t1.x;

		this.m10 = (float) 0.0;
		this.m11 = scale;
		this.m12 = (float) 0.0;
		this.m13 = t1.y;

		this.m20 = (float) 0.0;
		this.m21 = (float) 0.0;
		this.m22 = scale;
		this.m23 = t1.z;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Sets the value of this transform to a scale and translation matrix;
	 * the translation is scaled by the scale factor and all of the matrix
	 * values are modified.
	 * 
	 * @param t1    the translation amount
	 * @param scale the scale factor for the matrix
	 */
	public final void set(Vector3f t1, float scale) {
		this.m00 = scale;
		this.m01 = (float) 0.0;
		this.m02 = (float) 0.0;
		this.m03 = scale * t1.x;

		this.m10 = (float) 0.0;
		this.m11 = scale;
		this.m12 = (float) 0.0;
		this.m13 = scale * t1.y;

		this.m20 = (float) 0.0;
		this.m21 = (float) 0.0;
		this.m22 = scale;
		this.m23 = scale * t1.z;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Sets the value of this matrix from the rotation expressed by
	 * the rotation matrix m1, the translation t1, and the scale factor.
	 * The translation is not modified by the scale.
	 * 
	 * @param m1    the rotation component
	 * @param t1    the translation component
	 * @param scale the scale component
	 */
	public final void set(Matrix3f m1, Vector3f t1, float scale) {
		this.m00 = m1.m00 * scale;
		this.m01 = m1.m01 * scale;
		this.m02 = m1.m02 * scale;
		this.m03 = t1.x;

		this.m10 = m1.m10 * scale;
		this.m11 = m1.m11 * scale;
		this.m12 = m1.m12 * scale;
		this.m13 = t1.y;

		this.m20 = m1.m20 * scale;
		this.m21 = m1.m21 * scale;
		this.m22 = m1.m22 * scale;
		this.m23 = t1.z;

		this.m30 = 0.0f;
		this.m31 = 0.0f;
		this.m32 = 0.0f;
		this.m33 = 1.0f;
	}

	/**
	 * Modifies the translational components of this matrix to the values
	 * of the Vector3f argument; the other values of this matrix are not
	 * modified.
	 * 
	 * @param trans the translational component
	 */
	public final void setTranslation(Vector3f trans) {
		this.m03 = trans.x;
		this.m13 = trans.y;
		this.m23 = trans.z;
	}

	/**
	 * Sets the value of this matrix to a counter clockwise rotation
	 * about the x axis.
	 * 
	 * @param angle the angle to rotate about the X axis in radians
	 */
	public final void rotX(float angle) {
		float sinAngle, cosAngle;

		sinAngle = (float) Math.sin((double) angle);
		cosAngle = (float) Math.cos((double) angle);

		this.m00 = (float) 1.0;
		this.m01 = (float) 0.0;
		this.m02 = (float) 0.0;
		this.m03 = (float) 0.0;

		this.m10 = (float) 0.0;
		this.m11 = cosAngle;
		this.m12 = -sinAngle;
		this.m13 = (float) 0.0;

		this.m20 = (float) 0.0;
		this.m21 = sinAngle;
		this.m22 = cosAngle;
		this.m23 = (float) 0.0;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Sets the value of this matrix to a counter clockwise rotation
	 * about the y axis.
	 * 
	 * @param angle the angle to rotate about the Y axis in radians
	 */
	public final void rotY(float angle) {
		float sinAngle, cosAngle;

		sinAngle = (float) Math.sin((double) angle);
		cosAngle = (float) Math.cos((double) angle);

		this.m00 = cosAngle;
		this.m01 = (float) 0.0;
		this.m02 = sinAngle;
		this.m03 = (float) 0.0;

		this.m10 = (float) 0.0;
		this.m11 = (float) 1.0;
		this.m12 = (float) 0.0;
		this.m13 = (float) 0.0;

		this.m20 = -sinAngle;
		this.m21 = (float) 0.0;
		this.m22 = cosAngle;
		this.m23 = (float) 0.0;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Sets the value of this matrix to a counter clockwise rotation
	 * about the z axis.
	 * 
	 * @param angle the angle to rotate about the Z axis in radians
	 */
	public final void rotZ(float angle) {
		float sinAngle, cosAngle;

		sinAngle = (float) Math.sin((double) angle);
		cosAngle = (float) Math.cos((double) angle);

		this.m00 = cosAngle;
		this.m01 = -sinAngle;
		this.m02 = (float) 0.0;
		this.m03 = (float) 0.0;

		this.m10 = sinAngle;
		this.m11 = cosAngle;
		this.m12 = (float) 0.0;
		this.m13 = (float) 0.0;

		this.m20 = (float) 0.0;
		this.m21 = (float) 0.0;
		this.m22 = (float) 1.0;
		this.m23 = (float) 0.0;

		this.m30 = (float) 0.0;
		this.m31 = (float) 0.0;
		this.m32 = (float) 0.0;
		this.m33 = (float) 1.0;
	}

	/**
	 * Multiplies each element of this matrix by a scalar.
	 * 
	 * @param scalar the scalar multiplier.
	 */
	public final void mul(float scalar) {
		this.m00 *= scalar;
		this.m01 *= scalar;
		this.m02 *= scalar;
		this.m03 *= scalar;
		this.m10 *= scalar;
		this.m11 *= scalar;
		this.m12 *= scalar;
		this.m13 *= scalar;
		this.m20 *= scalar;
		this.m21 *= scalar;
		this.m22 *= scalar;
		this.m23 *= scalar;
		this.m30 *= scalar;
		this.m31 *= scalar;
		this.m32 *= scalar;
		this.m33 *= scalar;
	}

	/**
	 * Multiplies each element of matrix m1 by a scalar and places
	 * the result into this. Matrix m1 is not modified.
	 * 
	 * @param scalar the scalar multiplier.
	 * @param m1     the original matrix.
	 */
	public final void mul(float scalar, Matrix4f m1) {
		this.m00 = m1.m00 * scalar;
		this.m01 = m1.m01 * scalar;
		this.m02 = m1.m02 * scalar;
		this.m03 = m1.m03 * scalar;
		this.m10 = m1.m10 * scalar;
		this.m11 = m1.m11 * scalar;
		this.m12 = m1.m12 * scalar;
		this.m13 = m1.m13 * scalar;
		this.m20 = m1.m20 * scalar;
		this.m21 = m1.m21 * scalar;
		this.m22 = m1.m22 * scalar;
		this.m23 = m1.m23 * scalar;
		this.m30 = m1.m30 * scalar;
		this.m31 = m1.m31 * scalar;
		this.m32 = m1.m32 * scalar;
		this.m33 = m1.m33 * scalar;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying itself
	 * with matrix m1.
	 * 
	 * @param m1 the other matrix
	 */
	public final void mul(Matrix4f m1) {
		float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33; // vars for temp result matrix

		m00 = this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20 + this.m03 * m1.m30;
		m01 = this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21 + this.m03 * m1.m31;
		m02 = this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22 + this.m03 * m1.m32;
		m03 = this.m00 * m1.m03 + this.m01 * m1.m13 + this.m02 * m1.m23 + this.m03 * m1.m33;

		m10 = this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20 + this.m13 * m1.m30;
		m11 = this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21 + this.m13 * m1.m31;
		m12 = this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22 + this.m13 * m1.m32;
		m13 = this.m10 * m1.m03 + this.m11 * m1.m13 + this.m12 * m1.m23 + this.m13 * m1.m33;

		m20 = this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20 + this.m23 * m1.m30;
		m21 = this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21 + this.m23 * m1.m31;
		m22 = this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22 + this.m23 * m1.m32;
		m23 = this.m20 * m1.m03 + this.m21 * m1.m13 + this.m22 * m1.m23 + this.m23 * m1.m33;

		m30 = this.m30 * m1.m00 + this.m31 * m1.m10 + this.m32 * m1.m20 + this.m33 * m1.m30;
		m31 = this.m30 * m1.m01 + this.m31 * m1.m11 + this.m32 * m1.m21 + this.m33 * m1.m31;
		m32 = this.m30 * m1.m02 + this.m31 * m1.m12 + this.m32 * m1.m22 + this.m33 * m1.m32;
		m33 = this.m30 * m1.m03 + this.m31 * m1.m13 + this.m32 * m1.m23 + this.m33 * m1.m33;

		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying
	 * the two argument matrices together.
	 * 
	 * @param m1 the first matrix
	 * @param m2 the second matrix
	 */
	public final void mul(Matrix4f m1, Matrix4f m2) {
		if (this != m1 && this != m2) {

			this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30;
			this.m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31;
			this.m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32;
			this.m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33;

			this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30;
			this.m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31;
			this.m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32;
			this.m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33;

			this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30;
			this.m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31;
			this.m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32;
			this.m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33;

			this.m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30;
			this.m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31;
			this.m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32;
			this.m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33;
		} else {
			float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33; // vars for temp result matrix
			m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30;
			m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31;
			m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32;
			m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33;

			m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30;
			m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31;
			m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32;
			m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33;

			m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30;
			m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31;
			m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32;
			m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33;

			m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30;
			m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31;
			m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32;
			m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33;

			this.m00 = m00;
			this.m01 = m01;
			this.m02 = m02;
			this.m03 = m03;
			this.m10 = m10;
			this.m11 = m11;
			this.m12 = m12;
			this.m13 = m13;
			this.m20 = m20;
			this.m21 = m21;
			this.m22 = m22;
			this.m23 = m23;
			this.m30 = m30;
			this.m31 = m31;
			this.m32 = m32;
			this.m33 = m33;
		}
	}

	/**
	 * Multiplies the transpose of matrix m1 times the transpose of matrix
	 * m2, and places the result into this.
	 * 
	 * @param m1 the matrix on the left hand side of the multiplication
	 * @param m2 the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeBoth(Matrix4f m1, Matrix4f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02 + m1.m30 * m2.m03;
			this.m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12 + m1.m30 * m2.m13;
			this.m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22 + m1.m30 * m2.m23;
			this.m03 = m1.m00 * m2.m30 + m1.m10 * m2.m31 + m1.m20 * m2.m32 + m1.m30 * m2.m33;

			this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02 + m1.m31 * m2.m03;
			this.m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12 + m1.m31 * m2.m13;
			this.m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22 + m1.m31 * m2.m23;
			this.m13 = m1.m01 * m2.m30 + m1.m11 * m2.m31 + m1.m21 * m2.m32 + m1.m31 * m2.m33;

			this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02 + m1.m32 * m2.m03;
			this.m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12 + m1.m32 * m2.m13;
			this.m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22 + m1.m32 * m2.m23;
			this.m23 = m1.m02 * m2.m30 + m1.m12 * m2.m31 + m1.m22 * m2.m32 + m1.m32 * m2.m33;

			this.m30 = m1.m03 * m2.m00 + m1.m13 * m2.m01 + m1.m23 * m2.m02 + m1.m33 * m2.m03;
			this.m31 = m1.m03 * m2.m10 + m1.m13 * m2.m11 + m1.m23 * m2.m12 + m1.m33 * m2.m13;
			this.m32 = m1.m03 * m2.m20 + m1.m13 * m2.m21 + m1.m23 * m2.m22 + m1.m33 * m2.m23;
			this.m33 = m1.m03 * m2.m30 + m1.m13 * m2.m31 + m1.m23 * m2.m32 + m1.m33 * m2.m33;
		} else {
			float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, // vars for temp result matrix
					m30, m31, m32, m33;

			m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02 + m1.m30 * m2.m03;
			m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12 + m1.m30 * m2.m13;
			m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22 + m1.m30 * m2.m23;
			m03 = m1.m00 * m2.m30 + m1.m10 * m2.m31 + m1.m20 * m2.m32 + m1.m30 * m2.m33;

			m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02 + m1.m31 * m2.m03;
			m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12 + m1.m31 * m2.m13;
			m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22 + m1.m31 * m2.m23;
			m13 = m1.m01 * m2.m30 + m1.m11 * m2.m31 + m1.m21 * m2.m32 + m1.m31 * m2.m33;

			m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02 + m1.m32 * m2.m03;
			m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12 + m1.m32 * m2.m13;
			m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22 + m1.m32 * m2.m23;
			m23 = m1.m02 * m2.m30 + m1.m12 * m2.m31 + m1.m22 * m2.m32 + m1.m32 * m2.m33;

			m30 = m1.m03 * m2.m00 + m1.m13 * m2.m01 + m1.m23 * m2.m02 + m1.m33 * m2.m03;
			m31 = m1.m03 * m2.m10 + m1.m13 * m2.m11 + m1.m23 * m2.m12 + m1.m33 * m2.m13;
			m32 = m1.m03 * m2.m20 + m1.m13 * m2.m21 + m1.m23 * m2.m22 + m1.m33 * m2.m23;
			m33 = m1.m03 * m2.m30 + m1.m13 * m2.m31 + m1.m23 * m2.m32 + m1.m33 * m2.m33;

			this.m00 = m00;
			this.m01 = m01;
			this.m02 = m02;
			this.m03 = m03;
			this.m10 = m10;
			this.m11 = m11;
			this.m12 = m12;
			this.m13 = m13;
			this.m20 = m20;
			this.m21 = m21;
			this.m22 = m22;
			this.m23 = m23;
			this.m30 = m30;
			this.m31 = m31;
			this.m32 = m32;
			this.m33 = m33;
		}

	}

	/**
	 * Multiplies matrix m1 times the transpose of matrix m2, and
	 * places the result into this.
	 * 
	 * @param m1 the matrix on the left hand side of the multiplication
	 * @param m2 the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeRight(Matrix4f m1, Matrix4f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02 + m1.m03 * m2.m03;
			this.m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12 + m1.m03 * m2.m13;
			this.m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22 + m1.m03 * m2.m23;
			this.m03 = m1.m00 * m2.m30 + m1.m01 * m2.m31 + m1.m02 * m2.m32 + m1.m03 * m2.m33;

			this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02 + m1.m13 * m2.m03;
			this.m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12 + m1.m13 * m2.m13;
			this.m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22 + m1.m13 * m2.m23;
			this.m13 = m1.m10 * m2.m30 + m1.m11 * m2.m31 + m1.m12 * m2.m32 + m1.m13 * m2.m33;

			this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02 + m1.m23 * m2.m03;
			this.m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12 + m1.m23 * m2.m13;
			this.m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22 + m1.m23 * m2.m23;
			this.m23 = m1.m20 * m2.m30 + m1.m21 * m2.m31 + m1.m22 * m2.m32 + m1.m23 * m2.m33;

			this.m30 = m1.m30 * m2.m00 + m1.m31 * m2.m01 + m1.m32 * m2.m02 + m1.m33 * m2.m03;
			this.m31 = m1.m30 * m2.m10 + m1.m31 * m2.m11 + m1.m32 * m2.m12 + m1.m33 * m2.m13;
			this.m32 = m1.m30 * m2.m20 + m1.m31 * m2.m21 + m1.m32 * m2.m22 + m1.m33 * m2.m23;
			this.m33 = m1.m30 * m2.m30 + m1.m31 * m2.m31 + m1.m32 * m2.m32 + m1.m33 * m2.m33;
		} else {
			float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, // vars for temp result matrix
					m30, m31, m32, m33;

			m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02 + m1.m03 * m2.m03;
			m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12 + m1.m03 * m2.m13;
			m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22 + m1.m03 * m2.m23;
			m03 = m1.m00 * m2.m30 + m1.m01 * m2.m31 + m1.m02 * m2.m32 + m1.m03 * m2.m33;

			m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02 + m1.m13 * m2.m03;
			m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12 + m1.m13 * m2.m13;
			m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22 + m1.m13 * m2.m23;
			m13 = m1.m10 * m2.m30 + m1.m11 * m2.m31 + m1.m12 * m2.m32 + m1.m13 * m2.m33;

			m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02 + m1.m23 * m2.m03;
			m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12 + m1.m23 * m2.m13;
			m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22 + m1.m23 * m2.m23;
			m23 = m1.m20 * m2.m30 + m1.m21 * m2.m31 + m1.m22 * m2.m32 + m1.m23 * m2.m33;

			m30 = m1.m30 * m2.m00 + m1.m31 * m2.m01 + m1.m32 * m2.m02 + m1.m33 * m2.m03;
			m31 = m1.m30 * m2.m10 + m1.m31 * m2.m11 + m1.m32 * m2.m12 + m1.m33 * m2.m13;
			m32 = m1.m30 * m2.m20 + m1.m31 * m2.m21 + m1.m32 * m2.m22 + m1.m33 * m2.m23;
			m33 = m1.m30 * m2.m30 + m1.m31 * m2.m31 + m1.m32 * m2.m32 + m1.m33 * m2.m33;

			this.m00 = m00;
			this.m01 = m01;
			this.m02 = m02;
			this.m03 = m03;
			this.m10 = m10;
			this.m11 = m11;
			this.m12 = m12;
			this.m13 = m13;
			this.m20 = m20;
			this.m21 = m21;
			this.m22 = m22;
			this.m23 = m23;
			this.m30 = m30;
			this.m31 = m31;
			this.m32 = m32;
			this.m33 = m33;
		}

	}

	/**
	 * Multiplies the transpose of matrix m1 times matrix m2, and
	 * places the result into this.
	 * 
	 * @param m1 the matrix on the left hand side of the multiplication
	 * @param m2 the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeLeft(Matrix4f m1, Matrix4f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20 + m1.m30 * m2.m30;
			this.m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21 + m1.m30 * m2.m31;
			this.m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22 + m1.m30 * m2.m32;
			this.m03 = m1.m00 * m2.m03 + m1.m10 * m2.m13 + m1.m20 * m2.m23 + m1.m30 * m2.m33;

			this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20 + m1.m31 * m2.m30;
			this.m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21 + m1.m31 * m2.m31;
			this.m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22 + m1.m31 * m2.m32;
			this.m13 = m1.m01 * m2.m03 + m1.m11 * m2.m13 + m1.m21 * m2.m23 + m1.m31 * m2.m33;

			this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20 + m1.m32 * m2.m30;
			this.m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21 + m1.m32 * m2.m31;
			this.m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22 + m1.m32 * m2.m32;
			this.m23 = m1.m02 * m2.m03 + m1.m12 * m2.m13 + m1.m22 * m2.m23 + m1.m32 * m2.m33;

			this.m30 = m1.m03 * m2.m00 + m1.m13 * m2.m10 + m1.m23 * m2.m20 + m1.m33 * m2.m30;
			this.m31 = m1.m03 * m2.m01 + m1.m13 * m2.m11 + m1.m23 * m2.m21 + m1.m33 * m2.m31;
			this.m32 = m1.m03 * m2.m02 + m1.m13 * m2.m12 + m1.m23 * m2.m22 + m1.m33 * m2.m32;
			this.m33 = m1.m03 * m2.m03 + m1.m13 * m2.m13 + m1.m23 * m2.m23 + m1.m33 * m2.m33;
		} else {
			float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, // vars for temp result matrix
					m30, m31, m32, m33;

			m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20 + m1.m30 * m2.m30;
			m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21 + m1.m30 * m2.m31;
			m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22 + m1.m30 * m2.m32;
			m03 = m1.m00 * m2.m03 + m1.m10 * m2.m13 + m1.m20 * m2.m23 + m1.m30 * m2.m33;

			m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20 + m1.m31 * m2.m30;
			m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21 + m1.m31 * m2.m31;
			m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22 + m1.m31 * m2.m32;
			m13 = m1.m01 * m2.m03 + m1.m11 * m2.m13 + m1.m21 * m2.m23 + m1.m31 * m2.m33;

			m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20 + m1.m32 * m2.m30;
			m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21 + m1.m32 * m2.m31;
			m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22 + m1.m32 * m2.m32;
			m23 = m1.m02 * m2.m03 + m1.m12 * m2.m13 + m1.m22 * m2.m23 + m1.m32 * m2.m33;

			m30 = m1.m03 * m2.m00 + m1.m13 * m2.m10 + m1.m23 * m2.m20 + m1.m33 * m2.m30;
			m31 = m1.m03 * m2.m01 + m1.m13 * m2.m11 + m1.m23 * m2.m21 + m1.m33 * m2.m31;
			m32 = m1.m03 * m2.m02 + m1.m13 * m2.m12 + m1.m23 * m2.m22 + m1.m33 * m2.m32;
			m33 = m1.m03 * m2.m03 + m1.m13 * m2.m13 + m1.m23 * m2.m23 + m1.m33 * m2.m33;

			this.m00 = m00;
			this.m01 = m01;
			this.m02 = m02;
			this.m03 = m03;
			this.m10 = m10;
			this.m11 = m11;
			this.m12 = m12;
			this.m13 = m13;
			this.m20 = m20;
			this.m21 = m21;
			this.m22 = m22;
			this.m23 = m23;
			this.m30 = m30;
			this.m31 = m31;
			this.m32 = m32;
			this.m33 = m33;
		}

	}

	/**
	 * Returns true if all of the data members of Matrix4f m1 are
	 * equal to the corresponding data members in this Matrix4f.
	 * 
	 * @param m1 the matrix with which the comparison is made.
	 * @return true or false
	 */
	public boolean equals(Matrix4f m1) {
		try {
			return (this.m00 == m1.m00 && this.m01 == m1.m01 && this.m02 == m1.m02 && this.m03 == m1.m03 && this.m10 == m1.m10 && this.m11 == m1.m11 && this.m12 == m1.m12 && this.m13 == m1.m13 && this.m20 == m1.m20 && this.m21 == m1.m21
					&& this.m22 == m1.m22 && this.m23 == m1.m23 && this.m30 == m1.m30 && this.m31 == m1.m31 && this.m32 == m1.m32 && this.m33 == m1.m33);
		} catch (NullPointerException e) {
			return false;
		}

	}

	/**
	 * Returns true if the Object t1 is of type Matrix4f and all of the
	 * data members of t1 are equal to the corresponding data members in
	 * this Matrix4f.
	 * 
	 * @param t1 the matrix with which the comparison is made.
	 * @return true or false
	 */
	@Override
	public boolean equals(Object t1) {
		try {
			Matrix4f m2 = (Matrix4f) t1;
			return (this.m00 == m2.m00 && this.m01 == m2.m01 && this.m02 == m2.m02 && this.m03 == m2.m03 && this.m10 == m2.m10 && this.m11 == m2.m11 && this.m12 == m2.m12 && this.m13 == m2.m13 && this.m20 == m2.m20 && this.m21 == m2.m21
					&& this.m22 == m2.m22 && this.m23 == m2.m23 && this.m30 == m2.m30 && this.m31 == m2.m31 && this.m32 == m2.m32 && this.m33 == m2.m33);
		} catch (ClassCastException e1) {
			return false;
		} catch (NullPointerException e2) {
			return false;
		}
	}

	/**
	 * Returns true if the L-infinite distance between this matrix
	 * and matrix m1 is less than or equal to the epsilon parameter,
	 * otherwise returns false. The L-infinite
	 * distance is equal to
	 * MAX[i=0,1,2,3 ; j=0,1,2,3 ; abs(this.m(i,j) - m1.m(i,j)]
	 * 
	 * @param m1      the matrix to be compared to this matrix
	 * @param epsilon the threshold value
	 */
	public boolean epsilonEquals(Matrix4f m1, float epsilon) {

		boolean status = true;

		if (Math.abs(this.m00 - m1.m00) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m01 - m1.m01) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m02 - m1.m02) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m03 - m1.m03) > epsilon) {
			status = false;
		}

		if (Math.abs(this.m10 - m1.m10) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m11 - m1.m11) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m12 - m1.m12) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m13 - m1.m13) > epsilon) {
			status = false;
		}

		if (Math.abs(this.m20 - m1.m20) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m21 - m1.m21) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m22 - m1.m22) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m23 - m1.m23) > epsilon) {
			status = false;
		}

		if (Math.abs(this.m30 - m1.m30) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m31 - m1.m31) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m32 - m1.m32) > epsilon) {
			status = false;
		}
		if (Math.abs(this.m33 - m1.m33) > epsilon) {
			status = false;
		}

		return (status);

	}

	/**
	 * Transforms the normal parameter by this Matrix4f and places the value
	 * into normalOut. The fourth element of the normal is assumed to be zero.
	 * 
	 * @param normal    the input normal to be transformed.
	 * @param normalOut the transformed normal
	 */
	public final void transform(Vector3f normal, Vector3f normalOut) {
		float x, y;
		x = this.m00 * normal.x + this.m01 * normal.y + this.m02 * normal.z;
		y = this.m10 * normal.x + this.m11 * normal.y + this.m12 * normal.z;
		normalOut.z = this.m20 * normal.x + this.m21 * normal.y + this.m22 * normal.z;
		normalOut.x = x;
		normalOut.y = y;
	}

	/**
	 * Transforms the normal parameter by this transform and places the value
	 * back into normal. The fourth element of the normal is assumed to be zero.
	 * 
	 * @param normal the input normal to be transformed.
	 */
	public final void transform(Vector3f normal) {
		float x, y;

		x = this.m00 * normal.x + this.m01 * normal.y + this.m02 * normal.z;
		y = this.m10 * normal.x + this.m11 * normal.y + this.m12 * normal.z;
		normal.z = this.m20 * normal.x + this.m21 * normal.y + this.m22 * normal.z;
		normal.x = x;
		normal.y = y;
	}

	/**
	 * Sets the rotational component (upper 3x3) of this matrix to the
	 * matrix values in the single precision Matrix3f argument; the other
	 * elements of this matrix are unchanged; a singular value
	 * decomposition is performed on this object's upper 3x3 matrix to
	 * factor out the scale, then this object's upper 3x3 matrix components
	 * are replaced by the passed rotation components,
	 * and then the scale is reapplied to the rotational components.
	 * 
	 * @param m1 single precision 3x3 matrix
	 */
	public final void setRotation(Matrix3f m1) {
		double[] tmp_rot = new double[9]; // scratch matrix
		double[] tmp_scale = new double[3]; // scratch matrix

		this.getScaleRotate(tmp_scale, tmp_rot);

		this.m00 = (float) (m1.m00 * tmp_scale[0]);
		this.m01 = (float) (m1.m01 * tmp_scale[1]);
		this.m02 = (float) (m1.m02 * tmp_scale[2]);

		this.m10 = (float) (m1.m10 * tmp_scale[0]);
		this.m11 = (float) (m1.m11 * tmp_scale[1]);
		this.m12 = (float) (m1.m12 * tmp_scale[2]);

		this.m20 = (float) (m1.m20 * tmp_scale[0]);
		this.m21 = (float) (m1.m21 * tmp_scale[1]);
		this.m22 = (float) (m1.m22 * tmp_scale[2]);
	}

	/**
	 * Sets the rotational component (upper 3x3) of this matrix to the
	 * matrix equivalent values of the quaternion argument; the other
	 * elements of this matrix are unchanged; a singular value
	 * decomposition is performed on this object's upper 3x3 matrix to
	 * factor out the scale, then this object's upper 3x3 matrix components
	 * are replaced by the matrix equivalent of the quaternion,
	 * and then the scale is reapplied to the rotational components.
	 * 
	 * @param q1 the quaternion that specifies the rotation
	 */
	public final void setRotation(Quaternion q1) {
		double[] tmp_rot = new double[9]; // scratch matrix
		double[] tmp_scale = new double[3]; // scratch matrix
		this.getScaleRotate(tmp_scale, tmp_rot);

		this.m00 = (float) ((1.0f - 2.0f * q1.y * q1.y - 2.0f * q1.z * q1.z) * tmp_scale[0]);
		this.m10 = (float) ((2.0f * (q1.x * q1.y + q1.w * q1.z)) * tmp_scale[0]);
		this.m20 = (float) ((2.0f * (q1.x * q1.z - q1.w * q1.y)) * tmp_scale[0]);

		this.m01 = (float) ((2.0f * (q1.x * q1.y - q1.w * q1.z)) * tmp_scale[1]);
		this.m11 = (float) ((1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.z * q1.z) * tmp_scale[1]);
		this.m21 = (float) ((2.0f * (q1.y * q1.z + q1.w * q1.x)) * tmp_scale[1]);

		this.m02 = (float) ((2.0f * (q1.x * q1.z + q1.w * q1.y)) * tmp_scale[2]);
		this.m12 = (float) ((2.0f * (q1.y * q1.z - q1.w * q1.x)) * tmp_scale[2]);
		this.m22 = (float) ((1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.y * q1.y) * tmp_scale[2]);
	}

	/**
	 * Sets the rotational component (upper 3x3) of this matrix to the
	 * matrix equivalent values of the axis-angle argument; the other
	 * elements of this matrix are unchanged; a singular value
	 * decomposition is performed on this object's upper 3x3 matrix to
	 * factor out the scale, then this object's upper 3x3 matrix components
	 * are replaced by the matrix equivalent of the axis-angle,
	 * and then the scale is reapplied to the rotational components.
	 * 
	 * @param a1 the axis-angle to be converted (x, y, z, angle)
	 */
	public final void setRotation(AxisAngle4f a1) {
		double[] tmp_rot = new double[9]; // scratch matrix
		double[] tmp_scale = new double[3]; // scratch matrix

		this.getScaleRotate(tmp_scale, tmp_rot);

		double mag = Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);
		if (mag < EPS) {
			this.m00 = 1.0f;
			this.m01 = 0.0f;
			this.m02 = 0.0f;

			this.m10 = 0.0f;
			this.m11 = 1.0f;
			this.m12 = 0.0f;

			this.m20 = 0.0f;
			this.m21 = 0.0f;
			this.m22 = 1.0f;
		} else {
			mag = 1.0 / mag;
			double ax = a1.x * mag;
			double ay = a1.y * mag;
			double az = a1.z * mag;

			double sinTheta = Math.sin(a1.angle);
			double cosTheta = Math.cos(a1.angle);
			double t = 1.0 - cosTheta;

			double xz = a1.x * a1.z;
			double xy = a1.x * a1.y;
			double yz = a1.y * a1.z;

			this.m00 = (float) ((t * ax * ax + cosTheta) * tmp_scale[0]);
			this.m01 = (float) ((t * xy - sinTheta * az) * tmp_scale[1]);
			this.m02 = (float) ((t * xz + sinTheta * ay) * tmp_scale[2]);

			this.m10 = (float) ((t * xy + sinTheta * az) * tmp_scale[0]);
			this.m11 = (float) ((t * ay * ay + cosTheta) * tmp_scale[1]);
			this.m12 = (float) ((t * yz - sinTheta * ax) * tmp_scale[2]);

			this.m20 = (float) ((t * xz - sinTheta * ay) * tmp_scale[0]);
			this.m21 = (float) ((t * yz + sinTheta * ax) * tmp_scale[1]);
			this.m22 = (float) ((t * az * az + cosTheta) * tmp_scale[2]);
		}
	}

	/**
	 * Sets this matrix to all zeros.
	 */
	public final void setZero() {
		this.m00 = 0.0f;
		this.m01 = 0.0f;
		this.m02 = 0.0f;
		this.m03 = 0.0f;
		this.m10 = 0.0f;
		this.m11 = 0.0f;
		this.m12 = 0.0f;
		this.m13 = 0.0f;
		this.m20 = 0.0f;
		this.m21 = 0.0f;
		this.m22 = 0.0f;
		this.m23 = 0.0f;
		this.m30 = 0.0f;
		this.m31 = 0.0f;
		this.m32 = 0.0f;
		this.m33 = 0.0f;
	}

	/**
	 * Negates the value of this matrix: this = -this.
	 */
	public final void negate() {
		this.m00 = -this.m00;
		this.m01 = -this.m01;
		this.m02 = -this.m02;
		this.m03 = -this.m03;
		this.m10 = -this.m10;
		this.m11 = -this.m11;
		this.m12 = -this.m12;
		this.m13 = -this.m13;
		this.m20 = -this.m20;
		this.m21 = -this.m21;
		this.m22 = -this.m22;
		this.m23 = -this.m23;
		this.m30 = -this.m30;
		this.m31 = -this.m31;
		this.m32 = -this.m32;
		this.m33 = -this.m33;
	}

	/**
	 * Sets the value of this matrix equal to the negation of
	 * of the Matrix4f parameter.
	 * 
	 * @param m1 the source matrix
	 */
	public final void negate(Matrix4f m1) {
		this.m00 = -m1.m00;
		this.m01 = -m1.m01;
		this.m02 = -m1.m02;
		this.m03 = -m1.m03;
		this.m10 = -m1.m10;
		this.m11 = -m1.m11;
		this.m12 = -m1.m12;
		this.m13 = -m1.m13;
		this.m20 = -m1.m20;
		this.m21 = -m1.m21;
		this.m22 = -m1.m22;
		this.m23 = -m1.m23;
		this.m30 = -m1.m30;
		this.m31 = -m1.m31;
		this.m32 = -m1.m32;
		this.m33 = -m1.m33;
	}

	private final void getScaleRotate(double scales[], double rots[]) {

		double[] tmp = new double[9]; // scratch matrix
		tmp[0] = this.m00;
		tmp[1] = this.m01;
		tmp[2] = this.m02;

		tmp[3] = this.m10;
		tmp[4] = this.m11;
		tmp[5] = this.m12;

		tmp[6] = this.m20;
		tmp[7] = this.m21;
		tmp[8] = this.m22;

		Matrix3d.compute_svd(tmp, scales, rots);

		return;
	}

	/**
	 * Creates a new object of the same class as this object.
	 *
	 * @return a clone of this instance.
	 * @exception OutOfMemoryError if there is not enough memory.
	 * @see java.lang.Cloneable
	 */
	@Override
	public Object clone() {
		Matrix4f m1 = null;
		try {
			m1 = (Matrix4f) super.clone();
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}

		return m1;
	}

	/**
	 * Get the first matrix element in the first row.
	 * 
	 * @return Returns the m00.
	 */
	public final float getM00() {
		return this.m00;
	}

	/**
	 * Set the first matrix element in the first row.
	 * 
	 * @param m00 The m00 to set.
	 */
	public final void setM00(float m00) {
		this.m00 = m00;
	}

	/**
	 * Get the second matrix element in the first row.
	 * 
	 * @return Returns the m01.
	 */
	public final float getM01() {
		return this.m01;
	}

	/**
	 * Set the second matrix element in the first row.
	 * 
	 * @param m01 The m01 to set.
	 */
	public final void setM01(float m01) {
		this.m01 = m01;
	}

	/**
	 * Get the third matrix element in the first row.
	 * 
	 * @return Returns the m02.
	 */
	public final float getM02() {
		return this.m02;
	}

	/**
	 * Set the third matrix element in the first row.
	 * 
	 * @param m02 The m02 to set.
	 */
	public final void setM02(float m02) {
		this.m02 = m02;
	}

	/**
	 * Get first matrix element in the second row.
	 * 
	 * @return Returns the m10.
	 */
	public final float getM10() {
		return this.m10;
	}

	/**
	 * Set first matrix element in the second row.
	 * 
	 * @param m10 The m10 to set.
	 */
	public final void setM10(float m10) {
		this.m10 = m10;
	}

	/**
	 * Get second matrix element in the second row.
	 * 
	 * @return Returns the m11.
	 */
	public final float getM11() {
		return this.m11;
	}

	/**
	 * Set the second matrix element in the second row.
	 * 
	 * @param m11 The m11 to set.
	 */
	public final void setM11(float m11) {
		this.m11 = m11;
	}

	/**
	 * Get the third matrix element in the second row.
	 * 
	 * @return Returns the m12.
	 */
	public final float getM12() {
		return this.m12;
	}

	/**
	 * Set the third matrix element in the second row.
	 * 
	 * @param m12 The m12 to set.
	 */
	public final void setM12(float m12) {
		this.m12 = m12;
	}

	/**
	 * Get the first matrix element in the third row.
	 * 
	 * @return Returns the m20.
	 */
	public final float getM20() {
		return this.m20;
	}

	/**
	 * Set the first matrix element in the third row.
	 * 
	 * @param m20 The m20 to set.
	 */
	public final void setM20(float m20) {
		this.m20 = m20;
	}

	/**
	 * Get the second matrix element in the third row.
	 * 
	 * @return Returns the m21.
	 */
	public final float getM21() {
		return this.m21;
	}

	/**
	 * Set the second matrix element in the third row.
	 * 
	 * @param m21 The m21 to set.
	 */
	public final void setM21(float m21) {
		this.m21 = m21;
	}

	/**
	 * Get the third matrix element in the third row.
	 * 
	 * @return Returns the m22.
	 */
	public final float getM22() {
		return this.m22;
	}

	/**
	 * Set the third matrix element in the third row.
	 * 
	 * @param m22 The m22 to set.
	 */
	public final void setM22(float m22) {
		this.m22 = m22;
	}

	/**
	 * Get the fourth element of the first row.
	 * 
	 * @return Returns the m03.
	 */
	public final float getM03() {
		return this.m03;
	}

	/**
	 * Set the fourth element of the first row.
	 * 
	 * @param m03 The m03 to set.
	 */
	public final void setM03(float m03) {
		this.m03 = m03;
	}

	/**
	 * Get the fourth element of the second row.
	 * 
	 * @return Returns the m13.
	 */
	public final float getM13() {
		return this.m13;
	}

	/**
	 * Set the fourth element of the second row.
	 * 
	 * @param m13 The m13 to set.
	 */
	public final void setM13(float m13) {
		this.m13 = m13;
	}

	/**
	 * Get the fourth element of the third row.
	 * 
	 * @return Returns the m23.
	 */
	public final float getM23() {
		return this.m23;
	}

	/**
	 * Set the fourth element of the third row.
	 * 
	 * @param m23 The m23 to set.
	 */
	public final void setM23(float m23) {
		this.m23 = m23;
	}

	/**
	 * Get the first element of the fourth row.
	 * 
	 * @return Returns the m30.
	 */
	public final float getM30() {
		return this.m30;
	}

	/**
	 * Set the first element of the fourth row.
	 * 
	 * @param m30 The m30 to set.
	 */
	public final void setM30(float m30) {
		this.m30 = m30;
	}

	/**
	 * Get the second element of the fourth row.
	 * 
	 * @return Returns the m31.
	 */
	public final float getM31() {
		return this.m31;
	}

	/**
	 * Set the second element of the fourth row.
	 * 
	 * @param m31 The m31 to set.
	 */
	public final void setM31(float m31) {
		this.m31 = m31;
	}

	/**
	 * Get the third element of the fourth row.
	 * 
	 * @return Returns the m32.
	 */
	public final float getM32() {
		return this.m32;
	}

	/**
	 * Set the third element of the fourth row.
	 * 
	 * @param m32 The m32 to set.
	 */
	public final void setM32(float m32) {
		this.m32 = m32;
	}

	/**
	 * Get the fourth element of the fourth row.
	 * 
	 * @return Returns the m33.
	 */
	public final float getM33() {
		return this.m33;
	}

	/**
	 * Set the fourth element of the fourth row.
	 * 
	 * @param m33 The m33 to set.
	 */
	public final void setM33(float m33) {
		this.m33 = m33;
	}

	/**
	 * Return a new Array with the 16 values of this matrix in order
	 */
	public final float[] intoArray() {
		float[] m = new float[16];

		m[0] = this.m00;
		m[1] = this.m01;
		m[2] = this.m02;
		m[3] = this.m03;
		m[4] = this.m10;
		m[5] = this.m11;
		m[6] = this.m12;
		m[7] = this.m13;
		m[8] = this.m20;
		m[9] = this.m21;
		m[10] = this.m22;
		m[11] = this.m23;
		m[12] = this.m30;
		m[13] = this.m31;
		m[14] = this.m32;
		m[15] = this.m33;

		return m;
	}

	/** Check if this rotation matrix is rotating about 0 degrees (be sure this is a rotationMatrix!). */
	public final boolean isEmptyRotationMatrix() {
		if (this.m00 == 1 && this.m11 == 1 && this.m22 == 1) {
			float[] m = this.intoArray();
			boolean isEmptyRotationMatrix = true;
			for (int i = 0; i < m.length; i++) {
				if (i != 0 && i != 5 && i != 10 && i <= 10) {
					if (m[i] != 0) {
						isEmptyRotationMatrix = false;
						break;
					}
				}
			}

			return isEmptyRotationMatrix;
		}

		return false;
	}
}