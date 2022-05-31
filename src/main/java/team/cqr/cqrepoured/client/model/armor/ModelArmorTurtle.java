package team.cqr.cqrepoured.client.model.armor;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

/**
 * @author Sir Squidly
 */
public class ModelArmorTurtle<T extends LivingEntity> extends ModelCustomArmorBase<T> {

	public ModelRenderer shell1;
	public ModelRenderer shell2;
	public ModelRenderer tassetLeft;
	public ModelRenderer tassetRight;

	public ModelArmorTurtle(float scale) {
		super(RenderType::armorCutoutNoCull, scale, 64, 64);

		this.shell1 = new ModelRenderer(this, 0, 32);
		this.shell1.setPos(0.0F, -2.0F, 3.5F);
		this.shell1.addBox(-6.0F, 0.0F, 0.0F, 12, 16, 4, 0.0F);
		this.shell2 = new ModelRenderer(this, 32, 32);
		this.shell2.setPos(0.0F, 0.0F, 7.5F);
		this.shell2.addBox(-4.0F, 0.0F, 0.0F, 8, 12, 2, 0.0F);
		this.tassetLeft = new ModelRenderer(this, 0, 52);
		this.tassetLeft.mirror = true;
		this.tassetLeft.setPos(2.0F, -1.2F, -0.2F);
		this.tassetLeft.addBox(-1.5F, 0.0F, -2.5F, 3, 7, 5, 0.0F);
		this.setRotateAngle(this.tassetLeft, 0.0F, 0.0F, -0.4363323129985824F);
		this.tassetRight = new ModelRenderer(this, 0, 52);
		this.tassetRight.setPos(-2.0F, -1.2F, -0.2F);
		this.tassetRight.addBox(-1.5F, 0.0F, -2.5F, 3, 7, 5, 0.0F);
		this.setRotateAngle(this.tassetRight, 0.0F, 0.0F, 0.4363323129985824F);

		this.head = new ModelRenderer(this, 30, 46);
		this.head.setPos(0F, 0F, 0F);
		this.head.addBox(-4.5F, -8.0F, -4.0F, 9, 8, 8, 0.75F);

		this.body.addChild(this.shell2);
		this.body.addChild(this.shell1);
		this.leftLeg.addChild(this.tassetLeft);
		this.rightLeg.addChild(this.tassetRight);
	}

}
