package com.github.catbert.tlma.task.cook.handler.v2;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.api.task.v2.rule.itemhandler.MaidRecipe;
import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.init.InitItems;
import com.github.catbert.tlma.inventory.container.item.BagType;
import com.github.catbert.tlma.item.ItemCookBag;
import com.github.catbert.tlma.task.cook.handler.CookBagInventory;
import com.github.catbert.tlma.task.cook.handler.ICookInventory;
import com.github.catbert.tlma.task.cook.handler.MaidInventory;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.*;

public class MaidRecipesManager<T extends Recipe<? extends Container>> {
    private final MaidInventory maidInventory;
    private final ICookTask<?, T> task;
    private final boolean single;
    private String lastTaskRule;
    private List<String> recipeIds;
    private int repeatTimes = 0;
    private List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = new ArrayList<>();
    private final List<MaidRecipe<T>> maidRecs = new ArrayList<>();
    private List<T> currentRecs = new ArrayList<>();
    private ICookInventory lastInv;

    public MaidRecipesManager(EntityMaid maid, ICookTask<?, T> task, boolean single) {
        this(maid, task, single, false);
    }

    public MaidRecipesManager(EntityMaid maid, ICookTask<?, T> task, boolean single, boolean createRecIng) {
        this.maidInventory = new MaidInventory(maid);
        this.single = single;
        this.task = task;

        if (createRecIng) {
            this.initTaskData(maid);
            this.createRecipesIngredients(maid);
        }
    }

    public boolean isSingle() {
        return single;
    }

    public MaidInventory getMaidInventory() {
        return maidInventory;
    }

    private List<T> getRecs(EntityMaid maid) {
//        List<T> allRecipesFor = this.maidInventory.getMaid().level.getRecipeManager().getAllRecipesFor((RecipeType) recipeType);
        Level level = this.maidInventory.getMaid().level();

        List<T> allRecipesFor;
        if (CookData.Mode.SELECT.name.equals(this.lastTaskRule)) {
            allRecipesFor = task.getRecipes(level).stream()
                    .filter(r -> recipeIds.contains(r.getId().toString()))
                    .toList();
        } else {
            allRecipesFor = task.getRecipes(level);
        }

        allRecipesFor = new ArrayList<>(allRecipesFor);
        allRecipesFor = filterRecipes(allRecipesFor);
        shuffle(allRecipesFor);
        return allRecipesFor;
    }

    public List<Pair<List<Integer>, List<List<ItemStack>>>> getRecipesIngredients() {
        return recipesIngredients;
    }

    public Pair<List<Integer>, List<List<ItemStack>>> getRecipeIngredient() {
//        TLMAddon.LOGGER.info("MaidRecipesManager.getRecipeIngredient: ");
//        TLMAddon.LOGGER.info(recipesIngredients);
        if (recipesIngredients.isEmpty()) return null;
        int size = recipesIngredients.size();
        Pair<List<Integer>, List<List<ItemStack>>> integerListPair = recipesIngredients.get(0);
        List<Pair<List<Integer>, List<List<ItemStack>>>> pairs = recipesIngredients.subList(1, size);
        recipesIngredients = pairs;
        return integerListPair;
    }

    public void checkAndCreateRecipesIngredients(EntityMaid maid) {
        initTaskData(maid);
        // 缓存的配方原料没了
        if (!recipesIngredients.isEmpty()) return;
        // 是否为上一次的背包以及手上的物品
        boolean lastInv = isLastInv(maid);
//        if (lastInv) return;
        createRecipesIngredients(maid);
    }

    @SuppressWarnings("unchecked")
    private void initTaskData(EntityMaid maid) {
        if (lastTaskRule == null || recipeIds == null) {
            ICookTask<?, T> cookTask = (ICookTask<?, T>) maid.getTask();
            CookData cookData = cookTask.getTaskData(maid);
            this.lastTaskRule = cookData.mode();
            this.recipeIds = cookData.recs();
        }
    }

