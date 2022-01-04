package team.cqr.cqrepoured.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHookShotHook;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.util.CQRBlockUtil;
import team.cqr.cqrepoured.util.PropertyFileHelper;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public abstract class ItemHookshotBase extends ItemLore {

	private enum BlockGroup {
		BASE_SOLID("BASE_SOLID"), BASE_WOOD("BASE_WOOD"), BASE_STONE("BASE_STONE"), BASE_DIRT("BASE_DIRT");

		private final String configName;

		BlockGroup(String configName) {
			this.configName = configName;
		}

		public static Optional<BlockGroup> fromConfigString(String string) {
			for (BlockGroup bg : BlockGroup.values()) {
				if (bg.configName.equalsIgnoreCase(string)) {
					return Optional.of(bg);
				}
			}
			return Optional.empty();
		}

		public boolean containsBlock(Block block) {
			switch (this) {
			case BASE_SOLID:
				return true;
			case BASE_WOOD:
				return CQRBlockUtil.VANILLA_WOOD_SET.contains(block);
			case BASE_STONE:
				return CQRBlockUtil.VANILLA_STONE_SET.contains(block);
			case BASE_DIRT:
				return CQRBlockUtil.VANILLA_DIRT_SET.contains(block);
			default:
				return false;
			}
		}
	}

	protected List<Block> validLatchBlocks = new ArrayList<>();
	protected List<BlockGroup> latchGroups = new ArrayList<>();

	public ItemHookshotBase(String hookshotName, Properties props) {
		super(props);
		this.setMaxDamage(300);
		this.setMaxStackSize(1);

		this.loadPropertiesFromFile(hookshotName);

		this.addPropertyOverride(new ResourceLocation("hook_out"), new IItemPropertyGetter() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public float call(ItemStack stack, @Nullable ClientWorld worldIn, @Nullable LivingEntity entityIn) {
				if (entityIn != null && stack.getItem() instanceof ItemHookshotBase) {
					CompoundNBT stackTag = stack.getTag();
					if ((stackTag != null) && (stackTag.getBoolean("isShooting"))) {
						return 1.0f;
					}
				}

				return 0.0f;
			}
		});
	}

	private void loadPropertiesFromFile(String hookshotName) {
		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_ITEM_FOLDER, new String[] { "properties", "prop", "cfg" }, true);
		// Find the property file that matches this hookshot name
		Optional<File> configFile = files.stream().filter(f -> FilenameUtils.getBaseName(f.getName()).equalsIgnoreCase(hookshotName)).findFirst();

		if (configFile.isPresent()) {
			java.util.Properties hookshotConfig = new java.util.Properties();
			try (InputStream in = new FileInputStream(configFile.get())) {
				hookshotConfig.load(in);

				String[] latchBlocks = PropertyFileHelper.getStringArrayProperty(hookshotConfig, "latchblocks", new String[0], true);
				for (String blockType : latchBlocks) {
					Optional<BlockGroup> groupMatch = BlockGroup.fromConfigString(blockType);
					if (groupMatch.isPresent()) {
						this.latchGroups.add(groupMatch.get());
						continue;
					}

					Block blockMatch;
					// Try the vanilla blocks first
					blockMatch = Block.getBlockFromName(blockType);
					if (blockMatch != null) {
						this.validLatchBlocks.add(blockMatch);
						continue;
					}

					// Then try CQR blocks
					// TODO: Create modblocks lookup by name function and check against that

					// Then try other Mod blocks
					// TODO: Search other mod block registries

					CQRMain.logger.error("{}: Invalid latch block: {}", configFile.get().getName(), blockType);
				}

			} catch (IOException e) {
				CQRMain.logger.error("{}: Failed to load file!", configFile.get().getName(), e);
			}
		}
	}

	public boolean canLatchToBlock(Block block) {
		for (BlockGroup bg : this.latchGroups) {
			if (bg.containsBlock(block)) {
				return true;
			}
		}
		return this.validLatchBlocks.contains(block);
	}

	public abstract double getHookRange();

	public abstract ProjectileHookShotHook getNewHookEntity(World worldIn, LivingEntity shooter, ItemStack stack);

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		this.shoot(stack, worldIn, playerIn);
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	public void shoot(ItemStack stack, World worldIn, PlayerEntity player) {

		if (!worldIn.isClientSide) {
			ProjectileHookShotHook hookEntity = this.getNewHookEntity(worldIn, player, stack);
			hookEntity.shootHook(player, this.getHookRange(), 1.8D);
			worldIn.addFreshEntity(hookEntity);
			player.getCooldownTracker().setCooldown(this, 100);
			stack.damageItem(1, player);
			worldIn.playSound(null, player.posX, player.posY, player.posZ, CQRSounds.GUN_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
	}

	public ProjectileHookShotHook entityAIshoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		if (!worldIn.isClientSide) {
			ProjectileHookShotHook hookEntity = this.getNewHookEntity(worldIn, shooter, shooter.getUseItem());
			Vector3d v = target.position().subtract(shooter.position());
			hookEntity.shootHook(shooter, v.x, v.y, v.z, this.getHookRange(), 1.8D);
			worldIn.addFreshEntity(hookEntity);
			return hookEntity;
		}
		return null;
	}

	public SoundEvent getShootSound() {
		return CQRSounds.GUN_SHOOT;
	}

	public double getRange() {
		return 16.0D;
	}

	public int getCooldown() {
		return 300;
	}

	public int getChargeTicks() {
		return 0;
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
