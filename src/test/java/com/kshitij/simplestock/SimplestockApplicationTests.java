package com.kshitij.simplestock;


import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
public class SimplestockApplicationTests {
 
    @Autowired
    private ConsoleRunner consoleRunner;
    
    
    
        @Test 
        public void dividendCalculationForCommonStock(){
            String stock = "POP";
            String price="80";
            String result = consoleRunner.calcDiv(stock, price);
            assertThat(result, is(equalTo("Dividend yied for POP with latest price 80 is  0.1")));
        }
        
        @Test 
        public void dividendCalculationForPreferredStock(){
            String stock = "GIN";
            String price="200";
            String result = consoleRunner.calcDiv(stock, price);
            assertThat(result, is(equalTo("Dividend yied for GIN with latest price 200 is  0.01")));
        }
        
        @Test 
        public void peCalcForStock(){
            String stock = "ALE";
            String price="230";
            String result = consoleRunner.calcPERatio(stock, price);
           assertThat(result, is(equalTo("PE ratio for ALE with latest price 230 is  10.0")));
        }
        
         @Test 
        public void peCalcForStockWithZeroDiv(){
            String stock = "TEA";
            String price="100";
            String result = consoleRunner.calcPERatio(stock, price);
           
            assertThat(result, is(equalTo("Exception:: Cannot calculate PE ratio as dividend is zero for stockId TEA")));
        }
        
        @Test
        public void enterTradeDetails(){
            String tradeString = "stockId=TEA,tradeDate=2016-05-22T10:15:50,qty=6313,side=SELL,price=0.92";
            String result = consoleRunner.enterTradeDetails(tradeString);
            System.out.println(result);
            assertThat(result.startsWith("Trade with following details ["), is(true));
        }
        
        @Test
        public void enterTradeDetailsWithInvalidStock(){
            String tradeString = "stockId=ZZZ,tradeDate=2016-05-22T10:15:50,qty=6313,side=SELL,price=100";
            String result = consoleRunner.enterTradeDetails(tradeString);
            assertThat(result, is(equalTo("StockID ZZZ in entered trade is not present in cache.Please check and enter again.")));
          
        }
        
        @Test
        public void enterTradeDetailsWithZeroQty(){
            String tradeString = "stockId=TEA,tradeDate=2016-05-22T10:15:50,qty=0,side=SELL,price=100";
            String result = consoleRunner.enterTradeDetails(tradeString);
            assertThat(result, is(equalTo("Exception:: Qty or Price cannot be less than or equal to zero.")));
          
        }
        
        @Test
        public void enterTradeDetailsWithZeroPrice(){
            String tradeString = "stockId=TEA,tradeDate=2016-05-22T10:15:50,qty=10,side=SELL,price=0";
            String result = consoleRunner.enterTradeDetails(tradeString);
            assertThat(result, is(equalTo("Exception:: Qty or Price cannot be less than or equal to zero.")));
          
        }
        
         @Test
        public void enterTradeDetailsWithNegativePrice(){
            String tradeString = "stockId=TEA,tradeDate=2016-05-22T10:15:50,qty=10,side=SELL,price=-20";
            String result = consoleRunner.enterTradeDetails(tradeString);
            assertThat(result.startsWith("Invalid trade string."), is(true));
          
        }
        
        @Test
        public void calcGbceIndex(){
            String tradeString = "stockId=TEA,tradeDate=2016-05-22T10:15:50,qty=10,side=SELL,price=100";
            consoleRunner.enterTradeDetails(tradeString);
            tradeString = "stockId=POP,tradeDate=2016-05-22T10:15:50,qty=10,side=SELL,price=100";
            consoleRunner.enterTradeDetails(tradeString);
            tradeString = "stockId=ALE,tradeDate=2016-05-22T10:15:50,qty=10,side=SELL,price=100";
            consoleRunner.enterTradeDetails(tradeString);
            tradeString = "stockId=GIN,tradeDate=2016-05-22T10:15:50,qty=10,side=SELL,price=100";
            consoleRunner.enterTradeDetails(tradeString);
            tradeString = "stockId=JOE,tradeDate=2016-05-22T10:15:50,qty=10,side=SELL,price=100";
            consoleRunner.enterTradeDetails(tradeString);
            
            String result = consoleRunner.getGbceIndex();
           assertThat(result.startsWith("GBCE index for stock with valid prices is 100"), is(true));
          
        }
        
        @Test
        public void calcVwapIndexWithSamePrice(){
            String tradeStringTemplate = "stockId=TEA,tradeDate={0},qty={1},side=SELL,price={2}";
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            String tradeString1 = MessageFormat.format(tradeStringTemplate, formatter.format(now.minusMinutes(5)),100,200);
            String tradeString2 = MessageFormat.format(tradeStringTemplate, formatter.format(now.minusMinutes(4)),200,300);
            String tradeString3 = MessageFormat.format(tradeStringTemplate, formatter.format(now.minusMinutes(3)),300,400);
            String tradeString4 = MessageFormat.format(tradeStringTemplate, formatter.format(now.minusMinutes(2)),400,500);
            String tradeString5 = MessageFormat.format(tradeStringTemplate, formatter.format(now.minusMinutes(1)),500,600);
            
            consoleRunner.enterTradeDetails(tradeString1);
            consoleRunner.enterTradeDetails(tradeString2);
            consoleRunner.enterTradeDetails(tradeString3);
            consoleRunner.enterTradeDetails(tradeString4);
            consoleRunner.enterTradeDetails(tradeString5);
            
            String result = consoleRunner.getVwapPrice("TEA");
            assertThat(result, is(equalTo("VWAP price for TEA is  466.6667")));
          
        }
        
        
}