    // todo
    // 有时候不灵，应该是更新了数据存储的方式导致的，
    // 初始化的时候出问题
    private boolean isLastInv(EntityMaid maid) {

        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        List<ItemStack> lastInvStack = maidInventory.getLastInvStack();
        if (availableInv.getSlots() != lastInvStack.size()) return false;

        for (int i = 0; i < availableInv.getSlots(); i++) {
            ItemStack stackInSlot = availableInv.getStackInSlot(i);
            ItemStack cacheStack = lastInvStack.get(i);
            if (!(stackInSlot.is(cacheStack.getItem()) && stackInSlot.getCount() == cacheStack.getCount())) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getItemStack(Item item) {
        Map<Item, List<ItemStack>> inventoryStack = maidInventory.getInventoryStack();
        List<ItemStack> itemStacks = inventoryStack.get(item);
        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.isEmpty()) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    protected List<T> filterRecipes(List<T> recipes) {
        return recipes;
    }

    public void mapChestIngredient(EntityMaid maid) {
        ItemStackHandler maidInv = maid.getMaidInv();
        ItemStack stackInSlot1 = maidInv.getStackInSlot(4);
        if (!stackInSlot1.is(InitItems.COOK_BAG.get())) return;

        List<BlockPos> ingredientPos = ItemCookBag.getBindModePoses(stackInSlot1, BagType.INGREDIENT.name);
        if (ingredientPos.isEmpty()) return;

        Map<BagType, ItemStackHandler> containers = ItemCookBag.getContainers(stackInSlot1);
        if (!containers.containsKey(BagType.INGREDIENT)) return;
        ItemStackHandler inventory = containers.get(BagType.INGREDIENT);
//        ItemStackHandler inventory = ItemCookBag.getContainer(stackInSlot1);

        ServerLevel level = (ServerLevel) maid.level;

        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        Map<Item, Integer> available = new HashMap<>();
        Map<Item, List<ItemStack>> ingredientAmount = new HashMap<>();
        // 汇集所有箱子的原料
        for (BlockPos ingredientPo : ingredientPos) {
            BlockEntity blockEntity = level.getBlockEntity(ingredientPo);
            if (blockEntity != null) {
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(beInv -> {
                    for (int i = 0; i < beInv.getSlots(); i++) {
                        ItemStack stackInSlot = beInv.getStackInSlot(i);
                        Item item = stackInSlot.getItem();

                        if (stackInSlot.isEmpty()) continue;

                        available.merge(item, stackInSlot.getCount(), Integer::sum);

                        List<ItemStack> itemStacks = ingredientAmount.get(item);
                        if (itemStacks == null) {
                            ingredientAmount.put(item, Lists.newArrayList(stackInSlot));
                        }else {
                            itemStacks.add(stackInSlot);
                        }
                    }
                });
            }
        }
        // 获取配方所需的原料的份量
        for (T t : this.currentRecs) {
            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient(t, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));
            }
        }
        // 转移箱子原料至CookBag
        for (Pair<List<Integer>, List<Item>> listListPair : _make) {
            List<Integer> first = listListPair.getFirst();
            List<Item> second = listListPair.getSecond();

            for (int i = 0; i < first.size(); i++) {
                Integer i1 = first.get(i);
                Item item = second.get(i);

                List<ItemStack> itemStacks = ingredientAmount.get(item);
                for (ItemStack itemStack : itemStacks) {
                    if (itemStack.isEmpty()) continue;
                    int count = itemStack.getCount();
                    // 减去当前物品的数量还是大于0，证明还没满足，就整体移动
                    if (i1 - count > 0) {
                        ItemHandlerHelper.insertItemStacked(inventory, itemStack, false);
                    } else {
                        ItemHandlerHelper.insertItemStacked(inventory, itemStack.split(i1), false);
                        break;
                    }
                    i1 -= count;
                }
            }
        }
        // 更新CookBag的inventory
        ItemCookBag.setContainer(stackInSlot1, containers);
    }

    private void createRecipesIngredients(EntityMaid maid) {
        if (lastTaskRule == null || recipeIds == null) return;

//        recipesIngredients.clear();
//        recipesIngredients = new ArrayList<>();

        this.currentRecs = getRecs(maid);
        // 将CookBag里无用的配方原料放回原料箱子
        tranUnIngre2Chest(maid);
        // 获取原料箱子配方原料并置入CookBag
        mapChestIngredient(maid);
        this.maidInventory.refreshInv();
        createIngres(maid);
        this.currentRecs.clear();

//        LOGGER.info("MaidRecipesMrecipesIngredients = {ImmutableCollections$ListN@44015}  size = 2anager.createRecipesIngredients: " + this.maidInventory.getMaid());
//        LOGGER.info(this.recipesIngredients);
    }

    public void tranOutput2Chest(EntityMaid maid) {
        tranCookBag2Chest(maid, BagType.OUTPUT);
    }

    public void tranUnIngre2Chest(EntityMaid maid) {
        tranCookBag2Chest(maid, BagType.INGREDIENT);
    }

