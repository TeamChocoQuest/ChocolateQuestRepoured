package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;
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
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (this.hasLore(stack)) {
			addHoverTextLogic(this, tooltip, flagIn);
			appendAdditionalTooltipEntries(stack, worldIn, tooltip, flagIn, isLShiftPressed() || isRShiftPressed());
		} else {
			super.appendHoverText(stack, worldIn, tooltip, flagIn);
		}
	}

	@OnlyIn(Dist.CLIENT)
	protected void appendAdditionalTooltipEntries(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, boolean holdingShift) {

	}

	public static void addHoverTextLogic(Item item, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		addHoverTextLogic(item.getRegistryName(), tooltip, flagIn);
	}

	public static void addHoverTextLogic(ResourceLocation resourceLocation, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		addHoverTextLogic(resourceLocation.getNamespace(), resourceLocation.getPath(), tooltip, flagIn);
	}

	public static void addHoverTextLogic(String namespace, String path, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (isLShiftPressed() || isRShiftPressed()) {
			tooltip.add(Translator.translateItem(namespace, path, ".tooltip"));
		} else {
			tooltip.add(Translator.translate("item." + CQRMain.MODID + ".tooltip.hold_shift"));
		}
	}

	public static void addHoverTextLogic(List<ITextComponent> tooltip, ITooltipFlag flagIn, String path) {
		addHoverTextLogic(CQRMain.MODID, path, tooltip, flagIn);
	}

	public boolean hasLore(ItemStack stack) {
		return true;
	}

}
