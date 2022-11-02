import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RateList {

    public List<Rate> getRates(String url) throws IOException {
        final Document document = Jsoup.connect(url).get();
        List<Element> elementList = new ArrayList<>(document.select("table tr"));
        List<Rate> rateList = new ArrayList<>();

        for (int i = 2; i < elementList.size(); i++) {
            List<Element> tempList = new ArrayList<>(elementList.get(i).select("td"));
            rateList.add(new Rate(tempList.get(0).text(), new BigDecimal(tempList.get(1).text()), new BigDecimal(tempList.get(2).text()),
                    new BigDecimal(tempList.get(3).text()), new BigDecimal(tempList.get(4).text())));
        }

        return rateList;
    }
}
