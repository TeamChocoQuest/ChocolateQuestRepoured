package team.cqr.cqrepoured.event.item;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import team.cqr.cqrepoured.entity.EntitySlimePart;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorSlime;
import team.cqr.cqrepoured.util.ItemUtil;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class SlimeArmorEventHandler {

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (ItemUtil.hasFullSet(entity, ItemArmorSlime.class) && !CapabilityCooldownHandlerHelper.onCooldown(entity, CQRItems.CHESTPLATE_SLIME)) {
			if (!entity.world.isRemote) {
				EntitySlimePart slime = new EntitySlimePart(entity.world, entity);
				double x = entity.posX - 5.0D + 2.5D * slime.getRNG().nextDouble();
				double y = entity.posY;
				double z = entity.posZ - 5.0D + 2.5D * slime.getRNG().nextDouble();
				slime.setPosition(x, y, z);
				entity.world.spawnEntity(slime);
			}
			CapabilityCooldownHandlerHelper.setCooldown(entity, CQRItems.CHESTPLATE_SLIME, 160);
		}
	}

}
