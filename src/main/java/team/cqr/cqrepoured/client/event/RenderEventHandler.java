package team.cqr.cqrepoured.client.event;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.armor.ModelCrown;
import team.cqr.cqrepoured.client.render.MagicBellRenderer;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.item.ItemHookshotBase;
import team.cqr.cqrepoured.item.ItemUnprotectedPositionTool;
import team.cqr.cqrepoured.item.armor.ItemCrown;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemMusketKnife;
import team.cqr.cqrepoured.item.gun.ItemRevolver;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT)
public class RenderEventHandler {

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
		Item itemMain = event.getEntityPlayer().getHeldItemMainhand().getItem();
		Item itemOff = event.getEntityPlayer().getHeldItemOffhand().getItem();

		if (itemMain instanceof ItemRevolver
				|| itemOff instanceof ItemRevolver
				|| itemOff instanceof ItemMusketKnife
				|| itemMain instanceof ItemMusketKnife
				|| itemMain instanceof ItemHookshotBase
				|| itemOff instanceof ItemHookshotBase) {
			GlStateManager.pushMatrix();
		}

		if (itemMain instanceof ItemMusket || itemMain instanceof ItemMusketKnife) {
			if (event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
			}
		} else if (itemMain instanceof ItemRevolver || itemMain instanceof ItemHookshotBase) {
			if (event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT) {
				event.getRenderer().getMainModel().bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90));
			} else {
				event.getRenderer().getMainModel().bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
			}
		}
		if (itemOff instanceof ItemMusket || itemOff instanceof ItemMusketKnife) {
			if ((event.getEntityPlayer().getPrimaryHand() != EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
			}
		} else if (itemOff instanceof ItemRevolver || itemOff instanceof ItemHookshotBase) {
			if ((event.getEntityPlayer().getPrimaryHand() != EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90));
			} else {
				event.getRenderer().getMainModel().bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
			}
		}
	}

	@SubscribeEvent
	public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
		Item itemMain = event.getEntityPlayer().getHeldItemMainhand().getItem();
		Item itemOff = event.getEntityPlayer().getHeldItemOffhand().getItem();
		if (itemMain instanceof ItemRevolver && (!(itemMain instanceof ItemMusket) && !(itemMain instanceof ItemMusketKnife))) {
			if (event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT) {
				event.getRenderer().getMainModel().bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90));
				event.getRenderer().getMainModel().bipedLeftArm.postRender(1F);
			} else {
				event.getRenderer().getMainModel().bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
				event.getRenderer().getMainModel().bipedRightArm.postRender(1F);
			}
		} else if (itemMain instanceof ItemRevolver || itemMain instanceof ItemHookshotBase) {
			if ((event.getEntityPlayer().getPrimaryHand() != EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
				event.getRenderer().getMainModel().bipedLeftArm.postRender(1F);
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
				event.getRenderer().getMainModel().bipedRightArm.postRender(1F);
			}
		}
		if (itemOff instanceof ItemRevolver && (!(itemOff instanceof ItemMusket) && !(itemOff instanceof ItemMusketKnife))) {
			if ((event.getEntityPlayer().getPrimaryHand() != EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90));
				event.getRenderer().getMainModel().bipedLeftArm.postRender(1F);
			} else {
				event.getRenderer().getMainModel().bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
				event.getRenderer().getMainModel().bipedRightArm.postRender(1F);
			}
		} else if (itemOff instanceof ItemRevolver || itemOff instanceof ItemHookshotBase) {
			if ((event.getEntityPlayer().getPrimaryHand() != EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
				event.getRenderer().getMainModel().bipedLeftArm.postRender(1F);
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
				event.getRenderer().getMainModel().bipedRightArm.postRender(1F);
			}
		}

		if (itemMain instanceof ItemRevolver
				|| itemOff instanceof ItemRevolver
				|| itemOff instanceof ItemMusketKnife
				|| itemMain instanceof ItemMusketKnife
				|| itemMain instanceof ItemHookshotBase
				|| itemOff instanceof ItemHookshotBase) {
			GlStateManager.popMatrix();
		}
	}

	@SubscribeEvent
	public static void onRenderLivingEvent(RenderLivingEvent.Post<EntityLivingBase> event) {
		RenderLivingBase<EntityLivingBase> renderer = event.getRenderer();
		EntityLivingBase entity = event.getEntity();
		double x = event.getX();
		double y = event.getY();
		double z = event.getZ();
		float partialTicks = event.getPartialRenderTick();
		ModelBase entityModel = renderer.getMainModel();
		ItemStack helmet = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

		if (ItemCrown.hasCrown(helmet)) {
			ItemStack crown = new ItemStack(helmet.getTagCompound().getCompoundTag(ItemCrown.NBT_KEY_CROWN));
			ModelBiped model = crown.getItem().getArmorModel(entity, crown, EntityEquipmentSlot.HEAD, null);

			if (model != null) {
				/*
				 * Copied from RenderLivingBase with some adjustments
				 */
				if (model instanceof ModelCrown && entityModel instanceof ModelBiped) {
					GlStateManager.pushMatrix();
					GlStateManager.disableCull();
					model.setModelAttributes(entityModel);
					float f = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
					float f1 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
					float f2 = f1 - f;

					if (model.isRiding && entity.getRidingEntity() instanceof EntityLivingBase) {
						EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
						f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
						f2 = f1 - f;
						float f3 = MathHelper.wrapDegrees(f2);

						if (f3 < -85.0F) {
							f3 = -85.0F;
						}

						if (f3 >= 85.0F) {
							f3 = 85.0F;
						}

						f = f1 - f3;

						if (f3 * f3 > 2500.0F) {
							f += f3 * 0.2F;
						}

						f2 = f1 - f;
					}
					GlStateManager.translate(x, y, z);
					float f8 = entity.ticksExisted + partialTicks;
					applyRotations(entity, f8, f, partialTicks);
					renderer.prepareScale(entity, partialTicks);
					GlStateManager.enableAlpha();
					if (renderer instanceof RenderCQREntity
							&& model.isRiding
							&& entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemCrown) {
						GlStateManager.translate(0.0F, 0.6F, 0.0F);
					}
					GlStateManager.depthMask(true);

					ResourceLocation rs = new ResourceLocation(CQRMain.MODID, "textures/models/armor/king_crown_layer_1.png");
					Minecraft.getMinecraft().getTextureManager().bindTexture(rs);
					((ModelCrown) model).render(entity, 0.0625F, renderer, (ModelBiped) entityModel);

					GlStateManager.disableRescaleNormal();
					GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
					GlStateManager.enableTexture2D();
					GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
					GlStateManager.enableCull();
					GlStateManager.popMatrix();
				} else {
					GlStateManager.pushMatrix();
					GlStateManager.disableCull();

					boolean shouldSit = entityModel.isRiding;
					float f = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
					float f1 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
					float f2 = f1 - f;

					if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
						EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
						f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
						f2 = f1 - f;
						float f3 = MathHelper.wrapDegrees(f2);

						if (f3 < -85.0F) {
							f3 = -85.0F;
						}

						if (f3 >= 85.0F) {
							f3 = 85.0F;
						}

						f = f1 - f3;

						if (f3 * f3 > 2500.0F) {
							f += f3 * 0.2F;
						}

						f2 = f1 - f;
					}

					float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
					GlStateManager.translate(x, y, z);
					float f8 = entity.ticksExisted + partialTicks;
					applyRotations(entity, f8, f, partialTicks);
					float f4 = renderer.prepareScale(entity, partialTicks);
					float f5 = 0.0F;
					float f6 = 0.0F;

					if (!entity.isRiding()) {
						f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
						f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

						if (entity.isChild()) {
							f6 *= 3.0F;
						}

						if (f5 > 1.0F) {
							f5 = 1.0F;
						}
						f2 = f1 - f; // Forge: Fix MC-1207
					}

					GlStateManager.enableAlpha();
					GlStateManager.depthMask(true);
					model.setModelAttributes(entityModel);
					model.setLivingAnimations(entity, f6, f5, partialTicks);

					ResourceLocation rs = new ResourceLocation(CQRMain.MODID, "textures/models/armor/king_crown_layer_1.png");
					Minecraft.getMinecraft().getTextureManager().bindTexture(rs);
					model.render(entity, f6, f5, f8, f2, f7, f4);

					GlStateManager.disableRescaleNormal();
					GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
					GlStateManager.enableTexture2D();
					GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
					GlStateManager.enableCull();
					GlStateManager.popMatrix();
				}
			}
		}
	}

	/**
	 * Copied from {@link RenderLivingBase}
	 */
	protected static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f = yawOffset - prevYawOffset;

		while (f < -180.0F) {
			f += 360.0F;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return prevYawOffset + partialTicks * f;
	}

	/**
	 * Copied from {@link RenderLivingBase}
	 */
	protected static void applyRotations(EntityLivingBase entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
		GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);

		if (entityLiving.deathTime > 0) {
			float f = (entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);

			if (f > 1.0F) {
				f = 1.0F;
			}

			GlStateManager.rotate(f * 90.0F, 0.0F, 0.0F, 1.0F);
		} else {
			String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());

			if (s != null
					&& ("Dinnerbone".equals(s) || "Grumm".equals(s))
					&& (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer) entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
				GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		for (EnumHand hand : EnumHand.values()) {
			ItemStack stack = mc.player.getHeldItem(hand);
			if (!(stack.getItem() instanceof ItemUnprotectedPositionTool)) {
				continue;
			}
			ItemUnprotectedPositionTool item = (ItemUnprotectedPositionTool) stack.getItem();

			double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * event.getPartialTicks();
			double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * event.getPartialTicks();
			double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * event.getPartialTicks();
			double d1 = 1.0D / 1024.0D;
			double d2 = 1.0D + d1;
			double d3 = 1.0D / 512.0D;
			double d4 = 1.0D + d3;

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();

			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.disableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

			GlStateManager.color(0.0F, 0.0F, 1.0F, 0.5F);
			bufferbuilder.setTranslation(-x, -y, -z);
			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			item.getPositions(stack).forEach(pos -> {
				renderBox(bufferbuilder, pos.getX() - d1, pos.getY() - d1, pos.getZ() - d1, pos.getX() + d2, pos.getY() + d2, pos.getZ() + d2);
			});
			tessellator.draw();
			GlStateManager.color(0.0F, 0.0F, 1.0F, 1.0F);
			bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
			item.getPositions(stack).forEach(pos -> {
				renderBoxOutline(bufferbuilder, pos.getX() - d3, pos.getY() - d3, pos.getZ() - d3, pos.getX() + d4, pos.getY() + d4, pos.getZ() + d4);
			});
			tessellator.draw();
			bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.enableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.glLineWidth(1.0F);
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
		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x1, y1, z1).endVertex();

		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x1, y2, z2).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();

		buffer.pos(x1, y2, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();
		buffer.pos(x1, y2, z2).endVertex();
	}

	public static void renderBox(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2) {
		// down
		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x1, y1, z2).endVertex();

		// south
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
		buffer.pos(x1, y2, z2).endVertex();

		// north
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();
		buffer.pos(x2, y2, z1).endVertex();

		// up
		buffer.pos(x1, y2, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();

		// west
		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x1, y2, z2).endVertex();
		buffer.pos(x1, y2, z1).endVertex();

		// east
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
	}

}
