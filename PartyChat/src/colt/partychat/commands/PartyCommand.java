package colt.partychat.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import colt.partychat.Messenger;
import colt.partychat.Party;
import colt.partychat.Util;

public class PartyCommand implements CommandExecutor {
	
	/*
	 * This class contains our commands and their implementations.
	 * Due to the large nature of this project, a odd and somewhat messy
	 * approach is used to handle each argument.
	 * 
	 * Despite this, the code is fluent, manageable and operates very well.
	 */

	private Messenger msg = Messenger.getMessenger();
	private Util util = new Util();

	/* This manages our sub commands and args. 
	 * Not the most pretty system, but works wells.*/
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("party")) {
			if (sender instanceof Player) {
				Player player = (Player)sender;
				msg.sendHelp(player);
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("pc")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length == 0) {
					msg.sendHelp(player);
					return true;
				}
				switch(args[0].toLowerCase()) {
				case "create":
					return create(player, args);
				case "invite":
					return invite(player, args);
				case "accept":
					return accept(player, args);
				case "leave":
					return leave(player, args);
				case "kick":
					return kick(player, args);
				case "disband":
					return disband(player, args);
				case "m":
					return quickChat(player, args);
				case "directchat":
					return directChat(player, args);
				case "socialspy":
					return socialspy(player, args);
				case "reload":
					util.reloadConfig();
					player.sendMessage(msg.format("&aConfiguration file reloaded."));
					return true;
				default:
					msg.sendHelp(player);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean socialspy(Player sender, String[] args) {
		if(!sender.hasPermission("partychat.socialspy")) {
			sender.sendMessage(msg.format("&cYou lack the required permission node!"));
			return true;
		}
		if(args.length != 1) {
			msg.sendHelp(sender, "leave");
			return true;
		}
		if(msg.getSocialSpyStaff().contains(sender)) {
			sender.sendMessage(msg.format("&c[Party] SocialSpy disabled."));
			msg.getSocialSpyStaff().remove(sender);
			return true;
		} else {
			sender.sendMessage(msg.format("&a[Party] SocialSpy enabled."));
			msg.getSocialSpyStaff().add(sender);
			return true;
		}
	}
	
	public boolean leave(Player sender, String[] args) {
		if(args.length != 1) {
			msg.sendHelp(sender, "leave");
			return true;
		}
		if(!util.isInParty(sender)) {
			sender.sendMessage(msg.format("&cYou're not in a Party!"));
			return true;
		}
		if(util.isPartyLeader(sender)) {
			sender.sendMessage(msg.format("&cYou're the Party leader, use disband instead."));
			return true;
		}
		Party party = util.getPartyOfPlayer(sender);
		party.removePartyParticpants(sender);
		party.chat(msg.format("&c" + sender.getName() + " &aleft the Party"));
		if(msg.getDirectChatPlayers().contains(sender)) {
			msg.getDirectChatPlayers().remove(sender);
		}
		return true;
	}
	
	public boolean directChat(Player sender, String[] args) {
		if(args.length != 1) {
			msg.sendHelp(sender, "directChat");
			return true;
		}
		if(!util.isInParty(sender)) {
			sender.sendMessage(msg.format("&cYou're not in a Party!"));
			return true;
		}
		if(msg.getDirectChatPlayers().contains(sender)) {
			msg.getDirectChatPlayers().remove(sender);
			sender.sendMessage(msg.format("&a[Party] You disabled Direct Party Chat."));
			return true;
		} else {
			msg.getDirectChatPlayers().add(sender);
			sender.sendMessage(msg.format("&a[Party] You enabled Direct Party Chat."));
			return true;
		}
	}
	
	public boolean accept(Player sender, String[] args) {
		if(args.length != 2) {
			msg.sendHelp(sender, "accept");
			return true;
		}
		if(util.isInParty(sender)) {
			sender.sendMessage(msg.format("&cYou're already in a Party!"));
			return true;
		}
		if(Bukkit.getPlayer(args[1]) == null) {
			sender.sendMessage(msg.format("&cThat player isn't online!"));
			return true;
		}
		Player partyOwner = Bukkit.getPlayer(args[1]);
		Party party = util.getPartyOfPlayer(partyOwner);
		if(!util.isPartyLeader(partyOwner)) {
			sender.sendMessage(msg.format("&cThat player doesn't own a party!"));
			return true;
		}
		if(!party.getPendingInvites().contains(sender)) {
			sender.sendMessage(msg.format("&cYou were not invited to that player's Party."));
			return true;
		}
		party.addPartyParticpants(sender);
		party.removePendingInvites(sender);
		sender.sendMessage(msg.format("&aYou joined the Party of &c" + partyOwner.getName()));
		party.chat(msg.format("&c" + sender.getName() + " &ajoined the Party!"));
		return true;
	}
	
	public boolean invite(Player sender, String[] args) {
		if(args.length != 2) {
			msg.sendHelp(sender, "invite");
			return true;
		}
		if(!util.isInParty(sender)) {
			sender.sendMessage(msg.format("&cYou're not in a Party!"));
			return true;
		}
		if(!util.isPartyLeader(sender)) {
			sender.sendMessage(msg.format("&cOnly the Party leader may invite others."));
			return true;
		}
		if(Bukkit.getPlayer(args[1]) == null) {
			sender.sendMessage(msg.format("&cThat player isn't online!"));
			return true;
		}
		Party party = util.getPartyOfPlayer(sender);
		Player invitee = Bukkit.getPlayer(args[1]);
		if(util.isInParty(invitee)) {
			sender.sendMessage(msg.format("&cThat player is already in a Party."));
			return true;
		}
		party.addPendingInvites(invitee);
		invitee.sendMessage(msg.format("&aYou have been invited to join the party of " + sender.getName() +
				"\n" + "&aUse &c/pc accept " + sender.getName() + " &ato accept."));
		sender.sendMessage(msg.format("&c" + invitee.getName() + " &ahas been invited to your Party."));
		return true;
	}
	
	public boolean kick(Player sender, String[] args) {
		if(args.length != 2) {
			msg.sendHelp(sender, "kick");
			return true;
		}
		if(!util.isInParty(sender)) {
			sender.sendMessage(msg.format("&cYou're not in a Party!"));
			return true;
		}
		if(!util.isPartyLeader(sender)) {
			sender.sendMessage(msg.format("&cYou must be the Party Leader to kick!"));
			return true;
		}
		if(Bukkit.getPlayer(args[1]) == null) {
			sender.sendMessage(msg.format("&cThat player isn't online!"));
			return true;
		}
		Party party = util.getPartyOfPlayer(sender);
		Player kickee = Bukkit.getPlayer(args[1]);
		if(!util.isInParty(kickee)) {
			sender.sendMessage(msg.format("&cThat player is not in a Party!"));
			return true;
		}
		if(util.getPartyOfPlayer(kickee) != party) {
			sender.sendMessage(msg.format("&cThat player is not apart of your Party!"));
			return true;
		}
		if(party.getPartyOwner() == kickee) {
			sender.sendMessage(msg.format("&aYou should seek help for your suicidal ways!"));
			return true;
		}
		party.removePartyParticpants(kickee);
		party.chat(msg.format("&c" + kickee.getName() + " &ahas been kicked."));
		return true;
	}
	
	public boolean disband(Player sender, String[] args) {
		if(args.length != 1) {
			msg.sendHelp(sender, "disband");
			return true;
		}
		if(!util.isInParty(sender)) {
			sender.sendMessage(msg.format("&cYou're not in a Party!"));
			return true;
		}
		if(!util.isPartyLeader(sender)) {
			sender.sendMessage(msg.format("&cOnly the Party leader may disband"));
			return true;
		}
		Party party = util.getPartyOfPlayer(sender);
		party.chat(msg.format("&c[Party] Party Disbanded"));
		party.disbandParty();
		if(msg.getDirectChatPlayers().contains(sender)) {
			msg.getDirectChatPlayers().remove(sender);
		}
		return true;
	}
	
	/* We assure everything is valid and proceed to create a new Party class,
	 * which adds our new Party class to our global Party list. */
	public boolean create(Player sender, String[] args) {
		if(args.length != 1) {
			msg.sendHelp(sender, "create");
			return true;
		}
		if(util.isInParty(sender)) {
			sender.sendMessage(msg.format("&cYou're already in a party!"));
			return true;
		}
		new Party(sender);
		sender.sendMessage(msg.format("&a[Party] You created a Party! Invite friends to it with &c/pc invite <player>"));
		return true;
	}

	/* Handle our quick chat command (/pc m <msg>) 
	 * Check our args and assure everything is valid.
	 * 
	 * The loop used below to is somewhat confusing. Breakdown: 
	 * We create a new String List and begin looping through our args
	 * adding them to the list. We start the loop from "1" 
	 * Because of the arguments nature (/pc m <msg>)
	 *      (args)                          0 1    */
	public boolean quickChat(Player sender, String[] args) {
		if (args.length <= 1) {
			msg.sendHelp(sender, "quickChat");
			return true;
		}
		if (!util.isInParty(sender)) {
			sender.sendMessage(msg.format("&cYou're not in a party!"));
			return true;
		}
		Party party = util.getPartyOfPlayer(sender);
		List<String> msgArgs = new ArrayList<String>();
		for (int i = 1; i < args.length; i++) {
			msgArgs.add(args[i]);
		}
		String messageArgs = StringUtils.join(msgArgs, " ");
		String finalMessage = msg.partyChatFormat(sender, messageArgs);
		party.chat(finalMessage);
		msg.partySocialSpy(sender, party, messageArgs);
		return true;
	}
	
	/* Check for staff permission and execute the reload 
	 * out of our Util class. */
	public boolean reload(Player sender, String[] args) {
		if(!sender.hasPermission("partychat.relaod")) {
			sender.sendMessage(msg.format("&cYou lack the required permission node!"));
			return true;
		}
		util.reloadConfig();
		sender.sendMessage(msg.format("&aPartyChat configuration reloaded."));
		return true;
	}

}
