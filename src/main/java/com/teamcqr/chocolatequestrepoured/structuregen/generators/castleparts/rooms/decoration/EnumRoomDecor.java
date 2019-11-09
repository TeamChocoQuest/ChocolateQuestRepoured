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
    FURNACE          (RoomDecorFurnace::new, "Furnace");

    private final Supplier<RoomDecorBase> supplier;
    private final String name;

    EnumRoomDecor(Supplier<RoomDecorBase> supplier, String name)
    {
        this.supplier = supplier;
        this.name = name;
    }

    public RoomDecorBase createInstance()
    {
        return supplier.get();
    }
}
