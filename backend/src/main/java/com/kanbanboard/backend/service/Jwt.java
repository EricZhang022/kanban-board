package com.kanbanboard.backend.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Base64;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

@Service
public class Jwt {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public Jwt() throws Exception  {
        this.privateKey = loadPrivateKey("keys/jwt_private.pem");
        this.publicKey = loadPublicKey("keys/jwt_public.pem");
    }

    // strip headers, base64 decode
    private PrivateKey loadPrivateKey(String path) throws Exception {
        String pem = Files.readString(Path.of(path))
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(pem);

        // PKCS8 standardized binary layout for storing private key -> Public Key Cryptography Standards #8
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec); // hands back real PrivateKey obj use for signing
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        String pem = Files.readString(Path.of(path))
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(pem);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec); // hands back real PublicKey obj use for signing
    }

    // Java in ms (millisec)
    private final long EXPIRATION = 1000 * 60 * 60 * 24; //24 hrs

    //Generate signed JWT for given identifier
    public String generateToken(String identifier) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
        .subject(identifier)
        .issuedAt(now)
        .expiration(expireTime)
        .signWith(privateKey, Jwts.SIG.RS256)
        .compact();
    }
    
}
