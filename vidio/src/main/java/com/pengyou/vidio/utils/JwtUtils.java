package com.pengyou.vidio.utils;

import com.pengyou.vidio.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.util.Date;

/**
 * jwt工具类
 */
public class JwtUtils {


    public static final String SUBJECT = "xdclass";

    public static final long EXPIRE = 1000*60*60*24*7;  //过期时间，毫秒，一周

    //秘钥
    public static final  String APPSECRET = "xd666";

    /**
     * 生成jwt
     * @param user
     * @return
     */
    public static String geneJsonWebToken(User user){

        if(user == null || user.getId() == null || user.getName() == null
                || user.getHeadImg()==null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)//设置发行者
                .claim("id",user.getId())
                .claim("name",user.getName())
                .claim("img",user.getHeadImg())
                .setIssuedAt(new Date())//发行时间
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))//设置过期时间
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();//签名

        return token;
    }


    /**
     * 校验token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token ){

        try{
            final Claims claims =  Jwts.parser().setSigningKey(APPSECRET).
                    parseClaimsJws(token).getBody();
            return  claims;

        }catch (Exception e){ }
        return null;

    }



}
