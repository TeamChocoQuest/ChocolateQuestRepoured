package team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.apache.commons.lang3.Validate;

import net.minecraft.core.SectionPos;
import net.minecraft.nbt.ByteArrayTag;
import team.cqr.cqrepoured.generation.util.Section;
import team.cqr.cqrepoured.generation.util.SectionUtil;

public class NoiseSection extends Section<ByteArrayTag> {

	private final float[] noiseData = new float[16 * 16 * 16];

	public NoiseSection(SectionPos pos) {
		super(pos);
	}

	public NoiseSection(SectionPos pos, ByteArrayTag nbt) {
		super(pos);
		Validate.isTrue(nbt.getAsByteArray().length == this.noiseData.length * Float.BYTES);
		FloatBuffer buffer = ByteBuffer.wrap(nbt.getAsByteArray())
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		for (int i = 0; i < this.noiseData.length; i++) {
			this.noiseData[i] = buffer.get(i);
		}
	}

	@Override
	public ByteArrayTag save() {
		byte[] data = new byte[this.noiseData.length * Float.BYTES];
		FloatBuffer buffer = ByteBuffer.wrap(data)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		for (int i = 0; i < this.noiseData.length; i++) {
			buffer.put(i, this.noiseData[i]);
		}
		return new ByteArrayTag(data);
	}

	public double get(int x, int y, int z) {
		return this.noiseData[SectionUtil.index(x, y, z)];
	}

	void setIfLess(int x, int y, int z, double n) {
		int i = SectionUtil.index(x, y, z);
		if (n < this.noiseData[i]) {
			this.noiseData[i] = (float) n;
		}
	}

	void setIfGreater(int x, int y, int z, double n) {
		int i = SectionUtil.index(x, y, z);
		if (n > this.noiseData[i]) {
			this.noiseData[i] = (float) n;
		}
	}

}
