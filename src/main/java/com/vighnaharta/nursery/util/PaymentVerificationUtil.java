package com.vighnaharta.nursery.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class PaymentVerificationUtil {

    // 🔐 HMAC_SHA256(orderId + "|" + paymentId, secret)
    public static boolean verifySignature(String orderId, String paymentId, String signature, String secret) {
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] digest = mac.doFinal(payload.getBytes());
            String calc = Hex.encodeHexString(digest);
            return calc.equalsIgnoreCase(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
