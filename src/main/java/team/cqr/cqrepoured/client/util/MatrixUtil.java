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
		matrix.m10 = FLOAT_BUFFER.get(4);
		matrix.m20 = FLOAT_BUFFER.get(8);
		matrix.m30 = FLOAT_BUFFER.get(12);
		matrix.m01 = FLOAT_BUFFER.get(16);
		matrix.m11 = FLOAT_BUFFER.get(20);
		matrix.m21 = FLOAT_BUFFER.get(24);
		matrix.m31 = FLOAT_BUFFER.get(28);
		matrix.m02 = FLOAT_BUFFER.get(32);
		matrix.m12 = FLOAT_BUFFER.get(36);
		matrix.m22 = FLOAT_BUFFER.get(40);
		matrix.m32 = FLOAT_BUFFER.get(44);
		matrix.m03 = FLOAT_BUFFER.get(48);
		matrix.m13 = FLOAT_BUFFER.get(52);
		matrix.m23 = FLOAT_BUFFER.get(56);
		matrix.m33 = FLOAT_BUFFER.get(60);
		return matrix;
	}

	public static void storeMatrixInBuffer(Matrix4f matrix) {
		FLOAT_BUFFER.put(0, matrix.m00);
		FLOAT_BUFFER.put(4, matrix.m10);
		FLOAT_BUFFER.put(8, matrix.m20);
		FLOAT_BUFFER.put(12, matrix.m30);
		FLOAT_BUFFER.put(16, matrix.m01);
		FLOAT_BUFFER.put(20, matrix.m11);
		FLOAT_BUFFER.put(24, matrix.m21);
		FLOAT_BUFFER.put(28, matrix.m31);
		FLOAT_BUFFER.put(32, matrix.m02);
		FLOAT_BUFFER.put(36, matrix.m12);
		FLOAT_BUFFER.put(40, matrix.m22);
		FLOAT_BUFFER.put(44, matrix.m32);
		FLOAT_BUFFER.put(48, matrix.m03);
		FLOAT_BUFFER.put(52, matrix.m13);
		FLOAT_BUFFER.put(56, matrix.m23);
		FLOAT_BUFFER.put(60, matrix.m33);
	}

}
