import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class MakoRobot extends BaseRobot {
    public MakoRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
    }

    @Override
    public void getAllArticles(){
        SiteContent siteContent;
        Elements elements = this.getDocument().getElementsByTag("a");
        for(Element element : elements) {
            String elementString = element.toString();
            String url;
            if(elementString.contains("Article-")) {
                int startIndex = elementString.indexOf("href=") + 6;
                int endIndex = elementString.indexOf(".htm") + 4;
                url = elementString.substring(startIndex, endIndex);
                if(!url.contains("mako.co.il")) {
                    url = Commons.URL_MAKO.concat(url);
                }
                if (url.contains("news")){
                    siteContent = getArticle(url, Commons.TAG_TITLE, Commons.TAG_SUBTITLE, Commons.MAKO_TAG_ARTICLE);
                    addUniqueArticle(url, siteContent);
                }
            }
        }
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        Map<String, Integer> wordStatistics = new HashMap<>();
        for (SiteContent siteContent : getArticles().values()){
            String[] words =  siteContent.getAllSiteContent().split(" ");
            for (String word : words){
                wordStatistics.merge(word, 1, Integer::sum);
            }
        }
        return wordStatistics;
    }

    @Override
    public int countInArticlesTitles(String text) {
        StringBuilder makoContentTitle;
        StringBuilder makoContentSubtitle;

        int counter = 0;

        for (SiteContent siteContent : getArticles().values()){
            makoContentTitle = new StringBuilder(siteContent.getTitle());
            makoContentSubtitle = new StringBuilder(siteContent.getSubtitle());

            int indexTitle;
            int indexSubtitle;

            do {
                indexTitle = makoContentTitle.indexOf(text);
                indexSubtitle = makoContentSubtitle.indexOf(text);
                if(indexTitle != Commons.NOT_EQUAL) {
                    counter++;
                    makoContentTitle.setCharAt(indexTitle, '@');
                }
                if (indexSubtitle != Commons.NOT_EQUAL) {
                    counter++;
                    makoContentSubtitle.setCharAt(indexSubtitle, '*');
                }
            }while(indexTitle != Commons.NOT_EQUAL && indexSubtitle != Commons.NOT_EQUAL);
        }
        return counter;
    }

    @Override
    public String getLongestArticleTitle() {
        String makoTitleToReturn = "";
        int currentTitleLength;
        int longest = 0;

        for(SiteContent siteContent : getArticles().values()){
            currentTitleLength = siteContent.getContent().length();
            if(currentTitleLength > longest){
                makoTitleToReturn = siteContent.getTitle();
                longest = currentTitleLength;
            }
        }
        return makoTitleToReturn;
    }

    public int fiveGuess(MakoRobot makoRobot, Scanner userInput) {
        int score = 0;
        System.out.println("Guess what the most common word on the site: \n"  +
                "You have 5 tries.\n" +
                "Here is a hint: " + makoRobot.getLongestArticleTitle());
        for (int i = Commons.FIVE_GUESSES; i > 0; i--) {
            System.out.println("Enter your word");
            String input = userInput.next();
            if (makoRobot.getWordsStatistics().get(input) != null) {
                int points = makoRobot.getWordsStatistics().get(input);
                score += points;
                System.out.println("You got this");
            }else {
                System.out.println("Boo you lose");
            }
        }
        return score;
    }

    public int articleGuess(MakoRobot makoRobot, Scanner userInput) {
        int score = 0;
        System.out.println("Now enter some text that you thinks should appear in the headlines of the articles on the site. \n" +
                "The text can be 1-20 characters long.");
        int guess = makoRobot.countInArticlesTitles(userInput.next());
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