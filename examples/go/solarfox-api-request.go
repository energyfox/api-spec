package main

import (
	"bytes"
	"encoding/json"
	"io/ioutil"
	"log"
	"net/http"
	"time"
)

// JSONMap represents a JSON object in go
type JSONMap map[string]interface{}

func main() {
	// API endpoint
	endPoint := "http://api.solar-fox.com/import"

	// Define request JSON
	data := JSONMap{
		"Headquarters": JSONMap{
			"WR-118412E": JSONMap{
				"type": "ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE",
				"readings": []JSONMap{JSONMap{
					"timestamp": time.Now().Unix(),
					"value":     128013.0,
				}},
			},
		},
	}

	// Encode JSON
	b, err := json.Marshal(data)
	if err != nil {
		panic(err)
	}

	// Prepare request
	req, err := http.NewRequest("POST", endPoint, bytes.NewBuffer(b))
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "test")

	// Execute
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		panic(err)
	}

	// Read body
	body, _ := ioutil.ReadAll(resp.Body)
	defer resp.Body.Close()

	// Print response
	log.Printf("Status: %s\n", resp.Status)
	log.Printf("Header: %v\n", resp.Header)
	log.Printf("Body: %s\n", string(body))
}
