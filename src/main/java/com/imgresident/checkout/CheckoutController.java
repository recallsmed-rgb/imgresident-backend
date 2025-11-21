
package com.imgresident.checkout;

import com.imgresident.checkout.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

  @PostMapping("/create")
  public ResponseEntity<CheckoutResponse> createCheckout(@RequestBody CheckoutRequest request) {
    // TODO: integrate with Shopify.
    CheckoutResponse response = new CheckoutResponse();
    response.setCheckoutUrl("https://shopify.com/your-store/checkout/dummy-session");
    return ResponseEntity.ok(response);
  }
}
