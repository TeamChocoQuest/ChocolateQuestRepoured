package team.cqr.cqrepoured.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.util.Reference;

public class PotionCQR extends Potion {

	private final ResourceLocation texture;

	public PotionCQR(String name, boolean bad, int color) {
		super(bad, color);
		this.setRegistryName(Reference.MODID, name);
		this.setPotionName("effect." + Reference.MODID + "." + name);
		if (!bad) {
			this.setBeneficial();
		}
		this.texture = new ResourceLocation(Reference.MODID, "textures/mob_effect/" + name + ".png");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(texture);
		GuiHelper.drawTexture(x + 6, y + 7, 0.0D, 0.0D, 18.0D, 18.0D, 1.0D, 1.0D);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(texture);
		GuiHelper.drawTexture(x + 3, y + 3, 0.0D, 0.0D, 18.0D, 18.0D, 1.0D, 1.0D);
	}

}
