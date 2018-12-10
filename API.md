# Consumer Service API Specification


## Exposed URIs

- **/consumer/createUser**
- **/consumer/profile**
- **/consumer/outfits/{username}**
- **/consumer/outfits/**
- **/consumer/content/{outfitId}**
- **/consumer/contents/{outfitId}**
- **/consumer/items/{outfitId}**
- **/consumer/brand**
- **/consumer/retailers**



## 
</br></br>
## /consumer/createUser

### Request Type    

POST

### Request Body
```json
```

### Response Body
```json
```

### Success Response

### Error Response
</br>

## /consumer/profile

### Request Type    

GET

### Request Body
```json
```

### Response Body
```json
```
</br>

## /consumer/outfits/{username}

### Request Type    

GET
    
### Request Parameters

### Request Body
```json
```

### Response Body
```json
{
    "_embedded" : {
        "outfitDtoes" : [{
            "id" : "1C61D521-F38D-4D40-9A0C-4B0DF87D3735",
            "likes" : 0,
            "comments" : "",
            "contents" : [ {
                "id" : "85C6CBC4-D44D-4F56-A9D9-C22A0B23F7E1",
                "coverpicuri" : "tnl6382034883980351480",
                "picture" : {
                    "id" : "DF94BC38-371A-4AA0-BBF8-2301C3C7E2CA",
                    "thumbnailuri" : "tnl6382034883980351480",
                    "smalluri" : "sml8611587555070982371",
                    "largeuri" : "lrg7851693118710460537"
                },
                "items" : [ {
                    "id" : "F4086A24-8B11-4A6A-94E3-359CA6C29A9A",
                    "positionx" : 0.427419,
                    "positiony" : 0.74908,
                    "type" : "fasdf",
                    "size" : "asdf",
                    "retailer" : "1F757C18-BD14-11E8-BCC7-F23C9150975D",
                    "brand" : "FB04516A-BAFE-11E8-BCC7-F23C9150975D"
                }]
            }]
        }]    
    },
    "_links" : {
        "self" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/outfits?filter=&page=0&size=20"
        }
    },
    "page" : {
        "size" : 20,
        "totalElements" : 13,
        "totalPages" : 1,
        "number" : 0
    }
}
```
</br>

## /consumer/outfits/

### Request Type    

GET

### Request Parameters

   | Parameter Name | Default Value |
   | - | - |
   | filter | \"\" |
   | page | 0 |
   | size | 20 |

### Request Body
```json
```

### Response Body
```json
{
    "_embedded" : {
        "outfitDtoes" : [ {
            "id" : "032E4564-0BE4-40C9-873B-A19B27DEAD0F",
            "likes" : 0,
            "comments" : "",
            "contents" : [ ],
            "coverpicuri" : "sml7730456390398575947"
        }]
    },        
    "_links" : {
        "first" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/outfits?filter=all&page=0&size=20"
        },
        "self" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/outfits?filter=all&page=0&size=20"
        },
        "next" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/outfits?filter=all&page=1&size=20"
        },
        "last" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/outfits?filter=all&page=3&size=20"
        }
    },
    "page" : {
        "size" : 20,
        "totalElements" : 76,
        "totalPages" : 4,
        "number" : 0
    }
}
```
</br>

## /consumer/content/{outfitId}

### Request Type    

PUT, POST

### Request Body
```json
{
    "id" : "926B428C-8A33-406D-9C8E-EE9B466464F4",
    "coverpicuri" : "tnl8169660229519365810",
    "picture" : {
        "id" : "a092f15d-2ab3-4e4b-9f45-66a8feb8157a",
        "largeuri" : "lrg7648968760495701807",
        "smalluri" : "sml6795517037858929908",
        "thumbnailuri" : "tnl8169660229519365810",
    },
    "items" : [{
        "brand" : "FB04516A-BAFE-11E8-BCC7-F23C9150975D",
        "id" : "2A2FD06E-46A6-4BF1-A69C-C5C1D9E59084",
        "positionx" : 0.125561,
        "positiony" : 0.03821,
        "retailer" : "1F75E9F0-BD14-11E8-BCC7-F23C9150975D",
        "size" : "s",
        "type" : "asdf"
    }]
}
```

