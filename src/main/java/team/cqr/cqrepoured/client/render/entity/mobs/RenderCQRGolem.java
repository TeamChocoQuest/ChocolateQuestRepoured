package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.client.models.entities.mobs.ModelCQRGolem;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRGolem;

public class RenderCQRGolem extends RenderCQREntity<EntityCQRGolem> {

	public RenderCQRGolem(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGolem(), 0.5F, "mob/golem", 1.0D, 1.0D);
	}

	@Override
	public void doRender(EntityCQRGolem entity, double x, double y, double z, float entityYaw, float partialTicks) {
		Vec3d endPos = entity.getPositionVector().add(entity.getLookVec().normalize().scale(10));
		ElectricFieldRenderUtil.renderElectricLineBetween(new Vec3d(x,y,z), new Vec3d(x,y,z).add(endPos.subtract(entity.getPositionVector())), entity.getRNG(), 0.5D, x,y,z, 1);
		
		ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(entity, x, y, z);
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
