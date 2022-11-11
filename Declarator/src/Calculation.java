import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Calculation {
    private final TradeRepository tradeList = new TradeRepository();
    private final RateRepository rateList = new RateRepository();

    public List<CalculatedTrade> calculate() throws IOException {
        List<Trade> trades = tradeList.getTrades();
        List<Rate> rates = rateList.getRates(getRateUrl(getFirstOpenDate(trades), getLastCloseDate(trades)));
        List<CalculatedTrade> calculatedTrades = new ArrayList<>();

        for (Trade trade : trades) {
            String baseCurrency = splitPair(trade.getPair())[0].toUpperCase();
            String quoteCurrency = splitPair(trade.getPair())[1].toUpperCase();
            Rate rateOpen = getRate(rates, trade.getOpening());
            Rate rateClose = getRate(rates, trade.getClosing());

            calculatedTrades.add(calculateTrade(rateOpen, rateClose, trade, baseCurrency, quoteCurrency));
        }

        return calculatedTrades;
    }
    private CalculatedTrade calculateTrade(Rate rateOpen, Rate rateClose, Trade trade, String base, String quote) {
        final long lotMultiplier = 100000L;
        long amount = trade.getLotSize().multiply(new BigDecimal(lotMultiplier)).longValue();
        CalculatedTrade tradeNew = new CalculatedTrade();
        BigDecimal usdOpen = rateOpen.getUsd();
        BigDecimal usdClose = rateClose.getUsd();

        switch (quote) {
            case "CAD" -> {
                BigDecimal open = rateOpen.getCad();
                BigDecimal close = rateClose.getCad();

                tradeNew = trade.getDirection().equals("buy") ? addBuyTrade(amount, base, quote, trade, open, close, usdOpen, usdClose)
                        : addSellTrade(amount, base, quote, trade, open, close, usdOpen, usdClose);
            }
            case "CHF" -> {
                BigDecimal open = rateOpen.getChf();
                BigDecimal close = rateClose.getChf();

                tradeNew = trade.getDirection().equals("buy") ? addBuyTrade(amount, base, quote, trade, open, close, usdOpen, usdClose)
                        : addSellTrade(amount, base, quote, trade, open, close, usdOpen, usdClose);
            }
            case "JPY" -> {
                BigDecimal open = rateOpen.getJpy();
                BigDecimal close = rateClose.getJpy();

                tradeNew = trade.getDirection().equals("buy") ? addBuyTrade(amount, base, quote, trade, open, close, usdOpen, usdClose)
                        : addSellTrade(amount, base, quote, trade, open, close, usdOpen, usdClose);
            }
            case "USD" -> {
                BigDecimal open = rateOpen.getUsd();
                BigDecimal close = rateClose.getUsd();

                tradeNew = trade.getDirection().equals("buy") ? addBuyTrade(amount, base, quote, trade, open, close, usdOpen, usdClose)
                        : addSellTrade(amount, base, quote, trade, open, close, usdOpen, usdClose);
            }
        }

        return tradeNew;
    }

    private CalculatedTrade addBuyTrade(long amount, String base, String quote, Trade trade, BigDecimal rateOpen, BigDecimal rateClose, BigDecimal usdOpen, BigDecimal usdClose) {
        BigDecimal commission = trade.getCommission().multiply(usdOpen);

        if (quote.equals("CHF") || quote.equals("JPY")) {
            final BigDecimal rateDivider = new BigDecimal("100");
            rateOpen = rateOpen.divide(rateDivider, 9, RoundingMode.UNNECESSARY);
            rateClose = rateClose.divide(rateDivider, 9, RoundingMode.UNNECESSARY);
        }
        if (trade.isMt5()) {
            final BigDecimal commissionDivider = new BigDecimal("2");
            commission = trade.getCommission().divide(commissionDivider, RoundingMode.UNNECESSARY).multiply(usdOpen)
                    .add(trade.getCommission().divide(commissionDivider, RoundingMode.UNNECESSARY).multiply(usdClose));
        }

        BigDecimal sellPrice = trade.getPriceClose().multiply(new BigDecimal(amount).multiply(rateClose));
        BigDecimal buyPrice = trade.getPriceOpen().multiply(new BigDecimal(amount).multiply(rateOpen))
                .subtract(commission.add(trade.getSwap().multiply(usdClose)));

        BigDecimal profit = sellPrice.subtract(buyPrice).setScale(6, RoundingMode.HALF_UP);
        BigDecimal dollarProfit = trade.getProfit().add(trade.getCommission().add(trade.getSwap())).setScale(2, RoundingMode.HALF_UP);

        return new CalculatedTrade(amount, base.toUpperCase(), sellPrice, buyPrice, profit, dollarProfit, trade.isMt5());
    }

    private CalculatedTrade addSellTrade(long amount, String base, String quote, Trade trade, BigDecimal rateOpen, BigDecimal rateClose, BigDecimal usdOpen, BigDecimal usdClose) {
        BigDecimal commission = trade.getCommission().multiply(usdOpen);

        if (quote.equals("CHF") || quote.equals("JPY")) {
            final BigDecimal rateDivider = new BigDecimal("100");
            rateOpen = rateOpen.divide(rateDivider, 9, RoundingMode.UNNECESSARY);
            rateClose = rateClose.divide(rateDivider, 9, RoundingMode.UNNECESSARY);
        }
        if (trade.isMt5()) {
            final BigDecimal commissionDivider = new BigDecimal("2");
            commission = trade.getCommission().divide(commissionDivider, RoundingMode.UNNECESSARY).multiply(usdOpen)
                    .add(trade.getCommission().divide(commissionDivider, RoundingMode.UNNECESSARY).multiply(usdClose));
        }

        BigDecimal sellPrice = trade.getPriceOpen().multiply(new BigDecimal(amount).multiply(rateOpen));
        BigDecimal buyPrice = trade.getPriceClose().multiply(new BigDecimal(amount).multiply(rateClose))
                .subtract(commission.add(trade.getSwap().multiply(usdClose)));

        BigDecimal profit = sellPrice.subtract(buyPrice).setScale(6, RoundingMode.HALF_UP);
        BigDecimal dollarProfit = trade.getProfit().add(trade.getCommission().add(trade.getSwap())).setScale(2, RoundingMode.HALF_UP);

        return new CalculatedTrade(amount, base.toUpperCase(), sellPrice, buyPrice, profit, dollarProfit, trade.isMt5());
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

    private LocalDate getFirstOpenDate(List<Trade> trades) {
        return trades.stream()
                .min(Comparator.comparing(Trade::getOpening))
                .orElseThrow(NoSuchElementException::new).getOpening().minusDays(14);
    }

    private LocalDate getLastCloseDate(List<Trade> trades) {
        return trades.stream()
                .max(Comparator.comparing(Trade::getClosing))
                .orElseThrow(NoSuchElementException::new).getClosing();
    }

    private LocalDate getFirstCloseDate(List<Trade> trades) {
        return trades.stream()
                .min(Comparator.comparing(Trade::getClosing))
                .orElseThrow(NoSuchElementException::new).getClosing();
    }

    public String getTimePeriod() throws IOException {
        return getFirstCloseDate(tradeList.getTrades()).toString() + " - " + getLastCloseDate(tradeList.getTrades()).toString();
    }
}

