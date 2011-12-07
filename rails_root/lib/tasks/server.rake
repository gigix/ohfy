namespace :server do
  desc "Starts the rails server if it is not already running"
  task :start do
    `rails s -d -etest`
  end

  desc "Stops the rails server"
  task :stop do
    target_row = `ps aux|grep rails`.split("\n").map(&:strip).find{|row| row.include?("ruby script/rails")}
    begin
      pid = target_row.match(/^\w+\s+(\d+)\s+/)[1]
      `kill -9 #{pid}`
    rescue
    end
  end

  desc "Restarts the rails server"
  task :restart => ["server:stop", "server:start"]
end
