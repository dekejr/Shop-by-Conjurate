package conj.Shop.enums;

import java.util.*;
import org.bukkit.*;

public enum MessageType
{
    COOLDOWN("COOLDOWN", 0, "&cYou're currently on cooldown."), 
    PERMISSION("PERMISSION", 1, "&cYou don't have enough permission to access this item.");
    
    private String def;
    
    private MessageType(final String s, final int n, final String def) {
        this.def = def;
    }
    
    public List<String> getDefault() {
        final List<String> list = new ArrayList<String>();
        list.add(ChatColor.translateAlternateColorCodes('&', this.def));
        return list;
    }
    
    public static MessageType fromString(String string) {
        MessageType type = MessageType.COOLDOWN;
        string = string.replaceAll(" ", "_");
        string = string.toUpperCase();
        type = valueOf(string);
        return type;
    }
}
