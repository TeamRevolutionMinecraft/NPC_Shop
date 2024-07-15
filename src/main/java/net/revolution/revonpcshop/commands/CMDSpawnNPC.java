package net.revolution.revonpcshop.commands;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import net.revolution.revonpcshop.RevoNPCShop;
import net.revolution.revonpcshop.actions.NPCActions;
import net.revolution.revonpcshop.shop.Shop;
import net.revolution.revonpcshop.shop.ShopNPC;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.BiConsumer;

public class CMDSpawnNPC implements CommandExecutor {
    //TODO Remove this command
    private final JavaPlugin plugin;
    private final String testTexture = "ewogICJ0aW1lc3RhbXAiIDogMTcwMTYyNTY0NDIxOSwKICAicHJvZmlsZUlkIiA6ICJhY2Q4MTdmNTBhNGY0ZDcyOGU1MWUyN2Y2YmJhY2RkYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCdWlsZGVyc19JbmMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I5MTQ4MzQ5MmUwZTFiZjJhODNkNGZlNmNmZGQ5OTlhZGU5ZWI1NTViZDlkMDY3YjdhOWU0NDZmZjYwZmU0YyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNDBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9";
    private final String testSignature = "T4/8vwwBKIJz0bTkud4O/MgPdC9u20PBCYwX5tG1OSbInpO3o9QGcPz0totR66UExNw5bT0BIDF8Km+A1OdRMgC1ULVrTZe32qSldv0Cvv+UTRYnlMU21l2tv5g7YYEVmpoKkf5Kjhse2LT8tag/W1CIr+Akfa0L8ItydzAunzXqnbAY5IBDDCYfqyH2sDFPvQ5rlhVLUMQ9zdfyx1AFgwos5q9Gv9y6V0bMb98fItSAPJtWRFf6M/nNeQVZcpnLsGVF48FMN5LWTmDpU7wUtoGJuyWpEr8AA1/YPha3l3dGEGMQcNxyxZI+5JbmBtYDbJNtnPOYJjwUCn4pGvixI9nykyjPvS5pMjh6r6avtaaU+ZGxgQYGdHU2yZ421/3D6hFz6gCno/dZ0zvdqu3Avl63ehpTxIkS8mjWw8cXVOnY3v/S0omXr6PM5wjbYyKF9A4X84lEUVxxstfQq+rp2s0HGpJKSXHMZYbVuwfbFXyU2TCbQNqudT+u5wkDUFiaUs0/N23Td8jG0c9h7+DRAdMPY9n/UawfpCfjngnYS5sTeniWXmPoWN1koK9ewSODJ9dZ6EZX90bjFw1qCygho3CBMiNixx/laUrXXSrF9RfFhAYMlTjunCgYmYHq47fJy9NJmzJ6RC8MMcDicRJTtOscUpfCfic4+k4GItILgwc=";
    public CMDSpawnNPC(RevoNPCShop plugin) {
        this.plugin = plugin;
        plugin.getCommand("spawnnpc").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.isOp()) return false;
        UUID uuid = UUID.randomUUID();
        NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(plugin, player.getName()+ ":" + uuid, player.getLocation());
        String name = strings.length >= 1 ? strings[0] : "hallo";
        new ShopNPC(player.getLocation(), testTexture, testSignature, uuid, player.getName(), name);
        npc.setSkin(testTexture, testSignature);
//        NPC.Personal npc = NPCLib.getInstance().generatePersonalNPC(player, plugin, "TEST", player.getLocation());
//        npc.create();
//        npc.show();
        npc.setItem(NPC.Slot.LEGS, new ItemStack(Material.IRON_LEGGINGS));
        npc.setText(name);
        npc.setHideDistance(150);

        npc.addCustomClickAction(NPC.Interact.ClickType.RIGHT_CLICK, NPCActions.OPENSHOP);
        npc.update();
        npc.forceUpdate();
        this.plugin.getLogger().info("Command fertig");
        return true;
    }

}
