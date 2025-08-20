package com.vighnaharta.nursery.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    // 🔹 Razorpay Order create karta hai (amount paise me bhejna)
    public Order createOrder(long amountInRupees, String receiptId) throws Exception {
        int amountInPaise = (int) (amountInRupees * 100);

        RazorpayClient client = new RazorpayClient(key, secret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receiptId);
        orderRequest.put("payment_capture", 1); // auto-capture

        return client.orders.create(orderRequest);
    }

    public String getKey() { 
    	return key; 
    	}
    public String getSecret() { 
    	return secret; 
    	}
}
