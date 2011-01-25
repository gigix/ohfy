class Activity < ActiveRecord::Base
  belongs_to :execution
  belongs_to :habit
end
