/*
 * Item.java
 * PayPal_Here_Sample
 *
 * Created by mrakic on 6/16/13 11:33 PM.
 * Copyright (c)  2013 X.commerce Developer Tools.
 * Please see license.txt
 */

package com.paypal.here.sample;


import java.io.Serializable;

import android.os.Bundle;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable
{
  public static final String NAME = "name";
  public static final String DESC = "desc";
  public static final String QUANTITY = "quantity";
  public static final String TAX_NAME = "taxName";
  public static final String TAX_RATE = "taxRate";
  public static final String PRICE = "price";
  @SerializedName("name")
  private final String _name;
  @SerializedName("description")
  private final String _description;
  @SerializedName("quantity")
  private final Double _quantity;
  @SerializedName("taxRate")
  private final String _taxRate;
  @SerializedName("taxName")
  private final String _taxName;
  @SerializedName("unitPrice")
  private final Double _unitPrice;


  public Item(String name, String description, Double quantity, String taxRate, String taxName, Double unitPrice)
  {
    _name = name;
    _description = description;
    _quantity = quantity;
    _taxRate = taxRate;
    _taxName = taxName;
    _unitPrice = unitPrice;
  }


  public Item(String name, String description, Double quantity, Double unitPrice)
  {
    _name = name;
    _description = description;
    _quantity = quantity;
    _taxRate = null;
    _taxName = null;
    _unitPrice = unitPrice;
  }


  public String getName()
  {
    return _name;
  }


  public String getDescription()
  {
    return _description;
  }


  public Double getQuantity()
  {
    return _quantity;
  }


  public String getTaxRate()
  {
    return _taxRate;
  }


  public String getTaxName()
  {
    return _taxName;
  }


  public Double getUnitPrice()
  {
    return _unitPrice;
  }


  public static final class Converter
  {

    public static Bundle toBundle(Item item)
    {
      Bundle bundle = new Bundle();
      bundle.putSerializable(Item.class.getName(), item);
      return bundle;
    }


    public static Item fromBundle(Bundle bundle)
    {
      Item item = (Item) bundle.getSerializable(Item.class.getName());
      return item;
    }
  }

}
