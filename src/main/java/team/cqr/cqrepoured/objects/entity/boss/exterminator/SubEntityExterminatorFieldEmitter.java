package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.objects.entity.MultiPartEntityPartSizable;
import team.cqr.cqrepoured.objects.entity.misc.EntityElectricFieldSizable;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class SubEntityExterminatorFieldEmitter extends MultiPartEntityPartSizable<EntityCQRExterminator> {

	private EntityElectricFieldSizable electricField = null;
	private EntityCQRExterminator exterminator;

	private int remainingActiveTime;
	private int activeTimeNoTarget;
	private int cooldown;
	// When the target changes it gets inactive
	private EntityLivingBase target;
	
	protected static final DataParameter<Integer> TARGETED_ENTITY_ID = EntityDataManager.<Integer>createKey(SubEntityExterminatorFieldEmitter.class, DataSerializers.VARINT);

	public SubEntityExterminatorFieldEmitter(EntityCQRExterminator parent, String partName) {
		super(parent, partName, 0.5F, 0.5F);
		this.exterminator = parent;
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(TARGETED_ENTITY_ID, Integer.valueOf(0));
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
		return this.exterminator.areElectricCoilsActive() && this.remainingActiveTime > 0;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (this.isActive()) {
			
			if(this.ticksExisted % 10 == 0) {
				//Play a sound
			}
			
			this.remainingActiveTime--;
			// If we don't have a target we wait a bit and then deactivate
			if (this.target == null) {
				this.activeTimeNoTarget++;
				if (this.activeTimeNoTarget > 60) {
					// We didn'T have a target for 3 seconds, we deactivate forcefully
					this.remainingActiveTime = -1;
				}
			} else {
				//Damage the target by making it electrocuted
				this.target.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).setRemainingTicks(200);
			}
		} else {
			this.cooldown--;
			if (this.cooldown <= 0) {
				this.cooldown = DungeonGenUtils.randomBetween(60, 180, this.rand);
				// Now, re-set the active time
				this.remainingActiveTime = DungeonGenUtils.randomBetween(200, 400, this.rand);
				this.activeTimeNoTarget = 0;
			}
		}
	}

	public void setTarget(@Nonnull EntityLivingBase target) {
		if(target == null) {
			this.dataManager.set(TARGETED_ENTITY_ID, 0);
		}
		if (((target != null && this.target == null) || (target != null && !target.equals(this.target))) && !this.world.isRemote) {
			// New target => Deactivate if null => handled in update
			this.target = target;
			this.dataManager.set(TARGETED_ENTITY_ID, target.getEntityId());
		}
	}

	@Nullable
	public EntityLivingBase getTargetedEntity() {
		if(this.world.isRemote) {
			if(this.dataManager.get(TARGETED_ENTITY_ID) == 0) {
				return null;
			}
		}
		return this.target;
	}

	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);

		if (TARGETED_ENTITY_ID.equals(key) && this.world.isRemote) {
			int targetEntID = this.dataManager.get(TARGETED_ENTITY_ID);
			this.target = (EntityLivingBase) this.world.getEntityByID(targetEntID);
		}
	}

	public Random getRNG() {
		return this.exterminator.getRNG();
	}

}
