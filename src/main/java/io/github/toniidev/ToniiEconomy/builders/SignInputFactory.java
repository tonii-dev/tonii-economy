package io.github.toniidev.ToniiEconomy.builders;

import io.github.toniidev.ToniiEconomy.interfaces.ToniiSignInterface;
import io.github.toniidev.ToniiEconomy.managers.SignInputManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignInputFactory {
    private final Player player;
    private final List<String> signLines;

    private Material initialBlock;
    private ToniiSignInterface actionToExecute;

    public SignInputFactory(Player serverPlayer, String... lines){
        player = serverPlayer;
        signLines = Arrays.asList(lines);
        SignInputManager.signInputSetups.add(this);
    }

    public SignInputFactory(Player serverPlayer){
        player = serverPlayer;
        signLines = new ArrayList<>();
        SignInputManager.signInputSetups.add(this);
    }

    public SignInputFactory setLine(int i, String s){
        signLines.set(i, s);
        return this;
    }

    /**
     * To call only if signLines is already set
     */
    public void showSign(Plugin plugin){
        // set a sign at x = playerx, y = 254, z = playerz
        Location loc = player.getLocation().clone();
        loc.setY(loc.getY() - 2);
        initialBlock = loc.getBlock().getType();
        loc.getBlock().setType(Material.OAK_SIGN);

        Sign sign = (Sign) loc.getBlock().getState();
        sign.setWaxed(false);
        sign.update();

        sign.getSide(Side.FRONT).setColor(DyeColor.BLACK); // TODO: find a better color
        for(int i = 0; i < signLines.size(); i++)
            sign.getSide(Side.FRONT).setLine(i + 1, signLines.get(i));

        sign.setWaxed(false);
        sign.update();

        Bukkit.getScheduler().runTaskLater(plugin, () -> player.openSign(sign), 2L);
    }

    public Player getPlayer() {
        return player;
    }

    public List<String> getSignLines() {
        return signLines;
    }

    public Material getInitialBlock() {
        return initialBlock;
    }

    public ToniiSignInterface getActionToExecute() {
        return actionToExecute;
    }

    public SignInputFactory setActionToExecute(ToniiSignInterface actionToExecute) {
        this.actionToExecute = actionToExecute;
        return this;
    }
}
