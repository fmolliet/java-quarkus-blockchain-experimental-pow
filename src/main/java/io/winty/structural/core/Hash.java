package io.winty.structural.core;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class Hash {
    
    static MessageDigest digest256; 
    
    static {
        try{
            digest256 = MessageDigest.getInstance("SHA-256");
        } catch ( NoSuchAlgorithmException ex){
            log.error("Algoritmo não disponível", ex.getCause());
        }
    }
    
    public static String sha256(String payload) {
        try {
            return Hex.encode(digest256.digest(payload.getBytes("UTF-8")));
        } catch ( UnsupportedEncodingException ex ){
            log.error("Encoding não suportado", ex.getCause());
            throw new RuntimeException(ex);
        }
    }
    
}
