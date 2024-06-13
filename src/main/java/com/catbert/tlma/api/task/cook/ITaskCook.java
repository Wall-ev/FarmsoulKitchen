package com.catbert.tlma.api.task.cook;

import com.catbert.tlma.api.ILittleMaidTask;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public interface ITaskCook<T extends Recipe<? extends Container>, C extends BlockEntity> extends ILittleMaidTask {

    @Nullable
    @Override
    default SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FURNACE.get(), 0.5f);
    }

    default double getCloseEnoughDist() {
        return 2.1;
    }

    boolean isCookBE(BlockEntity blockEntity);

    RecipeType<T> getRecipeType();

    boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, C blockEntity, MaidRecipesManager<T> maidRecipesManager);

    void processCookMake(ServerLevel serverLevel, EntityMaid entityMaid, C blockEntity, MaidRecipesManager<T> maidRecipesManager);

}