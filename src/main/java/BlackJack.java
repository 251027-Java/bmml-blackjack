public class BlackJack {
    public static void main(String[] args) {
        System.out.println("BlackJack");

        // pseudo code plan

        // initialize deck(s)
            // deck contains 52 standard cards
            // Ace (A is 1 or 11), 2, 3, 4, ..., 9, 10 (or T), Jack (J is 10), Queen (Q is 10), King (K is 10)
            // 4 Suits : Hearts (H), Clubs (C), Spades (S), Diamonds (D)

        // set player money

        // player has cards and dealer has cards
            // player cards and dealer cards should be removed
                // from remaining deck cards. (Cannot be selected)


        // Simulate rounds

            // choose bet amount
            // subtract from player money

            // Deal cards
                // Initialize Player and Dealer hands

            // visualize cards, dealer only shows one
                // Map between Card String and ANSI? Display of Card
                    // Key: Card String
                    // Value: Card Display (print out value)

            // player could immediately win, check for win

            // first round has stand, hit (split, double)

            // each subsequent round a player has options
                // keep playing, hit( add card to player hand)
                // quit, stand (satisfied with current hand)

                // possible outcomes for each round
                    // player busts
                    // player blackjacks
                    // player total under 21

                // update player money with win/lose amount (blackjack gets 1.5)

            // Dealer rules
            // as long as player didnt bust, dealer must play similar rounds
            // dealer must hit until at least 17 is shown. In a tie, no money is gained or lost,
            // including both blackjack.

            // Check winner of round
                // Pay player if player win or give money back for tie

            // After each round, check player money
                // Can only bet less than or equal to currently money

            // if player out of money, game over!
                // ( do we add a credit line? -_- )

            // see if player wants to quit (take hypothetical money home)

    }
}
