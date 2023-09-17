import com.github.shawramland.Entry;

import java.util.ArrayList;
import java.util.List;
public class Journal {
    private List<Entry> entries;

    public Journal() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(String title, String content) {
        Entry entry = new Entry(title, content);
        entries.add(entry);
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void removeEntry(int index) {
        if(index >= 0 && index < entries.size()) {
            entries.remove(index);
        }
    }

    public Entry getEntry(int index) {
        return entries.get(index);
    }
}
