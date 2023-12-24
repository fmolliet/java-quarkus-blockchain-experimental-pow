package io.winty.structural.core;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class Blockchain {
    
    
    private ArrayList<Block> chain = new ArrayList<Block>();
    private int difficult = 4;
    private static final String PREFIX_POW = "0";

    public Blockchain(int difficult) {
        this.difficult=difficult;
        chain.add(createGenisisBlock());
    }
    
    public ArrayList<Block> getChain(){
        return chain;
    }
    
    private Block createGenisisBlock(){
        Payload payload = new Payload(BigInteger.ZERO, LocalDateTime.now(), "Bloco inicial", "");
        return new Block(new Header(BigInteger.ZERO, payload.hash()), payload);
    }
    
    private Block getLastBlock(){
        return chain.get(chain.size()-1);
    }
    
    private String getLastBlockHash(){
        return chain.get(chain.size()-1).header().blockHash();
    }

    public Payload createBlockPayload(String data) {
        Payload payload = new Payload(getLastBlock().payload().sequence().add(BigInteger.ONE), LocalDateTime.now(),data, getLastBlockHash());
        
        log.infof("Block: #%d criado: %s",payload.sequence(), payload.toString());
        return payload;
    }

    public Block mineBlock(Payload payload) {
        BigInteger nonce = BigInteger.ZERO;
        LocalDateTime startedAt = LocalDateTime.now();
        
        while ( true ){

            String hash = payload.hash();
            String hashPow = Hash.sha256(hash+nonce.intValue());
            if (isHashProofed(hashPow)) {
                LocalDateTime endedAt = LocalDateTime.now();
                Header header = new Header(nonce, hash);
                Duration duration = Duration.between(startedAt, endedAt);
                
                log.infof("Block #%d minerado em %dms. \nHash %s (%d tentativas)".formatted(payload.sequence(), duration.toMillis(), hash.substring(12), nonce));
                return new Block(header, payload);
            }
            
            nonce = nonce.add(BigInteger.ONE);
        }
    }
    
    private boolean isHashProofed( String hash){
        return hash.startsWith(PREFIX_POW.repeat(difficult));
    }

    public ArrayList<Block> sendBlock(Block block) {
        if ( verifyBlock(block)) {
            this.chain.add(block);
            log.infof("Block #%d foi adicionado a blockchain: $%s", block.payload().sequence(), block.toString());
        }
        return this.chain;
    }
    
    private boolean isLastHashEquals( Block block ){
        return block.payload().lastHash().equalsIgnoreCase(getLastBlockHash());
    }
    
    private boolean verifyBlock(Block block) {
        if ( !isLastHashEquals(block)){
            log.errorf("Bloco #%d inválido: O hash anterior é %s e não %s", block.payload().sequence(), getLastBlockHash().substring(12), block.payload().lastHash());
            return false;
        }
        
        String hashTest = Hash.sha256(block.payload().hash()+block.header().nonce().intValue());
        if (!isHashProofed(hashTest) ){
            log.errorf("Bloco #%d inválido: O nonce %d é invalido e não pode ser verificado", block.payload().sequence(), block.header().nonce());
            return false;
        }
        
        return true;
    }
    
}
