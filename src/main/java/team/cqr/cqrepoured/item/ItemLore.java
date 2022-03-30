package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;

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
			tooltip.add(new TranslationTextComponent("item." + CQRMain.MODID + "." + registryNamePath + ".tooltip"));
		} else {
			tooltip.add(new TranslationTextComponent("item." + CQRMain.MODID + ".tooltip.click_shift"));
		}
	}
	
	public static void addHoverTextLogic(Item item, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		addHoverTextLogic(tooltip, flagIn, item.getRegistryName().getPath());
	}
	
	public boolean hasLore(ItemStack stack) {
		return true;
	}

}
