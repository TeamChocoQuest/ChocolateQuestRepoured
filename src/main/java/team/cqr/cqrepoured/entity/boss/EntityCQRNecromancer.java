package team.cqr.cqrepoured.entity.boss;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.spells.EntityAIBlindTargetSpell;
import team.cqr.cqrepoured.entity.ai.spells.EntityAIFangAttack;
import team.cqr.cqrepoured.entity.ai.spells.EntityAISummonMinionSpell;
import team.cqr.cqrepoured.entity.bases.ISummoner;
import team.cqr.cqrepoured.entity.misc.EntityFlyingSkullMinion;
import team.cqr.cqrepoured.entity.misc.EntitySummoningCircle.ECircleTexture;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;

public class EntityCQRNecromancer extends AbstractEntityCQRMageBase implements ISummoner {

	private static final DataParameter<Boolean> BONE_SHIELD_ACTIVE = EntityDataManager.<Boolean>createKey(EntityCQRNecromancer.class, DataSerializers.BOOLEAN);

	protected List<Entity> summonedMinions = new ArrayList<>();
	protected List<EntityFlyingSkullMinion> summonedSkulls = new ArrayList<>();

	public EntityCQRNecromancer(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.spellHandler.addSpell(0, new EntityAISummonMinionSpell(this, 30, 10, new ResourceLocation(CQRMain.MODID, "skeleton"), ECircleTexture.SKELETON, true, 25, 5, new Vector3d(0, 0, 0)) {
			@Override
			public boolean isInterruptible() {
				return false;
			}
		});
		this.spellHandler.addSpell(1, new EntityAISummonMinionSpell(this, 20, 5, new ResourceLocation(CQRMain.MODID, "flying_skull"), ECircleTexture.FLYING_SKULL, false, 8, 4, new Vector3d(0, 2.5, 0)) {
			@Override
			public boolean isInterruptible() {
				return false;
			}
		});
		this.spellHandler.addSpell(2, new EntityAIBlindTargetSpell(this, 45, 10, 100));
		this.spellHandler.addSpell(3, new EntityAIFangAttack(this, 20, 10, 4, 8) {
			@Override
			public boolean isInterruptible() {
				return false;
			}
		});
		// this.spellHandler.addSpell(4, new EntityAIVampiricSpell(this, 30, 10));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.dataManager.register(BONE_SHIELD_ACTIVE, false);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.filterSummonLists();

		if (!this.summonedSkulls.isEmpty() && !this.hasAttackTarget()) {
			this.summonedSkulls.get(0).setSide(false);
			if (this.summonedSkulls.size() >= 2) {
				this.summonedSkulls.get(1).setSide(true);
			}
		}

		if (!this.world.isRemote && this.getHealth() <= this.getMaxHealth() / 2) {
			this.dataManager.set(BONE_SHIELD_ACTIVE, true);
		} else if (!this.world.isRemote) {
			this.dataManager.set(BONE_SHIELD_ACTIVE, false);
		}

		if (this.getAttackTarget() != null && !this.getAttackTarget().isDead && !this.summonedSkulls.isEmpty()) {
			for (int i = 0; i < this.summonedSkulls.size(); i++) {
				EntityFlyingSkullMinion skull = this.summonedSkulls.get(i);
				if (!skull.hasTarget()) {
					skull.setTarget(this.getAttackTarget());
				}
			}
			for (int i = 0; i < this.summonedSkulls.size(); i++) {
				EntityFlyingSkullMinion skull = this.summonedSkulls.get(i);
				if (!skull.isAttacking()) {
					skull.startAttacking();
				}
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!this.world.isRemote && this.getHealth() <= this.getMaxHealth() / 2) {
			if (source.isProjectile() || source.getImmediateSource() instanceof AbstractArrowEntity || source.getImmediateSource() instanceof IProjectile) {
				amount = 0;
				return false;
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	private void filterSummonLists() {
		List<Entity> tmp = new ArrayList<>();
		for (Entity ent : this.summonedMinions) {
			if (ent == null || ent.isDead) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.summonedMinions.remove(e);
		}
		tmp.clear();
		for (Entity ent : this.summonedSkulls) {
			if (ent == null || ent.isDead) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.summonedSkulls.remove(e);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		// Kill minions
		for (Entity e : this.getSummonedEntities()) {
			if (e != null && !e.isDead) {
				if (e instanceof LivingEntity) {
					((LivingEntity) e).onDeath(cause);
				}
				e.setDead();
			}
		}
		this.summonedMinions.clear();

		super.onDeath(cause);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_NECROMANCER;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Necromancer;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
	}

	@Override
	public Faction getSummonerFaction() {
		return this.getFaction();
	}

	@Override
	public List<Entity> getSummonedEntities() {
		List<Entity> list = new ArrayList<>(this.summonedMinions);
		list.addAll(this.summonedSkulls);
		return list;
	}

	@Override
	public LivingEntity getSummoner() {
		return this;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		if (summoned instanceof EntityFlyingSkullMinion) {
			this.summonedSkulls.add((EntityFlyingSkullMinion) summoned);
			return;
		}
		this.summonedMinions.add(summoned);
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ILLAGER;
	}

	public boolean isBoneShieldActive() {
		return this.dataManager.get(BONE_SHIELD_ACTIVE);
	}

}
