---
title: API 接口数据加密
subTitle: 2024-12-05 by Frank Cheung
description: 对 HTTP API 接口中的入参、出参进行数据加密、解密，其目的是保护敏感数据、防篡改、防抓包。
date: 2022-01-05
tags:
  - api
layout: layouts/docs.njk
---
# API 接口数据加密
对 HTTP API 接口中的入参、出参进行数据加密、解密，其目的是保护敏感数据、防篡改、防抓包。

示例（Java AES 加密）:
```java
// 加密
String json = "{\"username\":\"test\", \"password\":\"123456\"}";
String key = "xxxxxx"; // 16/24/32位
String encrypted = AESUtil.encrypt(json, key);
// 传 encrypted 给后端

// 解密
String decrypted = AESUtil.decrypt(encrypted, key);
// 得到原始json
```

传输格式举例
```json
{
"data": "8kfj2j38fjsl2j3dfk..."  // 加密后的密文
}
```java