package team.cqr.cqrepoured.structuregen.structurefile;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class BlockInfoMap extends AbstractBlockInfo {

	private EnumFacing facing = EnumFacing.NORTH;
	private int scale = 0;
	private EnumFacing orientation = EnumFacing.NORTH;
	private boolean lockOrientation = false;
	private int originX = 0;
	private int originZ = 0;
	private int offsetX = 0;
	private int offsetZ = 0;
	private boolean fillMap = false;
	private int fillRadius = 256;

	public BlockInfoMap(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoMap(BlockPos pos) {
		super(pos);
	}

	public BlockInfoMap(int x, int y, int z, EnumFacing facing, int scale, EnumFacing orientation, boolean lockOrientation, int originX, int originZ, int offsetX, int offsetZ, boolean fillMap, int fillRadius) {
		super(x, y, z);
		this.facing = facing;
		this.scale = scale;
		this.orientation = orientation;
		this.lockOrientation = lockOrientation;
		this.originX = originX;
		this.originZ = originZ;
		this.offsetX = offsetX;
		this.offsetZ = offsetZ;
		this.fillMap = fillMap;
		this.fillRadius = fillRadius;
	}

	public BlockInfoMap(BlockPos pos, EnumFacing facing, int scale, EnumFacing orientation, boolean lockOrientation, int originX, int originZ, int offsetX, int offsetZ, boolean fillMap, int fillRadius) {
		this(pos.getX(), pos.getY(), pos.getZ(), facing, scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
	}

	public BlockInfoMap(int x, int y, int z, EnumFacing facing, TileEntityMap tileEntity) {
		this(x, y, z, facing, tileEntity.getScale(), tileEntity.getOrientation(), tileEntity.lockOrientation(), tileEntity.getOriginX(), tileEntity.getOriginZ(), tileEntity.getOffsetX(), tileEntity.getOffsetZ(), tileEntity.fillMap(), tileEntity.getFillRadius());
	}

	public BlockInfoMap(BlockPos pos, EnumFacing facing, TileEntityMap tileEntity) {
		this(pos.getX(), pos.getY(), pos.getZ(), facing, tileEntity);
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {
		BlockPlacingHelper.setBlockState(world, pos, Blocks.AIR.getDefaultState(), 18, false);
		EnumFacing transformedFacing = settings.getRotation().rotate(settings.getMirror().mirror(this.facing));
		EntityItemFrame entity = new EntityItemFrame(world, pos.toImmutable(), transformedFacing);
		switch (this.orientation) {
		case EAST:
			entity.setItemRotation(entity.getRotation() + 3);
			break;
		case SOUTH:
			entity.setItemRotation(entity.getRotation() + 2);
			break;
		case WEST:
			entity.setItemRotation(entity.getRotation() + 1);
			break;
		default:
			break;
		}

		int x1 = this.offsetX * (128 << this.scale);
		int z1 = this.offsetZ * (128 << this.scale);
		int x2 = this.originX;
		int z2 = this.originZ;
		switch (settings.getMirror()) {
		case LEFT_RIGHT:
			if (!this.lockOrientation) {
				if (this.orientation.getAxis() == Axis.Z) {
					entity.setItemRotation(entity.getRotation() + 2);
				}
				z1 = -z1;
			} else {
				if (this.orientation.getAxis() == Axis.X) {
					z1 = -z1;
				}
				if (this.orientation.getAxis() == Axis.Z) {
					x1 = -x1;
				}
			}
			z2 = -z2;
			break;
		case FRONT_BACK:
			if (!this.lockOrientation) {
				if (this.orientation.getAxis() == Axis.X) {
					entity.setItemRotation(entity.getRotation() + 2);
				}
				x1 = -x1;
			} else {
				if (this.orientation.getAxis() == Axis.X) {
					z1 = -z1;
				}
				if (this.orientation.getAxis() == Axis.Z) {
					x1 = -x1;
				}
			}
			x2 = -x2;
			break;
		default:
			break;
		}

		int x3 = x1;
		int z3 = z1;
		int x4 = x2;
		int z4 = z2;
		switch (settings.getRotation()) {
		case COUNTERCLOCKWISE_90:
			if (!this.lockOrientation) {
				entity.setItemRotation(entity.getRotation() + 1);
				x1 = z3;
				z1 = -x3;
			}
			x2 = z4;
			z2 = -x4;
			break;
		case CLOCKWISE_90:
			if (!this.lockOrientation) {
				entity.setItemRotation(entity.getRotation() + 3);
				x1 = -z3;
				z1 = x3;
			}
			x2 = -z4;
			z2 = x4;
			break;
		case CLOCKWISE_180:
			if (!this.lockOrientation) {
				entity.setItemRotation(entity.getRotation() + 2);
				x1 = -x3;
				z1 = -z3;
			}
			x2 = -x4;
			z2 = -z4;
			break;
		default:
			break;
		}

		ItemStack stack = ItemMap.setupNewMap(world, pos.getX() + x1 + x2, pos.getZ() + z1 + z2, (byte) this.scale, true, true);
		if (this.fillMap) {
			updateMapData(world, pos.getX() + x1 + x2, pos.getZ() + z1 + z2, this.fillRadius, ((ItemMap) stack.getItem()).getMapData(stack, world));
		}

		entity.setDisplayedItem(stack);
		world.spawnEntity(entity);
	}

	@Override
	public byte getId() {
		return MAP_INFO_ID;
	}

	@Override
	protected void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setByte("facing", (byte) this.facing.getHorizontalIndex());
		compound.setByte("scale", (byte) this.scale);
		compound.setByte("orientation", (byte) this.orientation.getHorizontalIndex());
		compound.setBoolean("lockOrientation", this.lockOrientation);
		compound.setByte("originX", (byte) this.originX);
		compound.setByte("originZ", (byte) this.originZ);
		compound.setByte("offsetX", (byte) this.offsetX);
		compound.setByte("offsetZ", (byte) this.offsetZ);
		compound.setBoolean("fillMap", this.fillMap);
		compound.setShort("fillRadius", (short) this.fillRadius);
		ByteBufUtils.writeVarInt(buf, compoundTagList.tagCount(), 5);
		compoundTagList.appendTag(compound);
	}

	@Override
	protected void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		NBTTagCompound compound = compoundTagList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
		this.facing = EnumFacing.byHorizontalIndex(compound.getInteger("facing"));
		this.scale = compound.getByte("scale");
		this.orientation = EnumFacing.byHorizontalIndex(compound.getInteger("orientation"));
		this.lockOrientation = compound.getBoolean("lockOrientation");
		this.originX = compound.getByte("originX");
		this.originZ = compound.getByte("originZ");
		this.offsetX = compound.getByte("offsetX");
		this.offsetZ = compound.getByte("offsetZ");
		this.fillMap = compound.getBoolean("fillMap");
		this.fillRadius = compound.getShort("fillRadius");
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
								multiset.add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).getMapColor(worldIn, BlockPos.ORIGIN), 10);
							} else {
								multiset.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE).getMapColor(worldIn, BlockPos.ORIGIN), 100);
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
						double d2 = (d1 - d0) * 4.0D / (double) (scale + 4) + ((double) (k1 + l1 & 1) - 0.5D) * 0.4D;
						int i5 = 1;

						if (d2 > 0.6D) {
							i5 = 2;
						}

						if (d2 < -0.6D) {
							i5 = 0;
						}

						MapColor mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.AIR);

						if (mapcolor == MapColor.WATER) {
							d2 = (double) k3 * 0.1D + (double) (k1 + l1 & 1) * 0.2D;
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

}
