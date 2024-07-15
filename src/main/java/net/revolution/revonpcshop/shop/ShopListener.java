package net.revolution.revonpcshop.shop;

import net.kyori.adventure.text.Component;
import net.revolution.revonpcshop.RevoNPCShop;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class ShopListener implements Listener {
    public ShopListener(RevoNPCShop plugin) {
        RevoNPCShop.getInstance().getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void ownerAccess(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null ||
                !(event.getClickedInventory().getHolder() instanceof Shop)) return;
        Shop shop = (Shop) event.getClickedInventory().getHolder();
        if (!shop.isOwnerAccess()) return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem.getType() == Material.RED_STAINED_GLASS) {
            shop.setInEditMode(!shop.isInEditMode());
        }
        if(!shop.isInEditMode()) {
            player.openInventory(shop.buildShopGUI());
        } else {
            player.openInventory(shop.buildConfigGui());
        }
    }

    @EventHandler
    public void buyItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null ||
                !(event.getClickedInventory().getHolder() instanceof Shop)) return;

        Shop shop = (Shop) event.getClickedInventory().getHolder();

        if (shop.isInEditMode()) return;
        event.setCancelled(true);
        ShopOffer offer = shop.getShopView().getShopOfferAtSlot(event.getSlot());
        if (offer == null) return;
        ItemStack storage = offer.getStorage();
        if (storage == null) return;
        if (!(storage.getAmount() >= offer.getOfferItem().getAmount() || shop.isAdminShop())) {
            player.sendMessage(Component.text(ChatColor.DARK_RED + "Dieser Shop hat nicht genug resourcen"));
            return;
        }


        if (offer.canBuyAndRemove(player.getInventory())) {
            if (!shop.isAdminShop()) {

                storage.setAmount(storage.getAmount() - offer.getOfferItem().getAmount());
                offer.getVault().setAmount(offer.getVault().getAmount() + offer.getPrice().getAmount());
            }
            player.getWorld().dropItem(player.getLocation(), offer.getOfferItem());
            RevoNPCShop.getInstance().getLogger().info("Player bought");
        } else {
            player.sendMessage(Component.text(ChatColor.DARK_RED + "Du hast leider nicht genug Resourcen"));
        }

        RevoNPCShop.getInstance().getLogger().info("Noch " + storage.getAmount());
    }


    @EventHandler
    public void configStore(InventoryClickEvent event) {

    }

    @EventHandler
    public void startTimer(InventoryClickEvent event) {
        if (event.getClickedInventory() == null ||
                !(event.getClickedInventory().getHolder() instanceof Shop)) return;
        ((Shop) event.getClickedInventory().getHolder()).startTimer();
    }
}
