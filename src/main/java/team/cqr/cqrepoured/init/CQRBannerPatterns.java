package team.cqr.cqrepoured.init;

import java.lang.reflect.Field;
import java.util.Locale;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.banner.EBanners;

@EventBusSubscriber(modid = CQRConstants.MODID, bus = Bus.MOD)
public class CQRBannerPatterns {

	public static BannerPattern CQ_BLANK = addBanner("cq_blank");
	public static BannerPattern WITHER_SKULL = addBanner("cq_wither_skull");
	public static BannerPattern WITHER_SKULL_EYES = addBanner("cq_wither_eyes");
	public static BannerPattern FIRE = addBanner("cq_fire");
	public static BannerPattern EMERALD = addBanner("cq_emerald");
	public static BannerPattern BONES = addBanner("cq_bones");
	public static BannerPattern WALKER_BACKGROUND = addBanner("walker_black_bg");
	public static BannerPattern WALKER_BORDER = addBanner("walker_border");
	public static BannerPattern WALKER_INNER_BORDER = addBanner("walker_inner_border");
	public static BannerPattern WALKER_SKULL = addBanner("walker_skull");

	public static BannerPattern addBanner(String name) {
		return addBanner(name, null);
	}

	public static BannerPattern addBanner(String name, ItemStack craftingStack) {
		return BannerPattern.create(name.toUpperCase(), name, CQRConstants.MODID + "." + name, true);
	}

	//!!!CALL AFTER ITEMS ARE REGISTERED!!!
	@SubscribeEvent
	public static void registerBanners(RegistryEvent.Register<Item> event) {
		// Needed to just initialize that crap lol
		try {
			for (Field f : CQRBannerPatterns.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof BannerPattern) {
					BannerPattern pattern = (BannerPattern) obj;
					String name = f.getName().replace("PATTERN_", "").toLowerCase(Locale.ROOT);
					event.getRegistry().register(new BannerPatternItem(pattern, (new Item.Properties()).stacksTo(1).tab(CQRMain.CQR_BANNERS_TAB)).setRegistryName(CQRConstants.MODID + ":banner_pattern_" + name));

				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		NonNullList<ItemStack> bannersCQR = NonNullList.create();
		for(EBanners banner : EBanners.values()) {
			bannersCQR.add(banner.getBanner());
		}
		CQRMain.CQR_BANNERS_TAB.fillItemList(bannersCQR);
	}

}
