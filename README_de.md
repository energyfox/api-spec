# API

## Übersicht
1 zentrale Schnittstelle zur Annahme von Verbrauchs und/oder 
Ertragsdaten verschiedenster Energieformen.

## Endpoint

Alle Energiedaten werden an folgende URL gesendet:
`https://api.solar-fox.com/import`.

## Senden von Daten

Verbrauchs- oder Ertragsdaten können einzeln an die Schnittstelle übertragen
werden, sobald diese anfallen. Es ist auch möglich, Daten gebündelt zu
übermitteln. Die Daten werden im [JSON](http://json.org) Format übertragen.
1 minimaler Request hat folgende Struktur:

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

- `type` steht für den [Datentypen](#datentypen) des übermittelten Wertes
- `timestamp` steht für 1 Unix Zeitstempel
  (Sekunden seit dem 1.1.1970 0:00 UTC)
- `value` steht für 1 Zahl in Gleitkommadarstellung in 1 Maßeinheit,
  zugehörig zu `type`

Das obige Beispiel zeigt die Daten eines Requests, bei dem 1 Zählerdatum an
die Schnittstelle gesendet wird. Es veranschaulicht außerdem, dass einzelne
Meter immer 1 Gruppe angehören. Gruppen können beliebig viele Zähler
enthalten.

Die Namen `<GROUP_NAME>` und `<METER_NAME>` sind frei wählbar. Die Kombination
aus `<GROUP_NAME>` und `<METER_NAME>` muss 1 Zähler eindeutig
identifizieren. Jede weitere Sendung des Zählers `<METER_NAME>` der Gruppe
`<GROUP_NAME>` muss auch mit diesen Namen durchgeführt werden. Bei 1 Anlage
mit 1 Wechselrichter, kann dann 1 konkreter Request wie folgt aussehen:

```js
// Erster Request
{
  "Anlage Süd-Ost Hessen": {
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
// Nächster Request 15min später
{
  "Anlage Süd-Ost Hessen": {
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

1 Request mit gesammelten Daten zweier Anlagen und jeweils mehreren Zählern kann wie folgt aussehen:

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

### Response empfangen
Wurde 1 Request an die API gestellt, sind drei Responses
möglich:

- `success`: Written to db (HTTP Status Code 200)

   In diesem Fall wurden die Verbrauchs- oder Ertragsdaten erfolgreich an die
   API übertragen und gespeichert

- `error`: Bad request oder Invalid JSON (HTTP Status Code 400)

   In diesem Fall gab es 1 Fehler bei dem Request. Mögliche Ursachen sind:
   - Request hat 1 fehlerhafte Syntax
   - [Authentifizierung(#authentifizierung) ist fehlgeschlagen
     (Sicherheitstoken falsch?)
   - 1 Feld in der JSON Datenstruktur wurde nicht gefunden
   - Datentyp nicht vorhanden oder fehlerhaft

- `error`: Something went wrong (HTTP Status Code 500)
   - 1 interner Fehler auf dem Server ist aufgetreten

## Authentifizierung
Von der SOLEDOS GmbH freigeschaltete Nutzer erhalten 1 Schlüssel
(Sicherheitstoken), welcher ermöglicht, Daten an die Schnittstelle zu senden.
Bei jedem POST-Request muss dazu folgender Eintrag zum HTTP-Header hinzugefügt
werden: `Authorization: SICHERHEITSTOKEN`.

Beispiel: `'Authorization': 'c3VwZXJHZWlsZXJUb2tlbg=='`

Um 1 Sicherheitstoken zu erhalten, wenden sie sich bitte an info@solar-fox.de
**Das Sicherheitstoken muss geheim gehalten werden**

## Datentypen
Derzeit werden folgende Wertetypen unterstützt,

- `ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE`:
  Daten dieses Typs werden werden in **Wh** (Wattstunden) angegen.
  Sie entsprechen 1 Zählerstand.

- `ELECTRICITY_CONSUMPTION_ENERGY_DELTA`:
   Daten dieses Typs beschreiben die Änderung eines Zählerstands zur vorherigen
   Messung. Sie werden in **Wh** (Wattstunden) angegeben. Die einzelnen Daten
   sollten 1 möglichst konstanten zeitlichen Abstand besitzen.

- `ELECTRICITY_CONSUMPTION_POWER`:
   Daten dieses Typs beschreiben den momentanen Leistungswert (die
   Geschwindigkeit) mit der sich die Zählerstände ändern. Der Wert wird in
   `W` (Watt) angegeben.

- `ELECTRICITY_GENERATION_ENERGY_ABSOLUTE`:
   Siehe `ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE`

- `ELECTRICITY_GENERATION_ENERGY_DELTA`:
   Siehe `ELECTRICITY_CONSUMPTION_ENERGY_DELTA`

- `ELECTRICITY_GENERATION_POWER`: Siehe `ELECTRICITY_CONSUMPTION_POWER`

Weitere Typen können nach Anfrage hinzugfügt werden.

## Restriktionen
* Pro Stunde können 1800 Requests über 1 freigeschalteten Nutzer
  (Sicherheitstoken) an die API gestellt werden. Zusätzlich
  wird die IP Adresse jedes Requests geloggt.
* Die maximale Größe eines Requests beträgt 32kb

## Beispiele

Beispiele liegen in den folgenden Sprachen vor:

* [Go](examples/go)
* [Java](examples/java)
* [Node.js](examples/node)
* [PHP](examples/php)
* [python](examples/python)
* [Ruby](examples/ruby)
