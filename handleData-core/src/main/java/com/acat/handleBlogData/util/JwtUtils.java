package com.acat.handleBlogData.util;

import com.acat.handleBlogData.controller.resp.LoginRespVo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.common.collect.ImmutableMap;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
public class JwtUtils {

//    //todo 暂时不实现token超时
//    private static final long EXPIRE_TIME = 30 * 60 * 1000;
//
//    /**
//     * 创建秘钥
//     */
//    private static final byte[] SECRET = "handleBlogData".getBytes();
//
//    /**
//     * 生成HS256对称加密token
//     */
//    public static String getHS256Token(LoginRespVo loginRespVo) {
//        try {
//            //创建一个32-byte的密匙
//            MACSigner macSigner = new MACSigner(SECRET);
//            //建立payload载体
//            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRE_TIME))
//                    .claim("loginRespVo", loginRespVo)
//                    .build();
//            //建立签名
//            SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
//            signedJwt.sign(macSigner);
//            //生成token
//            return signedJwt.serialize();
//        } catch (Exception e) {
//            log.error("生成token失败", e);
//        }
//        return null;
//    }
//
//    /**
//     * 检验HS256对称加密token
//     */
//    public static LoginRespVo checkHS256Token(String token) throws Exception {
//        SignedJWT jwt = SignedJWT.parse(token);
//        JWSVerifier verifier = new MACVerifier(SECRET);
//        //校验是否有效
//        return checkParam(jwt, jwt.verify(verifier));
//    }
//
//    private static RSAKey rsaKey;
//
//    private static RSAKey publicRsaKey;
//
//    private static final int SIZE = 2048;
//
//    static {
//       //生成公钥，公钥是提供出去，让使用者校验token的签名
//        try {
//            rsaKey = new RSAKeyGenerator(SIZE).generate();
//            publicRsaKey = rsaKey.toPublicJWK();
//        } catch (Exception e) {
//            log.error("初始化公钥失败", e);
//        }
//    }
//
//    /**
//     * 生成RS256非对称加密token
//     */
//    public static String getRS256Token(LoginRespVo loginRespVo) {
//        try {
//            //生成秘钥,秘钥是token的签名方持有，不可对外泄漏
//            RSASSASigner rsassaSigner = new RSASSASigner(rsaKey);
//            //建立payload 载体
//            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRE_TIME))
//                    .claim("loginRespVo", loginRespVo)
//                    .build();
//            //建立签名
//            SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
//            signedJwt.sign(rsassaSigner);
//            //生成token
//            return signedJwt.serialize();
//        } catch (Exception e) {
//            log.error("生成token失败", e);
//        }
//        return null;
//    }
//
//    /**
//     * 检验RS256非对称加密token
//     */
//    public static LoginRespVo checkRS256Token(String token) throws Exception{
//        SignedJWT jwt = SignedJWT.parse(token);
//        //添加私密钥匙 进行解密
//        RSASSAVerifier rsassaVerifier = new RSASSAVerifier(publicRsaKey);
//        return checkParam(jwt, jwt.verify(rsassaVerifier));
//    }
//
//    /**
//     * token检验
//     */
//    private static LoginRespVo checkParam(SignedJWT jwt, boolean verify) throws Exception {
//        //校验是否有效
//        if (!verify) {
//            throw new Exception("token无效");
//        }
//        //校验超时
//        Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
//        if (null != expirationTime && new Date().after(expirationTime)) {
//            throw new Exception("token已过期");
//        }
//        //获取载体中的数据
//        Object loginRespVo = jwt.getJWTClaimsSet().getClaim("loginRespVo");
//        //是否有userInfo
//        if (loginRespVo == null) {
//            throw new Exception("用户信息为空");
//        }
//        return JacksonUtil.strToBean(JacksonUtil.beanToStr(loginRespVo), LoginRespVo.class);
//    }

    /**
     * 过期5分钟
     * */
    private static final long EXPIRE_TIME = 10 * 60 * 1000;

    /**
     * jwt密钥
     * */
    private static final String SECRET = "jwt_secret";

    /**
     * 生成jwt字符串，10分钟后过期  JWT(json web token)
     * @param loginRespVo,Map的value只能存放值的类型为：Map，List，Boolean，Integer，Long，Double，String and Date
     * @return
     * */
    public static String sign(LoginRespVo loginRespVo) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    //将userId保存到token里面
                    .withAudience(loginRespVo.getId().toString())
                    //存放自定义数据
                    .withClaim("info", JacksonUtil.beanToStr(loginRespVo))
                    //五分钟后token过期
                    .withExpiresAt(date)
                    //token的密钥
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据token获取userId
     * @param token
     * @return
     * */
    public static String getUserId(String token) {
        try {
            return JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException e) {
            return null;
        }
    }

//    /**
//     * 根据token获取自定义数据info
//     * @param token
//     * @return
//     * */
//    public static LoginRespVo getInfo(String token) {
//        try {
//            return JWT.decode(token).getClaim("info").asMap();
//        }catch (JWTDecodeException e) {
//            return null;
//        }
//    }

    /**
     * 校验token
     * @param token
     * @return
     * */
    public static boolean checkSign(String token) {
        try {
            Algorithm algorithm  = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withClaim("username, username)
                    .build();
            verifier.verify(token);
            return true;
        }catch (JWTVerificationException e) {
            throw new RuntimeException("token认证名称错误，请重新登录");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("token认证名称错误，请重新登录");
        }
    }
}
