package team.cqr.cqrepoured.generation.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.SectionPos;

public class SectionUtil {

	public static Iterable<BlockPos> positionsInSection(SectionPos sectionPos) {
		return () -> new Iterator<BlockPos>() {
			private int x;
			private int y;
			private int z;
			private final MutableBlockPos pos = new MutableBlockPos();

			@Override
			public boolean hasNext() {
				return x < 16;
			}

			@Override
			public BlockPos next() {
				if (!hasNext())
					throw new NoSuchElementException();
				setPos(pos, sectionPos, x, y, z);
				if (z < 15) {
					z++;
				} else {
					z = 0;
					if (y < 15) {
						y++;
					} else {
						y = 0;
						x++;
					}
				}
				return pos;
			}
		};
	}

	public static MutableBlockPos setPos(MutableBlockPos mutablePos, SectionPos sectionPos, int index) {
		return setPos(mutablePos, sectionPos, x(index), y(index), z(index));
	}

	public static MutableBlockPos setPos(MutableBlockPos mutablePos, SectionPos sectionPos, int x, int y, int z) {
		return mutablePos.set(sectionPos.minBlockX() | x, sectionPos.minBlockY() | y, sectionPos.minBlockZ() | z);
	}

	public static int index(BlockPos pos) {
		return index(pos.getX(), pos.getY(), pos.getZ());
	}

	public static int index(int x, int y, int z) {
		return (y & 15) << 8 | (z & 15) << 4 | (x & 15);
	}

	public static int x(int i) {
		return i & 15;
	}

	public static int y(int i) {
		return i >> 8;
	}

	public static int z(int i) {
		return (i >> 4) & 15;
	}

}
