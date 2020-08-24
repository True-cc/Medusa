package com.gladurbad.medusa.check;

import com.gladurbad.medusa.Medusa;
import com.gladurbad.medusa.manager.AlertManager;
import com.gladurbad.medusa.network.Packet;
import com.gladurbad.medusa.playerdata.PlayerData;

import io.github.retrooper.packetevents.packet.PacketType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Listener;

public abstract class Check implements Listener {

    protected final PlayerData data;
    @Getter
    private int vl;
    protected double buffer;
    @Getter
    @Setter
    protected Location lastLegitLocation;

    public Check(PlayerData data) {
        this.data = data;
        Bukkit.getServer().getPluginManager().registerEvents(this, Medusa.getMedusa());
    }

    public abstract void handle(final Packet packet);

    public CheckInfo getCheckInfo() {
        return this.getClass().getAnnotation(CheckInfo.class);
    }

    protected void fail() {
        ++vl;
        AlertManager.verbose(data, this);
    }

    protected void failAndSetback() {
        ++vl;
        AlertManager.verbose(data, this);
        data.getPlayer().teleport(lastLegitLocation);
        data.setLastSetbackTime(System.currentTimeMillis());
        buffer = 0;
    }

    protected void increaseBuffer() {
        buffer = Math.min(100, buffer + 1);
    }

    protected void decreaseBuffer() {
        buffer = Math.max(0, buffer - 1);
    }

    protected void increaseBufferBy(int amount) {
        buffer = Math.min(100, buffer + amount);
    }

    protected void decreaseBufferBy(int amount) {
        buffer = Math.max(0, buffer - amount);
    }

    protected void multiplyBuffer(double multiplier) {
        buffer *= multiplier;
    }

    protected boolean isFlyingPacket(Packet packet) {
        return PacketType.Client.Util.isInstanceOfFlying(packet.getPacketId());
    }

    protected long now() {
        return System.currentTimeMillis();
    }


    protected void debug(String info){ Bukkit.broadcastMessage(ChatColor.AQUA + "Debug: " + ChatColor.RESET + info); }
    protected void debug(double info){ Bukkit.broadcastMessage(ChatColor.AQUA + "Debug: " + ChatColor.RESET + info); }
    protected void debug(long info){ Bukkit.broadcastMessage(ChatColor.AQUA + "Debug: " + ChatColor.RESET + info); }
    protected void debug(boolean info){ Bukkit.broadcastMessage(ChatColor.AQUA + "Debug: " + ChatColor.RESET + info); }
    protected void debugPerPlayer(String info) { data.getPlayer().sendMessage(ChatColor.AQUA + "Debug: " + ChatColor.RESET + info);}

}