package team.cqr.cqrepoured.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEnergyOrb;

// Code largely copied from vanilla ender crystal
public class ModelEnergyOrb extends Model {
	/** The cube model for the Ender Crystal. */
	private final ModelRenderer cube;
	private final ProjectileEnergyOrb orb;

	public ModelEnergyOrb(ProjectileEnergyOrb entity) {
		super(RenderType::entityCutout);
		this.cube = new ModelRenderer(this);
		this.cube.texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
		this.orb = entity;
	}

	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		pMatrixStack.pushPose();
		pMatrixStack.translate(0.0F, 0.5F, 0.0F);
		pMatrixStack.scale(0.875F, 0.875F, 0.875F);

		//GlStateManager.pushMatrix();
		//GlStateManager.pushAttrib();
		//GlStateManager.translate(0.0F, 0.5F, 0.0F);


		//GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F); #TODO
		//GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F); #TODO

		float color = 0.5F + (float) (0.25F * (1 + Math.sin(this.orb.tickCount / Math.PI)));
		//GlStateManager.color(color, color, 0.0F);
		RenderSystem.color3f(color, color, 0.0F);
		this.cube.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		pMatrixStack.popPose();
		//GlStateManager.popAttrib();
		//GlStateManager.popMatrix();

	}
}
