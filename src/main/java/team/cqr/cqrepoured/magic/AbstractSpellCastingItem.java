package team.cqr.cqrepoured.magic;


import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;

public abstract class AbstractSpellCastingItem extends Item {
	
	public AbstractSpellCastingItem() {
		super();
		this.setMaxStackSize(1);
	}

	abstract float getCastingSpeedModifier();
	abstract float getManaCostModifier();
	abstract float getSpellPowerModifier();
	abstract float getSpellCooldownModifier();
	
	abstract int getSpellSlotCount();

	//TODO: Add capabilities or AttributeModifiers for items, than iterate through the entity's equipment which is not this item and multiply them together
	public float getManaCostModifier(EntityLivingBase caster) {
		return 1;
	}
	public float getSpellPowerModifier(EntityLivingBase caster) {
		return 1;
	}
	public float getSpellCooldownModifier(EntityLivingBase caster) {
		return 1;
	}
	
	@Nullable
	public AbstractSpell getCurrentSpell(ItemStack castingItem) {
		//TODO: Add capability that stores the slot-id of the currently selected spell
		return null;
	}
	
	//Casting speed
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		if(getCurrentSpell(stack) != null) {
			return Math.abs(Math.round(getCurrentSpell(stack).getCastingTime() * getCastingSpeedModifier()));
		}
		return 20;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase caster) {
		if(!worldIn.isRemote && getCurrentSpell(stack) != null) {
			final int castingMana = Math.round(getCurrentSpell(stack).getSpellCosts() * getManaCostModifier() * getManaCostModifier(caster));
			if( MagicUtil.getMana(caster) >= castingMana && MagicUtil.subtractMana(caster, castingMana)) {
				Vec3d start = caster.getPositionEyes(1.0F);
				Vec3d end = start.add(caster.getLookVec().scale(5.0D));
				RayTraceResult result = worldIn.rayTraceBlocks(start, end);
				BlockPos pos = caster.getPosition();
				if(result != null) {
					pos = result.getBlockPos();
				}
				if(getCurrentSpell(stack).castSpell(caster, worldIn, pos, this, stack, 1 * getSpellPowerModifier() * getSpellPowerModifier(caster))) {
					if(caster instanceof EntityPlayer) {
						((EntityPlayer)caster).getCooldownTracker().setCooldown(this, Math.round(getCurrentSpell(stack).getSpellCooldown() * getSpellCooldownModifier() * getSpellCooldownModifier(caster)));
					} else {
						//TODO: Add capability for cooldown to non-players
					}
				}
			}
		}
		return super.onItemUseFinish(stack, worldIn, caster);
	}
	
	//Inventory
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, this.getSpellSlotCount());
	}
}
