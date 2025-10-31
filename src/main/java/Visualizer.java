import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/*
TO USE VISUALIZER, CALL   printHand(List<String> hand);   WITH AN ARRAYLIST OF STRINGS AS AN ARGUMENT
ALL CARDS USE THE NOTATION OF 'value/suit' e.g. "AH" (Ace of Hearts), "KD" (Kind of Diamonds), OR "Blank" (blank)
 */
public class Visualizer{
    String[] cards;
    HashMap<String, Integer> cardIndex;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public Visualizer(){
        makeMap();
        try{
            makeCards();
        } catch (Exception e){ IO.print(e); }
    }

    public void printHand(List<String> hand){
        String result = "";
        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Boolean> isRed = new ArrayList<>();
        for(String card: hand){
            indexes.add(cardIndex.get(card));
            if ((card.charAt(1) == 'H') || (card.charAt(1) == 'D')) {
                isRed.add(true);
            } else {
                isRed.add(false);
            }
        }
        for(int i = 0; i < 11; i++){
            for (int j = 0; j < indexes.size(); j++) {
                int index = indexes.get(j);
                boolean index_is_red = isRed.get(j);
                if (index_is_red) {
                    result += ANSI_RED + cards[index].substring(i*17, (i*17)+17) + ANSI_RESET + "\t";
                } else {
                    result += cards[index].substring(i*17, (i*17)+17) + "\t";
                }
            }
            result += "\n";
        }
        IO.print(result);
    }

    public void printHand_with_hit(List<String> hand, int hit_state) {
        String result = "";
        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Boolean> isRed = new ArrayList<>();
        for(String card: hand){
            indexes.add(cardIndex.get(card));
            if ((card.charAt(1) == 'H') || (card.charAt(1) == 'D')) {
                isRed.add(true);
            } else {
                isRed.add(false);
            }
        }
        for(int i = 0; i < 11; i++){
            for (int j = 0; j < indexes.size(); j++) {
                int index = indexes.get(j);
                boolean index_is_red = isRed.get(j);
                if (index_is_red) {
                    result += ANSI_RED + cards[index].substring(i*17, (i*17)+17) + ANSI_RESET + "\t";
                } else {
                    result += cards[index].substring(i*17, (i*17)+17) + "\t";
                }
            }
            if (hit_state == 0) {
                result += hit_zero.substring(i*30, (i*30)+30);
            } else if (hit_state == 1){
                result += hit_one.substring(i*30, (i*30)+30);
            }
            result += "\n";
        }
        IO.print(result);
    }

    private void makeCards() throws IOException{
        cards = new String[53];
        BufferedReader br = new BufferedReader(new FileReader("./src/main/cards.txt"));
        for(int i = 0; i < cards.length; i++){
            cards[i] = "";
            for(int j = 0; j < 11; j++){
                cards[i] += br.readLine();
            }
        }
    }

    private void makeMap(){
        cardIndex = new HashMap<>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K"};
        String[] suits = {"H", "D", "C", "S"};
        int index = 0;
        for (String suit: suits){
            for (String value: values){
                cardIndex.put(value + suit, index);
                index++;
            }
        }
        cardIndex.put("Blank", index);
    }

    private static String hit_zero =    "                 __..--''     " +
                                        "           __.---'~~~         "  +
                                        "        .-'                   " +
                                        "      .'         __/          "  +
                                        "     /      .-'  \\ `-.        " +
                                        "    /   .  /\\   /  / /        "  +
                                        "   /    `/ /  /  /.~          " +
                                        "   \\      `-.______.-----.    " +
                                        "                        |~~ `." +
                                        "      .._          |___))     " +
                                        "         ~--._____.------'    " ;

    private static String hit_one =     "           __..--''  \\        " +
                                        " __.---'~~~           `.      " +
                                        "                        \\     " +
                                        "                __/      `.   " +
                                        "             .-' \\ `-.     \\  " +
                                        "         .  /\\   /  / /    /  " +
                                        "          `/ /  /  /.~    /   " +
                                        "            `-.______.-----.  " +
                                        "                        |~~ `." +
                                        " .._                    |___))" +
                                        "    ~--._____________.------' ";


    public void printTitle(){
        System.out.println(
                " /$$$$$$$  /$$                     /$$          /$$$$$                     /$$      \n" +
                "| $$__  $$| $$                    | $$         |__  $$                    | $$      \n" +
                "| $$  \\ $$| $$  /$$$$$$   /$$$$$$$| $$   /$$      | $$  /$$$$$$   /$$$$$$$| $$   /$$\n" +
                "| $$$$$$$ | $$ |____  $$ /$$_____/| $$  /$$/      | $$ |____  $$ /$$_____/| $$  /$$/\n" +
                "| $$__  $$| $$  /$$$$$$$| $$      | $$$$$$/  /$$  | $$  /$$$$$$$| $$      | $$$$$$/ \n" +
                "| $$  \\ $$| $$ /$$__  $$| $$      | $$_  $$ | $$  | $$ /$$__  $$| $$      | $$_  $$ \n" +
                "| $$$$$$$/| $$|  $$$$$$$|  $$$$$$$| $$ \\  $$|  $$$$$$/|  $$$$$$$|  $$$$$$$| $$ \\  $$\n" +
                "|_______/ |__/ \\_______/ \\_______/|__/  \\__/ \\______/  \\_______/ \\_______/|__/  \\__/\n" +
                "                                                                                    \n" +
                "                                                                                    \n"
        );
    }

}