package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.Core;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.util.UtilParticles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * Created by sacha on 10/08/15.
 */
public class GadgetAntiGravity extends Gadget {

    ArmorStand as;
    boolean running;


    public GadgetAntiGravity(UUID owner) {
        super(Material.EYE_OF_ENDER, (byte) 0x0, "AntiGravity", "ultracosmetics.gadgets.antigravity", 30, owner, GadgetType.ANTIGRAVITY, "&7&oIf you do not like gravity\n&7&oThis gadget is made for you!");
        Core.registerListener(this);
    }

    @Override
    void onInteractRightClick() {
        as = getPlayer().getWorld().spawn(getPlayer().getLocation(), ArmorStand.class);
        as.setGravity(false);
        as.setSmall(true);
        running = true;
        as.setVisible(false);
        as.setHelmet(new ItemStack(Material.SEA_LANTERN));
        Bukkit.getScheduler().runTaskLater(Core.getPlugin(), new Runnable() {
            @Override
            public void run() {
                as.remove();
                as = null;
                Bukkit.getScheduler().runTaskLater(Core.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        running = false;
                    }
                }, 20);
            }
        }, 220);
    }

    @Override
    void onInteractLeftClick() {

    }

    @Override
    void onUpdate() {
        if (as != null && as.isValid()) {
            as.setHeadPose(as.getHeadPose().add(0, 0.1, 0));
            UtilParticles.play(Particles.PORTAL, 3f, 3f, 3f, as.getLocation(), 150);
            UtilParticles.play(Particles.SPELL_WITCH, .3f, .3f, .3f, as.getEyeLocation(), 5);
            for (Entity ent : as.getNearbyEntities(3, 2, 3)) {
                if (ent instanceof LivingEntity && !(ent instanceof ArmorStand))
                    MathUtils.applyVelocity(ent, new Vector(0, 0.05, 0));
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        try {
            if (running) {
                if (as != null && as.isValid() && event.getReason().contains("Fly")) {
                    if (as.getLocation().distance(event.getPlayer().getLocation()) < 8) {
                        event.setCancelled(true);
                        System.out.println("UltraCosmetics >> Cancelling invalid Flight KicK.");
                        return;
                    }
                }
                event.setCancelled(true);
                System.out.println("UltraCosmetics >> Cancelling invalid Flight KicK.");
                return;
            }
        } catch (Exception exc) {
        }
    }

    @Override
    public void onClear() {
        if (as != null)
            as.remove();
        HandlerList.unregisterAll(this);
    }
}