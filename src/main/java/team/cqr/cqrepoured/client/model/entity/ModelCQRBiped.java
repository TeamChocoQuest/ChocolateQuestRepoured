package team.cqr.cqrepoured.client.model.entity;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.FirearmAnimation;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.GreatswordAnimation;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.IModelBipedAnimation;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.SpearAnimation;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.SpinToWinAnimation;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.ItemHookshotBase;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemRevolver;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;
import team.cqr.cqrepoured.util.PartialTicksUtil;

public class ModelCQRBiped extends ModelBiped {

	private static final Set<IModelBipedAnimation> ANIMATIONS = Stream
			.of(new SpinToWinAnimation(), new SpearAnimation(), new FirearmAnimation(), new SpearAnimation(), new GreatswordAnimation())
			.collect(Collectors.toSet());

	public ModelRenderer bipedLeftArmwear = null;
	public ModelRenderer bipedRightArmwear = null;
	public ModelRenderer bipedLeftLegwear = null;
	public ModelRenderer bipedRightLegwear = null;
	public ModelRenderer bipedBodyWear = null;
	public ModelRenderer bipedCape = null;

	public boolean hasExtraLayers = true;

	public ModelCQRBiped(int textureWidthIn, int textureHeightIn, boolean hasExtraLayer) {
		super(0.0F, 0.0F, textureWidthIn, textureHeightIn);
		this.hasExtraLayers = hasExtraLayer;

		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

		this.initExtraLayer();
		this.setClothingLayerVisible(hasExtraLayer);
	}

	protected void initExtraLayer() {
		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);

		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);

		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
		this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void renderCape(float scale) {
		this.bipedCape.render(scale);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so
	 * that arms and legs swing back and forth) and par2 represents how "far" arms and legs
	 * can swing at most.
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor,
			Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);



	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.bipedCape.showModel = visible;

		if (this.hasExtraLayers) {
			this.setClothingLayerVisible(visible);
		}
		AbstractEntityCQR entityCQR = (AbstractEntityCQR) entityIn;
		ANIMATIONS.stream().filter(ani -> ani.canApply(entityCQR)).forEach(ani -> ani.apply(this, ageInTicks, entityCQR));
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();

		if (this.isChild) {
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			this.bipedHead.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.bipedBody.render(scale);
			this.bipedRightArm.render(scale);
			this.bipedLeftArm.render(scale);
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
			this.bipedHeadwear.render(scale);
		} else {
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.bipedHead.render(scale);
			this.bipedBody.render(scale);
			this.bipedRightArm.render(scale);
			this.bipedLeftArm.render(scale);
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
			this.bipedHeadwear.render(scale);
		}

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();

		if (this.hasExtraLayers) {
			this.bipedLeftLegwear.render(scale);
			this.bipedRightLegwear.render(scale);
			this.bipedLeftArmwear.render(scale);
			this.bipedRightArmwear.render(scale);
			this.bipedBodyWear.render(scale);
		}

		GlStateManager.popMatrix();
	}

	protected void setClothingLayerVisible(boolean visible) {
		try {
			this.bipedLeftArmwear.showModel = visible;
			this.bipedRightArmwear.showModel = visible;
			this.bipedLeftLegwear.showModel = visible;
			this.bipedRightLegwear.showModel = visible;
			this.bipedBodyWear.showModel = visible;
		} catch (NullPointerException npe) {
			// Can occur cause by default these fields are null
			// However this can be ignored
		}
	}

	public static void copyModelRotationPoint(ModelRenderer source, ModelRenderer target) {
		if (source == null || target == null) {
			return;
		}
		target.rotationPointX = source.rotationPointX;
		target.rotationPointY = source.rotationPointY;
		target.rotationPointZ = source.rotationPointZ;
	}

}
