import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class BlackJack {

    // private int currentBet;
    private static String DOUBLE = "double";
    private static String HIT = "hit";
    private static String STAND = "stand";
    private static String SPLIT = "split";

    private static int getPlayerBet(Scanner scan, int playerCash, int min_bet_amount) {
        boolean invalidInput = true;

        //Scanner scan = new Scanner(System.in);
        int bet = min_bet_amount;
        do {
            System.out.println(String.format("Available Cash: %d", playerCash));
            System.out.print("Bet amount : ");
            String input = scan.nextLine();
            try {
                bet = Integer.parseInt(input);
            } catch (Exception e){
                System.out.println("Not a valid bet number.");
                continue;
            }
            if ((bet < min_bet_amount) || (bet > playerCash)) {
                System.out.println(String.format("Not a valid bet amount. Minimum bet amount is %d and Max bet amount is %d", min_bet_amount, playerCash));
            } else {
                invalidInput = false;
            }
        } while (invalidInput);
        return bet;
    }

    private static String getUserPlay(Scanner scan, boolean isFirstHand, boolean hasDoubleMoney,
                                      boolean canSplit, int handNumber, int numberOfHands) {
        // Gets the players play choice
        boolean invalidInput = true;

        //Scanner scan = new Scanner(System.in);
        Set<String> playerOptions;
        if (isFirstHand && hasDoubleMoney && canSplit) {
            playerOptions = Set.of(HIT, STAND, DOUBLE, SPLIT);
        } else if (isFirstHand && hasDoubleMoney){
            playerOptions = Set.of(HIT, STAND, DOUBLE);
        } else {
            playerOptions = Set.of(HIT, STAND);
        }
        String option = "";

        do {
            if (numberOfHands == 1) {
                System.out.println(String.format("Play options are : %s", String.valueOf(playerOptions)));
                System.out.print("What do you want to do : ");
                option = scan.nextLine();
                option = option.toLowerCase().strip();
                if (!playerOptions.contains(option)) {
                    System.out.println("\tNot a valid choice");
                } else {
                    invalidInput = false;
                }
            } else {
                System.out.println(String.format("Play options for hand %d are : %s",
                        handNumber, String.valueOf(playerOptions)));
                System.out.print("What do you want to do : ");
                option = scan.nextLine();
                option = option.toLowerCase().strip();
                if (!playerOptions.contains(option)) {
                    System.out.println("\tNot a valid choice");
                } else {
                    invalidInput = false;
                }
            }
        } while (invalidInput);

        return option;
    }

    private static int scoreHand(ArrayList<String> hand) {
        int score = 0;
        int aCount = 0;
        for (String card : hand) {
            char cardValue = card.charAt(0);
            switch (cardValue) {
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    score += cardValue - '0';
                        break;
                case 'T':
                case 'J':
                case 'Q':
                case 'K':
                    score += 10;
                    break;
                case 'A':
                    score += 11;
                    aCount++;
                    break;
                default:
                    throw new RuntimeException("Unexpected card found");
            }
        }
        while (score > 21 && aCount > 0) { // account for aces as 1 or 11
            score -= 10;
            aCount --;
        }
        return score;
    }
    
    private static int calculateHandOutcome(int bet, int playerScore, int dealerScore, boolean split, boolean isFirstHand, int handSize) {
        String handText = "";

        if (split) {
            handText = isFirstHand ? "Hand 1 Outcome: " : "Hand 2 Outcome: ";
        }
        if (playerScore > 21) { // player busts
            System.out.println(handText + "You Bust :(\n");
            return 0;
        }
        if (playerScore == dealerScore) { // scores are even
            System.out.println(handText + "Push, Money back \n");
            return bet;
        }

        if (playerScore == 21 && handSize == 2) { // You win + blackjack payout
            // TODO: fix this
            System.out.println(handText + "BlackJack! You win!\n");
            return (bet * 5) / 2;
        }

        // player < 21
        if (dealerScore > 21) {
            System.out.println(handText + "Dealer busts, you win! :)\n");
            return bet * 2;
        }
        if (playerScore > dealerScore) {
            System.out.println(handText + "You win! :)\n");
            return bet * 2;
        }
        System.out.println(handText + "Dealer wins :(\n");
        return 0;
    }

    private static boolean checkPlayNextHand (Scanner scan) {
        boolean keepPlaying = false;
        boolean invalidInput = true;
        do {
            System.out.print("Do you want to play another hand? (Y/n): ");
            String continueInput = scan.nextLine();
            if (continueInput.equalsIgnoreCase("Y")) {
                keepPlaying = true;
                invalidInput = false;
            } else if ( continueInput.equalsIgnoreCase("n")) {
                invalidInput = false;
            } else {
                System.out.println("Invalid input, please enter 'Y' or 'n'");
            }
        } while (invalidInput);
        return keepPlaying;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void printTable(ArrayList<String> dealer,
                                   ArrayList<ArrayList<String>> playerHands,
                                   Visualizer vis) {
        clearScreen();
        System.out.println(String.format("Dealer Hand:"));
        vis.printHand(dealer);
        for (int i=0; i < playerHands.size(); i++) {
            if (playerHands.size() == 1) {
                System.out.println(String.format("Player Hand:"));
                vis.printHand(playerHands.get(i));
            } else {
                System.out.printf("Player Hand %d:\n", i+1);
                vis.printHand(playerHands.get(i));
            }
        }
    }

    private static void splitHand(ArrayList<ArrayList<String>> playerHands,
                                                   Deck deck) {

        ArrayList<String> playerSplitCards = new ArrayList<>();
        ArrayList<String> playerCards = playerHands.getFirst();
        playerSplitCards.add(playerCards.getLast());
        playerCards.removeLast();
        playerCards.add(deck.drawCard());
        playerSplitCards.add(deck.drawCard());
        playerHands.set(0, playerCards);
        playerHands.add(playerSplitCards);

    }

    private static void printTableHit(ArrayList<String> dealer, ArrayList<String> player, Visualizer vis) {
        clearScreen();
        System.out.println(String.format("Dealer Hand:"));
        vis.printHand(dealer);
        System.out.println(String.format("Player Hand:"));
        vis.printHand_with_hit(player, 0);
        try {
            Thread.sleep(250);
        } catch (Exception e) {}
        clearScreen();
        System.out.println(String.format("Dealer Hand:"));
        vis.printHand(dealer);
        System.out.println(String.format("Player Hand:"));
        vis.printHand_with_hit(player, 1);
        try {
            Thread.sleep(250);
        } catch (Exception e) {}

        clearScreen();
        System.out.println(String.format("Dealer Hand:"));
        vis.printHand(dealer);
        System.out.println(String.format("Player Hand:"));
        vis.printHand_with_hit(player, 0);
        try {
            Thread.sleep(250);
        } catch (Exception e) {}
        clearScreen();
        System.out.println(String.format("Dealer Hand:"));
        vis.printHand(dealer);
        System.out.println(String.format("Player Hand:"));
        vis.printHand_with_hit(player, 1);
        try {
            Thread.sleep(250);
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        final int STARTING_CASH = 1000;
        Scanner scan = new Scanner(System.in);

        //System.out.println("BlackJack");
        Visualizer visualizer = new Visualizer();
        clearScreen();
        visualizer.printTitle();

        int playerCash = STARTING_CASH;
        int minBetAmount = 10;

        // Initialize Deck
        Deck deck = new Deck(2);

        // Simulate rounds
        while (playerCash > 0) {

            // Initialize Round Hands
            ArrayList<String> dealerCards = new ArrayList<>();
            ArrayList<ArrayList<String>> playerHands= new ArrayList<>();
            ArrayList<String> playerCards = new ArrayList<>();
            ArrayList<String> dealerHidden = new ArrayList<>();
            ArrayList<Boolean> handStands = new ArrayList<>();
            handStands.add(false);
            handStands.add(false);

            // choose bet amount
            int bet = getPlayerBet(scan, playerCash, minBetAmount);

            // subtract from player money
            boolean hasDoubleMoney = playerCash >= bet *2;
            playerCash -= bet;

            // Deal cards
            dealerCards.add(deck.drawCard());
            dealerHidden.add(dealerCards.get(0));
            printTable(dealerHidden, playerCards, visualizer);
            try {
                Thread.sleep(500);
            } catch (Exception e) {}

            playerCards.add(deck.drawCard());
            printTable(dealerHidden, playerCards, visualizer);
            try {
                Thread.sleep(500);
            } catch (Exception e) {}

            dealerCards.add(deck.drawCard());
            dealerHidden.add("Blank");
            printTable(dealerHidden, playerCards, visualizer);
            try {
                Thread.sleep(500);
            } catch (Exception e) {}

            playerCards.add(deck.drawCard());
            printTable(dealerHidden, playerCards, visualizer);
            try {
                Thread.sleep(500);
            } catch (Exception e) {}

            printTable(dealerHidden, playerCards, visualizer);
            

            // If dealer has an Ace, ask player if they want insurance
            if (dealerCards.get(0).charAt(0) == 'A'){
                boolean invalidInput = true;
                while (invalidInput) {
                    System.out.print("Do you want insurance? (Y/n): ");
                    String insurance = scan.nextLine();
                    if (insurance.equalsIgnoreCase("Y")) {
                        System.out.println("Bet has been halved");
                        bet = bet/2;
                        playerCash += bet;
                        invalidInput = false;
                    } else if (insurance.equalsIgnoreCase("n")) {
                        System.out.println("Suit yourself");
                        invalidInput = false;
                    } else {
                        System.out.println("Invalid input, please enter 'Y' or 'n'");
                    }
                }
            }

            boolean isFirstHand = true;


            // player could immediately win, check for win
            while (scoreHand(playerHands.getFirst()) < 21 || (playerHands.size() != 1 && scoreHand(playerHands.getLast()) < 21)) { //
                // only continue
                // if
                // player
                // hasn't
                // already won

                for (int i =0; i< playerHands.size(); i++) {
                    int handScore = scoreHand(playerHands.get(i));
                    if (handStands.get(i) || handScore >= 21) {
                        continue;
                    }
                    String choice = getUserPlay(scan, isFirstHand, hasDoubleMoney, canSplit, i+1,
                            playerHands.size());
                    isFirstHand = false;
                    if (choice.equals(HIT)) {

                        System.out.println("You hit!");
                        playerHands.get(i).add(deck.drawCard());

                        printTable(dealerHidden, playerHands, visualizer);
                    } else if (choice.equals(DOUBLE)) {
                        playerCash -= bet;
                        bet += bet;
                        System.out.printf("You doubled, upping the stakes! New bet amount: %d\n", bet);
                    } else if (choice.equals(SPLIT)) {
                        playerCash -= bet;
                        splitHand(playerHands, deck);
                        i--;
                        printTable(dealerHidden, playerHands, visualizer);
                    } else { // stand
                        handStands.set(i, true);
                    }
                }
                // first round has stand, hit (split, double)
                String choice = getUserPlay(scan, isFirstHand, hasDoubleMoney);
                isFirstHand = false;
                if (choice.equals(HIT)) {
                    printTableHit(dealerHidden, playerCards, visualizer);
                    System.out.println("You hit!");
                    playerCards.add(deck.drawCard());
                    printTable(dealerHidden, playerCards, visualizer);

                } else if (choice.equals(DOUBLE)) {
                    playerCash -= bet;
                    bet += bet;
                    playerCards.add(deck.drawCard());
                    printTable(dealerHidden, playerCards, visualizer);
                    System.out.printf("You doubled, upping the stakes and taking exactly one more card! New bet amount: %d\n", bet);
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {}
                    break;
                } else if (choice.equals(SPLIT)) {
                    //TODO: implement this
                } else { // stand
                    break;
                }

            }

            // Dealer play
            while (scoreHand(dealerCards) < 17) {
                printTable(dealerCards, playerHands, visualizer);
                dealerCards.add(deck.drawCard());
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}
            }
            // determine outcome of hand
            int dealerScore = scoreHand(dealerCards);
            int playerScore1 = scoreHand(playerHands.getFirst());
            int playerScore2 = scoreHand(playerHands.getLast());

            printTable(dealerCards, playerHands, visualizer);

            // Pay player
            playerCash += calculateHandOutcome(bet, playerScore1, dealerScore, playerHands.size() == 2, true, playerHands.getFirst().size());
            if (playerHands.size() != 1) {
                playerCash += calculateHandOutcome(bet, playerScore2, dealerScore, playerHands.size() == 2, false, playerHands.getLast().size());
            }

            deck.reset();

            System.out.printf("You now have %d dollars left.\n", playerCash);
            if (playerCash < minBetAmount) break;

            // see if player wants to quit (take hypothetical money home)
            if (!checkPlayNextHand(scan)) break;
        }

        int netCash = playerCash - STARTING_CASH;
        if (netCash < 0){
            System.out.printf("Yikes! You lost %d dollars.\n", Math.abs(netCash));
        }
        else if (netCash == 0){
            System.out.println("You went dead even.");
        }
        else {
            System.out.printf("Congrats! You won %d dollars.\n", (netCash));
        }
        System.out.println("GAME OVER");
    }
}
