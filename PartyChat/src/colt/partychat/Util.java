package colt.partychat;

import org.bukkit.entity.Player;

public class Util {
	
	private PartyChat partyChat;
	
	public Util() {}
	public Util(PartyChat plugin) {
		this.partyChat = plugin;
	}
	
	public Party getPartyOfPlayer(Player player) {
		for(Party party : Party.getActiveParties()) {
			if(party.getPartyParticipants().contains(player) || party.getPartyOwner() == player) {
				return party;
			}
		}
		return null;
	}
	
	public boolean isInParty(Player player) {
		for(Party party : Party.getActiveParties()) {
			if(party.getPartyParticipants().contains(player) || party.getPartyOwner() == player) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPartyLeader(Player player) {
		if(!isInParty(player)) return false;
		for(Party party : Party.getActiveParties()) {
			if(party.getPartyOwner() == player) {
				return true;
			}
		}
		return false;
	}
	
	public void reloadConfig() {
		partyChat.reloadConfig();
		partyChat.saveConfig();
	}
}