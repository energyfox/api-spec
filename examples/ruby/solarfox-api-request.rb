require 'net/http'
require 'json'
require 'uri'
require 'time'

uri = URI.parse('http://api.solar-fox.com/import')
headers = {'Content-Type' => 'application/json', 'Authorization' => 'test'}
data = {
  Headquarters: {
    "WR-118412E": {
      type: "ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE",
      readings: [
        {
          timestamp: Time.now.to_i,
          value: 128013.0
        }
      ]
    }
  }
}.to_json

# Prepare request
http = Net::HTTP.new(uri.host,uri.port)
req = Net::HTTP::Post.new(uri.path, initheader = headers)
req.body = data

# Execute
begin
  res = http.request(req)
rescue Exception => e
  print "Error: " + e
else
  # Collect headers
  res_headers = []
  res.each_header do |k, v|
    res_headers << "  #{k}: #{v}"
  end

  puts "Status: " + res.code
  puts "Headers:", res_headers
  puts "Body: " + res.body
end
