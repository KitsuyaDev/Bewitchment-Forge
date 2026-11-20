/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kitsuyadev.bewitchment.common.registry.BWWorldGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.List;

public class AddReferencedLoot extends LootModifier {
	public static final Codec<AddReferencedLoot> CODEC = RecordCodecBuilder.create(instance -> codecStart(instance)
			.and(ResourceLocation.CODEC.listOf().fieldOf("targets").forGetter(modifier -> modifier.targets))
			.and(ResourceLocation.CODEC.fieldOf("loot_table").forGetter(modifier -> modifier.lootTable))
			.apply(instance, AddReferencedLoot::new));

	private final List<ResourceLocation> targets;
	private final ResourceLocation lootTable;

	private AddReferencedLoot(LootItemCondition[] conditions, List<ResourceLocation> targets, ResourceLocation lootTable) {
		super(conditions);
		this.targets = targets;
		this.lootTable = lootTable;
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext context) {
		if (targets.contains(context.getQueriedLootTableId())) {
			context.getLevel().getServer().getLootData().getLootTable(lootTable).getRandomItemsRaw(context, loot::add);
		}
		return loot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return BWWorldGenerators.REFERENCED_LOOT.get();
	}
}
