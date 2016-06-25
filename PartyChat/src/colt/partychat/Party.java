package colt.partychat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Party {
	
	private static List<Party> activeParties = new ArrayList<Party>();
	
	private Player partyOwner;
	private List<Player> partyParticipants;
	private List<Player> pendingInvites;

	public Party(Player owner) {
		this.partyOwner = owner;
		activeParties.add(this);
		
		partyParticipants = new ArrayList<>();
		pendingInvites = new ArrayList<>();
	}
	
	public Player getPartyOwner() {
		return this.partyOwner;
	}
	
	public List<Player> getPartyParticipants() {
		return this.partyParticipants;
	}

	// varargs for the sake of things
	public void addPartyParticpants(Player... players) {
		for(Player pl : players) {
			if(this.partyParticipants.contains(pl)) {
				continue;
			}
			this.partyParticipants.add(pl);
		}
	}
	
	public void removePartyParticpants(Player... players) {
		for(Player pl : players) {
			if(this.partyParticipants.contains(pl)) {
				this.partyParticipants.remove(pl);
			}
		}
	}

	public List<Player> getPendingInvites() {
		return this.pendingInvites;
	}
	
	public void addPendingInvites(Player... players) {
		for(Player pl : players) {
			if(this.pendingInvites.contains(pl)) 
				continue;
			this.pendingInvites.add(pl);
		}
	}
	
	public void removePendingInvites(Player... players) {
		for(Player pl : players) {
			if(this.pendingInvites.contains(pl)) {
				this.pendingInvites.remove(pl);
			}
		}
	}
	
	public void disbandParty() {
		this.partyParticipants.clear();
		this.pendingInvites.clear();
		this.partyOwner = null;
		Party.activeParties.remove(this);
	}
	
	public void chat(String message) {
		for(Player player : this.partyParticipants) {
			player.sendMessage(message);
			continue;
		}
		this.partyOwner.sendMessage(message);
	}
	
	public static List<Party> getActiveParties() {
		return activeParties;
	}
}
