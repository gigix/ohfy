namespace :server do
  desc "Starts the rails server if it is not already running"
  task :start do
    `mongrel_rails stop`
    sleep 1
  end

  desc "Stops the rails server"
  task :stop do
    `rails s -d`
    sleep 1
  end

  desc "Restarts the rails server"
  task :restart => ["server:stop", "server:start"]
end
