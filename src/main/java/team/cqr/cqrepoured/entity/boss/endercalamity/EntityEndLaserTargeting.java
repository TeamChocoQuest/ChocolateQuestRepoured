package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityTargetingLaser;

public class EntityEndLaserTargeting extends EntityTargetingLaser {

	public EntityEndLaserTargeting(World worldIn) {
		super(worldIn);
	}

	public EntityEndLaserTargeting(EntityLivingBase caster, EntityLivingBase target) {
		this(caster.world, caster, 48, target);
	}

	public EntityEndLaserTargeting(World worldIn, EntityLivingBase caster, float length, EntityLivingBase target) {
		super(worldIn, caster, length, target);
		this.maxRotationPerTick = 0.5F;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1 || pass == 0;
	}

	@Override
	public float getColorR() {
		return 0.8F;
	}

	@Override
	public float getColorG() {
		return 0.01F;
	}

	@Override
	public float getColorB() {
		return 0.98F;
	}

	@Override
	public float getDamage() {
		return 2.5F;
	}

	@Override
	public boolean canBreakBlocks() {
		return true;
	}

	@Override
	public int getBreakingSpeed() {
		return 6;
	}

}
