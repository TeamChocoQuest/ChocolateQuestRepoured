package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityBubble;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBubble extends Render<EntityBubble> {

	public static int sphereIdOutside;
	public static int sphereIdInside;

	public RenderBubble(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		return;
	}
	
	@Override
	public void doRender(EntityBubble entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		GL11.glPushMatrix();
	    GL11.glTranslated(x, y + entity.height / 2, z);
	    GL11.glScalef(entity.getLowestRidingEntity().height /1.5F, entity.getLowestRidingEntity().height /1.5F, entity.getLowestRidingEntity().height /1.5F);
	    GL11.glEnable(GL11.GL_BLEND);

	    GL11.glDepthMask(false);


	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
	    GL11.glEnable(GL11.GL_ALPHA_TEST);

	    GL11.glCallList(sphereIdOutside);


	    GL11.glCallList(sphereIdInside);
	    GL11.glPopMatrix();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		Sphere sphere = new Sphere();

//Set up paramters that are common to both outside and inside.

//GLU_FILL as a solid.
		sphere.setDrawStyle(GLU.GLU_FILL);
//GLU_SMOOTH will try to smoothly apply lighting
		sphere.setNormals(GLU.GLU_SMOOTH);

//First make the call list for the outside of the sphere

		sphere.setOrientation(GLU.GLU_OUTSIDE);

		sphereIdOutside = GL11.glGenLists(1);
//Create a new list to hold our sphere data.
		GL11.glNewList(sphereIdOutside, GL11.GL_COMPILE);
//binds the texture 
		ResourceLocation rL = new ResourceLocation(Reference.MODID + ":textures/entities/bubble_sphere.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
//The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
//control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
		sphere.draw(0.5F, 32, 32);
		GL11.glEndList();

//Now make the call list for the inside of the sphere
		sphere.setOrientation(GLU.GLU_INSIDE);
		sphereIdInside = GL11.glGenLists(1);
//Create a new list to hold our sphere data.
		GL11.glNewList(sphereIdInside, GL11.GL_COMPILE);
		Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
		sphere.draw(0.5F, 32, 32);
		GL11.glEndList();

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBubble entity) {
		return null;
	}
	
	

}
