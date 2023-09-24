package team.cqr.cqrepoured.client.event;

import static net.minecraft.client.model.HumanoidModel.ArmPose.BOW_AND_ARROW;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.render.tileentity.TileEntityExporterRenderer;
import team.cqr.cqrepoured.item.ItemHookshotBase;
import team.cqr.cqrepoured.item.ItemUnprotectedPositionTool;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemMusketKnife;
import team.cqr.cqrepoured.item.gun.ItemRevolver;

@EventBusSubscriber(modid = CQRConstants.MODID, value = Dist.CLIENT)
public class RenderEventHandler {

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
		Player player = event.getEntity();
		Item itemMain = player.getMainHandItem().getItem();
		Item itemOff = player.getOffhandItem().getItem();

		if (itemMain instanceof ItemRevolver || itemOff instanceof ItemRevolver || itemOff instanceof ItemMusketKnife || itemMain instanceof ItemMusketKnife || itemMain instanceof ItemHookshotBase || itemOff instanceof ItemHookshotBase) {
			event.getPoseStack().pushPose();
		}

		if (itemMain instanceof ItemMusket || itemMain instanceof ItemMusketKnife) {
			if (player.getMainArm() == HumanoidArm.LEFT) {
				event.getRenderer().getModel().leftArmPose = BOW_AND_ARROW;
			} else {
				event.getRenderer().getModel().rightArmPose = BOW_AND_ARROW;
			}
		} else if (itemMain instanceof ItemRevolver || itemMain instanceof ItemHookshotBase) {
			if (player.getMainArm() == HumanoidArm.LEFT) {
				event.getRenderer().getModel().leftArm.xRot -= new Float(Math.toRadians(90));
			} else {
				event.getRenderer().getModel().rightArm.xRot -= new Float(Math.toRadians(90));
			}
		}
		if (itemOff instanceof ItemMusket || itemOff instanceof ItemMusketKnife) {
			if ((player.getMainArm() != HumanoidArm.LEFT)) {
				event.getRenderer().getModel().leftArmPose = BOW_AND_ARROW;
			} else {
				event.getRenderer().getModel().rightArmPose = BOW_AND_ARROW;
			}
		} else if (itemOff instanceof ItemRevolver || itemOff instanceof ItemHookshotBase) {
			if ((player.getMainArm() != HumanoidArm.LEFT)) {
				event.getRenderer().getModel().leftArm.xRot -= new Float(Math.toRadians(90));
			} else {
				event.getRenderer().getModel().rightArm.xRot -= new Float(Math.toRadians(90));
			}
		}
	}

	@SubscribeEvent
	public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
		Player player = event.getEntity();
		Item itemMain = player.getMainHandItem().getItem();
		Item itemOff = player.getOffhandItem().getItem();
		if (itemMain instanceof ItemRevolver && (!(itemMain instanceof ItemMusket) && !(itemMain instanceof ItemMusketKnife))) {
			if (player.getMainArm() == HumanoidArm.LEFT) {
				event.getRenderer().getModel().leftArm.xRot -= new Float(Math.toRadians(90));
//				event.getRenderer().getModel().leftArm.postRender(1F);
			} else {
				event.getRenderer().getModel().rightArm.xRot -= new Float(Math.toRadians(90));
//				event.getRenderer().getModel().rightArm.postRender(1F);
			}
		} else if (itemMain instanceof ItemRevolver || itemMain instanceof ItemHookshotBase) {
			if ((player.getMainArm() != HumanoidArm.LEFT)) {
				event.getRenderer().getModel().leftArmPose = BOW_AND_ARROW;
//				event.getRenderer().getModel().leftArm.postRender(1F);
			} else {
				event.getRenderer().getModel().rightArmPose = BOW_AND_ARROW;
//				event.getRenderer().getModel().rightArm.postRender(1F);
			}
		}
		if (itemOff instanceof ItemRevolver && (!(itemOff instanceof ItemMusket) && !(itemOff instanceof ItemMusketKnife))) {
			if ((player.getMainArm() != HumanoidArm.LEFT)) {
				event.getRenderer().getModel().leftArm.xRot -= new Float(Math.toRadians(90));
//				event.getRenderer().getModel().leftArm.postRender(1F);
			} else {
				event.getRenderer().getModel().rightArm.xRot -= new Float(Math.toRadians(90));
//				event.getRenderer().getModel().rightArm.postRender(1F);
			}
		} else if (itemOff instanceof ItemRevolver || itemOff instanceof ItemHookshotBase) {
			if ((player.getMainArm() != HumanoidArm.LEFT)) {
				event.getRenderer().getModel().leftArmPose = BOW_AND_ARROW;
//				event.getRenderer().getModel().leftArm.postRender(1F);
			} else {
				event.getRenderer().getModel().rightArmPose = BOW_AND_ARROW;
//				event.getRenderer().getModel().rightArm.postRender(1F);
			}
		}

		if (itemMain instanceof ItemRevolver || itemOff instanceof ItemRevolver || itemOff instanceof ItemMusketKnife || itemMain instanceof ItemMusketKnife || itemMain instanceof ItemHookshotBase || itemOff instanceof ItemHookshotBase) {
			event.getPoseStack().popPose();
		}
	}

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderLevelStageEvent event) {
		if (!event.getStage().equals(Stage.AFTER_LEVEL)) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		for (InteractionHand hand : InteractionHand.values()) {
			ItemStack stack = mc.player.getItemInHand(hand);
			if (!(stack.getItem() instanceof ItemUnprotectedPositionTool)) {
				continue;
			}
			ItemUnprotectedPositionTool item = (ItemUnprotectedPositionTool) stack.getItem();

			PoseStack matrixStack = event.getPoseStack();
			double x = mc.gameRenderer.getMainCamera().getPosition().x;
			double y = mc.gameRenderer.getMainCamera().getPosition().y;
			double z = mc.gameRenderer.getMainCamera().getPosition().z;
			double d1 = 1.0D / 1024.0D;
			double d2 = 1.0D + d1;
			double d3 = 1.0D / 512.0D;
			double d4 = 1.0D + d3;
			
			VertexConsumer vertexBuilder = mc.renderBuffers().bufferSource().getBuffer(CQRRenderTypes.overlayQuads());
			item.getPositions(stack).forEach(pos -> {
				double dx = pos.getX() - x;
				double dy = pos.getY() - y;
				double dz = pos.getZ() - z;
				TileEntityExporterRenderer.renderBox(matrixStack, vertexBuilder, dx - d1, dy - d1, dz - d1, dx + d2, dy + d2, dz + d2, 0.0F, 0.0F, 1.0F, 0.5F);
			});
			
			VertexConsumer vertexBuilder1 = mc.renderBuffers().bufferSource().getBuffer(CQRRenderTypes.overlayLines());
			item.getPositions(stack).forEach(pos -> {
				double dx = pos.getX() - x;
				double dy = pos.getY() - y;
				double dz = pos.getZ() - z;
				TileEntityExporterRenderer.renderBox(matrixStack, vertexBuilder1, dx - d3, dy - d3, dz - d3, dx + d4, dy + d4, dz + d4, 0.0F, 0.0F, 1.0F, 1.0F);
			});

			mc.renderBuffers().bufferSource().endBatch(CQRRenderTypes.overlayLines());
		}

		//MagicBellRenderer.getInstance().render(event.getPartialTicks());
	}

	@SubscribeEvent
	public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			//MagicBellRenderer.getInstance().tick();
		}
	}

}
