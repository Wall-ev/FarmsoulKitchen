package com.github.catbert.tlma.api.task.v2.rule.itemhandler.v2;

import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public interface CheckCondition {
    interface ItemHandler {
        boolean test(CombinedInvWrapper maidInv, ItemStackHandler beInv, ServerLevel level, EntityMaid maid, BlockEntity be, MaidRecipesManager<? extends Recipe<?>> recManager);
    }

    interface Container {
        boolean test(CombinedInvWrapper maidInv, Container beInv, ServerLevel level, EntityMaid maid, BlockEntity be, MaidRecipesManager<? extends Recipe<?>> recManager);
    }

}