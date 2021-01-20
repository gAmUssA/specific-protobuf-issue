# specific-protobuf-issue

* This application only supports running locally - Cloud config not yet supported. 

# Getting Started

### Pre-Requisites

Download and start the confluent platform using Docker.

### Application Description

    
### Running the application

* Invoke the BusinessServiceApplication. This will be listening on port 9001 for 
HTTP requests.

* Once the server is up and running use the following commands:

To generate/publish an EventType1:

* curl -X POST -d 'key=newKey1&field1=field1Value' http://localhost:9001/kafka/publishType1"

To generate/publish an EventType2:

* curl -X POST -d 'key=newKey2&field1=field1Value&field2=field2Value' http://localhost:9001/kafka/publishType2"

