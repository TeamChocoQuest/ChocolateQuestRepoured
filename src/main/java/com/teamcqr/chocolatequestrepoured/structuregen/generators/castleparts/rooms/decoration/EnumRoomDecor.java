package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.function.Supplier;

public enum EnumRoomDecor
{
    SHELF(0, RoomDecorShelf::new, "Shelf"),
    TABLE_S(1, RoomDecorTableSmall::new, "Small Table"),
    TABLE_M(1, RoomDecorTableMedium::new, "Medium Table");

    private final int id;
    private final Supplier<RoomDecorBase> supplier;
    private final String name;

    EnumRoomDecor(int id, Supplier<RoomDecorBase> supplier, String name)
    {
        this.id = id;
        this.supplier = supplier;
        this.name = name;
    }

    public RoomDecorBase createInstance()
    {
        return supplier.get();
    }
}
