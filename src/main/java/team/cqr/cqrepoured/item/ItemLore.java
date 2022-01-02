package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemLore extends Item {

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(this.hasLore(stack)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				tooltip.add(TextFormatting.BLUE + I18n.format("description." + this.getRegistryName().getPath() + ".name", '\n', '\n', '\n', '\n', '\n', '\n', '\n', '\n', '\n', '\n'));
			} else {
				tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
			}
		} else {
			super.addInformation(stack, worldIn, tooltip, flagIn);
		}
	}
	
	public boolean hasLore(ItemStack stack) {
		return true;
	}

}
