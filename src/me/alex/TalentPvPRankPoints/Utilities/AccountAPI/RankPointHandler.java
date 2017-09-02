package me.alex.TalentPvPRankPoints.Utilities.AccountAPI;

import com.google.common.collect.Maps;
import me.alex.TalentPvPRankPoints.RankPointsPlugin;
import me.alex.TalentPvPRankPoints.Utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by alexm on 9/2/2017.
 */
public class RankPointHandler implements Listener{

    private HashMap<UUID, Double> accountChache;
    private RankPointsPlugin plugin;

    public RankPointHandler(RankPointsPlugin rpp) {
        accountChache = Maps.newHashMap();
        this.plugin = rpp;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        updateCache();

        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                saveChacheToFile();
            }
        }, 20 * 60 * 5);//Every 5 minutes


    }

    /**
     * @param p - Who's balance to get
     * @param b - Check file if not present in chache
     * @return
     */
    public Double getBalance(Player p, boolean b) {
        if (accountChache.get(p.getUniqueId()) != null) {
            return (double) accountChache.get(p.getUniqueId());


        } else if (b) {
            if (plugin.getFileManager() != null) {
                if (plugin.getFileManager().getPluginFile("data.yml") != null) {
                    for (String s : plugin.getFileManager().getPluginFile("data.yml").getConfig().getKeys(false))
                    {
                        if(s.equals(p.getUniqueId()))
                        {
                            accountChache.put(p.getUniqueId(), plugin.getFileManager().getPluginFile("data.yml").getConfig().getDouble(s));
                        }


                    }


                } else
                {
                    Utilities.error("data.yml either does not exist or has not been loaded correctly.");
                }

            } else
            {
                Utilities.error("FileManager is null!");
            }


        }
        return (double)0;
    }

    public void updateCache()
    {
        if (plugin.getFileManager() != null) {
            if (plugin.getFileManager().getPluginFile("data.yml") != null) {
                accountChache.clear();
                for (String s : plugin.getFileManager().getPluginFile("data.yml").getConfig().getKeys(false))
                {
                    accountChache.put(UUID.fromString(s), plugin.getFileManager().getPluginFile("data.yml").getConfig().getDouble(s));


                }


            } else
            {
                Utilities.error("data.yml either does not exist or has not been loaded correctly.");
            }

        } else
        {
            Utilities.error("FileManager is null!");
        }


    }

    public void saveChacheToFile()
    {
        for(Map.Entry e : accountChache.entrySet())
        {
            plugin.getFileManager().getPluginFile("data.yml").getConfig().set(e.getKey().toString(), e.getValue());


        }
        plugin.getFileManager().getPluginFile("data.yml").saveFile();

        Utilities.debug("Saved data.yml");


    }

    /**
     *
     * @param p - Players account you are seeking
     * @param b - Check file if not present in chache
     * @return
     */
    public boolean hasAccount(Player p, boolean b)
    {
        if(accountChache.containsKey(p.getUniqueId()))
        {
            return true;
        }
        if(b)
        {
            if(plugin.getFileManager().getPluginFile("data.yml").getConfig().contains(p.getUniqueId().toString()))
            {
                accountChache.put(p.getUniqueId(), plugin.getFileManager().getPluginFile("data.yml").getConfig().getDouble(p.getUniqueId().toString()));
                return true;
            }

        }

        return false;


    }

    private void createAccount(Player p)
    {
        if(!hasAccount(p, true))
        {
            plugin.getFileManager().getPluginFile("data.yml").getConfig().set(p.getUniqueId().toString(), (double)0);
            plugin.getFileManager().getPluginFile("data.yml").saveFile();

        }

    }

    public void addPoints(Player p, double points)
    {
        double before = accountChache.get(p.getUniqueId());
        accountChache.replace(p.getUniqueId(), before + points);

    }

    public void removePoints(Player p, double points)
    {
        double before = accountChache.get(p.getUniqueId());
        accountChache.replace(p.getUniqueId(), before - points);
    }

    /**
     *
     * @param p
     * @param points - The amount of points the player is attempting to spend
     */
    public boolean canSpend(Player p, double points) {
        if (accountChache.get(p.getUniqueId()) >= points)
        {
            return true;
        }
        return false;
    }



    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        if(!hasAccount(e.getPlayer(), true))
        {

            createAccount(e.getPlayer());
            Utilities.debug("Created account for player : " + e.getPlayer().getUniqueId());


        }


    }

    @EventHandler
    public void onKill(PlayerDeathEvent e)
    {
        if(e.getEntity().getKiller() instanceof Player)
        {
            Player killer = e.getEntity().getKiller();
            Player killed = e.getEntity();


        }

    }
}
