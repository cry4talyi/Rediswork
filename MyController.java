package cn.com.taiji.redis.test.controller;

import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MyController {

    @Autowired
    private StringRedisTemplate template;

    @RequestMapping("aaa")
    public String aaa(String num){

        if (!template.hasKey(num)){
           return first(num);
        }
        else {
          List oowwoo = template.opsForList().range(num, 0, -1);
            String b = (String) oowwoo.get(1);
            int count=Integer.parseInt(b);
            if(count==3){

                Long startTime = Long.parseLong((String) oowwoo.get(2));
                Long currytime = System.currentTimeMillis();
                if (currytime-startTime>60*1000){
                   return first(num);
                }else
                return "请求次数过多";

            }else {
                System.err.println(count);
                count = count+1;
                System.err.println(count);
                String starttime = (String)oowwoo.get(2);
                Long thistime = Long.parseLong((String) oowwoo.get(3));
                Long time = System.currentTimeMillis();
                String lasttime=String.valueOf(time);
                 num =String.valueOf(oowwoo.get(0));
                 String scount = String.valueOf(count);
                System.err.println(scount);
                if(System.currentTimeMillis()-thistime>10000){
                    List a = new ArrayList();
                    a.add(num);
                    a.add(String.valueOf(scount));
                    a.add(starttime);
                    a.add(lasttime);
                    template.delete(num);
                    template.opsForList().rightPushAll(num,a);
                    return scount;

                }else {
                    return "pinfan";
                }
            }

        }
    }
    public String first(String num){
        int i=1;
        List a = new ArrayList();
        a.add(num);
        a.add(String.valueOf(i));
        String startTime = String.valueOf(System.currentTimeMillis());
        String LasttTime = String.valueOf(System.currentTimeMillis());
        a.add(startTime);
        a.add(LasttTime);

        template.opsForList().rightPushAll(num,a);

        List oowwoo = template.opsForList().range(num, 0, -1);
        System.out.println(oowwoo);
        return oowwoo.toString();
    }
}