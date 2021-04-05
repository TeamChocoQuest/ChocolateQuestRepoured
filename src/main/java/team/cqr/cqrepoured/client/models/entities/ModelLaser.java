package team.cqr.cqrepoured.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLaser extends ModelBase {

	private final ModelRenderer bone;

	public ModelLaser() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.bone = new ModelRenderer(this);
		this.bone.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 0, -4.0F, -4.0F, -16.0F, 8, 8, 16, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.bone.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
