package com.teamcqr.chocolatequestrepoured.client;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemHookshotBase;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemCrown;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemMusket;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemMusketKnife;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemRevolver;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT)
public class RenderEventHandler {

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
		Item itemMain = event.getEntityPlayer().getHeldItemMainhand().getItem();
		Item itemOff = event.getEntityPlayer().getHeldItemOffhand().getItem();

		if (itemMain instanceof ItemRevolver || itemOff instanceof ItemRevolver || itemOff instanceof ItemMusketKnife || itemMain instanceof ItemMusketKnife || itemMain instanceof ItemHookshotBase || itemOff instanceof ItemHookshotBase) {
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
			if (!(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
			}
		} else if (itemOff instanceof ItemRevolver || itemOff instanceof ItemHookshotBase) {
			if (!(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT)) {
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
		if (itemMain instanceof ItemRevolver && !(itemMain instanceof ItemMusket || itemMain instanceof ItemMusketKnife)) {
			if (event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT) {
				event.getRenderer().getMainModel().bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90));
				event.getRenderer().getMainModel().bipedLeftArm.postRender(1F);
			} else {
				event.getRenderer().getMainModel().bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
				event.getRenderer().getMainModel().bipedRightArm.postRender(1F);
			}
		} else if (itemMain instanceof ItemRevolver || itemMain instanceof ItemHookshotBase) {
			if (!(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
				event.getRenderer().getMainModel().bipedLeftArm.postRender(1F);
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
				event.getRenderer().getMainModel().bipedRightArm.postRender(1F);
			}
		}
		if (itemOff instanceof ItemRevolver && !(itemOff instanceof ItemMusket || itemOff instanceof ItemMusketKnife)) {
			if (!(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90));
				event.getRenderer().getMainModel().bipedLeftArm.postRender(1F);
			} else {
				event.getRenderer().getMainModel().bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
				event.getRenderer().getMainModel().bipedRightArm.postRender(1F);
			}
		} else if (itemOff instanceof ItemRevolver || itemOff instanceof ItemHookshotBase) {
			if (!(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
				event.getRenderer().getMainModel().bipedLeftArm.postRender(1F);
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
				event.getRenderer().getMainModel().bipedRightArm.postRender(1F);
			}
		}

		if (itemMain instanceof ItemRevolver || itemOff instanceof ItemRevolver || itemOff instanceof ItemMusketKnife || itemMain instanceof ItemMusketKnife || itemMain instanceof ItemHookshotBase || itemOff instanceof ItemHookshotBase) {
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
			ItemStack crown = new ItemStack(helmet.getTagCompound().getCompoundTag("CQR Crown"));
			ModelBiped model = crown.getItem().getArmorModel(entity, crown, EntityEquipmentSlot.HEAD, null);
			// model = new ModelBiped(1.001F);

			if (model != null) {
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
				renderLivingAt(entity, x, y, z);
				float f8 = handleRotationFloat(entity, partialTicks);
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

				model.bipedHead.rotateAngleX = ((ModelBiped) entityModel).bipedHead.rotateAngleX;
				model.bipedHead.rotateAngleY = ((ModelBiped) entityModel).bipedHead.rotateAngleY;
				model.bipedHead.rotateAngleZ = ((ModelBiped) entityModel).bipedHead.rotateAngleZ;
				model.bipedHead.rotationPointX = ((ModelBiped) entityModel).bipedHead.rotationPointX;
				model.bipedHead.rotationPointY = ((ModelBiped) entityModel).bipedHead.rotationPointY;
				model.bipedHead.rotationPointZ = ((ModelBiped) entityModel).bipedHead.rotationPointZ;
				model.bipedHead.offsetX = ((ModelBiped) entityModel).bipedHead.offsetX;
				model.bipedHead.offsetY = ((ModelBiped) entityModel).bipedHead.offsetY;
				model.bipedHead.offsetZ = ((ModelBiped) entityModel).bipedHead.offsetZ;

				if (renderer instanceof RenderCQREntity) {
					((RenderCQREntity<?>) renderer).setupHeadOffsets(model.bipedHead, EntityEquipmentSlot.HEAD);
				}
				GlStateManager.translate(-model.bipedHead.rotationPointX * 0.0625F * 0.3F, -model.bipedHead.rotationPointY * 0.0625F * 0.3F, -model.bipedHead.rotationPointZ * 0.0625F * 0.3F);
				GlStateManager.scale(1.3F, 1.3F, 1.3F);
				ResourceLocation rs = new ResourceLocation(Reference.MODID, "textures/models/armor/king_crown_layer_1.png");
				// rs = new ResourceLocation("minecraft", "textures/models/armor/iron_layer_1.png");
				Minecraft.getMinecraft().getTextureManager().bindTexture(rs);
				model.bipedHead.render(0.0625F);

				GlStateManager.disableRescaleNormal();
				GlStateManager.enableCull();
				GlStateManager.popMatrix();
			}
		}
	}

	protected static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f;

		for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return prevYawOffset + partialTicks * f;
	}

	protected static void renderLivingAt(EntityLivingBase entityLivingBaseIn, double x, double y, double z) {
		GlStateManager.translate(x, y, z);
	}

	protected static void applyRotations(EntityLivingBase entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
		GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);

		if (entityLiving.deathTime > 0) {
			float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);

			if (f > 1.0F) {
				f = 1.0F;
			}

			GlStateManager.rotate(f * 90.0F, 0.0F, 0.0F, 1.0F);
		} else {
			String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());

			if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer) entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
				GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}

	protected static float handleRotationFloat(EntityLivingBase livingBase, float partialTicks) {
		return (float) livingBase.ticksExisted + partialTicks;
	}

}
