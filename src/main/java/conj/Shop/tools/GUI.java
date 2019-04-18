package conj.Shop.tools;

import conj.Shop.data.*;
import conj.Shop.enums.*;
import org.apache.commons.lang.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import conj.Shop.events.*;
import java.util.*;

public class GUI implements Listener
{
    private Inventory inventory;
    private String title;
    private Plugin plugin;
    private Page page;
    private PageData data;
    private String viewer;
    private HashMap<Object, Object> pass;
    
    public PageData getData() {
        return this.data;
    }
    
    public HashMap<Object, Object> getPass() {
        return this.pass;
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public Page getPage() {
        return this.page;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }
    
    public void addPass(final Object entry, final Object data) {
        if (this.pass == null) {
            this.pass = new HashMap<Object, Object>();
        }
        this.pass.put(entry, data);
    }
    
    public void setInventory(final Inventory inventory) {
        this.inventory = inventory;
    }
    
    public void setData(final PageData data) {
        this.data = data;
    }
    
    public void setTitle(final String title) {
        this.title = StringUtils.left(title, 32);
    }
    
    public void register() {
        final PluginManager manager = this.plugin.getServer().getPluginManager();
        manager.registerEvents((Listener)this, this.plugin);
    }
    
    public void destroy() {
        InventoryClickEvent.getHandlerList().unregister((Listener)this);
        InventoryCloseEvent.getHandlerList().unregister((Listener)this);
        PluginDisableEvent.getHandlerList().unregister((Listener)this);
    }
    
    public void open(final Player player) {
        this.destroy();
        if (this.getTitle() != null) {
            final Inventory i = Bukkit.createInventory((InventoryHolder)null, this.inventory.getSize(), this.getTitle());
            i.setContents(this.inventory.getContents());
            this.inventory = i;
        }
        final PageOpenEvent e = new PageOpenEvent(player, this.data, this, this.page, 0, this.getInventory());
        Bukkit.getServer().getPluginManager().callEvent((Event)e);
        if (!e.isCancelled()) {
            player.openInventory(this.inventory);
            this.register();
            this.viewer = player.getUniqueId().toString();
        }
    }
    
    public GUI(final Plugin plugin, final PageData data, final Inventory inventory, final Page page) {
        this.plugin = plugin;
        this.data = data;
        this.page = page;
        this.inventory = inventory;
    }
    
    public GUI(final GUI gui) {
        this.plugin = gui.getPlugin();
        this.data = gui.getData();
        this.page = gui.getPage();
        this.inventory = gui.getInventory();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void InteractEditor(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getWhoClicked();
        if (this.viewer.equals(player.getUniqueId().toString()) && event.getClickedInventory() != null) {
            final boolean top = event.getRawSlot() < event.getView().getTopInventory().getSize();
            Debug.log((String.valueOf(event.getWhoClicked().getName()) + " clicked " + new String(top ? "top" : "bottom") + " of " + this.data + " on page " + this.page != null) ? this.page.getID() : "null");
            final PageClickEvent e = new PageClickEvent(player, this.data, this, this.page, event.getSlot(), event.getRawSlot(), event.getCurrentItem(), event.getInventory(), event.getClickedInventory(), event.getClick(), top);
            Bukkit.getServer().getPluginManager().callEvent((Event)e);
            if (!e.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void InteractEditor(final PlayerQuitEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        final Player player = event.getPlayer();
        if (this.viewer.equals(player.getUniqueId().toString())) {
            this.destroy();
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void InteractEditor(final InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getPlayer();
        if (this.viewer.equals(player.getUniqueId().toString())) {
            final PageCloseEvent e = new PageCloseEvent(player, this.data, this, this.page, 0, event.getInventory());
            Bukkit.getServer().getPluginManager().callEvent((Event)e);
            if (!e.isCancelled()) {
                this.destroy();
            }
            else {
                this.open(player);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void InteractEditor(final PluginDisableEvent event) {
        if (this.viewer != null && Bukkit.getPlayer(UUID.fromString(this.viewer)) != null) {
            Bukkit.getPlayer(UUID.fromString(this.viewer)).closeInventory();
        }
    }
}
