package team.cqr.cqrepoured.client.render.entity.boss.endercalamity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQREndermanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerGlowingAreasGeo;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderKing;

@OnlyIn(Dist.CLIENT)
public class RenderCQREnderKing extends RenderCQRBipedBaseGeo<EntityCQREnderKing> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/enderman.png");

	public RenderCQREnderKing(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQREndermanGeo<EntityCQREnderKing>(CQRMain.prefix("geo/entity/biped_enderman.geo.json"), TEXTURE, "mob/enderman"));
		
		this.addLayer(new LayerGlowingAreasGeo<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	@Override
	public float getWidthScale(EntityCQREnderKing entity) {
		float superVal = super.getWidthScale(entity);
		if (entity.isWide()) {
			superVal *= 2;
		}
		return superVal;
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQREnderKing currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQREnderKing currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQREnderKing currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQREnderKing currentEntity, IBone bone) {
		
	}

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQREnderKing currentEntity) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQREnderKing currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQREnderKing currentEntity) {
		
	}

}
