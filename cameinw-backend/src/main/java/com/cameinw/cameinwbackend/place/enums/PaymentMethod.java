package com.cameinw.cameinwbackend.place.enums;

/**
 * The PaymentMethod enum represents the accepted payment methods at a place.
 * It specifies the available payment options for reservations.
 */
public enum PaymentMethod {
    /**
     * Represents the payment method where only cash is accepted.
     */
    CASH_ONLY,
    /**
     * Represents the payment method where only card payments are accepted.
     */
    CARD_ONLY,
    /**
     * Represents the payment method where both cash and card payments are accepted.
     */
    CASH_AND_CARD
}
