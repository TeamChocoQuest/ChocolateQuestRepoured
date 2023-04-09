package team.cqr.cqrepoured.entity.boss;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.ai.boss.boarmage.BossAIBoarmageExplodeAreaAttack;
import team.cqr.cqrepoured.entity.ai.boss.boarmage.BossAIBoarmageTeleportSpell;
import team.cqr.cqrepoured.entity.ai.spells.EntityAIExplodeAreaStartSpell;
import team.cqr.cqrepoured.entity.ai.spells.EntityAIExplosionRay;
import team.cqr.cqrepoured.entity.ai.spells.EntityAISummonFireWall;
import team.cqr.cqrepoured.entity.ai.spells.EntityAISummonMeteors;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.ISummoner;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.init.CQREntityTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EntityCQRBoarmage extends AbstractEntityCQRMageBase implements ISummoner, IAnimatableCQR {

	protected List<Entity> summonedMinions = new ArrayList<>();

	protected boolean startedExplodeAreaAttack = false;

	public EntityCQRBoarmage(World world) {
		this(CQREntityTypes.BOARMAGE.get(), world);
	}

	public EntityCQRBoarmage(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}

	public void startExplodeAreaAttack() {
		this.startedExplodeAreaAttack = true;
	}

	public boolean isExecutingExplodeAreaAttack() {
		return this.startedExplodeAreaAttack;
	}

	public void stopExplodeAreaAttack() {
		this.startedExplodeAreaAttack = false;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		List<Entity> tmp = new ArrayList<>();
		for (Entity ent : this.summonedMinions) {
			if (ent == null || !ent.isAlive()) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.summonedMinions.remove(e);
		}

		if ((this.isInLava() || this.isOnFire()) && this.tickCount % 5 == 0) {
			this.heal(1);
		}
	}

	@Override
	public void die(DamageSource cause) {
		// Kill minions
		for (Entity e : this.summonedMinions) {
			if (e != null && e.isAlive()) {
				if (e instanceof LivingEntity) {
					((LivingEntity) e).die(cause);
				}
				e.remove();
			}
		}
		this.summonedMinions.clear();

		super.die(cause);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(10, new BossAIBoarmageTeleportSpell(this));
		this.goalSelector.addGoal(0, new BossAIBoarmageExplodeAreaAttack(this));
		this.spellHandler.addSpell(0, new EntityAISummonMeteors(this, 75, 20));
		this.spellHandler.addSpell(3, new EntityAIExplosionRay(this, 100, 10));
		this.spellHandler.addSpell(2, new EntityAISummonFireWall(this, 50, 25));
		this.spellHandler.addSpell(1, new EntityAIExplodeAreaStartSpell(this, 200, 20, 5));
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.boarmage.get();
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
		return this.summonedMinions;
	}

	@Override
	public LivingEntity getSummoner() {
		return this;
	}
	
	@Override
	public int getTextureCount() {
		return 3;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		this.summonedMinions.add(summoned);
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.UNDEAD;
	}

	@Override
	public boolean canPutOutFire() {
		return false;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	// Geckolib
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		this.registerControllers(this, data);
	}

}
