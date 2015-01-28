/*
 * AddItem.java
 * PayPal_Here_Sample
 *
 * Created by mrakic on 6/16/13 11:33 PM.
 * Copyright (c)  2013 X.commerce Developer Tools.
 * Please see license.txt
 */

package com.paypal.here.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddItem extends Activity
{
  private EditText _txtName;
  private EditText _txtDesc;
  private EditText _txtUnitPrice;
  private EditText _txtTaxName;
  private EditText _txtTaxRate;
  private EditText _txtQuantity;
  private Item _item;


  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.add_item);

    _txtName = (EditText) findViewById(R.id.name);
    _txtDesc = (EditText) findViewById(R.id.description);
    _txtUnitPrice = (EditText) findViewById(R.id.price);
    _txtTaxName = (EditText) findViewById(R.id.tax_name);
    _txtTaxRate = (EditText) findViewById(R.id.tax_rate);
    _txtQuantity = (EditText) findViewById(R.id.quantity);
  }


  public void addItem(View view)
  {
    if (validateFields())
    {
      Intent intent = this.getIntent();
      intent.putExtra(Item.class.getSimpleName(), Item.Converter.toBundle(_item));
      this.setResult(RESULT_OK, intent);
      finish();
    }
    else
    {
      Toast toast = Toast.makeText(this, "Required field missing!", Toast.LENGTH_SHORT);
      toast.show();
    }
  }


  private boolean validateFields()
  {
    String name = _txtName.getText().toString();
    String description = _txtDesc.getText().toString();
    String price = _txtUnitPrice.getText().toString();
    String taxName = _txtTaxName.getText().toString();
    String taxRate = _txtTaxRate.getText().toString();
    String quantity = _txtQuantity.getText().toString();

    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(quantity))
    {
      return false;
    }

    if (TextUtils.isEmpty(taxName) || TextUtils.isEmpty(taxRate))
    {
      _item = new Item(name, description, Double.valueOf(quantity), Double.valueOf(price));
    }
    else
    {
      _item = new Item(name, description, Double.valueOf(quantity), taxRate, taxName, Double.valueOf(price));
    }

    return true;
  }
}
