---
title: Home
subTitle: 2024-12-05 by Frank Cheung
description: TODO
date: 2022-01-05
tags:
  - last one
layout: layouts/docs.njk
---

# 实用的 Java Web 安全库

基于 Spring/HandlerInterceptor 拦截器机制，抽象一套过滤/校验的机制，形成统一一套的调用链，可灵活配置并扩展。本安全框架架构简单，代码精炼，没有其他额外的依赖，适用于任何基于 Spring 的项目。 Spring Boot 程序引入 jar 包即可开箱即用。

本框架的功能有

- HTTP Web 安全
  - HTTP Referer 校验
  - 时间戳加密 Token 校验
  - IP 白名单/黑名单
- 验证码 Captcha 机制
  - 简单 Java 图片验证码
  - 基于 kaptcha 的图片验证码
- HTTP 标准认证
  - HTTP Basic Auth 认证
  - HTTP Digest Auth 认证

## Why AJ MCP SDK?

Compared to the other MCP SDKs, AJ MCP SDK is designed to be lightweight and easy to use. It is built on top of the Java 8, and provides a simple and
intuitive API for developers to implement MCP server features. The size of the SDK is small, making it easy to integrate into your application.

### Why still Java8?

MCP is a protocol-based framework and should therefore be more universally applicable. It should address a broader range of market demands.

Currently, the situation with developing MCP in Java is:

| Framework              | requires JDK |
|------------------------|--------------|
| Official Java MCP SDK  | jdk17+       |
| Spring AI MCP          | jdk17+       |
| Quarkus MCP server     | jdk17+       |
| langchain4j-mcp client | jdk11+       |

Given that a large number of servers still run on JDK 8, the ability to develop MCP (or MCP Server) with Java 8 is essential to ensure broad compatibility and flexibility — that’s what real freedom for MCP means.

## Further

For specific information about working with individual components,
please see dedicated pages on [Architecture](auth), [Client SDK](captcha), [Server SDK](misc).

## Source Code

Under GNU GENERAL PUBLIC LICENSE v3.0.

- Github: [https://github.com/lightweight-component/aj-mcp](https://github.com/lightweight-component/aj-mcp)
- Gitcode: [https://gitcode.com/lightweight-component/aj-mcp](https://gitcode.com/lightweight-component/aj-mcp), for Chinese users faster access.

## Links

[Website](https://mcp.ajaxjs.com) | [DeepWiki](https://deepwiki.com/lightweight-component/aj-mcp)

## JavaDoc

- [aj-mcp-common](https://javadoc.io/doc/com.ajaxjs/aj-mcp-common)
- [aj-mcp-client](https://javadoc.io/doc/com.ajaxjs/aj-mcp-client)
- [aj-mcp-server](https://javadoc.io/doc/com.ajaxjs/aj-mcp-server)