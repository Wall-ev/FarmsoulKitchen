package com.catbert.tlma.api.task.cook;

import com.catbert.tlma.api.IMaidAction;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public interface ICook extends IMaidAction {

    int getOutputSlot();

    default int getInputStartSlot() {
        return 0;
    }

    int getInputEndSlot();

    default void extractOutputStack(ItemStackHandler inventory, CombinedInvWrapper availableInv, BlockEntity blockEntity) {
        ItemStack stackInSlot = inventory.getStackInSlot(this.getOutputSlot());

        if (stackInSlot.isEmpty()) return;
        inventory.extractItem(this.getOutputSlot(), stackInSlot.getCount(), false);
        ItemHandlerHelper.insertItemStacked(availableInv, stackInSlot.copy(), false);
        blockEntity.setChanged();
    }


    default void extractInputStack(ItemStackHandler inventory, CombinedInvWrapper availableInv, BlockEntity blockEntity) {
        for (int i = this.getInputStartSlot(); i <= this.getInputEndSlot(); ++i) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                inventory.extractItem(i, stackInSlot.getCount(), false);
                ItemHandlerHelper.insertItemStacked(availableInv, stackInSlot.copy(), false);
            }
        }
        blockEntity.setChanged();
    }

    default void insertInputStack(ItemStackHandler inventory, CombinedInvWrapper availableInv, BlockEntity blockEntity, Pair<Integer, List<List<ItemStack>>> ingredientPair) {
        Integer amount = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amount, ingredients)) {
            for (int i = getInputStartSlot(), j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                insertAndShrink(inventory, amount, ingredients, j, i);
            }
            blockEntity.setChanged();
        }

        updateIngredient(ingredientPair);
    }

    default void updateIngredients(List<Pair<Integer, List<List<ItemStack>>>> recipesIngredients) {

    }

    default void updateIngredient(Pair<Integer, List<List<ItemStack>>> ingredientPair) {

    }

    default boolean hasEnoughIngredient(Integer amount, List<List<ItemStack>> ingredients) {
        boolean canInsert = true;

        for (List<ItemStack> ingredient : ingredients) {
            int actualCount = amount;
            for (ItemStack itemStack : ingredient) {
                actualCount -= itemStack.getCount();
                if (actualCount <= 0) {
                    break;
                }
            }

            if (actualCount > 0) {
                canInsert = false;
                break;
            }
        }

        return canInsert;
    }

    default void insertAndShrink(ItemStackHandler inventory, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
        int shinkNum = amount;
        for (ItemStack itemStack : ingredient.get(ingredientIndex)) {
            int count = itemStack.getCount();

            if (count >= shinkNum) {
                inventory.insertItem(slotIndex, itemStack.copyWithCount(shinkNum), false);
                itemStack.shrink(shinkNum);
                break;
            } else {
                inventory.insertItem(slotIndex, itemStack.copyWithCount(count), false);
                itemStack.shrink(count);
                shinkNum -= count;
                if (shinkNum <= 0) {
                    break;
                }
            }
        }
    }

    default boolean hasInput(ItemStackHandler inventory) {
        for (int i = getInputStartSlot(); i <= getInputEndSlot(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }
}