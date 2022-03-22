package team.cqr.cqrepoured.item.gun;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (stack.getItem() == CQRItems.CANNON_BALL.get()) {
			tooltip.add(new StringTextComponent("+5 " + new TranslationTextComponent("description.bullet_damage.name")).withStyle(TextFormatting.BLUE));
		}
	}
}