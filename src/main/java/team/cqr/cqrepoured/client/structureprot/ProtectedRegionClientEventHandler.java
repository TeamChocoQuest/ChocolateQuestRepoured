package team.cqr.cqrepoured.client.structureprot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.util.Reference;

@Mod.EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class ProtectedRegionClientEventHandler {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/protection_zone.png");
	private static final Map<UUID, ProtectedRegionIndicator> PROTECTED_REGION_INDICATORS = new HashMap<>();
	private static int displayList;

	private ProtectedRegionClientEventHandler() {

	}

	public static void addOrResetProtectedRegionIndicator(ProtectedRegion protectedRegion, BlockPos pos, @Nullable EntityPlayerMP player) {
		if (protectedRegion.getWorld().isRemote) {
			ProtectedRegionIndicator protectedRegionIndicator = PROTECTED_REGION_INDICATORS.get(protectedRegion.getUuid());
			if (protectedRegionIndicator != null) {
				protectedRegionIndicator.resetLifeTime();
			} else {
				PROTECTED_REGION_INDICATORS.put(protectedRegion.getUuid(), new ProtectedRegionIndicator(protectedRegion));
			}

			World world = protectedRegion.getWorld();
			for (int i = 0; i < 4; i++) {
				double x = pos.getX() - 0.1D + 1.2D * world.rand.nextDouble();
				double y = pos.getY() - 0.1D + 1.2D * world.rand.nextDouble();
				double z = pos.getZ() - 0.1D + 1.2D * world.rand.nextDouble();
				world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, x, y, z, 0.0D, 0.0D, 0.0D);
			}
		} else if (player != null) {
			CQRMain.NETWORK.sendTo(new SPacketAddOrResetProtectedRegionIndicator(protectedRegion.getUuid(), pos), player);
		}
	}

	@SubscribeEvent
	public static void onClientWorldTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			return;
		}
		List<UUID> toRemove = new ArrayList<>();
		for (ProtectedRegionIndicator protectedRegionIndicator : PROTECTED_REGION_INDICATORS.values()) {
			if (protectedRegionIndicator.getLifeTime() <= 0) {
				toRemove.add(protectedRegionIndicator.getProtectedRegion().getUuid());
			} else {
				protectedRegionIndicator.update();
			}
		}
		for (UUID uuid : toRemove) {
			PROTECTED_REGION_INDICATORS.remove(uuid);
		}
	}

	@SubscribeEvent
	public static void onRenderWorld(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		float partialTicks = mc.getRenderPartialTicks();
		Entity entity = mc.getRenderViewEntity();
		double x = MathHelper.clampedLerp(entity.prevPosX, entity.posX, partialTicks);
		double y = MathHelper.clampedLerp(entity.prevPosY, entity.posY, partialTicks);
		double z = MathHelper.clampedLerp(entity.prevPosZ, entity.posZ, partialTicks);

		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		// GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableAlpha();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);

		mc.getTextureManager().bindTexture(TEXTURE);
		for (ProtectedRegionIndicator protectedRegionIndicator : PROTECTED_REGION_INDICATORS.values()) {
			BlockPos pos = protectedRegionIndicator.getProtectedRegion().getStartPos();
			render(protectedRegionIndicator, pos.getX() - x, pos.getY() - y, pos.getZ() - z, partialTicks);
		}

		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
	}

	private static void render(ProtectedRegionIndicator protectedRegionIndicator, double x, double y, double z, float partialTicks) {
		ProtectedRegion protectedRegion = protectedRegionIndicator.getProtectedRegion();
		BlockPos pos1 = protectedRegion.getStartPos();
		BlockPos pos2 = protectedRegion.getEndPos();
		int sizeX = pos2.getX() - pos1.getX() + 1;
		int sizeY = pos2.getY() - pos1.getY() + 1;
		int sizeZ = pos2.getZ() - pos1.getZ() + 1;

		GlStateManager.pushMatrix();

		float f1 = Minecraft.getMinecraft().world.getTotalWorldTime() + partialTicks;
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.loadIdentity();
		float f2 = f1 * 0.005F;
		float f3 = f1 * 0.005F;
		GlStateManager.translate(f2, f3, 0.0F);
		GlStateManager.scale(0.25F * sizeX, 0.25F * sizeY, 0.25F * sizeZ);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);

		GlStateManager.translate(x, y, z);
		GlStateManager.translate(-0.0078125F, -0.0078125F, -0.0078125F);
		GlStateManager.scale(1.015625F, 1.015625F, 1.015625F);
		GlStateManager.scale(1.0F * sizeX, 1.0F * sizeY, 1.0F * sizeZ);
		GlStateManager.color(1.0F, 1.0F, 1.0F, protectedRegionIndicator.getLifeTime() / 60.0F);
		GL11.glCallList(displayList);

		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);

		GlStateManager.popMatrix();
	}

	@SubscribeEvent
	public static void onModelRegistryEvent(ModelRegistryEvent event) {
		displayList = GLAllocation.generateDisplayLists(1);
		GlStateManager.glNewList(displayList, GL11.GL_COMPILE);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferbuilder.pos(1.0D, 0.0D, 1.0D);
		bufferbuilder.tex(1.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(1.0D, 0.0D, 0.0D);
		bufferbuilder.tex(0.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(1.0D, 1.0D, 0.0D);
		bufferbuilder.tex(0.0D, 1.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(1.0D, 1.0D, 1.0D);
		bufferbuilder.tex(1.0D, 1.0D);
		bufferbuilder.endVertex();

		bufferbuilder.pos(0.0D, 0.0D, 0.0D);
		bufferbuilder.tex(1.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 0.0D, 1.0D);
		bufferbuilder.tex(0.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 1.0D, 1.0D);
		bufferbuilder.tex(0.0D, 1.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 1.0D, 0.0D);
		bufferbuilder.tex(1.0D, 1.0D);
		bufferbuilder.endVertex();

		bufferbuilder.pos(1.0D, 0.0D, 1.0D);
		bufferbuilder.tex(1.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 0.0D, 1.0D);
		bufferbuilder.tex(0.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 0.0D, 0.0D);
		bufferbuilder.tex(0.0D, 1.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(1.0D, 0.0D, 0.0D);
		bufferbuilder.tex(1.0D, 1.0D);
		bufferbuilder.endVertex();

		bufferbuilder.pos(1.0D, 1.0D, 0.0D);
		bufferbuilder.tex(1.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 1.0D, 0.0D);
		bufferbuilder.tex(0.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 1.0D, 1.0D);
		bufferbuilder.tex(0.0D, 1.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(1.0D, 1.0D, 1.0D);
		bufferbuilder.tex(1.0D, 1.0D);
		bufferbuilder.endVertex();

		bufferbuilder.pos(1.0D, 0.0D, 0.0D);
		bufferbuilder.tex(1.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 0.0D, 0.0D);
		bufferbuilder.tex(0.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 1.0D, 0.0D);
		bufferbuilder.tex(0.0D, 1.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(1.0D, 1.0D, 0.0D);
		bufferbuilder.tex(1.0D, 1.0D);
		bufferbuilder.endVertex();

		bufferbuilder.pos(0.0D, 0.0D, 1.0D);
		bufferbuilder.tex(1.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(1.0D, 0.0D, 1.0D);
		bufferbuilder.tex(0.0D, 0.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(1.0D, 1.0D, 1.0D);
		bufferbuilder.tex(0.0D, 1.0D);
		bufferbuilder.endVertex();
		bufferbuilder.pos(0.0D, 1.0D, 1.0D);
		bufferbuilder.tex(1.0D, 1.0D);
		bufferbuilder.endVertex();

		tessellator.draw();
		GlStateManager.glEndList();
	}

}
