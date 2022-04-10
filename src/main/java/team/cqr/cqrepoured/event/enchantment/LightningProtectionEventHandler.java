package team.cqr.cqrepoured.event.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQREnchantments;

@EventBusSubscriber(modid = CQRMain.MODID)
public class LightningProtectionEventHandler {

	@SubscribeEvent
	public static void onStruckByLightning(EntityStruckByLightningEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) event.getEntity();
			ItemStack helmet = living.getItemBySlot(EquipmentSlotType.HEAD);

			int lvl = EnchantmentHelper.getItemEnchantmentLevel(CQREnchantments.LIGHTNING_PROTECTION.get(), helmet);
			if (lvl > 0 && lvl > living.getRandom().nextInt(10)) {
				event.setCanceled(true);
			}
		}
	}

}
