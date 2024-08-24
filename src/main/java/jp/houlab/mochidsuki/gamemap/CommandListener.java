package jp.houlab.mochidsuki.gamemap;

import jp.houlab.mochidsuki.border.BorderInfo;
import jp.houlab.mochidsuki.border.V;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(s.equalsIgnoreCase("givebigmap")) {
            GiveMap.giveBig((Player) commandSender);
        }
        if(s.equalsIgnoreCase("debugg")) {
            commandSender.sendMessage(BorderInfo.getNowPX() + "," + BorderInfo.getNowMX() + "," + BorderInfo.getNowPZ() + "," + BorderInfo.getNowMZ());
        }
        return false;
    }
}
