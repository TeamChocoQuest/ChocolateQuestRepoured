package team.cqr.cqrepoured.entity.mobs;

import java.util.Set;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class EntityCQRGolem extends AbstractEntityCQR implements IMechanical, IAnimatableCQR {

	public EntityCQRGolem(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.golem.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.DWARVES_AND_GOLEMS;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.IRON_GOLEM_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.IRON_GOLEM_STEP;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.IRON_GOLEM_DEATH;
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.isFire() || source.isExplosion()) {
			return false;
		}
		return super.hurt(source, amount);
	}

	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.MECHANICAL;
	}

	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();
		
		this.getAttribute(Attributes.ARMOR).setBaseValue(ArmorMaterial.IRON.getDefenseForSlot(EquipmentSlotType.CHEST));
		this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(ArmorMaterial.IRON.getToughness());
	}

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
