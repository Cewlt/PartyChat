package colt.partychat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import colt.partychat.Messenger;
import colt.partychat.Party;
import colt.partychat.PartyChat;
import colt.partychat.Util;

public class EventPlayerQuit implements Listener {
	private Messenger msg = Messenger.getMessenger();
	private Util util = new Util();

	public EventPlayerQuit(PartyChat plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(util.isInParty(player)) {
			Party party = util.getPartyOfPlayer(player);
			if(util.isPartyLeader(player)) {
				party.chat(msg.format("&c&lParty disbanded, leader left the game!"));
				party.disbandParty();
				return;
			}
			party.removePartyParticpants(player);
		}
		if(msg.getDirectChatPlayers().contains(player)) {
			msg.getDirectChatPlayers().remove(player);
		}
		for(Party party : Party.getActiveParties()) {
			if(party.getPendingInvites().contains(player)) {
				party.getPendingInvites().remove(player);
				continue;
			}
		}
		if(msg.getSocialSpyStaff().contains(player)) {
			msg.getSocialSpyStaff().remove(player);
		}
	}
}
