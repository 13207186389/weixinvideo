package com.pengyou.vidio;

import com.pengyou.vidio.domain.User;
import com.pengyou.vidio.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.Test;

public class CommonTest {
    @Test
    public void testGeneJwt(){

        User user = new User();
        user.setId(999);
        user.setHeadImg("www.xdclass.net");
        user.setName("xd");

        String token = JwtUtils.geneJsonWebToken(user);
        System.out.println(token);

    }

    @Test
    public void testCheck(){

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWQiOjk5OSwibmFtZSI6InhkIiwiaW1nIjoid3d3LnhkY2xhc3MubmV0IiwiaWF0IjoxNTY0NTgzMjI3LCJleHAiOjE1NjUxODgwMjd9.dcUwIhDiMYWs_-iD3gbynv5rDsOW3YUeFUaE3DFgRVI";
        Claims claims = JwtUtils.checkJWT(token);
        if(claims != null){
            String name = (String)claims.get("name");
            String img = (String)claims.get("img");
            int id =(Integer) claims.get("id");
            System.out.println(name);
            System.out.println(img);
            System.out.println(id);
        }else{
            System.out.println("非法token");
        }


    }

}
