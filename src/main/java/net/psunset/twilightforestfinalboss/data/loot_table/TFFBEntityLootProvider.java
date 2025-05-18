package net.psunset.twilightforestfinalboss.data.loot_table;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;
import twilightforest.init.TFItems;
import twilightforest.loot.LootingEnchantNumberProvider;
import twilightforest.loot.MultiplayerBasedAdditionLootFunction;
import twilightforest.loot.MultiplayerBasedNumberProvider;

import java.util.stream.Stream;

public class TFFBEntityLootProvider extends EntityLootSubProvider {
    protected TFFBEntityLootProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        add(TFFBEntities.CASTLE_KEEPER.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("naga_scales")
                        .setRolls(UniformGenerator.between(1.0F, 2.0F))
                        .add(LootItem.lootTableItem(TFItems.NAGA_SCALE.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 11.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(2.0F, 4.0F)))))
                .withPool(LootPool.lootPool()
                        .name("lich_scepters")
                        .setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(-1.0F, 1.0F), UniformGenerator.between(1.0F, 2.0F)))
                        .add(LootItem.lootTableItem(TFItems.TWILIGHT_SCEPTER.get()))
                        .add(LootItem.lootTableItem(TFItems.LIFEDRAIN_SCEPTER.get()))
                        .add(LootItem.lootTableItem(TFItems.ZOMBIE_SCEPTER.get()))
                        .add(LootItem.lootTableItem(TFItems.FORTIFICATION_SCEPTER.get())))
                .withPool(LootPool.lootPool()
                        .name("lich_pearls")
                        .setRolls(UniformGenerator.between(0.0F, 2.0F))
                        .add(LootItem.lootTableItem(Items.ENDER_PEARL)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(0.0F, 2.0F)))))
                .withPool(LootPool.lootPool()
                        .name("lich_crown")
                        .setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(-1.0F, 1.0F), ConstantValue.exactly(1.0F)))
                        .add(LootItem.lootTableItem(TFItems.CROWN_SPLINTER.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F)))))
                .withPool(LootPool.lootPool()
                        .name("minoshroom_stroganoff")
                        .setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(-1.0F, 1.0F), UniformGenerator.between(1.0F, 2.0F)))
                        .add(LootItem.lootTableItem(TFItems.MEEF_STROGANOFF.get())
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
                .withPool(LootPool.lootPool()
                        .name("minoshroom_axe")
                        .setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(-1.0F, 1.0F), UniformGenerator.between(0.0F, 1.0F)))
                        .add(LootItem.lootTableItem(TFItems.DIAMOND_MINOTAUR_AXE.get())
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(-2.0F, 1.0F)))))
                .withPool(LootPool.lootPool()
                        .name("hydra_chop")
                        .setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(-1.0F, 1.0F), UniformGenerator.between(1.0F, 2.0F)))
                        .add(LootItem.lootTableItem(TFItems.HYDRA_CHOP.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 35.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(5.0F, 10.0F)))))
                .withPool(LootPool.lootPool()
                        .name("hydra_blood")
                        .setRolls(UniformGenerator.between(1.0F, 2.0F))
                        .add(LootItem.lootTableItem(TFItems.FIERY_BLOOD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(7.0F, 10.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 2.0F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(1.0F, 3.0F)))))
                .withPool(LootPool.lootPool()
                        .name("knight_phantom_weapon")
                        .setRolls(new BinomialDistributionGenerator(LootingEnchantNumberProvider.applyLootingLevelTo(this.registries, MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(0.0F, 0.5F), ConstantValue.exactly(1.0F))), ConstantValue.exactly(0.4F)))
                        .add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(20))))
                        .add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_PICKAXE).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(20))))
                        .add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_AXE).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(20)))))
                .withPool(LootPool.lootPool()
                        .setRolls(new BinomialDistributionGenerator(LootingEnchantNumberProvider.applyLootingLevelTo(this.registries, MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(0.0F, 0.5F), ConstantValue.exactly(1.0F))), ConstantValue.exactly(0.4F)))
                        .name("knight_phantom_rare_armor")
                        .add(LootItem.lootTableItem(TFItems.PHANTOM_HELMET).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30))))
                        .add(LootItem.lootTableItem(TFItems.PHANTOM_CHESTPLATE).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30))))).withPool(LootPool.lootPool())
                .withPool(LootPool.lootPool()
                        .name("ur_ghast_carminite")
                        .setRolls(UniformGenerator.between(1.0F, 2.0F))
                        .add(LootItem.lootTableItem(TFItems.CARMINITE.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(1.0F, 3.0F)))))
                .withPool(LootPool.lootPool()
                        .name("ur_ghast_tears")
                        .setRolls(UniformGenerator.between(1.0F, 2.0F))
                        .add(LootItem.lootTableItem(TFItems.FIERY_TEARS.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 12.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(1.0F, 2.0F)))))
                .withPool(LootPool.lootPool()
                        .name("alpha_yeti_fur")
                        .setRolls(UniformGenerator.between(1.0F, 2.0F))
                        .add(LootItem.lootTableItem(TFItems.ALPHA_YETI_FUR.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 10.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(1.0F, 3.0F)))))
                .withPool(LootPool.lootPool()
                        .name("alpha_yeti_bombs")
                        .setRolls(UniformGenerator.between(0.0F, 2.0F))
                        .add(LootItem.lootTableItem(TFItems.ICE_BOMB.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 10.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(0.0F, 2.0F)))))
                .withPool(LootPool.lootPool()
                        .name("snow_queen_bows")
                        .setRolls(new BinomialDistributionGenerator(LootingEnchantNumberProvider.applyLootingLevelTo(this.registries, MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(0.0F, 0.5F), ConstantValue.exactly(1.0F))), ConstantValue.exactly(0.8F)))
                        .add(LootItem.lootTableItem(TFItems.TRIPLE_BOW.get()))
                        .add(LootItem.lootTableItem(TFItems.SEEKER_BOW.get())))
                .withPool(LootPool.lootPool()
                        .name("castle_keeper_cube_talisman")
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(TFItems.CUBE_TALISMAN.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 0.4F)))
                                .apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(0.0F, 0.8F))))));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return TFFBEntities.REGISTRY.getEntries().stream().map(DeferredHolder::value);
    }
}
