package com.example.chocolatequest.inventory;

import com.example.chocolatequest.registry.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MerchantMenu;

public class ContainerCQRMerchant extends MerchantMenu {
    public String factionName = "DEFAULT";
    public String tradeProfile = "merchant";

    public ContainerCQRMerchant(int containerId, Inventory playerInventory, net.minecraft.network.FriendlyByteBuf buf) {
        super(containerId, playerInventory);
        if (buf != null) {
            this.factionName = buf.readUtf();
            if (buf.readableBytes() > 0) {
                this.tradeProfile = buf.readUtf();
            }
        }
    }
    
    public ContainerCQRMerchant(int containerId, Inventory playerInventory, net.minecraft.world.item.trading.Merchant trader, String faction, String tradeProfile) {
        super(containerId, playerInventory, trader);
        this.factionName = faction;
        this.tradeProfile = tradeProfile;
    }

    // We override getType to return our custom MenuType so that the client knows to open GuiMerchant
    @Override
    public net.minecraft.world.inventory.MenuType<?> getType() {
        return ModMenuTypes.CQR_MERCHANT.get();
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return true;
    }
}
