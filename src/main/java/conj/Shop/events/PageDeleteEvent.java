package conj.Shop.events;

import org.bukkit.event.*;
import conj.Shop.data.*;

public class PageDeleteEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private Page page;
    private boolean cancelled;
    
    static {
        handlers = new HandlerList();
    }
    
    public PageDeleteEvent(final Page page) {
        this.page = page;
    }
    
    public Page getPage() {
        return this.page;
    }
    
    public HandlerList getHandlers() {
        return PageDeleteEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PageDeleteEvent.handlers;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
