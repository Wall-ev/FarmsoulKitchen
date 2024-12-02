package com.github.catbert.tlma.api.task.v2.rule.itemhandler.task;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.init.registry.tlm.RegisterData;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;
import umpaz.farmersrespite.common.crafting.KettlePouringRecipe;
import umpaz.farmersrespite.common.crafting.KettleRecipe;
import umpaz.farmersrespite.common.registry.FRBlocks;
import umpaz.farmersrespite.common.registry.FRRecipeTypes;

import java.util.List;
import java.util.Optional;

public class TaskFrKettleV2 extends ItemHandlerCookTask<KettleBlockEntity, KettleRecipe>{
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "fr_kettle");

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FR_KETTLE;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KettleBlockEntity;
    }

    @Override
    public RecipeType<KettleRecipe> getRecipeType() {
        return FRRecipeTypes.BREWING.get();
    }

    @Override
    public ItemStackHandler getItemHandler(KettleBlockEntity kettleBlockEntity) {
        return kettleBlockEntity.getInventory();
    }

    @Override
    public int getOutputSlot() {
        return KettleBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getOutputMealSlot() {
        return KettleBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public int getOutputContainerSlot() {
        return KettleBlockEntity.DRINK_DISPLAY_SLOT;
    }

    @Override
    public ItemStack getOutputContainerStack(KettleBlockEntity kettleBlockEntity) {
        Optional<KettlePouringRecipe> pouringRecipe = kettleBlockEntity.getPouringRecipe(getOutputMealStack(kettleBlockEntity).getItem(), kettleBlockEntity.getFluidTank().getFluid());
        return pouringRecipe.isPresent() ? pouringRecipe.get().getContainer() : ItemStack.EMPTY;
    }

    @Override
    public int getInputSlotSize() {
        return 2;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.FARMER_DELIGHT;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return FRBlocks.KETTLE.get().asItem().getDefaultInstance();
    }

    @Override
    public List<StaticState> getStateSTypes() {
        return List.of(StaticState.HEATED);
    }

    @Override
    public boolean beIsSHeated(KettleBlockEntity kettleBlockEntity, EntityMaid maid) {
        return kettleBlockEntity.isHeated();
    }
}
