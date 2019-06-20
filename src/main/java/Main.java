
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;

/*  done~~1. bot_token to be excluded from .java files and use file i/o
    done~converted to ArrayList~2. pop custom reaction arrays as vectors or use file i/o
        2a. make custom reactions writable
    3. embed
    4. moderating commands
        4a. kick
        4b. ban
        4c. mute
    5.
*/
public class Main extends ListenerAdapter {

    private static String bot_master;
    private static String token;
    private static ArrayList<String> promptlist = new ArrayList<>();
    private static ArrayList<String> answerlist = new ArrayList<>();
    private static ArrayList<String> credentials = new ArrayList<>();

    public static void main(String[] args) throws LoginException, IOException {

        //file input
        Path p1 = Paths.get("C:/Users/testerr/Documents/bot_info/credentials.txt");
        Path p2 = Paths.get("C:/Users/testerr/Documents/bot_info/customreactions.txt");
        List<String> details = Files.readAllLines(p1);
        List<String> c_react = Files.readAllLines(p2);
        //variable setup
        for(int i = 0;i<details.size();i++){
            credentials.add(details.get(i));
        }
        for(int i = 0;i<c_react.size();i=i+2){
            promptlist.add(c_react.get(i));
        }
        for(int i = 1;i<c_react.size();i=i+2){
            answerlist.add(c_react.get(i));
        }
        bot_master = credentials.get(1);
        token = credentials.get(3);

        //bot setup
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(getToken());
        builder.addEventListener(new Main());
        builder.setGame(Game.playing("TES VII: Oblivion 2"));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //ignores bot
        if(event.getAuthor().isBot()){
            return;
        }
        //guild variables
        Guild guild = event.getGuild();
        String guild_name = guild.getName();
        //user variables
        String human_id = event.getAuthor().getId();
        String human_name = event.getAuthor().getName();
        Member member = event.getMember();
        //message variables
        MessageChannel channel = event.getChannel();
        String channel_name = channel.getName();
        Message message = event.getMessage();
        String content = message.getContentRaw();
        String lowered = content.toLowerCase();

        //logger
        System.out.println("A human, " + human_name + " with ID " + human_id + " in " + channel_name + " of " + guild_name + " said \n" + content);

        //custom reactions
        for(int i = 0;i<promptlist.size();i++) {
            if (lowered.contains(promptlist.get(1))) {
                channel.sendMessage(answerlist.get(1)).queue();
                System.out.println("The bot said:\n" + answerlist.get(1));
                return;
            } else if (lowered.equals(promptlist.get(i))) {
                channel.sendMessage(answerlist.get(i)).queue();
                System.out.println("The bot said:\n" + answerlist.get(i));
            }
        }

        //special(?) commands
        if(lowered.equals("am i a bot master")){
            if (human_id.equals(bot_master)){
                channel.sendMessage("yes").queue();
                System.out.println("The bot said:\nYes");
                return;
            }
            else {
                channel.sendMessage("Imagine trying to master a bot <:pathetic:446632850880593922>").queue();
                return;
            }
        }
        //under development
        if(lowered.equals("%%help")){
            channel.sendMessage("Command is under development.").queue();

        }


        //admin commands
        if (lowered.startsWith("%%")){
        if(human_id.equals(getBot_master())){
            if(lowered.equals("%%sdb")){
                System.out.println("Shutting down b0ss");
                channel.sendMessage("Shutting down b0ss").queue();
                event.getJDA().shutdown();
                System.exit(0);
            }
            if (lowered.equals("%%fetch")){
                System.out.println(guild.getEmotes());
            }
            //under development
            if (lowered.equals("%%kick")){
                if(!member.hasPermission(Permission.KICK_MEMBERS)) {
                    channel.sendMessage("You don't have permission to kick people!").queue();
                    return;
                }
                List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
                if(mentionedMembers.isEmpty()) {
                    channel.sendMessage("You must mention who you want to be kicked").queue();
                    return;
                }
                guild.getController().kick(mentionedMembers.get(0)).queue();
                channel.sendMessage("Successfully kicked " + mentionedMembers.get(0).getUser().getName()).queue();


            }

        }
        else {
            String answer ="Error 403: Permission not Found\nInvoker is not a bot master";
            channel.sendMessage(answer).queue();
            System.out.println(answer);
        }
        }

    }

    public static String getBot_master(){
        return bot_master;
    }
    public static String getToken(){
        return token;
    }
}
