package io.winty.structural;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class BlockChainApplication {
    public static void main(String... args) {
        Quarkus.run(BlockchainRunner.class, args);
    }
    
    public static class BlockchainRunner implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
