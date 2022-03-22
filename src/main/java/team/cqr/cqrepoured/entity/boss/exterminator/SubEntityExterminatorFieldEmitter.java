package team.cqr.cqrepoured.entity.boss.exterminator;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.entity.MultiPartEntityPartSizable;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SubEntityExterminatorFieldEmitter extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	private EntityCQRExterminator exterminator;

	private final Supplier<LivingEntity> funcGetElectrocuteTarget;
	private final Supplier<Boolean> funcGetIsActive;
	private final Consumer<Boolean> funcSetIsActiveInParent;

	private int remainingActiveTime;
	private int activeTimeNoTarget;
	private int cooldown;

	public SubEntityExterminatorFieldEmitter(EntityCQRExterminator parent, String partName, final Supplier<LivingEntity> funcGetElectrocuteTarget, final Supplier<Boolean> funcGetIsActive, final Consumer<Boolean> funcSetIsActiveInParent) {
		super(parent, partName, 0.5F, 0.5F);
		this.exterminator = parent;
		this.funcGetElectrocuteTarget = funcGetElectrocuteTarget;
		this.funcGetIsActive = funcGetIsActive;
		this.funcSetIsActiveInParent = funcSetIsActiveInParent;
	}
	
	@Override
	public void kill() {
		this.exterminator.kill();
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	public boolean isActive() {
		if (this.level.isClientSide) {
			return this.funcGetIsActive.get();
		}
		return this.exterminator.canElectricCoilsBeActive() && this.remainingActiveTime > 0;
	}

	@Override
	public void baseTick() {
		super.baseTick();

		boolean active = this.isActive();
		this.funcSetIsActiveInParent.accept(active);
		if (active) {

			if (this.tickCount % 30 == 0) {
				// Play a sound
				this.playSound(CQRSounds.EXTERMINATOR_ELECTRO_ZAP, 0.75F, 1.0F);
			}
			LivingEntity target = this.getTargetedEntity();
			this.remainingActiveTime--;
			// If we don't have a target we wait a bit and then deactivate
			if (target == null) {
				this.activeTimeNoTarget++;
				if (this.activeTimeNoTarget > 60) {
					// We didn'T have a target for 3 seconds, we deactivate forcefully
					this.remainingActiveTime = -1;
				}
			} else {
				// Damage the target by making it electrocuted
				target.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).ifPresent((cap) -> {
					cap.setRemainingTicks(200);
				});
				target.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).ifPresent((cap) -> {
					cap.setRemainingSpreads(8);
				});
				target.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).ifPresent((cap) -> {
					cap.setCasterID(this.exterminator.getUUID());
				});
			}
		} else {
			this.cooldown--;
			if (this.remainingActiveTime > 0) {
				this.remainingActiveTime = -1;
			}
			if (this.cooldown <= 0) {
				this.cooldown = DungeonGenUtils.randomBetween(120, 360, this.getParent().getRandom());
				// Now, re-set the active time
				this.remainingActiveTime = DungeonGenUtils.randomBetween(100, 200, this.getParent().getRandom());
				this.activeTimeNoTarget = 0;
			}
		}
	}

	@Nullable
	public LivingEntity getTargetedEntity() {
		return this.funcGetElectrocuteTarget.get();
	}

	@Override
	public ActionResultType interact(PlayerEntity player, Hand hand) {
		if (this.exterminator == null || !this.exterminator.isAlive()) {
			return ActionResultType.FAIL;
		}
		return this.exterminator.interact(player, hand);
	}

}
