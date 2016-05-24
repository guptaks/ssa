package com.kshitij.simplestock.repo;

import com.kshitij.simplestock.modal.Stock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author kshitij
 */
public class StockRepo {
   private final Map<String,Stock> stockIdToStock = new HashMap<>();
   
   public void addStockToRepo(Stock stock){
       stockIdToStock.put(stock.getStockId(),stock);
   }
   
   public boolean isStockIdValid(String stockId){
       return stockIdToStock.containsKey(stockId);
   }
   
   public Optional<Stock> getStockDetailsById(String stockId){
      return Optional.ofNullable(stockIdToStock.get(stockId));
   }
   
   public List<Stock> getAllStocks(){
       return new ArrayList(stockIdToStock.values());
   }
   
   public void updateStockPrice(String stockId, double lastPrice){
       stockIdToStock.get(stockId).setLastPrice(lastPrice);
   }

    public void updateStockLastPrice(String stockId, Double price) {
       stockIdToStock.get(stockId).setLastPrice(price);
    }

  
}
