package team.cqr.cqrepoured.client.model.geo.armor;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

public class ModelKingCrownGeo extends AnimatedGeoModel<ItemArmorCrown> {
	
	protected static final ResourceLocation MODEL_RESLOC = CQRMain.prefix("geo/armor/crown.geo.json");
	protected static final ResourceLocation TEXTURE_RESLOC = CQRMain.prefix("textures/models/armor/king_crown.png");

	@Override
	public ResourceLocation getAnimationFileLocation(ItemArmorCrown animatable) {
		return CQRAnimations.Armor.KING_CROWN;
	}

	@Override
	public ResourceLocation getModelLocation(ItemArmorCrown object) {
		return MODEL_RESLOC;
	}

	@Override
	public ResourceLocation getTextureLocation(ItemArmorCrown object) {
		return TEXTURE_RESLOC;
	}

}