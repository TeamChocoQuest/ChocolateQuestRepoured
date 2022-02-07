package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRBoarman;

public class ModelCQRBoarman extends ModelCQRBiped<EntityCQRBoarman> {

	protected ModelRenderer righttusk;
	protected ModelRenderer lefttusk;
	protected ModelRenderer mane01;
	protected ModelRenderer mane02;

	public ModelCQRBoarman() {
		super(64, 64, true);

		this.righttusk = new ModelRenderer(this, 56, 16);
		this.righttusk.addBox(-1F, -1F, -2F, 1, 2, 2, 0.0F);
		this.righttusk.setPos(-2F, -2F, -4F);
		this.righttusk.setTexSize(64, 64);
		this.righttusk.mirror = true;
		this.setRotateAngle(this.righttusk, 0F, 0.7853982F, 0F);
		this.lefttusk = new ModelRenderer(this, 56, 16);
		this.lefttusk.addBox(0F, -1F, -2F, 1, 2, 2, 0.0F);
		this.lefttusk.setPos(2F, -2F, -4F);
		this.lefttusk.setTexSize(64, 64);
		this.lefttusk.mirror = true;
		this.setRotateAngle(this.lefttusk, 0F, -0.7853982F, 0F);
		this.mane01 = new ModelRenderer(this, 56, 18);
		this.mane01.addBox(0F, 0F, 0F, 0, 12, 2, 0.0F);
		this.mane01.setPos(0F, 0F, 2F);
		this.mane01.setTexSize(64, 64);
		this.mane01.mirror = true;
		this.setRotateAngle(this.mane01, 0F, 0.2617994F, 0F);
		this.mane02 = new ModelRenderer(this, 60, 18);
		this.mane02.addBox(0F, 0F, 0F, 0, 12, 2, 0.0F);
		this.mane02.setPos(0F, 0F, 2F);
		this.mane02.setTexSize(64, 64);
		this.mane02.mirror = true;
		this.setRotateAngle(this.mane02, 0F, -0.2617994F, 0F);

		this.body.addChild(this.mane01);
		this.body.addChild(this.mane02);
		this.head.addChild(this.righttusk);
		this.head.addChild(this.lefttusk);
	}

}
