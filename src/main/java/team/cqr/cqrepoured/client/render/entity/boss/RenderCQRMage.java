package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage.AbstractModelMageGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.boss.AbstractEntityCQRMageBase;

public class RenderCQRMage<T extends AbstractEntityCQRMageBase & IAnimatableCQR> extends RenderCQRBipedBaseGeo<T> {

	public static final ResourceLocation TEXTURES_HIDDEN = new ResourceLocation(CQRConstants.MODID, "textures/entity/boss/mage_hidden.png");
	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(CQRConstants.MODID, "textures/entity/magic_armor/mages.png");

	public RenderCQRMage(Context rendermanagerIn, AbstractModelMageGeo<T> model) {
		super(rendermanagerIn, model);
		
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

}
