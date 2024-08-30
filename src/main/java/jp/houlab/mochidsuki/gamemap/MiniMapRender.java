package jp.houlab.mochidsuki.gamemap;

import jp.houlab.mochidsuki.armorshield.ShieldUtil;
import jp.houlab.mochidsuki.border.BorderInfo;
import jp.houlab.mochidsuki.pin.V;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;

import static jp.houlab.mochidsuki.border.Main.plugin;


public class MiniMapRender extends MapRenderer {
    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        map.setCenterX(player.getLocation().getBlockX());
        map.setCenterZ(player.getLocation().getBlockZ());
        //ピクセルクリア
        for (int x = 0; x <= 128; x++) {
            for (int z = 0; z <= 128; z++) {
                canvas.setPixelColor(x, z, canvas.getBasePixelColor(x, z));
            }
        }
        //カーソルクリア
        for (int i = 0; i <= canvas.getCursors().size(); i++) {
            try {
                canvas.getCursors().removeCursor(canvas.getCursors().getCursor(i));
            } catch (Exception ignored) {
            }
        }
        //チームメイト表示
        Team playerteam = player.getScoreboard().getPlayerTeam(player);
        Iterator<String> iterator = playerteam.getEntries().iterator();
        MapCursorCollection cursor = new MapCursorCollection();
        while (iterator.hasNext()) {
            try {
                Player teammate = player.getServer().getPlayer(iterator.next());
                if (teammate != null) {
                    if (!(teammate == player)) {
                        int x = teammate.getLocation().getBlockX() - player.getLocation().getBlockX();
                        if (x > 128) {
                            x = 127;
                        } else if (x < -128) {
                            x = -127;
                        }
                        int z = (teammate.getLocation().getBlockZ() - player.getLocation().getBlockZ());
                        if (z > 128) {
                            z = 127;
                        } else if (z < -128) {
                            z = -127;
                        }
                        float yaw = teammate.getLocation().getYaw();
                        if(yaw<0){
                            yaw = yaw +360;
                        }
                        cursor.addCursor(new MapCursor((byte) x, (byte) z, (byte) ((yaw - yaw % 22.5) / 22.5), MapCursor.Type.BLUE_POINTER, true));

                    }
                    if (V.pin.containsKey(teammate)) {
                        int x = V.pin.get(teammate).getBlockX() - player.getLocation().getBlockX();
                        int z = V.pin.get(teammate).getBlockZ() - player.getLocation().getBlockZ();
                        if (x < 128 && x > -128 && z < 128 && z > -128) {
                            cursor.addCursor(new MapCursor((byte) x, (byte) z, (byte) 0, MapCursor.Type.BANNER_YELLOW, true));
                        }
                    }
                    if (V.pinRed.containsKey(teammate)) {
                        int xR = V.pinRed.get(teammate).getBlockX() - player.getLocation().getBlockX();
                        int zR = V.pinRed.get(teammate).getBlockZ() - player.getLocation().getBlockZ();
                        if (xR < 128 && xR > -128 && zR < 128 && zR > -128) {
                            cursor.addCursor(new MapCursor((byte) xR, (byte) zR, (byte) 0, MapCursor.Type.BANNER_RED, true));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        canvas.setCursors(cursor);

        //中心に対する線
        double[] cDistance = new double[4];
        cDistance[0] = BorderInfo.getNowCenterX() - player.getLocation().getBlockX();
        cDistance[1] = BorderInfo.getNowCenterZ() - player.getLocation().getBlockZ();
        for (int x = 0; x <= cDistance[0]; x++) {

        }


        //border予測線
        int[] distance = new int[4];
        distance[0] = (int) (BorderInfo.getTargetPX() - player.getLocation().getBlockX());
        distance[1] = (int) (BorderInfo.getTargetMX() - player.getLocation().getBlockX());
        distance[2] = (int) (BorderInfo.getTargetMZ() - player.getLocation().getBlockZ());
        distance[3] = (int) (BorderInfo.getTargetPZ() - player.getLocation().getBlockZ());
        for (; distance[1] <= distance[0]; distance[1]++) {
            if (distance[1] >= -64 && distance[1] <= 64 && distance[2] >= -64 && distance[2] <= 64) {
                canvas.setPixelColor(distance[1] + 64, distance[2] + 64, Color.gray);
            }
            if (distance[1] >= -64 && distance[1] <= 64 && distance[3] >= -64 && distance[3] <= 64) {
                canvas.setPixelColor(distance[1] + 64, distance[3] + 64, Color.gray);
            }
        }
        distance[1] = (int) (BorderInfo.getTargetMX() - player.getLocation().getBlockX());
        for (; distance[2] <= distance[3]; distance[2]++) {
            if (distance[0] >= -64 && distance[0] <= 64 && distance[2] >= -64 && distance[2] <= 64) {
                canvas.setPixelColor(distance[0] + 64, distance[2] + 64, Color.gray);
            }
            if (distance[1] >= -64 && distance[1] <= 64 && distance[2] >= -64 && distance[2] <= 64) {
                canvas.setPixelColor(distance[1] + 64, distance[2] + 64, Color.gray);
            }
        }

        //border現在位置
        int px = (int) (BorderInfo.getNowPX() - player.getLocation().getBlockX());
        int mx = (int) (BorderInfo.getNowMX() - player.getLocation().getBlockX());
        int pz = (int) (BorderInfo.getNowPZ() - player.getLocation().getBlockZ());
        int mz = (int) (BorderInfo.getNowMZ() - player.getLocation().getBlockZ());
        for (; mx <= px; mx++) {
            if (mx >= -64 && mx <= 64 && mz >= -64 && mz <= 64) {
                canvas.setPixelColor(mx + 64, mz + 64, Color.red);
            }
            if (mx >= -64 && mx <= 64 && pz >= -64 && pz <= 64) {
                canvas.setPixelColor(mx + 64, pz + 64, Color.red);
            }
        }
        mx = (int) (BorderInfo.getNowMX() - player.getLocation().getBlockX());
        for (; mz <= pz; mz++) {
            if (px >= -64 && px <= 64 && mz >= -64 && mz <= 64) {
                canvas.setPixelColor(px+ 64, mz + 64, Color.red);
            }
            if (mx >= -64 && mx <= 64 && mz >= -64 && mz <= 64) {
                canvas.setPixelColor(mx + 64, mz + 64, Color.red);
            }
        }
        int i = 0;
        for (String entry : playerteam.getEntries()) {
            if (plugin.getServer().getPlayer(entry) != null && plugin.getServer().getPlayer(entry).isOnline() && plugin.getServer().getPlayer(entry) != player) {
                Player teammate = Bukkit.getPlayer(entry);
                //下地
                for (int ii = 0; ii < 10; ii++) {
                    for (int iii = 0; iii < 128; iii++) {
                        if (teammate.hasPotionEffect(PotionEffectType.UNLUCK)) {
                            canvas.setPixelColor(iii, 127 - ii - i * 10, Color.RED);
                        } else if (teammate.getGameMode() == GameMode.SURVIVAL) {
                            canvas.setPixelColor(iii, 127 - ii - i * 10, Color.YELLOW);
                        } else {
                            canvas.setPixelColor(iii, 127 - ii - i * 10, Color.GRAY);
                        }
                    }
                }
                //枠
                for (int ii = 0; ii < 128; ii++) {
                    if (teammate.hasPotionEffect(PotionEffectType.UNLUCK)) {
                        canvas.setPixelColor(ii, 127 - i * 10, Color.RED.darker());
                        canvas.setPixelColor(ii, 127 - i * 10 - 9, Color.RED.darker());
                        if (ii < 10) {
                            canvas.setPixelColor(0, 128 - ii, Color.RED.darker());
                            canvas.setPixelColor(1, 128 - ii, Color.RED.darker());
                        }
                    } else if (teammate.getGameMode() == GameMode.SURVIVAL) {
                        canvas.setPixelColor(ii, 127 - i * 10, Color.YELLOW.darker());
                        canvas.setPixelColor(ii, 127 - i * 10 - 9, Color.YELLOW.darker());
                        if (ii < 10) {
                            canvas.setPixelColor(0, 128 - ii, Color.YELLOW.darker());
                            canvas.setPixelColor(127, 128 - ii, Color.YELLOW.darker());
                        }
                    } else {
                        canvas.setPixelColor(ii, 127 - i * 10, Color.GRAY.darker());
                        canvas.setPixelColor(ii, 127 - i * 10 - 9, Color.GRAY.darker());
                        if (ii < 10) {
                            canvas.setPixelColor(0, 128 - ii, Color.GRAY.darker());
                            canvas.setPixelColor(127, 128 - ii, Color.GRAY.darker());
                        }
                    }
                }

                MinecraftFont font = new MinecraftFont();
                canvas.drawText(2, 127 - i * 10 - 8, font, "§30;" + teammate.getName());

                int health = (int) teammate.getHealth();
                for (int ii = 0; ii < 20; ii++) {
                    if (ii < health) {
                        for (int iii = 0; iii < 3; iii++) {
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 1, Color.LIGHT_GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 2, Color.WHITE);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 3, Color.WHITE);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 4, Color.WHITE);
                            if (ii == health) {
                                canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 1, Color.LIGHT_GRAY);
                                canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 2, Color.LIGHT_GRAY);
                                canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 3, Color.LIGHT_GRAY);
                                canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 4, Color.LIGHT_GRAY);
                            }
                        }
                    } else {
                        for (int iii = 0; iii < 3; iii++) {
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 1, Color.DARK_GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 2, Color.GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 3, Color.GRAY);
                            canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 4, Color.GRAY);
                            if (ii == health) {
                                canvas.setPixelColor(127, 127 - i * 10 - 1, Color.DARK_GRAY);
                                canvas.setPixelColor(127, 127 - i * 10 - 2, Color.DARK_GRAY);
                                canvas.setPixelColor(127, 127 - i * 10 - 3, Color.DARK_GRAY);
                                canvas.setPixelColor(127, 127 - i * 10 - 4, Color.DARK_GRAY);
                            }
                        }
                    }
                }

                if (!teammate.hasPotionEffect(PotionEffectType.UNLUCK)) {
                    if (teammate.getInventory().getItem(22) != null) {
                        ShieldUtil shieldUtil = new ShieldUtil(teammate.getInventory().getItem(22));
                        for (int ii = 0; ii < shieldUtil.getShieldMax(); ii++) {
                            if (ii < shieldUtil.getShieldNow()) {
                                try {
                                    for (int iii = 0; iii < 3; iii++) {
                                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 5, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 6, shieldUtil.getShieldColor().asBungee().getColor());
                                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 7, shieldUtil.getShieldColor().asBungee().getColor());
                                        canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 8, shieldUtil.getShieldColor().asBungee().getColor());
                                        if (ii == shieldUtil.getShieldMax() - 1) {
                                            canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 5, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                            canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 6, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                            canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 7, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                            canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 8, shieldUtil.getShieldColor().asBungee().getColor().darker());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                for (int iii = 0; iii < 3; iii++) {
                                    canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 5, Color.DARK_GRAY);
                                    canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 6, Color.GRAY);
                                    canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 7, Color.GRAY);
                                    canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 8, Color.GRAY);
                                    if (ii == shieldUtil.getShieldMax() - 1) {
                                        canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 5, Color.DARK_GRAY);
                                        canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 6, Color.DARK_GRAY);
                                        canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 7, Color.DARK_GRAY);
                                        canvas.setPixelColor(67 + ii * 3 + 2, 127 - i * 10 - 8, Color.DARK_GRAY);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int ii = 0; ii < 20; ii++) {
                        if (ii < health - 20) {
                            for (int iii = 0; iii < 3; iii++) {
                                canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 5, Color.LIGHT_GRAY);
                                canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 6, Color.WHITE);
                                canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 7, Color.WHITE);
                                canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 8, Color.WHITE);
                            }
                        } else {
                            for (int iii = 0; iii < 3; iii++) {
                                canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 5, Color.DARK_GRAY);
                                canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 6, Color.GRAY);
                                canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 7, Color.GRAY);
                                canvas.setPixelColor(67 + ii * 3 + iii, 127 - i * 10 - 8, Color.GRAY);
                                if (ii == health - 20) {
                                    canvas.setPixelColor(127, 127 - i * 10 - 5, Color.DARK_GRAY);
                                    canvas.setPixelColor(127, 127 - i * 10 - 6, Color.DARK_GRAY);
                                    canvas.setPixelColor(127, 127 - i * 10 - 7, Color.DARK_GRAY);
                                    canvas.setPixelColor(127, 127 - i * 10 - 8, Color.DARK_GRAY);
                                }
                            }
                        }
                    }
                }

                i++;
            }
        }
    }
}
