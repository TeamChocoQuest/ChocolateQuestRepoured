package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRSounds;

public class EntityCQRGoblin extends AbstractEntityCQR {

	public EntityCQRGoblin(World worldIn) {
		super(worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GOBLIN;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Goblin;
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GOBLINS;
	}

	@Override
	public float getDefaultHeight() {
		return super.getDefaultHeight() * 0.75F;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.GOBLIN_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.GOBLIN_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.GOBLIN_HURT;
	}

}
