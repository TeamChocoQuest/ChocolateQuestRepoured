package team.cqr.cqrepoured.item.sword;

import java.util.List;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemSwordSpider extends ItemCQRWeapon {

	public ItemSwordSpider(IItemTier material, Item.Properties props) {
		super(material, props);
	}

	@Override
	public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
		pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
		return super.hurtEnemy(pStack, pTarget, pAttacker);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}

}
