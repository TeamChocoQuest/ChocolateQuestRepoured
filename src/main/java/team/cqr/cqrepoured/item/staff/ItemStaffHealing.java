package team.cqr.cqrepoured.item.staff;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.ISupportWeapon;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.item.sword.ItemFakeSwordHealingStaff;

public class ItemStaffHealing extends ItemLore implements ISupportWeapon<ItemFakeSwordHealingStaff> {

	public static final float HEAL_AMOUNT_ENTITIES = 4.0F;

	public ItemStaffHealing(Properties properties)
	{
		super(properties.durability(128).stacksTo(1));
		//this.setMaxDamage(128);
		//this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (!player.level.isClientSide && entity instanceof LivingEntity && !player.getCooldowns().isOnCooldown(stack.getItem())) {
			((LivingEntity) entity).heal(HEAL_AMOUNT_ENTITIES);
			entity.setRemainingFireTicks(0);
			((ServerWorld) player.level).sendParticles(ParticleTypes.HEART, entity.position().x, entity.position().y + entity.getBbHeight() * 0.5D, entity.position().z, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			player.level.playSound(null, entity.position().x, entity.position().y + entity.getBbHeight() * 0.5D, entity.position().z, CQRSounds.MAGIC, SoundCategory.MASTER, 0.6F, 0.6F + random.nextFloat() * 0.2F);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
			player.getCooldowns().addCooldown(stack.getItem(), 30);
		}
		return true;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.0D, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

	@Override
	public ItemFakeSwordHealingStaff getFakeWeapon() {
		return CQRItems.DIAMOND_SWORD_FAKE_HEALING_STAFF.get();
	}

}
