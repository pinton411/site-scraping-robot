import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseRobot {
    private final Map<String, SiteContent> articles;
    private String rootWebsiteUrl;
    private Document document;

    public BaseRobot(String rootWebsiteUrl) {
        this.rootWebsiteUrl = rootWebsiteUrl;
        this.articles = new HashMap<>();

        try {
            this.document = Jsoup.connect(rootWebsiteUrl).get();
        }catch (IOException e){
            e.printStackTrace();
        }
        getAllArticles();
    }

    public SiteContent getArticle(String url, String tagTitle, String tagSubtitle, String tagBodyArticle){
        SiteContent siteContent = null;
        String title;
        String subtitle;
        String content;
        try {
            this.document = Jsoup.connect(url).get();
            title = this.document.getElementsByTag(tagTitle).text();
            subtitle=this.document.getElementsByTag(tagSubtitle).text();
            content = this.document.getElementsByTag(tagBodyArticle).text();
            siteContent = new SiteContent(title,subtitle,content);
        }catch (IOException e){
            System.out.println("an article has failed to connect! ");
        }
        return siteContent;
    }

    public void addUniqueArticle(String url, SiteContent siteContent){
        this.articles.putIfAbsent(url, siteContent);
    }

    public Map<String, SiteContent> getArticles() {
        return articles;
    }

    public abstract void getAllArticles();

    public Document getDocument() {
        return document;
    }

    public String getRootWebsiteUrl() {
        return rootWebsiteUrl;
    }

    public void setRootWebsiteUrl(String rootWebsiteUrl) {
        this.rootWebsiteUrl = rootWebsiteUrl;
    }

    public abstract Map<String, Integer> getWordsStatistics();

    public abstract int countInArticlesTitles(String text);

    public abstract String getLongestArticleTitle();
}