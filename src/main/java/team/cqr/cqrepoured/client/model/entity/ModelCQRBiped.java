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
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.bipedBody = new ModelRenderer(this, 32, 0);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.bipedRightArm = new ModelRenderer(this, 56, 0);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 72, 0);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 88, 0);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 104, 0);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
	}

	protected void initModelOverlayParts() {
		this.bipedHeadwear = copy(this.bipedHead, 0, 0, 0.25F);
		this.bipedHead.addChild(this.bipedHeadwear);

		this.bipedBodywear = copy(this.bipedBody, 0, 0, 0.25F);
		this.bipedBody.addChild(this.bipedBodywear);

		this.bipedRightArmwear = copy(this.bipedRightArm, 0, 0, 0.25F);
		this.bipedRightArm.addChild(this.bipedRightArmwear);

		this.bipedLeftArmwear = copy(this.bipedLeftArm, 0, 0, 0.25F);
		this.bipedLeftArm.addChild(this.bipedLeftArmwear);

		this.bipedRightLegwear = copy(this.bipedRightLeg, 0, 0, 0.25F);
		this.bipedRightLeg.addChild(this.bipedRightLegwear);

		this.bipedLeftLegwear = copy(this.bipedLeftLeg, 0, 0, 0.25F);
		this.bipedLeftLeg.addChild(this.bipedLeftLegwear);
	}

	/**
	 * When source.cubeList is empty a new {@link ModelRenderer} will be returned.<br>
	 * <br>
	 * Otherwise a new {@link ModelRenderer} will be created and the first {@link ModelBox} of the source will be copied
	 * with the given scaleFactor. The texture coords of the copy will point at the position directly below the source
	 * texture coords.
	 */
	protected ModelRenderer copy(ModelRenderer source, int texOffX, int texOffY, float scaleFactor) {
		ModelRenderer copy = new ModelRenderer(this);
		if (source.cubeList.isEmpty()) {
			return copy;
		}
		ModelBox box = source.cubeList.get(0);
		int width = (int) (box.posX2 - box.posX1);
		int height = (int) (box.posY2 - box.posY1);
		int length = (int) (box.posZ2 - box.posZ1);
		copy.setTextureOffset(source.textureOffsetX + texOffX, source.textureOffsetY + texOffY + height + length);
		copy.addBox(box.posX1, box.posY1, box.posZ1, width, height, length, scaleFactor);
		return copy;
	}

	protected void setRotationDeg(ModelRenderer modelRenderer, double x, double y, double z) {
		this.setRotationRad(modelRenderer, Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
	}

	protected void setRotationRad(ModelRenderer modelRenderer, double x, double y, double z) {
		modelRenderer.rotateAngleX = (float) x;
		modelRenderer.rotateAngleY = (float) y;
		modelRenderer.rotateAngleZ = (float) z;
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
		this.setRotationRad(this.bipedHeadwear, 0, 0, 0);

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
