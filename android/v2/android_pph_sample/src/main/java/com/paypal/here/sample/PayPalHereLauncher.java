/*
 * PayPalHereLauncher.java
 * PayPal_Here_Sample
 *
 * Created by mrakic on 6/16/13 11:28 PM.
 * Copyright (c)  2013 X.commerce Developer Tools.
 * Please see license.txt
 */

package com.paypal.here.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.MessageFormat;

public class PayPalHereLauncher extends Activity
{
  public static final String MARKET_URL = "market://details?id=com.paypal.here";
  private static final String PPH_URL_STRING = "paypalhere://takePayment/v2/?accepted={0}&returnUrl={1}&invoice={2}&step=choosePayment&payerPhone={3}";
  private static final String RETURN_URL = "pphsample://handleResponse/?Type={Type}&InvoiceId={InvoiceId}&Tip={Tip}&Email={Email}&TxId={TxId}";
  private static final String ACCEPTED_PAYMENT_TYPES = "cash,card,paypal";
  private static final String RESPONSE_HOST = "handleResponse";
  private Invoice _invoice;
  private EditText _invoiceNumber;
  private EditText _paymentTerms;
  private EditText _payerEmail;
  private EditText _payerPhone;
  private EditText _merchantEmail;
  private TextView _itemCount;
  private EditText _discount;
  private EditText _currencyCode;

  private static final int ADD_ITEM_REQUEST_CODE = 100;


  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.paypal_here_launcher);

    _currencyCode = (EditText) findViewById(R.id.currency_code);
    _invoiceNumber = (EditText) findViewById(R.id.invoice_number);
    _discount = (EditText) findViewById(R.id.discount);
    _itemCount = (TextView) findViewById(R.id.item_count);
    _merchantEmail = (EditText) findViewById(R.id.merchant_email);
    _payerEmail = (EditText) findViewById(R.id.payer_email);
    _payerPhone = (EditText) findViewById(R.id.payer_phone);
    _paymentTerms = (EditText) findViewById(R.id.payment_terms);

    initializeDefaultDataList();
  }


  @Override
  protected void onNewIntent(Intent intent)
  {
    super.onNewIntent(intent);
    handleOpenURL(intent);
  }


  private void initializeDefaultDataList()
  {
    _invoice = new Invoice();
    _invoice.addItemWithName("T-Shirt", "I Love APIs shirt", "8.50", 9.99, "Sales Tax", 1.0);
    _itemCount.setText(String.valueOf(_invoice.getItemsCount()));
  }


  private void handleOpenURL(Intent intent)
  {
    Uri data = intent.getData();
    if (data == null)
    {
      return;
    }

    String host = data.getHost();
    if (RESPONSE_HOST.equals(host))
    {
      String response = data.toString();
      AlertDialog.Builder responseDialog = new AlertDialog.Builder(this);
      responseDialog.setMessage(response).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)
        {
          dialogInterface.dismiss();
        }
      });
      responseDialog.show();
    }
  }


  public void addItem(View view)
  {
    Intent intent = new Intent(this, AddItem.class);
    startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK)
    {
      Bundle bundle = data.getBundleExtra(Item.class.getSimpleName());
      Item item = Item.Converter.fromBundle(bundle);
      _invoice.addItem(item);
      _itemCount.setText(String.valueOf(_invoice.getItemsCount()));
    }
  }


  public void launchPayPalHere(View view)
  {
    _invoice.setPayerEmail(_payerEmail.getText().toString());
    _invoice.setMerchantEmail(_merchantEmail.getText().toString());
    _invoice.setCurrencyCode(_currencyCode.getText().toString());
    _invoice.setDiscountPercentage(_discount.getText().toString());
    _invoice.setPaymentTerms(_paymentTerms.getText().toString());
    _invoice.setNumber(_invoiceNumber.getText().toString());
    String phone = _payerPhone.getText().toString();

    String urlEncodedReturnUrl = Uri.encode(RETURN_URL);
    String urlEncodedPaymentTypes = Uri.encode(ACCEPTED_PAYMENT_TYPES);

    String urlEncodedInvoice = Invoice.Converter.serializeToUrlEncodedJSonString(_invoice);
    String pphUrl = MessageFormat.format(PPH_URL_STRING, urlEncodedPaymentTypes, urlEncodedReturnUrl, urlEncodedInvoice, phone);

    Intent pphIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pphUrl));
    PackageManager packageManager = getPackageManager();
    ResolveInfo resolveInfo = packageManager.resolveActivity(pphIntent, PackageManager.MATCH_DEFAULT_ONLY);
    if (resolveInfo == null)
    {
      AlertDialog.Builder pphNotFoundDialog = new AlertDialog.Builder(this);
      pphNotFoundDialog.setTitle("PayPal Here app Not Found!").setMessage("Install from Google Play");
      pphNotFoundDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)
        {
          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL)));
          dialogInterface.dismiss();
        }
      });
      pphNotFoundDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)
        {
          dialogInterface.dismiss();
        }
      });
      pphNotFoundDialog.show();
    }
    else
    {
      startActivity(pphIntent);
    }
  }
}
