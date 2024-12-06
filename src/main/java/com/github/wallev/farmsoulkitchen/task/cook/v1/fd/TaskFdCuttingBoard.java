package com.github.wallev.farmsoulkitchen.task.cook.v1.fd;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.task.ai.MaidCookMoveTask;
import com.github.wallev.farmsoulkitchen.task.ai.MaidCuttingMakeTask;
import com.github.wallev.farmsoulkitchen.task.cook.handler.v2.MaidRecipesManager;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TaskFdCuttingBoard implements ICookTask<CuttingBoardBlockEntity, CuttingBoardRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "fd_cutting_board");

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CuttingBoardBlockEntity;
    }

    @Override
    public RecipeType<CuttingBoardRecipe> getRecipeType() {
        return ModRecipeTypes.CUTTING.get();
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) return Collections.emptyList();
//        LOGGER.info("create brain tasks: " + maid.level() + " " + maid + " " + maid.level.isClientSide);

        MaidRecipesManager<CuttingBoardRecipe> cookingPotRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidCookMoveTask<CuttingBoardBlockEntity, CuttingBoardRecipe> maidCookMoveTask = new MaidCookMoveTask<>(maid, this, cookingPotRecipeMaidRecipesManager);
        MaidCuttingMakeTask maidCookMakeTask = new MaidCuttingMakeTask(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, CuttingBoardBlockEntity blockEntity, MaidRecipesManager<CuttingBoardRecipe> recManager) {
        if (blockEntity.getStoredItem().isEmpty() && !recManager.getRecipesIngredients().isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, CuttingBoardBlockEntity blockEntity, MaidRecipesManager<CuttingBoardRecipe> recManager) {

    }

    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, CuttingBoardBlockEntity blockEntity, MaidRecipesManager<CuttingBoardRecipe> recManager, Consumer<Item> item) {
        if (blockEntity.getStoredItem().isEmpty() && !recManager.getRecipesIngredients().isEmpty()) {
            Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();

            CombinedInvWrapper availableInv = maid.getAvailableInv(false);

            List<ItemStack> itemStacks = recipeIngredient.getSecond().get(0);
            for (ItemStack itemStack : itemStacks) {
                if (!itemStack.isEmpty()) {
                    ItemStack offhandItem = maid.getOffhandItem();
                    if (offhandItem != itemStack) {
                        ItemHandlerHelper.insertItemStacked(availableInv, offhandItem, false);
                    }

                    item.accept(itemStack.getItem());
                    maid.setItemInHand(InteractionHand.OFF_HAND, itemStack.copy());
                    itemStack.setCount(0);
                    break;
                }
            }

            List<ItemStack> toolStacks = recipeIngredient.getSecond().get(1);
            for (ItemStack itemStack : toolStacks) {
                if (!itemStack.isEmpty()) {
                    ItemStack maidMainHandItem = maid.getMainHandItem();
                    if (maidMainHandItem != itemStack) {
                        ItemHandlerHelper.insertItemStacked(availableInv, maidMainHandItem, false);
                    }

                    maid.setItemInHand(InteractionHand.MAIN_HAND, itemStack.copy());
                    itemStack.setCount(0);
                    break;
                }
            }

        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return ModBlocks.CUTTING_BOARD.get().asItem().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FD_CUTTING_BOARD;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        CuttingBoardRecipe cuttingBoardRecipe = (CuttingBoardRecipe) recipe;
        NonNullList<Ingredient> ingredients = cuttingBoardRecipe.getIngredients();
        ingredients.add(cuttingBoardRecipe.getTool());
        return ingredients;
    }
}