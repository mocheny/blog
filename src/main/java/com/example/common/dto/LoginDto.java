package com.example.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/*
登录验证时使用的实体类

Serializable:一个对象序列化的接口，一个类只有实现了Serializable接口，它的对象才能被序列化

序列化:序列化是将对象状态转换为可保持或传输的格式的过程。与序列化相对的是反序列化，它将流转换为对象。
      这两个过程结合起来，可以轻松地存储和传输数据。
当我们需要把对象的状态信息通过网络进行传输，或者需要将对象的状态信息持久化，以便将来使用时都需要把对象进行序列化

在Java中的这个Serializable接口其实是给jvm看的，通知jvm，我不对这个类做序列化了，你(jvm)帮我序列化就好了。

Serializable接口就是Java提供用来进行高效率的异地共享实例对象的机制，实现这个接口即可。
 */

@Data
public class LoginDto implements Serializable {

    //@NotBlank作用在String上，使得不能为空 要和@valid一起使用
    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
