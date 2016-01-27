# Introduction #

In the new version of app there are available 2 modes of changing the MTU because various models of phones have different specifications. In one group the interface after turning off is invisible (like it would be unplugged). I'd like to explain whole problem and pros and cons of all modes.

## On demand ##
(_recommended_)
The MTU will be changed only once in the moment of clicking the Start button. This option is designed for phones which have interface visible all the time (even if it is turned off). So if you can choose the proper interface from the dropdown list and the MTU configuration isn't reset after turning on again, you should select this option.

+ no running background service

- if your phone unplugs the interface, you can't use this mode

## On turning WiFi ##

In this mode the application monitors your WiFi connection and change the MTU automatically after turning on.

+ you don't have to monitor WiFi state on your own

- running background service