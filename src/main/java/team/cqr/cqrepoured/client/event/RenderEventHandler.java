package team.cqr.cqrepoured.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.cqr.cqrepoured.client.render.MagicBellRenderer;
import team.cqr.cqrepoured.item.ItemHookshotBase;
import team.cqr.cqrepoured.item.ItemUnprotectedPositionTool;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemMusketKnife;
import team.cqr.cqrepoured.item.gun.ItemRevolver;

import static net.minecraft.client.renderer.entity.model.BipedModel.ArmPose.BOW_AND_ARROW;

@OnlyIn(Dist.CLIENT)
//@EventBusSubscriber(modid = CQRMain.MODID, value = Dist.CLIENT)
public class RenderEventHandler {

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
		PlayerEntity player = event.getPlayer();
		Item itemMain = player.getMainHandItem().getItem();
		Item itemOff = player.getOffhandItem().getItem();

		if (itemMain instanceof ItemRevolver || itemOff instanceof ItemRevolver || itemOff instanceof ItemMusketKnife || itemMain instanceof ItemMusketKnife || itemMain instanceof ItemHookshotBase || itemOff instanceof ItemHookshotBase) {
			event.getMatrixStack().pushPose();
		}

		if (itemMain instanceof ItemMusket || itemMain instanceof ItemMusketKnife) {
			if (player.getMainArm() == HandSide.LEFT) {
				event.getRenderer().getModel().leftArmPose = BOW_AND_ARROW;
			} else {
				event.getRenderer().getModel().rightArmPose = BOW_AND_ARROW;
			}
		} else if (itemMain instanceof ItemRevolver || itemMain instanceof ItemHookshotBase) {
			if (player.getMainArm() == HandSide.LEFT) {
				event.getRenderer().getModel().leftArm.xRot -= new Float(Math.toRadians(90));
			} else {
				event.getRenderer().getModel().rightArm.xRot -= new Float(Math.toRadians(90));
			}
		}
		if (itemOff instanceof ItemMusket || itemOff instanceof ItemMusketKnife) {
			if ((player.getMainArm() != HandSide.LEFT)) {
				event.getRenderer().getModel().leftArmPose = BOW_AND_ARROW;
			} else {
				event.getRenderer().getModel().rightArmPose = BOW_AND_ARROW;
			}
		} else if (itemOff instanceof ItemRevolver || itemOff instanceof ItemHookshotBase) {
			if ((player.getMainArm() != HandSide.LEFT)) {
				event.getRenderer().getModel().leftArm.xRot -= new Float(Math.toRadians(90));
			} else {
				event.getRenderer().getModel().rightArm.xRot -= new Float(Math.toRadians(90));
			}
		}
	}

	@SubscribeEvent
	public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
		PlayerEntity player = event.getPlayer();
		Item itemMain = player.getMainHandItem().getItem();
		Item itemOff = player.getOffhandItem().getItem();
		if (itemMain instanceof ItemRevolver && (!(itemMain instanceof ItemMusket) && !(itemMain instanceof ItemMusketKnife))) {
			if (player.getMainArm() == HandSide.LEFT) {
				event.getRenderer().getModel().leftArm.xRot -= new Float(Math.toRadians(90));
//				event.getRenderer().getModel().leftArm.postRender(1F);
			} else {
				event.getRenderer().getModel().rightArm.xRot -= new Float(Math.toRadians(90));
//				event.getRenderer().getModel().rightArm.postRender(1F);
			}
		} else if (itemMain instanceof ItemRevolver || itemMain instanceof ItemHookshotBase) {
			if ((player.getMainArm() != HandSide.LEFT)) {
				event.getRenderer().getModel().leftArmPose = BOW_AND_ARROW;
//				event.getRenderer().getModel().leftArm.postRender(1F);
			} else {
				event.getRenderer().getModel().rightArmPose = BOW_AND_ARROW;
//				event.getRenderer().getModel().rightArm.postRender(1F);
			}
		}
		if (itemOff instanceof ItemRevolver && (!(itemOff instanceof ItemMusket) && !(itemOff instanceof ItemMusketKnife))) {
			if ((player.getMainArm() != HandSide.LEFT)) {
				event.getRenderer().getModel().leftArm.xRot -= new Float(Math.toRadians(90));
//				event.getRenderer().getModel().leftArm.postRender(1F);
			} else {
				event.getRenderer().getModel().rightArm.xRot -= new Float(Math.toRadians(90));
//				event.getRenderer().getModel().rightArm.postRender(1F);
			}
		} else if (itemOff instanceof ItemRevolver || itemOff instanceof ItemHookshotBase) {
			if ((player.getMainArm() != HandSide.LEFT)) {
				event.getRenderer().getModel().leftArmPose = BOW_AND_ARROW;
//				event.getRenderer().getModel().leftArm.postRender(1F);
			} else {
				event.getRenderer().getModel().rightArmPose = BOW_AND_ARROW;
//				event.getRenderer().getModel().rightArm.postRender(1F);
			}
		}

		if (itemMain instanceof ItemRevolver || itemOff instanceof ItemRevolver || itemOff instanceof ItemMusketKnife || itemMain instanceof ItemMusketKnife || itemMain instanceof ItemHookshotBase || itemOff instanceof ItemHookshotBase) {
			event.getMatrixStack().popPose();
		}
	}

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getInstance();
		for (Hand hand : Hand.values()) {
			ItemStack stack = mc.player.getItemInHand(hand);
			if (!(stack.getItem() instanceof ItemUnprotectedPositionTool)) {
				continue;
			}
			ItemUnprotectedPositionTool item = (ItemUnprotectedPositionTool) stack.getItem();

//			double x = mc.player.lastTickPosX + (mc.player.position().x - mc.player.lastTickPosX) * event.getPartialTicks();
//			double y = mc.player.lastTickPosY + (mc.player.position().y - mc.player.lastTickPosY) * event.getPartialTicks();
//			double z = mc.player.lastTickPosZ + (mc.player.position().z - mc.player.lastTickPosZ) * event.getPartialTicks();
//			double d1 = 1.0D / 1024.0D;
//			double d2 = 1.0D + d1;
//			double d3 = 1.0D / 512.0D;
//			double d4 = 1.0D + d3;
//
//			Tessellator tessellator = Tessellator.getInstance();
//			BufferBuilder bufferbuilder = tessellator.getBuffer();
//
//			GlStateManager.glLineWidth(2.0F);
//			GlStateManager.disableTexture2D();
//			GlStateManager.depthMask(false);
//			GlStateManager.enableBlend();
//			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
//			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
//			GlStateManager.disableTexture2D();
//			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
//
//			GlStateManager.color(0.0F, 0.0F, 1.0F, 0.5F);
//			bufferbuilder.setTranslation(-x, -y, -z);
//			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
//			item.getPositions(stack).forEach(pos -> {
//				renderBox(bufferbuilder, pos.getX() - d1, pos.getY() - d1, pos.getZ() - d1, pos.getX() + d2, pos.getY() + d2, pos.getZ() + d2);
//			});
//			tessellator.end();
//			GlStateManager.color(0.0F, 0.0F, 1.0F, 1.0F);
//			bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
//			item.getPositions(stack).forEach(pos -> {
//				renderBoxOutline(bufferbuilder, pos.getX() - d3, pos.getY() - d3, pos.getZ() - d3, pos.getX() + d4, pos.getY() + d4, pos.getZ() + d4);
//			});
//			tessellator.end();
//			bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
//			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//
//			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
//			GlStateManager.enableTexture2D();
//			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
//			GlStateManager.disableBlend();
//			GlStateManager.depthMask(true);
//			GlStateManager.enableTexture2D();
//			GlStateManager.glLineWidth(1.0F);
		}

		MagicBellRenderer.getInstance().render(event.getPartialTicks());
	}

	@SubscribeEvent
	public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			MagicBellRenderer.getInstance().tick();
		}
	}

	public static void renderBoxOutline(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2) {
		buffer.vertex(x1, y1, z1).endVertex();
		buffer.vertex(x2, y1, z1).endVertex();
		buffer.vertex(x2, y1, z1).endVertex();
		buffer.vertex(x2, y1, z2).endVertex();
		buffer.vertex(x2, y1, z2).endVertex();
		buffer.vertex(x1, y1, z2).endVertex();
		buffer.vertex(x1, y1, z2).endVertex();
		buffer.vertex(x1, y1, z1).endVertex();

		buffer.vertex(x1, y1, z1).endVertex();
		buffer.vertex(x1, y2, z1).endVertex();
		buffer.vertex(x1, y1, z2).endVertex();
		buffer.vertex(x1, y2, z2).endVertex();
		buffer.vertex(x2, y1, z1).endVertex();
		buffer.vertex(x2, y2, z1).endVertex();
		buffer.vertex(x2, y1, z2).endVertex();
		buffer.vertex(x2, y2, z2).endVertex();

		buffer.vertex(x1, y2, z2).endVertex();
		buffer.vertex(x2, y2, z2).endVertex();
		buffer.vertex(x2, y2, z2).endVertex();
		buffer.vertex(x2, y2, z1).endVertex();
		buffer.vertex(x2, y2, z1).endVertex();
		buffer.vertex(x1, y2, z1).endVertex();
		buffer.vertex(x1, y2, z1).endVertex();
		buffer.vertex(x1, y2, z2).endVertex();
	}

	public static void renderBox(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2) {
		// down
		buffer.vertex(x1, y1, z1).endVertex();
		buffer.vertex(x2, y1, z1).endVertex();
		buffer.vertex(x2, y1, z2).endVertex();
		buffer.vertex(x1, y1, z2).endVertex();

		// south
		buffer.vertex(x1, y1, z2).endVertex();
		buffer.vertex(x2, y1, z2).endVertex();
		buffer.vertex(x2, y2, z2).endVertex();
		buffer.vertex(x1, y2, z2).endVertex();

		// north
		buffer.vertex(x2, y1, z1).endVertex();
		buffer.vertex(x1, y1, z1).endVertex();
		buffer.vertex(x1, y2, z1).endVertex();
		buffer.vertex(x2, y2, z1).endVertex();

		// up
		buffer.vertex(x1, y2, z2).endVertex();
		buffer.vertex(x2, y2, z2).endVertex();
		buffer.vertex(x2, y2, z1).endVertex();
		buffer.vertex(x1, y2, z1).endVertex();

		// west
		buffer.vertex(x1, y1, z1).endVertex();
		buffer.vertex(x1, y1, z2).endVertex();
		buffer.vertex(x1, y2, z2).endVertex();
		buffer.vertex(x1, y2, z1).endVertex();

		// east
		buffer.vertex(x2, y1, z2).endVertex();
		buffer.vertex(x2, y1, z1).endVertex();
		buffer.vertex(x2, y2, z1).endVertex();
		buffer.vertex(x2, y2, z2).endVertex();
	}

}
