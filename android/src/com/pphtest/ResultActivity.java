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

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author aminaei
 * @author tmesserschmidt
 */
public class ResultActivity extends Activity {
	private TextView	txResultMessageText;
	private ImageView	txResultImage;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.result);

		txResultImage = (ImageView) findViewById(R.id.txResultImage);
		txResultMessageText = (TextView) findViewById(R.id.txResultMessage);
	}

	@Override
	protected void onResume() {
		super.onResume();
		handlePassedData();
	}

	public void displayHome(View view) {
		finish();
	}

	private void handlePassedData() {
		Uri data = getIntent().getData();

		if (data != null) {
			String host = data.getHost();

			if ("paymentResult".equals(host)) {
				String type = data.getQueryParameter("Type");
				String invoiceId = data.getQueryParameter("InvoiceId");

				TextView typeLabel = (TextView) findViewById(R.id.paymentTypeLabel);
				TextView invoiceIdLabel = (TextView) findViewById(R.id.invoiceIdLabel);

				if ("Cancel".equalsIgnoreCase(type)) {
					typeLabel.setText("Payment Cancelled");
					invoiceIdLabel.setText("");
					txResultImage.setImageResource(R.drawable.red_cross);
					txResultMessageText.setText(R.string.message_tx_cancelled);
				} else {
					typeLabel.setText("Payment Type: " + type);
					invoiceIdLabel.setText("Invoice Id: " + invoiceId);
					txResultImage.setImageResource(R.drawable.green_checkmark);
					txResultMessageText.setText(R.string.message_tx_complete);
				}
			}
		}
	}
}
