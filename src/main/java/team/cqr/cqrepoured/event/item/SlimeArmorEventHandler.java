package team.cqr.cqrepoured.event.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import team.cqr.cqrepoured.entity.EntitySlimePart;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorSlime;
import team.cqr.cqrepoured.util.ItemUtil;

@EventBusSubscriber(modid = CQRConstants.MODID)
public class SlimeArmorEventHandler {

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();

		if (ItemUtil.hasFullSet(entity, ItemArmorSlime.class) && !CapabilityCooldownHandlerHelper.onCooldown(entity, CQRItems.CHESTPLATE_SLIME.get())) {
			if (!entity.level().isClientSide()) {
				EntitySlimePart slime = new EntitySlimePart(CQREntityTypes.SMALL_SLIME.get(), entity.level(), entity);
				double x = entity.getX() - 5.0D + 2.5D * slime.getRandom().nextDouble();
				double y = entity.getY();
				double z = entity.getZ() - 5.0D + 2.5D * slime.getRandom().nextDouble();
				slime.setPos(x, y, z);
				entity.level().addFreshEntity(slime);
			}
			CapabilityCooldownHandlerHelper.setCooldown(entity, CQRItems.CHESTPLATE_SLIME.get(), 160);
		}
	}

}
