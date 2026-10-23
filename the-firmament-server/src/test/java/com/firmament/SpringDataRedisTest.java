package com.firmament;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.ReactiveHashCommands;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);
        // 操作String类型数据
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 操作Hash类型数据
        HashOperations hashOperations = redisTemplate.opsForHash();
        // 操作List类型数据
        ListOperations listOperations = redisTemplate.opsForList();
        // 操作Set类型数据
        SetOperations setOperations = redisTemplate.opsForSet();
        // 操作ZSet类型数据
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }

    @Test
    public void testRedisString(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //SET
        valueOperations.set("language","java");
        valueOperations.set("name","xla");
        //GET
        String language = (String) valueOperations.get("language");
        System.out.println(language);
        //SETEX
        valueOperations.set("code", "325328",5, TimeUnit.MINUTES);
        System.out.println(valueOperations.get("code"));
        //SETNX
        valueOperations.setIfAbsent("language","cpp");
        System.out.println(valueOperations.get("language"));
    }

    @Test
    public void testHash(){
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        //HSET
        hashOperations.put("id","xla","1123");
        hashOperations.put("id","aaa","987");
        //HGET
        String id = (String) hashOperations.get("id", "xla");
        //HDEL
        hashOperations.delete("id","aaa");
        //HKEYS
        Set<Object> ids = hashOperations.keys("id");
        //HVALS
        List<Object> vals = hashOperations.values("id");
    }

    @Test
    public void testList(){
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        //LPUSH
        listOperations.leftPushAll("number","001","002","003");
        //LRANGE
        List<Object> numberList = listOperations.range("number", 0, -1);
        System.out.println(numberList);
        //RPOP
        Object number = listOperations.rightPop("number");
        System.out.println(number);
        //LLEN
        Long size = listOperations.size("number");
        System.out.println(size);
    }

    @Test
    public void testSet(){
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        //SADD
        setOperations.add("set1","a","b","c","0","1","2");
        setOperations.add("set2","b","d","1","2","3");
        //SMEMBNERS
        Set<Object> members = setOperations.members("set1");
        System.out.println(members);
        //SCARD
        Long size = setOperations.size("set1");
        System.out.println(size);
        //SINNER
        Set<Object> intersect = setOperations.intersect("set1", "set2");
        System.out.println(intersect);
        //SUNION
        Set<Object> union = setOperations.union("set1", "set2");
        System.out.println(union);
        //SREM
        setOperations.remove("set1", "c");
        System.out.println(setOperations.members("set1"));
    }

    @Test
    public void testZSet(){
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        //ZADD
        zSetOperations.add("game","rotaeno",8.85);
        zSetOperations.add("game","maimai",8.1);
        zSetOperations.add("game","phigros",5.95);
        //ZRANGE
        Set<Object> range = zSetOperations.range("game", 0, -1);
        System.out.println(range);
        //ZINCRBY
        zSetOperations.incrementScore("game","maimai",0.3);
        System.out.println(zSetOperations.rangeWithScores("game",0,-1));
        //ZREM
        zSetOperations.remove("game","phigros");
        System.out.println(zSetOperations.rangeWithScores("game",0,-1));
    }

    @Test
    public void testCommon(){
        //KEYS
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);
        //EXISTS
        Boolean id = redisTemplate.hasKey("id");
        System.out.println(id);
        //TYPE
        for (String key : keys) {
            DataType type = redisTemplate.type(key);
            System.out.println(type + ":" + key);
        }
        //DEL
        redisTemplate.delete("name");
        System.out.println(redisTemplate.keys("*"));
    }
}
