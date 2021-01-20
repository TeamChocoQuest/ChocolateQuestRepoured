package team.cqr.cqrepoured.client.render.entity.boss;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.boss.ModelEnderCalamity;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.util.SphereHelper;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity.HANDS;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQREnderCalamity extends RenderCQREntityGeo<EntityCQREnderCalamity> {
	
	private final SphereHelper sphereHelper;
	
	private static final ResourceLocation SPHERE_TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity_shield.png");
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity.png");

	public RenderCQREnderCalamity(RenderManager renderManager) {
		super(renderManager, new ModelEnderCalamity(TEXTURE), "boss/ender_calamity");
		this.sphereHelper = new SphereHelper(1.5F, SPHERE_TEXTURE, CQRConfig.bosses.enderCalamityShieldRoundness);
	}
	
	@Override
	public boolean isMultipass() {
		return true;
	}
	
	@Override
	public void renderMultipass(EntityCQREnderCalamity entityIn, double x, double y, double z, float entityYaw, float partialTicks) {
		super.renderMultipass(entityIn, x, y, z, entityYaw, partialTicks);
		
		//since the sphere is transparent it needs to render in the "transparent entity" render-pass
		if (entityIn.isShieldActive()) {
			GlStateManager.pushMatrix();
			
			this.bindTexture(SPHERE_TEXTURE);
			
			float color = new Float(0.5 + 0.5 * (0.5 * Math.cos(0.0625 * entityIn.ticksExisted) + 0.5));
			
			//"Animation"
			GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float)entityIn.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            
			sphereHelper.render(entityIn, x,y,z, TEXTURE, color, color, color, 0.5F, this.getWidthScale(entityIn), this.getHeightScale(entityIn));
			
			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
			GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
			
			this.bindTexture(getEntityTexture(entityIn));
			
			GlStateManager.popMatrix();
		}
	}

	// we do not hold items, so we can ignore this
	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQREnderCalamity currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQREnderCalamity currentEntity) {
		//Unused
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQREnderCalamity currentEntity) {
		//Unused
	}


	@Nullable
	@Override
	protected IBlockState getHeldBlockForBone(String boneName, EntityCQREnderCalamity currentEntity) {
		Optional<IBlockState> optional = currentEntity.getBlockFromHand(HANDS.getFromBoneName(boneName));
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	protected void preRenderBlock(IBlockState block, String boneName, EntityCQREnderCalamity currentEntity) {
		//Unused
		GlStateManager.scale(0.9, 0.9, 0.9);
	}

	@Override
	protected void postRenderBlock(IBlockState block, String boneName, EntityCQREnderCalamity currentEntity) {
		//Unused
	}
	

}
