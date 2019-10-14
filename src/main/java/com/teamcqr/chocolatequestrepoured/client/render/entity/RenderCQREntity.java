package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRBiped;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityCape;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityPotion;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRLeaderFeather;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRSpeechbubble;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderCQREntity<T extends AbstractEntityCQR> extends RenderLiving<T> {

	public ResourceLocation texture;
	public double widthScale;
	public double heightScale;
	//private boolean scaleVarApplied = false;
	
	public RenderCQREntity(RenderManager rendermanagerIn, String entityName) {
		this(rendermanagerIn, entityName, 1.0D, 1.0D);
	}

	public RenderCQREntity(RenderManager rendermanagerIn, String entityName, double widthScale, double heightScale) {
		this(rendermanagerIn, new ModelCQRBiped(0.0F, true), 0.5F, entityName, widthScale, heightScale);
	}

	public RenderCQREntity(RenderManager rendermanagerIn, ModelBase model, float shadowSize, String entityName,
			double widthScale, double heightScale) {
		super(rendermanagerIn, model, shadowSize);
		this.texture = new ResourceLocation(Reference.MODID, "textures/entity/" + entityName + ".png");
		//Random rand = new Random();
		this.widthScale = widthScale;// + (0.5D * (-0.25D +(rand.nextDouble() *0.5D)));
		this.heightScale = heightScale;// + (-0.25D +(rand.nextDouble() *0.5D));;
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerElytra(this));
		this.addLayer(new LayerCQREntityCape(this));
		this.addLayer(new LayerCQREntityPotion(this));
		
		if(model instanceof ModelCQRBiped) {
			this.addLayer(new LayerCQRLeaderFeather(this, ((ModelCQRBiped)model).bipedHead));
			this.addLayer(new LayerCQRSpeechbubble(this));
		}
	}

	@Override
	protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
		double width = this.widthScale * (1.0D + 0.8D * entitylivingbaseIn.getSizeVariation());
		double height = this.heightScale * (1.0D + entitylivingbaseIn.getSizeVariation());
		GL11.glScaled(width, height, width);
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (this.mainModel instanceof ModelBiped) {
			GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
			ModelBiped model = (ModelBiped) this.mainModel;

			ItemStack itemMainHand = entity.getHeldItemMainhand();
			ItemStack itemOffHand = entity.getHeldItemOffhand();

			ModelBiped.ArmPose armPoseMain = ModelBiped.ArmPose.EMPTY;
			ModelBiped.ArmPose armPoseOff = ModelBiped.ArmPose.EMPTY;
			// Main arm
			if (!itemMainHand.isEmpty() && entity.getItemInUseCount() > 0 && itemMainHand.getItem() instanceof ItemShield) {
				EnumAction action = itemMainHand.getItemUseAction();
				switch (action) {
				case BLOCK:
					armPoseMain = ModelBiped.ArmPose.BLOCK;
					break;
				default:
					armPoseMain = ModelBiped.ArmPose.EMPTY;
					break;

				}
			}
			// Off arm
			if (!itemOffHand.isEmpty() && entity.getItemInUseCount() > 0 && itemOffHand.getItem() instanceof ItemShield) {
				EnumAction action = itemOffHand.getItemUseAction();
				switch (action) {
				case BLOCK:
					armPoseOff = ModelBiped.ArmPose.BLOCK;
					break;
				default:
					armPoseOff = ModelBiped.ArmPose.EMPTY;
					break;

				}
			}
			

			model.rightArmPose = armPoseMain;
			model.leftArmPose = armPoseOff;
			
			//if(entity.isSitting()) {
				//renderSitting((ModelBiped) model);
			//}
			
			switch(entity.getArmPose()) {
			case HOLDING_ITEM:
				break;
			case PULLING_BOW:
				break;
			case SPELLCASTING:
				renderSpellAnimation(entity, entity.ticksExisted, model);
				break;
			case STAFF_L:
				break;
			case STAFF_R:
				break;
			default:
				break;
			
			}
			
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		this.mainModel.isRiding = entity.isSitting() || this.mainModel.isRiding;
		
		
		
		if (this.mainModel instanceof ModelBiped) {
			GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		}
	}
	
	protected void renderSpellAnimation(T entityIn, float ageInTicks, ModelBiped model) {
		model.bipedRightArm.rotationPointZ = 0.0F;
		model.bipedRightArm.rotationPointX = -5.0F;
		model.bipedLeftArm.rotationPointZ = 0.0F;
		model.bipedLeftArm.rotationPointX = 5.0F;
		model.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		model.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		model.bipedRightArm.rotateAngleZ = 2.3561945F;
		model.bipedLeftArm.rotateAngleZ = -2.3561945F;
		model.bipedRightArm.rotateAngleY = 0.0F;
		model.bipedLeftArm.rotateAngleY = 0.0F;

		// Particles
		double dx = 0.7D;
		double dy = 0.5D;
		double dz = 0.2D;
		float f = ((AbstractEntityCQR) entityIn).renderYawOffset * 0.017453292F
				+ MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		float f1 = MathHelper.cos(f);
		float f2 = MathHelper.sin(f);
		entityIn.world.spawnParticle(EnumParticleTypes.SPELL_MOB, entityIn.posX + (double) f1 * 0.6D,
				entityIn.posY + 1.8D, entityIn.posZ + (double) f2 * 0.6D, dx, dy, dz);
		entityIn.world.spawnParticle(EnumParticleTypes.SPELL_MOB, entityIn.posX - (double) f1 * 0.6D,
				entityIn.posY + 1.8D, entityIn.posZ - (double) f2 * 0.6D, dx, dy, dz);
	}

	@Override
	protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scaleFactor) {
		
		if(entitylivingbaseIn.isSitting()) {
			GL11.glTranslatef(0, 0.6F, 0);
		}
		
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		
		this.mainModel.isRiding = entitylivingbaseIn.isSitting() || entitylivingbaseIn.isRiding();
	}
	
	protected ResourceLocation getEntityTexture(T entity) {
		return this.texture;
	}

}
