package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityDataPart implements IStructure {
    public static class ExtendedEntityData {
        private ResourceLocation resLoc;
        private NBTTagCompound nbt;

        public ExtendedEntityData(ResourceLocation resLoc, @Nullable NBTTagCompound nbt) {
            this.resLoc = resLoc;
            this.nbt = nbt;
        }

        public NBTTagCompound getNbt() {
            return nbt;
        }

        public ResourceLocation getResLoc() {
            return resLoc;
        }
    }

    private BlockPos pos;
    private BlockPos size;
    private ExtendedEntityData[][][] extendedEntities;

    public EntityDataPart(BlockPos pos, BlockPos size, ExtendedEntityData[][][] extendedBlockStateArray) {
        this.pos = pos;
        this.size = size;
        this.extendedEntities = new ExtendedEntityData[this.size.getX()][this.size.getY()][this.size.getZ()];

        for (int x = 0; x < this.size.getX() && x < extendedBlockStateArray.length; x++) {
            for (int y = 0; y < this.size.getY() && y < extendedBlockStateArray[x].length; y++) {
                for (int z = 0; z < this.size.getZ() && z < extendedBlockStateArray[x][y].length; z++) {
                    this.extendedEntities[x][y][z] = extendedBlockStateArray[x][y][z];
                }
            }
        }
    }

    @Override
    public void generate(World world, ProtectedRegion protectedRegion)
    {
        //Map<BlockPos, IBlockState> map = new HashMap<>();
        for (int x = 0; x < extendedEntities.length; x++) {
            for (int y = 0; y < extendedEntities[x].length; y++) {
                for (int z = 0; z < extendedEntities[x][y].length; z++) {
                    if (extendedEntities[x][y][z] != null) {
                        Entity entity = EntityList.createEntityByIDFromName(extendedEntities[x][y][z].getResLoc(), world);
                        entity.readFromNBT(extendedEntities[x][y][z].getNbt());
                        entity.setUniqueId(MathHelper.getRandomUUID());
                        //Don't need to do anything with the position key since each entity stores its position already
                        world.spawnEntity(entity);
                    }
                }
            }
        }
    }

    @Override
    public boolean canGenerate(World world) {
        return world.isAreaLoaded(this.pos, this.pos.add(this.size));
    }

    @Override
    public NBTTagCompound writeToNBT()
    {
        return new NBTTagCompound();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        ;
    }

    public static List<EntityDataPart> splitExtendedEntityDataMap(Map<BlockPos, ExtendedEntityData> map) {
        return EntityDataPart.splitExtendedEntityDataList(new ArrayList<>(map.entrySet()));
    }

    public static List<EntityDataPart> splitExtendedEntityDataList(List<Map.Entry<BlockPos, ExtendedEntityData>> entryList) {
        if (!entryList.isEmpty()) {
            int startX = entryList.get(0).getKey().getX();
            int startY = entryList.get(0).getKey().getY();
            int startZ = entryList.get(0).getKey().getZ();
            int endX = startX;
            int endY = startY;
            int endZ = startZ;

            for (Map.Entry<BlockPos, ExtendedEntityData> entry : entryList) {
                BlockPos pos = entry.getKey();
                if (pos.getX() < startX) {
                    startX = pos.getX();
                }
                if (pos.getY() < startY) {
                    startY = pos.getY();
                }
                if (pos.getZ() < startZ) {
                    startZ = pos.getZ();
                }
                if (pos.getX() > endX) {
                    endX = pos.getX();
                }
                if (pos.getY() > endY) {
                    endY = pos.getY();
                }
                if (pos.getZ() > endZ) {
                    endZ = pos.getZ();
                }
            }

            ExtendedEntityData[][][] extendedEntities = new ExtendedEntityData[endX - startX + 1][endY - startY + 1][endZ - startZ + 1];

            for (Map.Entry<BlockPos, ExtendedEntityData> entry : entryList) {
                BlockPos pos = entry.getKey();
                extendedEntities[pos.getX() - startX][pos.getY() - startY][pos.getZ() - startZ] = entry.getValue();
            }

            return EntityDataPart.split(new BlockPos(startX, startY, startZ), extendedEntities);
        }

        return Collections.emptyList();
    }

    /**
     * @param pos   Start position
     * @param array Array keys are the relative coordinates to the start position
     */
    public static List<EntityDataPart> split(BlockPos pos, ExtendedEntityData[][][] array) {
        return EntityDataPart.split(pos, array, 16);
    }

    /**
     * @param pos   Start position
     * @param array Array keys are the relative coordinates to the start position
     * @param size  Size of the parts
     */
    public static List<EntityDataPart> split(BlockPos pos, ExtendedEntityData[][][] array, int size) {
        List<EntityDataPart> list = new ArrayList<>();

        if (array.length > 0 && array[0].length > 0 && array[0][0].length > 0) {
            int xIterations = MathHelper.ceil((double) array.length / size);
            int yIterations = MathHelper.ceil((double) array[0].length / size);
            int zIterations = MathHelper.ceil((double) array[0][0].length / size);
            for (int y = 0; y < yIterations; y++) {
                for (int x = 0; x < xIterations; x++) {
                    for (int z = 0; z < zIterations; z++) {
                        int partOffsetX = x * size;
                        int partOffsetY = y * size;
                        int partOffsetZ = z * size;
                        int partSizeX = x == xIterations - 1 ? array.length - partOffsetX : size;
                        int partSizeY = y == yIterations - 1 ? array[0].length - partOffsetY : size;
                        int partSizeZ = z == zIterations - 1 ? array[0][0].length - partOffsetZ : size;
                        ExtendedEntityData[][][] entities = new ExtendedEntityData[partSizeX][partSizeY][partSizeZ];
                        boolean empty = true;

                        for (int x1 = 0; x1 < partSizeX; x1++) {
                            int x2 = x1 + partOffsetX;
                            for (int y1 = 0; y1 < partSizeY && y1 < array[x2].length; y1++) {
                                int y2 = y1 + partOffsetY;
                                for (int z1 = 0; z1 < partSizeZ && z1 < array[x2][y2].length; z1++) {
                                    entities[x1][y1][z1] = array[x2][y2][z1 + partOffsetZ];
                                    if (empty && entities[x1][y1][z1] != null) {
                                        empty = false;
                                    }
                                }
                            }
                        }

                        if (!empty) {
                            list.add(new EntityDataPart(pos.add(partOffsetX, partOffsetY, partOffsetZ), new BlockPos(partSizeX, partSizeY, partSizeZ), entities));
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public BlockPos getPos()
    {
        return pos;
    }

    @Override
    public BlockPos getSize()
    {
        return size;
    }
}
