package colt.partychat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messenger {
	private List<Player> directChatPlayers = new ArrayList<>();
	private List<Player> socialSpyStaff = new ArrayList<>();
	
	private Util util = new Util();
	private static PartyChat partyChat;
	
	private static Messenger messenger;
	private Messenger() {}

	public static Messenger getMessenger() {
		if (messenger == null)
			messenger = new Messenger();
		return messenger;
	}
	
	public static Messenger getMessenger(PartyChat plugin) {
		partyChat = plugin;
		if (messenger == null)
			messenger = new Messenger();
		return messenger;
	}
	
	public void sendHelp(Player player) {
		sendHelp(player, "create");
		sendHelp(player, "disband");
		sendHelp(player, "invite");
		sendHelp(player, "accept");
		sendHelp(player, "leave");
		sendHelp(player, "kick");
		sendHelp(player, "directChat");
		sendHelp(player, "quickChat");
		if(player.hasPermission("partychat.socialspy")) {
			sendHelp(player, "socialspy");
		}
	}
	
	public void sendHelp(Player player, String cmd) {
		switch(cmd) {
		case "quickChat":
			player.sendMessage(format("&a/pc m <message> &7Quickly send a message to your party"));
			return;
		case "invite":
			player.sendMessage(format("&a/pc invite <player> &7Invite someone to your Party"));
			return;
		case "directChat":
			player.sendMessage(format("&a/pc directchat &7Text sent in normal chat will get sent directly to your Party (enable/disable)"));
			return;
		case "kick":
			player.sendMessage(format("&a/pc kick <player> &7Kick a player from your Party"));
			return;
		case "leave":
			player.sendMessage(format("&a/pc leave &7Leave your current Party"));
			return;
		case "create":
			player.sendMessage(format("&a/pc create &7Create a new Party"));
			return;
		case "disband":
			player.sendMessage(format("&a/pc disband &7Disband your Party"));
			return;
		case "accept":
			player.sendMessage(format("&a/pc accept <player> &7Accept a Party invitation from <player>"));
			return;
		case "socialspy":
			player.sendMessage(format("&c(A) &a/pc socialspy &7Spy on the server's party chats (enable/disable)"));
		}
	}
	
	public List<Player> getDirectChatPlayers() {
		return this.directChatPlayers;
	}
	
	public List<Player> getSocialSpyStaff() {
		return this.socialSpyStaff;
	}
	
	public String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public String partyChatFormat(Player sender, String msg) {
		String configFormat = partyChat.getConfig().getString("party-chat-format");
		configFormat = configFormat.replace("@sender", sender.getName());
		configFormat = configFormat.replace("@args", msg);
		return format(configFormat); 
	}
	
	public void partySocialSpy(Player sender, Party sendingParty, String message) {
		String configFormat = partyChat.getConfig().getString("socialspy-format");
		configFormat = configFormat.replace("*sender*", sender.getName());
		configFormat = configFormat.replace("*args*", message);
		String msg = format(configFormat); 
		for(Player player : this.socialSpyStaff) {
			if(sendingParty == util.getPartyOfPlayer(player)) continue;
			player.sendMessage(msg);
		}
	}

}
