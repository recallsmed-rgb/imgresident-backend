
package com.imgresident.checkout.dto;

import java.util.List;

import lombok.Data;

@Data
public class CheckoutRequest {
  private List<CheckoutItem> items;

}
