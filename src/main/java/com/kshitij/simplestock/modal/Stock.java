package com.kshitij.simplestock.modal;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kshitij
 */
public class Stock {
    private static final int CALC_SCALE=4;
            
    public enum TYPE{
        Common(s -> 
            new BigDecimal(s.getLastDividend()).
                    divide(new BigDecimal(s.getLastPrice()),CALC_SCALE, RoundingMode.HALF_EVEN)
                    .doubleValue())
                          ,
        Preferred(s -> 
            new BigDecimal(s.getFixedDividend()).multiply(new BigDecimal(s.getParValue())).
                    multiply(new BigDecimal("0.01")).
                    divide(new BigDecimal(s.getLastPrice()),CALC_SCALE, RoundingMode.HALF_EVEN)
                    .doubleValue());
    
     private Function<Stock,Double> divYieldFunc;
     
     TYPE(Function<Stock,Double> divYieldFunction){
         divYieldFunc=divYieldFunction;
     }
     public Function<Stock,Double> getDividendYieldFunc(){
         return divYieldFunc;
     } 
   }
    
    private String stockId;
    private Stock.TYPE stockType;
    private Double lastDividend;
    private Double fixedDividend;
    private Double parValue;
    private Double lastPrice;
    
    private Stock(){
    }

    public String getStockId() {
        return stockId;
    }

    public TYPE getStockType() {
        return stockType;
    }

    public Double getLastDividend() {
        return lastDividend;
    }

    public Double getFixedDividend() {
        return fixedDividend;
    }

    public Double getParValue() {
        return parValue;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getDividendYield(){
        if(this.getLastPrice()!=null && !this.getLastPrice().equals(new Double(0))){
        return this.stockType.getDividendYieldFunc().apply(this);
        } else{
            throw new RuntimeException("Cannot calculate PE ratio as price is zero for stockId " + stockId);
        }
    }
    
   public double getPERatio(){
       if(this.getLastDividend()!=null && !this.getLastDividend().equals(new Double(0))){
          return new BigDecimal(this.getLastPrice()).
               divide(new BigDecimal(this.getLastDividend()), CALC_SCALE, RoundingMode.HALF_EVEN)
                       .doubleValue();
       }
       else{
           throw new RuntimeException("Cannot calculate PE ratio as dividend is zero for stockId "+ stockId);
       }
         
   } 
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.stockId);
        hash = 97 * hash + Objects.hashCode(this.stockType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stock other = (Stock) obj;
        if (!Objects.equals(this.stockId, other.stockId)) {
            return false;
        }
        if (this.stockType != other.stockType) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "Stock{" + "stockId=" + stockId + ", stockType=" + stockType + ", lastDividend=" + lastDividend + ", fixedDividend=" + fixedDividend + ", parValue=" + parValue + '}';
    }
    
    
    
    public static class StockBuilder{
        private Stock stock;
        
        public static StockBuilder createStock(){
             StockBuilder builder = new StockBuilder();
             builder.stock = new Stock();
             return builder;
         }
        
        public StockBuilder withStockId(String stockId){
            this.stock.stockId = stockId;
            return this;
        }
        
        public StockBuilder withLastDividend(double lastDividend){
            this.stock.lastDividend = lastDividend;
            return this;
        }
        
        public StockBuilder withStockType(Stock.TYPE stockType){
            this.stock.stockType = stockType;
            return this;
        }
        
        public StockBuilder withFixedDividend(double fixedDividend){
            this.stock.fixedDividend = fixedDividend;
            return this;
        }
        
        public StockBuilder withParValue(double parValue){
            this.stock.parValue = parValue;
            return this;
        }
        
        public Stock build(){
            return this.stock;
        }
        
        public static Stock createStockFromMap(Map<String,String> map){
            Stock newStock = new Stock();
            for(Map.Entry<String,String> entry :map.entrySet()){
                try {
                    String fieldName = entry.getKey();
                    Field field = newStock.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    if(field.getType().equals(Double.class)){
                        field.set(newStock, new Double(entry.getValue()));
                        continue;
                    }else if(field.getType().isEnum()){
                        field.set(newStock, Stock.TYPE.valueOf(entry.getValue()));
                        continue;
                    }else{
                        field.set(newStock, entry.getValue());
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            return newStock;
        }
    } 
}
