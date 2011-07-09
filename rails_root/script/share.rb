User.find(:all).each do |user|
  begin 
    user.share_to_sina(Date.today.to_s(:db))
  rescue => e
    puts "Error happens when dealing user ##{user.id} :"
    puts e.message
    puts e.backtrace.join("\n")
  end
end