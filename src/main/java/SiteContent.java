public class SiteContent {
    private final String title;
    private final String content;
    private final String subtitle;
    private final String allSiteContent;

    public SiteContent(String title, String content , String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.allSiteContent = title + " " + subtitle + " " + content;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAllSiteContent() {
        return allSiteContent;
    }
}