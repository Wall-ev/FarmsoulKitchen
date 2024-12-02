package com.github.catbert.tlma.api.task.v2.accessor;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface IItemHandlerBeAccessor<R extends Recipe<? extends Container>> {

    Optional<R> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper);

    boolean canCook$tlma(R recipe);

}