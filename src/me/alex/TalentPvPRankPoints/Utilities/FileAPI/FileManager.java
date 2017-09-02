package me.alex.TalentPvPRankPoints.Utilities.FileAPI;


import me.alex.TalentPvPRankPoints.RankPointsPlugin;
import me.alex.TalentPvPRankPoints.Utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 7/15/2017.
 */
public class FileManager {

    private RankPointsPlugin plugin;
    private ArrayList<PluginFile> files;
    private final List<String> preMadeFiles = Arrays.asList("config.yml", "messages.yml", "data.yml", "ranks.yml");

    public FileManager(RankPointsPlugin rpp) {
        this.plugin = rpp;
        files = new ArrayList<PluginFile>();
        Utilities.debug("Loading server files");



    }

    public void loadFiles() {
        if(!plugin.getDataFolder().exists())
        {
            plugin.getDataFolder().mkdir();
        }
        for(String fname : preMadeFiles)
        {
            boolean exists = new File(plugin.getDataFolder(), fname).exists();
            if(!exists)
            {
                Utilities.debug(" File : " + fname + " did not exist! Creating new..." );
                this.files.add(new PluginFile(fname, plugin));
                Utilities.debug("Complete");


            }
        }



        File[] files = plugin.getDataFolder().listFiles();

        for(File f: files)
        {
            if(f == null)
            {
                Utilities.debug("File " + f.getName() + " Is null");
            }
            if(plugin == null)
            {

                Utilities.debug("Plugin is null");

            }

            if(!this.isPluginFile(f.getName())) {

                PluginFile pf = new PluginFile(f, plugin);
                this.files.add(pf);
                Utilities.debug(" Loaded file : " + pf.getName());
            } else
            {
                Utilities.debug("File :" + f.getName() + " is already loaded");


            }


        }


    }

    public ArrayList<PluginFile> getPluginFiles()
    {
        return this.files;
    }

    public File getFile(String name) {
        for (File f : files) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }


        }
        return null;
    }

    public boolean isPluginFile(String name)
    {
        for(PluginFile f : getPluginFiles())
        {
            if(f.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;

    }

    public boolean isPluginFile(PluginFile pf)
    {
        if(getPluginFiles().contains(pf)) return true;
        return false;


    }

    public void addPluginFile(PluginFile pf)
    {
        if(isPluginFile(pf))
        {
            return;
        }
        files.add(pf);


    }

    public PluginFile getPluginFile(String name)
    {
        for(PluginFile pf : getPluginFiles())
        {
            if(pf.getName().equalsIgnoreCase(name))
            {
                return pf;
            }
        }
        return null;
    }

    public void deletePluginFile(PluginFile pf)
    {
        if(isPluginFile(pf))
        {
            this.files.remove(pf);
            pf.delete();

        }

    }


}
