package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.function.Supplier;

public enum EnumRoomDecor
{
    SHELF(0, RoomDecorShelf::new, "Shelf"),
    TABLE(1, RoomDecorTable::new, "Table");

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
