package com.example.kadai_002.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.kadai_002.entity.Shop;
import com.example.kadai_002.entity.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    public String createStripeSession(
            User user,
            Shop shop,
            LocalDateTime reservationAt,
            Integer numberOfPeople,
            HttpServletRequest httpServletRequest) {

        Stripe.apiKey = stripeApiKey;

        String baseUrl =
                httpServletRequest.getScheme()
                + "://"
                + httpServletRequest.getServerName()
                + ":"
                + httpServletRequest.getServerPort();

        SessionCreateParams params =
                SessionCreateParams.builder()

                    .addPaymentMethodType(
                        SessionCreateParams.PaymentMethodType.CARD
                    )

                    .addLineItem(
                        SessionCreateParams.LineItem.builder()

                            .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()

                                    .setCurrency("jpy")

                                    // テスト用500円
                                    .setUnitAmount(500L)

                                    .setProductData(
                                        SessionCreateParams.LineItem
                                            .PriceData
                                            .ProductData
                                            .builder()
                                            .setName(shop.getName())
                                            .build()
                                    )

                                    .build()
                            )

                            .setQuantity(1L)
                            .build()
                    )

                    .setMode(SessionCreateParams.Mode.PAYMENT)

                    .setSuccessUrl(
                        baseUrl + "/reservations?payment=success"
                    )

                    .setCancelUrl(
                        baseUrl
                        + "/reservations/create?shopId="
                        + shop.getId()
                    )

                    .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()

                            .putMetadata(
                                "userId",
                                user.getId().toString()
                            )

                            .putMetadata(
                                "shopId",
                                shop.getId().toString()
                            )

                            .putMetadata(
                                "reservationAt",
                                reservationAt.toString()
                            )

                            .putMetadata(
                                "numberOfPeople",
                                numberOfPeople.toString()
                            )

                            .build()
                    )

                    .build();

        try {

            Session session = Session.create(params);

            return session.getUrl();

        } catch (StripeException e) {

            e.printStackTrace();

            return "";
        }
    }
}