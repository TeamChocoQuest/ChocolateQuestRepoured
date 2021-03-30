package team.cqr.cqrepoured.client.render.entity.boss;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.boss.ModelGiantTortoiseGecko;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRGiantTortoiseGecko extends RenderCQREntityGeo<EntityCQRGiantTortoise> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_tortoise.png");

	public RenderCQRGiantTortoiseGecko(RenderManager renderManager) {
		super(renderManager, new ModelGiantTortoiseGecko(TEXTURE), "boss/giant_tortoise");
	}

	// we do not hold items, so we can ignore this
	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQRGiantTortoise currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Nullable
	@Override
	protected IBlockState getHeldBlockForBone(String boneName, EntityCQRGiantTortoise currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(IBlockState block, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Override
	protected void postRenderBlock(IBlockState block, String boneName, EntityCQRGiantTortoise currentEntity) {
		// Unused
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRGiantTortoise currentEntity) {
		return null;
	}

}
