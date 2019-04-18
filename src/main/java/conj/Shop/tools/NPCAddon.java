package conj.Shop.tools;

import net.citizensnpcs.api.trait.*;
import conj.Shop.control.*;
import org.bukkit.entity.*;
import conj.Shop.data.*;
import java.util.*;
import org.bukkit.event.*;
import net.citizensnpcs.api.event.*;

public class NPCAddon extends Trait
{
    public NPCAddon() {
        super("shop");
    }
    
    @EventHandler
    public void click(final NPCRightClickEvent event) {
        if (event.getNPC().hasTrait((Class)NPCAddon.class)) {
            final Player player = event.getClicker();
            final Manager manager = new Manager();
            final Page page = manager.getPage(event.getNPC());
            final List<String> perms = manager.getCitizenPermissions(event.getNPC().getId());
            if (!perms.isEmpty()) {
                for (final String s : perms) {
                    if (!player.hasPermission(s)) {
                        return;
                    }
                }
            }
            if (page != null) {
                page.openPage(player);
            }
        }
    }
    
    @EventHandler
    public void click(final NPCRemoveEvent event) {
        Manager.get().setCitizenPage(event.getNPC().getId(), null);
    }
    
    public static void setCitizenPage(final int id, final String page) {
        if (page == null) {
            if (Manager.cnpcs.containsKey(id)) {
                Manager.cnpcs.remove(id);
            }
            return;
        }
        Manager.cnpcs.put(id, page);
    }
    
    public void onAttach() {
    }
    
    public void onDespawn() {
    }
    
    public void onSpawn() {
    }
    
    public void onRemove() {
    }
}
