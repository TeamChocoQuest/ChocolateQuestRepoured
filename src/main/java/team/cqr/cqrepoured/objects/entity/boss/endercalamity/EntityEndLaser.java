package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntityRotatingLaser;

public class EntityEndLaser extends EntityRotatingLaser {

	public EntityEndLaser(World worldIn) {
		super(worldIn);
	}
	
	public EntityEndLaser(World worldIn, EntityLivingBase caster, float length, float deltaRotationYawPerTick, float deltaRotationPitchPerTick) {
		super(worldIn, caster, length, deltaRotationYawPerTick, deltaRotationPitchPerTick);
	}

	@Override
	public double laserEffectRadius() {
		return 0.5D;
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
	public int getBreakingSpeed() {
		return 18;
	}
	
	@Override
	public boolean canBreakBlocks() {
		return true;
	}
	
}
