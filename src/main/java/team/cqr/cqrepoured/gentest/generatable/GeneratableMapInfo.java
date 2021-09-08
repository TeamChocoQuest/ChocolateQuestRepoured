package team.cqr.cqrepoured.gentest.generatable;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.gentest.GeneratableDungeon;
import team.cqr.cqrepoured.gentest.generatable.GeneratablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class GeneratableMapInfo extends GeneratablePosInfo {

	private final EntityItemFrame entity;
	private final int mapX;
	private final int mapZ;
	private final byte scale;
	private final boolean fillMap;
	private final int fillRadius;

	public GeneratableMapInfo(int x, int y, int z, EntityItemFrame entity, int mapX, int mapZ, byte scale, boolean fillMap, int fillRadius) {
		super(x, y, z);
		this.entity = entity;
		this.mapX = mapX;
		this.mapZ = mapZ;
		this.scale = scale;
		this.fillMap = fillMap;
		this.fillRadius = fillRadius;
	}

	public GeneratableMapInfo(BlockPos pos, EntityItemFrame entity, int mapX, int mapZ, byte scale, boolean fillMap, int fillRadius) {
		this(pos.getX(), pos.getY(), pos.getZ(), entity, mapX, mapZ, scale, fillMap, fillRadius);
	}

	@Override
	public boolean place(World world, Chunk chunk, ExtendedBlockStorage blockStorage, BlockPos pos, GeneratableDungeon dungeon) {
		IBlockState state = blockStorage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
		int light = state.getLightValue(world, pos);
		if (light > 0) {
			dungeon.markRemovedLight(pos.getX(), pos.getY(), pos.getZ(), light);
		}
		boolean flag = BlockPlacingHelper.setBlockState(world, chunk, blockStorage, pos, Blocks.AIR.getDefaultState(), null, 16);
		ItemStack stack = ItemMap.setupNewMap(world, this.mapX, this.mapZ, this.scale, true, true);
		if (this.fillMap) {
			updateMapData(world, this.mapX, this.mapZ, this.fillRadius, ((ItemMap) stack.getItem()).getMapData(stack, world));
		}
		this.entity.setDisplayedItem(stack);
		world.spawnEntity(this.entity);
		return flag;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public boolean hasSpecialShape() {
		return true;
	}

	private static void updateMapData(World worldIn, int x, int z, int radius, MapData data) {
		if (worldIn.provider.getDimension() == data.dimension) {
			int scale = 1 << data.scale;
			int xCenter = data.xCenter;
			int zCenter = data.zCenter;

			Multiset<MapColor> multiset = HashMultiset.<MapColor>create();
			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

			for (int k1 = 0; k1 < 128; k1++) {
				double d0 = 0.0D;

				for (int l1 = -1; l1 < 128; l1++) {
					int k2 = xCenter + (k1 - 64) * scale;
					int l2 = zCenter + (l1 - 64) * scale;
					multiset.clear();
					Chunk chunk = worldIn.getChunk(k2 >> 4, l2 >> 4);

					if (!chunk.isEmpty()) {
						int i3 = k2 & 15;
						int j3 = l2 & 15;
						int k3 = 0;
						double d1 = 0.0D;

						if (worldIn.provider.isNether()) {
							int l3 = k2 + l2 * 231871;
							l3 = l3 * l3 * 31287121 + l3 * 11;

							if ((l3 >> 20 & 1) == 0) {
								multiset.add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).getMapColor(worldIn,
										BlockPos.ORIGIN), 10);
							} else {
								multiset.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE).getMapColor(worldIn,
										BlockPos.ORIGIN), 100);
							}

							d1 = 100.0D;
						} else {
							for (int i4 = 0; i4 < scale; ++i4) {
								for (int j4 = 0; j4 < scale; ++j4) {
									int k4 = chunk.getHeightValue(i4 + i3, j4 + j3) + 1;
									IBlockState iblockstate = Blocks.AIR.getDefaultState();

									if (k4 <= 1) {
										iblockstate = Blocks.BEDROCK.getDefaultState();
									} else {
										label175: {
											while (true) {
												--k4;
												iblockstate = chunk.getBlockState(i4 + i3, k4, j4 + j3);
												mutablePos.setPos((chunk.x << 4) + i4 + i3, k4, (chunk.z << 4) + j4 + j3);

												if (iblockstate.getMapColor(worldIn, mutablePos) != MapColor.AIR || k4 <= 0) {
													break;
												}
											}

											if (k4 > 0 && iblockstate.getMaterial().isLiquid()) {
												int l4 = k4 - 1;

												while (true) {
													IBlockState iblockstate1 = chunk.getBlockState(i4 + i3, l4--, j4 + j3);
													++k3;

													if (l4 <= 0 || !iblockstate1.getMaterial().isLiquid()) {
														break label175;
													}
												}
											}
										}
									}

									d1 += (double) k4 / (double) (scale * scale);
									multiset.add(iblockstate.getMapColor(worldIn, mutablePos));
								}
							}
						}

						k3 = k3 / (scale * scale);
						double d2 = (d1 - d0) * 4.0D / (scale + 4) + ((k1 + l1 & 1) - 0.5D) * 0.4D;
						int i5 = 1;

						if (d2 > 0.6D) {
							i5 = 2;
						}

						if (d2 < -0.6D) {
							i5 = 0;
						}

						MapColor mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.AIR);

						if (mapcolor == MapColor.WATER) {
							d2 = k3 * 0.1D + (k1 + l1 & 1) * 0.2D;
							i5 = 1;

							if (d2 < 0.5D) {
								i5 = 2;
							}

							if (d2 > 0.9D) {
								i5 = 0;
							}
						}

						d0 = d1;

						if (l1 >= 0 && DungeonGenUtils.isInsideCircle(k2 - x, l2 - z, radius)) {
							byte b0 = data.colors[k1 + l1 * 128];
							byte b1 = (byte) (mapcolor.colorIndex * 4 + i5);

							if (b0 != b1) {
								data.colors[k1 + l1 * 128] = b1;
								data.updateMapData(k1, l1);
							}
						}
					}
				}
			}
		}
	}

	public EntityItemFrame getEntity() {
		return this.entity;
	}

	public int getMapX() {
		return this.mapX;
	}

	public int getMapZ() {
		return this.mapZ;
	}

	public byte getScale() {
		return this.scale;
	}

	public boolean isFillMap() {
		return this.fillMap;
	}

	public int getFillRadius() {
		return this.fillRadius;
	}

	public static class Serializer implements ISerializer<GeneratableMapInfo> {

		@Override
		public void write(GeneratableMapInfo generatable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
			NBTTagCompound compound = new NBTTagCompound();
			generatable.entity.writeToNBTAtomically(compound);
			nbtList.appendTag(compound);
			ByteBufUtils.writeVarInt(buf, generatable.mapX, 5);
			ByteBufUtils.writeVarInt(buf, generatable.mapZ, 5);
			buf.writeByte(generatable.scale);
			buf.writeBoolean(generatable.fillMap);
			ByteBufUtils.writeVarInt(buf, generatable.fillRadius, 5);
		}

		@Override
		public GeneratableMapInfo read(World world, int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			NBTTagCompound compound = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			Entity entity = EntityList.createEntityFromNBT(compound, world);
			int mapX = ByteBufUtils.readVarInt(buf, 5);
			int mapZ = ByteBufUtils.readVarInt(buf, 5);
			byte scale = buf.readByte();
			boolean fillMap = buf.readBoolean();
			int fillRadius = ByteBufUtils.readVarInt(buf, 5);
			return new GeneratableMapInfo(x, y, z, (EntityItemFrame) entity, mapX, mapZ, scale, fillMap, fillRadius);
		}

	}

}