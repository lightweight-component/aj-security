---
title: Contact Us
subTitle: 2024-12-05 by Frank Cheung
description: TODO
date: 2022-01-05
tags:
  - timestamp
layout: layouts/docs-cn.njk
---
# 时间戳请求校验

时间戳（Timestamp）请求校验主要用于防止请求重放（Replay Attack），原理是在每次请求中附带一个时间戳（通常是毫秒），后端校验该时间戳是否在允许的时间窗口内。常与签名机制配合，确保请求的时效性和唯一性，但也可以单独使用。

## 常见校验逻辑

1. 客户端请求时带上时间戳（如参数`timestamp=xxx`）。
1. 后端校验当前服务器时间与请求时间戳之差，在合理范围内（如±5分钟）。
1. 超时则拒绝请求。


重放攻击：仅时间戳校验无法防止同一请求多次提交，需配合唯一`nonce`，对于带有业务幂等性要求的接口，也校验`nonce`是否已用过。