package team.cqr.cqrepoured.mixin.client.multiplayer;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.effectslib.api.ExtendedMobEffectHolder;
import team.cqr.cqrepoured.mixin.network.protocol.game.AdditionalDataProvider;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {
	
	@Shadow
	private Minecraft minecraft;
	@Shadow
	private ClientLevel level;
	
	//TODO: Replace this with a modifier for the local value directly after assignment
	@Inject(
			at = @At("HEAD"),
			method = "handleUpdateMobEffect(Lnet/minecraft/network/protocol/game/ClientboundUpdateMobEffectPacket;)V",
			cancellable = true
	)
	private void mixinHandleUpdateMobEffect(ClientboundUpdateMobEffectPacket packet, CallbackInfo ci) {
		if (packet instanceof AdditionalDataProvider adp) {
			Optional<Object> data = adp.getCustomData();
			if (data.isPresent()) {
				PacketUtils.ensureRunningOnSameThread(packet, (ClientPacketListener)(Object)this, this.minecraft);
				Entity entity = this.level.getEntity(packet.getEntityId());
				if (entity instanceof LivingEntity) {
					MobEffect mobeffect = packet.getEffect();
					if (mobeffect != null) {
						MobEffectInstance mobeffectinstance = new MobEffectInstance(mobeffect, packet.getEffectDurationTicks(), packet.getEffectAmplifier(), packet.isEffectAmbient(), packet.isEffectVisible(), packet.effectShowsIcon(),
								(MobEffectInstance) null, Optional.ofNullable(packet.getFactorData()));
						if (mobeffectinstance instanceof ExtendedMobEffectHolder emeh) {
							emeh.setExtendedMobEffectData(data.get());
						}
						((LivingEntity) entity).forceAddEffect(mobeffectinstance, (Entity) null);
					}
				}
			}
		} 
	}

}
