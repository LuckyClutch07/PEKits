package net.luckyfeed.pekits.kits;

import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.luckyfeed.pekits.PEKits;
import net.luckyfeed.pekits.Requirement;

import java.io.File;
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

    private List<Kit> loadedKits = Lists.newArrayList();

    public void load() {
        if(!kitsFile.exists()){
            plugin.saveResource("kits.yml", false);
            kitConfig = new Config(kitsFile, Config.YAML);
        } else {
            kitConfig = new Config(kitsFile, Config.YAML);
        }


    }

    @AllArgsConstructor
    @Getter
    public class Kit {
        String name;
        String displayName;
        List<String> lore;
        List<Item> items;
        List<Requirement> requirements;
        String permission;
    }
}
