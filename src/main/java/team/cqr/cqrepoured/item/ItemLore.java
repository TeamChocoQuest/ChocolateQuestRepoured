package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemLore extends Item {

	public ItemLore(Properties itemProperties) {
		super(itemProperties);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static boolean isLShiftPressed() {
		return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) != GLFW.GLFW_PRESS;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static boolean isRShiftPressed() {
		return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT) != GLFW.GLFW_PRESS;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if(this.hasLore(stack)) {
			addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
		} else {
			super.appendHoverText(stack, worldIn, tooltip, flagIn);
		}
	}
	
	public static void addHoverTextLogic(List<ITextComponent> tooltip, ITooltipFlag flagIn, String registryNamePath) {
		if (isLShiftPressed() || isRShiftPressed()) {
			tooltip.add(new StringTextComponent(TextFormatting.BLUE + I18n.get("description." + registryNamePath + ".name", '\n', '\n', '\n', '\n', '\n', '\n', '\n', '\n', '\n', '\n')));
		} else {
			tooltip.add(new StringTextComponent(TextFormatting.BLUE + I18n.get("description.click_shift.name")));
		}
	}
	
	public boolean hasLore(ItemStack stack) {
		return true;
	}

}
