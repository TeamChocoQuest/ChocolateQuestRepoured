package team.cqr.cqrepoured.block.banner;

import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerPattern;

public enum EBanners {

	PIRATE_BANNER(DyeColor.BLACK, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.SKULL }, new DyeColor[] { DyeColor.WHITE, DyeColor.WHITE }),
	WALKER_BANNER(DyeColor.SILVER, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.BRICKS, EBannerPatternsCQ.WITHER_SKULL_EYES.getPattern(), EBannerPatternsCQ.WITHER_SKULL.getPattern(), EBannerPatternsCQ.WITHER_SKULL.getPattern() },
			new DyeColor[] { DyeColor.WHITE, DyeColor.GRAY, DyeColor.CYAN, DyeColor.BLACK, DyeColor.BLACK }),
	PIGMAN_BANNER(DyeColor.RED, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), EBannerPatternsCQ.FIRE.getPattern() }, new DyeColor[] { DyeColor.WHITE, DyeColor.YELLOW }),
	ENDERMAN_BANNER(DyeColor.MAGENTA, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.TRIANGLE_BOTTOM, BannerPattern.TRIANGLE_TOP }, new DyeColor[] { DyeColor.WHITE, DyeColor.BLACK, DyeColor.BLACK }),
	ENDERMAN_BANNER2(DyeColor.MAGENTA, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.SQUARE_TOP_RIGHT, BannerPattern.SQUARE_BOTTOM_LEFT }, new DyeColor[] { DyeColor.WHITE, DyeColor.BLACK, DyeColor.BLACK }),
	NPC_BANNER(DyeColor.RED, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.FLOWER, BannerPattern.STRIPE_CENTER, EBannerPatternsCQ.EMERALD.getPattern() }, new DyeColor[] { DyeColor.WHITE, DyeColor.YELLOW, DyeColor.RED, DyeColor.LIME }),
	SKELETON_BANNER(DyeColor.BLACK, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.CROSS, EBannerPatternsCQ.BONES.getPattern() }, new DyeColor[] { DyeColor.WHITE, DyeColor.RED, DyeColor.WHITE }),
	// WINGS_OF_FREEDOM(EnumDyeColor.WHITE, new BannerPattern[] { BannerPattern.DIAGONAL_LEFT_MIRROR, BannerPattern.BRICKS,
	// BannerPattern.TRIANGLE_TOP,
	// BannerPattern.GRADIENT, BannerPattern.BORDER }, new EnumDyeColor[] { EnumDyeColor.BLUE, EnumDyeColor.SILVER,
	// EnumDyeColor.SILVER, EnumDyeColor.SILVER,
	// EnumDyeColor.SILVER }),
	// GERMANY(EnumDyeColor.RED, new BannerPattern[] { BannerPattern.STRIPE_MIDDLE, BannerPattern.STRIPE_CENTER,
	// BannerPattern.STRAIGHT_CROSS,
	// BannerPattern.CIRCLE_MIDDLE, BannerPattern.STRAIGHT_CROSS }, new EnumDyeColor[] { EnumDyeColor.WHITE,
	// EnumDyeColor.WHITE, EnumDyeColor.BLACK,
	// EnumDyeColor.WHITE, EnumDyeColor.BLACK }),
	ILLAGER_BANNER(DyeColor.WHITE, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.RHOMBUS_MIDDLE, BannerPattern.STRIPE_BOTTOM, BannerPattern.STRIPE_CENTER, BannerPattern.BORDER, BannerPattern.STRIPE_MIDDLE, BannerPattern.HALF_HORIZONTAL },
			new DyeColor[] { DyeColor.WHITE, DyeColor.RED, DyeColor.SILVER, DyeColor.GRAY, DyeColor.SILVER, DyeColor.BLACK, DyeColor.SILVER }),
	WALKER_ORDO(DyeColor.WHITE,
			new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), EBannerPatternsCQ.WALKER_BORDER.getPattern(), EBannerPatternsCQ.WALKER_BORDER.getPattern(), EBannerPatternsCQ.WALKER_BACKGROUND.getPattern(), EBannerPatternsCQ.WALKER_INNER_BORDER.getPattern(), EBannerPatternsCQ.WALKER_SKULL.getPattern() },
			new DyeColor[] { DyeColor.WHITE, DyeColor.PURPLE, DyeColor.PURPLE, DyeColor.BLACK, DyeColor.GRAY, DyeColor.BLACK, }),
	GREMLIN_BANNER(DyeColor.GRAY, new BannerPattern[] { EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.STRIPE_SMALL, BannerPattern.STRIPE_CENTER, BannerPattern.DIAGONAL_RIGHT_MIRROR, BannerPattern.FLOWER, BannerPattern.GRADIENT_UP, BannerPattern.GRADIENT },
			new DyeColor[] { DyeColor.WHITE, DyeColor.RED, DyeColor.GRAY, DyeColor.GRAY, DyeColor.WHITE, DyeColor.RED, DyeColor.BLACK });

	private BannerPattern[] patternList;
	private DyeColor[] colorList;
	private DyeColor mainColor;

	EBanners(DyeColor mainColor, BannerPattern[] patterns, DyeColor[] colors) {
		this.mainColor = mainColor;
		this.colorList = colors;
		this.patternList = patterns;
	}

	public ItemStack getBanner() {
		final ListNBT patternList = new ListNBT();

		for (int i = 0; i < this.patternList.length; i++) {
			BannerPattern currPatt = this.patternList[i];
			DyeColor currCol = this.colorList[i];

			final CompoundNBT tag = new CompoundNBT();
			tag.setString("Pattern", currPatt.getHashname());
			tag.setInteger("Color", currCol.getDyeDamage());

			patternList.appendTag(tag);
		}

		ItemStack item = BannerItem.makeBanner(this.mainColor, patternList);
		item = item.setTranslatableName("item.banner." + this.name().toLowerCase() + ".name");
		return item;
	}

}
