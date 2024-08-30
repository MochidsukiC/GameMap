package jp.houlab.mochidsuki.gamemap;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class OriginMapRender extends MapRenderer {

    static public Color[] colors = new Color[16384];
    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        int colorsi = 0;
        for(int x = 0;x <= 127;x++){
            for(int y = 0;y <= 127;y++){
                colors[colorsi] = mapCanvas.getBasePixelColor(x,y);
                colorsi++;

            }
        }
    }
}
