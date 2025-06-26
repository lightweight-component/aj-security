---
title: HTTP 基本认证（Basic Auth）
subTitle: 2024-12-05 by Frank Cheung
description: Architecture
date: 2022-01-05
tags:
  - HTTP Basic Auth
layout: layouts/docs.njk
---

# HTTP 基本认证（Basic Auth）
**HTTP 基本认证（Basic Auth）**是一种最简单的 HTTP 认证方式，客户端通过在 HTTP 请求头中带上用户名和密码（经过 Base64 编码）来进行身份验证。应用场景多见于测试环境、内部系统或简单接口保护（生产环境建议配合 HTTPS，否则密码易被窃取）。

## 基本原理

1. 客户端将 username:password 用 Base64 编码。
1. 在请求头中添加： `Authorization: Basic <base64编码后的字符串>`
1. 服务器收到请求后，解码并校验用户名密码。