package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRBiped;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class LayerCQREntityCape implements LayerRenderer<AbstractEntityCQR> {

	private final RenderCQREntity playerRenderer;

    public LayerCQREntityCape(RenderCQREntity renderCQREntity)
    {
        this.playerRenderer = renderCQREntity;
    }

    public void doRenderLayer(AbstractEntityCQR entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if(!(this.playerRenderer.getMainModel() instanceof ModelCQRBiped)) {
    		return;
    	}
    	
        if (/*entitylivingbaseIn.hasPlayerInfo() &&*/ !entitylivingbaseIn.isInvisible() && entitylivingbaseIn.hasCape() && entitylivingbaseIn.getResourceLocationOfCape() != null)
        {
            ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (itemstack.getItem() != Items.ELYTRA)
            {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.playerRenderer.bindTexture(entitylivingbaseIn.getResourceLocationOfCape());
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.125F);
                //ChasingPos: Positions of the cape
                double d0 = entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks);
                double d1 = entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks);
                double d2 = entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks);
                float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
                double d3 = (double)MathHelper.sin(f * 0.017453292F);
                double d4 = (double)(-MathHelper.cos(f * 0.017453292F));
                float f1 = (float)d1 * 10.0F;
                f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
                float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
                float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;

                if (f2 < 0.0F)
                {
                    f2 = 0.0F;
                }

                float f4 = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

                if (entitylivingbaseIn.isSneaking())
                {
                    f1 += 25.0F;
                }

                GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                //DONE: Base model for Human shaped entities for capes
                ((ModelCQRBiped)this.playerRenderer.getMainModel()).renderCape(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
