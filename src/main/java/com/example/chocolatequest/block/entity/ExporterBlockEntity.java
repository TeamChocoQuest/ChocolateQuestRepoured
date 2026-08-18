package com.example.chocolatequest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.example.chocolatequest.registry.ModBlockEntities;

public class ExporterBlockEntity extends BlockEntity {

    private String structureName = "NoName";
    private int startX = 0;
    private int startY = 0;
    private int startZ = 0;
    private int endX = 0;
    private int endY = 0;
    private int endZ = 0;
    private boolean relativeMode = true;
    private boolean ignoreEntities = true;

    public ExporterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXPORTER.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("StructureName", structureName);
        tag.putInt("StartX", startX);
        tag.putInt("StartY", startY);
        tag.putInt("StartZ", startZ);
        tag.putInt("EndX", endX);
        tag.putInt("EndY", endY);
        tag.putInt("EndZ", endZ);
        tag.putBoolean("RelativeMode", relativeMode);
        tag.putBoolean("IgnoreEntities", ignoreEntities);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("StructureName")) structureName = tag.getString("StructureName");
        if (tag.contains("StartX")) startX = tag.getInt("StartX");
        if (tag.contains("StartY")) startY = tag.getInt("StartY");
        if (tag.contains("StartZ")) startZ = tag.getInt("StartZ");
        if (tag.contains("EndX")) endX = tag.getInt("EndX");
        if (tag.contains("EndY")) endY = tag.getInt("EndY");
        if (tag.contains("EndZ")) endZ = tag.getInt("EndZ");
        if (tag.contains("RelativeMode")) relativeMode = tag.getBoolean("RelativeMode");
        if (tag.contains("IgnoreEntities")) ignoreEntities = tag.getBoolean("IgnoreEntities");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    public void setValues(String name, int sx, int sy, int sz, int ex, int ey, int ez, boolean relative, boolean ignoreEnt) {
        this.structureName = name;
        this.startX = sx;
        this.startY = sy;
        this.startZ = sz;
        this.endX = ex;
        this.endY = ey;
        this.endZ = ez;
        this.relativeMode = relative;
        this.ignoreEntities = ignoreEnt;
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public String getStructureName() { return structureName; }
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public int getStartZ() { return startZ; }
    public int getEndX() { return endX; }
    public int getEndY() { return endY; }
    public int getEndZ() { return endZ; }
    public boolean isRelativeMode() { return relativeMode; }
    public boolean isIgnoreEntities() { return ignoreEntities; }
}
