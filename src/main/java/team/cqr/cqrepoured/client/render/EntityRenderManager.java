package team.cqr.cqrepoured.client.render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.Reference;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class EntityRenderManager {

	private static final ReflectionField<List<?>> FIELD_RENDER_INFOS = new ReflectionField<>(RenderGlobal.class, "field_72755_R", "renderInfos");
	private static final ReflectionField<RenderChunk> FIELD_RENDER_CHUNK = new ReflectionField<>("net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation", "field_178036_a", "renderChunk");

	public static Iterable<RenderChunk> getRenderChunks() {
		return () -> new Iterator<RenderChunk>() {
			private List<?> renderInfos = FIELD_RENDER_INFOS.get(Minecraft.getMinecraft().renderGlobal);
			private int index;

			@Override
			public boolean hasNext() {
				return this.index < this.renderInfos.size();
			}

			@Override
			public RenderChunk next() {
				if (!this.hasNext()) {
					throw new NoSuchElementException();
				}
				return FIELD_RENDER_CHUNK.get(this.renderInfos.get(this.index++));
			}
		};
	}

	private static boolean shouldRenderAll = false;
	private static final Set<AbstractEntityCQR> ENTITIES_TO_RENDER = new HashSet<>();
	private static final Comparator<Entity> ENTITY_SORTER = (entity1, entity2) -> {
		EntityPlayer player = Minecraft.getMinecraft().player;
		double distance1 = player.getDistanceSq(entity1);
		double distance2 = player.getDistanceSq(entity2);
		if (distance1 < distance2) {
			return -1;
		}
		if (distance1 > distance2) {
			return 1;
		}
		return 0;
	};

	@SubscribeEvent
	public static void onRenderTickEvent(TickEvent.RenderTickEvent event) {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}
		shouldRenderAll = true;
		if (!ENTITIES_TO_RENDER.isEmpty()) {
			ENTITIES_TO_RENDER.clear();
		}
		if (CQRMain.isEntityCullingInstalled) {
			return;
		}
		if (!CQRConfig.advanced.limitEntityRendering) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.world == null) {
			return;
		}
		List<AbstractEntityCQR> list = new ArrayList<>();
		for (RenderChunk renderChunk : getRenderChunks()) {
			BlockPos pos = renderChunk.getPosition();
			Chunk chunk = mc.world.getChunk(pos);
			for (Entity entity : chunk.getEntityLists()[pos.getY() >> 4]) {
				if (!(entity instanceof AbstractEntityCQR)) {
					continue;
				}
				if (!entity.isNonBoss()) {
					continue;
				}
				list.add((AbstractEntityCQR) entity);
			}
		}
		if (list.size() >= CQRConfig.advanced.limitEntityRenderingCount) {
			shouldRenderAll = false;
			list.sort(ENTITY_SORTER);
			for (int i = 0; i < CQRConfig.advanced.limitEntityRenderingCount; i++) {
				ENTITIES_TO_RENDER.add(list.get(i));
			}
		}
	}

	public static boolean shouldEntityBeRendered(AbstractEntityCQR entity) {
		if (CQRMain.isEntityCullingInstalled) {
			return true;
		}
		if (!entity.isNonBoss()) {
			return true;
		}
		if (CQRConfig.advanced.limitEntityRendering && !shouldRenderAll && !ENTITIES_TO_RENDER.contains(entity)) {
			return false;
		}
		if (!CQRConfig.advanced.skipHiddenEntityRendering) {
			return true;
		}
		int maxDiff = CQRConfig.advanced.skipHiddenEntityRenderingDiff * CQRConfig.advanced.skipHiddenEntityRenderingDiff;
		Minecraft mc = Minecraft.getMinecraft();
		Vec3d start = mc.player.getPositionEyes(mc.getRenderPartialTicks());
		Vec3d end = entity.getPositionEyes(mc.getRenderPartialTicks());
		if (start.squareDistanceTo(end) <= maxDiff) {
			return true;
		}
		RayTraceResult result1 = rayTraceBlocks(mc.world, start, end, false, true);
		if (result1 == null || result1.hitVec.squareDistanceTo(end) <= maxDiff) {
			return true;
		}
		RayTraceResult result2 = rayTraceBlocks(mc.world, end, start, false, true);
		if (result2 == null) {
			return true;
		}
		return result1.hitVec.squareDistanceTo(result2.hitVec) <= maxDiff;
	}

	@Nullable
	private static RayTraceResult rayTraceBlocks(World world, Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox) {
		if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z)) {
			if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
				int i = MathHelper.floor(vec32.x);
				int j = MathHelper.floor(vec32.y);
				int k = MathHelper.floor(vec32.z);
				int l = MathHelper.floor(vec31.x);
				int i1 = MathHelper.floor(vec31.y);
				int j1 = MathHelper.floor(vec31.z);
				BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos(l, i1, j1);
				IBlockState iblockstate = world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();

				if (iblockstate.isOpaqueCube() && iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB && block.canCollideCheck(iblockstate, stopOnLiquid)) {
					RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, vec31, vec32);

					if (raytraceresult != null) {
						return raytraceresult;
					}
				}

				int k1 = 200;
				double x = vec31.x;
				double y = vec31.y;
				double z = vec31.z;

				while (k1-- >= 0) {
					if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
						return null;
					}

					if (l == i && i1 == j && j1 == k) {
						return null;
					}

					boolean flag2 = true;
					boolean flag = true;
					boolean flag1 = true;
					double d0 = 999.0D;
					double d1 = 999.0D;
					double d2 = 999.0D;

					if (i > l) {
						d0 = (double) l + 1.0D;
					} else if (i < l) {
						d0 = (double) l + 0.0D;
					} else {
						flag2 = false;
					}

					if (j > i1) {
						d1 = (double) i1 + 1.0D;
					} else if (j < i1) {
						d1 = (double) i1 + 0.0D;
					} else {
						flag = false;
					}

					if (k > j1) {
						d2 = (double) j1 + 1.0D;
					} else if (k < j1) {
						d2 = (double) j1 + 0.0D;
					} else {
						flag1 = false;
					}

					double d3 = 999.0D;
					double d4 = 999.0D;
					double d5 = 999.0D;
					double d6 = vec32.x - x;
					double d7 = vec32.y - y;
					double d8 = vec32.z - z;

					if (flag2) {
						d3 = (d0 - x) / d6;
					}

					if (flag) {
						d4 = (d1 - y) / d7;
					}

					if (flag1) {
						d5 = (d2 - z) / d8;
					}

					if (d3 == -0.0D) {
						d3 = -1.0E-4D;
					}

					if (d4 == -0.0D) {
						d4 = -1.0E-4D;
					}

					if (d5 == -0.0D) {
						d5 = -1.0E-4D;
					}

					EnumFacing enumfacing;

					if (d3 < d4 && d3 < d5) {
						enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
						x = d0;
						y = y + d7 * d3;
						z = z + d8 * d3;
					} else if (d4 < d5) {
						enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
						x = x + d6 * d4;
						y = d1;
						z = z + d8 * d4;
					} else {
						enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
						x = x + d6 * d5;
						y = y + d7 * d5;
						z = d2;
					}

					l = MathHelper.floor(x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
					i1 = MathHelper.floor(y) - (enumfacing == EnumFacing.UP ? 1 : 0);
					j1 = MathHelper.floor(z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
					blockpos.setPos(l, i1, j1);
					iblockstate = world.getBlockState(blockpos);
					block = iblockstate.getBlock();

					if (iblockstate.isOpaqueCube() && iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB && block.canCollideCheck(iblockstate, stopOnLiquid)) {
						RayTraceResult raytraceresult1 = iblockstate.collisionRayTrace(world, blockpos, new Vec3d(x, y, z), vec32);

						if (raytraceresult1 != null) {
							return raytraceresult1;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
