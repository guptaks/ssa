package com.kshitij.simplestock.modal;

import com.google.common.base.Splitter;
import com.kshitij.simplestock.validate.Validator;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author kshitijg
 */
public class Trade {
    public static String TRADE_DATEFORMAT = "yyyy-MM-ddTH:mm:ss";
    
   
    
    private String tradeId;
    private String stockId;
    private LocalDateTime tradeDate;
    private double qty;
    private Side side;
    private double price;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public LocalDateTime getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTradeId() {
        return tradeId;
    }

  

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.tradeId);
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
        final Trade other = (Trade) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Trade{" +   "tradeId=" + tradeId  + ", stockId=" + stockId + ", tradeDate=" + tradeDate + ", qty=" + qty + ", side=" + side + ", price=" + price + '}';
    }
    
    private Trade(){
        this.tradeId = UUID.randomUUID().toString();
    }
    
    public static class TradeBuilder{
        private Trade trade;
        private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        
        
        public static  TradeBuilder createTrade(){
            TradeBuilder builder = new TradeBuilder();
            builder.trade = new Trade();
            
            return builder;
        }
        
        public TradeBuilder withStockId(String stockId){
            this.trade.stockId=stockId;
            return this;
        }
        
        public TradeBuilder withTradeDate(LocalDateTime tradeDate){
            this.trade.tradeDate=tradeDate;
            return this;
        }
        
        public TradeBuilder withQty(double qty){
            this.trade.qty=qty;
            return this;
        }
        
        public TradeBuilder withPrice(double price){
            this.trade.price=price;
            return this;
        }
        
        public static Trade buildTradeFromString(String tradeString){
            Map<String,String> field2Value =Splitter.on(",").withKeyValueSeparator("=").split(tradeString);
                Trade trade = new Trade();
             for(Map.Entry<String,String> entry :field2Value.entrySet()){
                try {
                    String fieldName = entry.getKey();
                    Field field = trade.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    if(field.getType().isPrimitive()){
                        field.setDouble(trade,Double.parseDouble(entry.getValue()));
                        continue;
                    }if(field.getType().isEnum()){
                        field.set(trade,Side.valueOf(entry.getValue()));
                        continue;
                    }else if(field.getType().isAssignableFrom(LocalDateTime.class)){
                        LocalDateTime date =LocalDateTime.parse(entry.getValue(), formatter);
                        if(Validator.isTradeDateValid(date)){
                        field.set(trade, date);
                         continue;
                        }else{
                            throw new RuntimeException("Trade date is of future.Please enter a valid date");
                        }
                       
                    }else{
                        field.set(trade, entry.getValue());
                    }
                 
                }catch (NumberFormatException  ex) {
                   throw new RuntimeException("Unable to process entered trade.Please check trade fields type: Unable to parse field "  + entry.getKey());
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Trade.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("Unable to process entered trade.Please check trade fields name " + entry.getKey());
                }catch (DateTimeParseException  ex) {
                   throw new RuntimeException("Unable to process entered trade.Please check trade date:: Format should be "  + Trade.TRADE_DATEFORMAT);
                }
                
            }
           
           if(trade.getPrice() <= 0 || trade.getQty() <=0 ){
                    throw new RuntimeException("Qty or Price cannot be less than or equal to zero.");
               }  
             
            return trade;
        }
       
        
        public Trade build(){
            return this.trade;
        }
        
    }
    
    
}
