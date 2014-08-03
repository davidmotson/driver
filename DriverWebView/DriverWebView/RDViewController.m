//
//  RDViewController.m
//  DriverWebView
//
//  Created by Rahul Datta on 8/2/14.
//  Copyright (c) 2014 Rahul Datta. All rights reserved.
//

#import "RDViewController.h"

CLLocationManager *locationManager;
@interface RDViewController ()

@end

@implementation RDViewController

- (void)viewDidLoad
{

    [super viewDidLoad];
	NSLog(@"hello1");
	NSString *fullURL = @"http://localhost:8080/driver";
	NSLog(@"hello2");
    NSURL *url = [NSURL URLWithString:fullURL];
	NSLog(@"hello3");
    NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
	NSLog(@"hello4");
    [_viewWeb loadRequest:requestObj];
	/*locationManager = [[CLLocationManager alloc] init];
    locationManager.distanceFilter = kCLDistanceFilterNone;
    locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters;
    [locationManager startUpdatingLocation];*/
	
	//float latitude = locationManager.location.coordinate.latitude;
	//float longitude = locationManager.location.coordinate.longitude;
	/*float latitude = 37.386541;
	float longitude = -122.067029;
	NSString *latString = [NSString stringWithFormat:@"%f", latitude];
	NSString *longString = [NSString stringWithFormat:@"%f", longitude];
	NSString *latRequest = @"var lat=";
	NSString *lonRequest = @"var lon=";
	NSString *semi = @";";
	NSString *space = @" ";
	NSString *jsInject = [NSString stringWithFormat:@"%@%@%@%@%@%@%@", latRequest,latString, semi, space, lonRequest, longString, semi];
	[_viewWeb stringByEvaluatingJavaScriptFromString:jsInject];*/
}

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
