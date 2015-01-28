/*
 * Invoice.java
 * PayPal_Here_Sample
 *
 * Created by mrakic on 6/16/13 11:28 PM.
 * Copyright (c)  2013 X.commerce Developer Tools.
 * Please see license.txt
 */

package com.paypal.here.sample;


import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//
//  Invoice object to store invoice data
//  for more information about the Invoice object, please refer to
//  https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_CreateInvoice
//
public class Invoice
{
  @SerializedName("paymentTerms")
  private String _paymentTerms;
  @SerializedName("discountPercent")
  private String _discountPercentage;
  @SerializedName("currencyCode")
  private String _currencyCode;
  @SerializedName("merchantEmail")
  private String _merchantEmail;
  @SerializedName("number")
  private String _number;
  @SerializedName("payerEmail")
  private String _payerEmail; // payerEmail is optional, PayPal Here app will fill the value
  @SerializedName("itemList")
  private Items _items;


  public Invoice()
  {
    _items = new Items();
  }


  public void addItemWithName(String itemName, String description, String taxRate, Double unitPrice, String taxName, Double quantity)
  {
    Item item = new Item(itemName, description, quantity, taxRate, taxName, unitPrice);
    _items.add(item);
  }


  public String getNumber()
  {
    return _number;
  }


  public void setNumber(String number)
  {
    _number = number;
  }


  public void addItem(Item item)
  {
    _items.add(item);
  }


  public String getPaymentTerms()
  {
    return _paymentTerms;
  }


  public void setPaymentTerms(String paymentTerms)
  {
    _paymentTerms = paymentTerms;
  }


  public String getDiscountPercentage()
  {
    return _discountPercentage;
  }


  public void setDiscountPercentage(String discountPercentage)
  {
    _discountPercentage = discountPercentage;
  }


  public String getCurrencyCode()
  {
    return _currencyCode;
  }


  public void setCurrencyCode(String currencyCode)
  {
    _currencyCode = currencyCode;
  }


  public String getMerchantEmail()
  {
    return _merchantEmail;
  }


  public void setMerchantEmail(String merchantEmail)
  {
    _merchantEmail = merchantEmail;
  }


  public String getPayerEmail()
  {
    return _payerEmail;
  }


  public void setPayerEmail(String payerEmail)
  {
    _payerEmail = payerEmail;
  }


  public List<Item> getItemList()
  {
    return _items._itemList;
  }


  public int getItemsCount()
  {
    return _items.getItemsCount();
  }


  public static class Items
  {
    @SerializedName("item")
    private List<Item> _itemList;


    public Items()
    {
      _itemList = new ArrayList<Item>();
    }


    public void add(Item item)
    {
      _itemList.add(item);
    }


    public int getItemsCount()
    {
      return _itemList.size();
    }
  }


  /*
  Helper class used to serialize Invoice object to JSON string
  e.g.
  {
   "currencyCode" : "USD",
   "discountPercent" : "0",
   "itemList" :
      {
         "item" :
            [
               {
                  "description" : "Blue T-shirt",
                  "name" : "T-Shirt",
                  "quantity" : 1.0,
                  "taxName" : "Sales Tax",
                  "taxRate" : "8.50",
                  "unitPrice" : 15.99
               }
            ]
      },
   "merchantEmail" : "merchant@ebay.com",
   "number" : "9876",
   "payerEmail" : "foo@bar.com",
   "paymentTerms" : "DueOnReceipt"
  }
  */
  public static class Converter
  {
    public static String serializeToUrlEncodedJSonString(Invoice invoice)
    {
      Gson gson = new Gson();
      String invoiceJson = gson.toJson(invoice);
      return Uri.encode(invoiceJson);
    }


    public static String toUrlEncodedJSonString(Invoice invoice)
    {
      // Create invoice
      JSONObject invoiceJson = new JSONObject();
      try
      {
        JSONObject itemList = new JSONObject();
        JSONArray itemListArray = new JSONArray();

        List<Item> items = invoice.getItemList();
        // Create an item
        for (Item item : items)
        {
          JSONObject itemJson = new JSONObject();
          itemJson.put("description", item.getDescription());
          itemJson.put("taxRate", item.getTaxRate());
          itemJson.put("name", item.getName());
          itemJson.put("unitPrice", item.getUnitPrice());
          itemJson.put("taxName", item.getTaxName());
          itemJson.put("quantity", item.getQuantity());
          // Add the item to the items lis
          itemListArray.put(item);
        }
        itemList.put("item", itemListArray);

        // Add invoice details including items from above
        invoiceJson.put("paymentTerms", invoice.getPaymentTerms());
        invoiceJson.put("discountPercent", invoice.getDiscountPercentage());
        invoiceJson.put("currencyCode", invoice.getCurrencyCode());
        invoiceJson.put("number", invoice.getNumber());
        invoiceJson.put("merchantEmail", invoice.getMerchantEmail());
        invoiceJson.put("payerEmail", invoice.getPayerEmail());
        invoiceJson.put("itemList", itemList);
      }
      catch (JSONException e)
      {
        e.printStackTrace();
        //handle JSON exception here
      }

      return Uri.encode(invoiceJson.toString());
    }
  }
}
