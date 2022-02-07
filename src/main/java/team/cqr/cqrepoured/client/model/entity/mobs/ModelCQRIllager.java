package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRIllager;

public class ModelCQRIllager extends ModelCQRBiped<EntityCQRIllager> {

	private final ModelRenderer nose;

	public ModelCQRIllager() {
		super(64, 64, false);

		this.head = new ModelRenderer(this);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);

		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.addBox(-4.0F, -10.0F, -4.0F, 8, 12, 8, 0.0F + 0.45F);

		this.head.addChild(this.hat);
		this.hat.visible = false;

		this.nose = new ModelRenderer(this);
		this.nose.setPos(0.0F, -2.0F, 0.0F);
		this.nose.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);

		this.head.addChild(this.nose);

		this.body = new ModelRenderer(this);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
		this.body.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.0F + 0.5F);

		ModelRenderer modelrenderer = new ModelRenderer(this, 44, 22);
		modelrenderer.mirror = true;
		modelrenderer.addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);

		this.rightLeg = new ModelRenderer(this, 0, 22);
		this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);

		this.leftLeg = new ModelRenderer(this, 0, 22);
		this.leftLeg.mirror = true;
		this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);

		this.rightArm = new ModelRenderer(this, 40, 46);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);

		this.leftArm = new ModelRenderer(this, 40, 46);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
	}

}
