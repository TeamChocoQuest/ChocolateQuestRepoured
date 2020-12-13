package team.cqr.cqrepoured.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * CQR_Necro_Bone_Shield - Undefined Created using Tabula 7.0.1
 */
public class ModelBoneShield extends ModelBase {
	public ModelRenderer bone;
	public ModelRenderer shape1;
	public ModelRenderer shape2;
	public ModelRenderer shape3;
	public ModelRenderer shape4;
	public ModelRenderer shape5;
	public ModelRenderer shape6;
	public ModelRenderer shape7;
	public ModelRenderer shape8;

	public ModelBoneShield() {
		this.textureWidth = 16;
		this.textureHeight = 16;
		this.bone = new ModelRenderer(this, 0, 0);
		this.bone.setRotationPoint(0.0F, 8F, 0.0F);
		this.bone.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
		this.setRotateAngle(this.bone, 0, 0, 0);
		this.shape6 = new ModelRenderer(this, 0, 0);
		this.shape6.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape6.addBox(16.0F, -8.0F, -8.0F, 0, 16, 16, 0.0F);
		this.shape2 = new ModelRenderer(this, 0, 0);
		this.shape2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape2.addBox(-8.0F, -8.0F, 16.0F, 16, 16, 0, 0.0F);
		this.shape7 = new ModelRenderer(this, 0, 0);
		this.shape7.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape7.addBox(-16.0F, -8.0F, -8.0F, 0, 16, 16, 0.0F);
		this.setRotateAngle(this.shape7, 0.0F, 0.7853981633974483F, 0.0F);
		this.shape8 = new ModelRenderer(this, 0, 0);
		this.shape8.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape8.addBox(16.0F, -8.0F, -8.0F, 0, 16, 16, 0.0F);
		this.setRotateAngle(this.shape8, 0.0F, 0.7853981633974483F, 0.0F);
		this.shape1 = new ModelRenderer(this, 0, 0);
		this.shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape1.addBox(-8.0F, -8.0F, -16.0F, 16, 16, 0, 0.0F);
		this.shape5 = new ModelRenderer(this, 0, 0);
		this.shape5.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape5.addBox(-16.0F, -8.0F, -8.0F, 0, 16, 16, 0.0F);
		this.shape4 = new ModelRenderer(this, 0, 0);
		this.shape4.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape4.addBox(-8.0F, -8.0F, 16.0F, 16, 16, 0, 0.0F);
		this.setRotateAngle(this.shape4, 0.0F, 0.7853981633974483F, 0.0F);
		this.shape3 = new ModelRenderer(this, 0, 0);
		this.shape3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape3.addBox(-8.0F, -8.0F, -16.0F, 16, 16, 0, 0.0F);
		this.setRotateAngle(this.shape3, 0.0F, 0.7853981633974483F, 0.0F);

		this.bone.addChild(this.shape6);
		this.bone.addChild(this.shape2);
		this.bone.addChild(this.shape7);
		this.bone.addChild(this.shape8);
		this.bone.addChild(this.shape1);
		this.bone.addChild(this.shape5);
		this.bone.addChild(this.shape4);
		this.bone.addChild(this.shape3);
	}

	@Override
	public void render(Entity entity, float maxAngleX, float offsetAngleY, float offsetAngleZ, float f3, float f4, float f5) {
		float angle = entity.ticksExisted + offsetAngleY;
		angle -= 360 * (entity.ticksExisted / 360);
		angle = (float) Math.toRadians(angle);
		this.bone.rotateAngleY = angle * 6F;
		this.bone.rotateAngleX = (float) (Math.toRadians(maxAngleX) * Math.sin(angle * 4));
		this.bone.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
