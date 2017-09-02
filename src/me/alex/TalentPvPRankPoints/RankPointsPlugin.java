package me.alex.TalentPvPRankPoints;

import me.alex.TalentPvPRankPoints.Utilities.FileAPI.FileManager;
import me.alex.TalentPvPRankPoints.Utilities.FileAPI.PluginFile;
import me.alex.TalentPvPRankPoints.Utilities.MessageAPI.Messages;
import me.alex.TalentPvPRankPoints.Utilities.Utilities;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by alexm on 9/1/2017.
 */
public class RankPointsPlugin extends JavaPlugin{

    public void onEnable()
    {
        loadClasses();


    }

    public void onDisable()
    {



    }

    private FileManager fm; public FileManager getFileManager() { return fm;}
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

        msgs = new Messages(this);



    }
}
