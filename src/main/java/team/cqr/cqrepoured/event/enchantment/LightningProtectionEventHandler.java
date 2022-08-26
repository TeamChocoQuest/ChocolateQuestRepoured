package team.cqr.cqrepoured.event.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQREnchantments;

@EventBusSubscriber(modid = CQRMain.MODID)
public class LightningProtectionEventHandler {

	@SubscribeEvent
	public static void onStruckByLightning(EntityStruckByLightningEvent event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) event.getEntity();
			ItemStack helmet = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

			int lvl = EnchantmentHelper.getEnchantmentLevel(CQREnchantments.LIGHTNING_PROTECTION, helmet);
			if(lvl > 0) {
				float percentage = 1.0F - 0.1F * lvl;
				event.setCanceled(true);
				//TODO: Replace with ASM or mixin
				//Lightning damage is by default 5
				living.onStruckByLightning(event.getLightning());
				//Undo the damage caused by above call
				living.heal(5.0F);
				//Apply the correct damage
				living.attackEntityFrom(DamageSource.LIGHTNING_BOLT, percentage * 5.0F);
			}
		}
	}

}
