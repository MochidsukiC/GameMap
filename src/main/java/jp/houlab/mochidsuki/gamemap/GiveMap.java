package jp.houlab.mochidsuki.gamemap;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import static jp.houlab.mochidsuki.gamemap.Main.config;

/**
 * マップを付与する
 * @author Mochidski
 */
public class GiveMap {
    /**
     * ビッグマップを付与する
     * @param player ターゲットのプレイヤー
     */
    static public void giveBig(Player player){
        ItemStack mapItemB = new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMetaB = (MapMeta)mapItemB.getItemMeta();

        MapView viewB = player.getServer().createMap(player.getWorld());
        viewB.addRenderer(new BigMapRender());
        mapMetaB.setMapView(viewB);
        mapItemB.setItemMeta(mapMetaB);
        switch (config.getInt("MAP.MapScale")) {
            case 0:
                viewB.setScale(MapView.Scale.CLOSEST);
                break;
            case 1:
                viewB.setScale(MapView.Scale.CLOSE);
                break;
            case 2:
                viewB.setScale(MapView.Scale.NORMAL);
                break;
            case 3:
                viewB.setScale(MapView.Scale.FAR);
                break;
            case 4:
                viewB.setScale(MapView.Scale.FARTHEST);
                break;
        }
        viewB.setTrackingPosition(true);
        viewB.setCenterX(config.getInt("MAP.Center.x"));
        viewB.setCenterZ(config.getInt("MAP.Center.z"));
        //ビッグマップ付与
        player.getInventory().setItem(8,mapItemB);
    }

    /**
     * ミニマップを付与する
     * @param player ターゲットのプレイヤー
     */
    static public void giveMini(Player player) {
        ItemStack mapItem = new ItemStack(Material.FILLED_MAP,1);//
        MapMeta mapMeta = (MapMeta)mapItem.getItemMeta();//
        MapView view = player.getServer().createMap(player.getWorld());//
        view.addRenderer(new MiniMapRender());//
        mapMeta.setMapView(view);//
        mapItem.setItemMeta(mapMeta);//
        view.setScale(MapView.Scale.CLOSEST);
        view.setTrackingPosition(true);
        player.getInventory().setItem(EquipmentSlot.OFF_HAND,mapItem);
    }
}
