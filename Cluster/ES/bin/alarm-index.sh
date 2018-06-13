#!/bin/bash
##curl -XDELETE 's100:9200/alarm?pretty'  -H 'Content-Type: application/json'
curl -XPUT 's100:9200/alarm?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
	    "number_of_shards":5,
        "number_of_replicas":1
    },
        "mappings":{
            "rec_alarm":{
                "properties":{
                    "ipc_id": {
                    "type" : "keyword"
                    },
                    "alarm_type":{
                    "type" : "integer"
                    },
                    "alarm_time":{
                     "type" : "date",
                     "format": "yyyy-MM-dd HH:mm:ss"
                    },
                    "host_name":{
                      "type" : "keyword"
                    },
                    "big_picture_url": {
                      "type" : "keyword"
                    },
                    "small_picture_url":{
                       "type" : "keyword"
                    },
                    "static_id":{
                       "type" : "keyword"
                    },
                    "similarity":{
                       "type": "double"
                    },
                    "object_type":{
                       "type": "keyword"
                    },
                    "flag": {
                       "type" : "integer"
                    },
                    "confirm": {
                       "type" : "integer"
                    },
                    "check_use": {
                       "type" : "keyword"
                    },
                    "suggestion": {
                       "type" : "text"
                    }
                }
            },
            "off_alarm":{
                "properties":{
                    "alarm_type":{
                        "type" : "integer"
                    },
                    "alarm_time":{
                        "type" : "date",
                        "format": "yyyy-MM-dd HH:mm:ss"
                    },
                    "static_id":{
                        "type" : "keyword"
                    },
                    "similarity":{
                        "type": "double"
                    },
                    "object_type":{
                        "type": "keyword"
                    },
                    "last_appearance_time":{
                        "type" : "date",
                        "format": "yyyy-MM-dd HH:mm:ss"
                    },
                    "flag":{
                        "type" : "integer"
                    },
                    "confirm":{
                        "type" : "integer"
                    },
                    "check_use":{
                        "type" : "keyword"
                    },
                    "suggestion":{
                        "type" : "text"
                    }
                }
            }
        }
    }'

curl -XPUT 's100:9200/alarm/_settings' -d '{
    "index": {
        "max_result_window": 1000000000
    }
}'