package team.cqr.cqrepoured.util.palette;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BitArray;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.palette.ArrayPalette;
import net.minecraft.util.palette.HashMapPalette;
import net.minecraft.util.palette.IPalette;
import net.minecraft.util.palette.IResizeCallback;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PalettedContainer<T> implements IResizeCallback<T> {

	private final IPalette<T> globalPalette;
	private final IResizeCallback<T> dummyPaletteResize = (p_205517_0_, p_205517_1_) -> {
		return 0;
	};
	private final ObjectIntIdentityMap<T> registry;
	private final Function<CompoundNBT, T> reader;
	private final Function<T, CompoundNBT> writer;
	private final T defaultValue;
	protected BitArray storage;
	private IPalette<T> palette;
	private int bits;
	private final ReentrantLock lock = new ReentrantLock();

	public void acquire() {
		if (this.lock.isLocked() && !this.lock.isHeldByCurrentThread()) {
			String s = Thread.getAllStackTraces().keySet().stream().filter(Objects::nonNull).map((p_210458_0_) -> {
				return p_210458_0_.getName() + ": \n\tat " + (String) Arrays.stream(p_210458_0_.getStackTrace())
						.map(Object::toString).collect(Collectors.joining("\n\tat "));
			}).collect(Collectors.joining("\n"));
			CrashReport crashreport = new CrashReport("Writing into PalettedContainer from multiple threads",
					new IllegalStateException());
			CrashReportCategory crashreportcategory = crashreport.addCategory("Thread dumps");
			crashreportcategory.setDetail("Thread dumps", s);
			throw new ReportedException(crashreport);
		} else {
			this.lock.lock();
		}
	}

	public void release() {
		this.lock.unlock();
	}

	public PalettedContainer(IPalette<T> pGlobalPalette, ObjectIntIdentityMap<T> pRegistry,
			Function<CompoundNBT, T> pReader, Function<T, CompoundNBT> pWriter, T pDefaultValue) {
		this.globalPalette = pGlobalPalette;
		this.registry = pRegistry;
		this.reader = pReader;
		this.writer = pWriter;
		this.defaultValue = pDefaultValue;
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
				this.bits = MathHelper.ceillog2(this.registry.size());
				if (forceBits)
					this.bits = bitsIn;
			}

			this.palette.idFor(this.defaultValue);
			this.storage = new BitArray(this.bits, 4096);
		}
	}

	public int onResize(int p_onResize_1_, T p_onResize_2_) {
		this.acquire();
		BitArray bitarray = this.storage;
		IPalette<T> ipalette = this.palette;
		this.setBits(p_onResize_1_);

		for (int i = 0; i < bitarray.getSize(); ++i) {
			T t = ipalette.valueFor(bitarray.get(i));
			if (t != null) {
				this.set(i, t);
			}
		}

		int j = this.palette.idFor(p_onResize_2_);
		this.release();
		return j;
	}

	public T getAndSet(int pX, int pY, int pZ, T pState) {
		this.acquire();
		T t = this.getAndSet(getIndex(pX, pY, pZ), pState);
		this.release();
		return t;
	}

	public T getAndSetUnchecked(int pX, int pY, int pZ, T pState) {
		return this.getAndSet(getIndex(pX, pY, pZ), pState);
	}

	protected T getAndSet(int pIndex, T pState) {
		int i = this.palette.idFor(pState);
		int j = this.storage.getAndSet(pIndex, i);
		T t = this.palette.valueFor(j);
		return (T) (t == null ? this.defaultValue : t);
	}

	public void set(int pIndex, T pState) {
		int i = this.palette.idFor(pState);
		this.storage.set(pIndex, i);
	}

	public T get(int pX, int pY, int pZ) {
		return this.get(getIndex(pX, pY, pZ));
	}

	public T get(int pIndex) {
		T t = this.palette.valueFor(this.storage.get(pIndex));
		return (T) (t == null ? this.defaultValue : t);
	}

	@OnlyIn(Dist.CLIENT)
	public void read(PacketBuffer pBuf) {
		this.acquire();
		int i = pBuf.readByte();
		if (this.bits != i) {
			this.setBits(i, true); // Forge, Force bit density to fix network issues, resize below if needed.
		}

		this.palette.read(pBuf);
		pBuf.readLongArray(this.storage.getRaw());
		this.release();

		int regSize = MathHelper.ceillog2(this.registry.size());
		if (this.palette == globalPalette && this.bits != regSize) // Resize bits to fit registry.
			this.onResize(regSize, defaultValue);
	}

	public void write(PacketBuffer pBuf) {
		this.acquire();
		pBuf.writeByte(this.bits);
		this.palette.write(pBuf);
		pBuf.writeLongArray(this.storage.getRaw());
		this.release();
	}

	public void read(ListNBT pPaletteNbt, long[] pData) {
		this.acquire();
		int i = Math.max(4, MathHelper.ceillog2(pPaletteNbt.size()));
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

		this.release();
	}

	public void write(CompoundNBT pCompound, String pPaletteName, String pPaletteDataName) {
		this.acquire();
		HashMapPalette<T> hashmappalette = new HashMapPalette<>(this.registry, this.bits, this.dummyPaletteResize,
				this.reader, this.writer);
		T t = this.defaultValue;
		int i = hashmappalette.idFor(this.defaultValue);
		int[] aint = new int[4096];

		for (int j = 0; j < 4096; ++j) {
			T t1 = this.get(j);
			if (t1 != t) {
				t = t1;
				i = hashmappalette.idFor(t1);
			}

			aint[j] = i;
		}

		ListNBT listnbt = new ListNBT();
		hashmappalette.write(listnbt);
		pCompound.put(pPaletteName, listnbt);
		int l = Math.max(4, MathHelper.ceillog2(listnbt.size()));
		BitArray bitarray = new BitArray(l, 4096);

		for (int k = 0; k < aint.length; ++k) {
			bitarray.set(k, aint[k]);
		}

		pCompound.putLongArray(pPaletteDataName, bitarray.getRaw());
		this.release();
	}

	public int getSerializedSize() {
		return 1 + this.palette.getSerializedSize() + PacketBuffer.getVarIntSize(this.storage.getSize())
				+ this.storage.getRaw().length * 8;
	}

	public boolean maybeHas(Predicate<T> p_235963_1_) {
		return this.palette.maybeHas(p_235963_1_);
	}

	public void count(PalettedContainer.ICountConsumer<T> pCountConsumer) {
		Int2IntMap int2intmap = new Int2IntOpenHashMap();
		this.storage.getAll((p_225498_1_) -> {
			int2intmap.put(p_225498_1_, int2intmap.get(p_225498_1_) + 1);
		});
		int2intmap.int2IntEntrySet().forEach((p_225499_2_) -> {
			pCountConsumer.accept(this.palette.valueFor(p_225499_2_.getIntKey()), p_225499_2_.getIntValue());
		});
	}

	@FunctionalInterface
	public interface ICountConsumer<T> {
		void accept(T p_accept_1_, int p_accept_2_);
	}

}
