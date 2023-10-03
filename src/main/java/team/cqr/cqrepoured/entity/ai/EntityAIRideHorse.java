package team.cqr.cqrepoured.entity.ai;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

/*
 * Partly copied from AW2
 * https://github.com/P3pp3rF1y/AncientWarfare2/blob/faf3f1e632316811f8eb53449d56221e6e70beff/src/main/java/net/
 * shadowmage/ancientwarfare/npc/ai/NpcAIRideHorse.
 * java
 */
public class EntityAIRideHorse<T extends AbstractEntityCQR> extends AbstractCQREntityAI<T> {

	private static final AttributeModifier FOLLOW_RANGE_MODIFIER = new AttributeModifier("modifier.cqr_horse_path_extension", 24.d, Operation.ADDITION) {
		//Disable saving
		public CompoundTag save() {
			return new CompoundTag();};
	};
	private final AttributeModifier moveSpeedModifier;

	protected Mob horse;
	private final List<WrappedGoal> horseAI = new ArrayList<>();

	public EntityAIRideHorse(T entity, double speedFactor) {
		super(entity);
		this.moveSpeedModifier = new AttributeModifier("modifier.cqr_ride_speed", speedFactor, Operation.MULTIPLY_BASE) {
			// Disable saving
			public CompoundTag save() {
				return new CompoundTag();
			};
		};
	}

	@Override
	public boolean canUse() {
		if (this.entity != null && this.entity.canMountEntity()) {
			return this.shouldRideHorse();
		}
		return false;
	}

	protected boolean shouldRideHorse() {
		return this.horse == null && this.entity.getVehicle() instanceof AbstractHorse;
	}

	@Override
	public void start() {
		this.horse = (Mob) this.entity.getVehicle();
		this.onMountHorse();
	}

	protected void onMountHorse() {
		this.removeHorseAI();
		if (this.horse instanceof AbstractHorse) {
			AbstractHorse h = (AbstractHorse) this.horse;
			h.equipSaddle(SoundSource.AMBIENT);
			h.setEating(false);
			h.setJumping(false); //Previously setRearing, is this the correct replacement?
		}
		this.applyModifiers();
	}

	public void onKilled() {
		if (this.horse != null) {
			this.onDismountHorse();
			this.horse = null;
		}
	}

	protected void onDismountHorse() {
		this.addHorseAI();
		if (this.horse instanceof AbstractHorse) {
			((AbstractHorse) this.horse).equipSaddle(SoundSource.AMBIENT);
			this.removeModifiers();
		}
	}

	private void applyModifiers() {
		if (this.horse instanceof AbstractHorse) {
			this.removeModifiers();
			this.horse.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(this.moveSpeedModifier);
			this.horse.getAttribute(Attributes.FOLLOW_RANGE).addTransientModifier(FOLLOW_RANGE_MODIFIER);
		}
	}

	private void removeModifiers() {
		this.horse.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(this.moveSpeedModifier);
		this.horse.getAttribute(Attributes.FOLLOW_RANGE).removeModifier(FOLLOW_RANGE_MODIFIER);
	}

	private void removeHorseAI() {
		this.horseAI.clear();
		this.horseAI.addAll(this.horse.goalSelector.getAvailableGoals());
		for (WrappedGoal task : this.horseAI) {
			this.horse.goalSelector.removeGoal(task.getGoal());
		}
	}

	private void addHorseAI() {
		if (this.horse.goalSelector.getAvailableGoals().isEmpty()) {
			this.horse.goalSelector.getAvailableGoals().addAll(this.horseAI);
		}
		this.horseAI.clear();
	}

}
