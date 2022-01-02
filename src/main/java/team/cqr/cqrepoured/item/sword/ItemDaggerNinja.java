package team.cqr.cqrepoured.item.sword;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraftforge.api.distmarker.Dist;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemDaggerNinja extends ItemDagger {

	public ItemDaggerNinja(ToolMaterial material, int cooldown) {
		super(material, cooldown);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.isSneaking()) {
			worldIn.playSound(playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
			playerIn.getCooldownTracker().setCooldown(stack.getItem(), 30);

			for (int i = 0; i < 6; i++) {
				worldIn.spawnParticle(ParticleTypes.PORTAL, playerIn.posX + itemRand.nextFloat() - 0.5D, playerIn.posY + itemRand.nextFloat() - 0.5D, playerIn.posZ + itemRand.nextFloat() - 0.5D, itemRand.nextFloat() - 0.5F, itemRand.nextFloat() - 0.5F, itemRand.nextFloat() - 0.5F);
			}

			double x = -Math.sin(Math.toRadians(playerIn.rotationYaw));
			double z = Math.cos(Math.toRadians(playerIn.rotationYaw));
			double y = -Math.sin(Math.toRadians(playerIn.rotationPitch));
			x *= (1.0D - Math.abs(y));
			z *= (1.0D - Math.abs(y));
			int dist = 4;

			BlockPos pos = new BlockPos(playerIn.posX + x * dist, playerIn.posY + y * dist + 1, playerIn.posZ + z * dist);

			if (worldIn.getBlockState(pos).getBlock().isPassable(worldIn, pos) && pos.getY() > 0) {
				playerIn.setPosition(playerIn.posX + x * dist, playerIn.posY + y * dist + 1, playerIn.posZ + z * dist);
				playerIn.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 40, 5, false, false));
			} else {
				return new ActionResult<>(ActionResultType.FAIL, stack);
			}

			stack.damageItem(1, playerIn);

			for (int i = 0; i < 6; i++) {
				worldIn.spawnParticle(ParticleTypes.PORTAL, playerIn.posX + itemRand.nextFloat() - 0.5D, playerIn.posY + itemRand.nextFloat() - 0.5D, playerIn.posZ + itemRand.nextFloat() - 0.5D, itemRand.nextFloat() - 0.5F, itemRand.nextFloat() - 0.5F, itemRand.nextFloat() - 0.5F);
			}
		} else {
			super.onItemRightClick(worldIn, playerIn, handIn);
		}
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	@Dist(OnlyIn.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + "200% " + I18n.format("description.rear_damage.name"));

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.ninja_dagger.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}
