package net.luckyfeed.pekits.kits;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.luckyfeed.pekits.PEKits;
import net.luckyfeed.pekits.Requirement;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>Copyright (c) LUCKYFEED, 2018.</p>
 *
 * @author LuckyClutch
 * @version 1.0.0
 * @since 3/1/2018
 */
public class KitLoader {

    private PEKits plugin;

    public KitLoader(PEKits plugin){
        this.plugin = plugin;
    }

    File kitsFile = new File("plugins/PEKits/kits.yml");
    Config kitConfig = new Config(kitsFile, Config.YAML);

    private Gson gson = new Gson();

    private List<Kit> loadedKits = Lists.newArrayList();

    public void load() {
        if(!kitsFile.exists()){
            plugin.saveResource("kits.yml", false);
            kitConfig = new Config(kitsFile, Config.YAML);
        } else {
            kitConfig = new Config(kitsFile, Config.YAML);
        }

        ConfigSection cs = kitConfig.getSections("");

        if(cs == null)
            return;

        for(String kits : cs.getKeys(false)){
            ConfigSection kit = cs.getSection(kits);
            String name = kit.getString("name", "default");
            String displayName = kit.getString("displayName", "default");
            List<String> lore = kit.getStringList("lore");
            Inventory items = gson.fromJson(kit.getString("items"), new TypeToken<Inventory>() {}.getType());
            List<String> requirements = kit.getStringList("requirements");
            String permission = kit.getString("permission", "none");

            List<Requirement> requirementMap = Lists.newArrayList();
            for(String req : requirements){
                requirementMap.add(new Requirement(Requirement.RequirementType.valueOf(req.split(":")[0]), Integer.valueOf(req.split(":")[1])));
            }

            loadedKits.add(new Kit(name, displayName, lore, items, requirementMap, permission));
        }
    }

    public class Kit {
        @Getter String name;
        @Getter String displayName;
        @Getter List<String> lore;
        @Getter Inventory items;
        @Getter List<Requirement> requirements;
        @Getter String permission;

        public Kit(String name, String displayName, List<String> lore, Inventory items, List<Requirement> requirements, String permission) {
            this.name = name;
            this.displayName = displayName;
            this.lore = lore;
            this.items = items;
            this.requirements = requirements;
            this.permission = permission;
        }

        public void saveItems(Player player){
            if(player.getInventory() == null){
                player.sendMessage(TextFormat.colorize(PEKits.SERVER_TAG + "You need to have some items to save items to a kit."));
                return;
            }

            items = player.getInventory();

            ConfigSection cs = kitConfig.getSections(name);
            cs.set("items", items);
            kitConfig.save();

            player.sendMessage(TextFormat.colorize(PEKits.SERVER_TAG + "You have saved your inventory items into " + name));
        }
        public void loadItems(Player player){
            if(items == null || items.isEmpty()){
                player.sendMessage("This kit items are empty.");
                return;
            }

            player.getInventory().clearAll();

            player.getInventory().setContents(items.getContents());

            player.sendMessage(TextFormat.colorize(PEKits.SERVER_TAG + "You have loaded &e" + name + " &7items into your inventory"));
        }
    }
}
