package team.cqr.cqrepoured.mixin.network.protocol.game;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.tslat.effectslib.api.ExtendedMobEffect;
import net.tslat.effectslib.api.ExtendedMobEffectHolder;
import team.cqr.cqrepoured.api.effect.SynchableMobEffect;

@Mixin(ClientboundUpdateMobEffectPacket.class)
public abstract class MixinClientboundUpdateMobEffectPacket implements AdditionalDataProvider {
	
	@Shadow
	@Final
	private MobEffect effect;
	
	
	@Unique
	private Optional<Object> customData = Optional.empty();
	@Unique
	private Optional<MobEffectInstance> effectInstance = Optional.empty();
	
	@Inject(
			at = @At("TAIL"),
			method = "<init>(ILnet/minecraft/world/effect/MobEffectInstance;)V"
	)
	private void mixinConstructor(int entityId, MobEffectInstance effectInstance, CallbackInfo ci) {
		if (effectInstance instanceof ExtendedMobEffectHolder emeh) {
			if (effectInstance.getEffect() instanceof SynchableMobEffect) {
				this.customData = Optional.ofNullable(emeh.getExtendedMobEffectData());
				this.effectInstance = Optional.ofNullable(effectInstance);
			}
		} 
	}
	
	@Inject(
			at = @At("TAIL"),
			method = "write(Lnet/minecraft/network/FriendlyByteBuf;)V"
	)
	private void mixinWrite(FriendlyByteBuf buffer, CallbackInfo ci) {
		if (this.effectInstance.isPresent() && this.effectInstance.get() instanceof ExtendedMobEffectHolder && this.effect instanceof SynchableMobEffect && this.effect instanceof ExtendedMobEffect) {
			buffer.writeBoolean(this.customData.isPresent());
			if (this.customData.isPresent()) {
				// Write NBT of custoMData to buffer
				this.effectInstance.ifPresent(mei -> {
					CompoundTag tag = new CompoundTag();
					
					((ExtendedMobEffect)this.effect).write(tag, this.effectInstance.get());
					
					buffer.writeNbt(tag);
				});
			}
		}
		buffer.writeBoolean(false);
	}
	
	@Inject(
			at = @At("TAIL"),
			method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V"
	)
	private void mixinConstructor(FriendlyByteBuf buffer, CallbackInfo ci) {
		if (this.effect != null && this.effect instanceof SynchableMobEffect && this.effect instanceof ExtendedMobEffect eme) {
			if (buffer.readBoolean()) {
				CompoundTag nbt = buffer.readNbt();
				
				MobEffectInstance instance = new MobEffectInstance(effect);
				
				eme.read(nbt, instance);
				this.customData = Optional.ofNullable(((ExtendedMobEffectHolder)instance).getExtendedMobEffectData());
			}
		}
	}
	
	@Override
	public Optional<Object> getCustomData() {
		return this.customData;
	}

}
