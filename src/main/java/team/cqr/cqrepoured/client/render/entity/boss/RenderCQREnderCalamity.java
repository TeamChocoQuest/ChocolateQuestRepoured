package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.boss.ModelEnderCalamity;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQREnderCalamity;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQREnderCalamity extends RenderCQREntityGeo<EntityCQREnderCalamity> {

	public RenderCQREnderCalamity(RenderManager renderManager) {
		super(renderManager, new ModelEnderCalamity(new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity.png")), "boss/ender_calamity");
	}

	//we do not hold items, so we can ignore this
	@Override
	protected ItemStack getHeldItemForBone(String boneName, EntityCQREnderCalamity currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQREnderCalamity currentEntity) {
		
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQREnderCalamity currentEntity) {
		
	}

}
