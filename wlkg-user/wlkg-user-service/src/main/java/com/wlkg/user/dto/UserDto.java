package com.wlkg.user.dto;

import lombok.Data;

@Data
public class UserDto {
        private Integer id;
        private String username;
        private Integer sex;
        private String[] birthday;
        //    private String local;
        private String job;
        private String userId;


}
