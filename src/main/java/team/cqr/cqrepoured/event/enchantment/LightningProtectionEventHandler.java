package team.cqr.cqrepoured.event.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQREnchantments;

@EventBusSubscriber(modid = CQRMain.MODID)
public class LightningProtectionEventHandler {

	@SubscribeEvent
	public static void onStruckByLightning(LivingHurtEvent event) {
		if (event.getSource() == DamageSource.LIGHTNING_BOLT) {
			EntityLivingBase living = event.getEntityLiving();
			ItemStack helmet = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

			int lvl = EnchantmentHelper.getEnchantmentLevel(CQREnchantments.LIGHTNING_PROTECTION, helmet);
			if (lvl > 0) {
				event.setAmount(event.getAmount() * Math.max(1.0F - 0.1F * lvl, 0.0F));
			}
		}
	}

}
