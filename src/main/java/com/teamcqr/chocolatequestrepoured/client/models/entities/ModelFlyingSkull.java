package com.teamcqr.chocolatequestrepoured.client.models.entities;

import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityFlyingSkullMinion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

/**
 * Skullo - ArloTheEpic
 * Created using Tabula 7.0.1
 */
public class ModelFlyingSkull extends ModelBase {

	public ModelRenderer skull;
    public ModelRenderer jaw;

    public ModelFlyingSkull(float scale) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.jaw = new ModelRenderer(this, 0, 14);
        this.jaw.setRotationPoint(0.0F, 2.0F, 4.0F);
        this.jaw.addBox(-4.0F, 0.0F, -8.0F, 8, 2, 8, scale);
        this.skull = new ModelRenderer(this, 0, 0);
        this.skull.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skull.addBox(-4.0F, -4.0F, -4.0F, 8, 6, 8, scale);
        this.skull.addChild(this.jaw);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) { 
        if(entity instanceof EntityFlyingSkullMinion) {
        	this.skull.rotateAngleY = new Float(Math.atan2(entity.motionX, entity.motionZ));
        	//this.skull.rotateAngleX = (entity.motionY == 0 ? 0F : (new Float(Math.atan2(entity.motionY, 0)))) -90F;
        	this.skull.rotateAngleX = headPitch +180;
        	this.jaw.rotateAngleX = new Float(Math.toRadians(22.5D * (0.5D*(1D + (Math.sin( ((2D * Math.PI) / 8) * entity.ticksExisted))))));
        	this.skull.render(scale);
        	Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.SPELL_WITCH, entity.getPositionVector().x, entity.getPositionVector().y + 0.02, entity.getPositionVector().z, 0F, 0.5F, 0F, 2);
        }
    }

}
