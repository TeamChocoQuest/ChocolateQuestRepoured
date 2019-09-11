package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelNetherDragonHead;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRNetherDragon extends RenderLiving<EntityCQRNetherDragon> {

	public static final ResourceLocation TEXTURES_NORMAL = new ResourceLocation((Reference.MODID + ":textures/entity/boss/nether_dragon.png"));
	public static final ResourceLocation TEXTURES_SKELETAL = new ResourceLocation((Reference.MODID + ":textures/entity/boss/skeletal_nether_dragon.png"));

	public RenderCQRNetherDragon(RenderManager manager) {
		//super(manager, new ModelNetherDragon(), 0.5F);
		super(manager, new ModelNetherDragonHead(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRNetherDragon entity) {
		return TEXTURES_NORMAL;
	}

}
