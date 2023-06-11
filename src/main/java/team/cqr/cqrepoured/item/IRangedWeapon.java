package team.cqr.cqrepoured.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

public interface IRangedWeapon {

	void shoot(Level world, LivingEntity shooter, Entity target, InteractionHand hand);

	SoundEvent getShootSound();

	double getRange();

	int getCooldown();

	int getChargeTicks();

}
