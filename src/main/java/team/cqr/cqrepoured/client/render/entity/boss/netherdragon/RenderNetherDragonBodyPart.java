package team.cqr.cqrepoured.client.render.entity.boss.netherdragon;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelNetherDragonBodyGeo;
import team.cqr.cqrepoured.client.render.entity.RenderEntityGeo;
import team.cqr.cqrepoured.entity.boss.netherdragon.SubEntityNetherDragonSegment;

public class RenderNetherDragonBodyPart extends RenderEntityGeo<SubEntityNetherDragonSegment> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/boss/nether_dragon_body.png");
	private static final ResourceLocation MODEL_RESLOC = CQRMain.prefix("geo/entity/boss/netherdragon/body/nether_dragon_body_normal.geo.json");
	
	public RenderNetherDragonBodyPart(EntityRenderDispatcher dispatcher) {
		this(generateContext(dispatcher));
	}
	
	public RenderNetherDragonBodyPart(EntityRendererProvider.Context renderManager) {
		this(renderManager, new ModelNetherDragonBodyGeo(MODEL_RESLOC, TEXTURE, "boss/nether_dragon"));
	}

	public RenderNetherDragonBodyPart(EntityRendererProvider.Context renderManager, GeoModel<SubEntityNetherDragonSegment> modelProvider) {
		super(renderManager, modelProvider);
	}
	
}
