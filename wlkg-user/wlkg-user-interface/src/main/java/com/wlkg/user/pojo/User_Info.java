package com.wlkg.user.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "user_info")
public class User_Info {

    @Id
    private Integer id;
    private String username;
    private Integer sex;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
//    private String local;
    private String job;
    private String userId;
}
