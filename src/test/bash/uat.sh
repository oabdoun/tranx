echo "==========================="
echo "= tranx UAT start"
echo

cars='{"amount":5000,"type":"cars"}'
echo "* add cars transaction: $cars"
curl -H 'Content-type:application/json' -d $cars -X PUT 'http://localhost:8080/transactionservice/transaction/10'
echo; echo

shopping='{"amount":10000,"type":"shopping","parent_id":10}'
echo "* add shopping transaction: $shopping"
curl -H 'Content-type:application/json' -d $shopping -X PUT 'http://localhost:8080/transactionservice/transaction/11'
echo; echo

echo "* get cars transactions"
curl -X GET 'http://localhost:8080/transactionservice/type/cars'
echo; echo

echo "* get sum for transaction 10"
curl -X GET 'http://localhost:8080/transactionservice/sum/10'
echo; echo

echo "* get sum for transaction 11"
curl -X GET 'http://localhost:8080/transactionservice/sum/11'
echo; echo

echo "= tranx UAT end"
echo "==========================="
