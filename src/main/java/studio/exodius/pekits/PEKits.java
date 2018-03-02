package studio.exodius.pekits;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import studio.exodius.pekits.listeners.PlayerConnection;
import studio.exodius.pekits.listeners.PlayerDamaged;
import studio.exodius.pekits.listeners.PlayerJoin;

import java.io.File;

/**
 * <p>Copyright (c) ExodiusMC, 2018. Property of Exodius Studios.</p>
 *
 * @author aleja
 * @version 1.0.0
 * @since 2/14/2018
 */
public class PEKits extends PluginBase{

    private static File KITS_FILE = new File("plugins/PEKits/kits.yml");
    private static Config KITS;
    public static final String COMBAT_TAG = TextFormat.colorize("&c&lCombat&r \uD83E\uDD14 ");
    public static final String SERVER_TAG = TextFormat.colorize("&b&lLuckyFEED&r \uD83E\uDD14 ");
    @Getter public Nukkit nukkit;

    public void onLoad(){
        if(!KITS_FILE.exists()){
            saveResource("kits.yml", false);
            KITS = new Config(KITS_FILE, Config.YAML);
        } else {
            KITS = new Config(KITS_FILE, Config.YAML);
        }

        this.getLogger().info("PEKits has been loaded.");
    }

    public void onDisable(){
        this.getLogger().info("PEKits has been disabled.");
    }
    public void onEnable(){
        nukkit = getNukkit();

        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamaged(this), this);
        getServer().getPluginManager().registerEvents(new PlayerConnection(this), this);

        getServer().getCommandMap().registerSimpleCommands(this);

        this.getLogger().info("PEKits has been enabled.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("kititemdb")){
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
            return true;
        }

        return false;
    }
}
