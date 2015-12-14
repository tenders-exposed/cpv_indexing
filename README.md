  This folder contains the files necessary to import CPV codes with Logstash and
index them in order to use the auto completion functionality of ElasticSearch.

  The file cpvcodes_utf8.csv is the UTF-8 version of the
   [file here](https://github.com/opented/opented/blob/master/cpvcodes/cpvcodes.csv)

#### Requirements

  * elasticsearch 2.2.0
  * logstash 2.1.1

  See `cpv_import_logstash.conf` for information on how to use it and necessary modifications.

To make the auto completion more relevant we manipulated the query so that the
most general CPV codes get suggested first. To achieve the same thing, include
`boost_by_category.groovy` in your `elasticsearch/config/scripts`. This will allow
a query like:

```
$ curl -XGET 'http://localhost:9200/cpvs/_search?pretty&human' -d '{
  "query":{
    "function_score": {
      "query": {  "match": {"ORIGINAL_CODE":"503"}},
      "functions": [
        {
          "script_score":{
             "script" : {"lang":"groovy", "file":"boost_by_category"}
          }
        }
      ]
    }
  },
  "sort" : [
    "_score",
    { "REAL_CODE" : {"order" : "asc"}}
  ]
}'

```

to return results like:

```javascript
"hits" : {
  "total" : 224,
  "max_score" : null,
  "hits" : [ {
    "_index" : "cpvs",
    "_type" : "cpv",
    "_id" : "AVGhLKJVFPbH746UWNDO",
    "_score" : 22.450527,
    "ORIGINAL_CODE":"50000000-5",
    "PLAIN_CODE":"50000000",
    "NUM_DIGITS":0,
    "REAL_CODE":null,
    "NAME":"Repair and maintenance services",
  }, {
    "_index" : "cpvs",
    "_type" : "cpv",
    "_id" : "AVGhLKJVFPbH746UWNDP",
    "_score" : 22.450386,
    "ORIGINAL_CODE":"50100000-6",
    "PLAIN_CODE":"50100000",
    "NUM_DIGITS":3,
    "REAL_CODE":501,
    "NAME":"Repair, maintenance and associated services of vehicles and related equipment"
  }, {
    "_index" : "cpvs",
    "_type" : "cpv",
    "_id" : "AVGhLKJVFPbH746UWND4",
    "_score" : 22.450386,
    "ORIGINAL_CODE":"50200000-7",
    "PLAIN_CODE":"50200000",
    "NUM_DIGITS":3,
    "REAL_CODE":502,
    "NAME":"Repair, maintenance and associated services related to aircraft, railways, roads and marine equipment"
  }, {
    "_index" : "cpvs",
    "_type" : "cpv",
    "_id" : "AVGhLKJVFPbH746UWNDQ",
    "_score" : 22.450338,
    "ORIGINAL_CODE":"50110000-9",
    "PLAIN_CODE":"50110000",
    "NUM_DIGITS":4,
    "REAL_CODE":5011,
    "NAME":"Repair and maintenance services of motor vehicles and associated equipment"
  }
}
```
