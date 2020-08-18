import sys, os, base64, datetime, hashlib, hmac 
import requests

# ************* REQUEST VALUES *************
method = 'POST'
service = 'dynamodb'
host = 'dynamodb.us-west-2.amazonaws.com'
region = 'us-west-2'
endpoint = 'https://dynamodb.us-west-2.amazonaws.com/'
content_type = 'application/x-amz-json-1.0'
amz_target = 'DynamoDB_20120810.CreateTable'

request_parameters =  '{'
request_parameters +=  '"KeySchema": [{"KeyType": "HASH","AttributeName": "Id"}],'
request_parameters +=  '"TableName": "TestTable","AttributeDefinitions": [{"AttributeName": "Id","AttributeType": "S"}],'
request_parameters +=  '"ProvisionedThroughput": {"WriteCapacityUnits": 5,"ReadCapacityUnits": 5}'
request_parameters +=  '}'


canonical_uri = '/'

canonical_querystring = ''

canonical_headers = 'content-type:' + content_type + '\n' + 'host:' + host + '\n'
canonical_request = method + '\n' + canonical_uri + '\n' + canonical_querystring + '\n' + canonical_headers + '\n'


print('\nBEGIN REQUEST++++++++++++++++++++++++++++++++++++')
print('Request URL = ' + endpoint)

r = requests.post(endpoint, data=request_parameters)

print('\nRESPONSE++++++++++++++++++++++++++++++++++++')
print('Response code: %d\n' % r.status_code)
print(r.text)
 


