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
	
	/*
	 * The primary function of this class is to 
	 * handle people who quit while still existing in Lists.
	 */
	
	private Messenger msg = Messenger.getMessenger();
	private Util util = new Util();

	/* Register our events, keep our main class looking pretty. */
	public EventPlayerQuit(PartyChat plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/* The goal here is to keep it as efficient as possible. 
	 * We check if the quiting player exists in any parties
	 * either as a member or leader 
	 * along with checks for DirectChat and SocialSpy. */
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
