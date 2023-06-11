package team.cqr.cqrepoured.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.RenderType;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;
import team.cqr.cqrepoured.client.model.entity.ModelPentagram;

public class PentagramUtil {

	public static void renderPentagram(MatrixStack matrix, RenderType renderType, float ageInTicks) {
		float r = 0.6F + 0.4F * MathHelper.sin(0.25F * ageInTicks);
		float g = 0.0F;
		float b = 0.0F;
		renderPentagram(matrix, renderType, r, g, b, 5);
	}

	@SuppressWarnings("deprecation")
	public static void renderPentagram(MatrixStack matrix, RenderType renderType, float r, float g, float b, int corners) {
		RenderSystem.color3f(r, g, b);
		ModelPentagram.render(matrix, renderType, corners, 0.75F, 0.05F, 0.05F);
		RenderSystem.color3f(1.0F, 1.0F, 1.0F);
	}

}
