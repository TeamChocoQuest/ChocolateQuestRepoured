package team.cqr.cqrepoured.objects.entity.ai;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

/*
 * Partly copied from AW2
 * https://github.com/P3pp3rF1y/AncientWarfare2/blob/faf3f1e632316811f8eb53449d56221e6e70beff/src/main/java/net/
 * shadowmage/ancientwarfare/npc/ai/NpcAIRideHorse.
 * java
 */
public class EntityAIRideHorse<T extends AbstractEntityCQR> extends AbstractCQREntityAI<T> {

	private static final AttributeModifier FOLLOW_RANGE_MODIFIER = new AttributeModifier("modifier.cqr_horse_path_extension", 24.d, 0).setSaved(false);
	private final AttributeModifier moveSpeedModifier;

	protected EntityLiving horse;
	private final List<EntityAITasks.EntityAITaskEntry> horseAI = new ArrayList<>();

	public EntityAIRideHorse(T entity, double speedFactor) {
		super(entity);
		this.moveSpeedModifier = new AttributeModifier("modifier.cqr_ride_speed", speedFactor, 1).setSaved(false);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity != null && this.entity.canMountEntity()) {
			return this.shouldRideHorse();
		}
		return false;
	}

	protected boolean shouldRideHorse() {
		return this.horse == null && this.entity.getRidingEntity() instanceof EntityHorse;
	}

	@Override
	public void startExecuting() {
		this.horse = (EntityLiving) this.entity.getRidingEntity();
		this.onMountHorse();
	}

	protected void onMountHorse() {
		this.removeHorseAI();
		if (this.horse instanceof AbstractHorse) {
			AbstractHorse h = (AbstractHorse) this.horse;
			h.setHorseSaddled(true);
			h.setEatingHaystack(false);
			h.setRearing(false);
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
			((AbstractHorse) this.horse).setHorseSaddled(true);
			this.removeModifiers();
		}
	}

	private void applyModifiers() {
		if (this.horse instanceof AbstractHorse) {
			this.removeModifiers();
			this.horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(this.moveSpeedModifier);
			this.horse.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(FOLLOW_RANGE_MODIFIER);
		}
	}

	private void removeModifiers() {
		this.horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(this.moveSpeedModifier);
		this.horse.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).removeModifier(FOLLOW_RANGE_MODIFIER);
	}

	private void removeHorseAI() {
		this.horseAI.clear();
		this.horseAI.addAll(this.horse.tasks.taskEntries);
		for (EntityAITasks.EntityAITaskEntry task : this.horseAI) {
			this.horse.tasks.removeTask(task.action);
		}
	}

	private void addHorseAI() {
		if (this.horse.tasks.taskEntries.isEmpty()) {
			this.horse.tasks.taskEntries.addAll(this.horseAI);
		}
		this.horseAI.clear();
	}

}
