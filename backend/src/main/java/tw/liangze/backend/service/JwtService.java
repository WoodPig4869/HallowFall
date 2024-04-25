package tw.liangze.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tw.liangze.backend.model.User;

import javax.crypto.SecretKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    //    設定日期格式的物件
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //    自訂生成一個高強度密鑰(https://asecuritysite.com/encryption/plain)
    //    用以簽發和驗證 JWT Token
    private final String SECRET_KEY = "b64f3dbdc3d16f585b858d8fd66faa42420921f1047b07ae7e93123f106416e2";

    //    從 JWT Token 中提取使用者名稱(這裡的 username 是 phone 值)
    public String extractUsername(String token) {
        return extracClaim(token, Claims::getSubject);
    }
    //    驗證 JWT Token 是否有效
    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
    //    判斷 JWT Token 是否過期
    private boolean isTokenExpired(String token) {
        return extracExpiration(token).before(new Date());
    }
    //    從 JWT Token 中提取 Token 發行時間
    private Date extracExpiration(String token) {
        return extracClaim(token, Claims::getExpiration);
    }
    //    使用 Function 介面提取 JWT Token 中的指定 Claim
    public <T> T extracClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extracAllClaims(token);
        return resolver.apply(claims);
    }

    //    從 JWT Token 中提取所有 Claim
    private Claims extracAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //        產生 JWT Token
    public String generateToken(User user) {
        String token = Jwts
                .builder()
                .setSubject(user.getUsername())//設定Token的主旨(username)
                .claim("userId", user.getUserId())//將 userId 存入 Claim
                .claim("nickname", user.getNickname())//將 nickname 存入 Claim
                .claim("registrationDate", dateFormat.format(user.getRegistrationDate()))
                .claim("role", user.getRole())//將 role 存入 Claim
                .issuedAt(new Date(System.currentTimeMillis()))
                // 設定 Token 有效期為 7 天
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                // 使用密鑰對 Token 進行簽名
                .signWith(getSigningKey())
                .compact();
        return token;
    }

    //      取得簽名用的密鑰
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
