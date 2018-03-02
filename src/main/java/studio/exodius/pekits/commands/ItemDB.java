package studio.exodius.pekits.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;

/**
 * <p>Copyright (c) ExodiusMC, 2018. Property of Exodius Studios.</p>
 *
 * @author aleja
 * @version 1.0.0
 * @since 2/14/2018
 */
public class ItemDB implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("pekits.itemdb")){
            if(commandSender instanceof Player){
                Player player = (Player) commandSender;

                Item item = player.getInventory().getItemInHand();

                player.sendMessage(TextFormat.GOLD + item.getName() + TextFormat.YELLOW + "("+item.getNamedTag()+")");
                player.sendMessage(TextFormat.GOLD + String.valueOf(item.getId()) + TextFormat.YELLOW + "("+item.getCustomBlockData()+")");
                player.sendMessage(TextFormat.GOLD + "Tier: " + TextFormat.YELLOW + "("+String.valueOf(item.getTier())+")");
                player.sendTitle("TITLE", "SUBTITLEEEE", 10, 40, 10);
                player.sendActionBar("ACTION BAR", 15, 40, 10);
            } else {
                commandSender.sendMessage(TextFormat.RED + "Command can only be done by a player.");
            }
        } else {
            commandSender.sendMessage(TextFormat.RED + "You do not have permission");
        }

        return false;
    }
}
