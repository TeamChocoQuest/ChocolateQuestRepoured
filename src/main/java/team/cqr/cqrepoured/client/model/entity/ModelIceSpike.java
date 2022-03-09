package team.cqr.cqrepoured.client.model.entity;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.entity.misc.EntityIceSpike;

public class ModelIceSpike<T extends EntityIceSpike> extends SegmentedModel<T> {

	public ModelRenderer baseplate;
	public ModelRenderer base;
	public ModelRenderer middle;
	public ModelRenderer top;

	public ModelIceSpike() {
		this.base = new ModelRenderer(this, 0, 0);
		this.base.setPos(0.0F, -2.0F, 0.0F);
		this.base.addBox(-4.0F, -6.0F, -4.0F, 8, 6, 8, 0.0F);
		this.middle = new ModelRenderer(this, 0, 0);
		this.middle.setPos(0.0F, -6.0F, 0.0F);
		this.middle.addBox(-2.5F, -6.0F, -2.5F, 5, 6, 5, 0.0F);
		this.top = new ModelRenderer(this, 0, 0);
		this.top.setPos(0.0F, -6.0F, 0.0F);
		this.top.addBox(-1.0F, -6.0F, -1.0F, 2, 6, 2, 0.0F);
		this.baseplate = new ModelRenderer(this, 0, 0);
		this.baseplate.setPos(0.0F, 2.0F, 0.0F);
		this.baseplate.addBox(-6.0F, -4.0F, -6.0F, 12, 4, 12, 0.0F);
		this.baseplate.addChild(this.base);
		this.base.addChild(this.middle);
		this.middle.addChild(this.top);
	}
	
	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		float animationProgress = pLimbSwing;
		if (animationProgress > 1.0F) {
			animationProgress = 1.0F;
		}

		/* animationProgress = 1.0F - animationProgress * animationProgress * animationProgress; */
		/*
		 * Y when everything is in floor = 16 Y when everything is exposed = -2
		 */
		this.base.y = 16 - 4 * (animationProgress * 4.5F);
	}
	
	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.base);
	}

}
