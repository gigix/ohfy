RAILS_ROOT = File.join(File.dirname(__FILE__), "rails_root")
ANDROID_ROOT = File.join(File.dirname(__FILE__), "android_root")

def exec(cmd)
  puts "[Exec] #{cmd}"
  raise "Execution failed!" unless system cmd
end

task :default => ["e2e:test"]

namespace :e2e do
  task :test do
    cd RAILS_ROOT do
      exec "rake"
      exec "rake db:reset db:test:load server:restart"
    end
    
    cd ANDROID_ROOT do
      exec "rake"
    end
    
    cd RAILS_ROOT do
      exec "rake server:stop"
    end
  end
end