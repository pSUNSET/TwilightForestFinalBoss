package net.psunset.twilightforestfinalboss.loot.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.psunset.twilightforestfinalboss.init.TFFBLootPoolEntries;

import java.util.List;
import java.util.function.Consumer;

public class OptionalLootItem extends LootPoolSingletonContainer {
    public static final MapCodec<OptionalLootItem> CODEC = RecordCodecBuilder.mapCodec((p_344670_) -> p_344670_.group(ResourceLocation.CODEC.fieldOf("name").forGetter((p_298016_) -> p_298016_.item)).and(singletonFields(p_344670_)).apply(p_344670_, OptionalLootItem::new));
    private final ResourceLocation item;

    private OptionalLootItem(ResourceLocation item, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.item = item;
    }

    @Override
    public LootPoolEntryType getType() {
        return TFFBLootPoolEntries.OPTIONAL_ITEM.get();
    }

    @Override
    public void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
        BuiltInRegistries.ITEM.getHolder(ResourceKey.create(Registries.ITEM, this.item)).ifPresent(holder -> {
            ItemStack itemStack = new ItemStack(holder.value());
            stackConsumer.accept(itemStack);
        });
    }

    public static LootPoolSingletonContainer.Builder<?> optionalItem(ResourceLocation item) {
        return simpleBuilder((p_298018_, p_298019_, p_298020_, p_298021_) -> new OptionalLootItem(item, p_298018_, p_298019_, p_298020_, p_298021_));
    }
}
