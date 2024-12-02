package com.github.catbert.tlma.init.registry.tlm;

import com.github.catbert.tlma.api.task.v2.rule.itemhandler.task.TaskFrKettleV2;
import com.github.catbert.tlma.config.subconfig.RegisterConfig;
import com.github.catbert.tlma.entity.data.inner.task.BerryData;
import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.entity.data.inner.task.FruitData;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.task.cook.v1.bakery.TaskDbkCookingPot;
import com.github.catbert.tlma.task.cook.v1.bakery.TaskDbkStove;
import com.github.catbert.tlma.task.cook.v1.beachparty.TaskDbpMiniFridge;
import com.github.catbert.tlma.task.cook.v1.beachparty.TaskDbpTikiBar;
import com.github.catbert.tlma.task.cook.v1.bnc.TaskBncKey;
import com.github.catbert.tlma.task.cook.v1.candlelight.TaskDclCookingPan;
import com.github.catbert.tlma.task.cook.v1.candlelight.TaskDclCookingPot;
import com.github.catbert.tlma.task.cook.v1.drinkbeer.TaskDbBeerBarrel;
import com.github.catbert.tlma.task.cook.v1.fd.TaskFDCookPot;
import com.github.catbert.tlma.task.cook.v1.herbal.TaskDhbCauldron;
import com.github.catbert.tlma.task.cook.v1.herbal.TaskDhbTeaKettle;
import com.github.catbert.tlma.task.cook.v1.mc.TaskFurnace;
import com.github.catbert.tlma.task.cook.v1.md.TaskMDCopperPot;
import com.github.catbert.tlma.task.cook.v1.vinery.TaskFermentationBarrel;
import com.github.catbert.tlma.task.cook.v1.yhc.TaskDryingRack;
import com.github.catbert.tlma.task.cook.v1.yhc.TaskYhcMoka;
import com.github.catbert.tlma.task.cook.v1.yhc.TaskYhcTeaKettle;
import com.github.catbert.tlma.task.farm.TaskBerryFarm;
import com.github.catbert.tlma.task.farm.TaskFruitFarm;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;

public final class RegisterData {
    private RegisterData() {}
    public static TaskDataKey<BerryData> BERRY_FARM;
    public static TaskDataKey<FruitData> FRUIT_FARM;

    public static TaskDataKey<CookData> MC_FURNACE;
    // farmer's delight && addon
    public static TaskDataKey<CookData> FD_COOK_POT;
    public static TaskDataKey<CookData> MD_COPPER_POT;
    public static TaskDataKey<CookData> BNC_KEY;
    public static TaskDataKey<CookData> YHC_MOKA;
    public static TaskDataKey<CookData> YHC_TEA_KETTLE;
    public static TaskDataKey<CookData> YHC_DRYING_RACK;
    public static TaskDataKey<CookData> FR_KETTLE;

    public static TaskDataKey<CookData> DB_BEER;

    public static TaskDataKey<CookData> DBK_COOKING_POT;
    public static TaskDataKey<CookData> DBK_STOVE;
    public static TaskDataKey<CookData> DBP_MINE_FRIDGE;
    public static TaskDataKey<CookData> DBP_TIKI_BAR;
    public static TaskDataKey<CookData> DCL_COOKING_PAN;
    public static TaskDataKey<CookData> DCL_COOKING_POT;
    public static TaskDataKey<CookData> DHB_CAULDRON;
    public static TaskDataKey<CookData> DHB_TEA_KETTLE;
    public static TaskDataKey<CookData> FERMENTATION_BARREL;

