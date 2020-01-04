package mcalibrary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import mcalibrary.math.Matrix4f;
import mcalibrary.math.Quaternion;
import mcalibrary.math.Vector3f;

public class Utils {
	/**
	 * Make a direct NIO FloatBuffer from an array of floats
	 * 
	 * @param arr The array
	 * @return The newly created FloatBuffer
	 */
	public static FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}

	/**
	 * Make a direct NIO ByteBuffer from an array of floats
	 * 
	 * @param arr The array
	 * @return The newly created FloatBuffer
	 */
	public static ByteBuffer makeByteBuffer(byte[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length);
		bb.order(ByteOrder.nativeOrder());
		bb.put(arr);
		bb.position(0);
		return bb;
	}

	/** Get the quaternion from a matrix. We need to transpose the matrix. */
	public static Quaternion getQuaternionFromMatrix(Matrix4f matrix) {
		Matrix4f copy = new Matrix4f(matrix);
		return new Quaternion(copy.transpose());
	}

	/** Get the quaternion from euler angles. Minecraft eulers order is XYZ. */
	public static Quaternion getQuaternionFromEulers(float x, float y, float z) {
		Quaternion quatX = new Quaternion(Vector3f.UNIT_X, (float) Math.toRadians(x));
		Quaternion quatY = new Quaternion(Vector3f.UNIT_Y, (float) Math.toRadians(y));
		Quaternion quatZ = new Quaternion(Vector3f.UNIT_Z, (float) Math.toRadians(z));
		quatY.mul(quatY, quatX);
		quatZ.mul(quatZ, quatY);
		return quatZ;
	}
}
