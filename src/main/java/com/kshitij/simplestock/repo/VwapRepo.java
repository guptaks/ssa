package com.kshitij.simplestock.repo;

import com.kshitij.simplestock.modal.Trade;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


  
public class VwapRepo {
   private int vwapComputationInMinutes = 15; 
   private final Map<String,VwapStockList> stockToTrades = new HashMap<>();
   
    public int getVwapComputationInMinutes() {
        return vwapComputationInMinutes;
    }

    public void setVwapComputationInMinutes(int vwapComputationInMinutes) {
        this.vwapComputationInMinutes = vwapComputationInMinutes;
    }
    
    public void addVwapTradeForStock(Trade trade){
        LocalDateTime now = LocalDateTime.now();
        if(trade.getTradeDate().isAfter(now.minusMinutes(vwapComputationInMinutes))){
            if(!stockToTrades.containsKey(trade.getStockId())){                    
                VwapStockList vwap = new VwapStockList(vwapComputationInMinutes);
                stockToTrades.put(trade.getStockId(), vwap);
            }
            stockToTrades.get(trade.getStockId()).addTrade(trade);
        }
  }
   
   public Optional<List<Trade>> getVwapTradesForStock(String stockId){
        List<Trade> trades = null;
        if(stockToTrades.containsKey(stockId)){
            trades =stockToTrades.get(stockId).getVwapTrades();
        }
        return Optional.ofNullable(trades);
    }
   
   
}
