class Habit < ActiveRecord::Base
  belongs_to :plan
  has_many :activities  
end
