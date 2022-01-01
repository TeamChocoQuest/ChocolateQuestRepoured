package team.cqr.cqrepoured.item;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public interface IRangedWeapon {

	void shoot(World world, EntityLivingBase shooter, Entity target, EnumHand hand);

	SoundEvent getShootSound();

	double getRange();

	int getCooldown();

	int getChargeTicks();

}
