package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.function.Supplier;

public enum EnumRoomDecor
{
    NONE             (RoomDecorNone::new, "NONE"),
    SHELF            (RoomDecorShelf::new, "Shelf"),
    TABLE_SM         (RoomDecorTableSmall::new, "Small Table"),
    TABLE_MD         (RoomDecorTableMedium::new, "Medium Table"),
    BREW_STAND       (RoomDecorBrewingStand::new, "Brewing Stand"),
    CAULDRON         (RoomDecorCauldron::new, "Cauldron"),
    CRAFTING_TABLE   (RoomDecorCraftingTable::new, "Crafting Table"),
    ANVIL            (RoomDecorAnvil::new, "Anvil"),
    FURNACE          (RoomDecorFurnace::new, "Furnace"),
    ARMOR_STAND      (RoomDecorArmorStand::new, "Armor Stand");

    private final Supplier<IRoomDecor> supplier;
    private final String name;

    EnumRoomDecor(Supplier<IRoomDecor> supplier, String name)
    {
        this.supplier = supplier;
        this.name = name;
    }

    public IRoomDecor createInstance()
    {
        return supplier.get();
    }
}
