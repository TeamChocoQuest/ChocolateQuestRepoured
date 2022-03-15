package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.EquipmentSlotType.Group;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.item.armor.ItemArmorBull;
import team.cqr.cqrepoured.util.ItemUtil;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends LivingEntity {

	protected MixinLivingEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "push(Lnet/minecraft/entity/Entity;)V", cancellable = true)
	private void mixinDoPush(Entity pEntity, CallbackInfo ci) {
		// pEntity is the pusher
		if (pEntity instanceof LivingEntity) {
			if (ItemUtil.hasFullSet((LivingEntity) pEntity, ItemArmorBull.class)) {
				if (pEntity.isSprinting()) {
					int thornLevel = 0;
					for(EquipmentSlotType slot : EquipmentSlotType.values()) {
						if(slot.getType() == Group.ARMOR) {
							ItemStack stack = ((LivingEntity) pEntity).getItemBySlot(slot);
							thornLevel += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.THORNS, stack);
						}
					}
					if(thornLevel > 0) {
						this.hurt(DamageSource.thorns(pEntity), thornLevel * 1.5F);
					}
					
					double myVolume = this.getBbWidth() * this.getBbHeight() * this.getBbWidth();
					double theirVolume = pEntity.getBbWidth() * pEntity.getBbHeight() * pEntity.getBbWidth();

					if((myVolume / 2) <= theirVolume) {
						
						Vector3d velocity = this.position().subtract(pEntity.position());
						velocity = velocity.add(0, 0.1, 0);
						velocity = velocity.multiply(1.5, 1.5, 1.5);
						velocity = velocity.add(this.getDeltaMovement());
						this.setDeltaMovement(velocity);
					}
				}
			}
		}
	}

}
