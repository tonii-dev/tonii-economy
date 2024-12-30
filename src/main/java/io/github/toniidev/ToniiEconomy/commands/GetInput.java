package io.github.toniidev.ToniiEconomy.commands;

import io.github.toniidev.ToniiEconomy.builders.SignInputFactory;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiSignInterface;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GetInput implements CommandExecutor {
    private Plugin p;
    public GetInput(Plugin main){
        p = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        new SignInputFactory((Player) commandSender, "^^^^^^^", "metti il tuo nome")
                .setActionToExecute(new ToniiSignInterface() {
                    @Override
                    public void run(String input, Player player){
                        player.sendMessage(input);
                    }
                })
                .showSign(p);
        return true;
    }
}
