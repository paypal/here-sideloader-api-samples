//
//  PPViewController.m
//  PPHere
//
//  Created by Messerschmidt, Tim on 14.02.13.
//  Copyright (c) 2013 PayPal Developer. All rights reserved.
//

#import "PPViewController.h"
#import "JSONKit.h"

@interface PPViewController ()

@end

@implementation PPViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.taxField.delegate = self;
    self.priceField.delegate = self;
    self.quantityField.delegate = self;
    self.nameField.delegate = self;
    self.descriptionField.delegate = self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}


- (IBAction)onLaunchPayment:(id)sender {
    NSLog(@"Launching");
    NSMutableDictionary *shirt = [NSMutableDictionary dictionary];
    NSMutableDictionary *itemList = [NSMutableDictionary dictionary];
    NSMutableDictionary *invoice = [NSMutableDictionary dictionary];
    
    [shirt setObject:self.taxField.text forKey:@"taxRate"];
    [shirt setObject:self.priceField.text forKey:@"unitPrice"];
    [shirt setObject:self.quantityField.text forKey:@"quantity"];
    [shirt setObject:self.nameField.text forKey:@"name"];
    [shirt setObject:self.descriptionField.text forKey:@"description"];
    [shirt setObject:@"Tax" forKey:@"taxName"];
    
    NSMutableArray *items = [NSMutableArray arrayWithObject:shirt];
    [itemList setObject:items forKey:@"item"];
    
    [invoice setObject:@"DueOnReceipt" forKey:@"paymentTerms"];
    [invoice setObject:@"0" forKey:@"discountPercent"];
    [invoice setObject:@"USD" forKey:@"currencyCode"];
    [invoice setObject:@"merchant@ebay.com" forKey:@"merchantEmail"];
    [invoice setObject:@"foo@bar.com" forKey:@"payerEmail"];
    [invoice setObject:itemList forKey:@"itemList"];
    
    NSString *jsonInvoice = [invoice JSONString];
    NSString *encodedInvoice = [jsonInvoice stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
    
    NSString *encodedPaymentTypes = [@"cash,card,paypal" stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
    
    NSString *encodedReturnUrl = [@"myapp://handler?{result}?Type={Type}&InvoiceId={InvoiceId}&Tip={Tip}&Email={Email}&TxId={TxId}" stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
    
    NSString *pphUrlString = [NSString stringWithFormat:@"paypalhere://takePayment?accepted=%@&returnUrl=%@&invoice=%@&step=choosePayment",
                              encodedPaymentTypes, encodedReturnUrl, encodedInvoice];
    
    NSURL *pphUrl = [NSURL URLWithString:pphUrlString];
    
    NSLog(@"%@", pphUrlString);
    
    UIApplication *application = [UIApplication sharedApplication];
    //iOS 9 -> Don't forget to add this schema to info.plist as a white schema (http://useyourloaf.com/blog/querying-url-schemes-with-canopenurl.html)
    if ([application canOpenURL:[NSURL URLWithString:@"paypalhere://"]]){
        [application openURL:pphUrl];
    } else {
        NSURL *url = [NSURL URLWithString:@"itms://itunes.apple.com/us/app/paypal-here/id505911015?mt=8"];
        [application openURL:url];
    }
    
}

@end
