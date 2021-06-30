import java.util.InputMismatchException;
import java.util.Scanner;

public class Main implements Commons{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int userSelection;
        int userScore = 0;

        System.out.println("""
                Welcome to guessing game\s
                Which site you want to scrap\s
                0. Mako\s
                1. Walla\s
                2. Ynet""");
        System.out.println("Enter your choice: ");

        do {
            while (true) {
                try {
                    userSelection = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input try again:");
                    scanner.nextLine();
                }
            }
            switch (userSelection) {
                case MAKO -> {
                    System.out.println("Loading articles from mako: ..........");
                    MakoRobot makoRobot = new MakoRobot(URL_MAKO);
                    userScore += makoRobot.fiveGuess(makoRobot, scanner);
                    userScore += makoRobot.articleGuess(makoRobot, scanner);
                    System.out.println("You scored: " + userScore);
                }
                case WALLA -> {
                    System.out.println("Loading articles from walla: ..........");
                    WallaRobot wallaRobot = new WallaRobot(URL_WALLA);
                    userScore += wallaRobot.fiveGuess(wallaRobot, scanner);
                    userScore += wallaRobot.articleGuess(wallaRobot, scanner);
                    System.out.println("You scored: " + userScore);
                }
                case YNET -> {
                    System.out.println("Loading articles from ynet: ..........");
                    YnetRobot ynetRobot = new YnetRobot(URL_YNET);
                    userScore += ynetRobot.fiveGuess(ynetRobot, scanner);
                    userScore += ynetRobot.articleGuess(ynetRobot, scanner);
                    System.out.println("You scored: " + userScore);
                }
                default -> System.out.println("Invalid input try again:");
            }
        } while (userSelection != MAKO && userSelection != YNET);
    }
}