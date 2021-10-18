package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.entity.MultiPartEntityPartSizable;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class SubEntityExterminatorFieldEmitter extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	private EntityCQRExterminator exterminator;
	
	private final Function<Object, EntityLivingBase> funcGetElectrocuteTarget;
	private final Function<Object, Boolean> funcGetIsActive;
	private final Function<Boolean, Object> funcSetIsActiveInParent;
	
	private int remainingActiveTime;
	private int activeTimeNoTarget;
	private int cooldown;
	
	public SubEntityExterminatorFieldEmitter(
			EntityCQRExterminator parent, 
			String partName, 
			final Function<Object, EntityLivingBase> funcGetElectrocuteTarget,
			final Function<Object, Boolean> funcGetIsActive,
			final Function<Boolean, Object> funcSetIsActiveInParent
		) {
		super(parent, partName, 0.5F, 0.5F);
		this.exterminator = parent;
		this.funcGetElectrocuteTarget = funcGetElectrocuteTarget;
		this.funcGetIsActive = funcGetIsActive;
		this.funcSetIsActiveInParent = funcSetIsActiveInParent;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	public boolean isActive() {
		if(this.world.isRemote) {
			return this.funcGetIsActive.apply(this);
		}
		return this.exterminator.canElectricCoilsBeActive() && this.remainingActiveTime > 0;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		boolean active = this.isActive();
		this.funcSetIsActiveInParent.apply(active);
		if (active) {
			
			if(this.ticksExisted % 30 == 0) {
				//Play a sound
				this.playSound(CQRSounds.EXTERMINATOR_ELECTRO_ZAP, 0.75F, 1.0F);
			}
			EntityLivingBase target = this.getTargetedEntity();
			this.remainingActiveTime--;
			// If we don't have a target we wait a bit and then deactivate
			if (target == null) {
				this.activeTimeNoTarget++;
				if (this.activeTimeNoTarget > 60) {
					// We didn'T have a target for 3 seconds, we deactivate forcefully
					this.remainingActiveTime = -1;
				}
			} else {
				//Damage the target by making it electrocuted
				target.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).setRemainingTicks(200);
			}
		} else {
			this.cooldown--;
			if(this.remainingActiveTime > 0) {
				this.remainingActiveTime = -1;
			}
			if (this.cooldown <= 0) {
				this.cooldown = DungeonGenUtils.randomBetween(60, 180, this.rand);
				// Now, re-set the active time
				this.remainingActiveTime = DungeonGenUtils.randomBetween(200, 400, this.rand);
				this.activeTimeNoTarget = 0;
			}
		}
	}

	@Nullable
	public EntityLivingBase getTargetedEntity() {
		return this.funcGetElectrocuteTarget.apply(this);
	}

	public Random getRNG() {
		return this.exterminator.getRNG();
	}

}
