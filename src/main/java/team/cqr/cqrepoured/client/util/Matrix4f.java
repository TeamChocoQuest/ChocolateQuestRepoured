package team.cqr.cqrepoured.client.util;

import java.nio.FloatBuffer;

public class Matrix4f {

	public float f00;
	public float f01;
	public float f02;
	public float f03;
	public float f10;
	public float f11;
	public float f12;
	public float f13;
	public float f20;
	public float f21;
	public float f22;
	public float f23;
	public float f30;
	public float f31;
	public float f32;
	public float f33;

	public Matrix4f() {

	}

	public Matrix4f(float f00, float f01, float f02, float f03, float f10, float f11, float f12, float f13, float f20, float f21, float f22, float f23, float f30, float f31, float f32, float f33) {
		this.f00 = f00;
		this.f01 = f01;
		this.f02 = f02;
		this.f03 = f03;
		this.f10 = f10;
		this.f11 = f11;
		this.f12 = f12;
		this.f13 = f13;
		this.f20 = f20;
		this.f21 = f21;
		this.f22 = f22;
		this.f23 = f23;
		this.f30 = f30;
		this.f31 = f31;
		this.f32 = f32;
		this.f33 = f33;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Matrix4f)) {
			return false;
		}
		Matrix4f other = (Matrix4f) obj;
		return this.f00 == other.f00 && this.f01 == other.f01 && this.f02 == other.f02 && this.f03 == other.f03 && this.f10 == other.f10 && this.f11 == other.f11 && this.f12 == other.f12 && this.f13 == other.f13 && this.f20 == other.f20 && this.f21 == other.f21 && this.f22 == other.f22 && this.f23 == other.f23
				&& this.f30 == other.f30 && this.f31 == other.f31 && this.f32 == other.f32 && this.f33 == other.f33;
	}

	@Override
	public int hashCode() {
		int hashCode = Float.hashCode(this.f00);
		hashCode = 31 * hashCode + Float.hashCode(this.f01);
		hashCode = 31 * hashCode + Float.hashCode(this.f02);
		hashCode = 31 * hashCode + Float.hashCode(this.f03);
		hashCode = 31 * hashCode + Float.hashCode(this.f10);
		hashCode = 31 * hashCode + Float.hashCode(this.f11);
		hashCode = 31 * hashCode + Float.hashCode(this.f12);
		hashCode = 31 * hashCode + Float.hashCode(this.f13);
		hashCode = 31 * hashCode + Float.hashCode(this.f20);
		hashCode = 31 * hashCode + Float.hashCode(this.f21);
		hashCode = 31 * hashCode + Float.hashCode(this.f22);
		hashCode = 31 * hashCode + Float.hashCode(this.f23);
		hashCode = 31 * hashCode + Float.hashCode(this.f30);
		hashCode = 31 * hashCode + Float.hashCode(this.f31);
		hashCode = 31 * hashCode + Float.hashCode(this.f32);
		hashCode = 31 * hashCode + Float.hashCode(this.f33);
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.f00);
		sb.append(' ');
		sb.append(this.f01);
		sb.append(' ');
		sb.append(this.f02);
		sb.append(' ');
		sb.append(this.f03);
		sb.append('\n');
		sb.append(this.f10);
		sb.append(' ');
		sb.append(this.f11);
		sb.append(' ');
		sb.append(this.f12);
		sb.append(' ');
		sb.append(this.f13);
		sb.append('\n');
		sb.append(this.f20);
		sb.append(' ');
		sb.append(this.f21);
		sb.append(' ');
		sb.append(this.f22);
		sb.append(' ');
		sb.append(this.f23);
		sb.append('\n');
		sb.append(this.f30);
		sb.append(' ');
		sb.append(this.f31);
		sb.append(' ');
		sb.append(this.f32);
		sb.append(' ');
		sb.append(this.f33);
		return sb.toString();
	}

	public static Matrix4f identityMatrix() {
		Matrix4f matrix = new Matrix4f();
		matrix.f00 = 1.0F;
		matrix.f11 = 1.0F;
		matrix.f22 = 1.0F;
		matrix.f33 = 1.0F;
		return matrix;
	}

	public static Matrix4f translateMatrix(float x, float y, float z) {
		Matrix4f matrix = new Matrix4f();
		matrix.f00 = 1.0F;
		matrix.f11 = 1.0F;
		matrix.f22 = 1.0F;
		matrix.f33 = 1.0F;
		matrix.f03 = x;
		matrix.f13 = y;
		matrix.f23 = z;
		return matrix;
	}

	public static Matrix4f scaleMatrix(float x, float y, float z) {
		Matrix4f matrix = new Matrix4f();
		matrix.f00 = x;
		matrix.f11 = y;
		matrix.f22 = z;
		matrix.f33 = 1.0F;
		return matrix;
	}

	public static Matrix4f rotateMatrix(float radian, float x, float y, float z) {
		// setup quaternion
		float f = (float) Math.sin(radian * 0.5F);
		float fx = x * f;
		float fy = y * f;
		float fz = z * f;
		float fw = (float) Math.cos(radian * 0.5F);

		// setup rotation matrix
		float fxfx = fx * fx;
		float fyfy = fy * fy;
		float fzfz = fz * fz;
		float fxfy = fx * fy;
		float fyfz = fy * fz;
		float fxfz = fx * fz;
		float fxfw = fx * fw;
		float fyfw = fy * fw;
		float fzfw = fz * fw;

		Matrix4f matrix = new Matrix4f();
		matrix.f00 = 1.0F - 2.0F * (fyfy - fzfz);
		matrix.f01 = 2.0F * (fxfy - fzfw);
		matrix.f02 = 2.0F * (fxfz - fyfw);

		matrix.f10 = 2.0F * (fxfy + fzfw);
		matrix.f11 = 1.0F - 2.0F * (fxfx - fzfz);
		matrix.f12 = 2.0F * (fyfz - fxfw);

		matrix.f20 = 2.0F * (fxfz + fyfw);
		matrix.f21 = 2.0F * (fyfz + fxfw);
		matrix.f22 = 1.0F - 2.0F * (fxfx - fyfy);

		matrix.f33 = 1.0F;

		return matrix;
	}

	public static Matrix4f perspectiveMatrix(float fovy, float aspect, float near, float far) {
		Matrix4f matrix = new Matrix4f();
		float f = 1.0F / (float) Math.tan(Math.toRadians(fovy) * 0.5F);
		matrix.f00 = f / aspect;
		matrix.f11 = f;
		matrix.f22 = (far + near) / (near - far);
		matrix.f33 = 0.0F;
		matrix.f32 = -1.0F;
		matrix.f23 = 2.0F * far * near / (near - far);
		return matrix;
	}

	public void loadIdentity() {
		this.f00 = 1;
		this.f01 = 0;
		this.f02 = 0;
		this.f03 = 0;
		this.f10 = 0;
		this.f11 = 1;
		this.f12 = 0;
		this.f13 = 0;
		this.f20 = 0;
		this.f21 = 0;
		this.f22 = 1;
		this.f23 = 0;
		this.f30 = 0;
		this.f31 = 0;
		this.f32 = 0;
		this.f33 = 1;
	}

	public void multiply(Matrix4f matrix) {
		float nf00 = this.f00 * matrix.f00 + this.f01 * matrix.f10 + this.f02 * matrix.f20 + this.f03 * matrix.f30;
		float nf01 = this.f00 * matrix.f01 + this.f01 * matrix.f11 + this.f02 * matrix.f21 + this.f03 * matrix.f31;
		float nf02 = this.f00 * matrix.f02 + this.f01 * matrix.f12 + this.f02 * matrix.f22 + this.f03 * matrix.f32;
		float nf03 = this.f00 * matrix.f03 + this.f01 * matrix.f13 + this.f02 * matrix.f23 + this.f03 * matrix.f33;
		float nf10 = this.f10 * matrix.f00 + this.f11 * matrix.f10 + this.f12 * matrix.f20 + this.f13 * matrix.f30;
		float nf11 = this.f10 * matrix.f01 + this.f11 * matrix.f11 + this.f12 * matrix.f21 + this.f13 * matrix.f31;
		float nf12 = this.f10 * matrix.f02 + this.f11 * matrix.f12 + this.f12 * matrix.f22 + this.f13 * matrix.f32;
		float nf13 = this.f10 * matrix.f03 + this.f11 * matrix.f13 + this.f12 * matrix.f23 + this.f13 * matrix.f33;
		float nf20 = this.f20 * matrix.f00 + this.f21 * matrix.f10 + this.f22 * matrix.f20 + this.f23 * matrix.f30;
		float nf21 = this.f20 * matrix.f01 + this.f21 * matrix.f11 + this.f22 * matrix.f21 + this.f23 * matrix.f31;
		float nf22 = this.f20 * matrix.f02 + this.f21 * matrix.f12 + this.f22 * matrix.f22 + this.f23 * matrix.f32;
		float nf23 = this.f20 * matrix.f03 + this.f21 * matrix.f13 + this.f22 * matrix.f23 + this.f23 * matrix.f33;
		float nf30 = this.f30 * matrix.f00 + this.f31 * matrix.f10 + this.f32 * matrix.f20 + this.f33 * matrix.f30;
		float nf31 = this.f30 * matrix.f01 + this.f31 * matrix.f11 + this.f32 * matrix.f21 + this.f33 * matrix.f31;
		float nf32 = this.f30 * matrix.f02 + this.f31 * matrix.f12 + this.f32 * matrix.f22 + this.f33 * matrix.f32;
		float nf33 = this.f30 * matrix.f03 + this.f31 * matrix.f13 + this.f32 * matrix.f23 + this.f33 * matrix.f33;

		this.f00 = nf00;
		this.f01 = nf01;
		this.f02 = nf02;
		this.f03 = nf03;

		this.f10 = nf10;
		this.f11 = nf11;
		this.f12 = nf12;
		this.f13 = nf13;

		this.f20 = nf20;
		this.f21 = nf21;
		this.f22 = nf22;
		this.f23 = nf23;

		this.f30 = nf30;
		this.f31 = nf31;
		this.f32 = nf32;
		this.f33 = nf33;
	}

	public void transpose() {
		float nf01 = this.f10;
		float nf02 = this.f20;
		float nf03 = this.f30;
		float nf10 = this.f01;
		float nf12 = this.f21;
		float nf13 = this.f31;
		float nf20 = this.f02;
		float nf21 = this.f12;
		float nf23 = this.f32;
		float nf30 = this.f03;
		float nf31 = this.f13;
		float nf32 = this.f23;
		this.f01 = nf01;
		this.f02 = nf02;
		this.f03 = nf03;
		this.f10 = nf10;
		this.f12 = nf12;
		this.f13 = nf13;
		this.f20 = nf20;
		this.f21 = nf21;
		this.f23 = nf23;
		this.f30 = nf30;
		this.f31 = nf31;
		this.f32 = nf32;
	}

	public void store(FloatBuffer buffer, boolean transpose) {
		if (transpose) {
			buffer.put(this.f00);
			buffer.put(this.f10);
			buffer.put(this.f20);
			buffer.put(this.f30);
			buffer.put(this.f01);
			buffer.put(this.f11);
			buffer.put(this.f21);
			buffer.put(this.f31);
			buffer.put(this.f02);
			buffer.put(this.f12);
			buffer.put(this.f22);
			buffer.put(this.f32);
			buffer.put(this.f03);
			buffer.put(this.f13);
			buffer.put(this.f23);
			buffer.put(this.f33);
		} else {
			buffer.put(this.f00);
			buffer.put(this.f01);
			buffer.put(this.f02);
			buffer.put(this.f03);
			buffer.put(this.f10);
			buffer.put(this.f11);
			buffer.put(this.f12);
			buffer.put(this.f13);
			buffer.put(this.f20);
			buffer.put(this.f21);
			buffer.put(this.f22);
			buffer.put(this.f23);
			buffer.put(this.f30);
			buffer.put(this.f31);
			buffer.put(this.f32);
			buffer.put(this.f33);
		}
	}

	public Matrix4f copy() {
		return new Matrix4f(this.f00, this.f01, this.f02, this.f03, this.f10, this.f11, this.f12, this.f13, this.f20, this.f21, this.f22, this.f23, this.f30, this.f31, this.f32, this.f33);
	}

}
