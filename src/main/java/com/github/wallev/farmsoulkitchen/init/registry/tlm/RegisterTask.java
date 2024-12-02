package com.github.wallev.farmsoulkitchen.init.registry.tlm;

import com.github.wallev.farmsoulkitchen.config.subconfig.RegisterConfig;
import com.github.wallev.farmsoulkitchen.foundation.utility.Mods;
import com.github.wallev.farmsoulkitchen.task.cook.v1.bakery.TaskDbkCookingPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.bakery.TaskDbkStove;
import com.github.wallev.farmsoulkitchen.task.cook.v1.beachparty.TaskDbpMiniFridge;
import com.github.wallev.farmsoulkitchen.task.cook.v1.beachparty.TaskDbpTikiBar;
import com.github.wallev.farmsoulkitchen.task.cook.v1.bnc.TaskBncKey;
import com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight.TaskDclCookingPan;
import com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight.TaskDclCookingPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.drinkbeer.TaskDbBeerBarrel;
import com.github.wallev.farmsoulkitchen.task.cook.v1.fd.TaskFDCookPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.herbal.TaskDhbCauldron;
import com.github.wallev.farmsoulkitchen.task.cook.v1.herbal.TaskDhbTeaKettle;
import com.github.wallev.farmsoulkitchen.task.cook.v1.mc.TaskFurnace;
import com.github.wallev.farmsoulkitchen.task.cook.v1.md.TaskMDCopperPot;
import com.github.wallev.farmsoulkitchen.task.cook.v1.vinery.TaskFermentationBarrel;
import com.github.wallev.farmsoulkitchen.task.cook.v1.yhc.TaskDryingRack;
import com.github.wallev.farmsoulkitchen.task.cook.v1.yhc.TaskFermentationTank;
import com.github.wallev.farmsoulkitchen.task.cook.v1.yhc.TaskYhcMoka;
import com.github.wallev.farmsoulkitchen.task.cook.v1.yhc.TaskYhcTeaKettle;
import com.github.wallev.farmsoulkitchen.task.farm.TaskBerryFarm;
import com.github.wallev.farmsoulkitchen.task.farm.TaskCompatMelonFarm;
import com.github.wallev.farmsoulkitchen.task.farm.TaskFruitFarm;
import com.github.wallev.farmsoulkitchen.task.farm.TaskSSFarm;
import com.github.wallev.farmsoulkitchen.task.other.TaskFeedAndDrinkOwner;
import com.github.wallev.farmsoulkitchen.task.other.TaskFeedAnimalT;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

public final class RegisterTask {
    private RegisterTask() {
    }

    public static void register(TaskManager manager) {
        if (Mods.MC.isLoaded() && RegisterConfig.COMPAT_MELON_FARM_TASK_ENABLED.get()){
            manager.add(TaskCompatMelonFarm.getInstance());
        }
        if (Mods.MC.isLoaded() && RegisterConfig.BERRY_FARM_TASK_ENABLED.get()) {
            manager.add(TaskBerryFarm.getInstance());
        }
        if (Mods.MC.isLoaded() && RegisterConfig.FRUIT_FARM_TASK_ENABLED.get()) {
            manager.add(TaskFruitFarm.getInstance());
        }
        if (Mods.MC.isLoaded() && RegisterConfig.FEED_ANIMAL_T_TASK_ENABLED.get()) {
            manager.add(TaskFeedAnimalT.getInstance());
        }

        if (Mods.SS.isLoaded() && RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED.get()) {
            manager.add(TaskSSFarm.getInstance());
        }


        if (Mods.TWT.isLoaded() && RegisterConfig.FEED_AND_DRINK_OWNER_TASK_ENABLED.get()) {
            manager.add(TaskFeedAndDrinkOwner.getInstance());
        }


        if (Mods.MC.isLoaded() && RegisterConfig.FURNACE_TASK_ENABLED.get()) {
            manager.add(TaskFurnace.getInstance());
        }


        if (Mods.FD.isLoaded() && RegisterConfig.FD_COOK_POT_TASK_ENABLED.get()) {
            manager.add(TaskFDCookPot.getInstance());
        }
        if (Mods.MD.isLoaded() && RegisterConfig.MD_COOK_POT_TASK_ENABLED.get()) {
            manager.add(TaskMDCopperPot.getInstance());
        }
        if (Mods.BNCD.isLoaded() && RegisterConfig.BNC_KEY_TASK_ENABLED.get()) {
            manager.add(TaskBncKey.getInstance());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_MOKA_TASK_ENABLED.get()) {
            manager.add(TaskYhcMoka.getInstance());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED.get()) {
            manager.add(TaskYhcTeaKettle.getInstance());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED.get()) {
            manager.add(TaskDryingRack.getInstance());
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_FERMENTATION_TANK_TASK_ENABLED.get()) {
            manager.add(TaskFermentationTank.getInstance());
        }


        if (Mods.DB.isLoaded() && RegisterConfig.DB_BEER_TASK_ENABLED.get()) {
            manager.add(TaskDbBeerBarrel.getInstance());
        }


        if (Mods.DBK.isLoaded() && RegisterConfig.DBK_COOKING_POT_TASK_ENABLED.get()) {
            manager.add(TaskDbkCookingPot.getInstance());
        }
        if (Mods.DBK.isLoaded() && RegisterConfig.DBK_STOVE_TASK_ENABLED.get()) {
            manager.add(TaskDbkStove.getInstance());
        }

        if (Mods.DBP.isLoaded() && RegisterConfig.DBP_MINE_FRIDGE_TASK_ENABLED.get()) {
            manager.add(TaskDbpMiniFridge.getInstance());
        }
        if (Mods.DBP.isLoaded() && RegisterConfig.DBP_TIKI_BAR_TASK_ENABLED.get()) {
            manager.add(TaskDbpTikiBar.getInstance());
        }

        if (Mods.DCL.isLoaded() && RegisterConfig.DCL_COOKING_PAN_TASK_ENABLED.get()) {
            manager.add(TaskDclCookingPan.getInstance());
        }
        if (Mods.DCL.isLoaded() && RegisterConfig.DCL_COOKING_POT_TASK_ENABLED.get()) {
            manager.add(TaskDclCookingPot.getInstance());
        }

        if (Mods.DHB.isLoaded() && RegisterConfig.DHB_CAULDRON_TASK_ENABLED.get()) {
            manager.add(TaskDhbCauldron.getInstance());
        }
        if (Mods.DHB.isLoaded() && RegisterConfig.DHB_TEA_KETTLE_TASK_ENABLED.get()) {
            manager.add(TaskDhbTeaKettle.getInstance());
        }

        if (Mods.DV.isLoaded() && RegisterConfig.FERMENTATION_BARREL_TASK_ENABLED.get()) {
            manager.add(TaskFermentationBarrel.getInstance());
        }
    }
}
