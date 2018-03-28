#!/bin/bash
##动态库抓拍统计索引（为了数据可视化使用）
#curl -XDELETE 's103:9200/dynamicshow?pretty'  -H 'Content-Type: application/json'
curl -XPUT 's103:9200/dynamicshow?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
	    "number_of_shards":5,
        "number_of_replicas":1,
        "analysis": {
            "filter": {
                "trigrams_filter": {
                    "type":     "ngram",
                    "min_gram": 2,
                    "max_gram": 20
                }
            },
            "analyzer": {
                "trigrams": {
                    "type":      "custom",
                    "tokenizer": "standard",
                    "filter":   [
                        "lowercase",
                        "trigrams_filter"
                    ]
                },
                "ik": {
                    "tokenizer" : "ik_max_word"
                }
            }
        }
    },
    "mappings": {
         "person": {
              "properties": {
                    "ipcid" : {
                                    "type" : "text"
                              },
                              "time" : {
                                    "type" : "keyword"
                               },
                              "count" : {
                                    "type" : "long"
                              }
                    }
              }
        }
    }
'

curl -XPUT 's103:9200/dynamicshow/_settings' -d '{
    "index": {
        "max_result_window": 1000000000
    }
}'

##静态信息库objectinfo表的映射
#curl -XDELETE 's100:9200/objectinfo?pretty' -H 'Content-Type: application/json'
curl -XPUT 's100:9200/objectinfo?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
	    "number_of_shards":5,
        "number_of_replicas":1,
        "analysis": {
            "filter": {
                "trigrams_filter": {
                    "type":     "ngram",
                    "min_gram": 4,
                    "max_gram": 20
                }
            },
            "analyzer": {
                "trigrams": {
                    "type":      "custom",
                    "tokenizer": "standard",
                    "filter":   [
                        "lowercase",
                        "trigrams_filter"
                    ]
                },
                "ik": {
                    "tokenizer" : "ik_max_word"
                }
            }
        }
    },
    "mappings": {
        "person": {
            "properties": {
                "name" : {
                    "type" : "text",
                    "analyzer" : "ik_max_word",
                    "fielddata" : true
                },
                "idcard" : {
                    "type" : "text",
                    "analyzer": "trigrams",
                    "fielddata" : true
                },
                "sex" : {
                    "type" : "long",
                    "index" : "not_analyzed"
                },
                "reson" : {
                    "type" : "text",
                    "analyzer" : "ik_max_word"
                },
                "pkey" : {
                    "type" : "text",
                    "index" : "not_analyzed"
                },
                "tag" : {
                    "type" : "text",
                    "index" : "not_analyzed"
                },
                "creator" : {
                    "type" : "text",
                    "analyzer" : "ik_max_word"
                },
                "cphone" : {
                    "type" : "text",
                    "analyzer": "trigrams"
                },
                "platformid" : {
                    "type" : "text",
                    "index" : "not_analyzed"
                },
                "feature" : {
                    "type" : "text",
                    "index" : "not_analyzed"
                },
                "createtime" : {
                    "type" : "date",
                    "format": "yyyy-MM-dd HH:mm:ss"
                },
                "updatetime" : {
                    "type" : "date",
                    "format": "yyyy-MM-dd HH:mm:ss"
                }
            }
        }
    }
}
'

curl -XPUT 's105:9200/objectinfo/_settings' -d '{
    "index": {
        "max_result_window": 1000000000
    }
}'
curl -XPUT "http://s105:9200/objectinfo/_mapping/person/" -H 'Content-Type: application/json' -d'
{
  "properties": {
    "pkey": {
      "type":     "text",
      "fielddata": true
    }
  }
}'

## 动态信息库dynamic表的映射
#curl -XDELETE 's109:9200/dynamic?pretty'  -H 'Content-Type: application/json'
curl -XPUT 's109:9200/dynamic?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
	    "number_of_shards":5,
        "number_of_replicas":1,
        "analysis": {
            "filter": {
                "trigrams_filter": {
                    "type":     "ngram",
                    "min_gram": 2,
                    "max_gram": 20
                }
            },
            "analyzer": {
                "trigrams": {
                    "type":      "custom",
                    "tokenizer": "standard",
                    "filter":   [
                        "lowercase",
                        "trigrams_filter"
                    ]
                },
                "ik": {
                    "tokenizer" : "ik_max_word"
                }
            }
        }
    },
    "mappings": {
         "person": {
              "properties": {
                    "ftpurl" : {
                                          "type" : "text"
                              },
                              "eyeglasses" : {
                                           "type" : "long"
                               },
                              "gender" : {
                                           "type" : "long"
                              },
                              "haircolor" : {
                                           "type" : "long"
                              },
                              "hairstyle" : {
                                           "type" : "long"
                              },
                              "hat" : {
                                           "type" : "long"
                              },
                              "huzi" : {
                                          "type" : "long"
                              },
                              "tie" : {
                                           "type" : "long"
                              },
                              "ipcid" : {
                                           "type" : "text"
                              },
                              "timeslot" : {
                                           "type" : "long"
                              },
                              "date" : {
                                            "type" : "text"
                              },
                              "exacttime" : {
                                  "type" : "date",
                                  "format": "yyyy-MM-dd HH:mm:ss"
                              },
                              "searchtype" : {
                                  "type" : "text"
                              }
                              "clusterid" : {
                                  "type" : "text"
                              }
                              "alarmid" : {
                                  "type" : "long"
                              }
                              "alarmtime" : {
                                  "type" : "text"
                              }
                    }
              }
        }
    }
'

curl -XPUT 's109:9200/dynamic/_settings' -d '{
    "index": {
        "max_result_window": 1000000000
    }
}'