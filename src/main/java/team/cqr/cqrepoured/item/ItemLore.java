package team.cqr.cqrepoured.item;

import java.awt.TextComponent;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRConstants;

public class ItemLore extends Item implements IRegistryNameProvider {

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

	public static void addHoverTextLogic(List<Component> tooltip, TooltipFlag flagIn, String registryNamePath, String registryNameKey) {
		if (isLShiftPressed() || isRShiftPressed()) {
			tooltip.add(Component.translatable("item." + registryNameKey + "." + registryNamePath + ".tooltip"));
		} else {
			tooltip.add(Component.translatable("item." + CQRConstants.MODID + ".tooltip.hold_shift"));
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if(this.hasLore(stack)) {
			addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath(), this.getRegistryName().getNamespace());
		} else {
			super.appendHoverText(stack, worldIn, tooltip, flagIn);
		}
	}

	@OnlyIn(Dist.CLIENT)
	protected void appendAdditionalTooltipEntries(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn, boolean holdingShift) {

	}

	public static void addHoverTextLogic(Item item, List<Component> tooltip, TooltipFlag flagIn) {
		addHoverTextLogic(tooltip, flagIn, IRegistryNameProvider.getRegistryNameOf(item).getPath(), IRegistryNameProvider.getRegistryNameOf(item).getNamespace());
	}

	public static void addHoverTextLogic(ResourceLocation id, List<Component> tooltip, TooltipFlag flagIn) {
		addHoverTextLogic(tooltip, flagIn, id.getPath(), id.getNamespace());
	}

	public static void addHoverTextLogic(String namespace, String path, List<Component> tooltip, TooltipFlag flagIn) {
		addHoverTextLogic(new ResourceLocation(namespace, path), tooltip, flagIn);
	}

	public static void addHoverTextLogic(List<Component> tooltip, TooltipFlag flagIn, String path) {
		addHoverTextLogic(CQRConstants.MODID, path, tooltip, flagIn);
	}

	public boolean hasLore(ItemStack stack) {
		return true;
	}

}
