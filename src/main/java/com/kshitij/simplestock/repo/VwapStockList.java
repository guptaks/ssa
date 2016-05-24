package com.kshitij.simplestock.repo;

import com.kshitij.simplestock.modal.Trade;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;


public class VwapStockList {
    private final int vwapComputationInMinutes;
    private List<Trade> trades = new ArrayList<>();
    
    VwapStockList(int vwapComputationInMinutes) {
        this.vwapComputationInMinutes =vwapComputationInMinutes;
    }

    void addTrade(Trade trade) {
        trades.add(trade);
    }

    List<Trade> getVwapTrades() {
        LocalDateTime now = LocalDateTime.now();
        List<Trade> vwapTrades = trades.stream().filter(t-> t.getTradeDate().isAfter(now.minusMinutes(vwapComputationInMinutes))).collect(toList());
        this.trades =vwapTrades;
        return vwapTrades;
    }
    
}
