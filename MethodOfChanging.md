The application was prepared for different methods of change the MTU parameter. Now only one is available but in the future it should be changed.

## Ifconfig ##

In this method the program execute shell command:
```
ifconfig $INTERFACE_NAME mtu $MTU
```
where $INTERFACE\_NAME and $MTU are respectively: name of the interface and the target MTU value from the configuration of app. Naturally the command is being executed as the root user.

_...next methods soon..._