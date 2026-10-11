package team.cqr.cqrepoured.integration.jei.trade;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;

public class TradeCategory implements IRecipeCategory<TradeWrapper> {

	public static final String ID = new ResourceLocation(CQRMain.MODID, "trading").toString();

	private static final ResourceLocation BACKGROUND = new ResourceLocation(CQRMain.MODID, "textures/gui/container/jei_trade_background.png");
	private static final ResourceLocation ICON = new ResourceLocation(CQRMain.MODID, "textures/gui/container/jei_trade_icon.png");
	private static final int WIDTH = 178;
	private static final int HEIGHT = 57;

	private static final int OUTPUT_SLOT = 0;
	private static final int INPUT_SLOT_1 = 1;

	private final IDrawable background;
	private final IDrawable icon;

	public TradeCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, WIDTH, HEIGHT)
				.setTextureSize(WIDTH, HEIGHT)
				.build();
		icon = new IDrawable() {
			@Override
			public int getWidth() {
				return 16;
			}

			@Override
			public int getHeight() {
				return 16;
			}

			@Override
			public void draw(Minecraft minecraft, int xOffset, int yOffset) {
				minecraft.getTextureManager().bindTexture(ICON);
				Gui.drawScaledCustomSizeModalRect(xOffset, yOffset, 0.0F, 0.0F, 128, 128, 16, 16, 128.0F, 128.0F);
			}
		};
	}

	@Override
	public String getUid() {
		return ID;
	}

	@Override
	public String getTitle() {
		return "Trading";
	}

	@Override
	public String getModName() {
		return "Chocolate Quest Repoured";
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, TradeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(OUTPUT_SLOT, false, 156, 4);
		guiItemStacks.set(OUTPUT_SLOT, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

		for (int i = 0; i < 4; i++) {
			guiItemStacks.init(INPUT_SLOT_1 + i, true, i * 18 + 54, 4);
		}
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		for (int i = 0; i < inputs.size(); i++) {
			guiItemStacks.set(INPUT_SLOT_1 + i, ingredients.getInputs(VanillaTypes.ITEM).get(i));
		}
	}

}
