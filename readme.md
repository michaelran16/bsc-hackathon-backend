curl -X POST -H "Content-Type: application/json" -d '{"fee":10}' http://localhost:8090/contract

curl -X POST -H "Content-Type: application/json" -d '{"fee":5}' http://localhost:8090/contract

curl -X POST -H "Content-Type: application/json" -d '{"amount":1000000,"fromId":1}' http://localhost:8091/transaction
