package jp.houlab.mochidsuki.gamemap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getMap;
/**
 * メインクラス
 */
public final class Main extends JavaPlugin {

    static FileConfiguration config;
    static public Plugin plugin;
    /**
     * 起動時の初期化処理
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

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
    /**
     * 終了
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
