# API

A central gateway to send consumption- or output data of various energy types
to the Solarfox® and Energyfox® platform.

## Table of contents

- [Endpoint](#endpoint)
- [Request](#request)
- [Response](#response)
- [Authentication](#authentication)
- [Data types](#data-types)
- [Restrictions](#restrictions)
- [Examples](#examples)

## Endpoint

Each request should be sent to the following URL:
`https://api.solar-fox.com/import`.

## Request

Consumption- or output data can be sent as single requests or bundled.
The data format for request and response is [JSON](http://json.org).

A minimal request has the following structure:
```json
{
  "<GROUP_NAME>": {
    "<METER_NAME>": {
      "type": "<TYPE>",
      "readings": [
        {
          "timestamp": <TIMESTAMP>,
          "value": <VALUE>
        }
      ]
    }
  }
}
```

- `type`: The [data type](#data-types) of the transmitted value
- `timestamp`: The unix timestamp of the reading
- `value`: A floating number in the unit described in [data types](#data-types)

Every meter is assigned to a group.  A group can have an unlimited number of
meters.
The names `<GROUP_NAME>` and `<METER_NAME>` are free to be choosen, but the
combination of both has to identify a meter.
Every transmission for the meter `<METER_NAME` has to be sent with the correct
name and group.

This is an example of a property with one inverter:
```js
// First request
{
  "Headquarters": {
    "WR-118412E": {
      "type": "ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE",
      "readings": [
        {
          "timestamp": 1439215363,
          "value": 127932.0
        }
      ]
    }
  }
}
```

```js
// Next request after 15min
{
  "Headquarters": {
    "WR-118412E": {
      "type": "ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE",
      "readings": [
        {
          "timestamp": 1439216263,
          "value": 128013.0
        }
      ]
    }
  }
}
```

A bundled request with data from two properties with multiple meters should
look like this:

```js
{
  "<GROUP_NAME>": {
    "<METER_NAME>": {
      "type": "<TYPE>",
      "readings": [
        {"timestamp": <TIMESTAMP>, "value": <VALUE>}
        // ...
      ]
    },
    "<METER_NAME>": {
      "type": "<TYPE>",
      "readings": [
        {"timestamp": <TIMESTAMP>, "value": <VALUE>}
        // ...
      ]
    }
  },
  "<GROUP_NAME>": {
    "<METER_NAME>": {
      "type": "<TYPE>",
      "readings": [
        {"timestamp": <TIMESTAMP>, "value": <VALUE>}
        // ...
      ]
    },
    "<METER_NAME>": {
      "type": "<TYPE>",
      "readings": [
        {"timestamp": <TIMESTAMP>, "value": <VALUE>}
        // ...
      ]
    },
    "<METER_NAME>": {
      "type": "<TYPE>",
      "readings": [
        {"timestamp": <TIMESTAMP>, "value": <VALUE>}
        // ...
      ]
    }
  }
}
```

### Response

The response-object has either a `success` or an `error` key.

- `success`: Written to db (HTTP Status Code `200`)
- `error`: Bad request or Invalid JSON (HTTP Status Code `400`)

In case of an `error`, most likely one of those issues occured:
- Request has an invalid syntax ([lint](http://jsonlint.com) your JSON and
 check your [request](#request)-format)
- [Authentication](#authentication) failed (check your token)
- Invalid [datatype](#datatype)

## Authentication

Every user activated by the SOLEDOS GmbH receives a token which can be used
to authenticate requests.

Every POST-request has to have the following header:
`'Authorization': '<SECRET_TOKEN>'`

To get a token, please contact info@solar-fox.de.

**The token is to be kept secret.`**

## Data types

The following data types are currently supported:

- `ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE`:
  Data with this type should have the unit `Wh` (watt-hours) and represent
  a meter reading.

- `ELECTRICITY_CONSUMPTION_ENERGY_DELTA`:
  Data with this type describe the change of a meter reading in relation to
  the previous reading and should have the unit `Wh` (watt-hours).
  The delta-readings should be sent in a constant interval.

- `ELECTRICITY_CONSUMPTION_POWER`:
  Data with this type represent the current power value in `W` (watt).

- `ELECTRICITY_GENERATION_ENERGY_ABSOLUTE`:
  See `ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE`

- `ELECTRICITY_GENERATION_ENERGY_DELTA`:
  See `ELECTRICITY_CONSUMPTION_ENERGY_DELTA`

- `ELECTRICITY_GENERATION_POWER`:
  See `ELECTRICITY_CONSUMPTION_POWER`
  
- `ELECTRICITY_GRIDIN_ENERGY_ABSOLUTE`:
  Data with this type should have the unit `Wh` (watt-hours) and represent the 
  total amount fed into grid.

- `ELECTRICITY_GRIDIN_ENERGY_DELTA`:
  Data with this type describe the change of the total amount fed into grid 
  since the last reading and should have the unit `Wh` (watt-hours).
  
- `ELECTRICITY_GRIDIN_POWER`:
  Data with this type represent the current power which gets fed into grid 
  in `W` (watt).
  
- `ELECTRICITY_GRIDOUT_ENERGY_ABSOLUTE`:
  Data with this type should have the unit `Wh` (watt-hours) and represent the 
  total amount received from grid.

- `ELECTRICITY_GRIDOUT_ENERGY_DELTA`:
  Data with this type describe the change of the total amount received from grid 
  since the last reading and should have the unit `Wh` (watt-hours).
  
- `ELECTRICITY_GRIDOUT_POWER`:
  Data with this type represent the current power which gets received from grid 
  in `W` (watt).
  
- `ELECTRICITY_BATIN_ENERGY_ABSOLUTE`:
  Data with this type should have the unit `Wh` (watt-hours) and represent the 
  total amount fed into the storage system.

- `ELECTRICITY_BATIN_ENERGY_DELTA`:
  Data with this type describe the change of the total amount fed into the storage 
  system since the last reading and should have the unit `Wh` (watt-hours).
  
- `ELECTRICITY_BATIN_POWER`:
  Data with this type represent the current power which gets fed into the storage
  system in `W` (watt).
  
- `ELECTRICITY_BATOUT_ENERGY_ABSOLUTE`:
  Data with this type should have the unit `Wh` (watt-hours) and represent the 
  total amount received from the storage system.

- `ELECTRICITY_BATOUT_ENERGY_DELTA`:
  Data with this type describe the change of the total amount received from the 
  storage system since the last reading and should have the unit `Wh` (watt-hours).
  
- `ELECTRICITY_BATOUT_POWER`:
  Data with this type represent the current power which gets received from the 
  storage system in `W` (watt).
  
- `ELECTRICITY_BAT_STATUS`:
  Data with this type represent the current storage system load state and should have
  the unit `%` (percent).

More types can be added upon request.

## Restrictions

* Every activated user can send up to 1800 requests.
* The request data should not not exceed 32kb

## Examples

Examples can be accessed within the following programming languages:

* [Go](examples/go)
* [Java](examples/java)
* [Node.js](examples/node)
* [PHP](examples/php)
* [Python](examples/python)
* [Ruby](examples/ruby)
