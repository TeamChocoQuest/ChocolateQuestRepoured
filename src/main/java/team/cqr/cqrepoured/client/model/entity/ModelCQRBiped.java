package team.cqr.cqrepoured.client.model.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.client.model.IHideable;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.FirearmAnimation;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.GreatswordAnimation;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.IModelBipedAnimation;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.SpearAnimation;
import team.cqr.cqrepoured.client.model.entity.mobs.animation.SpinToWinAnimation;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class ModelCQRBiped extends ModelBiped implements IHideable {

	private static final Set<IModelBipedAnimation> ANIMATIONS = Stream
			.of(new SpinToWinAnimation(), new SpearAnimation(), new FirearmAnimation(), new SpearAnimation(), new GreatswordAnimation())
			.collect(Collectors.toSet());

	protected final Map<String, ModelRenderer> parts = new HashMap<>();

	public ModelRenderer bipedBodywear;
	public ModelRenderer bipedRightArmwear;
	public ModelRenderer bipedLeftArmwear;
	public ModelRenderer bipedRightLegwear;
	public ModelRenderer bipedLeftLegwear;

	public ModelCQRBiped(int textureWidthIn, int textureHeightIn) {
		super(0.0F, 0.0F, textureWidthIn, textureHeightIn);

		this.initModelParts();
		this.initModelOverlayParts();
		this.fillModelOverlayParts();

		this.parts.put("bipedHead", this.bipedHead);
		this.parts.put("bipedBody", this.bipedBody);
		this.parts.put("bipedRightArm", this.bipedRightArm);
		this.parts.put("bipedLeftArm", this.bipedLeftArm);
		this.parts.put("bipedRightLeg", this.bipedRightLeg);
		this.parts.put("bipedLeftLeg", this.bipedLeftLeg);

		this.parts.put("bipedHeadwear", this.bipedHeadwear);
		this.parts.put("bipedBodywear", this.bipedBodywear);
		this.parts.put("bipedRightArmwear", this.bipedRightArmwear);
		this.parts.put("bipedLeftArmwear", this.bipedLeftArmwear);
		this.parts.put("bipedRightLegwear", this.bipedRightLegwear);
		this.parts.put("bipedLeftLegwear", this.bipedLeftLegwear);
	}

	protected void initModelParts() {
		// seperate textures coords for both arms
		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		// seperate textures coords for both legs
		this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
	}

	protected void initModelOverlayParts() {
		this.bipedHeadwear = new ModelRenderer(this, 32, 0);
		this.bipedHead.addChild(this.bipedHeadwear);

		this.bipedBodywear = new ModelRenderer(this, 16, 32);
		this.bipedBody.addChild(this.bipedBodywear);

		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArm.addChild(this.bipedRightArmwear);

		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArm.addChild(this.bipedLeftArmwear);

		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLeg.addChild(this.bipedRightLegwear);

		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLeg.addChild(this.bipedLeftLegwear);
	}

	protected void fillModelOverlayParts() {
		copyBox(this.bipedHead, this.bipedHeadwear, 0.5F);
		copyBox(this.bipedBody, this.bipedBodywear, 0.25F);
		copyBox(this.bipedRightArm, this.bipedRightArmwear, 0.25F);
		copyBox(this.bipedLeftArm, this.bipedLeftArmwear, 0.25F);
		copyBox(this.bipedRightLeg, this.bipedRightLegwear, 0.25F);
		copyBox(this.bipedLeftLeg, this.bipedLeftLegwear, 0.25F);
	}

	private static void copyBox(ModelRenderer source, ModelRenderer target, float scaleFactor) {
		if (source.cubeList.isEmpty()) {
			return;
		}
		ModelBox box = source.cubeList.get(0);
		int width = (int) (box.posX2 - box.posX1);
		int height = (int) (box.posY2 - box.posY1);
		int length = (int) (box.posZ2 - box.posZ1);
		target.addBox(box.posX1, box.posY1, box.posZ1, width, height, length, scaleFactor);
	}

	protected void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
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

		// fix rotation of bipedHeadwear (it is now a child of bipedHead and thus does not need to copy the rotation point and
		// angles)
		this.bipedHeadwear.setRotationPoint(0, 0, 0);
		this.setRotateAngle(this.bipedHeadwear, 0, 0, 0);

		AbstractEntityCQR entityCQR = (AbstractEntityCQR) entityIn;
		ANIMATIONS.stream().filter(ani -> ani.canApply(entityCQR)).forEach(ani -> ani.apply(this, ageInTicks, entityCQR));
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

		if (entityIn.isSneaking()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		this.bipedHead.render(scale);
		this.bipedBody.render(scale);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
		this.bipedRightLeg.render(scale);
		this.bipedLeftLeg.render(scale);

		if (entityIn.isSneaking()) {
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void setupVisibility(Collection<String> visibleParts) {
		this.parts.values().forEach(part -> part.showModel = false);
		visibleParts.stream().map(this.parts::get).filter(Objects::nonNull).forEach(part -> part.showModel = true);
	}

	@Override
	public void resetVisibility() {
		this.parts.values().forEach(part -> part.showModel = true);
	}

}
