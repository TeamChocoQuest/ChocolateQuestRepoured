package team.cqr.cqrepoured.client.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHookShotHook;

/**
 * ModelHook - DerToaster98 Created using Tabula 7.0.1
 */
public class ModelHook<T extends ProjectileHookShotHook> extends SegmentedModel<T> {
	public ModelRenderer stem;
	public ModelRenderer clawLeft;
	public ModelRenderer clawRight;
	public ModelRenderer clawUp;
	public ModelRenderer clawDown;

	public ModelHook() {
		this.clawRight = new ModelRenderer(this, 0, 6);
		this.clawRight.setPos(-0.5F, 0.0F, 0.0F);
		this.clawRight.addBox(-3.0F, -0.5F, 0.0F, 3, 1, 1, 0.0F);
		this.setRotateAngle(this.clawRight, 0.0F, 0.7853981633974483F, 0.0F);
		this.stem = new ModelRenderer(this, 0, 8);
		this.stem.setPos(0.0F, 0.0F, 0.0F);
		this.stem.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 6, 0.0F);
		this.clawLeft = new ModelRenderer(this, 0, 4);
		this.clawLeft.setPos(0.5F, 0.0F, 0.0F);
		this.clawLeft.addBox(0.0F, -0.5F, 0.0F, 3, 1, 1, 0.0F);
		this.setRotateAngle(this.clawLeft, 0.0F, -0.7853981633974483F, 0.0F);
		this.clawUp = new ModelRenderer(this, 0, 0);
		this.clawUp.setPos(0.0F, -0.5F, 0.0F);
		this.clawUp.addBox(-0.5F, -3.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotateAngle(this.clawUp, -0.7853981633974483F, 0.0F, 0.0F);
		this.clawDown = new ModelRenderer(this, 4, 0);
		this.clawDown.setPos(0.0F, 0.5F, 0.0F);
		this.clawDown.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotateAngle(this.clawDown, 0.7853981633974483F, 0.0F, 0.0F);
		this.stem.addChild(this.clawRight);
		this.stem.addChild(this.clawLeft);
		this.stem.addChild(this.clawUp);
		this.stem.addChild(this.clawDown);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.stem);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		
	}
}
