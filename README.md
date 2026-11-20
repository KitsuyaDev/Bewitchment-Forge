# Bewitchment Forge 1.20.1

Bewitchment adds witchcraft to Minecraft through rituals, brews, familiars, curses, and supernatural transformations. This port targets **Minecraft 1.20.1** on **Forge**. Use **Java 17** to build and run it.

## Installation

Place the Bewitchment JAR in the `mods` folder on the client and server. Packaged builds have been tested on Forge 47.4.20. The Gradle development environment uses Forge 47.2.0.

For standalone Forge, also install these Minecraft 1.20.1 dependencies.

| Dependency | Version |
| --- | --- |
| [Curios API](https://modrinth.com/mod/curios/version/IPQlZkz1) | 5.14.1+1.20.1 |
| [Patchouli](https://modrinth.com/mod/patchouli/version/JMtc0mTS) | 1.20.1-84-FORGE or 1.20.1-85-FORGE |
| [Pehkui](https://modrinth.com/mod/pehkui/version/SQpqSgAE) | 3.8.2 for Forge |
| [MidnightLib](https://modrinth.com/mod/midnightlib/version/UhRAdyzr) | 1.4.2 for Forge |

## Build from source

Set `JAVA_HOME` to a JDK 17 installation. Run `./gradlew clean build` on Linux or macOS or `.\gradlew.bat clean build` in Windows PowerShell. The installable JAR is `build/libs/bewitchment-1.20-10-rc.1.jar`. See the [testing instructions](TESTING.md) for GameTests and the validation companion.

## Release status

Version `1.20-10-rc.1` is a release candidate. Some optional integrations and world-generation cases still need in-game validation. Fabric save migration covers specific legacy data formats. A complete Fabric world migration still needs an end-to-end test. See the [migration notes](PORTING_NOTES.md#save-formats).

## Credits and licence

Forge port maintained by [KitsuyaDev](https://github.com/KitsuyaDev). Original Bewitchment by MoriyaShiine and cybercat5555, with contributions from Aelpecyem, Samaritans, and other upstream contributors. [Upstream contributor list](https://github.com/MoriyaShiine/bewitchment/graphs/contributors)

This repository begins with the Forge release candidate source snapshot. The original project history is available in the [upstream repository](https://github.com/MoriyaShiine/bewitchment).

Bewitchment is All Rights Reserved. The source notices and [upstream credits](src/main/resources/META-INF/UPSTREAM-CREDITS.txt) accompany the port. Adapted Trinkets code carries its [MIT notice](src/main/resources/META-INF/LICENSE-trinkets.txt).

## Release candidate changes

- Ported registration, world generation, networking and player state to Forge 1.20.1.
- Moved equipment to Curios and kept support for Patchouli, Pehkui and MidnightLib.
- Added migration for Bewitchment Terraform boats and recovery of legacy Trinkets item stacks.
- Fixed brewing mixin failures on Forge 47.4.20 and a player-death crash while waiting to respawn.
