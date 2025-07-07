package com.foo.model;

import com.ajaxjs.security.desensitize.DesensitizeType;
import com.ajaxjs.security.desensitize.annotation.DesensitizeModel;
import com.ajaxjs.security.desensitize.annotation.DesensitizeProperty;
import com.ajaxjs.security.encryptedbody.EncryptedData;
import lombok.Data;

@Data
@DesensitizeModel
//@EncryptedData
public class User {
    private String name;

    @DesensitizeProperty(DesensitizeType.PHONE)
    private String phone;

    private int age;
}
