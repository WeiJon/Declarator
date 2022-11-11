import java.math.BigDecimal;
import java.time.LocalDate;

public class Trade {
    private LocalDate opening;
    private String direction;
    private BigDecimal lotSize;
    private String pair;
    private BigDecimal priceOpen;
    private LocalDate closing;
    private BigDecimal priceClose;
    private BigDecimal commission;
    private BigDecimal swap;
    private BigDecimal profitInUsd;
    private boolean isMt5;

    public Trade() {
    }
    public Trade(LocalDate opening, String direction, BigDecimal lotSize, String pair, BigDecimal priceOpen, LocalDate closing,
                 BigDecimal priceClose, BigDecimal commission, BigDecimal swap, BigDecimal profit, boolean isMt5) {
        this.opening = opening;
        this.direction = direction;
        this.lotSize = lotSize;
        this.pair = pair;
        this.priceOpen = priceOpen;
        this.closing = closing;
        this.priceClose = priceClose;
        this.commission = commission;
        this.swap = swap;
        this.profitInUsd = profit;
        this.isMt5 = isMt5;
    }

    public LocalDate getOpening() {
        return opening;
    }

    public void setOpening(LocalDate opening) {
        this.opening = opening;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigDecimal getLotSize() {
        return lotSize;
    }

    public void setLotSize(BigDecimal lotSize) {
        this.lotSize = lotSize;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public BigDecimal getPriceOpen() {
        return priceOpen;
    }

    public void setPriceOpen(BigDecimal priceOpen) {
        this.priceOpen = priceOpen;
    }

    public LocalDate getClosing() {
        return closing;
    }

    public void setClosing(LocalDate closing) {
        this.closing = closing;
    }

    public BigDecimal getPriceClose() {
        return priceClose;
    }

    public void setPriceClose(BigDecimal priceClose) {
        this.priceClose = priceClose;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getSwap() {
        return swap;
    }

    public void setSwap(BigDecimal swap) {
        this.swap = swap;
    }

    public BigDecimal getProfit() {
        return profitInUsd;
    }

    public void setProfit(BigDecimal profit) {
        this.profitInUsd = profit;
    }

    public BigDecimal getProfitInUsd() {
        return profitInUsd;
    }

    public void setProfitInUsd(BigDecimal profitInUsd) {
        this.profitInUsd = profitInUsd;
    }

    public boolean isMt5() {
        return isMt5;
    }

    public void setMt5(boolean mt5) {
        isMt5 = mt5;
    }
}