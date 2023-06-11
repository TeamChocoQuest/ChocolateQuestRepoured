package team.cqr.cqrepoured.entity.ai.boss.piratecaptain;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAIPirateSummonParrot extends AbstractEntityAISpell<EntityCQRPirateCaptain> implements IEntityAISpellAnimatedVanilla {

	public BossAIPirateSummonParrot(EntityCQRPirateCaptain entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
		this.setup(true, true, true, false);
	}

	@Override
	public boolean shouldExecute() {
		return !this.entity.hasSpawnedParrot() && super.shouldExecute();
	}

	@Override
	public void startCastingSpell() {
		Vec3 v = this.entity.getLookAngle().normalize().scale(3);
		v = VectorUtil.rotateVectorAroundY(v, 90);
		if (this.entity.level.getBlockState(new BlockPos(this.entity.position().add(v).add(0, 1, 0))).getBlock() != Blocks.AIR) {
			v = new Vec3(0, 1, 0);
		}
		EntityCQRPirateParrot parrot = new EntityCQRPirateParrot(CQREntityTypes.PIRATE_PARROT.get(), this.world);
		parrot.setOwnerUUID(this.entity.getUUID());
		parrot.setTame(true);
		parrot.setOwnerUUID(this.entity.getUUID());
		Vec3 pos = this.entity.position().add(v);
		parrot.setPos(pos.x, pos.y, pos.z);
		this.entity.level.addFreshEntity(parrot);
		this.entity.setSpawnedParrot(true);
	}

	@Override
	public int getWeight() {
		return 1;
	}

	@Override
	public boolean ignoreWeight() {
		return true;
	}

	@Override
	public float getRed() {
		return 1;
	}

	@Override
	public float getGreen() {
		return 0;
	}

	@Override
	public float getBlue() {
		return 0;
	}

}
