package com.ecommerce.paymentservice.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService implements IPaymentService {
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    @Value("${stripe.redirect.url}")
    private String redirectUrl;

    @Override
    public String generatePaymentLink(String orderId) throws StripeException {

        Stripe.apiKey = stripeApiKey;

        ProductCreateParams productCreateParams =
                ProductCreateParams.builder()
                        .setName("T-Shirt").build();

        Product product = Product.create(productCreateParams);

        PriceCreateParams priceCreateParams =
                PriceCreateParams.builder()
                        .setCurrency("GBP")
                        .setUnitAmount(1000L) //10.00
                        .setProduct(product.getId())
                        .build();

        Price price = Price.create(priceCreateParams);

        PaymentLinkCreateParams paymentLinkCreateParams =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        )
                        .setAfterCompletion(
                                PaymentLinkCreateParams.AfterCompletion.builder()
                                        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                        .setRedirect(
                                                PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                        .setUrl(redirectUrl)
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        PaymentLink paymentLink = PaymentLink.create(paymentLinkCreateParams);

        return paymentLink.getUrl();
    }
}
