package com.jishuqin.util;



import com.jishuqin.constant.JwtConstant;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {


    /**
     * 生成token
     * @param username
     * @param role
     * @return 返回token
     */
    public static String createToken(String username,String role){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                // header
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                // payload
                .claim("username",username)
                .claim("role",role)
                .setSubject("admin-login")
                .setExpiration(new Date(System.currentTimeMillis()+JwtConstant.TOKEN_EXPIRE_TIME))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, JwtConstant.SECRET_KEY)
                .compact();
        return jwtToken;
    }

    /**
     * 解析验证token
     * @param token 加密后的token字符串
     * @return 返回TRUE或FALSE表示token验证成功或失败
     */
    public static Boolean verifyToken(String token){

        if(StringUtils.isEmpty(token)){
            return Boolean.FALSE;
        }

        try{
            JwtParser jwtParser = Jwts.parser();
            jwtParser.setSigningKey(JwtConstant.SECRET_KEY).parseClaimsJws(token);
            return Boolean.TRUE;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 解析token
     * @param token token
     * @param key   key
     * @return 结果
     */
    public static String getString(String token, String key) {
        if (StringUtils.isEmpty(token)) return null;
        try {
            Jws<Claims> claimsJws
                    = Jwts.parser() // 构造器
                    .setSigningKey(JwtConstant.SECRET_KEY) // 密钥
                    .parseClaimsJws(token);
            // 获取token里的信息
            Claims claims = claimsJws.getBody();
            return (String) claims.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取username
     * @param token
     * @return
     */
    public static String getUsername(String token){
        return getString(token,"username");
    }

    /**
     * 获取role
     * @param token
     * @return
     */
    public static String getRole(String token){
        return getString(token,"role");
    }

}
