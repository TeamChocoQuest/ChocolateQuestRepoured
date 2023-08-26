package team.cqr.cqrepoured.item;

import java.awt.TextComponent;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.util.Translator;

public class ItemLore extends Item {

	public ItemLore(Properties itemProperties) {
		super(itemProperties);
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean isLShiftPressed() {
		return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean isRShiftPressed() {
		return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		if (this.hasLore(stack)) {
			addHoverTextLogic(this, tooltip, flagIn);
			appendAdditionalTooltipEntries(stack, worldIn, tooltip, flagIn, isLShiftPressed() || isRShiftPressed());
		} else {
			super.appendHoverText(stack, worldIn, tooltip, flagIn);
		}
	}

	@OnlyIn(Dist.CLIENT)
	protected void appendAdditionalTooltipEntries(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn, boolean holdingShift) {

	}

	public static void addHoverTextLogic(Item item, List<TextComponent> tooltip, TooltipFlag flagIn) {
		addHoverTextLogic(item.getRegistryName(), tooltip, flagIn);
	}

	public static void addHoverTextLogic(ResourceLocation resourceLocation, List<TextComponent> tooltip, TooltipFlag flagIn) {
		addHoverTextLogic(resourceLocation.getNamespace(), resourceLocation.getPath(), tooltip, flagIn);
	}

	public static void addHoverTextLogic(String namespace, String path, List<TextComponent> tooltip, TooltipFlag flagIn) {
		if (isLShiftPressed() || isRShiftPressed()) {
			tooltip.add(Translator.translateItem(namespace, path, ".tooltip"));
		} else {
			tooltip.add(Translator.translate("item." + CQRConstants.MODID + ".tooltip.hold_shift"));
		}
	}

	public static void addHoverTextLogic(List<TextComponent> tooltip, TooltipFlag flagIn, String path) {
		addHoverTextLogic(CQRConstants.MODID, path, tooltip, flagIn);
	}

	public boolean hasLore(ItemStack stack) {
		return true;
	}

}
