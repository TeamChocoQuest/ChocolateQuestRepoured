package team.cqr.cqrepoured.client.util;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import team.cqr.cqrepoured.client.model.entity.ModelPentagram;

public class PentagramUtil {

	public static void renderPentagram(PoseStack matrix, RenderType renderType, float ageInTicks) {
		float r = 0.6F + 0.4F * Mth.sin(0.25F * ageInTicks);
		float g = 0.0F;
		float b = 0.0F;
		renderPentagram(matrix, renderType, r, g, b, 5);
	}

	public static void renderPentagram(PoseStack matrix, RenderType renderType, float r, float g, float b, int corners) {
		//Adjusted to the stuff 16.5 does internally
		//RenderSystem.color3f(r, g, b);
		GL11.glColor4f(r,  g,  b,  1);
		ModelPentagram.render(matrix, renderType, corners, 0.75F, 0.05F, 0.05F);
		//RenderSystem.color3f(1.0F, 1.0F, 1.0F);
		GL11.glColor4f(1, 1, 1, 1);
	}

}
