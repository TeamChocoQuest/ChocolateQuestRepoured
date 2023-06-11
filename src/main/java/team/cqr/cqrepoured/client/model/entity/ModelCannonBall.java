package team.cqr.cqrepoured.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;

// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ModelCannonBall<T extends ProjectileCannonBall> extends EntityModel<T> {
	private final ModelRenderer bb_main;

	public ModelCannonBall() {
		texWidth = 16;
		texHeight = 16;

		bb_main = new ModelRenderer(this);
		bb_main.setPos(0.0F, 24.0F, 0.0F);
		bb_main.texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		bb_main.texOffs(0, 0).addBox(-2.0F, -5.0F, 2.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
		bb_main.texOffs(0, 0).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
		bb_main.texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		bb_main.texOffs(0, 0).addBox(2.0F, -5.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		bb_main.texOffs(0, 0).addBox(-3.0F, -5.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(ProjectileCannonBall entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}