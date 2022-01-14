package team.cqr.cqrepoured.block.banner;

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerPattern.Builder;
import net.minecraft.util.text.TranslationTextComponent;
import team.cqr.cqrepoured.init.CQRBannerPatterns;

public enum EBanners {

	PIRATE_BANNER(DyeColor.BLACK, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, BannerPattern.SKULL }, new DyeColor[] { DyeColor.WHITE, DyeColor.WHITE }),
	WALKER_BANNER(DyeColor.LIGHT_GRAY, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, BannerPattern.BRICKS, CQRBannerPatterns.WITHER_SKULL_EYES, CQRBannerPatterns.WITHER_SKULL, CQRBannerPatterns.WITHER_SKULL },
			new DyeColor[] { DyeColor.WHITE, DyeColor.GRAY, DyeColor.CYAN, DyeColor.BLACK, DyeColor.BLACK }),
	PIGMAN_BANNER(DyeColor.RED, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, CQRBannerPatterns.FIRE }, new DyeColor[] { DyeColor.WHITE, DyeColor.YELLOW }),
	ENDERMAN_BANNER(DyeColor.MAGENTA, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, BannerPattern.TRIANGLE_BOTTOM, BannerPattern.TRIANGLE_TOP }, new DyeColor[] { DyeColor.WHITE, DyeColor.BLACK, DyeColor.BLACK }),
	ENDERMAN_BANNER2(DyeColor.MAGENTA, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, BannerPattern.SQUARE_TOP_RIGHT, BannerPattern.SQUARE_BOTTOM_LEFT }, new DyeColor[] { DyeColor.WHITE, DyeColor.BLACK, DyeColor.BLACK }),
	NPC_BANNER(DyeColor.RED, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, BannerPattern.FLOWER, BannerPattern.STRIPE_CENTER, CQRBannerPatterns.EMERALD }, new DyeColor[] { DyeColor.WHITE, DyeColor.YELLOW, DyeColor.RED, DyeColor.LIME }),
	SKELETON_BANNER(DyeColor.BLACK, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, BannerPattern.CROSS, CQRBannerPatterns.BONES }, new DyeColor[] { DyeColor.WHITE, DyeColor.RED, DyeColor.WHITE }),
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
	ILLAGER_BANNER(DyeColor.WHITE, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, BannerPattern.RHOMBUS_MIDDLE, BannerPattern.STRIPE_BOTTOM, BannerPattern.STRIPE_CENTER, BannerPattern.BORDER, BannerPattern.STRIPE_MIDDLE, BannerPattern.HALF_HORIZONTAL },
			new DyeColor[] { DyeColor.WHITE, DyeColor.RED, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.LIGHT_GRAY, DyeColor.BLACK, DyeColor.LIGHT_GRAY }),
	WALKER_ORDO(DyeColor.WHITE,
			new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, CQRBannerPatterns.WALKER_BORDER, CQRBannerPatterns.WALKER_BORDER, CQRBannerPatterns.WALKER_BACKGROUND, CQRBannerPatterns.WALKER_INNER_BORDER, CQRBannerPatterns.WALKER_SKULL },
			new DyeColor[] { DyeColor.WHITE, DyeColor.PURPLE, DyeColor.PURPLE, DyeColor.BLACK, DyeColor.GRAY, DyeColor.BLACK, }),
	GREMLIN_BANNER(DyeColor.GRAY, new BannerPattern[] { CQRBannerPatterns.CQ_BLANK, BannerPattern.STRIPE_SMALL, BannerPattern.STRIPE_CENTER, BannerPattern.DIAGONAL_RIGHT_MIRROR, BannerPattern.FLOWER, BannerPattern.GRADIENT_UP, BannerPattern.GRADIENT },
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
		BannerPattern.Builder builder = new Builder();
		
		for (int i = 0; i < this.patternList.length; i++) {
			builder = builder.addPattern(this.patternList[i], this.colorList[i]);
		}

		ItemStack bannerItem = null;
		switch(this.mainColor) {
		case BLACK:
			bannerItem = new ItemStack(Items.BLACK_BANNER);
			break;
		case BLUE:
			bannerItem = new ItemStack(Items.BLUE_BANNER);
			break;
		case BROWN:
			bannerItem = new ItemStack(Items.BROWN_BANNER);
			break;
		case CYAN:
			bannerItem = new ItemStack(Items.CYAN_BANNER);
			break;
		case GRAY:
			bannerItem = new ItemStack(Items.GRAY_BANNER);
			break;
		case GREEN:
			bannerItem = new ItemStack(Items.GREEN_BANNER);
			break;
		case LIGHT_BLUE:
			bannerItem = new ItemStack(Items.LIGHT_BLUE_BANNER);
			break;
		case LIGHT_GRAY:
			bannerItem = new ItemStack(Items.LIGHT_GRAY_BANNER);
			break;
		case LIME:
			bannerItem = new ItemStack(Items.LIME_BANNER);
			break;
		case MAGENTA:
			bannerItem = new ItemStack(Items.MAGENTA_BANNER);
			break;
		case ORANGE:
			bannerItem = new ItemStack(Items.ORANGE_BANNER);
			break;
		case PINK:
			bannerItem = new ItemStack(Items.PINK_BANNER);
			break;
		case PURPLE:
			bannerItem = new ItemStack(Items.PURPLE_BANNER);
			break;
		case RED:
			bannerItem = new ItemStack(Items.RED_BANNER);
			break;
		case YELLOW:
			bannerItem = new ItemStack(Items.YELLOW_BANNER);
			break;
		default:
			bannerItem = new ItemStack(Items.WHITE_BANNER);
			break;
		}
		//Copied from Raid.class#getLeaderBannerInstance()
		CompoundNBT nbt = bannerItem.getOrCreateTagElement("BlockEntityTag");
		nbt.put("Patterns", builder.toListTag());
		
		bannerItem.hideTooltipPart(ItemStack.TooltipDisplayFlags.ADDITIONAL);
		bannerItem.setHoverName(new TranslationTextComponent("item.banner." + this.name().toLowerCase() + ".name"));
		
		return bannerItem;
	}

}
