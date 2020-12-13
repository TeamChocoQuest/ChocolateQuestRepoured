package team.cqr.cqrepoured.magic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractSpell {

	private ResourceLocation spellId;
	
	private AbstractSpell(ResourceLocation spellId) {
		super();
		this.spellId = spellId;
	}
	
	ResourceLocation getSpellId() {
		return this.spellId;
	}
	
	public abstract int getSpellCosts();
	public abstract int getCastingTime();
	public abstract int getSpellCooldown();
	
	public abstract DamageSource getDamageSourceToUse();
	//ONly gets called server side
	public abstract boolean castSpell(EntityLivingBase caster, World world, BlockPos clickedPos, AbstractSpellCastingItem castingItem, ItemStack castingItemStack, float power);

}
