package com.github.catbert.tlma.api.task.v2.rule.itemhandler.v2;

import com.github.catbert.tlma.api.task.v2.ISlotCook;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.catbert.tlma.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.*;

public interface ItemHandlerCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends ISlotCook.ItemHandler<B, R>, IBeCookState<B> {
    Map<TaskType, List<CheckCondition.ItemHandler>> getRules();

    @SuppressWarnings("all")
    default List<CheckCondition.ItemHandler> createCheckCondition(CheckCondition.ItemHandler... checkCondition) {
        return new ArrayList<>(Arrays.asList(checkCondition));
    }

    @SuppressWarnings("all")
    default List<CheckCondition.ItemHandler> getNormalRule() {
        return createCheckCondition((maidInv, beInv, level, maid, be, recManager) -> {
            return this.beOutputCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        }, (maidInv, beInv, level, maid, be, recManager) -> {
            return this.beCookCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        }, (maidInv, beInv, level, maid, be, recManager) -> {
            return this.beInputsCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
//        }, (maidInv, beInv, level, maid, be, recManager) -> {
//            return this.beCookAExtra(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        });
    }

    @SuppressWarnings("all")
    default List<CheckCondition.ItemHandler> getFdRule() {
        return createCheckCondition((maidInv, beInv, level, maid, be, recManager) -> {
            return this.beOutputCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        }, (maidInv, beInv, level, maid, be, recManager) -> {
            return this.beMealCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        }, (maidInv, beInv, level, maid, be, recManager) -> {
            return this.beCookCanMoveTo(maidInv, beInv, level, maid, ((B) be), (MaidRecipesManager<R>) recManager)
                    && !hasOutputMeal(level, maid, ((B) be), (MaidRecipesManager<R>) recManager);
        }, (maidInv, beInv, level, maid, be, recManager) -> {
            return this.beInputsCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        }, (maidInv, beInv, level, maid, be, recManager) -> {
            return this.beContainerCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
//        }, (maidInv, beInv, level, maid, be, recManager) -> {
//            return this.beCookAExtra(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        });
    }

    @SuppressWarnings("all")
    default List<CheckCondition.ItemHandler> getFuranceRule() {
        return createCheckCondition((maidInv, beInv, level, maid, be, recManager) -> {
            return this.beOutputCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        }, (maidInv, beInv, level, maid, be, recManager) -> {
            return this.beCookCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        }, (maidInv, beInv, level, maid, be, recManager) -> {
            return this.beInputsCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
//        }, (maidInv, beInv, level, maid, be, recManager) -> {
//            return this.beNeedFuelCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
//        }, (maidInv, beInv, level, maid, be, recManager) -> {
//            return this.beNoNeedFuelCanMoveTo(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
//        }, (maidInv, beInv, level, maid, be, recManager) -> {
//            return this.beCookAExtra(maidInv, beInv, level, maid, (B) be, (MaidRecipesManager<R>) recManager);
        });
    }

    default boolean canMoveToBe(List<CheckCondition.ItemHandler> checkConditions, CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        for (CheckCondition.ItemHandler brtCheckCondition : checkConditions) {
            if (brtCheckCondition.test(maidInv, beInv, level, maid, be, recManager)) {
                return true;
            }
        }
        return false;
    }

    default boolean beCookSPreExtra(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        for (StaticState actionState : getStateSTypes()) {
            boolean extra = switch (actionState) {
                case HEATED -> this.beIsSHeated(be, maid);
                case TEMPERA -> this.beIsSEnoughTempera(be, maid);
                case NONE -> true;
            };
            if (!extra) {
                return false;
            }
        }
        return true;
    }

    default boolean beCookSExtra(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        for (StaticState actionState : getStateSTypes()) {
            boolean extra = switch (actionState) {
                case HEATED -> this.beIsSHeated(be, maid);
                case TEMPERA -> this.beIsSEnoughTempera(be, maid);
                case NONE -> true;
            };
            if (!extra) {
                return false;
            }
        }
        return true;
    }

    default boolean beCookAExtra(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        for (ActionState actionState : getStateATypes()) {
            boolean extra = switch (actionState) {
                case WATER -> this.beIsAEnoughWater(be, maid);
                case FUEL -> this.beIsAEnoughFuel(be, maid);
                case NONE -> true;
            };
            if (!extra) {
                return false;
            }
        }
        return true;
    }



    /**
     * 厨具内部的条件:
     * 厨具内的原料是否符合配方，一盘通过mixin获得...
     */
    @SuppressWarnings("unchecked")
    default boolean beInnerCanCookNow(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        Optional<R> matchingRecipe = beInnerMatchRec(be, beInv);
        return (matchingRecipe.isPresent() && beInnerRecCanCook(be, matchingRecipe.get()));
    }

    @SuppressWarnings("unchecked")
    default Optional<R> beInnerMatchRec(B be, ItemStackHandler inventoryHandler) {
        RecipeWrapper recipeWrapper = new RecipeWrapper(inventoryHandler);
        return ((IFdCbeAccessor<R>) be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @SuppressWarnings("unchecked")
    default boolean beInnerRecCanCook(B be, R rec) {
        return ((IFdCbeAccessor<R>) be).canCook$tlma(rec);
    }

//    /**
//     * 女仆移动至厨具的条件:
//     * 厨锅内可以烹饪但缺少燃料，且女仆有该燃料
//     */
//    default boolean beNeedFuelCanMoveTo(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
//        return beInnerCanCookNow(maidInv, beInv, level, maid, be, recManager) && findFuelMaidSlot(maidInv, beInv, level, maid, be, recManager) > -1;
//    }
//
//    /**
//     * 女仆移动至厨具的条件:
//     * 厨锅内不可以烹饪，且燃料槽有燃料
//     */
//    default boolean beNoNeedFuelCanMoveTo(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
//        return !beInnerCanCookNow(maidInv, beInv, level, maid, be, recManager) && hasInputFuel(level, maid, be, recManager);
//    }

    /**
     * 女仆移动至厨具的条件:
     * 厨锅内有未能取出的烹饪好的食物（需要容器），且女仆背包内有该容器
     */
    default boolean beMealCanMoveTo(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        if (hasOutputMeal(level, maid, be, recManager)) {
            ItemStack outputContainerStack = getMealNeedContainer(be);
            return ItemsUtil.findStackSlot(maidInv, stack -> stack.is(outputContainerStack.getItem())) > -1;
        }
        return false;
    }

    /**
     * 女仆移动至厨具的条件:
     * 厨锅内未能烹饪，且有容器
     */
    default boolean beContainerCanMoveTo(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return !beInnerCanCookNow(maidInv, beInv, level, maid, be, recManager) && hasOutputContainer(level, maid, be, recManager);
    }

    /**
     * 女仆移动至厨具的条件:
     * 有最终物品
     */
    default boolean beOutputCanMoveTo(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return hasOutput(level, maid, be, recManager);
    }

    /**
     * 女仆移动至厨具的条件:
     * 厨具内的原料不符很配方和女仆身上有对应配方的原料
     */
    default boolean beCookCanMoveTo(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return !beInnerCanCookNow(maidInv, beInv, level, maid, be, recManager) && hasRecIngres(recManager)
                && beCookSExtra(maidInv, beInv, level, maid, be, recManager) && beCookAExtra(maidInv, beInv, level, maid, be, recManager);
    }

    private boolean hasRecIngres(MaidRecipesManager<R> recManager) {
        return !recManager.getRecipesIngredients().isEmpty();
    }

    /**
     * 女仆移动至厨具的条件:
     * 厨具内有原料，但不符合配方
     */
    default boolean beInputsCanMoveTo(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> recManager) {
        return !beInnerCanCookNow(maidInv, beInv, level, maid, be, recManager) && hasInput(level, maid, be, recManager);
    }

    default List<ActionState> getStateATypes() {
        return List.of();
    }

    default List<StaticState> getStateSTypes() {
        return List.of();
    }
}
