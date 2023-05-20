# SimpleNick - Customizable Nick Plugin

![Version](https://img.shields.io/github/v/release/LakyLuc/SimpleNick?style=flat-square)
![License](https://img.shields.io/badge/license-AGPL%20v3-yellow?style=flat-square)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/Ysfntbb8?style=flat-square)](https://modrinth.com/plugin/simplenick)

## Description

SimpleNick is a plugin that allows players to change their display name and skin. It is highly customizable and supports
LuckPerms.

## What changes are made?

- Change your nickname to a randomly generated one
- Change your Skin (Skin are cached in JSON format)
- Change the Chat format
- Display prefixes and suffixes from LuckPerms (optional)
- Change tablist header and footer (optional)

## Features

- Autonick on join
- See who is nicked
- Change nickname/skin from other player
- Change nickname of offline players
- Manage nicknames/skins from console
- Traceable by logs in the console
- Customize messages in the config file
- Reload the configuration without restarting
- Get notified about new updates

## Installation

- Put the plugin in the `plugins` folder of your server
- Start the server
- Configure the plugin in `plugins/SimpleNick/config.yml` (optional)
- Reload the config with `/simplenick reload` (only if you modified the config)
- Finished! You can now use the plugin.

## Commands

| Command                                          | Function                                                                  | Permission                                                                                          |
|--------------------------------------------------|---------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| `/simplenick`                                    | shows the available commands                                              | displays all commands for which the player has permission, or all available commands in the console |
| `/simplenick random`                             | gives you a random nickname and skin                                      | `simplenick.nick`                                                                                   |
| `/simplenick random <player>`                    | gives a player a random nickname and skin                                 | `simplenick.nick.other` or console                                                                  |
| `/simplenick skin` or `/simplenick skin random`  | gives you a random skin from the cache                                    | `simplenick.nick`                                                                                   |
| `/simplenick skin <name>`                        | gives you the skin of a player (who has been on the server before)        | `simplenick.nick`                                                                                   |
| `/simplenick skin <UUID>`                        | gives you the skin of a player (queried, saved and used for random skins) | `simplenick.nick`                                                                                   |
| `/simplenick skin <random\|name\|UUID> <player>` | gives a player the specified skin                                         | `simplenick.nick.other` or console                                                                  |
| `/simplenick reset`                              | reset your nickname and skin                                              | `simplenick.nick`                                                                                   |
| `/simplenick reset <player>`                     | resets the nickname and skin of a player                                  | `simplenick.nick.other` or console                                                                  |
| `/simplenick resetall`                           | resets all nicknames and skins                                            | `simplenick.nick.other` or console                                                                  |
| `/simplenick list`                               | displays all nicked players                                               | `simplenick.list` or console                                                                        |
| `/simplenick reload`                             | reloads the configuration                                                 | `simplenick.reload` or console                                                                      |

Alias of `/simplenick`: `/snick`

## Configuration

| Option                        | Type             | Description                                                                 |
|-------------------------------|------------------|-----------------------------------------------------------------------------|
| `update_channel`              | string           | Update channel release, beta or alpha; permission: `simplenick.update`      |
| `use_luckperms`               | boolean          | Use LuckPerms for prefixes and suffixes                                     |
| `use_teams`                   | boolean          | Use Teams for player ranks                                                  |
| `lp_group_for_nicked_players` | string           | LuckPerms group for nicked players (used for prefix and suffix)             |
| `tablist_header`              | array of strings | Tab-list header                                                             |
| `tablist_footer`              | array of strings | Tab-list footer                                                             |
| `custom_chat_format`          | boolean          | Use custom chat format (cancel AsyncChatEvent and resend formatted message) |
| `disable_collisions`          | boolean          | Sets player collisions to NEVER                                             |
| message_key                   | string           | Used to change messages (including placeholders)                            |
| more_message_keys...          | ...              | ...                                                                         |

You can find the default configuration [here](example_config.yml).

### Note

If you see a warning in the console (`WARN: There are still nicked players online...`), you can ignore it when
restarting/reloading the server. This is only important if you want to disable the plugin manually. The nicknames and
skins cannot be updated when the plugin is disabled. Please use `/simplenick resetall` before disabling the plugin.
Players with wrong names/skins will have to rejoin the server if the plugin is disabled to update.

[![Modrinth](modrinth.png)](https://modrinth.com/plugin/simplenick)