package conj.Shop.auto;

import conj.Shop.enums.*;
import org.bukkit.*;
import conj.Shop.base.*;
import conj.Shop.tools.*;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;
import java.util.*;
import org.apache.commons.io.*;
import java.io.*;
import conj.Shop.control.*;
import java.text.*;

public class Autobackup
{
    private static int id;
    
    public static void start() {
        if (Config.AUTOBACKUP.isActive()) {
            cancel();
            final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            Autobackup.id = scheduler.scheduleSyncRepeatingTask((Plugin)Initiate.getPlugin((Class)Initiate.class), (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!Config.AUTOBACKUP.isActive()) {
                        Autobackup.cancel();
                        Debug.log("Autobackup has been disabled.");
                        return;
                    }
                    Autobackup.create();
                }
            }, 0L, 72000L);
        }
    }
    
    public static void create() {
        final long start = System.currentTimeMillis();
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = Calendar.getInstance();
        final String date = dateFormat.format(cal.getTime());
        final File file = new File(String.valueOf(((Initiate)Initiate.getPlugin((Class)Initiate.class)).getDataFolder().getPath()) + "/data");
        final File backupfile = new File(String.valueOf(((Initiate)Initiate.getPlugin((Class)Initiate.class)).getDataFolder().getPath()) + "/backup/" + date);
        if (!backupfile.exists()) {
            backupfile.mkdir();
        }
        if (file.exists()) {
            try {
                FileUtils.copyDirectory(file, backupfile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        Debug.log("Backup took: " + Manager.getDuration(start));
    }
    
    public static void delete() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm");
        final Calendar cal = Calendar.getInstance();
        final String date = dateFormat.format(cal.getTime());
        final File backupfile = new File(String.valueOf(((Initiate)Initiate.getPlugin((Class)Initiate.class)).getDataFolder().getPath()) + "/backup/" + date);
        if (backupfile.exists()) {
            backupfile.delete();
        }
    }
    
    public static void cancel() {
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.cancelTask(Autobackup.id);
    }
}
