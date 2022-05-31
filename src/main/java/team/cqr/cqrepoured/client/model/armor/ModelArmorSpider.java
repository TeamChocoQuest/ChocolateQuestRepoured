package team.cqr.cqrepoured.client.model.armor;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

/**
 * @author Silentine
 */
public class ModelArmorSpider<T extends LivingEntity> extends ModelCustomArmorBase<T> {

	public ModelRenderer helmR;
	public ModelRenderer helmL;
	public ModelRenderer bipedBody_1;
	public ModelRenderer arm1part1;
	public ModelRenderer arm1part2;
	public ModelRenderer arm2part1;
	public ModelRenderer arm2part2;
	public ModelRenderer arm3part1;
	public ModelRenderer arm3part2;
	public ModelRenderer arm4part1;
	public ModelRenderer arm4part2;
	public ModelRenderer arm5part1;
	public ModelRenderer arm5part2;
	public ModelRenderer arm6part1;
	public ModelRenderer arm6part2;
	public ModelRenderer armLarm;
	public ModelRenderer armRarm;

	public ModelArmorSpider(float scale) {
		super(RenderType::armorCutoutNoCull, scale, 128, 128);

		this.helmR = new ModelRenderer(this, 0, 64);
		this.helmR.setPos(-5.0F, -2.0F, -5.0F);
		this.helmR.addBox(0.0F, -1.0F, -2.0F, 1, 4, 4, 0.0F);
		this.setRotateAngle(this.helmR, 0.0F, -0.7853981633974483F, 0.0F);
		this.helmL = new ModelRenderer(this, 0, 64);
		this.helmL.mirror = true;
		this.helmL.setPos(5.0F, -2.0F, -5.0F);
		this.helmL.addBox(-1.0F, -1.0F, -2.0F, 1, 4, 4, 0.0F);
		this.setRotateAngle(this.helmL, 0.0F, 0.7853981633974483F, 0.0F);

		this.bipedBody_1 = new ModelRenderer(this, 64, 84);
		this.bipedBody_1.setPos(0.0F, 0.0F, 1.0F);
		this.bipedBody_1.addBox(-3.0F, 2.0F, 0.0F, 6, 8, 4, 0.0F);
		this.setRotateAngle(this.bipedBody_1, 0.2617993877991494F, 0.0F, 0.0F);

		this.arm1part1 = new ModelRenderer(this, 64, 64);
		this.arm1part1.setPos(2.0F, 2.0F, 3.0F);
		this.arm1part1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm1part1, 2.530727415391778F, 0.0F, 0.7853981633974483F);
		this.arm1part2 = new ModelRenderer(this, 64, 74);
		this.arm1part2.setPos(0.0F, 8.0F, 0.0F);
		this.arm1part2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm1part2, 1.5707963267948966F, 0.0F, 0.0F);
		this.arm2part1 = new ModelRenderer(this, 64, 64);
		this.arm2part1.setPos(-2.0F, 2.0F, 3.0F);
		this.arm2part1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm2part1, 2.530727415391778F, 0.0F, -0.7853981633974483F);
		this.arm2part2 = new ModelRenderer(this, 64, 74);
		this.arm2part2.setPos(0.0F, 8.0F, 0.0F);
		this.arm2part2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm2part2, 1.5707963267948966F, 0.0F, 0.0F);
		this.arm3part1 = new ModelRenderer(this, 64, 64);
		this.arm3part1.setPos(2.0F, 5.0F, 3.0F);
		this.arm3part1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm3part1, 1.5707963267948966F, 0.7853981633974483F, 0.0F);
		this.arm3part2 = new ModelRenderer(this, 64, 74);
		this.arm3part2.setPos(0.0F, 8.0F, 0.0F);
		this.arm3part2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm3part2, 0.0F, 0.0F, -1.5707963267948966F);
		this.arm4part1 = new ModelRenderer(this, 64, 64);
		this.arm4part1.setPos(-2.0F, 5.0F, 3.0F);
		this.arm4part1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm4part1, 1.5707963267948966F, -0.7853981633974483F, 0.0F);
		this.arm4part2 = new ModelRenderer(this, 64, 74);
		this.arm4part2.setPos(0.0F, 8.0F, 0.0F);
		this.arm4part2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm4part2, 0.0F, 0.0F, 1.5707963267948966F);
		this.arm5part1 = new ModelRenderer(this, 64, 64);
		this.arm5part1.setPos(2.0F, 8.0F, 3.0F);
		this.arm5part1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm5part1, 0.7853981633974483F, 0.7853981633974483F, 0.0F);
		this.arm5part2 = new ModelRenderer(this, 64, 74);
		this.arm5part2.setPos(0.0F, 8.0F, 0.0F);
		this.arm5part2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm5part2, 0.0F, 0.0F, -1.5707963267948966F);
		this.arm6part1 = new ModelRenderer(this, 64, 64);
		this.arm6part1.setPos(-2.0F, 8.0F, 3.0F);
		this.arm6part1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm6part1, 0.7853981633974483F, -0.7853981633974483F, 0.0F);
		this.arm6part2 = new ModelRenderer(this, 64, 74);
		this.arm6part2.setPos(0.0F, 8.0F, 0.0F);
		this.arm6part2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
		this.setRotateAngle(this.arm6part2, 0.0F, 0.0F, 1.5707963267948966F);

		this.armRarm = new ModelRenderer(this, 64, 0);
		this.armRarm.mirror = true;
		this.armRarm.setPos(-0.9F, -0.75F, 0.0F);
		this.armRarm.addBox(-1.0F, -4.0F, -5.0F, 2, 6, 6, 0.0F);
		this.setRotateAngle(this.armRarm, 0.0F, 0.7853981633974483F, 0.0F);
		this.armLarm = new ModelRenderer(this, 64, 0);
		this.armLarm.mirror = true;
		this.armLarm.setPos(0.9F, -0.75F, 0.0F);
		this.armLarm.addBox(-1.0F, -4.0F, -5.0F, 2, 6, 6, 0.0F);
		this.setRotateAngle(this.armLarm, 0.0F, -0.7853981633974483F, 0.0F);

		this.head.addChild(this.helmR);
		this.head.addChild(this.helmL);
		this.body.addChild(this.bipedBody_1);
		this.body.addChild(this.arm1part1);
		this.arm1part1.addChild(this.arm1part2);
		this.body.addChild(this.arm2part1);
		this.arm2part1.addChild(this.arm2part2);
		this.body.addChild(this.arm3part1);
		this.arm3part1.addChild(this.arm3part2);
		this.body.addChild(this.arm4part1);
		this.arm4part1.addChild(this.arm4part2);
		this.body.addChild(this.arm5part1);
		this.arm5part1.addChild(this.arm5part2);
		this.body.addChild(this.arm6part1);
		this.arm6part1.addChild(this.arm6part2);
		this.rightArm.addChild(this.armRarm);
		this.leftArm.addChild(this.armLarm);
	}

}
