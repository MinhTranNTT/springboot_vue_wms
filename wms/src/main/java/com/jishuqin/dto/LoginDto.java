package com.jishuqin.dto;

import lombok.Data;

@Data
public class LoginDto {
    private Integer id;
    private String no;
    private String name;
    private Integer sex;
    private String phone;
    private Integer roleId;
    private String token;

    public LoginDto(Integer id, String no, String name, Integer sex, String phone, Integer roleId) {
        this.id = id;
        this.no = no;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.roleId = roleId;
    }
}
