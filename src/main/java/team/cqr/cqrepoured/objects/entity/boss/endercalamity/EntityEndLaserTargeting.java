package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntityTargetingLaser;

public class EntityEndLaserTargeting extends EntityTargetingLaser {

	public EntityEndLaserTargeting(World worldIn) {
		super(worldIn);
	}
	
	public EntityEndLaserTargeting(EntityLivingBase caster, EntityLivingBase target) {
		this(caster.world, caster, 32, target);
	}

	public EntityEndLaserTargeting(World worldIn, EntityLivingBase caster, float length, EntityLivingBase target) {
		super(worldIn, caster, length, target);
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


}