    public static void register(TaskDataRegister register) {
        if (Mods.MC.isLoaded() && RegisterConfig.BERRY_FARM_TASK_ENABLED.get()) {
            BERRY_FARM = register.register(TaskBerryFarm.UID, BerryData.CODEC);
        }
        if (Mods.MC.isLoaded() && RegisterConfig.FRUIT_FARM_TASK_ENABLED.get()) {
            FRUIT_FARM = register.register(TaskFruitFarm.UID, FruitData.CODEC);
        }


        if (Mods.MC.isLoaded() && RegisterConfig.FURNACE_TASK_ENABLED.get()) {
            MC_FURNACE = register.register(TaskFurnace.UID, CookData.CODEC);
        }


        if (Mods.FD.isLoaded() && RegisterConfig.FD_COOK_POT_TASK_ENABLED.get()) {
            FD_COOK_POT = register.register(TaskFDCookPot.UID, CookData.CODEC);
        }
        if (Mods.MD.isLoaded() && RegisterConfig.MD_COOK_POT_TASK_ENABLED.get()) {
            MD_COPPER_POT = register.register(TaskMDCopperPot.UID, CookData.CODEC);
        }
        if (Mods.BNCD.isLoaded() && RegisterConfig.BNC_KEY_TASK_ENABLED.get()) {
            BNC_KEY = register.register(TaskBncKey.UID, CookData.CODEC);
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_MOKA_TASK_ENABLED.get()) {
            YHC_MOKA = register.register(TaskYhcMoka.UID, CookData.CODEC);
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_TEA_KETTLE_TASK_ENABLED.get()) {
            YHC_TEA_KETTLE = register.register(TaskYhcTeaKettle.UID, CookData.CODEC);
        }
        if (Mods.YHCD.isLoaded() && RegisterConfig.YHC_DRYING_RACK_TASK_ENABLED.get()) {
            YHC_DRYING_RACK = register.register(TaskDryingRack.UID, CookData.CODEC);
        }
        FR_KETTLE = register.register(TaskFrKettleV2.UID, CookData.CODEC);


        if (Mods.DB.isLoaded() && RegisterConfig.DB_BEER_TASK_ENABLED.get()) {
            DB_BEER = register.register(TaskDbBeerBarrel.UID, CookData.CODEC);
        }


        if (Mods.DBK.isLoaded() && RegisterConfig.DBK_COOKING_POT_TASK_ENABLED.get()) {
            DBK_COOKING_POT = register.register(TaskDbkCookingPot.UID, CookData.CODEC);
        }
        if (Mods.DBK.isLoaded() && RegisterConfig.DBK_STOVE_TASK_ENABLED.get()) {
            DBK_STOVE = register.register(TaskDbkStove.UID, CookData.CODEC);
        }

        if (Mods.DBP.isLoaded() && RegisterConfig.DBP_MINE_FRIDGE_TASK_ENABLED.get()) {
            DBP_MINE_FRIDGE = register.register(TaskDbpMiniFridge.UID, CookData.CODEC);
        }
        if (Mods.DBP.isLoaded() && RegisterConfig.DBP_TIKI_BAR_TASK_ENABLED.get()) {
            DBP_TIKI_BAR = register.register(TaskDbpTikiBar.UID, CookData.CODEC);
        }

        if (Mods.DCL.isLoaded() && RegisterConfig.DCL_COOKING_PAN_TASK_ENABLED.get()) {
            DCL_COOKING_PAN = register.register(TaskDclCookingPan.UID, CookData.CODEC);
        }
        if (Mods.DCL.isLoaded() && RegisterConfig.DCL_COOKING_POT_TASK_ENABLED.get()) {
            DCL_COOKING_POT = register.register(TaskDclCookingPot.UID, CookData.CODEC);
        }

        if (Mods.DHB.isLoaded() && RegisterConfig.DHB_CAULDRON_TASK_ENABLED.get()) {
            DHB_CAULDRON = register.register(TaskDhbCauldron.UID, CookData.CODEC);
        }
        if (Mods.DHB.isLoaded() && RegisterConfig.DHB_TEA_KETTLE_TASK_ENABLED.get()) {
            DHB_TEA_KETTLE = register.register(TaskDhbTeaKettle.UID, CookData.CODEC);
        }

        if (Mods.DV.isLoaded() && RegisterConfig.FERMENTATION_BARREL_TASK_ENABLED.get()) {
            FERMENTATION_BARREL = register.register(TaskFermentationBarrel.UID, CookData.CODEC);
        }
    }
}