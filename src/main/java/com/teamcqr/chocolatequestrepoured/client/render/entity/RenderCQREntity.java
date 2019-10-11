package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRBiped;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityCape;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityPotion;
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
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
	}

	@Override
	protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
		//if(!this.scaleVarApplied) {
			//Random rand = new Random();
			//this.widthScale = widthScale + (0.5D * (-0.25D +(rand.nextDouble() *0.5D)));
			//this.heightScale = heightScale + (-0.25D +(rand.nextDouble() *0.5D));;
			//this.scaleVarApplied = true;
		//}
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
				case BOW:
					armPoseMain = ModelBiped.ArmPose.BOW_AND_ARROW;
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
				case BOW:
					armPoseOff = ModelBiped.ArmPose.BOW_AND_ARROW;
					break;
				default:
					armPoseOff = ModelBiped.ArmPose.EMPTY;
					break;

				}
			}

			model.rightArmPose = armPoseMain;
			model.leftArmPose = armPoseOff;
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		if (entity.isChatting()) {
			renderChatting(entity);
		}
		
		if (this.mainModel instanceof ModelBiped) {
			GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		}
	}
	
	protected void renderChatting(Entity entity) {
		/*
		 * GL11.glPushMatrix(); GL11.glTranslatef(0.0F, height * 1.6F, 0.0F);
		 * GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		 * GL11.glRotatef(180.0F - renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		 * renderManager.renderEngine.bindTexture(BDHelper.getItemTexture());
		 * Tessellator tessellator = Tessellator.instance;
		 * 
		 * int spriteIndex = (ticksExisted / 160 + entity.getEntityId()) % 16; float i1
		 * = (spriteIndex % 16 * 16 + 0) / 256.0F; float i2 = (spriteIndex % 16 * 16 +
		 * 16) / 256.0F; float i3 = 0.8125F; float i4 = 0.875F; float size = 0.6F; float
		 * width = size; float x = -0.5F;
		 * 
		 * tessellator.startDrawingQuads(); tessellator.addVertexWithUV(x, 0.0D, 0.0D,
		 * i1, i3); tessellator.addVertexWithUV(x + width, 0.0D, 0.0D, i2, i3);
		 * tessellator.addVertexWithUV(x + width, size, 0.0D, i2, i4);
		 * tessellator.addVertexWithUV(x + 0.0F, size, 0.0D, i1, i4);
		 * tessellator.draw();
		 * 
		 * GL11.glPopMatrix();
		 */
	}

	/*protected void renderHelmetFeather(AbstractEntityCQR entity) {
	 	 *
		 * GL11.glPushMatrix(); mainModel).bipedHead.postRender(0.0625F);
		 * GL11.glTranslatef(-0.05F, featherY, 0.01F); GL11.glRotatef(180.0F, 1.0F,
		 * 0.0F, 1.0F); GL11.glRotatef(125.0F, 0.0F, 1.0F, 0.0F); GL11.glScalef(0.5F,
		 * 0.5F, 0.5F); renderManager.itemRenderer.renderItem(entity, item, 0);
		 * GL11.glPopMatrix();
		 *
	}*/

	protected ResourceLocation getEntityTexture(T entity) {
		return this.texture;
	}

}
