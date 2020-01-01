package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRBiped;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityCape;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityPotion;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRLeaderFeather;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRSpeechbubble;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemMusket;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemRevolver;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class RenderCQREntity<T extends AbstractEntityCQR> extends RenderLiving<T> {

	public ResourceLocation texture;
	public double widthScale;
	public double heightScale;

	private final String entityName;

	public RenderCQREntity(RenderManager rendermanagerIn, String entityName) {
		this(rendermanagerIn, entityName, 1.0D, 1.0D);
	}

	public RenderCQREntity(RenderManager rendermanagerIn, String entityName, double widthScale, double heightScale) {
		this(rendermanagerIn, new ModelCQRBiped(0.0F, true), 0.5F, entityName, widthScale, heightScale);
	}

	public RenderCQREntity(RenderManager rendermanagerIn, ModelBase model, float shadowSize, String entityName, double widthScale, double heightScale) {
		super(rendermanagerIn, model, shadowSize);
		this.entityName = entityName;
		this.texture = new ResourceLocation(Reference.MODID, "textures/entity/" + this.entityName + ".png");
		// Random rand = new Random();
		this.widthScale = widthScale;// + (0.5D * (-0.25D +(rand.nextDouble() *0.5D)));
		this.heightScale = heightScale;// + (-0.25D +(rand.nextDouble() *0.5D));;
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		// this.addLayer(new LayerRevolver(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerElytra(this));
		this.addLayer(new LayerCQREntityCape(this));
		this.addLayer(new LayerCQREntityPotion(this));

		if (model instanceof ModelCQRBiped) {
			this.addLayer(new LayerCQRLeaderFeather(this, ((ModelCQRBiped) model).bipedHead));
			this.addLayer(new LayerCQRSpeechbubble(this));
		}
	}

	@Override
	protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
		if (entitylivingbaseIn.getTextureCount() > 1) {
			this.texture = new ResourceLocation(Reference.MODID, "textures/entity/" + this.entityName + "_" + entitylivingbaseIn.getTextureIndex() + ".png");
		}
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

			boolean dontRenderOffItem = false;
			boolean dontRenderMainItem = false;

			boolean flagMain = false;
			boolean flagOff = false;

			// Main arm
			if (!itemMainHand.isEmpty()) {
				if (itemMainHand.getItem() instanceof ItemMusket) {
					armPoseMain = ModelBiped.ArmPose.BOW_AND_ARROW;
					dontRenderOffItem = true;
				} else if (itemMainHand.getItem() instanceof ItemRevolver) {
					flagMain = true;
				} else if (entity.getItemInUseCount() > 0) {
					EnumAction action = itemMainHand.getItemUseAction();
					switch (action) {
					case DRINK:
					case EAT:
						armPoseMain = ModelBiped.ArmPose.ITEM;
						break;
					case BOW:
						armPoseMain = ModelBiped.ArmPose.BOW_AND_ARROW;
						dontRenderOffItem = true;
						break;
					case BLOCK:
						armPoseMain = ModelBiped.ArmPose.BLOCK;
						break;
					default:
						// armPoseMain = ModelBiped.ArmPose.EMPTY;
						break;
					}
				}
			}
			// Off arm
			if (!itemOffHand.isEmpty()) {
				// if(itemOffHand.getItem() instanceof ItemShield) {

				if (itemOffHand.getItem() instanceof ItemMusket) {
					armPoseOff = ModelBiped.ArmPose.BOW_AND_ARROW;
					dontRenderMainItem = true;
				} else if (itemMainHand.getItem() instanceof ItemRevolver) {
					flagOff = true;
				} else if (entity.getItemInUseCount() > 0) {
					EnumAction action = itemOffHand.getItemUseAction();
					switch (action) {
					case DRINK:
					case EAT:
						armPoseOff = ModelBiped.ArmPose.ITEM;
						break;
					case BOW:
						armPoseOff = ModelBiped.ArmPose.BOW_AND_ARROW;
						dontRenderMainItem = true;
						break;
					case BLOCK:
						armPoseOff = ModelBiped.ArmPose.BLOCK;
						break;
					default:
						break;

					}
				}

			}

			if (entity.getPrimaryHand() == EnumHandSide.LEFT) {
				ArmPose tmp = armPoseMain;
				armPoseMain = armPoseOff;
				armPoseOff = tmp;
				boolean tmp2 = dontRenderMainItem;
				dontRenderMainItem = dontRenderOffItem;
				dontRenderOffItem = tmp2;
			}
			if (!flagMain) {
				model.rightArmPose = armPoseMain;
			}
			if (!flagOff) {
				model.leftArmPose = armPoseOff;
			}
			if (dontRenderMainItem) {
				model.rightArmPose = ArmPose.EMPTY;
			}
			if (dontRenderOffItem) {
				model.leftArmPose = ArmPose.EMPTY;
			}

		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		this.mainModel.isRiding = entity.isSitting() || this.mainModel.isRiding;

		if (this.mainModel instanceof ModelBiped) {
			GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		}
	}

	@Override
	protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

		if (this.mainModel.isRiding) {
			GL11.glTranslatef(0, 0.6F, 0);
		}

		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

		this.mainModel.isRiding = entitylivingbaseIn.isSitting() || entitylivingbaseIn.isRiding();
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.texture;
	}

}
