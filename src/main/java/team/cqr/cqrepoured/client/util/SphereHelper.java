package team.cqr.cqrepoured.client.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class SphereHelper {

	private int sphereIdOutside;
	private int sphereIdInside;
	private final ResourceLocation texture;
	private final float radius;

	private Sphere sphere;

	public SphereHelper(float radius, ResourceLocation tex, int detail) {
		this.texture = tex;
		this.radius = radius;

		this.sphere = new Sphere();
		// GLU_FILL as a solid.
		sphere.setDrawStyle(GLU.GLU_FILL);
		// GLU_SMOOTH will try to smoothly apply lighting
		sphere.setNormals(GLU.GLU_SMOOTH);

		// First make the call list for the outside of the sphere

		sphere.setOrientation(GLU.GLU_OUTSIDE);
		sphere.setTextureFlag(true);

		sphereIdOutside = GlStateManager.glGenLists(1);
		// Create a new list to hold our sphere data.
		GlStateManager.glNewList(sphereIdOutside, GL11.GL_COMPILE);
		// binds the texture
		ResourceLocation rL = this.texture;
		Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
		// The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
		// control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
		sphere.draw(this.radius + 0.05F, detail, detail);
		GlStateManager.glEndList();

		// Now make the call list for the inside of the sphere
		sphere.setOrientation(GLU.GLU_INSIDE);
		sphereIdInside = GlStateManager.glGenLists(1);
		// Create a new list to hold our sphere data.
		GlStateManager.glNewList(sphereIdInside, GL11.GL_COMPILE);
		Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
		sphere.draw(this.radius, detail, detail);
		GlStateManager.glEndList();
	}

	public void render(Entity entity, double x, double y, double z, ResourceLocation previousTexture, float colorR, float colorG, float colorB, float colorA, double scaleW, double scaleH) {
		GlStateManager.translate(x, y + entity.height / 2, z);
		GlStateManager.scale(scaleW, scaleH, scaleW);
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		/*
		 * boolean flag = entity.isInvisible(); GlStateManager.depthMask(flag);
		 */
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		GlStateManager.color(colorR, colorG, colorB, colorA);

		GlStateManager.callList(this.sphereIdOutside);
		GlStateManager.callList(this.sphereIdInside);

		GlStateManager.resetColor();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		// GlStateManager.depthMask(!flag);
		Minecraft.getMinecraft().getTextureManager().bindTexture(previousTexture);
	}

}
