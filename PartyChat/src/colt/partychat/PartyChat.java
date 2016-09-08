package colt.partychat;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import colt.partychat.commands.PartyCommand;
import colt.partychat.listeners.EventPlayerChat;

public class PartyChat extends JavaPlugin implements Listener {
	private Messenger msg;
	private PartyChat partyChat;
	
	@Override
	public void onEnable() {
		getCommand("party").setExecutor(new PartyCommand());
		getCommand("pc").setExecutor(new PartyCommand());
		uglyOnEnable();
	}
	
	@Override
	public void onDisable() {
		msg.getDirectChatPlayers().clear();
		msg.getSocialSpyStaff().clear();
	}
	
	public void uglyOnEnable() {
		partyChat = this;
		saveDefaultConfig();
		msg = Messenger.getMessenger(partyChat);
		new EventPlayerChat(this);
		new Util(partyChat);
	}
}
