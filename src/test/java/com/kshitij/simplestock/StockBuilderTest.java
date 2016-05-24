/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kshitij.simplestock;

import com.google.common.base.Splitter;
import com.kshitij.simplestock.modal.Stock;
import com.kshitij.simplestock.modal.Stock.StockBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;


public class StockBuilderTest {
    

   @Test
   public void testCreateCommonStockFromString(){
        String stockStr = "stockId=TEA,stockType=Common,lastDividend=5,parValue=100";
        
        Stock stock = StockBuilder.createStockFromMap(Splitter.on(",").withKeyValueSeparator("=").split(stockStr));
        assertThat("Stock is null",stock,is(notNullValue()));
        assertThat("Stock ID is not TEA",stock.getStockId(),is("TEA"));
        assertThat("Stock Type is not common",stock.getStockType(),is(Stock.TYPE.Common));
        assertThat("Stock last dividend in not 5 ",stock.getLastDividend(),is(new Double("5")));
        assertThat("Stock fixed dividend in not null ",stock.getFixedDividend(),is(nullValue()));
        assertThat("Stock par Value is not 100",stock.getParValue(),is(new Double("100")));
       
      }  
   
   
    @Test
   public void testCreatePrefferedStockFromString(){
        String stockStr = "stockId=GIN,stockType=Preferred,lastDividend=8,fixedDividend=2,parValue=100";
        
        Stock stock = StockBuilder.createStockFromMap(Splitter.on(",").withKeyValueSeparator("=").split(stockStr));
        assertThat("Stock is null",stock,is(notNullValue()));
        assertThat("Stock ID is not GIN",stock.getStockId(),is("GIN"));
        assertThat("Stock Type is not common",stock.getStockType(),is(Stock.TYPE.Preferred));
        assertThat("Stock last dividend in not 8 ",stock.getLastDividend(),is(new Double("8")));
        assertThat("Stock fixed dividend in not 2 ",stock.getFixedDividend(),is(new Double("2"))); 
        assertThat("Stock par Value is not 100",stock.getParValue(),is(new Double("100")));
       
      } 
    
}