### Response Body
```json
{
    "id" : "926b428c-8a33-406d-9c8e-ee9b466464f4",
    "coverpicuri" : "tnl8169660229519365810",
    "picture" : {
         "id" : "a092f15d-2ab3-4e4b-9f45-66a8feb8157a",
         "thumbnailuri" : "tnl8169660229519365810",
         "smalluri" : "sml6795517037858929908",
         "largeuri" : "lrg7648968760495701807"
    },
    "items" : [{
        "brand" : "FB04516A-BAFE-11E8-BCC7-F23C9150975D",
        "id" : "2A2FD06E-46A6-4BF1-A69C-C5C1D9E59084",
        "positionx" : 0.125561,
        "positiony" : 0.03821,
        "retailer" : "1F75E9F0-BD14-11E8-BCC7-F23C9150975D",
        "size" : "s",
        "type" : "asdf"
    }]
}
```
</br>

## /consumer/contents/{outfitId}

### Request Type    

PUT, POST

### Request Parameters

### Request Body
```json
```

### Response Body
```json
```
</br>

## /consumer/contents/{outfitId}

### Request Type    

GET

### Request Parameters

### Request Body
```json
```

### Response Body
```json
```
</br>

## /consumer/items/{outfitId}

### Request Type    

PUT, POST

### Request Parameters

### Request Body
```json
{
    "BCF18DA8-9DF2-42A1-9A27-72ED15C222F1" : [{
        "id" : "82CCB098-94C7-4F0E-9264-68315A6BD836",
        "brand" : "FB0482E7-BAFE-11E8-BCC7-F23C9150975D",
        "positionx" : 0.7480787643312101,
        "positiony" : 0.4168514568599717,
        "retailer" : "1F757C18-BD14-11E8-BCC7-F23C9150975D",
        "size" : "asdf",
        "type" : "asdf"
    }]
}
```

### Response Body
```json
{
    "id" : "0A49D926-D922-4D79-B390-0CE8699C9C64",
    "likes" : 0,
    "comments" : "",
    "contents" : [ {
        "id" : "BCF18DA8-9DF2-42A1-9A27-72ED15C222F1",
        "coverpicuri" : "tnl5353324419553237848",
        "picture" : {
            "id" : "27704617-870E-49FA-BE99-45060EC320B8",
            "thumbnailuri" : "tnl5353324419553237848",
            "smalluri" : "sml4478001644451872349",
            "largeuri" : "lrg899864000119047900"
        },
        "items" : [ {
            "id" : "82CCB098-94C7-4F0E-9264-68315A6BD836",
            "brand" : "FB0482E7-BAFE-11E8-BCC7-F23C9150975D",
            "positionx" : 0.7480787643312101,
            "positiony": 0.4168514568599717,
            "retailer" : "1F757C18-BD14-11E8-BCC7-F23C9150975D",
            "size" : "asdf",
            "type" : "asdf"
        }]
    } ],
    "coverpicuri" : "sml4478001644451872349"
}
```
</br>

## /consumer/brands

### Request Type    

GET

### Request Body
```json
```

### Response Body
```json
{
    "_embedded" : {
        "brandDtoe" : [{
            "id" : "FB034BB6-BAFE-11E8-BCC7-F23C9150975D",
            "name" : "Zulily",
            "link" : "https://www.zulily.com",
            "red" : 255,
            "blue" : 255,
            "green" : 255
        }]
    },
    "_links" : {
        "first" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/brands?page=0&size=50"
        },
        "self" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/brands?page=0&size=50"
        },
        "next" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/brands?page=1&size=50"
        },
        "last" : {
            "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/brands?page=1&size=50"
        }
    },
    "page" : {
        "size" : 50,
        "totalElements" : 62,
        "totalPages" : 2,
        "number" : 0
    }
}
```
</br>

## /consumer/retailers

### Request Type:    

    GET

### Request Body
```json
```

### Response Body
```json
{
     "_embedded" : {
        "retailerDtoes" : [ {
            "id" : "1F7463CD-BD14-11E8-BCC7-F23C9150975D",
            "name" : "ASOS plc",
            "link" : "http://asos.com', 'http://asosplc.com",
            "red" : null,
            "blue" : null,
            "green" : null
        }]
    },        
    "_links" : {
        "first" : {
          "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/retailers?page=0&size=50"
        },
        "self" : {
          "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/retailers?page=0&size=50"
        },
        "next" : {
          "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/retailers?page=1&size=50"
        },
        "last" : {
          "href" : "http://72.14.177.220/gs-convert-jar-to-war-0.1.0/consumer/retailers?page=1&size=50"
        }
    },
    "page" : {
        "size" : 50,
        "totalElements" : 70,
        "totalPages" : 2,
        "number" : 0
    }
}
```

