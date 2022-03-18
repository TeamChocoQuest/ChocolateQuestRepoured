package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.EquipmentSlotType.Group;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.item.armor.ItemArmorBull;
import team.cqr.cqrepoured.item.armor.ItemArmorSlime;
import team.cqr.cqrepoured.util.ItemUtil;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

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
	
	@Inject(at = @At("HEAD"), method = "travel(Lnet/minecraft/util/math/vector/Vector3d;)V", cancellable = true)
	private void mixinTravel(Vector3d vectorIn, CallbackInfo ci) {
		 if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
			 FluidState fluidstate = this.level.getFluidState(this.blockPosition());
	         if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
	        	 //Not needed here
	         } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
	        	 //Not needed here
	         } else if (this.isFallFlying()) {
	        	 //We're elytra flying
	        	 if(ItemUtil.hasFullSet(this, ItemArmorSlime.class) && (this.horizontalCollision || this.verticalCollision)) {
	        		 Vector3d lastMovedToPos = this.getBoundingBox().getCenter().subtract(this.getBoundingBox().getXsize(), this.getBoundingBox().getYsize(), this.getBoundingBox().getZsize());
	        		 //TODO: Add AT for this, couldn't find the correct mapping using the CSV file
	        		 Vector3d vector3d = this.collide(lastMovedToPos);
	        		 //Taken from the calculations for this.horizontalCollison
	        		 boolean collidedOnXAxis = MathHelper.equal(lastMovedToPos.x, vector3d.x);
	        		 boolean collidedOnZAxis = MathHelper.equal(lastMovedToPos.z, vector3d.z);
	        		 
	        		 Vector3d resultingVector = this.getDeltaMovement().scale(0.75);
	        		 resultingVector = resultingVector.multiply(collidedOnXAxis ? -1 : 1, this.verticalCollision ? -1 : 1, collidedOnZAxis ? -1 : 1);
	        		 
	        		 this.setDeltaMovement(resultingVector);
	        	 }
	         }
		 }
	}

	@Shadow
	private boolean isFallFlying() {
		throw new IllegalStateException("Mixin failed to shadow isFallFlying()");
	}

	@Shadow
	private boolean canStandOnFluid(Fluid type) {
		throw new IllegalStateException("Mixin failed to shadow canStandOnFluid()");
	}

	@Shadow
	public boolean isAffectedByFluids() {
		throw new IllegalStateException("Mixin failed to shadow isAffectedByFluids()");
	}

	@Shadow
	public boolean isEffectiveAi() {
		throw new IllegalStateException("Mixin failed to shadow isEffectiveAi()");
	}

}
