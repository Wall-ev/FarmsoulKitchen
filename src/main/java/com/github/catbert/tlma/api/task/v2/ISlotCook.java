package com.github.catbert.tlma.api.task.v2;

import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.List;

public interface ISlotCook<B extends BlockEntity, R extends Recipe<? extends Container>> {

    /** 待取出食物——最终槽位——可直接拿出来 */
    default boolean hasOutput(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return false;
    }

    default boolean hasOutputMeal(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return false;
    }
    default boolean hasOutputContainer(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return false;
    }

    default boolean hasInput(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return false;
    }

    default boolean hasInputPre(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return false;
    }
    default boolean hasInputPost(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return false;
    }

    default boolean hasInputFuel(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return false;
    }



    ItemStack getOutputStack(B be);

    default ItemStack getOutputMealStack(B be) {
        return ItemStack.EMPTY;
    }
    default ItemStack getOutputContainerStack(B be) {
        return ItemStack.EMPTY;
    }
    default ItemStack getMealNeedContainer(B be) {
        return ItemStack.EMPTY;
    }

    List<ItemStack> getInputsStack(B be);

    default ItemStack getInputPreStack(B be) {
        return ItemStack.EMPTY;
    }
    default ItemStack getInputPostStack(B be) {
        return ItemStack.EMPTY;
    }

    default ItemStack getInputFuelStack(B be) {
        return ItemStack.EMPTY;
    }


    int getOutputSlot();

    default int getOutputMealSlot() {
        return -1;
    }
    default int getOutputContainerSlot() {
        return -1;
    }

    default int getInputPreSlots() {
        return 0;
    }
    int getInputSlotSize();

    default int getInputPreSlot() {
        return -1;
    }
    default int getInputPostSlot() {
        return -1;
    }
    default int getInputFuelSlot() {
        return -1;
    }

    interface ItemHandler<B extends BlockEntity, R extends Recipe<? extends Container>> extends ISlotCook<B, R>, IBeInv.IItemHandlerInv<B, ItemStackHandler> {
        @Override
        default boolean hasOutput(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getOutputStack(be).isEmpty();
        }

        @Override
        default boolean hasOutputMeal(ServerLevel serverLevel, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getOutputMealStack(be).isEmpty();
        }

        @Override
        default boolean hasOutputContainer(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getOutputContainerStack(be).isEmpty();
        }

        @Override
        default boolean hasInput(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            ItemStackHandler itemHandler = getItemHandler(be);
            for (int i = getInputPreSlots(); i < getInputPreSlots() + getInputSlotSize(); i++) {
                if (!itemHandler.getStackInSlot(i).isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        default boolean hasInputPre(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getInputPreStack(be).isEmpty();
        }

        @Override
        default boolean hasInputPost(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getInputPostStack(be).isEmpty();
        }

        @Override
        default boolean hasInputFuel(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getInputFuelStack(be).isEmpty();
        }

        @Override
        default ItemStack getOutputStack(B be) {
            return getStackInSlot(be, getOutputSlot());
        }

        @Override
        default ItemStack getOutputMealStack(B be) {
            return getStackInSlot(be, getOutputMealSlot());
        }

        @Override
        default ItemStack getOutputContainerStack(B be) {
            return getStackInSlot(be, getOutputContainerSlot());
        }

        @Override
        default List<ItemStack> getInputsStack(B be) {
            ItemStackHandler itemHandler = getItemHandler(be);
            List<ItemStack> itemStacks = new ArrayList<>();
            for (int i = 0; i < getInputPreSlots() + getInputSlotSize(); i++) {
                ItemStack stackInSlot = itemHandler.getStackInSlot(i);
                itemStacks.add(stackInSlot);
            }
            return itemStacks;
        }

        @Override
        default ItemStack getInputPreStack(B be) {
            return getStackInSlot(be, getInputPreSlot());
        }

        @Override
        default ItemStack getInputPostStack(B be) {
            return getStackInSlot(be, getInputPostSlot());
        }

        @Override
        default ItemStack getInputFuelStack(B be) {
            return getStackInSlot(be, getInputFuelSlot());
        }

        default int findFuelMaidSlot(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return -1;
        }
    }

    interface ContainerHandler<B extends BlockEntity, R extends Recipe<? extends Container>, T extends Container> extends ISlotCook<B, R>, IBeInv.IContainerInv<B, T> {

        @Override
        default boolean hasOutput(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getOutputStack(be).isEmpty();
        }

        @Override
        default boolean hasOutputMeal(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getOutputMealStack(be).isEmpty();
        }

        @Override
        default boolean hasOutputContainer(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getOutputStack(be).isEmpty();
        }

        @Override
        default boolean hasInput(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            Container container = getContainer(be);
            for (int i = 0; i < getInputPreSlots() + getInputSlotSize(); i++) {
                if (!container.getItem(i).isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        default boolean hasInputPre(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getInputPreStack(be).isEmpty();
        }

        @Override
        default boolean hasInputPost(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getInputPostStack(be).isEmpty();
        }

        @Override
        default boolean hasInputFuel(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
            return !getInputFuelStack(be).isEmpty();
        }

        @Override
        default ItemStack getOutputStack(B be) {
            return getStackInSlot(be, getOutputSlot());
        }

        @Override
        default ItemStack getOutputMealStack(B be) {
            return getStackInSlot(be, getOutputMealSlot());
        }

        @Override
        default ItemStack getOutputContainerStack(B be) {
            return getStackInSlot(be, getOutputContainerSlot());
        }

        @Override
        default List<ItemStack> getInputsStack(B be) {
            Container itemHandler = getContainer(be);
            List<ItemStack> itemStacks = new ArrayList<>();
            for (int i = 0; i < getInputPreSlots() + getInputSlotSize(); i++) {
                ItemStack stackInSlot = itemHandler.getItem(i);
                itemStacks.add(stackInSlot);
            }
            return itemStacks;
        }

        @Override
        default ItemStack getInputPreStack(B be) {
            return getStackInSlot(be, getInputPreSlot());
        }

        @Override
        default ItemStack getInputPostStack(B be) {
            return getStackInSlot(be, getInputPostSlot());
        }

        @Override
        default ItemStack getInputFuelStack(B be) {
            return getStackInSlot(be, getInputFuelSlot());
        }
    }

    enum TaskType {
        NORMAL,
        FARMER_DELIGHT,
        ;
    }
}
