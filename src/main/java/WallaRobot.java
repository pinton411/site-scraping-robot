import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class WallaRobot extends BaseRobot {
    public WallaRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
    }

    @Override
    public void getAllArticles() {
        SiteContent siteContent;
        Elements elements = this.getDocument().getElementsByTag("a");
        for(Element element : elements) {
            String elementString = element.toString();
            String url;
            if (elementString.contains("https://news.walla.co.il/item")) {
                int startIndex = elementString.indexOf("href=") + 6;
                int endIndex = elementString.indexOf("item") + 12;
                url = elementString.substring(startIndex, endIndex);
                siteContent = getArticle(url, Commons.TAG_TITLE, Commons.WALLA_TAG_SUBTITLE, Commons.WALLA_TAG_ARTICLE);
                addUniqueArticle(url, siteContent);
            }
        }
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        Map<String,Integer> wordStatistics = new HashMap<>();
        for (SiteContent siteContent : getArticles().values()){
            String[] words =  siteContent.getContent().split(" ");
            for (String word : words) {
                wordStatistics.merge(word, 1, Integer::sum);
            }
        }
        return wordStatistics;
    }

    @Override
    public int countInArticlesTitles(String text) {
        StringBuilder wallaContentTitle;
        StringBuilder wallaContentSubtitle;

        int counter = 0;

        for (SiteContent siteContent : getArticles().values()){
            wallaContentTitle = new StringBuilder(siteContent.getTitle());
            wallaContentSubtitle = new StringBuilder(siteContent.getSubtitle());

            int indexTitle;
            int indexSubtitle;

            do {
                indexTitle = wallaContentTitle.indexOf(text);
                indexSubtitle = wallaContentSubtitle.indexOf(text);
                if(indexTitle != Commons.NOT_EQUAL) {
                    counter++;
                    wallaContentTitle.setCharAt(indexTitle, '@');
                }
                if (indexSubtitle != Commons.NOT_EQUAL) {
                    counter++;
                    wallaContentSubtitle.setCharAt(indexSubtitle, '*');
                }
            }while(indexTitle != Commons.NOT_EQUAL && indexSubtitle != Commons.NOT_EQUAL);
        }
        return counter;
    }

    @Override
    public String getLongestArticleTitle() {
        String wallaTitleToReturn = "";

        int currentTitleLength;
        int longest = 0;

        for(SiteContent siteContent : getArticles().values()) {
            currentTitleLength = siteContent.getContent().length();
            if(currentTitleLength > longest) {
                wallaTitleToReturn = siteContent.getTitle();
                longest = currentTitleLength;
            }
        }
        return wallaTitleToReturn;
    }

    public int fiveGuess(WallaRobot wallaRobot, Scanner userInput) {
        int score = 0;
        System.out.println("Guess what the most common word on the site: \n"  +
                "You have 5 tries.\n" +
                "Here is a hint: " + wallaRobot.getLongestArticleTitle());
        for (int i = Commons.FIVE_GUESSES; i > 0; i--) {
            System.out.println("Enter your word");
            String input = userInput.next();
            if (wallaRobot.getWordsStatistics().get(input) != null) {
                int points = wallaRobot.getWordsStatistics().get(input);
                score += points;
                System.out.println("You got this");
            }else {
                System.out.println("Boo you lose");
            }
        }
        return score;
    }

    public int articleGuess(WallaRobot wallaRobot, Scanner userInput) {
        int score = 0;
        System.out.println("Now enter some text that you thinks should appear in the headlines of the articles on the site. \n" +
                "The text can be 1-20 characters long.");
        int guess = wallaRobot.countInArticlesTitles(userInput.next());
        userInput.nextLine();
        for (int i = 1; i <= Commons.ARTICLE_TWO_GUESSES; i++) {
            try {
                System.out.println("If you hits a number up to an accuracy of 2 plus minus you got this.");
                int number = userInput.nextInt();
                if (guess == number) {
                    System.out.println("You got this");
                    score += Commons.JACK_POT;
                    break;
                }else {
                    System.out.println("Boo you lose");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input try again:");
            }
        }
        return score;
    }
}