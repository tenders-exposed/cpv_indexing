/* This script is used to manipulate ElasticSearch scores so that the most general
CPV codes have a higher score. Place it in your elasticsearch_version/config/scripts
to be able to use it in a function_score query */

_score - (1 * doc.NUM_DIGITS / 100000.00)
