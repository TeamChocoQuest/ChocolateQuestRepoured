package team.cqr.cqrepoured.item.gun;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.init.CQRItems;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCannonBall extends Item {

	public ItemCannonBall(Properties properties)
	{
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		if (stack.getItem() == CQRItems.CANNON_BALL.get()) {
			tooltip.add(new TranslationTextComponent("item.cqrepoured.tooltip.bullet_damage", 5.0).withStyle(ChatFormatting.BLUE));
		}
	}
}