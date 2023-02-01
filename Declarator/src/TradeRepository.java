import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradeRepository {
    private final List<Trade> tradeList = new ArrayList<>();

    public List<Trade> getTrades() throws IOException {
        final File fileMt4 = new File("C:/Users/Jonas/Desktop/Statement.htm");
        final File fileMt51 = new File("C:/Users/Jonas/Desktop/ReportHistory-7148423.html");
        final File fileMt52 = new File("C:/Users/Jonas/Desktop/ReportHistory-5330507.html");

        final Document documentMt4 = fileMt4.exists() ? Jsoup.parse(fileMt4, "UTF-8") : null;
        final Document documentMt51 = fileMt52.exists() ? Jsoup.parse(fileMt51, "UTF-8") : null;
        final Document documentMt52 = fileMt52.exists() ? Jsoup.parse(fileMt52, "UTF-8") : null;

        if (documentMt4 != null) {
            List<Element> elementListMt4 = new ArrayList<>(documentMt4.select("table tr"));
            addTradeMt4(elementListMt4);
        }
        if (documentMt51 != null) {
            List<Element> elementListMt5 = new ArrayList<>(documentMt51.select("table tr"));
            addTradeMt5(elementListMt5);
        }
        if (documentMt52 != null) {
            List<Element> elementListMt5 = new ArrayList<>(documentMt52.select("table tr"));
            addTradeMt5(elementListMt5);
        }

        return tradeList;
    }

    private void addTradeMt4(List<Element> elements) {
        for (int i = 3; i < elements.size(); i++) {
            if (elements.get(i).select("td").hasClass("msdate")
                    && elements.get(i).select("td").hasAttr("colspan")) {
                continue;
            }
            if (elements.get(i).select("td").hasClass("mspt")
                    && elements.get(i).select("td").hasAttr("colspan")) {
                break;
            }

            List<Element> tempList = new ArrayList<>(elements.get(i).select("td"));

            if (tempList.get(4).text().length() != 6) {
                continue;
            }

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
                    new BigDecimal(tempList.get(13).text()),
                    false));
        }
    }

    private void addTradeMt5(List<Element> elements) {
        for (Element element : elements) {
            if (element.select("td").hasAttr("style")
                    && element.select("td").size() == 1) {
                break;
            }
            if (element.select("td").hasAttr("style")
                    || element.select("th").hasAttr("style")
                    || element.select("div").hasAttr("style")) {
                continue;
            }

            List<Element> tempList = new ArrayList<>(element.select("td"));

            tradeList.add(new Trade(
                    formatDate(tempList.get(0).text()),
                    tempList.get(3).text(),
                    new BigDecimal(tempList.get(5).text()),
                    tempList.get(2).text(),
                    new BigDecimal(tempList.get(6).text()),
                    formatDate(tempList.get(9).text()),
                    new BigDecimal(tempList.get(10).text()),
                    new BigDecimal(tempList.get(11).text()),
                    new BigDecimal(tempList.get(12).text()),
                    new BigDecimal(tempList.get(13).text()),
                    true));
        }
    }

    private LocalDate formatDate(String dateString) {
        String date = dateString.split(" ")[0];
        date = date.substring(0, 4) + "-" + date.substring(5, 7) + "-" + date.substring(8, 10);

        return LocalDate.parse(date);
    }
}
