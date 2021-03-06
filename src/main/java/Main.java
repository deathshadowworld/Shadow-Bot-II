
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;

/*  done~~1. bot_token to be excluded from .java files and use file i/o
    done~converted to ArrayList~2. pop custom reaction arrays as vectors or use file i/o
        2.a. make custom reactions writable
            2.a.i.  accept %% commands and turn to array of 3 elements
            2.a.ii. append array to file
    3. embed
    4. moderating commands
        4.a. kick
        4.b. ban
        4.c. mute
    5.
*/
public class Main extends ListenerAdapter {

        private static String bot_master, token;
        private static String cred_path = "C:\\Users\\USER\\Documents\\bot_info\\credentials.txt";
        private static String react_path = "C:\\Users\\USER\\Documents\\bot_info\\customreactions.txt";
        private static String pm_path = "C:\\Users\\USER\\Documents\\bot_info\\privatemessages.txt";
        private static ArrayList<String> promptlist = new ArrayList<>();
        private static ArrayList<String> answerlist = new ArrayList<>();
        private static ArrayList<String> prompt_pm = new ArrayList<>();
        private static ArrayList<String> answer_pm = new ArrayList<>();
        private static ArrayList<String> credentials = new ArrayList<>();
        private static BufferedWriter writer;
        private static BufferedWriter writer1;
        private static String content, lowered;

    public static void main(String[] args) throws LoginException, IOException {

        //file input
        Path p1 = Paths.get(cred_path);
        Path p2 = Paths.get(react_path);
        Path p3 = Paths.get(pm_path);
        List<String> details = Files.readAllLines(p1);
        List<String> c_react = Files.readAllLines(p2);
        List<String> pm_react = Files.readAllLines(p3);
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
        for(int i = 0;i<pm_react.size();i=i+2){
            prompt_pm.add(pm_react.get(i));
        }
        for(int i = 1;i<pm_react.size();i=i+2){
            answer_pm.add(pm_react.get(i));
        }
        bot_master = credentials.get(1);
        token = credentials.get(3);
        writer1 = new BufferedWriter(new FileWriter(react_path, true));
        writer = new BufferedWriter(new FileWriter(pm_path, true));
        //bot setup
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(getToken());
        builder.addEventListener(new Main());
        builder.setGame(Game.playing("TES VII: Oblivion 2"));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //ignores bot
        if (event.getAuthor().isBot()) {
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
        content = message.getContentRaw();
        lowered = content.toLowerCase();


        //logger
        System.out.println("A human, " + human_name + " in " + channel_name + " of " + guild_name + " said \n" + content);

        //custom reactions
        for (int i = 0; i < promptlist.size(); i++) {
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
        //check user for authority
        if (lowered.equals("am i a bot master")) {
            if (human_id.equals(getBot_master())) {
                channel.sendMessage("yes").queue();
                System.out.println("The bot said:\nYes");
                return;
            } else {
                channel.sendMessage("Imagine trying to master a bot <:pathetic:446632850880593922>").queue();
                return;
            }
        }
        //help command
        //under development
        if (lowered.equals("%%help")) {
            channel.sendMessage("Command is under development.").queue();

        }


        //admin commands
        if (lowered.startsWith("%%")) {
            if (human_id.equals(getBot_master())) {
                //bot shut down
                if (lowered.equals("%%sdb")) {
                    System.out.println("Shutting down b0ss");
                    channel.sendMessage("Shutting down b0ss").queue();
                    event.getJDA().shutdown();
                    System.exit(0);
                }
                //fetch emotes from a guild
                if (lowered.equals("%%fetch")) {
                    System.out.println(guild.getEmotes());
                }
                //add new reaction to list
                if (lowered.startsWith("%add")) {
                    //writeReactiontoFile();
                    channel.sendMessage("cant use yet").queue();

                } else {
                    String answer = "Error 403: Permission not Found\nInvoker is not a bot master";
                    channel.sendMessage(answer).queue();
                    System.out.println(answer);
                }
            }
        }
    }
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event){
        //ignores bot
        if (event.getAuthor().isBot()) {
            return;
        }
        //user variables
        String human_id = event.getAuthor().getId();
        String human_name = event.getAuthor().getName();
        //message variables
        MessageChannel channel = event.getChannel();
        String channel_name = channel.getName();
        Message message = event.getMessage();
        content = message.getContentRaw();
        lowered = content.toLowerCase();

        //logger
        System.out.println("A human, " + human_name + " in " + channel_name  + " said \n" + content);

        //custom reactions
        for (int i = 0; i < prompt_pm.size(); i++) {
            if (lowered.equals(prompt_pm.get(i))) {
                channel.sendMessage(answer_pm.get(i)).queue();
                System.out.println("The bot said:\n" + answer_pm.get(i));
            }
        }
    }

    public boolean writeCReactiontoFile()throws IOException{
        ArrayList<String> add = new ArrayList<>();
        String[] words = content.split("\"+");

        for(int i = 0;i<(words.length);i++){
            add.add(words[i]);
        }
        add.remove(0);
        for( int i = 0; i<add.size();i++){
            writer1.append(System.lineSeparator());
            writer1.append(add.get(i));
        }
        writer1.close();
        return true;
    }
    public boolean writepmReactiontoFile()throws IOException{
        ArrayList<String> add = new ArrayList<>();
        String[] words = content.split("\"+");

        for(int i = 0;i<(words.length);i++){
            add.add(words[i]);
        }
        add.remove(0);
        for( int i = 0; i<add.size();i++){
            writer.append(System.lineSeparator());
            writer.append(add.get(i));
        }
        writer.close();
        return true;
    }

    public static String getBot_master(){
        return bot_master;
    }
    public static String getToken(){
        return token;
    }

}
