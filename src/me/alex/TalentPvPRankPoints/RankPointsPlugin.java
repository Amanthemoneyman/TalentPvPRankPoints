package me.alex.TalentPvPRankPoints;

import com.google.common.collect.Maps;
import me.alex.TalentPvPRankPoints.Utilities.AccountAPI.RankPointHandler;
import me.alex.TalentPvPRankPoints.Utilities.FileAPI.FileManager;
import me.alex.TalentPvPRankPoints.Utilities.MessageAPI.Messages;
import me.alex.TalentPvPRankPoints.Utilities.Utilities;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.permission.Permission;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexm on 9/1/2017.
 */
public class RankPointsPlugin extends JavaPlugin{

    Permission perms;
    Economy econ;

    public HashMap<Integer, String> ranks = Maps.newHashMap();

    public void onEnable()
    {
        if(setupPermissions())
        {
            Utilities.debug("Sucessfully hooked with Vault permissions");

        } else
        {
            Utilities.error("Vault could not be hooked");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if(setupEconomy())
        {
            Utilities.info("Sucessfully hooked with Vault economy");
        } else
        {
            Utilities.error("Vault could not be hooked with economy, all features dealing with money are disabled");
        }
        loadClasses();


    }

    public void onDisable()
    {
        rph.saveChacheToFile();


    }

    private FileManager fm; public FileManager getFileManager() { return fm;}
    private RankPointHandler rph; public RankPointHandler getRankPointHandler() { return rph;}
    private Messages msgs; public Messages getMessages() { return msgs;}
    private void loadClasses()
    {
        fm = new FileManager(this);

        if(fm != null) {
            fm.loadFiles();
        } else
        {
            Utilities.debug("FileManager is null");
        }

        rph = new RankPointHandler(this);

        msgs = new Messages(this);

        Utilities.debug("" + rph.getBalance(Bukkit.getPlayer("Amanthemoneyman"), true));
        rph.addPoints(Bukkit.getPlayer("Amanthemoneyman"), 10000);
        Utilities.debug("" + rph.getBalance(Bukkit.getPlayer("Amanthemoneyman"), true));
        Utilities.debug("" + rph.canSpend(Bukkit.getPlayer("Amanthemoneyman"), 1000));
        rph.removePoints(Bukkit.getPlayer("Amanthemoneyman"), 1000);
        Utilities.debug("" + rph.getBalance(Bukkit.getPlayer("Amanthemoneyman"), true));



    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public int getRankDistance(String killerRank, String killedRank)
    {
        int returnInt = 0;
        int killerInt = 0;
        int killedInt = 0;

        if(ranks.isEmpty())
        {
            for(String ranknum : getFileManager().getPluginFile("ranks.yml").getConfig().getKeys(false))
            {
                if(ranknum != "BaseRank")
                {
                    try {
                        int rankNumber = Integer.parseInt(ranknum);
                        String s = getConfig().getString( rankNumber + ".Name");

                        Utilities.debug("RankNumber : " + ranknum + ", And Rank : " + s + " are loaded");
                        ranks.put(rankNumber, s);



                    }catch(Throwable e)
                    {
                        Utilities.error("The rank number " + ranknum + " is not an integer!");
                    }


                }

            }


        }

        for(Map.Entry entry : ranks.entrySet())
        {
            String rank = (String)entry.getValue();
            if(killedRank.equalsIgnoreCase(rank))
            {
                killedInt = (int)entry.getKey();


            }
            if(killerRank.equalsIgnoreCase(rank))
            {
                killerInt = (int)entry.getKey();


            }
            returnInt = killedInt-killerInt;
        }

        Utilities.debug("Returned : " + returnInt);


        if(returnInt < 0) return 0;
        return returnInt;
    }

    public int getRankDistance(Player killed, Player killer)
    {
        String killedRank = perms.getPrimaryGroup(killed);
        String killerRank = perms.getPrimaryGroup(killer);

        return getRankDistance(killerRank, killedRank);
    }
}
