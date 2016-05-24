/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kshitij.simplestock;

import com.kshitij.simplestock.cmd.CommandProcessor;
import com.kshitij.simplestock.repo.StockRepo;
import com.kshitij.simplestock.repo.TradeRepo;
import com.kshitij.simplestock.repo.VwapRepo;
import com.kshitij.simplestock.validate.Validator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableConfigurationProperties
public class TestConfiguration {

     @Bean
        public ConsoleRunner consoleRunner() {
            ConsoleRunner console = new ConsoleRunnerMock();
            console.setProcessor(commandProcessor());
            console.setValidator(validator());
            return console;
        }
        
        @Bean
        public StockRepo stockRepo() {
            return new StockRepo();
        }
        
        @Bean
        public VwapRepo vwapRepo() {
            return new VwapRepo();
        }
        
        @Bean
        public TradeRepo tradeRepo(){
            return  new TradeRepo();
        } 
        
        @Bean
        public CommandProcessor commandProcessor(){
            CommandProcessor processor = new CommandProcessor();
            processor.setStockRepo(stockRepo());
            processor.setTradeRepo(tradeRepo());
            processor.setVwapRepo(vwapRepo());
            return processor;
        }
        
        
        @Bean
         public Validator validator(){
            Validator validator = new Validator();
            validator.setStockRepo(stockRepo());
            return validator;
        }
    
    
}
