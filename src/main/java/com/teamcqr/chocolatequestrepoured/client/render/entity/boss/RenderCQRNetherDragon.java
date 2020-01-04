package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonHead;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRNetherDragon extends RenderLiving<EntityCQRNetherDragon> {

	public static final ResourceLocation TEXTURES_NORMAL = new ResourceLocation((Reference.MODID + ":textures/entity/boss/nether_dragon.png"));
	public static final ResourceLocation TEXTURES_SKELETAL = new ResourceLocation((Reference.MODID + ":textures/entity/boss/skeletal_nether_dragon.png"));

	private ModelNetherDragonHead model = new ModelNetherDragonHead();
	private int animState = 0;
	private boolean mouthIsOpen = false;

	public RenderCQRNetherDragon(RenderManager manager, ModelNetherDragonHead model) {
		// super(manager, new ModelNetherDragon(), 0.5F);
		super(manager, model, 0.5F);

		this.model = model;
	}

	@Override
	public void doRender(EntityCQRNetherDragon entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		// DONE: Rotate move around z axis when the mouth is open

		if (this.animState < 11 && entity.isMouthOpen() && !this.mouthIsOpen) {
			float angle = (this.animState) * 3.375F;
			this.model.Mouth_Bottom.rotateAngleZ = new Float(Math.toRadians(angle));

			this.animState++;
			if (this.animState == 11) {
				this.mouthIsOpen = true;
				this.animState = 0;
			}
		}

		else if (this.animState < 11 && !entity.isMouthOpen() && this.mouthIsOpen) {
			// this.Mouth_Bottom.rotateAngleZ = new Float(Math.toRadians(33.75D));
			float angle = (10 - this.animState) * 3.375F;
			this.model.Mouth_Bottom.rotateAngleZ = new Float(Math.toRadians(angle));

			this.animState++;
			if (this.animState == 11) {
				this.mouthIsOpen = false;
				this.animState = 0;
			}
		} else {

		}

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRNetherDragon entity) {
		return TEXTURES_NORMAL;
	}

}
