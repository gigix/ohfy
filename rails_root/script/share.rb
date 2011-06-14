User.find(:all).each do |user|
  begin 
    user.share_to_sina(Date.today.to_s(:db))
  rescue => e
    puts e.backtrace.join("\n")
  end
end