package net.revolution.revonpcshop.commands;

import net.revolution.revonpcshop.RevoNPCShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CMDRemoveAllNPCs implements CommandExecutor {

    public CMDRemoveAllNPCs(RevoNPCShop plugin) {
        plugin.getCommand("removenps").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) return false;
        File dir = new File(RevoNPCShop.getInstance().getDataFolder().getPath() + "/npcs");
        File[] directoryListing = dir.listFiles();
        for (File f : directoryListing) {
            f.delete();
        }
        File dir2 = new File(RevoNPCShop.getInstance().getDataFolder().getPath() + "/shops");
        File[] directoryListing2 = dir2.listFiles();
        for (File f : directoryListing2) {
            f.delete();
        }

        return true;
    }
}
