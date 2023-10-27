
# 📃VelocityWhitelist
A Proxy wide Whitelist for Velocity **with added Geyser/Floodgate support**
Allows all connections from local network.

## Permissions
| Permission          | Purpose                                  |
|---------------------|------------------------------------------|
| `vwhitelist.admin`  | Needed for all `/vwhitelist` commands    |
| `vwhitelist.bypass` | Can bypass whitelist even if not in list |

## Commands
| Command                         | Response                         |
|---------------------------------|----------------------------------|
| `/vwhitelist`                   | Info command                     |
| `/vwhitelist on`                | Turn the whitelist on            |
| `/vwhitelist off`               | Turn the whitelist off           |
| `/vwhitelist add <username>`    | Add a user to the whitelist      |
| `/vwhitelist remove <username>` | Remove a user from the whitelist |
| `/vwhitelist reload`            | Reload the whitelist config      |

## Config
```toml
# Is the whitelist enabled?
enabled = false

# Are Floodgate and Geyser installed?
geyserFloodgateEnabled = false
geyserPrefix = ""

# What is the kick message?
message = "&aWhitelist enabled!"
```