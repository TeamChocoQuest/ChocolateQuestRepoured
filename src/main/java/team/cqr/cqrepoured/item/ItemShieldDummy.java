package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemShieldDummy extends ItemLore {

	public ItemShieldDummy(Properties properties)
	{
		super(properties);
		// TODO: ToolTip
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		//if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
		//	tooltip.add(TextFormatting.BLUE + I18n.format("description.dummy_shield.name"));
		//} else {
		//	tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		//}
	}

}
