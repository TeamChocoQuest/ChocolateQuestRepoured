package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.effectslib.api.ExtendedMobEffectHolder;
import team.cqr.cqrepoured.api.effect.SynchableMobEffect;
import team.cqr.cqrepoured.item.armor.ItemArmorBull;
import team.cqr.cqrepoured.util.ItemUtil;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

	protected MixinLivingEntity(EntityType<? extends LivingEntity> type, Level world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "push(Lnet/minecraft/entity/Entity;)V", cancellable = true)
	private void mixinDoPush(Entity pEntity, CallbackInfo ci) {
		// pEntity is the pusher
		if (pEntity instanceof LivingEntity) {
			if (ItemUtil.hasFullSet((LivingEntity) pEntity, ItemArmorBull.class)) {
				if (pEntity.isSprinting()) {
					int thornLevel = 0;
					for(EquipmentSlot slot : EquipmentSlot.values()) {
						if(slot.getType() == EquipmentSlot.Type.ARMOR) {
							ItemStack stack = ((LivingEntity) pEntity).getItemBySlot(slot);
							thornLevel += stack.getEnchantmentLevel(Enchantments.THORNS);
						}
					}
					if(thornLevel > 0) {
						this.hurt(pEntity.damageSources().thorns(pEntity), thornLevel * 1.5F);
					}
					
					double myVolume = this.getBbWidth() * this.getBbHeight() * this.getBbWidth();
					double theirVolume = pEntity.getBbWidth() * pEntity.getBbHeight() * pEntity.getBbWidth();

					if((myVolume / 2) <= theirVolume) {
						
						Vec3 velocity = this.position().subtract(pEntity.position());
						velocity = velocity.add(0, 0.5, 0);
						velocity = velocity.multiply(1.5, 1.5, 1.5);
						velocity = velocity.add(this.getDeltaMovement());
						this.setDeltaMovement(velocity);
					}
				}
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "travel(Lnet/minecraft/util/math/vector/Vector3d;)V", cancellable = true)
	private void mixinTravel(Vec3 vectorIn, CallbackInfo ci) {
		//Doesn't work yet
		/*Entity ent = (Entity) this;
		 if ((this.isEffectiveAi() || this.isControlledByLocalInstance())) {
			 FluidState fluidstate = this.level.getFluidState(this.blockPosition());
	         if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
	        	 //Not needed here
	         } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
	        	 //Not needed here
	         } else if (this.isFallFlying() || !this.isOnGround()) {
	        	 //We're elytra flying
	        	// System.out.println("Falldistance: " + this.fallDistance);
	        	 if(ItemUtil.hasFullSet(this, ItemArmorSlime.class) && (this.horizontalCollision || this.verticalCollision) && this.fallDistance > 3) {
	        		 Vector3d lastMovedToPos = this.getBoundingBox().getCenter().subtract(this.getBoundingBox().getXsize(), this.getBoundingBox().getYsize(), this.getBoundingBox().getZsize());
	        		 //TODO: Add AT for this, couldn't find the correct mapping using the CSV file
	        		 Vector3d vector3d = this.collide(lastMovedToPos);
	        		 //Taken from the calculations for this.horizontalCollison
	        		 boolean collidedOnXAxis = MathHelper.equal(lastMovedToPos.x, vector3d.x);
	        		 boolean collidedOnZAxis = MathHelper.equal(lastMovedToPos.z, vector3d.z);
	        		 //System.out.println("Collision: x: " + collidedOnXAxis + "   y: " + this.verticalCollision + "   z: " + collidedOnZAxis);
	        		 
	        		 Vector3d resultingVector = this.getDeltaMovement().scale(0.75);
	        		 resultingVector = resultingVector.multiply(collidedOnXAxis ? -1 : 1, 4 * (this.verticalCollision ? -1 : 1), collidedOnZAxis ? -1 : 1);
	        		 //System.out.println("Resulting vector: " + resultingVector.toString());
	        		 
	        		 this.setDeltaMovement(resultingVector);
	        	 }
	         }
		 }*/
	}

	@Shadow
	private boolean isFallFlying() {
		throw new IllegalStateException("Mixin failed to shadow isFallFlying()");
	}

	/*@Shadow
	private boolean canStandOnFluid(Fluid type) {
		throw new IllegalStateException("Mixin failed to shadow canStandOnFluid()");
	}*/

	@Shadow
	public boolean isAffectedByFluids() {
		throw new IllegalStateException("Mixin failed to shadow isAffectedByFluids()");
	}

	@Shadow
	public boolean isEffectiveAi() {
		throw new IllegalStateException("Mixin failed to shadow isEffectiveAi()");
	}

	// Mob effect synching
	@Inject(
			at = @At("HEAD"),
			method = "sendEffectToPassengers(Lnet/minecraft/world/effect/MobEffectInstance;)V",
			cancellable = true
	)
	private void mixinSendEffectToPassengers(MobEffectInstance pEffectInstance, CallbackInfo ci) {
		if (pEffectInstance instanceof ExtendedMobEffectHolder) {
			if (pEffectInstance.getEffect() instanceof SynchableMobEffect) {
				ClientboundUpdateMobEffectPacket packet = new ClientboundUpdateMobEffectPacket(this.getId(), pEffectInstance);
				
				((ServerChunkCache)this.getCommandSenderWorld().getChunkSource()).broadcastAndSend(this, packet);
				
				ci.cancel();
			}
		}
	}
}
