# tranx [![Build Status](https://snap-ci.com/oabdoun/tranx/branch/master/build_image)](https://snap-ci.com/oabdoun/tranx/branch/master)

Number26 code challenge solution

## introduction

tranx is implemented using the [dropwizard](http://www.dropwizard.io/0.9.2/docs/) framework, saving a lot of boilerplate code for delivering HTTP, REST, JSON serialization, validation.

it uses [gradle](http://gradle.org/) as its build tool and [snap-ci](https://snap-ci.com/) as its continuous integration platform.

because i had to reboot my java dev environment from scratch, i developed it using "Sublime Text", with no "import" optimization...

## running

to run the test suite:
```
$ gradle test
```

to run the webservice:
```
$ gradle start
```

then you can run the UAT curl-based scenario in another console:
```
$ . src/test/bash/uat.sh 
```

press `Ctrl+C` for stopping the webservice...

## known issues

because [jackson](http://wiki.fasterxml.com/JacksonHome) uses java's default value when deserializing a missing property, the default parent_id of a transaction is '0' when no parent transaction is given. hence, transaction ID '0' becomes a special value... for the same reason, getting a transaction with no parent ID will show '0' in parent_id field:
```
PUT transaction/32 { "amount": 12, "type": "foo"} => { "status": "ok" }
GET transaction/32 => { "amount": 12, "type": "foo", "transaction_id": 0 }
```

also, the `PUT transaction/$txid` endpoint was interpreted as meaning "set or replace transaction with ID `$txid`". another possibility would be to forbid replace, by using `ConcurrentMap::putIfAbsent` instead of `Map::put` in `TransactionService::setTransaction`.
