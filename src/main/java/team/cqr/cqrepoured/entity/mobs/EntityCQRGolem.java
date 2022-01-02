package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRLoottables;

public class EntityCQRGolem extends AbstractEntityCQR implements IMechanical {

	public EntityCQRGolem(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Golem;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.DWARVES_AND_GOLEMS;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GOLEM;
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

	//Removed in 1.16
	/*@Override
	public boolean isImmuneToExplosions() {
		return true;
	}*/

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
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getAttribute(Attributes.ARMOR).setBaseValue(ArmorMaterial.IRON.getDefenseForSlot(EquipmentSlotType.CHEST));
		this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(ArmorMaterial.IRON.getToughness());
	}

}
