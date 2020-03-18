package bettercommands.commands.bcommands;

public interface ISubCommand {
    
    void execute();

    void sendMessage(String section, Object... replacements);
    
    boolean hasPermission();
    
    boolean hasPermission(String permission);
    
    boolean isPlayerRequired();
    
    boolean argIsInteger(String arg);
    
    boolean isCorrectUsage();

    boolean isWorldExist(String name);
    
}
