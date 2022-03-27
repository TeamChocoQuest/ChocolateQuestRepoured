package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BackpackInventory extends Inventory implements INamedContainerProvider
{
    protected final ItemStack stack;

    public BackpackInventory(int size, ItemStack stack)
    {
        super(size);
        this.stack = stack;

        this.loadItems();
    }

    @Override
    public void setChanged()
    {
        //this.stack.getTag().put("Items", this.createTag());
        saveItems();
        super.setChanged();
    }

    public void loadItems()
    {
        //if(compoundNBT.hasUUID("Items"))
        //{
        //    this.fromTag((ListNBT)compoundNBT.get("Items"));
        //}
        ItemStackHelper.loadAllItems(getTagCompound(stack), this.items);
    }

    public void saveItems()
    {
        //getTagCompound(stack).put("Items", this.createTag());
        ItemStackHelper.saveAllItems(getTagCompound(stack), this.items);
    }

    public CompoundNBT getTagCompound(ItemStack stack)
    {
        if(stack.getTag() == null)
        {
            CompoundNBT tag = new CompoundNBT();
            stack.setTag(tag);
        }

        return stack.getTag();
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("backpack");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
    {
        return new ContainerBackpack(windowId, playerInventory, this);
    }

    public static void openGUI(ServerPlayerEntity serverPlayerEntity, ItemStack stack)
    {
        if(!serverPlayerEntity.level.isClientSide)
        {
            NetworkHooks.openGui(serverPlayerEntity, new BackpackInventory(27, stack));//packetBuffer.writeItemStack(stack, false).writeByte(screenID));
        }
    }
}