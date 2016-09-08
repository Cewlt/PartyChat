package colt.partychat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import colt.partychat.Messenger;
import colt.partychat.PartyChat;
import colt.partychat.Util;

public class EventPlayerChat implements Listener {
	private Messenger msg = Messenger.getMessenger();
	private Util util = new Util();

	public EventPlayerChat(PartyChat plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void playerChatEvent(AsyncPlayerChatEvent event) {
		if(msg.getDirectChatPlayers().contains(event.getPlayer())) {
			Player sender = event.getPlayer();
			String message = msg.partyChatFormat(sender, event.getMessage());
			util.getPartyOfPlayer(sender).chat(message);
			msg.partySocialSpy(sender, util.getPartyOfPlayer(sender), event.getMessage());
			event.setCancelled(true);
		}
	}

}
