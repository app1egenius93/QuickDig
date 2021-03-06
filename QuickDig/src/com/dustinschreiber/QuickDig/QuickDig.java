package com.dustinschreiber.QuickDig;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
 
public final class QuickDig extends JavaPlugin implements Listener{
	
	double pluginVersion = 1.0;
	Map<String, Boolean> playersActive = new HashMap<String, Boolean>();
    List<Integer> allowedTools = new ArrayList<Integer>();
    
    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(this, this);
	    	getLogger().info("[WELCOME] QuickDig " + pluginVersion + " enabled!");
	    	allowedTools.add(257);
	    	allowedTools.add(270);
	    	allowedTools.add(274);
	    	allowedTools.add(278);
	    	allowedTools.add(285);
	    	allowedTools.add(256);
	    	allowedTools.add(268);
	    	allowedTools.add(273);
	    	allowedTools.add(277);
	    	allowedTools.add(284);
    }
 
    @Override
    public void onDisable() {
        getLogger().info("[BYE] QuickDig " + pluginVersion + " disabled!");
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	    	if(cmd.getName().equalsIgnoreCase("quick")){
	    		if(!(sender instanceof Player)){
	    			sender.sendMessage("QuickDig can only be used by a player.");
	    		}else{
	    			Player player = (Player) sender;
	    			if(playersActive.containsKey(player.getName())){
	    				sender.sendMessage(ChatColor.GREEN + "QuickDig inactive");
	    				playersActive.remove(player.getName());
	    				return true;
	    			}else{
		    			if(player.hasPermission("quickdig.use")){
		    	    		sender.sendMessage(ChatColor.GREEN + "QuickDig active!");
		    	    		playersActive.put(player.getName(), true);
		    	    		return true;
		    			}
	    			}
	    		}
	    	}
	    	return false;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
    		Player player = event.getPlayer();
		int itemID = player.getItemInHand().getTypeId();
		boolean allowed = false;
		if(playersActive.containsKey(player.getName())){
	    		switch(itemID){
	    			case 256 : if(player.hasPermission("quickdig.iron.shovel")){ allowed = true; } break;
	    			case 257 : if(player.hasPermission("quickdig.iron.pickaxe")){ allowed = true; } break;
	    			case 258 : if(player.hasPermission("quickdig.iron.axe")){ allowed = true; } break;
	    			case 269 : if(player.hasPermission("quickdig.wooden.shovel")){ allowed = true; } break;
	    			case 270 : if(player.hasPermission("quickdig.wooden.pickaxe")){ allowed = true; } break;
	    			case 271 : if(player.hasPermission("quickdig.wooden.axe")){ allowed = true; } break;
	    			case 273 : if(player.hasPermission("quickdig.stone.shovel")){ allowed = true; } break;
	    			case 274 : if(player.hasPermission("quickdig.stone.pickaxe")){ allowed = true; } break;
	    			case 275 : if(player.hasPermission("quickdig.stone.axe")){ allowed = true; } break;
	    			case 277 : if(player.hasPermission("quickdig.diamond.shovel")){ allowed = true; } break;
	    			case 278 : if(player.hasPermission("quickdig.diamond.pickaxe")){ allowed = true; } break;
	    			case 279 : if(player.hasPermission("quickdig.diamond.axe")){ allowed = true; } break;
	    			case 284 : if(player.hasPermission("quickdig.gold.shovel")){ allowed = true; } break;
	    			case 285 : if(player.hasPermission("quickdig.gold.pickaxe")){ allowed = true; } break;
	    			case 286 : if(player.hasPermission("quickdig.gold.axe")){ allowed = true; } break;
	    			default : allowed = false; break;
	    		}
	    		if(allowed == false){
	    			return;
	    		}
	    		Location loc = event.getBlock().getLocation();
	    		int i = 0;
	    		String dir = getCardinalDirection(player);
	    		if(!dir.isEmpty()){
		    		while(i < 4){
			    		switch(dir){
				    		case "N" : loc = new Location(player.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1, loc.getYaw(), loc.getPitch()); break;
				    		case "E" : loc = new Location(player.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()); break;
				    		case "S" : loc = new Location(player.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1, loc.getYaw(), loc.getPitch()); break;
				    		case "W" : loc = new Location(player.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()); break;
				    		default: break;
			    		}
			    		Block block = player.getWorld().getBlockAt(loc);
		    			if(block.getDrops(player.getItemInHand()).isEmpty() == false){
		    				if(block.getType() != Material.BEDROCK){
		    					block.breakNaturally(player.getItemInHand());
				                player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() + 2));
				                i++;	
		    				}else{
		    					break;
		    				}
			    		}else{
			    			break;
			    		}
		    		}
	    		}
		}
    }
    
    public static String getCardinalDirection(Player player) {
	    double rotation = (player.getLocation().getYaw() -180) % 360;
	    if (rotation < 0) {
	    	rotation += 360.0;
	    }
	    if(0 <= rotation && rotation < 22.5){
	    return "N";
	    }else if(67.5 <= rotation && rotation < 112.5){
	    return "E";
	    }else if(157.5 <= rotation && rotation < 202.5){
	    return "S";
	    }else if(247.5 <= rotation && rotation < 292.5){
	    return "W";
	    }else if(337.5 <= rotation && rotation < 360.0){
	    return "N";
	    }else{
	    return null;
	    }
    }
}