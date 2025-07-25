---
title: 防止重复提交
subTitle: 2024-12-05 by Frank Cheung
description: 防止重复提交（如表单或接口的“二次提交”、“刷新重复”）是后端开发常见的需求
date: 2022-01-05
tags:
  - last one
layout: layouts/docs-cn.njk
---

# 防止重复提交

防止重复提交（如表单或接口的“二次提交”、“刷新重复”）是后端开发常见的需求。常用的校验和防护方案如下：

## 前端禁用按钮（基础方案）

在表单提交时，前端禁用按钮或显示 loading，防止用户多次点击。 只能防误操作，不能防止恶意或快速重复提交。

## 后端幂等性校验

前端禁用按钮只能防止误操作，后端校验才安全可靠。

### 唯一 Token 校验

- 前端请求表单页时，后端生成唯一 token（如 UUID），返回给前端。
- 前端提交表单时带上 token。
- 后端校验 token 是否已用过，用过即拒绝，并立即让 token 失效。
- 实现方式可用 Redis、数据库等存储 token。

### 基于请求内容的唯一性校验

- 针对同一用户、同一业务参数，在短时间内只能提交一次。
- 可用请求参数哈希（如 MD5）、加上用户 ID，作为 Redis Key，短时间内只能用一次。

## 参考

- [resubmit 渐进式防重复提交框架](https://mp.weixin.qq.com/s/tVkeyrDNc_scRusbClrY1w)