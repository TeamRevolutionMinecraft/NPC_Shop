package net.revolution.revonpcshop.actions;

import dev.sergiferry.playernpc.api.NPC;
import net.revolution.revonpcshop.shop.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class NPCActions {
    public static final BiConsumer<NPC, Player> OPENSHOP = (npc1, player1) -> {
        Shop shop = Shop.openShop(player1, npc1);
        player1.openInventory(shop.buildShopGUI());
    };
}
