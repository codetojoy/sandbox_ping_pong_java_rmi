
### sandbox_ping_pong_java_rmi

Example of co-dependent services in Java RMI.

### Notes

* Both `PingService` and `PongService` append a message to a `Ball` object, and then call the other service.
    * `Ping` uses `Pong`
    * `Pong` uses `Ping` 
* The `Ball` info object has a max payload, and `Ping` and `Pong` will stop when the max is reached.

### To Run

* in window 0: `gradle :Registry:run`
    * this is a stand-alone which the services will use
* in window 1: `gradle :PingService:run`
* in window 2: `gradle :PongService:run`
* in window 4: `./run_client.sh`
* alternatively, in window 5: `gradle :Monitor:run`
    * will ping services

### Commands

* `i` to use PingService
* `b` to use BillingService
* `c` to use CompoundService
* `r` to list registry
* `q` to quit

### Notes

* `client` must be run _in situ_ because it uses `stdin`
