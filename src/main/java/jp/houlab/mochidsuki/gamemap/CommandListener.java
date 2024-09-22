package jp.houlab.mochidsuki.gamemap;

import jp.houlab.mochidsuki.border.BorderInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static jp.houlab.mochidsuki.gamemap.Main.plugin;

/**
 * コマンドリスナー
 * @author Mochidsuki
 */
public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(s.equalsIgnoreCase("givebigmap")) {
            if(strings.length >= 1 && strings[0].equalsIgnoreCase("@a")) {
                for(Player player : plugin.getServer().getOnlinePlayers()) {
                    GiveMap.giveBig(player);
                    GiveMap.giveMini(player);
                }
            }else {
                GiveMap.giveBig((Player) commandSender);
                GiveMap.giveMini((Player) commandSender);
            }
        }
        if(s.equalsIgnoreCase("debugg")) {
            commandSender.sendMessage(BorderInfo.getNowPX() + "," + BorderInfo.getNowMX() + "," + BorderInfo.getNowPZ() + "," + BorderInfo.getNowMZ());
        }
        return false;
    }
}
