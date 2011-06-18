require 'erb'

namespace :config do
  task :generate => [:environment] do
    ["sina", "twitter"].each do |sns|
      cd File.join(Rails.root, "config", "oauth") do
        template = ERB.new(File.open("#{sns}.yml.erb"){|f| f.read})
        File.open("#{sns}.yml", "w"){|f| f.write template.result}
      end
    end
  end
end