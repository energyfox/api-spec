var http = require('http');

var postData = JSON.stringify({
  "Headquarters": {
    "WR-118412E": {
      "type": "ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE",
      "readings": [
        {
          "timestamp": Math.floor(Date.now() / 1000),
          "value": 128013.0
        }
      ]
    }
  }
});

var options = {
  hostname: 'api.solar-fox.com',
  port: 80,
  path: '/import',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'test'
  }
};

var req = http.request(options, function(res) {
  console.log('Status:', res.statusCode);
  console.log('Headers:', JSON.stringify(res.headers));
  res.setEncoding('utf8');
  res.on('data', function (chunk) {
    console.log('Body:', chunk);
  });
});

req.on('error', function(e) {
  console.error('Error:', e.message);
});

req.write(postData);
req.end();
