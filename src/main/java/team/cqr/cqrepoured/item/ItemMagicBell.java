package team.cqr.cqrepoured.item;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRParticleType;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

public class ItemMagicBell extends ItemLore {

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 20;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (!worldIn.isRemote) {
			IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(worldIn);
			List<ProtectedRegion> protectedRegions = protectedRegionManager.getProtectedRegionsAt(new BlockPos(entityLiving));

			protectedRegions.stream().map(ProtectedRegion::getEntityDependencies).flatMap(Collection::stream).map(uuid -> EntityUtil.getEntityByUUID(worldIn, uuid)).filter(Objects::nonNull)
					.forEach(entity -> CQRParticleType.spawnParticles(CQRParticleType.BLOCK_HIGHLIGHT, worldIn, entityLiving.posX, entityLiving.posY, entityLiving.posZ, 0, 0, 0, 1, 0, 0, 0, 200, 0xC00000, entity.getEntityId()));

			protectedRegions.stream().map(ProtectedRegion::getBlockDependencies).flatMap(Collection::stream)
					.forEach(pos -> CQRParticleType.spawnParticles(CQRParticleType.BLOCK_HIGHLIGHT, worldIn, entityLiving.posX, entityLiving.posY, entityLiving.posZ, 0, 0, 0, 1, 0, 0, 0, 200, 0x4050D0, pos.getX(), pos.getY(), pos.getZ()));

			protectedRegions.forEach(pr -> {
				BlockPos start = pr.getStartPos();
				BlockPos size = pr.getEndPos().subtract(pr.getStartPos()).add(1, 1, 1);
				byte[] protectionStates = pr.getProtectionStates();
				for (int i = 0; i < protectionStates.length; i++) {
					byte protectionState = protectionStates[i];
					if (protectionState == 1 || protectionState == 2) {
						int color = protectionState == 1 ? 0xFFFF00 : 0x00FF00;
						int x = i / size.getZ() / size.getY();
						int y = i / size.getZ() % size.getY();
						int z = i % size.getZ();
						CQRParticleType.spawnParticles(CQRParticleType.BLOCK_HIGHLIGHT, worldIn, entityLiving.posX, entityLiving.posY, entityLiving.posZ, 0, 0, 0, 1, 0, 0, 0, 200, color, start.getX() + x, start.getY() + y, start.getZ() + z);
					}
				}
			});

			worldIn.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, CQRSounds.BELL_USE, entityLiving.getSoundCategory(), 1.0F, 1.0F);
			if (entityLiving instanceof EntityPlayer) {
				if (protectedRegions.isEmpty()) {
					((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(stack.getItem(), 60);
				} else {
					((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(stack.getItem(), 200);
				}
			}
		}

		return stack;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

}
