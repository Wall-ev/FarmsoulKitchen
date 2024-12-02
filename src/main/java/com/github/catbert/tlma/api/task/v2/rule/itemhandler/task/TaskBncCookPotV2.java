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
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCBlocks;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

import java.util.List;

public class TaskBncCookPotV2 extends ItemHandlerCookTask<KegBlockEntity, KegRecipe> {
    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FD_COOK_POT;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KegBlockEntity;
    }

    @Override
    public RecipeType<KegRecipe> getRecipeType() {
        return BCRecipeTypes.FERMENTING.get();
    }

    @Override
    public ItemStackHandler getItemHandler(KegBlockEntity KegBlockEntity) {
        return KegBlockEntity.getInventory();
    }

    @Override
    public int getOutputSlot() {
        return KegBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSlotSize() {
        return 5;
    }

    @Override
    public int getOutputMealSlot() {
        return KegBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public int getOutputContainerSlot() {
        return KegBlockEntity.DRINK_DISPLAY_SLOT;
    }

    @Override
    public ItemStack getMealNeedContainer(KegBlockEntity KegBlockEntity) {
        return KegBlockEntity.getContainer();
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.FARMER_DELIGHT;
    }

    @Override
    public List<StaticState> getStateSTypes() {
        return List.of(StaticState.TEMPERA);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("catbert", "bnc_cook_pot_v2");
    }

    @Override
    public ItemStack getIcon() {
        return BCBlocks.KEG.get().asItem().getDefaultInstance();
    }

    @Override
    public boolean beIsSEnoughTempera(KegBlockEntity KegBlockEntity, EntityMaid maid) {
        return !KegBlockEntity.getTemperature().equals("cold");
    }
}
