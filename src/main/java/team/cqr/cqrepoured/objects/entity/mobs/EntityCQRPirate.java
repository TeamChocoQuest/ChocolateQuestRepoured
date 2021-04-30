package team.cqr.cqrepoured.objects.entity.mobs;

import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityCQRPirate extends AbstractEntityCQR {

	public EntityCQRPirate(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Pirate;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PIRATE;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_PIRATE;
	}

	@Override
	public int getTextureCount() {
		return 3;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.PIRATE_AMBIENT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.PIRATE_DEATH;
	}
	
	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.PIRATE_HURT;
	}
	
	@Override
	protected float getSoundVolume() {
		return 0.5F * super.getSoundVolume();
	}

}
