package team.cqr.cqrepoured.entity.bases;

import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import team.cqr.cqrepoured.common.entity.VariantEntity;
import team.cqr.cqrepoured.entity.ai.behaviour.idle.ChatBehaviour;

public class UndeadEntityBase<T extends UndeadEntityBase<T>> extends WalkingEntityBase<T> implements ISynchedDataHelper {
	
	/*
	 * Init
	 */
	
	protected static final EntityDataAccessor<Boolean> BURIED = SynchedEntityData.defineId(UndeadEntityBase.class, EntityDataSerializers.BOOLEAN);
	protected final DataHolder<Boolean> buriedHolder = new DataHolder<>(false);
	
	@SuppressWarnings("rawtypes")
	private final Map<EntityDataAccessor, DataHolder> dataAccessorMapping = this.createAccessHolderMap();

	public UndeadEntityBase(EntityType<? extends VariantEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	/*
	 * AI section
	 */
	
	@Override
	public BrainActivityGroup<? extends T> getCoreTasks() {
		return super.getCoreTasks();
		// If low on HP => run away, drink potion
		// Also avoid electrocution fields (Maybe not undeads, they're pretty ... dumb... but maybe for more intelligent entities...
		// In general we avoid the sun
		// If we don't wear headwear, we escape the sun
		// SKELETAL ENEMIES: Avoid wolves
		// In general, avoid high level enemies if not in a group
		// We look at our look target
		// If we are ranged (and the weapon allows it), we strafe
		// Move to the walk target
		// If we lost our attack target (we can't see it and have reached it's last position), search for it for a bit
	}

	@SuppressWarnings("unchecked")
	@Override
	public BrainActivityGroup<? extends T> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				new FirstApplicableBehaviour<T>( 				// Run only one of the below behaviours, trying each one in order. Include explicit generic typing because javac is silly
						// TODO: If you have a crossbow, reload it
						new TargetOrRetaliate<>(),						// Set the attack target TODO: Add predicate for attackable
						// TODO: Mounting and horse taming tasks
						// TODO: Emerging => If we have a attack target we emerge
						// TODO: Follow your schedule, if you have one
						// TODO: Follow path AI
						// TODO: Firefighting AI
						// TODO: Go home
						// TODO: Trading chat icon behaviour => If trader => chat with customer
						new ChatBehaviour<>(),
						new SetRandomLookTarget<>(),					// Set the look target to a random nearby location
						// TODO: Walk to burying spot
						// TODO: Bury yourself
						// If we can't talk to anyone or search a attack target, we'll just stroll around...
						new OneRandomBehaviour<>( 					// Run only one of the below behaviours, picked at random
								new SetRandomWalkTarget<>().speedModifier(1), 						// Set the walk target to a nearby random pathable location
								new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))	 // Don't walk anywhere
						)
				)
		);
				
	}
	
	@Override
	public BrainActivityGroup<? extends T> getFightTasks() {
		return super.getFightTasks();
		// Invalidate attack target, if applicable
		// Healing staff + switching
		// Spells
		// Ranged attacks
		//	Bow
		//	Crossbow
		//	Pistol, musket etc.
		// Melee attacks
		// 	Twohanded
		//		Spear
		//		Greatsword
		//	Onehanded
		//		Dagger
		//		Sword
		// Special weapons
		//	Hookshot => also needs own sensor for hook target
		//	Cursed bone => Using and summoning
		//	Potion throwers / grenadiers
	}
	
	/*
	 * Mob-Type specific section
	 */

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}
	
	/*
	 * ISynchedDataHelper specific section
	 */
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		super.onSyncedDataUpdated(pKey);
		
		this.callOnSyncedDataUpdated(pKey);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void registerMappings(BiConsumer<EntityDataAccessor, DataHolder> registerFunction) {
		registerFunction.accept(BURIED, buriedHolder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<EntityDataAccessor, DataHolder> accessHolderMap() {
		return dataAccessorMapping;
	}
	
	/*
	 * Data access
	 */
	public void setBuried(boolean value) {
		this.setSided(BURIED, value);
	}
	
	public boolean isBuried() {
		return this.getSided(BURIED, false);
	}

	
}
