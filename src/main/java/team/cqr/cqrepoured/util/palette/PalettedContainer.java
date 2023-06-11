package team.cqr.cqrepoured.util.palette;

import java.util.function.Function;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.BitArray;
import net.minecraft.util.Mth;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.palette.ArrayPalette;
import net.minecraft.util.palette.HashMapPalette;
import net.minecraft.util.palette.IPalette;
import net.minecraft.util.palette.IResizeCallback;

public class PalettedContainer<T> implements IResizeCallback<T> {

	private final IPalette<T> globalPalette;
	private final IResizeCallback<T> dummyPaletteResize = (p_205517_0_, p_205517_1_) -> {
		return 0;
	};
	private final ObjectIntIdentityMap<T> registry;
	private final Function<CompoundTag, T> reader;
	private final Function<T, CompoundTag> writer;
	protected BitArray storage;
	private IPalette<T> palette;
	private int bits;
	private final BooleanArray nonemptyBlocks = new BooleanArray();

	public PalettedContainer(IPalette<T> pGlobalPalette, ObjectIntIdentityMap<T> pRegistry,
                             Function<CompoundTag, T> pReader, Function<T, CompoundTag> pWriter) {
		this.globalPalette = pGlobalPalette;
		this.registry = pRegistry;
		this.reader = pReader;
		this.writer = pWriter;
		this.setBits(4);
	}

	public static int getIndex(int pX, int pY, int pZ) {
		return pY << 8 | pZ << 4 | pX;
	}

	private void setBits(int pBits) {
		setBits(pBits, false);
	}

	private void setBits(int bitsIn, boolean forceBits) {
		if (bitsIn != this.bits) {
			this.bits = bitsIn;
			if (this.bits <= 4) {
				this.bits = 4;
				this.palette = new ArrayPalette<>(this.registry, this.bits, this, this.reader);
			} else if (this.bits < 9) {
				this.palette = new HashMapPalette<>(this.registry, this.bits, this, this.reader, this.writer);
			} else {
				this.palette = this.globalPalette;
				this.bits = Mth.ceillog2(this.registry.size());
				if (forceBits)
					this.bits = bitsIn;
			}

			this.storage = new BitArray(this.bits, 4096);
		}
	}

	public int onResize(int p_onResize_1_, T p_onResize_2_) {
		BitArray bitarray = this.storage;
		IPalette<T> ipalette = this.palette;
		this.setBits(p_onResize_1_);

		for (int i = 0; i < bitarray.getSize(); ++i) {
			if (!this.nonemptyBlocks.get(i)) {
				continue;
			}
			T t = ipalette.valueFor(bitarray.get(i));
			this.set(i, t);
		}

		int j = this.palette.idFor(p_onResize_2_);
		return j;
	}

	public void set(int pX, int pY, int pZ, T pState) {
		this.set(getIndex(pX, pY, pZ), pState);
	}

	public void set(int pIndex, T pState) {
		if (pState == null) {
			this.nonemptyBlocks.set(pIndex, false);
			return;
		}
		this.nonemptyBlocks.set(pIndex, true);
		int i = this.palette.idFor(pState);
		this.storage.set(pIndex, i);
	}

	public T get(int pX, int pY, int pZ) {
		return this.get(getIndex(pX, pY, pZ));
	}

	public T get(int pIndex) {
		if (!this.nonemptyBlocks.get(pIndex)) {
			return null;
		}
		T t = this.palette.valueFor(this.storage.get(pIndex));
		return t;
	}

	public void read(ListTag pPaletteNbt, long[] pData) {
		// TODO doesn't work with null values
		int i = Math.max(4, Mth.ceillog2(pPaletteNbt.size()));
		if (i != this.bits) {
			this.setBits(i);
		}

		this.palette.read(pPaletteNbt);
		int j = pData.length * 64 / 4096;
		if (this.palette == this.globalPalette) {
			IPalette<T> ipalette = new HashMapPalette<>(this.registry, i, this.dummyPaletteResize, this.reader,
					this.writer);
			ipalette.read(pPaletteNbt);
			BitArray bitarray = new BitArray(i, 4096, pData);

			for (int k = 0; k < 4096; ++k) {
				this.storage.set(k, this.globalPalette.idFor(ipalette.valueFor(bitarray.get(k))));
			}
		} else if (j == this.bits) {
			System.arraycopy(pData, 0, this.storage.getRaw(), 0, pData.length);
		} else {
			BitArray bitarray1 = new BitArray(j, 4096, pData);

			for (int l = 0; l < 4096; ++l) {
				this.storage.set(l, bitarray1.get(l));
			}
		}
	}

	public void write(CompoundTag pCompound, String pPaletteName, String pPaletteDataName) {
		// TODO doesn't work with null values
		HashMapPalette<T> hashmappalette = new HashMapPalette<>(this.registry, this.bits, this.dummyPaletteResize,
				this.reader, this.writer);
		T t = null;
		int i = 0;
		int[] aint = new int[4096];

		for (int j = 0; j < 4096; ++j) {
			T t1 = this.get(j);
			if (t1 != t) {
				t = t1;
				i = hashmappalette.idFor(t1);
			}

			aint[j] = i;
		}

		ListTag listnbt = new ListTag();
		hashmappalette.write(listnbt);
		pCompound.put(pPaletteName, listnbt);
		int l = Math.max(4, Mth.ceillog2(listnbt.size()));
		BitArray bitarray = new BitArray(l, 4096);

		for (int k = 0; k < aint.length; ++k) {
			bitarray.set(k, aint[k]);
		}

		pCompound.putLongArray(pPaletteDataName, bitarray.getRaw());
	}

	private static class BooleanArray {

		private final int[] data = new int[128];

		public void set(int index, boolean value) {
			if (value) {
				data[index >>> 5] |= 1 << (index & 31);
			} else {
				data[index >>> 5] &= ~(1 << (index & 31));
			}
		}

		public boolean get(int index) {
			return ((data[index >>> 5] >>> (index & 31)) & 1) == 1;
		}

	}

}
