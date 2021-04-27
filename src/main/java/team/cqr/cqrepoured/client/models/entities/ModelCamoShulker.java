package team.cqr.cqrepoured.client.models.entities;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.render.texture.InvisibilityTexture;
import team.cqr.cqrepoured.client.util.BlockRenderUtil;
import team.cqr.cqrepoured.objects.entity.misc.EntityCamoShulker;

public class ModelCamoShulker extends ModelShulker {
	
	private final Render<? extends Entity> renderer;
	
	public ModelCamoShulker(Render<? extends Entity> renderer) {
		super();
		this.lid.showModel = false;
		this.base.showModel = false;
		this.renderer = renderer;
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		//TODO: Only rotate the head
		this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		//TODO: Render only the box block
		EntityCamoShulker entityshulker = (EntityCamoShulker)entityIn;
        float f = ageInTicks - (float)entityshulker.ticksExisted;
        float f1 = (0.5F + entityshulker.getClientPeekAmount(f)) * (float)Math.PI;
        float f2 = -1.0F + MathHelper.sin(f1);
        float f3 = 0.0F;

        if (f1 > (float)Math.PI)
        {
            f3 = MathHelper.sin(ageInTicks * 0.1F) * 0.7F;
        }

        IBlockState camoBlock = entityshulker.getCamoBlock();
        
        float peekAmount = MathHelper.sin(f1) * 8.0F + f3;
        
        //TODO: Apply closing/opening animation
        //this.lid.setRotationPoint(0.0F, 16.0F + MathHelper.sin(f1) * 8.0F + f3, 0.0F);

        //The following is only to rotate the lid, we only need the upper part
        if (entityshulker.getClientPeekAmount(f) > 0.3F)
        {
        	//TODO: Apply closing/opening animation
            //this.lid.rotateAngleY = f2 * f2 * f2 * f2 * (float)Math.PI * 0.125F;
        }
        else
        {
        	//TODO: Apply closing/opening animation
            //this.lid.rotateAngleY = 0.0F;
        }
	}

}
