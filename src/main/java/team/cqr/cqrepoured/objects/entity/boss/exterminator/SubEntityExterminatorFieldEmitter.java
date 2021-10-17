package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.entity.MultiPartEntityPartSizable;
import team.cqr.cqrepoured.objects.entity.misc.EntityElectricFieldSizable;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class SubEntityExterminatorFieldEmitter extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	private EntityElectricFieldSizable electricField = null;
	private EntityCQRExterminator exterminator;
	
	private final Function<Object, EntityLivingBase> funcGetElectrocuteTarget;

	private int remainingActiveTime;
	private int activeTimeNoTarget;
	private int cooldown;
	
	public SubEntityExterminatorFieldEmitter(EntityCQRExterminator parent, String partName, final Function<Object, EntityLivingBase> funcGetElectrocuteTarget) {
		super(parent, partName, 0.5F, 0.5F);
		this.exterminator = parent;
		this.funcGetElectrocuteTarget = funcGetElectrocuteTarget;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public void applySizeVariation(float value) {
		// No, this should not be done in this entity, it happens in the parent...
		try {
			this.electricField.applySizeVariation(value);
		} catch (NullPointerException npe) {
			// Ignore
		}
	}

	@Override
	public void setSizeVariation(float size) {
		super.setSizeVariation(size);
		try {
			this.electricField.setSizeVariation(size);
		} catch (NullPointerException npe) {
			// Ignore
		}
	}

	@Override
	public void resize(float widthScale, float heightSacle) {
		super.resize(widthScale, heightSacle);
		try {
			this.electricField.resize(widthScale, heightSacle);
		} catch (NullPointerException npe) {
			// Ignore
		}
	}

	public void createElectricField(int charge) {
		if (this.electricField != null) {
			this.electricField.setCharge(charge);
		} else {
			this.electricField = new EntityElectricFieldSizable(this.exterminator.getWorld(), charge, this.exterminator.getPersistentID());
		}
	}

	public void destroyElectricField() {
		try {
			this.electricField.destroyField();
		} catch (NullPointerException npe) {
			// Ignore
		}
	}

	public boolean isActive() {
		return this.exterminator.canElectricCoilsBeActive() && this.remainingActiveTime > 0;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (this.isActive()) {
			
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
