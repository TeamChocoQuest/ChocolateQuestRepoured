package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRGiantTortoise extends RenderLiving<EntityCQRGiantTortoise> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_tortoise.png");

	private ModelGiantTortoise model = new ModelGiantTortoise();
	private int animState = 0;
	private boolean mouthIsOpen = false;

	public RenderCQRGiantTortoise(RenderManager rendermanagerIn, ModelGiantTortoise modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantTortoise entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(EntityCQRGiantTortoise entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		// DONE: Rotate move around z axis when the mouth is open

		switch (entity.getCurrentAnimation()) {
		case HEALING:
			/*
			 * entity.world.spawnParticle(EnumParticleTypes.HEART, entity.posX - 0.5, entity.posY + 1.0D, entity.posZ - 0.5, 1, 1.5, 1);
			 */
			// TODO: Heart particles
			break;
		case MOVE_PARTS_IN:
			break;
		case MOVE_PARTS_OUT:
			break;
		case NONE:
			break;
		case SPIN:
			break;
		case SPIN_DOWN:
			break;
		case SPIN_UP:
			break;
		case WALKING:
			break;
		default:
			break;

		}

		if (this.animState < 11 && entity.isMouthOpen() && !this.mouthIsOpen) {
			float angle = (this.animState) * 3.375F;
			this.model.jaw.rotateAngleZ = new Float(Math.toRadians(angle));

			this.animState++;
			if (this.animState == 11) {
				this.mouthIsOpen = true;
				this.animState = 0;
			}
		}

		else if (this.animState < 11 && !entity.isMouthOpen() && this.mouthIsOpen) {
			// this.Mouth_Bottom.rotateAngleZ = new Float(Math.toRadians(33.75D));
			float angle = (10 - this.animState) * 3.375F;
			this.model.jaw.rotateAngleZ = new Float(Math.toRadians(angle));

			this.animState++;
			if (this.animState == 11) {
				this.mouthIsOpen = false;
				this.animState = 0;
			}
		} else {

		}

	}

}
