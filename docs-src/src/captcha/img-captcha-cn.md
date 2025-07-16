---
title: 图片验证码
subTitle: 2024-12-05 by Frank Cheung
description: 图片验证码
date: 2022-01-05
tags:
  - captcha
  - 图片验证码
layout: layouts/docs-cn.njk
---

# 图片验证码

图片验证码能够防止机器人注册、灌水等的恶意操作。一般在开放接口进行写入操作的时候需要添加保护的。

# 使用方式

## YAML 配置

```yaml
security:
  ImageCaptcha: # 图片验证码
    enabled: true
    expireSeconds: 60
```

## 添加控制器
添加一个接口，返回图片验证码：

```java
@Autowired
ImageCaptcha imageCaptcha;

@GetMapping("/captcha")
void showCaptcha(HttpServletRequest req, HttpServletResponse response) {
    imageCaptcha.captchaImage(req, response);
}
```

前端请求该接口须携带一个`uuid`，表明本次生成验证码的唯一标识。建议使用 QueryString 传参的方式，例如`/captcha?uuid=xxx`。


## 保护接口
在需要保护的接口上添加注解`@ImageCaptchaCheck`。

```java
@PostMapping("/create_user")
@ImageCaptchaCheck
boolean createUser(@ModelAttribute User user);
```

基本上这样就可以了，拦截器会自动校验验证码，ok 的话走下去业务逻辑，不 ok 抛出异常拦截。