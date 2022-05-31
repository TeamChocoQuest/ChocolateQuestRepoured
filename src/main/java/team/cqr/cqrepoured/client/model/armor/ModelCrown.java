package team.cqr.cqrepoured.client.model.armor;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

/**
 * ModelCrown - DerToaster Created using Tabula 7.0.1
 */
public class ModelCrown<T extends LivingEntity> extends ModelCustomArmorBase<T> {

	public ModelRenderer crownBorderFront;
	public ModelRenderer crownBorderBack;
	public ModelRenderer crownBorderRight;
	public ModelRenderer crownBorderLeft;
	public ModelRenderer crownUpper;
	public ModelRenderer crownJewelFrontBase;
	public ModelRenderer crownJewelFront;

	public ModelCrown(float scale) {
		super(RenderType::armorCutoutNoCull, scale, 32, 32);

		this.crownBorderLeft = new ModelRenderer(this, 18, 16);
		this.crownBorderLeft.setPos(3.0F, 1.3F, 0.0F);
		this.crownBorderLeft.addBox(0.0F, -9.0F, -3.0F, 1, 2, 6, 0.0F);
		this.crownJewelFrontBase = new ModelRenderer(this, 0, 0);
		this.crownJewelFrontBase.setPos(0.0F, -9.2F, -0.5F);
		this.crownJewelFrontBase.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 1, 0.0F);
		this.crownBorderRight = new ModelRenderer(this, 18, 24);
		this.crownBorderRight.setPos(-3.0F, 1.3F, 0.0F);
		this.crownBorderRight.addBox(-1.0F, -9.0F, -3.0F, 1, 2, 6, 0.0F);
		this.crownUpper = new ModelRenderer(this, 0, 0);
		this.crownUpper.setPos(0.0F, -2.7F, 0.0F);
		this.crownUpper.addBox(-4.0F, -7.0F, -4.0F, 8, 2, 8, 0.0F);
		this.crownJewelFront = new ModelRenderer(this, 0, 3);
		this.crownJewelFront.setPos(0.0F, 0.0F, -0.7F);
		this.crownJewelFront.addBox(-0.5F, 0.5F, -0.5F, 1, 1, 1, 0.0F);
		this.crownBorderBack = new ModelRenderer(this, 0, 24);
		this.crownBorderBack.setPos(0.0F, 1.3F, 2.0F);
		this.crownBorderBack.addBox(-4.0F, -9.0F, 1.0F, 8, 2, 1, 0.0F);
		this.head = new ModelRenderer(this, 0, 10);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		// this.head.addBox(-3.0F, -7.0F, -3.0F, 6, 1, 6, 0.0F);
		this.crownBorderFront = new ModelRenderer(this, 0, 29);
		this.crownBorderFront.setPos(0.0F, 1.3F, -3.0F);
		this.crownBorderFront.addBox(-4.0F, -9.0F, -1.0F, 8, 2, 1, 0.0F);

		this.head.addChild(this.crownBorderLeft);
		this.crownBorderFront.addChild(this.crownJewelFrontBase);
		this.head.addChild(this.crownBorderRight);
		this.head.addChild(this.crownUpper);
		this.crownJewelFrontBase.addChild(this.crownJewelFront);
		this.head.addChild(this.crownBorderBack);
		this.head.addChild(this.crownBorderFront);

		this.hat.visible = false;
		this.body.visible = false;
		this.leftArm.visible = false;
		this.rightArm.visible = false;
		this.leftLeg.visible = false;
		this.rightLeg.visible = false;
	}

	@Override
	public void setAllVisible(boolean pVisible) {
		super.setAllVisible(false);
		this.head.visible = pVisible;
	}

}
