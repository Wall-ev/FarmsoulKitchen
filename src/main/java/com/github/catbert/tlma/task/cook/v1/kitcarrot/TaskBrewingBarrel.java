package com.github.catbert.tlma.task.cook.v1.kitcarrot;

import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.catbert.tlma.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.BrewingBarrelRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TaskBrewingBarrel extends TaskFdPot<BrewingBarrelBlockEntity, BrewingBarrelRecipe> {
    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return null;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof BrewingBarrelBlockEntity;
    }

    @Override
    public RecipeType<BrewingBarrelRecipe> getRecipeType() {
        return null;
    }

    @Override
    public int getMealStackSlot() {
        return 0;
    }

    @Override
    public int getContainerStackSlot() {
        return 0;
    }

    @Override
    public ItemStack getFoodContainer(BrewingBarrelBlockEntity blockEntity) {
        return null;
    }

    @Override
    public ItemStackHandler getItemStackHandler(BrewingBarrelBlockEntity be) {
        return null;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputSize() {
        return 0;
    }

    @Override
    public boolean isHeated(BrewingBarrelBlockEntity be) {
        return false;
    }

    @Override
    public ResourceLocation getUid() {
        return null;
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, BrewingBarrelBlockEntity blockEntity, MaidRecipesManager<BrewingBarrelRecipe> maidRecipesManager) {
        return super.shouldMoveTo(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }
}
