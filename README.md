**Spring Security 实现短信验证码和图片验证码登录**。

**参考文章**:

- [添加图片验证码](https://www.cnblogs.com/zyly/p/12287310.html)

- [短信验证码校验逻辑](https://www.cnblogs.com/zyly/p/12287813.html)
- [Spring Security中UsernameNotFoundException的解决方案](https://www.it610.com/article/1280916147809566720.htm)

# 1.  实现原理

![实现原理](https://cdn.jsdelivr.net/gh/RingoTangs/image-hosting@master/spring-security/authenticate.2saovizfn4a0.jpg)

- `ImageAuthenticationFilter、SmsAuthenticationFilter` 在这两个过滤器分别校验了图片验证码和短信验证码。
- 由于`Spring Security` 默认支持的是表单登录，项目中采用的是 `InputStream` 的形式来读取 POST 请求中的 JSON，所以就直接在这里校验了（流只能获取一次，如果在这两个过滤前再加验证码过滤器，使用流获读JSON，就会报错）。
- 图片验证码只需要自定义`ImageAuthenticaionFilter`即可，然后将 filter 加入到 Spring Security 过滤器链中。
- 短信验证码需要自定义`SmsAuthenticationFilter、SmsAuthenticationToken、SmsAuthenticationProvider`，然后将 filter 和 provider 加入到 Spring Security 过滤器链中。

> **自定义的 AuthenticationFilter 继承自 AbstractAuthenticationProcessingFilter**：
>
> - filter 必须要设置 AuthenticationManager 属性；
> - requiresAuthenticationRequestMatcher 中保存 filter 拦截的请求路径；
> - 当校验通过时就会进入到 AuthenticationSuccessHandler 中；
> - 校验失败时就会进入到 AuthenticationFailureHandler。
>
> 更多信息请看 AbstractAuthenticationProcessingFilter 源码注释。



# 2. 验证码实现

<img src="https://cdn.jsdelivr.net/gh/RingoTangs/image-hosting@master/spring-security/验证码实现.2a0upgozg6fw.png" alt="验证码实现" style="zoom:150%;" />

- `ValidateCode`: 抽象方法，保存【验证码】和 【过期时间】。
- `SmsCode`: 短信验证码，直接继承 ValidateCode。
- `ImageCode`: 图片验证码，继承 ValidateCode，并且添加了 BufferedImage 属性，用于生成图片。
- `ValidateCodeGenerator`: 抽象方法，依赖了 ValidateCode，有验证码 生成逻辑和存储逻辑（项目中使用 Redis 存储）。



> 注意：图片验证码存储的 key 不是使用的用户名，而是【验证码类型 + "_" + 本次获取验证码的随机字符串（id）】。短信验证码使用【验证码类型 + "__" + 手机号】作为key来存储。
>
> - 前端在获取图片验证码之前要生成一个随机字符串（唯一性），代表本次获取图片验证码的唯一标识。然后在请求带着这个字符串（项目中用参数 id 来标识）去请求获取图片验证码。
> - 当校验图片验证码时，前端需要将这个 id 一起传过来，用于从 Redis 中获取存储的图片验证码。

```java
/**
* @param id   前端传过来的随机字符串（用于生成key）
* @param type 枚举类型, 表示验证码类型
* @return 存在Redis中的key。如: sms_code_qwead、image_code_asdqwe
*/
public static String keyGenerator(String id, ValidateCodeType type) {
    return type.value + "_" + id;
}
```



# 3. 如何使用?

## 3.1. 开发环境

- *MySQL 5.7*。
- *Redis 6.0.4*。
- *Idea 2019.3.3 必须安装 lombok*。
- *spring boot 2.4.1*。



## 3.1. 下载配置

```properties
# 1、下载项目
git clone https://github.com/RingoTangs/spring-security-login-demo.git

# 2、修改配置文件。idea 需要安装 lombok
# 只需要修改 datasource 和 reids 的配置

# 3、图片验证码可以手动开启和关闭(默认开启)
validate.code.image.enabled=true

# 4、创建数据库表（查看项目中的user.sql文件）

# 5、启动项目访问 /JsonLogin.html 即可。
```



[百度网盘项目下载地址](https://pan.baidu.com/s/1C_Sh9gF1phzp52a76ofknQ)
提取码：gjva 


## 3.2. 接口描述

**接口地址**:

**（1）/smsCode**：获取短信验证码。

| 接口参数 | 描述                                        |
| -------- | ------------------------------------------- |
| mobile   | 手机号。必填。                              |
| expireIn | 验证码过期时间（单位: 秒）。选填。默认60s。 |
| length   | 验证码的位数（长度）。选填。默认6位。       |

> **注意**：
>
> 项目中短信验证码并没有接入通信运营商，验证码信息会以 JSON 的形式返回，请注意查看 ~



**（2）/imageCode**：获取图片验证码。

例如：`<img src="http://localhost:8081/imageCode?id=qwead" />` 即可获取验证码。

| 接口参数 | 描述                                 |
| -------- | ------------------------------------ |
| id       | 本次获取图片验证码的唯一标识。必填。 |
| expireIn | 同上。                               |
| length   | 同上。                               |



**（3）/doLogin**：使用 username、password登录的地址。

详细信息请看 `ImageAuthenticationFilter`。

| 接口参数  | 描述                                                         |
| --------- | ------------------------------------------------------------ |
| username  | 用户名。必填。如不填写，按空字符串匹配。                     |
| password  | 密码。必填。如不填写，按空字符串匹配。                       |
| id        | 本次获取图片验证码的唯一标识。必填。如不填写，按空字符串匹配。 |
| imageCode | 图片验证码。必填。如不填写，按空字符串匹配。                 |



**（4）/login/mobile**：使用手机号登录的地址。

详细信息请看 `SmsAuthenticationFilter`。

| 接口参数 | 描述                                         |
| -------- | -------------------------------------------- |
| mobile   | 手机号。必填。如不填写，按空字符串匹配。     |
| smsCode  | 短信验证码。必填。如不填写，按空字符串匹配。 |



**（5）/JsonLogin.html**：H5页面，用于测试登录。也可以用 Postman 等工具测试。

**（6）/logout**：注销本次登录。





# 4. more ~

欢迎您对本项目提出宝贵的意见。如果本项目对您的学习有帮助，请收藏 ~

联系QQ：1466637477。

[更多学习笔记](https://github.com/RingoTangs/LearningNote)。

