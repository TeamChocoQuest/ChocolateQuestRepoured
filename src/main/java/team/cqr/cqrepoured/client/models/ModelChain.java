package team.cqr.cqrepoured.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelChain extends ModelBase {

	private final ModelRenderer bone;

	public ModelChain() {
		this.textureWidth = 16;
		this.textureHeight = 16;

		this.bone = new ModelRenderer(this);
		this.bone.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 0, -1.5F, -0.5F, -1.0F, 1, 1, 8, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 0, 0.5F, -0.5F, -1.0F, 1, 1, 8, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 0, -0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 0, -0.5F, -0.5F, 6.0F, 1, 1, 1, 0.0F, false));
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
