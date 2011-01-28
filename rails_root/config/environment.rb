# Load the rails application
ENV['GEM_PATH'] = File.expand_path('~/.gems') + ':/usr/lib/ruby/gems/1.8'

# Load the rails application
require File.expand_path('../application', __FILE__)

# Initialize the rails application
Ohfy::Application.initialize!
