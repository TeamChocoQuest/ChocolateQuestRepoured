package team.cqr.cqrepoured.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public interface IRangedWeapon {

	void shoot(World world, LivingEntity shooter, Entity target, Hand hand);

	SoundEvent getShootSound();

	double getRange();

	int getCooldown();

	int getChargeTicks();

}
