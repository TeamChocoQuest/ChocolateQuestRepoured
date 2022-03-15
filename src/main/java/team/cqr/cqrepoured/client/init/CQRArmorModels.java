package team.cqr.cqrepoured.client.init;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import team.cqr.cqrepoured.client.model.armor.ModelArmorBull;
import team.cqr.cqrepoured.client.model.armor.ModelArmorHeavy;
import team.cqr.cqrepoured.client.model.armor.ModelArmorInquisition;
import team.cqr.cqrepoured.client.model.armor.ModelArmorSpider;
import team.cqr.cqrepoured.client.model.armor.ModelArmorTransparent;
import team.cqr.cqrepoured.client.model.armor.ModelArmorTurtle;
import team.cqr.cqrepoured.client.model.armor.ModelBackpack;
import team.cqr.cqrepoured.client.model.armor.ModelCrown;

public class CQRArmorModels {

	public static BipedModel<? extends LivingEntity> BACKPACK = new ModelBackpack<>(0);
	
	public static BipedModel<? extends LivingEntity> SLIME_ARMOR = new ModelArmorTransparent<>(0.75F);
	public static BipedModel<? extends LivingEntity> SLIME_ARMOR_LEGS = new ModelArmorTransparent<>(0.375F);
	
	public static BipedModel<? extends LivingEntity> TURTLE_ARMOR = new ModelArmorTurtle<>(1.0F);
	public static BipedModel<? extends LivingEntity> TURTLE_ARMOR_LEGS = new ModelArmorTurtle<>(0.5F);
	
	public static BipedModel<? extends LivingEntity> BULL_ARMOR = new ModelArmorBull<>(1.0F);
	public static BipedModel<? extends LivingEntity> BULL_ARMOR_LEGS = new ModelArmorBull<>(0.5F);
	
	public static BipedModel<? extends LivingEntity> ARMOR_HEAVY = new ModelArmorHeavy<>(1.0F);
	public static BipedModel<? extends LivingEntity> ARMOR_HEAVY_LEGS = new ModelArmorHeavy<>(0.5F);
	
	public static BipedModel<? extends LivingEntity> INQUISITION_ARMOR = new ModelArmorInquisition<>(1.0F);
	public static BipedModel<? extends LivingEntity> INQUISITION_ARMOR_LEGS = new ModelArmorInquisition<>(0.5F);
	
	public static BipedModel<? extends LivingEntity> SPIDER_ARMOR = new ModelArmorSpider<>(1.0F);
	public static BipedModel<? extends LivingEntity> SPIDER_ARMOR_LEGS = new ModelArmorSpider<>(0.5F);
	
	public static BipedModel<? extends LivingEntity> CROWN = new ModelCrown<LivingEntity>(1.0F);

}
