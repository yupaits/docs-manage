package com.yupaits.docs.bean;

import com.yupaits.auth.bean.UserDTO;
import lombok.Data;

/**
 * 文档阅读VO类
 * Created by yupaits on 2017/9/28.
 */
@Data
public class DocumentVO {
    private String name;
    private String Content;
    private UserDTO author;
}
