package me.brokencloud.postal.command;

import me.brokencloud.postal.Postal;
import me.brokencloud.postal.menu.ListMenu;
import me.brokencloud.postal.menu.SendMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PostalCommand implements CommandExecutor {
    // TODO: Currently test purpose only, full interface awaiting
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 0) {
                switch (args[0]) {
                    case "send":
                        if (args.length == 2) {
                            Player recipient = Bukkit.getPlayer(args[1]);
                            if (recipient == null) {
                                player.sendMessage(ChatColor.RED + "Player " + args[1] + " not found.");
                            } else {
                                Postal.getInstance().getOdalitaMenus().openMenu(new SendMenu(recipient), player);
                                player.sendMessage(ChatColor.GREEN + "Package sent to " + args[1]);
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Usage: /postal send <player>");
                        }
                        break;
                    case "list":
                        Postal.getInstance().getOdalitaMenus().openMenu(new ListMenu(), player);
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /postal [list|send]");
            }
        }
        return true;
    }
}
