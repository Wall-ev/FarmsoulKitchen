package com.github.catbert.tlma.api.task.v2.rule.itemhandler;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public class MaidRecipe<T extends Recipe<? extends Container>> {
    public static final Pair<AdditionType, ItemStack> PAIR_EMPTY = Pair.of(AdditionType.NONE, ItemStack.EMPTY);
    public final T recipe;
    public final Pair<List<Integer>, List<List<ItemStack>>> recIngres;
    public final Pair<AdditionType, ItemStack> startAction;
    public final Pair<AdditionType, ItemStack> endAction;

    public MaidRecipe(T recipe, Pair<List<Integer>, List<List<ItemStack>>> recIngres, Pair<AdditionType, ItemStack> startAction, Pair<AdditionType, ItemStack> endAction) {
        this.recipe = recipe;
        this.recIngres = recIngres;
        this.startAction = startAction;
        this.endAction = endAction;
    }

    public MaidRecipe(T recipe, Pair<List<Integer>, List<List<ItemStack>>> recIngres) {
        this.recipe = recipe;
        this.recIngres = recIngres;
        this.startAction = PAIR_EMPTY;
        this.endAction = PAIR_EMPTY;
    }

    public static boolean isPairEmpty(Pair<AdditionType, ItemStack> pair) {
        return pair == null || pair.getFirst() == AdditionType.NONE || pair.getSecond().isEmpty();
    }

    public enum AdditionType {
        ADD_STACK,
        INTERACTION,
        NONE;
    }
}
