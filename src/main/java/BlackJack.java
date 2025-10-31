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
    
    private static int calculateHandOutcome(int bet, int playerScore, int dealerScore) {
        if (playerScore == dealerScore) { // scores are even
            System.out.println("Push, Money back \n");
            return bet;
        }

        if (playerScore > 21) { // player busts
            if (dealerScore > 21){ // both bust
                System.out.println("You both bust. Push, Money back \n");
                return bet;
            }
            System.out.println("You Bust :(\n");
            return 0;
        }

        if (playerScore == 21) { // You win + blackjack payout
            System.out.println("BlackJack! You win!\n");
            return (bet * 5) / 2;
        }

        // player < 21
        if (dealerScore > 21) {
            System.out.println("Dealer busts, you win! :)\n");
            return bet * 2;
        }
        if (playerScore > dealerScore) {
            System.out.println("You win! :)\n");
            return bet * 2;
        }
        System.out.println("Dealer wins :(\n");
        return 0;
    }

    private static boolean checkPlayNextHand (int playerCash, int startingCash, Scanner scan) {
        boolean keepPlaying = false;
        boolean invalidInput = true;
        do {
            System.out.print("Do you want to play another hand? (Y/n): ");
            String continueInput = scan.nextLine();
            if (continueInput.equalsIgnoreCase("Y")) {
                keepPlaying = true;
                invalidInput = false;
            } else if (continueInput.equalsIgnoreCase("n")) {
                int netCash = playerCash - startingCash;
                if (netCash < 0){
                    System.out.printf("Yikes! You lost %d dollars.\n", Math.abs(netCash));
                }
                else if (netCash == 0){
                    System.out.println("You went dead even.");
                }
                else {
                    System.out.printf("Congrats! You won %d dollars.\n", (netCash));
                }
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

    private static ArrayList<ArrayList<String>> splitHand(ArrayList<ArrayList<String>> playerHands,
                                                   Deck deck) {

        ArrayList<String> playerSplitCards = new ArrayList<>();
        ArrayList<String> playerCards = playerHands.getFirst();
        playerSplitCards.add(playerCards.getLast());
        playerCards.removeLast();
        playerCards.add(deck.drawCard());
        playerSplitCards.add(deck.drawCard());
        playerHands.set(0, playerCards);
        playerHands.add(playerSplitCards);

        return playerHands;
    }


    public static void main(String[] args) {
        System.out.println("BlackJack");
        final int STARTING_CASH = 1000;
        Scanner scan = new Scanner(System.in);

        Visualizer visualizer = new Visualizer();

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
            dealerCards.add(deck.drawCard());
            dealerHidden.add(dealerCards.get(0));
            dealerHidden.add("Blank");
            playerCards.add(deck.drawCard());
            playerCards.add(deck.drawCard());
            playerHands.add(playerCards);

            // determine if cards are the same
            boolean canSplit = false;
            if (playerCards.getFirst().charAt(0) == playerCards.getLast().charAt(0)) {
                canSplit = true;
            }


            // visualize cards, dealer only shows one
//            System.out.println(String.format("Dealer Hand: %s", String.valueOf(dealerCards)));
//            System.out.println(String.format("Player Hand: %s", String.valueOf(playerCards)));
            printTable(dealerHidden, playerHands, visualizer);


            boolean isFirstHand = true;

            // player could immediately win, check for win
            while (scoreHand(playerHands.getFirst()) < 21 || (playerHands.size() != 1 && scoreHand(playerHands.getLast()) < 21)) { //
                // only continue
                // if
                // player
                // hasn't
                // already won

                for (int i =0; i< playerHands.size(); i++) {
                    if (handStands.get(i)) {
                        continue;
                    }
                    String choice = getUserPlay(scan, isFirstHand, hasDoubleMoney, canSplit, i+1,
                            playerHands.size());
                    isFirstHand = false;
                    if (choice.equals(HIT)) {

                        System.out.println("You hit!");
                        playerHands.get(i).add(deck.drawCard());
                        //TODO visualize hand
                        //System.out.println("\n\n");
                        printTable(dealerHidden, playerHands, visualizer);
                        //Visualize entire 'table' including dealer hand and my hand
                    } else if (choice.equals(DOUBLE)) {
                        playerCash -= bet;
                        bet += bet;
                        System.out.printf("You doubled, upping the stakes! New bet amount: %d\n", bet);
                    } else if (choice.equals(SPLIT)) {
                        playerCash -= bet;
                        playerHands = splitHand(playerHands, deck);
                        printTable(dealerHidden, playerHands, visualizer);
                    } else { // stand
                        handStands.set(i, true);
                    }
                }
                // first round has stand, hit (split, double)
                // 1, 2, 3, 4 as options
                if (playerHands.size() == 1 && handStands.getFirst()) {
                    break;
                } else if (handStands.getFirst() && handStands.getLast()){
                    break;
                }

            }

            // **DEALER STILL NEEDS TO PLAY HAND SINCE BOTH CAN BUST AND OUTCOME WOULD BE A PUSH**
            // immediately end, no need to show dealer cards
            //boolean handActive = !(scoreHand(playerCards) > 21) ;

            // Dealer play
            while (scoreHand(dealerCards) < 17) {
                printTable(dealerCards, playerHands, visualizer);
                dealerCards.add(deck.drawCard());
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    continue;
                }
                //TODO visualize hand actually not sure we need it here
                    // visualize 'table'
            }
            // determine outcome of hand
            int dealerScore = scoreHand(dealerCards);
            int playerScore1 = scoreHand(playerHands.getFirst());
            int playerScore2 = scoreHand(playerHands.getLast());

            printTable(dealerCards, playerHands, visualizer);

            // Pay player
            playerCash += calculateHandOutcome(bet, playerScore1, dealerScore);
            if (playerHands.size() != 1) {
                playerCash += calculateHandOutcome(bet, playerScore2, dealerScore);
            }

            deck.reset();

            System.out.printf("You now have %d dollars left.\n", playerCash);
            if (playerCash == 0) break;

            // see if player wants to quit (take hypothetical money home)
            if (!checkPlayNextHand(playerCash,STARTING_CASH, scan)) break;
        }

        // if player out of money, game over!
        if (playerCash <= 0) {
            System.out.println("You are out of money");
            System.out.println("GAME OVER");
        }
    }
}
