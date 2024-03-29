package team.cqr.cqrepoured.event.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.init.CQREnchantments;

@EventBusSubscriber(modid = CQRConstants.MODID)
public class LightningProtectionEventHandler {

	@SubscribeEvent
	public static void onStruckByLightning(EntityStruckByLightningEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) event.getEntity();
			ItemStack helmet = living.getItemBySlot(EquipmentSlot.HEAD);

			int lvl = EnchantmentHelper.getItemEnchantmentLevel(CQREnchantments.LIGHTNING_PROTECTION.get(), helmet);
			if (lvl > 0 && lvl > living.getRandom().nextInt(10)) {
				event.setCanceled(true);
			}
		}
	}

}
