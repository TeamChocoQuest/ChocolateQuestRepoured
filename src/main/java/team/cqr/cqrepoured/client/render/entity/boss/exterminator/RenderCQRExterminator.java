package team.cqr.cqrepoured.client.render.entity.boss.exterminator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.boss.ModelExterminator;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.objects.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRExterminator extends RenderCQREntityGeo<EntityCQRExterminator> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/exterminator.png");
	
	public RenderCQRExterminator(RenderManager renderManager) {
		super(renderManager, new ModelExterminator(TEXTURE), "boss/exterminator");
	}

	public static final String HAND_IDENT_LEFT = "item_left_hand";
	
	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQRExterminator currentEntity) {
		if(boneName.equalsIgnoreCase(HAND_IDENT_LEFT)) {
			return currentEntity.getHeldItem(EnumHand.MAIN_HAND);
		}
		return null;
	}
	
	@Override
	protected float getDeathMaxRotation(EntityCQRExterminator entityLivingBaseIn) {
		return 0.0F;
	}

	@Override
	protected IBlockState getHeldBlockForBone(String boneName, EntityCQRExterminator currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected void preRenderBlock(IBlockState block, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderBlock(IBlockState block, String boneName, EntityCQRExterminator currentEntity) {
		// Unused
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRExterminator currentEntity) {
		// Unused
		return null;
	}

}
