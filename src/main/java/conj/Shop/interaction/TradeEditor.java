package conj.Shop.interaction;

import conj.Shop.events.*;
import conj.Shop.enums.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import conj.Shop.data.*;
import org.bukkit.event.*;
import org.bukkit.*;
import conj.Shop.base.*;
import conj.Shop.tools.*;
import org.bukkit.plugin.*;
import java.util.*;

public class TradeEditor implements Listener
{
    @EventHandler
    public void PageviewClick(final PageClickEvent event) {
        final Player player = event.getPlayer();
        final int slot = event.getSlot();
        final Page page = event.getPage();
        final ItemStack item = event.getItem();
        if (event.getPageData().equals(PageData.EDIT_ITEM_MANAGE)) {
            final int itemslot = (int) event.getGUI().getPass().get("slot");
            final PageSlot ps = page.getPageSlot(itemslot);
            if (event.isTopInventory() && slot == 19) {
                if (!ps.getFunction().equals(Function.TRADE)) {
                    return;
                }
                if (event.getClick().equals((Object)ClickType.LEFT)) {
                    this.openTradeEditor(player, page, itemslot);
                }
            }
        }
        else if (event.getPageData().equals(PageData.EDIT_ITEM_INVENTORY)) {
            final int itemslot = (int) event.getGUI().getPass().get("slot");
            final PageSlot ps = page.getPageSlot(itemslot);
            if (event.isTopInventory()) {
                if (slot == 22) {
                    Editor.editItem(player, page, itemslot);
                    return;
                }
                if (item != null && !item.getType().equals((Object)Material.AIR) && ps.getItems().contains(item)) {
                    ps.removeItem(item);
                    this.openTradeEditor(player, page, itemslot);
                }
            }
            else if (item != null && !item.getType().equals((Object)Material.AIR) && ps.getItems().size() < 7) {
                ps.addItem(item);
                this.openTradeEditor(player, page, itemslot);
            }
        }
    }
    
    public void openTradeEditor(final Player player, final Page page, final int slot) {
        final InventoryCreator inv = new InventoryCreator(new StringBuilder().append(ChatColor.BLUE).append(slot).append(ChatColor.DARK_GRAY).append("\u2590 Inventory").toString(), 3);
        final GUI gui = new GUI((Plugin)Initiate.getPlugin((Class)Initiate.class), PageData.EDIT_ITEM_INVENTORY, inv.getInventory(), page);
        if (page.getInventory().getItem(slot) == null) {
            page.openEditor(player);
            return;
        }
        final PageSlot ps = page.getPageSlot(slot);
        final int[] blanks = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 23, 24, 25, 26 };
        inv.setBlank(blanks, Material.BLACK_STAINED_GLASS_PANE, 15);
        inv.setItem(22, Material.COMPASS, ChatColor.RED + "Back");
        inv.addLore(22, ChatColor.GRAY + "Return to item properties");
        for (final ItemStack i : ps.getItems()) {
            if (inv.getInventory().firstEmpty() != -1) {
                inv.setItem(inv.getInventory().firstEmpty(), i);
            }
        }
        gui.addPass("slot", slot);
        gui.open(player);
    }
}
