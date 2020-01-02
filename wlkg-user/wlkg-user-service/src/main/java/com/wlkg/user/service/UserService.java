package com.wlkg.user.service;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.utils.CodecUtils;
import com.wlkg.common.utils.NumberUtils;
import com.wlkg.user.dto.UserDto;
import com.wlkg.user.mapper.UserInfomapper;
import com.wlkg.user.mapper.UserMapper;
import com.wlkg.user.pojo.User;
import com.wlkg.user.pojo.User_Info;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserMapper userMapper;

    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }
        return this.userMapper.selectCount(record) == 0;

    }

    public Boolean sendVerifyCode(String phone) {

        String code = NumberUtils.generateCode(6);
        try {
            Map<String,String> msg=new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            amqpTemplate.convertAndSend("wlkg.sms.exchange","sms.verify.code",msg);
            //将code 存入redis
            stringRedisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    //注册
    public Boolean register(User user, String code) {

        String key=KEY_PREFIX+user.getPhone();
        String codeCache = stringRedisTemplate.opsForValue().get(key);
        if(!code.equals(codeCache)){
            throw new WlkgException(ExceptionEnums.INVALID_VERFIY_CODE);
        }
        user.setCreated(new Date());
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        // 写入数据库
       boolean boo= this.userMapper.insertSelective(user) == 1;

        // 如果注册成功，删除redis中的code
        if (boo) {
            try {
                this.stringRedisTemplate.delete(key);
            } catch (Exception e) {
                logger.error("删除缓存验证码失败，code：{}", code, e);
            }
        }
        return boo;

    }


    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */


    public User queryUser(String username, String password) {

        // 查询
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        // 校验用户名
        if (user == null) {
            throw new WlkgException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        }
        // 校验密码
        if (!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) {
            throw new WlkgException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        }
        // 用户名密码都正确
        return user;

    }



    @Autowired
    private UserInfomapper userInfomapper;
    public User_Info selectUser(Integer id) {

        User_Info userInfo=new User_Info();
        userInfo.setUserId(String.valueOf(id));




        return userInfomapper.selectOne(userInfo);

    }

    public void update(UserDto userDto) {

//        usermapper.deleteByPrimaryKey(userInfo.getUserId());
//        usermapper.insert(userInfo);
        User_Info userInfo = new User_Info();
        String[] birthday = userDto.getBirthday();
        String join = StringUtils.join(birthday, "-");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(join);
            System.out.println(date);
            userInfo.setBirthday(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userInfo.setUserId(userDto.getUserId());
        userInfo.setId(userDto.getId());
        userInfo.setJob(userDto.getJob());
        userInfo.setSex(userDto.getSex());
        userInfo.setUsername(userDto.getUsername());
        System.out.println(userInfo);
        userInfomapper.updateByPrimaryKey(userInfo);

    }
}