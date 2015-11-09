<?php

  // Get cURL resource
  $ch = curl_init();

  // Create body
  $body = json_encode([
    "Headquarters" => [
      "WR-118412E" => [
        "type" => "ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE",
        "readings" => [
          [
            "timestamp" => time(),
            "value" => 127932.0
          ]
        ]
      ]
    ]
  ]);

  // Set opts
  curl_setopt_array($ch, [
    CURLOPT_URL => 'http://api.solar-fox.com/import',
    CURLOPT_CUSTOMREQUEST => 'POST',
    CURLOPT_RETURNTRANSFER => 1,
    CURLOPT_HTTPHEADER => ["Content-Type: application/json", "Authorization: test"],
    CURLOPT_POST => 1,
    CURLOPT_POSTFIELDS => $body,
  ]);

  // Send the request & save response to $resp
  $resp = curl_exec($ch);

  if(!$resp) {
    die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
  } else {
    echo "Status: " . curl_getinfo($ch, CURLINFO_HTTP_CODE) . PHP_EOL;
    echo "Body: " . $resp;
  }

  // Close request to clear up some resources
  curl_close($ch);

?>
