package com.mucyomiller.shoppingcart.model;

import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Implements this interface for any product which can be added to shopping cart
 */
public interface Saleable extends Parcelable{
    BigDecimal getPrice();

    String getName();

}
