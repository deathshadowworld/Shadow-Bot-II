
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Main extends ListenerAdapter {
    private static String bot_master = "259999538666930177";
    private static String token = "NTkwMTgyODE2ODIyNzIyNTYy.XQehYA.P0dm1rSOkYZK-DGzSsvpxpzTjfs";

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);

        builder.setToken(token);
        builder.addEventListener(new Main());
        builder.setGame(Game.playing("TES VII: Oblivion 2"));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        if(event.getAuthor().isBot()){
            return;
        }
        Guild guild = event.getGuild();
        String guild_name = event.getGuild().getName();

        String human_id = event.getAuthor().getId();
        String human_name = event.getAuthor().getName();
        Member member = event.getMember();

        MessageChannel channel = event.getChannel();
        String channel_name = event.getChannel().getName();


        Message message = event.getMessage();
        String content = message.getContentRaw();
        String lowered = content.toLowerCase();

        System.out.println("A human, " + human_name + " with ID " + human_id + " in " + channel_name + " said \n" + content);

        //custom reactions
        String[] promptlist = {
                "loli",
                "@everyone",
                "who is your master",
                "who am i",
                "what server is this",
                "gay",
                "!help",
                "nanachi",
                "skrrt",

        };
        String[] answerlist = {
                "u r under arrest",
                "https://cdn.discordapp.com/attachments/355198025511075855/515868932616355851/Pin.jpg",
                "He is <@259999538666930177>",
                "You're <@"+human_id+">",
                "This is "+guild_name,
                "no u",
                "Command List:\n1. Nothing\n2. As\n3. Of\n4. Yet",
                "<:whaa:446632487331037184>",
                "<:wheelie:590517783851433985>",

        };
        for(int i=0;i<promptlist.length;i++){
            if (lowered.contains(promptlist[1])){
                channel.sendMessage(answerlist[1]).queue();
                System.out.println("The bot said:\n"+answerlist[1]);
            }
            else if (lowered.equals(promptlist[i])){
                channel.sendMessage(answerlist[i]).queue();
                System.out.println("The bot said:\n"+answerlist[i]);
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


        //admin commands
        if (lowered.startsWith("%")){
        if(human_id.equals(bot_master)){
            if(lowered.equals("%sdb")){
                System.out.println("Shutting down b0ss");
                channel.sendMessage("Shutting down b0ss").queue();
                event.getJDA().shutdown();
                System.exit(0);
            }
            if (lowered.equals("%fetch")){
                System.out.println(guild.getEmotes());
            }

        }
        else {
            channel.sendMessage("Error 403: Permission not Found\nInvoker is not a bot master").queue();
        }
        }

    }

    public String getMaster_ID(){
        return bot_master;
    }
    public String getToken(){
        return token;
    }
}
