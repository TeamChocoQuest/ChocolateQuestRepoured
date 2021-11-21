package team.cqr.cqrepoured.structuregen.generation.part;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import scala.actors.threadpool.Arrays;
import team.cqr.cqrepoured.structuregen.generation.DungeonPlacement;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.structuregen.generation.part.DungeonPart.Registry.ISerializer;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparablePosInfo;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;

public class MultiBlockDungeonPart extends DungeonPart implements IProtectable {

	protected final List<GeneratablePosInfo> blocks;

	protected MultiBlockDungeonPart(Collection<GeneratablePosInfo> blocks) {
		this.blocks = new ArrayList<>(blocks);
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		this.blocks.forEach(generatable -> {
			generatable.generate(world, dungeon);
			dungeon.mark(generatable.getChunkX(), generatable.getChunkY(), generatable.getChunkZ());
		});
		this.blocks.clear();
	}

	@Override
	public boolean isGenerated() {
		return this.blocks.isEmpty();
	}

	public Collection<GeneratablePosInfo> getBlocks() {
		return Collections.unmodifiableCollection(this.blocks);
	}

	@Override
	public BlockPos minPos() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		for (GeneratablePosInfo block : this.blocks) {
			minX = Math.min(block.getX(), minX);
			minY = Math.min(block.getY(), minY);
			minZ = Math.min(block.getZ(), minZ);
		}
		return new BlockPos(minX, minY, minZ);
	}

	@Override
	public BlockPos maxPos() {
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for (GeneratablePosInfo block : this.blocks) {
			maxX = Math.max(block.getX(), maxX);
			maxY = Math.max(block.getY(), maxY);
			maxZ = Math.max(block.getZ(), maxZ);
		}
		return new BlockPos(maxX, maxY, maxZ);
	}

	public static class Builder implements IDungeonPartBuilder {

		private final List<PreparablePosInfo> blocks = new ArrayList<>();

		public Builder add(PreparablePosInfo block) {
			this.blocks.add(block);
			return this;
		}

		public Builder addAll(Collection<PreparablePosInfo> blocks) {
			this.blocks.addAll(blocks);
			return this;
		}

		@Override
		public MultiBlockDungeonPart build(World world, DungeonPlacement placement) {
			List<GeneratablePosInfo> list = this.blocks.stream().map(preparable -> preparable.prepare(world, placement)).filter(Objects::nonNull)
					.collect(Collectors.toList());
			return new MultiBlockDungeonPart(list);
		}

	}

	public static class Serializer implements ISerializer<MultiBlockDungeonPart> {

		@Override
		public NBTTagCompound write(MultiBlockDungeonPart part, NBTTagCompound compound) {
			ByteBuf buf = Unpooled.buffer();
			BlockStatePalette palette = new BlockStatePalette();
			NBTTagList compoundList = new NBTTagList();
			part.blocks.forEach(generatable -> {
				buf.writeShort(generatable.getX());
				buf.writeShort(generatable.getY());
				buf.writeShort(generatable.getZ());
				GeneratablePosInfo.Registry.write(generatable, buf, palette, compoundList);
			});
			compound.setByteArray("blocks", Arrays.copyOf(buf.array(), buf.writerIndex()));
			compound.setTag("palette", palette.writeToNBT());
			compound.setTag("compoundList", compoundList);
			return compound;
		}

		@Override
		public MultiBlockDungeonPart read(World world, NBTTagCompound compound) {
			ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("blocks"));
			BlockStatePalette palette = new BlockStatePalette(compound.getTagList("palette", Constants.NBT.TAG_COMPOUND));
			NBTTagList compoundList = compound.getTagList("compoundList", Constants.NBT.TAG_COMPOUND);
			List<GeneratablePosInfo> blocks = new ArrayList<>();
			while (buf.readableBytes() > 0) {
				int x = buf.readShort();
				int y = buf.readShort();
				int z = buf.readShort();
				blocks.add(GeneratablePosInfo.Registry.read(world, x, y, z, buf, palette, compoundList));
			}
			return new MultiBlockDungeonPart(blocks);
		}

	}

}
