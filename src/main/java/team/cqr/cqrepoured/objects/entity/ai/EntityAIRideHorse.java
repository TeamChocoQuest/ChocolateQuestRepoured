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
 * https://github.com/P3pp3rF1y/AncientWarfare2/blob/faf3f1e632316811f8eb53449d56221e6e70beff/src/main/java/net/shadowmage/ancientwarfare/npc/ai/NpcAIRideHorse.java
 */
public class EntityAIRideHorse<T extends AbstractEntityCQR> extends AbstractCQREntityAI<T> {
	
	private static final AttributeModifier FOLLOW_RANGE_MODIFIER = new AttributeModifier("modifier.cqr_horse_path_extension", 24.d, 0).setSaved(false);
	private final AttributeModifier moveSpeedModifier;

	protected EntityLiving horse;
	private final List<EntityAITasks.EntityAITaskEntry> horseAI = new ArrayList<>();

	public EntityAIRideHorse(T entity, double speedFactor) {
		super(entity);
		moveSpeedModifier = new AttributeModifier("modifier.cqr_ride_speed", speedFactor, 1).setSaved(false);
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity != null && this.entity.canMountEntity()) {
			return shouldRideHorse();
		}
		return false;
	}
	
	protected boolean shouldRideHorse() {
		return horse == null && this.entity.getRidingEntity() instanceof EntityHorse;
	}
	
	@Override
	public void startExecuting() {
		horse = (EntityLiving) this.entity.getRidingEntity();
		onMountHorse();
	}

	protected void onMountHorse() {
		removeHorseAI();
		if (horse instanceof AbstractHorse) {
			AbstractHorse h = (AbstractHorse) horse;
			h.setHorseSaddled(true);
			h.setEatingHaystack(false);
			h.setRearing(false);
		}
		applyModifiers();
	}

	public void onKilled() {
		if (horse != null) {
			onDismountHorse();
			horse = null;
		}
	}

	protected void onDismountHorse() {
		addHorseAI();
		if (horse instanceof AbstractHorse) {
			((AbstractHorse) horse).setHorseSaddled(true);
			removeModifiers();
		}
	}

	private void applyModifiers() {
		if (horse instanceof AbstractHorse) {
			removeModifiers();
			horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(moveSpeedModifier);
			horse.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(FOLLOW_RANGE_MODIFIER);
		}
	}

	private void removeModifiers() {
		horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(moveSpeedModifier);
		horse.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).removeModifier(FOLLOW_RANGE_MODIFIER);
	}

	private void removeHorseAI() {
		horseAI.clear();
		horseAI.addAll(horse.tasks.taskEntries);
		for (EntityAITasks.EntityAITaskEntry task : horseAI) {
			horse.tasks.removeTask(task.action);
		}
	}

	private void addHorseAI() {
		if (horse.tasks.taskEntries.isEmpty()) {
			horse.tasks.taskEntries.addAll(horseAI);
		}
		horseAI.clear();
	}

}
