package me.alex.TalentPvPRankPoints.Utilities.FileAPI;

import me.alex.TalentPvPRankPoints.RankPointsPlugin;
import me.alex.TalentPvPRankPoints.Utilities.Utilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 7/14/2017.
 */
public class PluginFile extends File{

    protected FileConfiguration config;
    protected FileType type;
    protected RankPointsPlugin plugin;
    private FileManager fm;

    public PluginFile(File pluginFile, RankPointsPlugin rpp)
    {
        super(rpp.getDataFolder(),pluginFile.getName());
        this.plugin = plugin;
        if (!this.exists()) {
                if (!this.getName().endsWith(".yml")) {

                    if(this.getName().endsWith(".txt"))
                    {
                        type = FileType.TXT;
                    } else { type = FileType.OTHER; }
                    mkdir();
                } else {
                    plugin.saveResource(getName(), false);
                }
        }

        if (this.getName().endsWith(".yml"))
        {
            type = FileType.YML;
            loadFileConfiguration();
            this.saveFile();
        }


    }

    public PluginFile(String fileName, RankPointsPlugin rpp)
    {
        super(rpp.getDataFolder(),fileName);
        this.plugin = rpp;
        this.fm = plugin.getFileManager();
        if (!this.exists()) {
                if (!this.getName().endsWith(".yml")) {

                    if(this.getName().endsWith(".txt"))
                    {
                        type = FileType.TXT;
                    } else { type = FileType.OTHER; }
                    mkdir();
                } else {
                    plugin.saveResource(this.getName(), false);
                }
        }

        if (this.getName().endsWith(".yml"))
            type = FileType.YML;
        this.loadFileConfiguration();
        this.saveFile();

    }


    public void saveFile()
    {
        try {
            this.config.save(this);
            Utilities.debug(this.getName() + " has been saved");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void loadFileConfiguration()
    {
        if(this.getType() == FileType.YML) {
            try {

                config = YamlConfiguration.loadConfiguration(this);

            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }



    }

    public FileType getType()
    { return this.type; }

    private void addToManager()
    {
        this.plugin.getFileManager().addPluginFile(this);


    }

    public FileConfiguration getConfig() {
        return config;
    }
}
