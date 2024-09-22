package jp.houlab.mochidsuki.gamemap;


import jp.houlab.mochidsuki.border.BorderInfo;
import jp.houlab.mochidsuki.pin.Pin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.Objects;

import static jp.houlab.mochidsuki.gamemap.Main.config;
import static jp.houlab.mochidsuki.pin.V.*;

/**
 * ビッグマップをレンダリングする
 * @author Mochiduski
 */
public class BigMapRender extends MapRenderer {
    /**
     * レンダリングする
     * @param map マップ
     * @param canvas キャンバス
     * @param player 対象プレイヤー
     */
    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        //Mapコピー
        int mi = 0;
        for (int x = 0; x <= 127; x++) {
            for (int z = 0; z <= 127; z++) {

                canvas.setPixelColor(x, z, OriginMapRender.colors[mi]);
                mi = mi + 1;
            }
        }
        int mapZoom;
        switch (config.getInt("MAP.MapScale")) {
            case 0:
                mapZoom = 1;
                break;
            case 1:
                mapZoom = 2;
                break;
            case 2:
                mapZoom = 4;
                break;
            case 3:
                mapZoom = 8;
                break;
            case 4:
                mapZoom = 16;
                break;
            default:
                mapZoom = 8;
        }
        //カーソルクリア
        for (int i = 0; i < canvas.getCursors().size(); i++) {
            canvas.getCursors().removeCursor(canvas.getCursors().getCursor(i));
        }

        //チームメイト表示

        MapCursorCollection cursor = new MapCursorCollection();

        for (String name : player.getScoreboard().getPlayerTeam(player).getEntries()) {
            if (Bukkit.getPlayer(name) != null && Bukkit.getPlayer(name).isOnline() ) {

                Player teammate = Bukkit.getPlayer(name);
                if(teammate != player) {

                    float yaw = teammate.getLocation().getYaw();
                    if (yaw < 0) {
                        yaw = yaw + 360;
                    }
                    cursor.addCursor(createCursor((teammate.getLocation().getBlockX() - config.getInt("MAP.Center.x")) / mapZoom*2, (teammate.getLocation().getBlockZ() - config.getInt("MAP.Center.z")) / mapZoom*2, (byte) ((yaw - yaw % 22.5) / 22.5), MapCursor.Type.BLUE_POINTER));

                }
                if (pin.containsKey(teammate)) {
                    cursor.addCursor(createCursor(((pin.get(teammate).getBlockX() - config.getInt("MAP.Center.x")) / mapZoom*2),((pin.get(teammate).getBlockZ() - config.getInt("MAP.Center.z")) / mapZoom * 2),(byte) 0,MapCursor.Type.BANNER_YELLOW));

                }
                if (pinRed.containsKey(teammate)) {
                    cursor.addCursor(createCursor(((pinRed.get(teammate).getBlockX() - config.getInt("MAP.Center.x")) / mapZoom*2),((pinRed.get(teammate).getBlockZ() - config.getInt("MAP.Center.z")) / mapZoom * 2),(byte) 0,MapCursor.Type.BANNER_RED));

                }
            }
        }

        canvas.setCursors(cursor);


        //border予測線
        int[] distance = new int[4];
        distance[0] = (int) ((BorderInfo.getTargetPX() - config.getInt("MAP.Center.x")) / mapZoom);
        distance[1] = (int) ((BorderInfo.getTargetMX() - config.getInt("MAP.Center.x")) / mapZoom);
        distance[2] = (int) ((BorderInfo.getTargetMZ() - config.getInt("MAP.Center.z")) / mapZoom);
        distance[3] = (int) ((BorderInfo.getTargetPZ() - config.getInt("MAP.Center.z")) / mapZoom);
        for (; distance[1] <= distance[0]; distance[1]++) {
            if (distance[1] >= -64 && distance[1] <= 64 && distance[2] >= -64 && distance[2] <= 64) {
                canvas.setPixelColor(distance[1] + 64, distance[2] + 64, Color.gray);
            }
            if (distance[1] >= -64 && distance[1] <= 64 && distance[3] >= -64 && distance[3] <= 64) {
                canvas.setPixelColor(distance[1] + 64, distance[3] + 64, Color.gray);
            }
        }
        distance[1] = (int) ((BorderInfo.getTargetMX() - config.getInt("MAP.Center.x")) / mapZoom);
        for (; distance[2] <= distance[3]; distance[2]++) {
            if (distance[0] >= -64 && distance[0] <= 64 && distance[2] >= -64 && distance[2] <= 64) {
                canvas.setPixelColor(distance[0] + 64, distance[2] + 64, Color.gray);
            }
            if (distance[1] >= -64 && distance[1] <= 64 && distance[2] >= -64 && distance[2] <= 64) {
                canvas.setPixelColor(distance[1] + 64, distance[2] + 64, Color.gray);
            }
        }

        //border現在位置
        int[] distanceNow = new int[4];
        distanceNow[0] = (int) (BorderInfo.getNowPX() - config.getInt("MAP.Center.x")) / mapZoom;
        distanceNow[1] = (int) (BorderInfo.getNowMX() - config.getInt("MAP.Center.x")) / mapZoom;
        distanceNow[2] = (int) (BorderInfo.getNowMZ() - config.getInt("MAP.Center.z")) / mapZoom;
        distanceNow[3] = (int) (BorderInfo.getNowPZ() - config.getInt("MAP.Center.z")) / mapZoom;
        for (; distanceNow[1] <= distanceNow[0]; distanceNow[1]++) {
            if (distanceNow[1] >= -64 && distanceNow[1] <= 64 && distanceNow[2] >= -64 && distanceNow[2] <= 64) {
                canvas.setPixelColor(distanceNow[1] + 64, distanceNow[2] + 64, Color.red);
            }
            if (distanceNow[1] >= -64 && distanceNow[1] <= 64 && distanceNow[3] >= -64 && distanceNow[3] <= 64) {
                canvas.setPixelColor(distanceNow[1] + 64, distanceNow[3] + 64, Color.red);
            }
        }
        distanceNow[1] = (int) (BorderInfo.getNowMX() - config.getInt("MAP.Center.x")) / mapZoom;
        for (; distanceNow[2] <= distanceNow[3]; distanceNow[2]++) {
            if (distanceNow[0] >= -64 && distanceNow[0] <= 64 && distanceNow[2] >= -64 && distanceNow[2] <= 64) {
                canvas.setPixelColor(distanceNow[0] + 64, distanceNow[2] + 64, Color.red);
            }
            if (distanceNow[1] >= -64 && distanceNow[1] <= 64 && distanceNow[2] >= -64 && distanceNow[2] <= 64) {
                canvas.setPixelColor(distanceNow[1] + 64, distanceNow[2] + 64, Color.red);
            }
        }
    }

    /**
     * カーソルを位置の調整をして作成
     * @param x x座標
     * @param z z座標
     * @param rotation 回転
     * @param type カーソルタイプ
     * @return カーソル
     * マップ外のカーソル位置を調整
     */
    private MapCursor createCursor(int x, int z, byte rotation, MapCursor.Type type) {
        if (x > 128) {
            x = 128;
        } else if (x < -128) {
            x = -128;
        }
        if (z > 128) {
            z = 128;
        } else if (z < -128) {
            z = -128;
        }
        return new MapCursor((byte) x, (byte) z,rotation,type,true);
    }
}
