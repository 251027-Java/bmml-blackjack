import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    // initialize deck(s) and drawing cards

    private ArrayList<String> cards_in_deck = new ArrayList<>();
    private ArrayList<String> cards_in_play = new ArrayList<>();

    public Deck() {
        makeCards();

        this.shuffle();
    }

    public Deck(int num_decks) {
        makeCards();

        // initialize num_decks number of decks
        ArrayList<String> temp = new ArrayList<>(cards_in_deck);
        for (int i = 0; i < num_decks; i++) {
            cards_in_deck.addAll(temp);
        }
        temp = null;
        // shuffle
        this.shuffle();
    }

    public void reset() {
        // For each num deck, add on another deck.
        cards_in_deck.addAll(cards_in_play);
        cards_in_play.clear();
        this.shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards_in_deck);
    }

    public String drawCard() {
        // Draw top card from deck
        String card = cards_in_deck.removeLast();

        // Add card to cards in play
        cards_in_play.add(card);

        return card;
    }

    private void makeCards() {
        // Make the initial possible cards
        String[] cardValues = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
        String[] cardSuits = {"S", "D", "C", "H"};

        for (String value : cardValues) {
            for (String suit : cardSuits) {
                String card = value + suit;
                cards_in_deck.add(card);
            }
        }
    }
    public void printDeck() {
        System.out.println(cards_in_deck);
    }
}
