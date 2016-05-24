package com.kshitij.simplestock;

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import com.kshitij.simplestock.cmd.CommandProcessor;
import com.kshitij.simplestock.validate.Validator;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;

/**
 *
 * @author kshitij
 */
public class ConsoleRunner implements CommandLineRunner{
    protected final Log logger = LogFactory.getLog(getClass());
    
    
   
    private CommandProcessor processor;
    
  
    private Validator validator;
    
     @Command(description="Register trade details .\n Usage enter-trade-details <trade-string> \n .Trade "
             + "string should be in following format \n. +"
             + "Usage e.g>>etd|enter-trade-details  stockId=TEA,tradeDate=2016-05-22T10:15:50,qty=6,side=SELL,price=10 \n +"
             + "stockId=Any valid stock string already present in properties files\n +"
             + "tradeDate= Format of tradeDate should be yyyy-MM-ddTH:mm:ss \n"
             + "qty = quantity should be a positive integer \n"
             + "side = BUY or SELL \n" 
             + "price= should be a positive integer upto two decimal number of format 9.99 ")
    public String enterTradeDetails(String tradeString){
        try{
           Optional<String> validationMessage =validator.validateTradeString(tradeString);
          if(validationMessage.isPresent()){
              return validationMessage.get();
          }
            
            String resultMessage  = processor.enterTradeToRepo(tradeString);
            return resultMessage;           
           }catch(RuntimeException ex){
           return "Exception:: " + ex.getMessage();
        }
    }
    
    @Command(description="Get vwap price of stock for trades entered in last fifteen minutes/\n "
             + "Usage gvp|get-vwap-price <stock name>")
    public String getVwapPrice(String stockId){
        try{
          Optional<String> validationMessage =validator.validateStock(stockId);
          if(validationMessage.isPresent()){
              return validationMessage.get();
          }
          String result =processor.getVwapPriceFor(stockId);
          return "VWAP price for " + stockId + " is  "+ result;
           }catch(Exception ex){
          return "Exception:: " + ex.getMessage();
        }
    }   
    
    
     @Command(description="Calculate GBCE index for stocks for which prices are available.\n "
             + "Usage ggi|get-gbce-index  ")
    public String getGbceIndex(){
        try{
            String result =processor.getGBCEIndex();
            return result;
           }catch(RuntimeException ex){
           return "Exception:: " + ex.getMessage();
        }
    } 
    
    
    @Command(description="Calculate PE ratio for given stock.\n "
            + "Usage cpr|calc-PE-ratio <stock name> <price of stock>")
    public String calcPERatio(String stockId, String price){
        try{
          Optional<String> validationMessage =validator.validateStock(stockId);
          if(validationMessage.isPresent()){
              return validationMessage.get();
          }
          double dprice = Double.parseDouble(price);
          double result = processor.getPE(stockId, dprice);
          return "PE ratio for " + stockId + " with latest price " + price +" is  "+ result;
        }catch(NumberFormatException ex){
            return "Please enter price in double format";
        }catch(RuntimeException ex){
            return "Exception:: " + ex.getMessage();
        }
    }
    
   
    
    
    @Command(description="Calculate dividend yield for given stock.\n "
            + "Usage cd|calc-div <stock name> <price of stock>")
    public String calcDiv(String stockId, String price){
        try{
          Optional<String> validationMessage =validator.validateStock(stockId);
          if(validationMessage.isPresent()){
              return validationMessage.get();
          }
          double dprice = Double.parseDouble(price);
          double result = processor.getDividendYied(stockId, dprice);
          return "Dividend yied for " + stockId + " with latest price " + price +" is  "+ result;
        }catch(NumberFormatException ex){
            return "Please enter price in double format";
        }catch(RuntimeException ex){
           // return "Exception " + ex.getStackTrace();
            throw ex;
        }
    }
    
    
    @Override
    public void run(String... strings) throws Exception {
        ShellFactory.createConsoleShell("simple-stock-app", "The Simple Stock Shell\n" +
                "Enter ?l to list available commands. For help ?help <name of command>. To exit shell please enter 'exit'.", this)
                .commandLoop();
        
    }

    public void setProcessor(CommandProcessor processor) {
        this.processor = processor;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    
   
    
}
