package jp.houlab.mochidsuki.gamemap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getMap;

public final class Main extends JavaPlugin {

    static FileConfiguration config;
    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();
        config = getConfig();

        getCommand("giveBigMap").setExecutor(new CommandListener());
        getCommand("debugg").setExecutor(new CommandListener());

        ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();
        getMap(config.getInt("MAP.MapData")).addRenderer(new OriginMapRender());
        mapMetaB.setMapId(config.getInt("MAP.MapData"));
        getMap(config.getInt("MAP.MapData")).setTrackingPosition(true);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
