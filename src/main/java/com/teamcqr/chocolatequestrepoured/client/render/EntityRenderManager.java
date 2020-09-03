package com.teamcqr.chocolatequestrepoured.client.render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionField;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class EntityRenderManager {

	private static boolean shouldRenderAll = false;
	private static final List<AbstractEntityCQR> ENTITIES_TO_RENDER = new ArrayList<>();
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
	private static ReflectionField<List<?>> renderInfos = new ReflectionField<>(RenderGlobal.class, "field_72755_R", "renderInfos");
	private static ReflectionField<RenderChunk> renderChunk = new ReflectionField<>("net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation", "field_178036_a", "renderChunk");

	public static List<RenderChunk> getRenderChunks() {
		List<RenderChunk> list = new ArrayList<>();
		for (Object object : renderInfos.get(Minecraft.getMinecraft().renderGlobal)) {
			list.add(renderChunk.get(object));
		}
		return list;
	}

	@SubscribeEvent
	public static void onRenderTickEvent(TickEvent.RenderTickEvent event) {
		if (event.phase == Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.world != null) {
				if (CQRConfig.advanced.limitEntityRendering) {
					List<AbstractEntityCQR> list = new ArrayList<>();
					for (RenderChunk renderChunk : EntityRenderManager.getRenderChunks()) {
						BlockPos pos = renderChunk.getPosition();
						Chunk chunk = mc.world.getChunk(pos);
						for (Entity entity : chunk.getEntityLists()[pos.getY() >> 4]) {
							if (entity instanceof AbstractEntityCQR && entity.isNonBoss()) {
								list.add((AbstractEntityCQR) entity);
							}
						}
					}
					if (list.size() >= CQRConfig.advanced.limitEntityRenderingCount) {
						list.sort(ENTITY_SORTER);
						for (int i = 0; i < CQRConfig.advanced.limitEntityRenderingCount; i++) {
							ENTITIES_TO_RENDER.add(list.get(i));
						}
					} else {
						shouldRenderAll = true;
					}
				} else {
					shouldRenderAll = true;
				}
			}
		} else if (event.phase == Phase.END) {
			ENTITIES_TO_RENDER.clear();
			shouldRenderAll = false;
		}
	}

	public static boolean shouldEntityBeRendered(AbstractEntityCQR entity) {
		if (!entity.isNonBoss()) {
			return true;
		}
		if (CQRConfig.advanced.limitEntityRendering && !shouldRenderAll && !ENTITIES_TO_RENDER.contains(entity)) {
			return false;
		}
		if (CQRConfig.advanced.skipHiddenEntityRendering) {
			int maxDiff = CQRConfig.advanced.skipHiddenEntityRenderingDiff * CQRConfig.advanced.skipHiddenEntityRenderingDiff;
			Minecraft mc = Minecraft.getMinecraft();
			Vec3d start = mc.player.getPositionEyes(mc.getRenderPartialTicks());
			Vec3d end = entity.getPositionEyes(mc.getRenderPartialTicks());
			RayTraceResult result1 = mc.world.rayTraceBlocks(start, end, false, true, false);
			if (result1 == null || result1.getBlockPos().distanceSq(MathHelper.floor(end.x), Math.floor(end.y), Math.floor(end.z)) <= maxDiff) {
				return true;
			}
			RayTraceResult result2 = mc.world.rayTraceBlocks(end, start, false, true, false);
			if (result2 == null) {
				return true;
			}
			BlockPos pos1 = result1.getBlockPos();
			BlockPos pos2 = result2.getBlockPos();
			return pos1.distanceSq(pos2) <= maxDiff;
			// return Math.abs(pos1.getX() - pos2.getX()) <= maxDiff && Math.abs(pos1.getY() - pos2.getY()) <= maxDiff && Math.abs(pos1.getZ() - pos2.getZ()) <= maxDiff;
		}
		return true;
	}

}
