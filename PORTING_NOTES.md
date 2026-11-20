# Porting notes

## Source and attribution

The Forge port is based on upstream Bewitchment commit `842cd9777b7bde2a0208099d5184baf88f24f63a`.

The Forge port credits KitsuyaDev in its loader metadata and uses `kitsuyadev.bewitchment` for Java packages and reflective class references. Upstream attribution is bundled in `META-INF/UPSTREAM-CREDITS.txt`, alongside the existing source notices and contributor roster. Java add-ons that import Bewitchment classes need to use the new package. Minecraft registry IDs and saved capability keys still use `bewitchment`.

## Build and mappings

The initial 47.4.28 MDK supplied Gradle 8.8. ForgeGradle defaults to Forge 47.2.0 because its development container interaction signature matches the Pehkui 3.8.2 Forge Mixin. Distributed JARs declare Forge `[47.2.0,48)` for Minecraft 1.20.1. Runtime results by specific version are in [TESTING.md](TESTING.md). The port uses ForgeGradle 6.0.54, official 1.20.1 mappings and Java 17. Development client and dedicated server use separate run directories.

Bewitchment registers its MidnightConfig data from Forge's queued common setup. A clean packaged client exposed a race in MidnightLib's shared configuration list when Bewitchment registered during Forge's parallel mod construction. Contributor horns use the bundled roster and keep a separate model for each player renderer.

## Mixins and access

The source config requires matching injections. Its existing Mixins patch combat, brewing, familiar behaviour, villager conversions, item rules, entity behaviour, rendering, and more. The access widener opens the phantom swoop goal and makes the living-entity animation field mutable. Forge access transformers preserve the original Phantom and animation access, extend sign allowlists, and reuse vanilla boat placement and wood behaviour. Forge replaces the command bootstrap Mixin and provides the living-breath event used for voodoo drowning.

## Save formats

The Fabric source uses CCA for entity state and separate per-world and universal vanilla persistent state. The Forge capability serializes the original component IDs and field names. It reads the CCA compound when an entity lacks saved Forge component data. The port copies eligible components across player clone events and synchronizes tracked data on player and entity lifecycle events.

- Terraform Wood API 7.0.3 writes `terraform:boat` or `terraform:chest_boat` with the `TerraformBoat` variant. The four known Bewitchment variants are mapped to their Forge entity types at NBT load. Entity UUID, position, passengers and chest data follow the normal entity loading path. Other Terraform mods' variants require their own conversion.
- Trinkets 3.7.2 writes `cardinal_components/trinkets:trinkets/<group>/<slot>/Items`. Players receive registered stacks in vacant main-inventory slots. Full inventories, unknown item IDs, and stacks above the registered item limit are kept in `ForgeData/PlayerPersisted/bewitchment:trinkets_migration/Pending` and retried on the next player load. The import flag prevents replay. Transfer preserves item count and NBT. Equip accessories manually in Curios. Trinkets slot modifiers and equipment layout require their own conversion.
- Full Fabric-to-Forge world import still needs a test on a disposable complete world. Keep an independent copy of any world being migrated.

## Compatibility

See [DEPENDENCY_AUDIT.md](DEPENDENCY_AUDIT.md) for the chosen dependency versions and the integrations still to be migrated. The platform migration retains the original `bewitchment` namespace so that authored resources can keep their existing content references.

## Registration, networking and rendering

- Forge deferred registration handles entity types and attributes, block entities, sounds, effects, particles, recipes, menus, command argument types, and custom gameplay registries. The block and item catalogue registers through `RegisterEvent`.
- One versioned SimpleChannel handles the original packet roles. Serverbound messages use distinct codecs and directions. Broom input comes from the controlling rider, transformation requests have a server cooldown, and teleport requests validate the cauldron before using server-owned power and destination data.
- Curios replaces Trinkets for equipment and its multiplayer sync. The Curios render transforms reuse the original Trinkets pose offsets. The copyright and MIT permission notice are packaged in `META-INF/LICENSE-trinkets.txt`.
- Forge client events register layers, renderers, particles, colours, and key bindings. Forge item extensions handle armour and chest item rendering. Chest and sign sprites are included through atlas sources.
- Forge biome modifiers use the original spawn configuration and existing configured and placed features. Global loot modifiers reference the original seed and fortress loot tables. Forge tags alias the original common data where applicable.
