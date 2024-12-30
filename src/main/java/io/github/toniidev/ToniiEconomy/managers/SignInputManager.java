package io.github.toniidev.ToniiEconomy.managers;

import io.github.toniidev.ToniiEconomy.builders.SignInputFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class SignInputManager implements Listener {
    public static List<SignInputFactory> signInputSetups = new ArrayList<>();

    @EventHandler
    public void onSignChanged(SignChangeEvent e){
        SignInputFactory setup = signInputSetups.stream()
                .filter(x -> x.getPlayer().equals(e.getPlayer()))
                .findFirst().orElse(null);

        if(setup == null) return; // it means player doesn't have to be watched
        if(setup.getActionToExecute() == null) return; // it means that there isn't an action to execute

        String userInput = e.getLine(0);
        e.getPlayer().getLocation().clone().add(0, -2, 0).getBlock().setType(setup.getInitialBlock());
        setup.getActionToExecute().run(userInput, e.getPlayer());
        signInputSetups.remove(setup);
    }
}
