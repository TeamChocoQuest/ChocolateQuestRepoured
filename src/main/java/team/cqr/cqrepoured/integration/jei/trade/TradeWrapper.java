package team.cqr.cqrepoured.integration.jei.trade;

import java.nio.file.Path;
import java.util.stream.Collectors;

import javax.vecmath.Matrix4f;

import org.lwjgl.opengl.GL11;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import team.cqr.cqrepoured.client.util.MatrixUtil;
import team.cqr.cqrepoured.client.world.structure.preview.StructurePreview;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.Trade;

public class TradeWrapper implements IRecipeWrapper {

	private static final int structureX = 0;
	private static final int structureY = 0;
	private static final int structureWidth = 52;
	private static final int structureHeight = 57;
	private static final int entityX = 54;
	private static final int entityY = 24;
	private static final int entityWidth = 22;
	private static final int entityHeight = 33;

	private final Path structure;
	private final AbstractEntityCQR trader;
	private final Trade trade;

	public TradeWrapper(Path structure, AbstractEntityCQR trader, Trade trade) {
		this.structure = structure;
		this.trader = trader;
		this.trade = trade;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.ITEM, this.trade.getInputItems().stream().map(input -> input.getStack()).collect(Collectors.toList()));
		ingredients.setOutput(VanillaTypes.ITEM, this.trade.getOutput());
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		GlStateManager.enableDepth(); // some item tooltips don't re-enable depth testing

		float pitch = 10.0F;
		float yaw = (float) (System.currentTimeMillis() / 20.0D % 360);

		// render structure preview
		{
			GlStateManager.shadeModel(GL11.GL_SMOOTH);

			StructurePreview structurePreview = StructurePreview.of(this.structure);
			StructureBoundingBox bb = structurePreview.boundingBox();
			float scale = (float) Math.min(structureWidth / Math.sqrt(bb.getXSize() * bb.getXSize() + bb.getZSize() * bb.getZSize()), (double) structureHeight / bb.getYSize());
			float depth = (float) (0.5 * Math.sqrt(bb.getXSize() * bb.getXSize() + bb.getYSize() * bb.getYSize() + bb.getZSize() * bb.getZSize()));

			GlStateManager.pushMatrix();
			GlStateManager.translate(structureX + structureWidth / 2, structureY + structureHeight / 2, 0);
			GlStateManager.scale(1, -1, 1);
			GlStateManager.translate(0.0F, 0.0F, 100.0F);
			GlStateManager.scale(1.0F, 1.0F, 100.0F / depth);
			GlStateManager.scale(1.0F, 1.0F, 1.0F / scale);
			GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.translate(-bb.getXSize() * 0.5F, -bb.getYSize() * 0.5F, -bb.getZSize() * 0.5F);
			GlStateManager.translate(-bb.minX, -bb.minY, -bb.minZ);

			GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, MatrixUtil.FLOAT_BUFFER);
			Matrix4f matrix = MatrixUtil.createMatrixFromBuffer();
			GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, MatrixUtil.FLOAT_BUFFER);
			matrix.mul(MatrixUtil.createMatrixFromBuffer());
			structurePreview.sortTranslucent(matrix);
			structurePreview.draw();

			GlStateManager.popMatrix();

			GlStateManager.shadeModel(GL11.GL_FLAT);
		}

		// render trader preview
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(entityX + entityWidth / 2, entityY + entityHeight * 0.9375, 0);
			GlStateManager.scale(1, -1, 1);
			GlStateManager.translate(0.0F, 0.0F, 100.0F);
			GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
			float scale = (float) Math.min(entityWidth / Math.sqrt((this.trader.width * 1.25) * (this.trader.width * 1.25) * 2.0), (double) entityHeight / (this.trader.height * 1.25));
			GlStateManager.scale(scale, scale, scale);

			GlStateManager.enableColorMaterial();
			RenderHelper.enableStandardItemLighting();
			minecraft.getRenderManager().renderEntity(this.trader, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, true);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.disableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

			GlStateManager.popMatrix();
		}

		if (this.trade.getRequiredReputation() != Integer.MIN_VALUE) {
			minecraft.fontRenderer.drawStringWithShadow("Rep" + " " + this.trade.getRequiredReputation(), 80, 30, 0xFFFFFFFF);
		}
		if (this.trade.getRequiredAdvancement() != null) {
			minecraft.fontRenderer.drawStringWithShadow("Adv" + " " + this.trade.getRequiredAdvancement(), 80, 40, 0xFFFFFFFF);
		}
	}

}
