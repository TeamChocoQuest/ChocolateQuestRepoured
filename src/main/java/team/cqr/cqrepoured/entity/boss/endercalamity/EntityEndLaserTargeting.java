package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityTargetingLaser;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityEndLaserTargeting extends EntityTargetingLaser {

	public EntityEndLaserTargeting(Level worldIn) {
		this(CQREntityTypes.END_LASER_TARGETING.get(), worldIn);
	}
	
	public EntityEndLaserTargeting(EntityType<? extends EntityEndLaserTargeting> type, Level world) {
		super(type, world);
	}

	public EntityEndLaserTargeting(LivingEntity caster, LivingEntity target) {
		this(caster.level, caster, 48, target);
	}

	public EntityEndLaserTargeting(Level worldIn, LivingEntity caster, float length, LivingEntity target) {
		super(CQREntityTypes.END_LASER_TARGETING.get(), worldIn, caster, length, target);
		this.maxRotationPerTick = 0.5F;
	}

	/*@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1 || pass == 0;
	}*/

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
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
