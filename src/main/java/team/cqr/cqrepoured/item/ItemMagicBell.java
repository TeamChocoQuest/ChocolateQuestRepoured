package team.cqr.cqrepoured.item;

import java.util.stream.Stream;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.particles.BlockHighlightParticleData;
import team.cqr.cqrepoured.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectedRegion;
import team.cqr.cqrepoured.protection.ProtectedRegionManager;

public class ItemMagicBell extends ItemLore {

	public ItemMagicBell(Properties properties)
	{
		super(properties);
	}
	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 20;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		playerIn.startUsingItem(handIn);
		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		if (!worldIn.isClientSide) {
			IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(worldIn);
			Stream<ProtectedRegion> protectedRegions = protectedRegionManager.getProtectedRegionsAt(entityLiving.blockPosition());

			/*protectedRegions.map(ProtectedRegion::getEntityDependencies).flatMap(Collection::stream).map(uuid -> EntityUtil.getEntityByUUID(worldIn, uuid)).filter(Objects::nonNull)
					.forEach(entity -> {
						//OLD: CQRParticleType.spawnParticles(CQRParticleType.BLOCK_HIGHLIGHT, worldIn, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), 0, 0, 0, 1, 0, 0, 0, 200, 0xC00000, entity.getId())
						worldIn.addParticle(new BlockHighlightParticleData(BlockHighlightParticleData.COLOR_ENTITY_HIGHLIGHT, 200, entity.getId()), entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), 0, 0, 0);
					});

			protectedRegions.map(ProtectedRegion::getBlockDependencies).flatMap(Collection::stream)
					.forEach(pos -> {
						//OLD: CQRParticleType.spawnParticles(CQRParticleType.BLOCK_HIGHLIGHT, worldIn, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), 0, 0, 0, 1, 0, 0, 0, 200, 0x4050D0, pos.getX(), pos.getY(), pos.getZ())
						worldIn.addParticle(new BlockHighlightParticleData(BlockHighlightParticleData.BLOCK_DEPENDENCY_HIGHLIGHT, 200, pos), entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), 0, 0, 0);
					});*/

			protectedRegions.forEach(pr -> {
				BlockPos start = pr.getStartPos();
				BlockPos size = pr.getEndPos().subtract(pr.getStartPos()).offset(1, 1, 1);
				byte[] protectionStates = pr.getProtectionStates();
				for (int i = 0; i < protectionStates.length; i++) {
					byte protectionState = protectionStates[i];
					if (protectionState == 1 || protectionState == 2) {
						int color = protectionState == 1 ? BlockHighlightParticleData.BLOCK_PROTECTED_HIGHLIGHT : BlockHighlightParticleData.BLOCK_UNPROTECTED_HIGHLIGHT;
						int x = i / size.getZ() / size.getY();
						int y = i / size.getZ() % size.getY();
						int z = i % size.getZ();
						//OLD: CQRParticleType.spawnParticles(CQRParticleType.BLOCK_HIGHLIGHT, worldIn, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), 0, 0, 0, 1, 0, 0, 0, 200, color, start.getX() + x, start.getY() + y, start.getZ() + z);
						worldIn.addParticle(new BlockHighlightParticleData(color, 200, start.offset(x, y, z)), entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), 0, 0, 0);
					}
				}
			});

			worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CQRSounds.BELL_USE, entityLiving.getSoundSource(), 1.0F, 1.0F);
			if (entityLiving instanceof Player) {
				if (protectedRegions.count() <= 0) {
					((Player) entityLiving).getCooldowns().addCooldown(stack.getItem(), 60);
				} else {
					((Player) entityLiving).getCooldowns().addCooldown(stack.getItem(), 200);
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