    private static void tranCookBag2Chest(EntityMaid maid, BagType bagType) {
        ItemStackHandler maidInv = maid.getMaidInv();
        ItemStack stackInSlot1 = maidInv.getStackInSlot(4);
        if (!stackInSlot1.is(InitItems.COOK_BAG.get())) return;

        List<BlockPos> ingredientPos = ItemCookBag.getBindModePoses(stackInSlot1, bagType.name);
        if (ingredientPos.isEmpty()) return;

        Map<BagType, ItemStackHandler> containers = ItemCookBag.getContainers(stackInSlot1);
        if (!containers.containsKey(bagType)) return;
        ItemStackHandler itemStackHandler = containers.get(bagType);

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            for (BlockPos ingredientPo : ingredientPos) {
                BlockEntity blockEntity = maid.level.getBlockEntity(ingredientPo);
                if (stack.isEmpty()) break;

                if (blockEntity != null) {
                    blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(beInv -> {
                        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(beInv, stack.copy(), false);
                        stack.shrink(stack.getCount() - leftStack.getCount());
                    });
                }
            }
        }
        ItemCookBag.setContainer(stackInSlot1, containers);
    }



    private void createIngres2(EntityMaid maid) {
        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        Map<Item, Integer> available = new HashMap<>(getIngredientInv(maid).getInventoryItem());

        for (T t : this.currentRecs) {
            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient2(t, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));
            }
        }

        repeat(_make, available, this.repeatTimes);

        this.recipesIngredients = transform(maid, _make, available);
    }

    protected Pair<List<Integer>, List<Item>> getAmountIngredient2(T recipe, Map<Item, Integer> available) {
        List<Ingredient> ingredients = recipe.getIngredients();
        boolean[] canMake = {true};
        boolean[] single = {false};
        List<Item> invIngredient = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();

        extraStartRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

        for (Ingredient ingredient : ingredients) {

            if (ingredient.getItems().length == 0) {
                Item emptyItem = ItemStack.EMPTY.getItem();
                invIngredient.add(emptyItem);
                itemTimes.put(emptyItem, 0);
                continue;
            }

            boolean hasIngredient = false;
            for (Item item : available.keySet()) {
                ItemStack stack = item.getDefaultInstance();
                if (ingredient.test(stack)) {
                    invIngredient.add(item);
                    hasIngredient = true;

                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    break;
                }
            }

            if (!hasIngredient) {
                canMake[0] = false;
                itemTimes.clear();
                invIngredient.clear();
                break;
            }
        }

        extraEndRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

        if (!canMake[0] || invIngredient.stream().anyMatch(item -> available.get(item) <= 0)) {
            return Pair.of(new ArrayList<>(), new ArrayList<>());
        }

        int maxCount = 64;
        if (single[0] || this.single) {
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




    private void createIngres(EntityMaid maid) {
        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        Map<Item, Integer> available = new HashMap<>(getIngredientInv(maid).getInventoryItem());

        for (T t : this.currentRecs) {
            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient(t, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));
            }
        }

        repeat(_make, available, this.repeatTimes);

        this.recipesIngredients = transform(maid, _make, available);
    }

    public ICookInventory getIngredientInv(EntityMaid maid) {
        ItemStackHandler maidInv = maid.getMaidInv();
        ItemStack stackInSlot1 = maidInv.getStackInSlot(4);
        if (!stackInSlot1.is(InitItems.COOK_BAG.get()))  {
            this.setLastInv(this.maidInventory);
        } else {
            this.setLastInv(new CookBagInventory(stackInSlot1));
        }
        return this.getLastInv();
    }

    private void setLastInv(ICookInventory lastInv) {
        this.lastInv = lastInv;
    }

    public ICookInventory getLastInv() {
        return lastInv;
    }

    protected void repeat(List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available, int times) {
        ArrayList<Pair<List<Integer>, List<Item>>> oriPairs = new ArrayList<>(oriList);
        for (int l = 0; l < times; l++) {
            for (Pair<List<Integer>, List<Item>> listListPair : oriPairs) {
                List<Integer> first = listListPair.getFirst();
                List<Item> second = listListPair.getSecond();

                boolean canRepeat = true;
                for (int i = 0; i < second.size(); i++) {
                    Integer availableCount = available.get(second.get(i));
                    if (availableCount < first.get(i)) {
                        canRepeat = false;
                        break;
                    }
                }

                if (canRepeat) {
                    for (int i = 0; i < second.size(); i++) {
                        Item item = second.get(i);
                        available.put(item, available.get(item) - first.get(i));
                    }
                    oriList.add(listListPair);
                }
            }
        }
    }

