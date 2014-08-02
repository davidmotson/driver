//
//  RDViewController.m
//  DriverWebView
//
//  Created by Rahul Datta on 8/2/14.
//  Copyright (c) 2014 Rahul Datta. All rights reserved.
//

#import "RDViewController.h"

@interface RDViewController ()

@end

@implementation RDViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	[super viewDidLoad];
    NSString *fullURL = @"http://107.150.8.38:8080/driver/";
    NSURL *url = [NSURL URLWithString:fullURL];
    NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
    [_viewWeb loadRequest:requestObj];}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)prefersStatusBarHidden
{
    return YES;
}

@end
