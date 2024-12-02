package com.github.catbert.tlma.api.task.v2.rule.itemhandler.task;

import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.init.registry.tlm.RegisterData;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

public class TaskFdCookPotV2 extends ItemHandlerCookTask<CookingPotBlockEntity, CookingPotRecipe> {
    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FD_COOK_POT;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CookingPotBlockEntity;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }

    @Override
    public ItemStackHandler getItemHandler(CookingPotBlockEntity cookingPotBlockEntity) {
        return cookingPotBlockEntity.getInventory();
    }

    @Override
    public int getOutputSlot() {
        return CookingPotBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSlotSize() {
        return 6;
    }

    @Override
    public int getOutputMealSlot() {
        return CookingPotBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public int getOutputContainerSlot() {
        return CookingPotBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getMealNeedContainer(CookingPotBlockEntity cookingPotBlockEntity) {
        return cookingPotBlockEntity.getContainer();
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.FARMER_DELIGHT;
    }

    @Override
    public List<StaticState> getStateSTypes() {
        return List.of(StaticState.HEATED);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("catbert", "fd_cook_pot_v2");
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.CUTTING_BOARD.get().getDefaultInstance();
    }

    @Override
    public boolean beIsSHeated(CookingPotBlockEntity cookingPotBlockEntity, EntityMaid maid) {
        return cookingPotBlockEntity.isHeated();
    }
}
