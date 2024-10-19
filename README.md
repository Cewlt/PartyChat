# PartyChat

A small but feature-rich Party system. Users can create a "Party" and invite others to it, allowing for private chats between each other.

### Getting started

### Commands
![Screenshot of commands](https://i.imgur.com/OPnKLQF.png)

### Permissions:
```
partychat.reload (reload the config - /pc reload)

partychat.socialspy (enable/disable socialspy - /pc socialspy)
```

### Configuration
```
# party-chat-format - the format to use for PartyChat messages.
# socialspy-format - the format to use for Party SocialSpy reports.
# @sender - the player sending/involved with the command
# @args - the arguments involved with the command, such as the message

party-chat-format: "&b&l[Party] &a(@sender&a): @args"
socialspy-format: "&e&l[Party-Spy] &a(@sender&a): @args"
```
