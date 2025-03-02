package team.cqr.cqrepoured.client.util;

import java.nio.FloatBuffer;

import javax.vecmath.Matrix4f;

import org.lwjgl.BufferUtils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MatrixUtil {

	public static final FloatBuffer FLOAT_BUFFER = BufferUtils.createFloatBuffer(16);

	public static Matrix4f createMatrixFromBuffer() {
		return loadMatrixFromBuffer(new Matrix4f());
	}

	public static Matrix4f loadMatrixFromBuffer(Matrix4f matrix) {
		matrix.m00 = FLOAT_BUFFER.get(0);
		matrix.m10 = FLOAT_BUFFER.get(1);
		matrix.m20 = FLOAT_BUFFER.get(2);
		matrix.m30 = FLOAT_BUFFER.get(3);
		matrix.m01 = FLOAT_BUFFER.get(4);
		matrix.m11 = FLOAT_BUFFER.get(5);
		matrix.m21 = FLOAT_BUFFER.get(6);
		matrix.m31 = FLOAT_BUFFER.get(7);
		matrix.m02 = FLOAT_BUFFER.get(8);
		matrix.m12 = FLOAT_BUFFER.get(9);
		matrix.m22 = FLOAT_BUFFER.get(10);
		matrix.m32 = FLOAT_BUFFER.get(11);
		matrix.m03 = FLOAT_BUFFER.get(12);
		matrix.m13 = FLOAT_BUFFER.get(13);
		matrix.m23 = FLOAT_BUFFER.get(14);
		matrix.m33 = FLOAT_BUFFER.get(15);
		return matrix;
	}

	public static void storeMatrixInBuffer(Matrix4f matrix) {
		FLOAT_BUFFER.put(0, matrix.m00);
		FLOAT_BUFFER.put(1, matrix.m10);
		FLOAT_BUFFER.put(2, matrix.m20);
		FLOAT_BUFFER.put(3, matrix.m30);
		FLOAT_BUFFER.put(4, matrix.m01);
		FLOAT_BUFFER.put(5, matrix.m11);
		FLOAT_BUFFER.put(6, matrix.m21);
		FLOAT_BUFFER.put(7, matrix.m31);
		FLOAT_BUFFER.put(8, matrix.m02);
		FLOAT_BUFFER.put(9, matrix.m12);
		FLOAT_BUFFER.put(10, matrix.m22);
		FLOAT_BUFFER.put(11, matrix.m32);
		FLOAT_BUFFER.put(12, matrix.m03);
		FLOAT_BUFFER.put(13, matrix.m13);
		FLOAT_BUFFER.put(14, matrix.m23);
		FLOAT_BUFFER.put(15, matrix.m33);
	}

}
