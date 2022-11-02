import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Calculation {

    public void calculate() throws IOException, ParseException {
        TradeList tradeService = new TradeList();
        RateList rateService = new RateList();
        List<Trade> trades = tradeService.getTrades();
        List<Rate> rates = rateService.getRates(getRateUrl(getFirstDate(trades), getLastDate(trades)));

        int i = 1;
        for (Trade trade : trades) {
            i++;
            System.out.println(i + " "+ trade.getPair());
        }

    }

    private String getRateUrl(LocalDate from, LocalDate to) {
        return "https://www.riksbank.se/sv/statistik/sok-rantor--valutakurser/" +
                "?g130-SEKCADPMI=on&g130-SEKCHFPMI=on&g130-SEKJPYPMI=on&g130-SEKUSDPMI=on&from=" +
                from + "&to=" + to + "&f=Day&c=cAverage&s=Dot";
    }

    private LocalDate getFirstDate(List<Trade> trades) {
        return trades.stream()
                .min(Comparator.comparing(Trade::getOpening))
                .orElseThrow(NoSuchElementException::new).getOpening();
    }

    private LocalDate getLastDate(List<Trade> trades) {
        return trades.stream()
                .max(Comparator.comparing(Trade::getClosing))
                .orElseThrow(NoSuchElementException::new).getClosing();
    }
}
