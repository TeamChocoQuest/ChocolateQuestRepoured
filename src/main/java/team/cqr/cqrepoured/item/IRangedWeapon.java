package team.cqr.cqrepoured.item;

import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.entity.LivingEntity;

public interface IRangedWeapon {

	void shoot(World world, LivingEntity shooter, Entity target, Hand hand);

	SoundEvent getShootSound();

	double getRange();

	int getCooldown();

	int getChargeTicks();

}
