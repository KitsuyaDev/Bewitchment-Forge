/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class LegacyTrinketsInventory {
	private static final String TRINKETS = "trinkets:trinkets";
	private static final String MIGRATION = "bewitchment:trinkets_migration";

	private LegacyTrinketsInventory() {
	}

	public static void load(ServerPlayer player, CompoundTag playerTag) {
		CompoundTag legacy = playerTag.getCompound("cardinal_components");
		CompoundTag persistent = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
		if (!legacy.contains(TRINKETS, Tag.TAG_COMPOUND) && !persistent.contains(MIGRATION, Tag.TAG_COMPOUND)) {
			return;
		}

		CompoundTag migration = persistent.getCompound(MIGRATION);
		ListTag pending = migration.getList("Pending", Tag.TAG_COMPOUND);
		if (!migration.getBoolean("Imported")) {
			collectStacks(legacy.getCompound(TRINKETS), pending);
			migration.putBoolean("Imported", true);
		}

		for (int index = 0; index < pending.size(); ) {
			int slot = player.getInventory().getFreeSlot();
			if (slot == -1) {
				break;
			}
			CompoundTag saved = pending.getCompound(index);
			ResourceLocation itemId = ResourceLocation.tryParse(saved.getString("id"));
			if (itemId != null && ForgeRegistries.ITEMS.containsKey(itemId)) {
				ItemStack stack = ItemStack.of(saved);
				if (!stack.isEmpty() && stack.getCount() <= stack.getMaxStackSize()) {
					player.getInventory().setItem(slot, stack);
					pending.remove(index);
					continue;
				}
			}
			index++;
		}

		migration.put("Pending", pending);
		persistent.put(MIGRATION, migration);
		player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persistent);
	}

	private static void collectStacks(CompoundTag trinkets, ListTag pending) {
		for (String groupName : trinkets.getAllKeys()) {
			CompoundTag group = trinkets.getCompound(groupName);
			for (String slotName : group.getAllKeys()) {
				ListTag items = group.getCompound(slotName).getList("Items", Tag.TAG_COMPOUND);
				for (int index = 0; index < items.size(); index++) {
					CompoundTag stack = items.getCompound(index);
					if (stack.getByte("Count") > 0 && !"minecraft:air".equals(stack.getString("id"))) {
						pending.add(stack.copy());
					}
				}
			}
		}
	}
}
