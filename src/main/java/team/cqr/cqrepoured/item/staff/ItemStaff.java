package team.cqr.cqrepoured.item.staff;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaff extends ItemLore {

	public ItemStaff() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.knockback(attacker, 1.0F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		return false;
	}

}
