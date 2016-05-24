package com.kshitij.simplestock.validate;

import com.google.common.base.Splitter;
import com.kshitij.simplestock.repo.StockRepo;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.apache.commons.lang3.math.NumberUtils;



/**
 *
 * @author kshitijg
 */

public class Validator {
    
   
    private StockRepo stockRepo;
    
    private final Predicate<String> isStockNotPresent=s -> !stockRepo.isStockIdValid(s);
    private final Predicate<String> isNotValidNumber = s -> !NumberUtils.isDigits(s);
    private final String regex= "^stockId=\\w*,tradeDate=\\S*?,qty=\\d*,side=(SELL|BUY),price=\\d+(\\.\\d{1,2})?" ;
    private Pattern pattern = Pattern.compile(regex);
    private final Predicate<String> isTradeStringNotValid = s -> !pattern.matcher(s).matches();
    
    
    
    
    public static boolean isTradeDateValid(LocalDateTime tradeDate){
            LocalDateTime curr_time = LocalDateTime.now();
        return !tradeDate.isAfter(curr_time);
     
    } 
    
    public Optional<String> validateStock(String stockId){
        String message=null;
        if(isStockNotPresent.test(stockId)){
            message="StockID " + stockId + " is not present in cache.Please check and enter again.";
        }
        return Optional.ofNullable(message);
    }
    
    public Optional<String> validateTradeString(String tradeString){
         String message=null;
         if(isTradeStringNotValid.test(tradeString)){
             return Optional.of("Invalid trade string.\n Please input valid trade string in following format \n " + regex);
         }
         StringBuilder builder = new StringBuilder();
         Map<String,String> field2Value =Splitter.on(",").withKeyValueSeparator("=").split(tradeString);
         if(isStockNotPresent.test(field2Value.get("stockId")))
             builder.append("StockID ").append(field2Value.get("stockId")).append(" in entered trade is not present in cache.Please check and enter again.");
         if(isNotValidNumber.test(field2Value.get("qty")))
             builder.append("\n Qty should be valid number");
        
         
         if(builder.length()>0){
             message = builder.toString();
         }
         
         return Optional.ofNullable(message);
    }

    public void setStockRepo(StockRepo stockRepo) {
        this.stockRepo = stockRepo;
    }
    
    
}
