import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradeList {
    public List<Trade> getTrades() throws IOException, ParseException {
        final File file = new File("C:/Users/Jonas/Desktop/Statement.htm");
        final Document document = Jsoup.parse(file, "UTF-8");
        List<Element> elementList = new ArrayList<>(document.select("table tr"));
        List<Trade> tradeList = new ArrayList<>();

        for (int i = 3; i < elementList.size(); i++) {
            if (elementList.get(i).select("td").hasClass("msdate")
                    && elementList.get(i).select("td").hasAttr("colspan")) {
                continue;
            }
            if (elementList.get(i).select("td").hasClass("mspt")
                    && elementList.get(i).select("td").hasAttr("colspan")) {
                break;
            }

            List<Element> tempList = new ArrayList<>(elementList.get(i).select("td"));

            tradeList.add(new Trade(
                    formatDate(tempList.get(1).text()),
                    tempList.get(2).text(),
                    new BigDecimal(tempList.get(3).text()),
                    tempList.get(4).text(),
                    new BigDecimal(tempList.get(5).text()),
                    formatDate(tempList.get(8).text()),
                    new BigDecimal(tempList.get(9).text()),
                    new BigDecimal(tempList.get(10).text()),
                    new BigDecimal(tempList.get(12).text()),
                    new BigDecimal(tempList.get(13).text())));
        }

        return tradeList;
    }

    private LocalDate formatDate(String dateString) {
        String date = dateString.split(" ")[0];
        date = date.substring(0, 4) + "-" + date.substring(5, 7) + "-" + date.substring(8, 10);

        return LocalDate.parse(date);
    }
}
