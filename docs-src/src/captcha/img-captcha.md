---
title: Image captcha 
subTitle: 2024-12-05 by Frank Cheung
description: Image captcha 
date: 2022-01-05
tags:
  - captcha
layout: layouts/docs.njk
---


# Image CAPTCHA

Image CAPTCHA helps prevent malicious activities such as bot registration and spamming. It is generally recommended to add this protection when performing write operations on public-facing interfaces.

## Usage

### YAML Configuration

```yaml
security:
  ImageCaptcha: # Image CAPTCHA
    enabled: true
    expireSeconds: 60
```

### Add a Controller

Add an API endpoint that returns the image CAPTCHA:

```java
@Autowired
ImageCaptcha imageCaptcha;

@GetMapping("/captcha")
void showCaptcha(HttpServletRequest req, HttpServletResponse response) {
    imageCaptcha.captchaImage(req, response);
}
```

The frontend must include a `uuid` parameter when requesting this endpoint, which serves as the unique identifier for this CAPTCHA generation. It's recommended to pass it via query string, for example: `/captcha?uuid=xxx`.

### Protect an API

Add the `@ImageCaptchaCheck` annotation to the API you want to protect:

```java
@PostMapping("/create_user")
@ImageCaptchaCheck
boolean createUser(@ModelAttribute User user);
```

That's basically all you need. The interceptor will automatically verify the CAPTCHA. If valid, the request proceeds to the business logic; otherwise, an exception is thrown and intercepted.

