package io.winty.structural;

import java.util.Optional;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.winty.structural.core.Block;
import io.winty.structural.core.Blockchain;
import io.winty.structural.core.Payload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class AppLifeCycleBean {
    
    private static final Logger logger = Logger.getLogger(AppLifeCycleBean.class);
    
    void onStart(@Observes StartupEvent ev) {
        logger.info("Application Starting...");

        int difficult = Integer.parseInt(Optional.ofNullable(System.getenv("BLOCK_DIFFICULT_LEVEL")).orElse("4"));
        Blockchain blockchain = new Blockchain(difficult);
        
        int numOfBlocks = Integer.parseInt(Optional.ofNullable(System.getenv("TOTAL_BLOCKS")).orElse("50"));
        
        var chain = blockchain.getChain();
        
        for ( var i=0; i<=numOfBlocks; i++){
            Payload block = blockchain.createBlockPayload(String.format("Block %d", i));
            Block minedBlock = blockchain.mineBlock(block);
            
            chain = blockchain.sendBlock(minedBlock);
        }
        
        logger.info("--- BLOCKCHAIN ---");
        logger.info(chain.toString());
    }

    void onStop(@Observes ShutdownEvent ev) {
        logger.info("Application is stopping...");
    }
}
