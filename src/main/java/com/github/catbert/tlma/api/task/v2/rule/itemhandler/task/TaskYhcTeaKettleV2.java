package com.github.catbert.tlma.api.task.v2.rule.itemhandler.task;

import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.init.registry.tlm.RegisterData;
import com.github.catbert.tlma.mixin.yhc.KettleBlockAccessor;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class TaskYhcTeaKettleV2 extends ItemHandlerCookTask<KettleBlockEntity, KettleRecipe> {
    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.YHC_TEA_KETTLE;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KettleBlockEntity;
    }

    @Override
    public RecipeType<KettleRecipe> getRecipeType() {
        return YHBlocks.KETTLE_RT.get();
    }

    @Override
    public ItemStackHandler getItemHandler(KettleBlockEntity KettleBlockEntity) {
        return KettleBlockEntity.getInventory();
    }

    @Override
    public int getOutputSlot() {
        return KettleBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSlotSize() {
        return 4;
    }

    @Override
    public int getOutputMealSlot() {
        return KettleBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public int getOutputContainerSlot() {
        return KettleBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getMealNeedContainer(KettleBlockEntity KettleBlockEntity) {
        return KettleBlockEntity.getContainer();
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
    public List<ActionState> getStateATypes() {
        return List.of(ActionState.WATER);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("catbert", "yhc_tea_kettle_v2");
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.FERMENT.asStack();
    }

    @Override
    public boolean beIsSHeated(KettleBlockEntity KettleBlockEntity, EntityMaid maid) {
        return KettleBlockEntity.isHeated();
    }

    @Override
    public boolean beIsAEnoughWater(KettleBlockEntity kettleBlockEntity, EntityMaid maid) {
        return !needWater(kettleBlockEntity) || findMaidHasWaterResource(maid, kettleBlockEntity) > -1;
    }

    public int getWater(KettleBlockEntity kettleBlockEntity) {
        return kettleBlockEntity.getWater();
    }

    public boolean needWater(KettleBlockEntity kettleBlockEntity) {
        return getWater(kettleBlockEntity) <= 200;
    }

    public List<ItemStack> getWaterSourceList(KettleBlockEntity kettleBlockEntity) {
        Block block = kettleBlockEntity.getBlockState().getBlock();

        Lazy<Map<Ingredient, Integer>> waterMAP = ((KettleBlockAccessor) block).getMAP();
        Set<Ingredient> ingredients = waterMAP.get().keySet();
        return ingredients.stream().map(Ingredient::getItems).flatMap(Stream::of).toList();
    }

    public int findMaidHasWaterResource(EntityMaid entityMaid, KettleBlockEntity kettleBlockEntity) {
        List<ItemStack> waterSourceList = getWaterSourceList(kettleBlockEntity);

        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        return ItemsUtil.findStackSlot(availableInv, itemStack -> waterSourceList.stream().anyMatch(ingredient -> ingredient.is(itemStack.getItem())));
    }
}
