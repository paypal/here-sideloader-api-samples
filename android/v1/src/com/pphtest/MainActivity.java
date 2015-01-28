/*
* Copyright 2013 eBay Inc
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.pphtest;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author aminaei
 * @author tmesserschmidt
 */
@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	private final static String		TAG						= "PPHere";

	private EditText				amountText;
	private EditText				nameText;
	private Button					currencyButton;
	private final int				CURRENCY_DIALOG			= 1;
	private final int				INVALID_AMOUNT_DIALOG	= 2;
	private final CharSequence[]	CURRENCIES				= {
			"AUD", "BRL", "CAD", "CHF", "CZK", "DKK", "EUR", "GBP", "HKD",
			"HUF", "ILS", "JPY", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN",
			"SEK", "SGD", "THB", "TED", "USD"
															};

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		amountText = (EditText) findViewById(R.id.amountText);
		nameText = (EditText) findViewById(R.id.nameText);
		currencyButton = (Button) findViewById(R.id.currencyButton);
	}

	public void changeCurrency(View view) {
		showDialog(CURRENCY_DIALOG);
	}

	public void continuePayment(View view) {
		String url = "paypalhere://takePayment/?returnUrl={{returnUrl}}&invoice=%7B%22merchantEmail%22%3A%22%22,%22payerEmail%22%3A%22spotireddy-biz%40paypal.com%22,%22itemList%22%3A%7B%22item%22%3A%5B%7B%22name%22%3A%22{{name}}%22,%22description%22%3A%22{{description}}%22,%22quantity%22%3A%221.0%22,%22unitPrice%22%3A%22{{price}}%22,%22taxName%22%3A%22Tax%22,%22taxRate%22%3A%220.0%22%7D%5D%7D,%22currencyCode%22%3A%22{{currency}}%22,%22paymentTerms%22%3A%22DueOnReceipt%22,%22discountPercent%22%3A%220.0%22%7D";
		String returnUrl = "hailocabtest://paymentResult/?{result}?Type={Type}&InvoiceId={InvoiceId}&Tip={Tip}&Email={Email}&TxId={TxId}";
		String priceString = amountText.getText().toString();
		String nameString = nameText.getText().toString();
		String currency = currencyButton.getText().toString();

		try {
			Float.parseFloat(priceString);
		} catch (Exception e) {
			showDialog(INVALID_AMOUNT_DIALOG);
			return;
		}

		returnUrl = URLEncoder.encode(returnUrl);

		url = url.replace("{{returnUrl}}", returnUrl);
		url = url.replace("{{price}}", priceString);
		url = url.replace("{{name}}", nameString);
		url = url.replace("{{description}}", nameString);
		url = url.replace("{{currency}}", currency);

		Log.v(TAG, "URL: " + url);

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = null;
		switch (id) {
			case CURRENCY_DIALOG:
				builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.label_select_currency);
				builder.setSingleChoiceItems(CURRENCIES, 6,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								currencyButton.setText(CURRENCIES[item]);
								dismissDialog(CURRENCY_DIALOG);
							}
						});
				dialog = builder.create();
				break;
			case INVALID_AMOUNT_DIALOG:
				builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.invalid_amount_message)
						.setCancelable(false)
						.setPositiveButton(R.string.label_ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										dismissDialog(INVALID_AMOUNT_DIALOG);
									}
								});
				dialog = builder.create();
				break;
		}

		return dialog;
	}
}
