client_dump = File.open("#{File.dirname(__FILE__)}/client_dump"){|f| f.read}
dump = eval(client_dump)
p dump
client = OauthChina::Sina.load(dump)
client.authorize(:oauth_verifier => ARGV.first.to_i)