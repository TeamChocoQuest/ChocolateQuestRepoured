package team.cqr.cqrepoured.util.math;

import net.minecraft.util.math.MathHelper;

public class InterpolationUtil {

	@FunctionalInterface
	public interface Interpolation {

		float interpolate(float f0, float f1, float fracX);

		default float interpolate(float f00, float f01, float f10, float f11, float fracX, float fracY) {
			return interpolate(interpolate(f00, f10, fracX), interpolate(f01, f11, fracX), fracY);
		}

		default float interpolate(float f000, float f001, float f010, float f011, float f100, float f101, float f110, float f111, float fracX, float fracY, float fracZ) {
			return interpolate(interpolate(f000, f100, f010, f110, fracX, fracY), interpolate(f001, f101, f011, f111, fracX, fracY), fracZ);
		}

	}

	public static Interpolation construct(Interpolation parent, FloatUnaryOperator func) {
		return new Interpolation() {

			@Override
			public float interpolate(float f0, float f1, float fracX) {
				return parent.interpolate(f0, f1, func.applyAsFloat(fracX));
			}

			@Override
			public float interpolate(float f00, float f01, float f10, float f11, float fracX, float fracY) {
				return parent.interpolate(f00, f01, f10, f11, func.applyAsFloat(fracX), func.applyAsFloat(fracY));
			}

			@Override
			public float interpolate(float f000, float f001, float f010, float f011, float f100, float f101, float f110, float f111, float fracX, float fracY, float fracZ) {
				return parent.interpolate(f000, f001, f010, f011, f100, f101, f110, f111, func.applyAsFloat(fracX), func.applyAsFloat(fracY), func.applyAsFloat(fracZ));
			}

		};
	}

	public static final Interpolation LINEAR = (f0, f1, fracX) -> f0 + (f1 - f0) * fracX;
	public static final Interpolation SMOOTHSTEP = construct(LINEAR, x -> x * x * (3.0F - 2.0F * x));
	public static final Interpolation SMOOTHERSTEP = construct(LINEAR, x -> x * x * x * (x * (x * 6.0F - 15.0F) + 10.0F));
	public static final Interpolation COSINE = construct(LINEAR, x -> (1.0F - MathHelper.cos(x * (float) Math.PI) * 0.5F));

}
