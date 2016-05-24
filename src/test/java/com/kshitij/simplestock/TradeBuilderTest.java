/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kshitij.simplestock;


import com.kshitij.simplestock.modal.Side;
import com.kshitij.simplestock.modal.Trade;
import com.kshitij.simplestock.modal.Trade.TradeBuilder;
import java.time.Month;
import java.util.regex.Pattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Test;

/**
 *
 * @author Admin
 */
public class TradeBuilderTest {
    
   @Test
   public void testCreateTradeFromString(){
        String tradeString = "stockId=TEA,tradeDate=2016-05-22T10:15:50,qty=6,side=SELL,price=10";
        Trade trade = TradeBuilder.buildTradeFromString(tradeString);
        assertThat("Trade is null",trade,is(notNullValue()));
        assertThat("Trade ID is null",trade.getTradeId(),is(notNullValue()));
        assertThat("Trade Qty is not 6", trade.getQty(),is(equalTo(6.0)));
        assertThat("Trade stockID is not TEA", trade.getStockId(),is(equalTo("TEA")));
        assertThat("Trade date is null", trade.getTradeDate(),is(notNullValue()));
        assertThat("Trade date month is not MAY", trade.getTradeDate().getMonth(),is(equalTo(Month.MAY)));
        assertThat("Trade  side  is not SELL", trade.getSide(),is(equalTo(Side.SELL)));
        assertThat("Trade price  is not 10", trade.getPrice(),is(equalTo(10.0)));
          
      }  
   
    @Test 
    public void testRegex(){
        String regex= "^stockId=\\w*,tradeDate=\\S*?,qty=\\d*,side=(SELL|BUY),price=\\d*$" ;
        String tradeString1="stockId=TEA,tradeDate=2016-05-22T10:15:50,qty=6313,side=SELL,price=10021";
        String tradeString2="stockid=TEA,tradeDate=2016-05-22T10:15:50,qty=6,side=SELL,price=10";
        
        Pattern pattern = Pattern.compile(regex);
        assertThat("Right pattern is not matched",pattern.matcher(tradeString1).matches());
        assertThat("Wrong pattern is  matched",!(pattern.matcher(tradeString2).matches()));
        
    }
   
    
}
