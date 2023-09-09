package team.cqr.cqrepoured.client.init;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import team.cqr.cqrepoured.client.model.armor.*;

public class CQRArmorModels {

	public static HumanoidModel<? extends LivingEntity> SLIME_ARMOR = new ModelArmorTransparent<>(0.75F);
	public static HumanoidModel<? extends LivingEntity> SLIME_ARMOR_LEGS = new ModelArmorTransparent<>(0.375F);
	
	public static HumanoidModel<? extends LivingEntity> ARMOR_HEAVY = new ModelArmorHeavy<>(1.0F);
	public static HumanoidModel<? extends LivingEntity> ARMOR_HEAVY_LEGS = new ModelArmorHeavy<>(0.5F);
	
	public static HumanoidModel<? extends LivingEntity> INQUISITION_ARMOR = new ModelArmorInquisition<>(1.0F);
	public static HumanoidModel<? extends LivingEntity> INQUISITION_ARMOR_LEGS = new ModelArmorInquisition<>(0.5F);
	
	public static HumanoidModel<? extends LivingEntity> SPIDER_ARMOR = new ModelArmorSpider<>(1.0F);
	public static HumanoidModel<? extends LivingEntity> SPIDER_ARMOR_LEGS = new ModelArmorSpider<>(0.5F);

}
