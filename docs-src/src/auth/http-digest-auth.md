---
title: HTTP Digest 认证
subTitle: 2024-12-05 by Frank Cheung
description: HTTP Digest 认证
date: 2022-01-05
tags:
  - HTTP Digest
layout: layouts/docs-cn.njk
---

# HTTP Digest 认证（Digest Authentication）
HTTP Digest Auth 与 Basic Auth 类似，但不会直接传输明文密码，而是将密码、随机数、请求方法等混合生成摘要（哈希），传输哈希值。优点是即使被窃听也无法直接获得密码，防止重放攻击。