//    private List<Pair<List<Integer>, List<Item>>> repeat(List<Pair<List<Integer>, List<Item>>> oriList) {
//        Map<Item, Integer> available = new HashMap<>(this.maidInventory.getInventoryItem());
//        List<Pair<List<Integer>, List<Item>>> list = new ArrayList<>(oriList);
//        for (Pair<List<Integer>, List<Item>> listListPair : oriList) {
//            List<Integer> first = listListPair.getFirst();
//            List<Item> second = listListPair.getSecond();
//
//            boolean canRepeat = true;
//            for (int i = 0; i < second.size(); i++) {
//                Integer availableCount = available.get(second.get(i));
//                if (availableCount < first.get(i)) {
//                    canRepeat = false;
//                    break;
//                }
//            }
//
//            if (canRepeat) {
//                for (int i = 0; i < second.size(); i++) {
//                    Item item = second.get(i);
//                    available.put(item, available.get(item) - first.get(i));
//                }
//                list.add(listListPair);
//            }
//        }
//        return list;
//    }

    protected List<Pair<List<Integer>, List<List<ItemStack>>>> transform(EntityMaid maid, List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available) {
        Map<Item, List<ItemStack>> inventoryStack = this.getIngredientInv(maid).getInventoryStack();
        List<Pair<List<Integer>, List<List<ItemStack>>>> list1 = oriList.stream().map(p -> {
            List<List<ItemStack>> list = p.getSecond().stream().map(item -> {
                return inventoryStack.get(item);
//                return inventoryStack.getOrDefault(item, new ArrayList<>());
            }).toList();
            return Pair.of(p.getFirst(), list);
        }).toList();
        return list1;
    }

    protected Pair<List<Integer>, List<Item>> getAmountIngredient(T recipe, Map<Item, Integer> available) {
        List<Ingredient> ingredients = recipe.getIngredients();
        boolean[] canMake = {true};
        boolean[] single = {false};
        List<Item> invIngredient = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();

        extraStartRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

        for (Ingredient ingredient : ingredients) {

            if (ingredient.getItems().length == 0) {
                Item emptyItem = ItemStack.EMPTY.getItem();
                invIngredient.add(emptyItem);
                itemTimes.put(emptyItem, 0);
                continue;
            }

            boolean hasIngredient = false;
            for (Item item : available.keySet()) {
                ItemStack stack = item.getDefaultInstance();
                if (ingredient.test(stack)) {
                    invIngredient.add(item);
                    hasIngredient = true;

                    if (stack.getMaxStackSize() == 1) {
                        single[0] = true;
                        itemTimes.put(item, 1);
                    } else {
                        itemTimes.merge(item, 1, Integer::sum);
                    }

                    break;
                }
            }

            if (!hasIngredient) {
                canMake[0] = false;
                itemTimes.clear();
                invIngredient.clear();
                break;
            }
        }

        extraEndRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

        if (!canMake[0] || invIngredient.stream().anyMatch(item -> available.get(item) <= 0)) {
            return Pair.of(new ArrayList<>(), new ArrayList<>());
        }

        int maxCount = 64;
        if (single[0] || this.single) {
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

    protected boolean extraStartRecipe(T recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        return true;
    }

    protected boolean extraEndRecipe(T recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        return true;
    }

    private void shuffle(List<T> recipes) {
        Collections.shuffle(recipes);
    }

    public int getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public IItemHandlerModifiable getOutputInv(EntityMaid maid) {
        ItemStackHandler maidInv = maid.getMaidInv();
        ItemStack stackInSlot1 = maidInv.getStackInSlot(4);
        if (!stackInSlot1.is(InitItems.COOK_BAG.get())) return maidInv;

        ICookInventory lastIngredientInv1 = this.getLastInv();

        return lastIngredientInv1.getAvailableInv(maid, BagType.OUTPUT);
    }

    public IItemHandlerModifiable getIngreInv(EntityMaid maid) {
        ItemStackHandler maidInv = maid.getMaidInv();
        ItemStack stackInSlot1 = maidInv.getStackInSlot(4);
        if (!stackInSlot1.is(InitItems.COOK_BAG.get())) return maidInv;

        ICookInventory lastIngredientInv1 = this.getLastInv();

        return lastIngredientInv1.getAvailableInv(maid, BagType.INGREDIENT);
    }
}
