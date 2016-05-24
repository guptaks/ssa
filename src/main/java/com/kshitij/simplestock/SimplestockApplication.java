package com.kshitij.simplestock;

import com.kshitij.simplestock.cmd.CommandProcessor;
import com.kshitij.simplestock.repo.StockRepo;
import com.kshitij.simplestock.repo.TradeRepo;
import com.kshitij.simplestock.repo.VwapRepo;
import com.kshitij.simplestock.validate.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;




@SpringBootApplication
@EnableConfigurationProperties
public class SimplestockApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplestockApplication.class, args);
	}
        
        @Bean
        public ConsoleRunner consoleRunner() {
            ConsoleRunner console = new ConsoleRunner();
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
