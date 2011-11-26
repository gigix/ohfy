namespace :server do
  desc "Starts the rails server if it is not already running"
  task :start do
    `rails s -d -etest`
    sleep 1
  end

  desc "Stops the rails server"
  task :stop do
    # `killall -9 ruby` #TODO: not a best solution...
    sleep 1
  end

  desc "Restarts the rails server"
  task :restart => ["server:stop", "server:start"]
end
