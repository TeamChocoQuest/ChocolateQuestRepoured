package team.cqr.cqrepoured.item.gun;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.init.CQRItems;

public class ItemBullet extends Item
{
	public ItemBullet(Properties properties)
	{
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (stack.getItem() == CQRItems.BULLET_IRON.get())
		{
			tooltip.add((new TranslationTextComponent("description.bullet_damage.name", 2.5)).withStyle(TextFormatting.BLUE));
			//tooltip.add(TextFormatting.BLUE + "+2.5 " + I18n.exists("description.bullet_damage.name"));
		}

		if (stack.getItem() == CQRItems.BULLET_GOLD.get())
		{
			tooltip.add((new TranslationTextComponent("description.bullet_damage.name", 3.75)).withStyle(TextFormatting.BLUE));
		}

		if (stack.getItem() == CQRItems.BULLET_DIAMOND.get()) {
			tooltip.add((new TranslationTextComponent("description.bullet_damage.name", 5)).withStyle(TextFormatting.BLUE));
		}

		if (stack.getItem() == CQRItems.BULLET_FIRE.get())
		{
			tooltip.add((new TranslationTextComponent("description.bullet_damage.name", 5)).withStyle(TextFormatting.RED));
			tooltip.add(new TranslationTextComponent("description.bullet_fire.name").withStyle(TextFormatting.DARK_RED));
		}
	}

}
