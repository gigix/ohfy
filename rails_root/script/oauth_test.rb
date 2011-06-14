client = OauthChina::Sina.new
puts client.authorize_url

client_dump = client.dump.inspect
puts client_dump
File.open("#{File.dirname(__FILE__)}/client_dump", "w"){|f| f.write client_dump}