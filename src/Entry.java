import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Entry {
    private LocalDateTime timestamp;
    private String title;
    private String content;

    public Entry(String title, String content) {
        this.timestamp = LocalDateTime.now();
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setContent(String content){
        this.content = content;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter) + " - " + title + "\n" + content;
    }
}
