package com.github.catbert.tlma.task.cook.handler;

import com.github.catbert.tlma.inventory.container.item.BagType;
import com.github.catbert.tlma.item.bauble.ItemCookBag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookBagInventory implements ICookInventory{
    private final ItemStack stack;
    private Map<BagType, ItemStackHandler> containers;
    private final Map<Item, Integer> inventoryItem = new HashMap<>();
    private final Map<Item, List<ItemStack>> inventoryStack = new HashMap<>();
    private final List<ItemStack> lastInvStack = new ArrayList<>();

    public CookBagInventory(ItemStack stack) {
        this.stack = stack;
        this.refreshInv();
    }

    public void refreshInv() {
        clearCacheStackInfo();
        containers = ItemCookBag.getContainers(stack);
        ItemStackHandler availableInv = containers.getOrDefault(BagType.INGREDIENT, new ItemStackHandler(BagType.INGREDIENT.size * 9));
        List<Integer> blackSlots = getBlackSlots();
        for (int i = 0; i < availableInv.getSlots(); i++) {
            ItemStack stack = availableInv.getStackInSlot(i);
            proseLastInvStack(i, stack);
            if (blackSlots.contains(i)) continue;
            if (stack.isEmpty()) continue;
            add(stack);
        }
    }

    public void proseLastInvStack(int index, ItemStack invStack) {
        if (index < lastInvStack.size()) {
            ItemStack cacheStack = lastInvStack.get(index);
            if (cacheStack.is(invStack.getItem()) && cacheStack != invStack) {
                cacheStack.setCount(invStack.getCount());
                return;
            }
        }
        lastInvStack.add(invStack.copy());
    }

    public void clearCacheStackInfo() {
        inventoryItem.clear();
        inventoryStack.clear();
        lastInvStack.clear();
    }

    public List<Integer> getBlackSlots() {
        List<Integer> blockSlots = new ArrayList<>();
//        BaubleItemHandler maidBauble = this.maid.getMaidBauble();
//        for (int i = 0; i < maidBauble.getSlots(); i++) {
//            if (maidBauble.getStackInSlot(i).getItem() instanceof ItemWirelessIO itemWirelessIO) {
////                itemWirelessIO.get
//            }
//        }
        return blockSlots;
    }

    public void add(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (this.inventoryStack.get(item) == null) {
                List<ItemStack> stackList = new ArrayList<>();
                stackList.add(stack);
                this.inventoryStack.put(item, stackList);
            } else {
                this.inventoryStack.get(item).add(stack);
            }

            this.inventoryItem.merge(item, stack.getCount(), (a, b) -> a + b);
        }
    }

    public Map<Item, List<ItemStack>> getInventoryStack() {
        return inventoryStack;
    }

    public Map<Item, Integer> getInventoryItem() {
        return inventoryItem;
    }

    public ItemStack getStack() {
        return stack;
    }

    public List<ItemStack> getLastInvStack() {
        return lastInvStack;
    }

    @Override
    public void syncInv() {
        ItemCookBag.setContainer(stack, containers);
    }
}
