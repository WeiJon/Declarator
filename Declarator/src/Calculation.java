import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Calculation {

    public void calculate(LocalDate startDate, LocalDate endDate) throws IOException, ParseException {
        TradeList tradeList = new TradeList();
        RateList rateList = new RateList();
        List<Trade> trades = tradeList.getTrades();
        List<Rate> rates = rateList.getRates(getRateUrl(getFirstDate(trades), getLastDate(trades)));
        List<TradeCalculated> calculatedTrades = new ArrayList<>();

        for (Trade trade : trades) {
            String baseCurrency = splitPair(trade.getPair())[0];
            String quoteCurrency = splitPair(trade.getPair())[1];
            Rate rateOpen = getRate(rates, trade.getOpening());
            Rate rateClose = getRate(rates, trade.getClosing());

            calculatedTrades.add(calculateTrade(rateOpen, rateClose, trade, baseCurrency, quoteCurrency));
        }

        /*BigDecimal sum = BigDecimal.ZERO;
        BigDecimal sumDollar = BigDecimal.ZERO;
        for (TradeCalculated trade : calculatedTrades) {
            sum = sum.add(trade.getResult());
            sumDollar = sumDollar.add(trade.getDollarResult());
        }
        System.out.println(sum);
        System.out.println(sumDollar);*/
    }

    private TradeCalculated calculateTrade(Rate rateOpen, Rate rateClose, Trade trade, String base, String quote) {
        final long lotMultiplier = 100000L;
        long amount = trade.getLotSize().multiply(new BigDecimal(lotMultiplier)).longValue();
        TradeCalculated tradeNew = new TradeCalculated();
        BigDecimal usdOpen = rateOpen.getUsd();
        BigDecimal usdClose = rateClose.getUsd();

        switch (quote) {
            case "cad" -> {
                BigDecimal open = rateOpen.getCad();
                BigDecimal close = rateClose.getCad();

                if (trade.getDirection().equals("buy")) {
                    tradeNew = addBuyTrade(amount, base, trade, open, close, usdOpen, usdClose, quote);
                } else {
                    tradeNew = addSellTrade(amount, base, trade, open, close, usdOpen, usdClose, quote);
                }
            }
            case "chf" -> {
                BigDecimal open = rateOpen.getChf();
                BigDecimal close = rateClose.getChf();

                if (trade.getDirection().equals("buy")) {
                    tradeNew = addBuyTrade(amount, base, trade, open, close, usdOpen, usdClose, quote);
                } else {
                    tradeNew = addSellTrade(amount, base, trade, open, close, usdOpen, usdClose, quote);
                }
            }
            case "jpy" -> {
                BigDecimal open = rateOpen.getJpy();
                BigDecimal close = rateClose.getJpy();

                if (trade.getDirection().equals("buy")) {
                    tradeNew = addBuyTrade(amount, base, trade, open, close, usdOpen, usdClose, quote);
                } else {
                    tradeNew = addSellTrade(amount, base, trade, open, close, usdOpen, usdClose, quote);
                }
            }
            case "usd" -> {
                BigDecimal open = rateOpen.getUsd();
                BigDecimal close = rateClose.getUsd();

                if (trade.getDirection().equals("buy")) {
                    tradeNew = addBuyTrade(amount, base, trade, open, close, usdOpen, usdClose, quote);
                } else {
                    tradeNew = addSellTrade(amount, base, trade, open, close, usdOpen, usdClose, quote);
                }
            }
        }
        return tradeNew;
    }

    private TradeCalculated addBuyTrade(long amount, String base, Trade trade, BigDecimal rateOpen, BigDecimal rateClose, BigDecimal usdOpen, BigDecimal usdClose, String quote) {
        if (quote.equals("chf") || quote.equals("jpy")) {
            final BigDecimal rateDivider = new BigDecimal("100");
            rateOpen = rateOpen.divide(rateDivider, 9, RoundingMode.HALF_UP);
            rateClose = rateClose.divide(rateDivider, 9, RoundingMode.HALF_UP);
        }

        BigDecimal sellPrice = trade.getPriceClose().multiply(new BigDecimal(amount)).multiply(rateClose);
        BigDecimal buyPrice = trade.getPriceOpen().multiply(new BigDecimal(amount).multiply(rateOpen))
                .subtract((trade.getCommission().multiply(usdOpen)).add(trade.getSwap().multiply(usdClose)));

        BigDecimal profit = sellPrice.subtract(buyPrice).setScale(6, RoundingMode.HALF_UP);
        BigDecimal dollarProfit = trade.getProfit().add(trade.getCommission().add(trade.getSwap())).setScale(2, RoundingMode.HALF_UP);

        return new TradeCalculated(amount, base.toUpperCase(), sellPrice, buyPrice, profit, dollarProfit);
    }

    private TradeCalculated addSellTrade(long amount, String base, Trade trade, BigDecimal rateOpen, BigDecimal rateClose, BigDecimal usdOpen, BigDecimal usdClose, String quote) {
        if (quote.equals("chf") || quote.equals("jpy")) {
            final BigDecimal rateDivider = new BigDecimal("100");
            rateOpen = rateOpen.divide(rateDivider, 9, RoundingMode.HALF_UP);
            rateClose = rateClose.divide(rateDivider, 9, RoundingMode.HALF_UP);
        }

        BigDecimal sellPrice = trade.getPriceOpen().multiply(new BigDecimal(amount).multiply(rateOpen));
        BigDecimal buyPrice = trade.getPriceClose().multiply(new BigDecimal(amount).multiply(rateClose))
                .subtract((trade.getCommission().multiply(usdOpen)).add(trade.getSwap().multiply(usdClose)));

        BigDecimal profit = sellPrice.subtract(buyPrice).setScale(6, RoundingMode.HALF_UP);
        BigDecimal dollarProfit = trade.getProfit().add(trade.getCommission().add(trade.getSwap())).setScale(2, RoundingMode.HALF_UP);

        return new TradeCalculated(amount, base.toUpperCase(), sellPrice, buyPrice, profit, dollarProfit);
    }

    private Rate getRate(List<Rate> rates, LocalDate date) {
        Rate rate = new Rate();
        LocalDate tempDate = date;
        boolean notFound = true;

        while (notFound) {
            for (Rate value : rates) {
                if (value.getDate().equals(tempDate)) {
                    rate = value;
                    notFound = false;
                    break;
                }
            }
            tempDate = tempDate.minusDays(1);
        }
        return rate;
    }

    private String[] splitPair(String pair) {
        char[] chars = pair.toCharArray();

        return new String[]{String.copyValueOf(chars, 0, 3), String.copyValueOf(chars, 3, 3)};
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
