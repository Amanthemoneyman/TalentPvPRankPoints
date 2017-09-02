package me.alex.TalentPvPRankPoints.Utilities.MessageAPI;

import com.google.common.collect.Maps;
import me.alex.TalentPvPRankPoints.RankPointsPlugin;
import me.alex.TalentPvPRankPoints.Utilities.FileAPI.PluginFile;
import me.alex.TalentPvPRankPoints.Utilities.Utilities;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexm on 9/1/2017.
 */
public class Messages {

    HashMap<String,String> messages;
    RankPointsPlugin plugin;
    Boolean loaded;
    public Messages(RankPointsPlugin rpp)
    {
        plugin = rpp;
        messages = Maps.newHashMap();
        loadMessages();

    }

    public String getMessage(String ID)
    {
        return (messages.get(ID) != null ? messages.get(ID) : "&cMessage \"&e" + ID + "&c\" could not be loaded");

    }

    private void loadMessages()
    {
        if(plugin.getFileManager() != null)
        {

            if(plugin.getFileManager().getPluginFiles() != null)
            {

                for(PluginFile pf : plugin.getFileManager().getPluginFiles())
                {

                    if(pf.getName().contains("messages"))
                    {

                        for(String s : pf.getConfig().getKeys(false)) {
                            if (pf.getConfig().getString(s) != null)
                            {
                                messages.put(s,pf.getConfig().getString(s));

                            } else
                            {
                                Utilities.error("&cMessage &e" + s + " &cis null and has not been loaded");
                            }

                        }

                    }


                }


            } else
            {
                Utilities.error("Unable to read files, they have not been loaded.");


            }


        } else
        {
            Utilities.error("FileManager has not been initiated, Error comming from Messages.class");


        }

        loaded = true;
    }
}
