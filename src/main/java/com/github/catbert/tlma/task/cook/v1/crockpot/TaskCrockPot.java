package com.github.catbert.tlma.task.cook.v1.crockpot;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.catbert.tlma.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@LittleMaidExtension
public class TaskCrockPot extends TaskFdPot<CrockPotBlockEntity, CrockPotCookingRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "cp_crock_pot");

    @Override
    public boolean canLoaded() {
        return Mods.CP.isLoaded;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CrockPotBlockEntity;
    }

    @Override
    public RecipeType<CrockPotCookingRecipe> getRecipeType() {
        return CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get();
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
    public ItemStack getFoodContainer(CrockPotBlockEntity blockEntity) {
        return null;
    }

    @Override
    public ItemStackHandler getItemStackHandler(CrockPotBlockEntity be) {
        return be.getItemHandler();
    }

    @Override
    public int getOutputSlot() {
        return 5;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public boolean isHeated(CrockPotBlockEntity be) {
        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return CrockPotItems.CROCK_POT.get().getDefaultInstance();
    }

    @Override
    public boolean canCook(CrockPotBlockEntity be, CrockPotCookingRecipe recipe) {
        return be.isCooking() ||
               ((be.isBurning() || !getItemStackHandler(be).getStackInSlot(4).isEmpty()) &&
               be.getLevel() != null && CrockPotCookingRecipe.getRecipeFor(be.getRecipeWrapper(), be.getLevel()).isPresent());
    }

    @Override
    public MaidRecipesManager<CrockPotCookingRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false) {
            @Override
            //todo
            protected Pair<List<Integer>, List<Item>> getAmountIngredient(CrockPotCookingRecipe recipe, Map<Item, Integer> available) {
                List<IRequirement> requirements = recipe.getRequirements();
                boolean[] canMake = {true};
                boolean[] single = {false};
                List<Item> invIngredient = new ArrayList<>();
                Map<Item, Integer> itemTimes = new HashMap<>();

//                for (IRequirement requirement : requirements) {
//                    boolean hasIngredient = false;
//                    for (Item item : available.keySet()) {
//                        ItemStack stack = item.getDefaultInstance();
//                        if (requirement.test(stack)) {
//                            invIngredient.add(item);
//                            hasIngredient = true;
//
//                            if (stack.getMaxStackSize() == 1) {
//                                single[0] = true;
//                                itemTimes.put(item, 1);
//                            } else {
//                                itemTimes.merge(item, 1, Integer::sum);
//                            }
//
//                            break;
//                        }
//                    }
//
//                    if (!hasIngredient) {
//                        canMake[0] = false;
//                        itemTimes.clear();
//                        invIngredient.clear();
//                        break;
//                    }
//                }

                if (!canMake[0] || invIngredient.stream().anyMatch(item -> available.get(item) <= 0)) {
                    return Pair.of(new ArrayList<>(), new ArrayList<>());
                }

                int maxCount = 64;
                if (single[0] || this.isSingle()) {
                    maxCount = 1;
                } else {
                    for (Item item : itemTimes.keySet()) {
                        maxCount = Math.min(maxCount, item.getDefaultInstance().getMaxStackSize());
                        maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
                    }
                }

                List<Integer> countList = new ArrayList<>();
                for (Item item : invIngredient) {
                    countList.add(maxCount);
                    available.put(item, available.get(item) - maxCount);
                }

                return Pair.of(countList, invIngredient);
            }
        };
    }
}
