package team.cqr.cqrepoured.entity.boss.exterminator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityTargetingLaser;

public class EntityExterminatorHandLaser extends EntityTargetingLaser {

	public EntityExterminatorHandLaser(World worldIn) {
		super(worldIn);
	}

	public EntityExterminatorHandLaser(EntityLivingBase caster, EntityLivingBase target) {
		this(caster.world, caster, 48, target);
	}

	public EntityExterminatorHandLaser(World worldIn, EntityLivingBase caster, float length, EntityLivingBase target) {
		super(worldIn, caster, length, target);
		this.maxRotationPerTick = 1.25F;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1 || pass == 0;
	}

	@Override
	public Vec3d getOffsetVector() {
		if (this.caster instanceof EntityCQRExterminator) {
			return ((EntityCQRExterminator) this.caster).getCannonFiringPointOffset();
		}
		return super.getOffsetVector();
	}

	@Override
	public float getColorR() {
		return 1.0F;
	}

	@Override
	public float getColorG() {
		return 0.0F;
	}

	@Override
	public float getColorB() {
		return 0.0F;
	}

	@Override
	public float getDamage() {
		return 1.25F;
	}

	@Override
	public boolean canBreakBlocks() {
		return true;
	}

	@Override
	public int getBreakingSpeed() {
		return 12;
	}

}
