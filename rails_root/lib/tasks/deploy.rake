require 'erb'

namespace :config do
  task :generate => [:environment] do
    cd File.join(Rails.root, "config", "oauth") do
      template = ERB.new(File.open("sina.yml.erb"){|f| f.read})
      File.open("sina.yml", "w"){|f| f.write template.result}
    end
  end
end